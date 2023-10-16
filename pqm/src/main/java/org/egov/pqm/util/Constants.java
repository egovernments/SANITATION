package org.egov.pqm.util;

import org.springframework.stereotype.Component;

@Component
public class Constants {

  //MDMS constants
  public static final String MODULE_NAME = "TQM";

  public static final String  MASTER_NAME_TESTING_STANDARD = "TestingStandard";

  public static final String  MASTER_NAME_QUALITY_CRITERIA = "PQM.QualityCriteria";

  public static final String MASTER_NAME_BENCHMARK_RULES = "BenchmarkRule";

	public static final String FSTPO_EMPLOYEE = "FSM_EMP_FSTPO";

	public static final String PQM_MODULE_NAME = "PQM";

	public static final String PQM_BUSINESS_SERVICE = "PQM";

  //Benchmark Rule Constants

  public static final String GREATER_THAN = "GTR.Greater Than";

  public static final String LESS_THAN = "LST.Less Than";

  public static final String BETWEEN = "BTW.Between";

  public static final String OUTSIDE_RANGE = "OSD.Outside Range";

  public static final String EQUALS = "EQ.Equals";

  public static final String NOT_EQUAL = "NEQ.Not Equal";

  public static final String GREATER_THAN_EQUAL_TO = "GTROREQ.Greater than or Equal to";

  public static final String LESS_THAN_EQUAL_TO = "LSTOREQ.Less than or Equal to";

	public static final String TENANT_ID_KEY = "tenantId";

  // Workflow Constants

	public static final String BUSINESS_SERVICE_KEY = "businessService";

	public static final String ACTION_KEY = "action";

	public static final String COMMENT_KEY = "comment";

	public static final String RATING = "rating";

	public static final String MODULE_NAME_KEY = "moduleName";

	public static final String BUSINESS_ID_KEY = "businessId";

	public static final String DOCUMENTS_KEY = "documents";

	public static final String ASSIGNEE_KEY = "assignes";

	public static final String MODULE_NAME_VALUE = "pqm";

	public static final String UUID_KEY = "uuid";

	public static final String WORKFLOW_REQUEST_ARRAY_KEY = "ProcessInstances";

	public static final String REQUEST_INFO_KEY = "RequestInfo";

	public static final String PROCESS_INSTANCES_JOSN_KEY = "$.ProcessInstances";

	public static final String BUSINESS_ID_JOSN_KEY = "$.businessId";

	public static final String STATUS_JSON_KEY = "$.state.applicationStatus";

  //Validation MDMS Constants
  public static final String PQM_SCHEMA_CODE_CRITERIA = "PQM.QualityCriteria";
  public static final String PQM_SCHEMA_CODE_PLANT = "PQM.Plant";
  public static final String PQM_SCHEMA_CODE_PROCESS = "PQM.Process";
  public static final String PQM_SCHEMA_CODE_STAGE = "PQM.Stage";
  public static final String PQM_SCHEMA_CODE_MATERIAL = "PQM.Material";
  public static final String WF_ACTION_SCHEDULE = "SCHEDULE";

}
