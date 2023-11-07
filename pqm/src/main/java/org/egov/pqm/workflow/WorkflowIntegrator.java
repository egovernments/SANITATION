package org.egov.pqm.workflow;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pqm.util.Constants;
import org.egov.pqm.util.ErrorConstants;
import org.egov.pqm.web.model.Test;
import org.egov.pqm.web.model.TestRequest;
import org.egov.pqm.config.ServiceConfiguration;
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

import static org.egov.pqm.util.Constants.*;

@Service
@Slf4j
public class WorkflowIntegrator {

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
		obj.put(BUSINESS_ID_KEY, test.getTestId());
		obj.put(TENANT_ID_KEY, wfTenantId);

		obj.put(BUSINESS_SERVICE_KEY, Constants.PQM_BUSINESS_SERVICE);

		obj.put(MODULE_NAME_KEY, MODULE_NAME_VALUE);
		obj.put(ACTION_KEY, test.getWorkflow().getAction());
		obj.put(COMMENT_KEY, test.getWorkflow().getComments());
		obj.put(RATING, test.getWorkflow().getRating());

		if (!CollectionUtils.isEmpty(test.getWorkflow().getAssignes())) {
			List<Map<String, String>> uuidmaps = new LinkedList<>();
			test.getWorkflow().getAssignes().forEach(assignee -> {
				Map<String, String> uuidMap = new HashMap<>();
				uuidMap.put(UUID_KEY, assignee);
				uuidmaps.add(uuidMap);
			});
			obj.put(ASSIGNEE_KEY, uuidmaps);
		}

		obj.put(DOCUMENTS_KEY, test.getWorkflow().getVerificationDocuments());
		array.add(obj);
		JSONObject workFlowRequest = new JSONObject();
		workFlowRequest.put(REQUEST_INFO_KEY, testRequest.getRequestInfo());
		workFlowRequest.put(WORKFLOW_REQUEST_ARRAY_KEY, array);
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
		List<Map<String, Object>> responseArray = responseContext.read(PROCESS_INSTANCES_JOSN_KEY);
		Map<String, String> idStatusMap = new HashMap<>();
		responseArray.forEach(object -> {

			DocumentContext instanceContext = JsonPath.parse(object);
			idStatusMap.put(instanceContext.read(BUSINESS_ID_JOSN_KEY), instanceContext.read(STATUS_JSON_KEY));
		});
		// setting the status back to pqm object from wf response
		test.setWfStatus(idStatusMap.get(test.getTestId()));

	}


}