package org.egov.vendor.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;

import org.egov.common.models.core.Role;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.egov.vendor.config.VendorConfiguration;
import org.egov.vendor.repository.ServiceRequestRepository;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.egov.common.models.individual.*;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.egov.vendor.util.VendorConstants.*;

@Component
@Slf4j
public class UserIndividualMigrationUtil {

    @Autowired
    private VendorConfiguration config;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;


    @Transactional
    public void migrate(RequestInfo requestInfo) {
        log.info("Starting migration");


        String driverDetailsQuery = "SELECT * from eg_driver;";
        List<Map<String,Object>> driverDetails = jdbcTemplate.queryForList(driverDetailsQuery);

        log.info("Fetched Driver details");
        for (Map<String,Object> driver : driverDetails) {

            String driverId = (String) driver.get("id");
            String owner_id = (String) driver.get("owner_id");
            String licenseNumber = (String) driver.get("licensenumber");
            String tenant_id = (String) driver.get("tenantid");
            String createdBy = (String) driver.get("createdBy");
            String lastModifiedBy = (String) driver.get("lastModifiedBy");
            Long createdTime = (Long) driver.get("createdTime");
            Long lastModifiedTime = (Long) driver.get("lastModifiedTime");

            //fetching user details for a driverId
            String userDetailsQuery =  "SELECT userdata.title, userdata.salutation, userdata.dob, userdata.locale, userdata.username, userdata" +
                    ".password, userdata.pwdexpirydate,  userdata.mobilenumber, userdata.altcontactnumber, userdata.emailid, userdata.createddate, userdata" +
                    ".lastmodifieddate,  userdata.createdby, userdata.lastmodifiedby, userdata.active, userdata.name, userdata.gender, userdata.pan, userdata.aadhaarnumber, userdata" +
                    ".type,  userdata.version, userdata.guardian, userdata.guardianrelation, userdata.signature, userdata.accountlocked, userdata.accountlockeddate, userdata" +
                    ".bloodgroup, userdata.photo, userdata.identificationmark,  userdata.tenantid, userdata.id, userdata.uuid, userdata.alternatemobilenumber, ur.role_code as role_code, ur.role_tenantid as role_tenantid \n" +
                    "\tFROM eg_user userdata LEFT OUTER JOIN eg_userrole_v1 ur ON userdata.id = ur.user_id AND userdata.tenantid = ur.user_tenantid WHERE userdata.uuid = '"+owner_id+"';";

            List<Map<String,Object>> userDetails = jdbcTemplate.queryForList(userDetailsQuery);

            log.info("Fetched user details");
            if (userDetails == null || userDetails.isEmpty()) {
                log.info("Userdetails not found for owner Id :: "+ owner_id);
                continue;
            }

            Map<String, Object> userDetail = userDetails.get(0);
            Map<String, String> decrypt = new HashMap<>();

            String userName = (String) userDetail.get("username");
            String mobileNumber = (String) userDetail.get("mobilenumber");
            String name = (String) userDetail.get("name");
            Integer numericGender = (Integer) userDetail.get("gender");
            Timestamp sqlTimestamp = (Timestamp) userDetail.get("dob");
            Date javaDate = new Date(sqlTimestamp.getDate());

            //validating whether individual already exists with the mobile number
            String individualSearchQuery = "Select * from individual where userid = '"+owner_id+"';";
            List<Map<String,Object>> existingindividualList  = jdbcTemplate.queryForList(individualSearchQuery);

            if(!existingindividualList.isEmpty())
            {
                log.error("Individual already exists with the ownerId -> "+ owner_id);
                continue;
            }

            //decrypting encrypted fields
            decrypt.put("username", userName);
            decrypt.put("mobilenumber", mobileNumber);
            decrypt.put("name", name);
            log.info("Calling for decryption");
            Map<String, String> decryptedValues = encryptionDecryptionUtil(decrypt, false);

            String decryptedName = decryptedValues.get("name");
            String decryptedMobileNumber = decryptedValues.get("mobilenumber");
            log.info("decryptedMobileNumber -> "+ decryptedMobileNumber);

            Individual individual = Individual.builder().tenantId(tenant_id).name(Name.builder().givenName(decryptedName).build())
                    .mobileNumber(decryptedMobileNumber).dateOfBirth(javaDate).gender(getGender(numericGender))
                    .userId(owner_id).isSystemUser(Boolean.TRUE).build();

            addDriverRelatedSkills(individual);
            addDriverRelatedAdditionalFields(individual);
            addDriverRelatedUserDetails(individual, decryptedMobileNumber);

            if(licenseNumber!=null && !licenseNumber.isEmpty())
                addDriverRelatedIdentifiers(individual,licenseNumber);

            IndividualRequest individualRequest = createIndividual(new IndividualRequest(requestInfo, individual));
            log.info("Successfully created individual with Individual Id = "+individualRequest.getIndividual().getIndividualId());

            try {
                String vendorIDQuery = "SELECT vendor_id FROM eg_vendor_driver WHERE driver_id = ?";
                String vendorId =  jdbcTemplate.queryForObject(vendorIDQuery, String.class, driverId);

                //insert into vendor-sanitation worker mapping table
                Individual createdIndividual = individualRequest.getIndividual();
                String insertQuery = "INSERT INTO eg_vendor_sanitation_worker (id, tenantid, vendor_id, individual_id, vendor_sw_status, createdby, lastmodifiedby, createdtime, lastmodifiedtime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(insertQuery, UUID.randomUUID().toString(), createdIndividual.getTenantId(), vendorId, createdIndividual.getIndividualId(),  "ACTIVE", createdBy, lastModifiedBy, createdTime, lastModifiedTime);
            } catch (Exception e) {
               log.info("Vendor-Driver mapping not present for driverId "+driverId);
            }

        }
        log.info("Ending migration....");
    }

    private Map<String, String> encryptionDecryptionUtil(Map<String, String> encryptionDecryptionMap, Boolean isEncryption) {
        JsonNode request = null;
        StringBuilder uri = new StringBuilder();
        Map<String,Object> encryptionRequestMap = new HashMap<>();
        Map<String, List<Object>> requestMap = new HashMap<>();

        if (isEncryption == true){
            log.info("Encrypting mobile number");
            uri.append(config.getEncryptionHost()).append(config.getEncryptionEndpoint());
            Map<String,String> valueMap = new HashMap<>();
            valueMap.put("contact_mobile_number",encryptionDecryptionMap.get("contact_mobile_number"));
            encryptionRequestMap.put("tenantId",encryptionDecryptionMap.get("tenant_id"));
            encryptionRequestMap.put("type","Normal");
            encryptionRequestMap.put("value",valueMap);

            requestMap.put("encryptionRequests",Collections.singletonList(encryptionRequestMap));

        }else {
            log.info("Decrypting Encrypted values");
            uri.append(config.getEncryptionHost()).append(config.getDecryptionEndpoint());
            request = mapper.createObjectNode()
                    .put("mobilenumber",encryptionDecryptionMap.get("mobilenumber"))
                    .put("name",encryptionDecryptionMap.get("name"))
                    .put("emailid",encryptionDecryptionMap.get("emailid"))
                    .put("username",encryptionDecryptionMap.get("username"));
            requestMap = mapper.convertValue(request,Map.class);
        }
        Object response = serviceRequestRepository.fetchResult(uri, requestMap);
        log.info("Got response from encryption service");
        JsonNode responseMap = mapper.valueToTree(response);
        JsonNode encryptedOrDecryptedValue = null;
        if (responseMap.isArray()) {
            encryptedOrDecryptedValue = responseMap.get(0);
        }else {
            encryptedOrDecryptedValue = responseMap;
        }
        Map<String,String> encryptedOrDecryptedMap = mapper.convertValue(encryptedOrDecryptedValue, Map.class);
        log.info("Successfully mapped encryption service");
        return encryptedOrDecryptedMap;
    }

    /**
     * creates individual
     * @param individualRequest
     */
    public IndividualRequest createIndividual(IndividualRequest individualRequest)
    {
        StringBuilder uri = new StringBuilder(config.getIndividualHost() + config.getIndividualCreateEndpoint());
        IndividualRequest individual=null;
        try {
            Object resp = serviceRequestRepository.fetchResult(uri, individualRequest);
            individual = mapper.convertValue(resp,IndividualRequest.class);
        }
        catch (Exception e)
        {
            throw new CustomException("UNABLE TO CREATE INDIVIUAL", " Unable to create individual with driverid "+String.format("{%s}", individualRequest.getIndividual().getUserId()));
        }

        return individual;
    }


    /**
     * adds driver related skills to individual object
     * @param individual
     */
    private void addDriverRelatedSkills(Individual individual)
    {
        Skill skill = Skill.builder().type(SKILL_DRIVER).level(SKILL_LEVEL_UNSKILLED).build();
        individual.addSkillsItem(skill);
    }

    /**
     * adds driver related additional fields to individual object
     * @param individual
     */
    private void addDriverRelatedAdditionalFields(Individual individual)
    {
        List<Field> fieldList = new ArrayList<>();
        fieldList.add(Field.builder().key(FUNCTIONAL_ROLE).value(FUNCTIONAL_ROLE_DRIVER).build());
        AdditionalFields additionalFields = AdditionalFields.builder().fields(fieldList).build();
        individual.setAdditionalFields(additionalFields);
    }

    /**
     * adds driver related user details to individual object
     * @param individual
     */
    private void addDriverRelatedUserDetails(Individual individual, String username)
    {
        String tenantId = individual.getTenantId();

        Role sanitationWorkerRole = Role.builder().code(SYSTEM_ROLE_CODE_SANITATION_WORKER).name(SYSTEM_ROLE_NAME_SANITATION_WORKER).tenantId(tenantId).build();
        Role driverRole = Role.builder().code(SYSTEM_ROLE_CODE_FSM_DRIVER).name(SYSTEM_ROLE_NAME_FSM_DRIVER).tenantId(tenantId).build();
        List<Role> roleList = new ArrayList<>(Arrays.asList(sanitationWorkerRole, driverRole));

        UserDetails userDetails = UserDetails.builder().username(username).roles(roleList)
                .tenantId(tenantId).build();
        individual.setUserDetails(userDetails);

    }

    /**
     * adds driver related identifiers to individual object
     * @param individual
     */
    private void addDriverRelatedIdentifiers(Individual individual, String licenseNumber) {

        Identifier licenseNumberIdentifier = Identifier.builder().identifierType(DRIVING_LICENSE_NUMBER_IDENTIFIER).identifierId(licenseNumber).build();
        List<Identifier> identifierList = new ArrayList<>(Collections.singletonList(licenseNumberIdentifier));
        individual.setIdentifiers(identifierList);
    }

//    "identifiers": [
//    {
//        "identifierType": "DRIVING_LICENSE_NUMBER",
//            "identifierId": "12312312312333"
//    }
    /**
     *
     * @param numericGender
     * @return gender mapped to the specific number
     */
    private static Gender getGender(int numericGender) {
        Gender gender;

        switch (numericGender) {
            case 0:
                gender = Gender.FEMALE;
                break;
            case 1:
                gender = Gender.MALE;
                break;
            case 2:
                gender = Gender.OTHER;
                break;
            case 3:
                gender = Gender.TRANSGENDER;
                break;
            default:
                gender = Gender.OTHER;
                break;
        }

        return gender;
    }
//
//    private void validateWhetherIndividualExists()
//    {
//        fetch vendor for driver id
//            look whether indiv
//    }

}
