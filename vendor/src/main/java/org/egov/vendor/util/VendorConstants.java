package org.egov.vendor.util;

import org.springframework.stereotype.Component;

@Component
public class VendorConstants {

	private VendorConstants() {

	}

	public static final String VENDOR_MODULE_CODE = "VENDOR";

	// mdms master names
	public static final String SUCTION_TYPE = "SuctionType";
	public static final String VEHICLE_TYPE = "Type";
	public static final String MODEL = "Model";

	public static final String EMPLOYEE = "EMPLOYEE";
	public static final String VENDOR = "VENDOR";

	public static final String EMP_STATUS = "EMPLOYED";

	public static final String EMP_TYPE = "CONTRACT";

	public static final String JURIDICTION_HIERARAHY = "ADMIN";

	public static final String JURIDICTION_BOUNDARYTYPE = "City";

	public static final String ASSIGNMENT_DEPT = "DEPT_3";

	public static final String ASSIGNMENT_DESGNATION = "DESIG_17";

	public static final String COULD_NOT_CREATE_VEHICLE = "COULD_NOT_CREATE_VEHICLE";

	public static final String CITIZEN = "CITIZEN";

	public static final String AGENCY_TYPE = "AGENCY_TYPE";

	public static final String PAYMENT_PREFERENCE = "PAYMENT_PREFERENCE";

	public static final String VENDOR_JSONPATH_CODE = "$.MdmsRes.Vendor";

	public static final String VENDOR_AGENCY_TYPE = "AgencyType";

	public static final String SW_FUNCTIONAL_ROLES = "SanitationWorkerFunctionalRoles";

	public static final String SW_SKILLS = "SanitationWorkerSkills";

	public static final String VENDOR_PAYMENT_PREFERENCE = "PaymentPreference";

	public static final String VENDOR_MODULE = "Vendor";
	public static final String FSM_MODULE = "FSM";

	public static final String UPDATE_ERROR = "Update Error";

	public static final String UPDATE_VEHICLE_ERROR = "UPDATE_VEHICLE_ERROR";
	public static final String CREATED_DATE = "createdDate";
	public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
	public static final String DOB = "dob";
	public static final String ACTIVE = "ACTIVE";
	public static final String ILLEGAL_ARGUMENT_EXCEPTION = "IllegalArgumentException";
	public static final String TENANT_ID_MANDATORY = "TenantId is mandatory in search";
	public static final String DISABLED = "DISABLED";
	public static final String FSM_DRIVER = "FSM_DRIVER";
	public static final String DuplicateUserNameException = "DuplicateUserNameException";
	public static final String ROLE_FSM_DSO = "FSM_DSO";

	//Skills
	public static final String SKILL_DRIVER = "DRIVER";
	public static final String SKILL_LEVEL_UNSKILLED = "Unskilled";

	//Functional Roles

	public static final String FUNCTIONAL_ROLE = "FUNCTIONAL_ROLE";
	public static final String FUNCTIONAL_ROLE_DRIVER = "DRIVER";

	//System Roles
	public static final String SYSTEM_ROLE_CODE_SANITATION_WORKER = "SANITATION_WORKER";
	public static final String SYSTEM_ROLE_NAME_SANITATION_WORKER = "Sanitation Worker";
	public static final String SYSTEM_ROLE_CODE_FSM_DRIVER = "FSM_DRIVER";
	public static final String SYSTEM_ROLE_NAME_FSM_DRIVER = "FSM_DRIVER";

	//Identifiers

	public static final String DRIVING_LICENSE_NUMBER_IDENTIFIER = "DRIVING_LICENSE_NUMBER";

}
