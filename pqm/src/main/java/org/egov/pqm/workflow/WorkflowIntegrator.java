package org.egov.pqm.workflow;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.config.ServiceConfiguration;
import org.egov.pqm.web.model.TestResultStatus;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WorkflowIntegrator {

	private static final String TENANTIDKEY = "tenantId";

	private static final String BUSINESSSERVICEKEY = "businessService";

	private static final String ACTIONKEY = "action";

	private static final String COMMENTKEY = "comment";

	private static final String RATING = "rating";

	private static final String MODULENAMEKEY = "moduleName";

	private static final String BUSINESSIDKEY = "businessId";

	private static final String DOCUMENTSKEY = "documents";

	private static final String ASSIGNEEKEY = "assignes";

	private static final String MODULENAMEVALUE = "pqm";

	private static final String UUIDKEY = "uuid";

	private static final String WORKFLOWREQUESTARRAYKEY = "ProcessInstances";

	private static final String REQUESTINFOKEY = "RequestInfo";

	private static final String PROCESSINSTANCESJOSNKEY = "$.ProcessInstances";

	private static final String BUSINESSIDJOSNKEY = "$.businessId";

	private static final String STATUSJSONKEY = "$.state.applicationStatus";

	private RestTemplate rest;

	private ServiceConfiguration config;

	@Autowired
	public WorkflowIntegrator(RestTemplate rest, ServiceConfiguration config) {
		this.rest = rest;
		this.config = config;
	}

	/**
	 * Method to integrate with workflow
	 *
	 * takes the pqm request as parameter constructs the work-flow request
	 *
	 * and sets the resultant status from wf-response back to pqm object
	 *
	 * @param testRequest
	 */
	public void callWorkFlow(TestRequest testRequest) {
		String wfTenantId = testRequest.getTests().get(0).getTenantId();
		JSONArray array = new JSONArray();
		Test test = testRequest.getTests().get(0);
		JSONObject obj = new JSONObject();
		obj.put(BUSINESSIDKEY, test.getId());
		obj.put(TENANTIDKEY, wfTenantId);

		obj.put(BUSINESSSERVICEKEY, Constants.PQM_BUSINESS_SERVICE);

		obj.put(MODULENAMEKEY, MODULENAMEVALUE);
		obj.put(ACTIONKEY, testRequest.getWorkflow().getAction());
		obj.put(COMMENTKEY, testRequest.getWorkflow().getComments());
		obj.put(RATING, testRequest.getWorkflow().getRating());

		if (!CollectionUtils.isEmpty(testRequest.getWorkflow().getAssignes())) {
			List<Map<String, String>> uuidmaps = new LinkedList<>();
			testRequest.getWorkflow().getAssignes().forEach(assignee -> {
				Map<String, String> uuidMap = new HashMap<>();
				uuidMap.put(UUIDKEY, assignee);
				uuidmaps.add(uuidMap);
			});
			obj.put(ASSIGNEEKEY, uuidmaps);
		}

		obj.put(DOCUMENTSKEY, testRequest.getWorkflow().getVerificationDocuments());
		array.add(obj);
		JSONObject workFlowRequest = new JSONObject();
		workFlowRequest.put(REQUESTINFOKEY, testRequest.getRequestInfo());
		workFlowRequest.put(WORKFLOWREQUESTARRAYKEY, array);
		String response = null;
		try {
			response = rest.postForObject(config.getWfHost().concat(config.getWfTransitionPath()), workFlowRequest,
					String.class);
		} catch (HttpClientErrorException e) {

			/*
			 * extracting message from client error exception
			 */
			DocumentContext responseContext = JsonPath.parse(e.getResponseBodyAsString());
			List<Object> errros = null;
			try {
				errros = responseContext.read("$.Errors");
			} catch (PathNotFoundException pnfe) {
				log.error(ErrorConstants.CREATE_ERROR,
						" Unable to read the json path in error object : " + pnfe.getMessage());
				throw new CustomException(ErrorConstants.CREATE_ERROR,
						" Unable to read the json path in error object : " + pnfe.getMessage());
			}
			throw new CustomException(ErrorConstants.CREATE_ERROR, errros.toString());
		} catch (Exception e) {
			throw new CustomException(ErrorConstants.CREATE_ERROR,
					" Exception occured while integrating with workflow : " + e.getMessage());
		}

		/*
		 * on success result from work-flow read the data and set the status back to pqm
		 * object
		 */
		DocumentContext responseContext = JsonPath.parse(response);
		List<Map<String, Object>> responseArray = responseContext.read(PROCESSINSTANCESJOSNKEY);
		Map<String, String> idStatusMap = new HashMap<>();
		responseArray.forEach(object -> {

			DocumentContext instanceContext = JsonPath.parse(object);
			idStatusMap.put(instanceContext.read(BUSINESSIDJOSNKEY), instanceContext.read(STATUSJSONKEY));
		});
		// setting the status back to pqm object from wf response
		test.setStatus(TestResultStatus.valueOf(idStatusMap.get(test.getId())));

	}


}