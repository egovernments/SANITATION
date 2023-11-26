import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Toast, Header, FormComposerV2, Loader } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom";
import WorkerConfig from "../../configs/WorkerConfig";
import { useQueryClient } from "react-query";

// IND-2023-11-24-010875
const EditWorker = ({ parentUrl, heading }) => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const stateId = Digit.ULBService.getStateId();
  const [showToast, setShowToast] = useState(null);
  const history = useHistory();
  const queryClient = useQueryClient();
  const { t } = useTranslation();
  const [canSubmit, setSubmitValve] = useState(false);
  const [workerinfo, setWorkerinfo] = useState(null);
  const [defaultValues, setDefaultValues] = useState(null);
  const [skillsOption, setSkillsOption] = useState([]);
  const [employer, setEmployer] = useState([]);
  const [Config, setConfig] = useState(WorkerConfig({ t }));
  const searchParams = new URLSearchParams(location.search);
  const id = searchParams.get("id");
  const [checkRoleField, setCheckRoleField] = useState(false);

  const { isLoading: ismdms, data: mdmsOptions } = Digit.Hooks.useCustomMDMS(
    stateId,
    "FSM",
    [
      {
        name: "SanitationWorkerSkills",
      },
      {
        name: "SanitationWorkerEmployer",
      },
      {
        name: "SanitationWorkerEmploymentType",
      },
      {
        name: "SanitationWorkerFunctionalRoles",
      },
    ],
    {
      select: (data) => {
        return data?.FSM;
      },
    }
  );

  const { isLoading: isPlantUserLoading, data: plantUserData } = Digit.Hooks.useCustomAPIHook({
    url: "/pqm-service/plant/user/v1/_search",
    params: {},
    body: {
      plantUserSearchCriteria: {
        tenantId: tenantId,
        individualIds: [id],
      },
      pagination: {},
    },
    changeQueryName: "plantUser",
    staletime: 0,
  });

  useEffect(() => {
    setSkillsOption(mdmsOptions?.SanitationWorkerSkills);
    setEmployer(mdmsOptions?.SanitationWorkerEmployer);
  }, [mdmsOptions, ismdms]);

  useEffect(() => {
    setConfig(WorkerConfig({ t, skillsOption, employer }));
  }, [skillsOption, employer]);

  const { isLoading: isLoading, isError: vendorCreateError, data: updateResponse, error: updateError, mutate } = Digit.Hooks.fsm.useWorkerUpdate(tenantId);

  const { data: workerData, isLoading: WorkerLoading } = Digit.Hooks.fsm.useWorkerSearch({
    tenantId,
    params: {
      offset: 0,
      limit: 100,
    },
    details: {
      Individual: {
        individualId: id,
      },
    },
  });

  const {
    isLoading: isPlantUpdateLoading,
    isError: isPlantUserError,
    data: plantUserResponse,
    error: PlantUserError,
    mutate: PlantUserMutate,
  } = Digit.Hooks.fsm.usePlantUserUpdate(tenantId);

  const {
    isLoading: isVendorUpdateLoading,
    isError: isvendorUpdateError,
    data: vendorUpdateResponse,
    error: vendorUpdateError,
    mutate: vendorMutate,
  } = Digit.Hooks.fsm.useVendorUpdate(tenantId);

  function transformData(rdata) {
    const data = rdata?.additionalFields?.fields;
    const functionalRoleCount = parseInt(data?.find((item) => item?.key === "FUNCTIONAL_ROLE_COUNT")?.value, 10) || null;
    const resultArray = [];
    const allowedRoles = mdmsOptions?.SanitationWorkerFunctionalRoles;
    const resproles = rdata?.userDetails?.roles;
    if (functionalRoleCount) {
      for (let i = 1; i <= functionalRoleCount; i++) {
        const functionalRoleKey = `FUNCTIONAL_ROLE_${i}`;
        const functionalRoleValue = data.find((item) => item.key === functionalRoleKey).value;

        const transformedData = {
          emp_Type: {
            code: data.find((item) => item.key === `EMPLOYMENT_TYPE_${i}`).value,
          },
          fn_role: {
            code: functionalRoleValue,
          },
        };

        if (functionalRoleValue === "DRIVER") {
          transformedData.licenseNo = rdata?.identifiers?.[0]?.identifierId;
        }

        if (functionalRoleValue === "PLANT_OPERATOR") {
          transformedData.plant = {
            ...plantUserData?.plantUsers?.[0],
            name: plantUserData?.plantUsers?.[0]?.plantCode,
            i18nKey: `PQM_PLANT_${plantUserData?.plantUsers?.[0]?.plantCode}`,
          };
        }

        const tempRoles = allowedRoles?.find((i) => i.code === functionalRoleValue)?.allowedSystemRoles;
        const filterCodes = resproles.map((item) => item.code);
        const filteredArray = tempRoles.filter((item) => filterCodes.includes(item.code));
        transformedData.sys_role = filteredArray;
        resultArray.push(transformedData);
      }
    }

    return resultArray;
  }

  useEffect(() => {
    if (workerData && workerData?.Individual) {
      const workerDetails = workerData?.Individual?.[0];
      setWorkerinfo(workerDetails);
      const values = {
        SelectEmployeePhoneNumber: {
          mobileNumber: workerDetails?.mobileNumber,
        },
        name: workerDetails?.name?.givenName,
        selectGender: {
          code: workerDetails?.gender,
          active: true,
          i18nKey: `COMMON_GENDER_${workerDetails?.gender}`,
        },
        dob: workerDetails?.dateOfBirth.split("/").reverse().join("-"),
        pincode: workerDetails?.address?.[0]?.pincode,
        address: {
          city: {
            code: workerDetails?.address?.[0]?.city,
          },
          locality: { ...workerDetails?.address?.[0]?.locality },
        },
        street: workerDetails?.address?.[0]?.street,
        doorNo: workerDetails?.address?.[0]?.doorNo,
        landmark: workerDetails?.address?.[0]?.landmark,
        skills: workerDetails?.skills?.map((obj) => ({ ...obj, code: obj.type })),
        employementDetails: {
          employer: { name: workerDetails?.additionalFields?.fields?.find((i) => i.key === "EMPLOYER")?.value },
          vendor: {
            id: "ea138945-f35b-42a3-96af-9ded096fb809",
            tenantId: "pg.citya",
            name: "Raj",
            address: {
              tenantId: "pg.citya",
              doorNo: "",
              plotNo: "",
              id: "894f60f2-0d27-4f24-b1ec-a0017920b51a",
              landmark: "",
              city: "CityA",
              district: "CityA",
              region: "CityA",
              state: null,
              country: "in",
              pincode: "",
              additionalDetails: '{"description": ""}',
              buildingName: "",
              street: "",
              locality: {
                code: "SUN01",
                name: "Ajit Nagar - Area1",
                label: "Locality",
                latitude: "31.63089",
                longitude: "74.871552",
                children: [],
                materializedPath: null,
              },
              geoLocation: null,
              auditDetails: null,
            },
            owner: {
              id: 861,
              uuid: "31b14828-1c46-42bb-9364-8f456509c375",
              userName: "6543217890",
              password: null,
              salutation: null,
              name: "Raj",
              gender: "MALE",
              mobileNumber: "6543217890",
              emailId: "abc@egov.com",
              altContactNumber: null,
              pan: null,
              aadhaarNumber: null,
              permanentAddress: null,
              permanentCity: null,
              permanentPinCode: null,
              correspondenceCity: null,
              correspondencePinCode: null,
              correspondenceAddress: null,
              active: true,
              dob: 1700611200000,
              pwdExpiryDate: 1708715718000,
              locale: null,
              type: "CITIZEN",
              signature: null,
              accountLocked: false,
              roles: [
                {
                  id: null,
                  name: "FSM Desluding Operator",
                  code: "FSM_DSO",
                  tenantId: "pg",
                },
                {
                  id: null,
                  name: "Citizen",
                  code: "CITIZEN",
                  tenantId: "pg",
                },
              ],
              fatherOrHusbandName: "Raj",
              relationship: "OTHER",
              bloodGroup: null,
              identificationMark: null,
              photo: null,
              createdBy: "715",
              createdDate: 1700820918000,
              lastModifiedBy: "715",
              lastModifiedDate: 1700836672000,
              otpReference: null,
              tenantId: "pg",
            },
            vehicles: null,
            drivers: [
              {
                id: "f24d113e-c163-4aa1-a1b3-b54807308a40",
                tenantId: "pg.citya",
                name: "pintu",
                owner: {
                  id: 820,
                  uuid: "fc688f84-6b5c-4da5-ae69-3580d3d81fc6",
                  userName: "1111111149",
                  password: null,
                  salutation: null,
                  name: "pintu",
                  gender: "MALE",
                  mobileNumber: "1111111149",
                  emailId: "abc@egov.com",
                  altContactNumber: null,
                  pan: null,
                  aadhaarNumber: null,
                  permanentAddress: null,
                  permanentCity: null,
                  permanentPinCode: null,
                  correspondenceCity: null,
                  correspondencePinCode: null,
                  correspondenceAddress: null,
                  active: true,
                  dob: 0,
                  pwdExpiryDate: 1706927828000,
                  locale: null,
                  type: "CITIZEN",
                  signature: null,
                  accountLocked: false,
                  roles: [
                    {
                      id: null,
                      name: "FSM Driver",
                      code: "FSM_DRIVER",
                      tenantId: "pg.citya",
                    },
                    {
                      id: null,
                      name: "FSM Driver",
                      code: "FSM_DRIVER",
                      tenantId: "pg",
                    },
                  ],
                  fatherOrHusbandName: "pintu",
                  relationship: "OTHER",
                  bloodGroup: null,
                  identificationMark: null,
                  photo: null,
                  createdBy: "715",
                  createdDate: 1699013642000,
                  lastModifiedBy: "715",
                  lastModifiedDate: 1700836879000,
                  otpReference: null,
                  tenantId: "pg",
                },
                ownerId: "fc688f84-6b5c-4da5-ae69-3580d3d81fc6",
                additionalDetails: null,
                description: null,
                licenseNumber: "12345678999995",
                status: "DISABLED",
                auditDetails: {
                  createdBy: "4a747fc5-6a8c-4645-8748-fec35f1b9e17",
                  lastModifiedBy: "4a747fc5-6a8c-4645-8748-fec35f1b9e17",
                  createdTime: 1698993428899,
                  lastModifiedTime: 1700817079094,
                },
                vendorDriverStatus: "ACTIVE",
              },
            ],
            workers: [
              {
                id: "f3104f07-9b62-460d-8b9c-e16a4b6c740f",
                tenantId: "pg.citya",
                vendorId: "ea138945-f35b-42a3-96af-9ded096fb809",
                individualId: "IND-2023-11-23-010844",
                additionalDetails: null,
                auditDetails: {
                  createdBy: "4a747fc5-6a8c-4645-8748-fec35f1b9e17",
                  lastModifiedBy: "4a747fc5-6a8c-4645-8748-fec35f1b9e17",
                  createdTime: 1700801118082,
                  lastModifiedTime: 1700803699721,
                },
                vendorWorkerStatus: "ACTIVE",
              },
            ],
            additionalDetails: {
              description: "",
            },
            source: "WhatsApp",
            description: null,
            ownerId: "31b14828-1c46-42bb-9364-8f456509c375",
            agencyType: "ULB",
            paymentPreference: "post-service",
            status: "ACTIVE",
            auditDetails: {
              createdBy: "4a747fc5-6a8c-4645-8748-fec35f1b9e17",
              lastModifiedBy: "4a747fc5-6a8c-4645-8748-fec35f1b9e17",
              createdTime: 1700801118082,
              lastModifiedTime: 1700816872520,
            },
          },
        },
        AddWorkerRoles: transformData(workerDetails),
        documents: {
          img_photo: [
            [
              "SAMPLE",
              {
                file: { name: "sample" },
                fileStoreId: {
                  fileStoreId: workerDetails?.photo,
                  tenantId: tenantId,
                },
              },
            ],
          ],
        },
      };

      setDefaultValues(values);
    }
  }, [workerData, WorkerLoading, plantUserData]);

  const onFormValueChange = (setValue, formData) => {
    for (let i = 0; i < formData?.AddWorkerRoles?.length; i++) {
      let key = formData?.AddWorkerRoles[i];
      if (!(key?.emp_Type && key?.fn_role && key?.sys_role && ((key?.licenseNo && key?.fn_role?.code === "DRIVER") || (key?.fn_role?.code === "PLANT_OPERATOR" && key?.plant)))) {
        setCheckRoleField(false);
        break;
      } else {
        setCheckRoleField(true);
      }
    }

    if (
      !isNaN(formData?.SelectEmployeePhoneNumber?.mobileNumber?.length) &&
      formData?.SelectEmployeePhoneNumber?.mobileNumber?.length === 10 &&
      formData?.name &&
      formData?.selectGender &&
      formData?.dob &&
      formData?.address?.city &&
      formData?.address?.locality &&
      formData?.skills &&
      formData?.employementDetails?.employer &&
      formData?.AddWorkerRoles?.length > 0 &&
      checkRoleField
    ) {
      setSubmitValve(true);
    } else {
      setSubmitValve(false);
    }
  };

  const closeToast = () => {
    setShowToast(null);
  };

  const onSubmit = (data) => {
    const name = data?.name;
    const mobileNumber = data?.SelectEmployeePhoneNumber?.mobileNumber;
    const gender = data?.selectGender?.code;
    const dob = new Date(`${data.dob}`).getTime() || new Date(`1/1/1970`).getTime();
    const photograph = data?.documents?.img_measurement_book?.[0]?.[1]?.fileStoreId?.fileStoreId || null;
    const pincode = data?.pincode;
    const city = data?.address?.city?.name;
    const locality = data?.address?.locality?.code;
    const doorNo = data?.doorNo;
    const street = data?.street;
    const landmark = data?.landmark;
    const skills = data?.skills?.map((i) => {
      return { type: i?.name || i?.code, level: "UNSKILLED" };
    });
    const employer = data?.employementDetails?.employer?.code || data?.employementDetails?.employer?.name;
    const vendor = data?.employementDetails?.vendor;
    const roleDetails = data?.AddWorkerRoles;
    const restructuredData = [];

    roleDetails.forEach((item) => {
      const restructuredItem = {};
      restructuredItem["FUNCTIONAL_ROLE"] = item.fn_role.code;
      restructuredItem["EMPLOYMENT_TYPE"] = item.emp_Type.name;
      restructuredItem["SYSTEM_ROLE"] = item.sys_role;
      restructuredItem["PLANT"] = item?.plant;
      restructuredData.push(restructuredItem);
    });

    const driverLicenses = roleDetails?.filter((entry) => entry.fn_role && entry.fn_role.code === "DRIVER" && entry.licenseNo).map((entry) => entry.licenseNo);
    const roleDetailsArray = [];

    roleDetails.forEach((item, index) => {
      // Extracting functional role information
      const fnRoleKey = `FUNCTIONAL_ROLE_${index + 1}`;
      const fnRoleValue = item.fn_role.code;

      // Extracting employment type information
      const empTypeKey = `EMPLOYMENT_TYPE_${index + 1}`;
      const empTypeValue = item.emp_Type.code.toUpperCase();

      // Pushing the extracted information to the output array
      roleDetailsArray.push({ key: fnRoleKey, value: fnRoleValue });
      roleDetailsArray.push({ key: empTypeKey, value: empTypeValue });
    });

    // Adding the count of functional roles
    roleDetailsArray.push({ key: "FUNCTIONAL_ROLE_COUNT", value: `${roleDetails.length < 10 ? "0" : ""}${roleDetails.length.toString()}` });

    // Adding the employer information (assuming it's a constant value like "PRIVATE_VENDOR")
    roleDetailsArray.push({ key: "EMPLOYER", value: employer });

    const formData = {
      Individual: {
        ...workerinfo,
        tenantId: tenantId,
        name: {
          ...workerinfo.name,
          givenName: name,
        },
        dateOfBirth: dob,
        gender: gender,
        mobileNumber: mobileNumber,
        address: [
          {
            ...workerinfo?.address?.[0],
            tenantId: tenantId,
            pincode: pincode,
            city: tenantId,
            street: street,
            doorNo: doorNo,
            locality: {
              ...workerinfo?.address?.[0]?.locality,
              code: locality,
            },
            landmark: landmark,
            type: "PERMANENT",
          },
        ],
        identifiers:
          driverLicenses.length > 0
            ? [
                {
                  ...workerinfo?.indentifiers?.[0],
                  identifierType: "DRIVING_LICENSE_NUMBER",
                  identifierId: driverLicenses?.[0],
                },
              ]
            : null,
        skills: skills,
        photo: photograph,
        additionalFields: {
          ...workerinfo?.additionalDetails,
          // fields: restructuredData,
          fields: roleDetailsArray,
        },
        isSystemUser: false,
        userDetails: {
          ...workerinfo?.userDetails,
          username: name,
          tenantId: tenantId,
          roles: roleDetails?.map((entry) => {
            return { code: entry.sys_role.code, tenantId };
          }),
          type: roleDetails?.map((entry) => entry.sys_role.code)?.includes("citizen") ? "CITIZEN" : "EMPLOYEE",
        },
      },
    };

    mutate(formData, {
      onError: (error, variables) => {
        setShowToast({ key: "error", action: error });
        setTimeout(closeToast, 5000);
      },
      onSuccess: async (data, variables) => {
        setShowToast({ key: "success", action: "ADD_WORKER" });
        queryClient.invalidateQueries("FSM_WORKER_SEARCH");
        if (roleDetails.some((entry) => entry.plant)) {
          try {
            const PlantCode = roleDetails
              ?.map((entry) => ({
                tenantId: tenantId,
                plantCode: entry?.plant?.code,
                individualId: data?.Individual?.individualId,
                isActive: true,
              }))
              .filter((i) => i?.plantCode);
            const plantFormData = {
              plantUsers: PlantCode,
            };
            const plantresponse = await PlantUserMutate(plantFormData);
          } catch (err) {
            console.error("Plant user create", err);
            setShowToast({ key: "error", action: err });
          }
        }
        if (employer !== "CITIZEN" && vendor) {
          try {
            const vendorData = {
              vendor: {
                ...vendor,
                workers: vendor.workers
                  ? [...vendor.workers, { individualId: data?.Individual?.individualId, vendorWorkerStatus: "ACTIVE" }]
                  : [{ individualId: data?.Individual?.individualId, vendorWorkerStatus: "ACTIVE" }],
              },
            };
            const response = await vendorMutate(vendorData);
          } catch (updateError) {
            console.error("Error updating data:", updateError);
            setShowToast({ key: "error", action: updateError });
          }
        }
        setTimeout(() => {
          closeToast();
          history.push(`/${window?.contextPath}/employee/fsm/registry?selectedTabs=WORKER`);
        }, 5000);
      },
    });
  };
  const isMobile = window.Digit.Utils.browser.isMobile();

  if (!defaultValues) {
    return <Loader />;
  }
  return (
    <React.Fragment>
      <div>
        <Header>{t("FSM_REGISTRY_ADD_WORKER_HEADING")}</Header>
      </div>
      <div style={!isMobile ? { marginLeft: "-15px" } : {}}>
        <FormComposerV2
          isDisabled={!canSubmit}
          label={t("ES_COMMON_APPLICATION_SUBMIT")}
          config={Config.filter((i) => !i.hideInEmployee).map((config) => {
            return {
              ...config,
              body: config.body.filter((a) => !a.hideInEmployee),
            };
          })}
          fieldStyle={{ marginRight: 0 }}
          sectionHeaderClassName="fsm-registry"
          onSubmit={onSubmit}
          defaultValues={defaultValues}
          onFormValueChange={onFormValueChange}
          noBreakLine={true}
        />
        {showToast && (
          <Toast
            error={showToast.key === "error" ? true : false}
            label={t(showToast.key === "success" ? `ES_FSM_REGISTRY_${showToast.action}_SUCCESS` : showToast.action)}
            onClose={closeToast}
          />
        )}
      </div>
    </React.Fragment>
  );
};

export default EditWorker;
