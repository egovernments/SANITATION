package org.egov.fsm.calculator.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.fsm.calculator.config.CalculatorConfig;
import org.egov.fsm.calculator.kafka.broker.CalculatorProducer;
import org.egov.fsm.calculator.repository.BillingSlabRepository;
import org.egov.fsm.calculator.repository.querybuilder.BillingSlabQueryBuilder;
import org.egov.fsm.calculator.utils.CalculationUtils;
import org.egov.fsm.calculator.utils.CalculatorConstants;
import org.egov.fsm.calculator.web.models.BillResponse;
import org.egov.fsm.calculator.web.models.BillingSlab;
import org.egov.fsm.calculator.web.models.BillingSlab.SlumEnum;
import org.egov.fsm.calculator.web.models.BillingSlabSearchCriteria;
import org.egov.fsm.calculator.web.models.Calculation;
import org.egov.fsm.calculator.web.models.CalculationReq;
import org.egov.fsm.calculator.web.models.CalculationRes;
import org.egov.fsm.calculator.web.models.CalulationCriteria;
import org.egov.fsm.calculator.web.models.EstimatesAndSlabs;
import org.egov.fsm.calculator.web.models.FSM;
import org.egov.fsm.calculator.web.models.demand.Category;
import org.egov.fsm.calculator.web.models.demand.TaxHeadEstimate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CalculationService {

	@Autowired
	private MDMSService mdmsService;

	@Autowired
	private DemandService demandService;

	@Autowired
	private CalculatorConfig config;

	@Autowired
	private CalculationUtils utils;

	@Autowired
	private CalculatorProducer producer;

	@Autowired
	private FSMService fsmService;

	@Autowired
	private BillingSlabQueryBuilder billingSlabQueryBuilder;

	@Autowired
	private BillingSlabRepository billingSlabRepository;

	@Autowired
	private BillingService billingService;
//	@Autowired
//	private CalculatorConstants constant;

	/**
	 * Calculates tax estimates and creates demand
	 * 
	 * @param calculationReq The calculationCriteria request
	 * @return List of calculations for all applicationNumbers or tradeLicenses in
	 *         calculationReq
	 */
	public List<Calculation> calculate(CalculationReq calculationReq) {
		String tenantId = calculationReq.getCalulationCriteria().get(0).getTenantId().split("\\.")[0];
		Object mdmsData = mdmsService.mDMSCall(calculationReq, tenantId);

		List<Calculation> calculations = getCalculation(calculationReq.getRequestInfo(),
				calculationReq.getCalulationCriteria(), mdmsData);
		demandService.generateDemand(calculationReq.getRequestInfo(), calculations, mdmsData);
		CalculationRes calculationRes = CalculationRes.builder().calculations(calculations).build();
//		producer.push(config.getSaveTopic(), calculationRes);
		return calculations;
	}

	/**
	 * AdvanceBalanceCalculates tax estimates and creates demand
	 * 
	 * @param TotalTripAmount ,tenantId,RequestInfo
	 * @return cancellationAmount
	 */
	public BigDecimal advanceCalculate(BigDecimal TotalTripAmount, String tenantId, RequestInfo requestInfo) {
		Object mdmsData = mdmsService.mDMSCall(requestInfo, tenantId);
		BigDecimal advanceAmount = new BigDecimal(0);
		List<Map> advancePayment = JsonPath.read(mdmsData, CalculatorConstants.Advance_Payment_MODEL_JSON_PATH);
		for (Map advancePayemntMap : advancePayment) {
			if (advancePayemntMap.get("code").equals("FIXEDVALUE")
					&& config.getAdvancePaymentType().equals("FIXEDVALUE")) {
				log.debug((String) advancePayemntMap.get("name"));
				advanceAmount = new BigDecimal((String) advancePayemntMap.get("advanceAmount"));
				break;
			} else if (advancePayemntMap.get("code").equals("PERCENTAGEVALUE")
					&& config.getAdvancePaymentType().equals("PERCENTAGEVALUE")) {
				final BigDecimal ONE_HUNDRED = new BigDecimal(100);
				BigDecimal percentageValue = new BigDecimal((String) advancePayemntMap.get("advancePercantage"));
				advanceAmount = TotalTripAmount.multiply(percentageValue).divide(ONE_HUNDRED);
				log.debug("Total Amount:: " + advancePayemntMap.get("advanceAmount"));
				break;
			}
		}
		return advanceAmount;
	}

	/**
	 * cancellationFeeCalculates and creates demand
	 * 
	 * @param TotalTripAmount ,tenantId,RequestInfo
	 * @return cancellationAmount
	 */
	public BigDecimal cancellationAmount(BigDecimal TotalTripAmount, String tenantId, RequestInfo requestInfo) {
		Object mdmsData = mdmsService.mDMSCall(requestInfo, tenantId);
		BigDecimal cancellationAmount = new BigDecimal(0);
		List<Map> cancellationFee = JsonPath.read(mdmsData, CalculatorConstants.Cancellation_FEE_MODEL_JSON_PATH);
		for (Map cancellationFeeMap : cancellationFee) {
			if (cancellationFeeMap.get("code").equals("FIXEDVALUE")
					&& config.getCancellationFeeType().equals("FIXEDVALUE")) {
				cancellationAmount = new BigDecimal((String) cancellationFeeMap.get("cancellationAmount"));
				break;
			} else if (cancellationFeeMap.get("code").equals("PERCENTAGEVALUE")
					&& config.getCancellationFeeType().equals("PERCENTAGEVALUE")) {
				final BigDecimal ONE_HUNDRED = new BigDecimal(100);
				BigDecimal percentageValue = new BigDecimal((String) cancellationFeeMap.get("cancellationPercentage"));
				cancellationAmount = TotalTripAmount.multiply(percentageValue).divide(ONE_HUNDRED);
				break;
			}
		}
		return cancellationAmount;
	}

	/**
	 * Calculates tax estimates
	 * 
	 * @param calculationReq The calculationCriteria request
	 * @return List of calculations for all applicationNumbers or tradeLicenses in
	 *         calculationReq
	 */
	public List<Calculation> estimate(CalculationReq calculationReq) {
		String tenantId = calculationReq.getCalulationCriteria().get(0).getTenantId().split("\\.")[0];
		Object mdmsData = mdmsService.mDMSCall(calculationReq, tenantId);

		List<Calculation> calculations = getCalculation(calculationReq.getRequestInfo(),
				calculationReq.getCalulationCriteria(), mdmsData);
		CalculationRes calculationRes = CalculationRes.builder().calculations(calculations).build();
		return calculations;
	}

	/***
	 * Calculates tax estimates
	 * 
	 * @param requestInfo The requestInfo of the calculation request
	 * @param criterias   list of CalculationCriteria containing the tradeLicense or
	 *                    applicationNumber
	 * @return List of calculations for all applicationNumbers or tradeLicenses in
	 *         criterias
	 */
	public List<Calculation> getCalculation(RequestInfo requestInfo, List<CalulationCriteria> criterias,
			Object mdmsData) {
		List<Calculation> calculations = new LinkedList<>();
		for (CalulationCriteria criteria : criterias) {
			FSM fsm;
			if (criteria.getFsm() == null && criteria.getApplicationNo() != null) {
				fsm = fsmService.getFsmApplication(requestInfo, criteria.getTenantId(), criteria.getApplicationNo());
				criteria.setFsm(fsm);
			}

			EstimatesAndSlabs estimatesAndSlabs = getTaxHeadEstimates(criteria, requestInfo, mdmsData);
			List<TaxHeadEstimate> taxHeadEstimates = estimatesAndSlabs.getEstimates();

			Calculation calculation = new Calculation();
			calculation.setFsm(criteria.getFsm());
			calculation.setTenantId(criteria.getTenantId());
			calculation.setTaxHeadEstimates(taxHeadEstimates);
			calculation.setFeeType(criteria.getFeeType());
			calculations.add(calculation);

		}
		return calculations;
	}

	/**
	 * Creates TacHeadEstimates
	 * 
	 * @param calulationCriteria CalculationCriteria containing the tradeLicense or
	 *                           applicationNumber
	 * @param requestInfo        The requestInfo of the calculation request
	 * @return TaxHeadEstimates and the billingSlabs used to calculate it
	 */
	private EstimatesAndSlabs getTaxHeadEstimates(CalulationCriteria calulationCriteria, RequestInfo requestInfo,
			Object mdmsData) {
		List<TaxHeadEstimate> estimates = new LinkedList<>();
		EstimatesAndSlabs estimatesAndSlabs = getBaseTax(calulationCriteria, requestInfo, mdmsData);
		estimates.addAll(estimatesAndSlabs.getEstimates());

		estimatesAndSlabs.setEstimates(estimates);

		return estimatesAndSlabs;
	}

	/**
	 * Calculates base tax and cretaes its taxHeadEstimate
	 * 
	 * @param calulationCriteria CalculationCriteria containing the tradeLicense or
	 *                           applicationNumber
	 * @param requestInfo        The requestInfo of the calculation request
	 * @return BaseTax taxHeadEstimate and billingSlabs used to calculate it
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private EstimatesAndSlabs getBaseTax(CalulationCriteria calulationCriteria, RequestInfo requestInfo,
			Object mdmsData) {
		FSM fsm = calulationCriteria.getFsm();
		EstimatesAndSlabs estimatesAndSlabs = new EstimatesAndSlabs();
		ArrayList<TaxHeadEstimate> estimates = new ArrayList<TaxHeadEstimate>();
		TaxHeadEstimate estimate = new TaxHeadEstimate();

		BigDecimal amount = null;
		BigDecimal advance = null;
		BigDecimal calculatedAmount = new BigDecimal(0);
		BigDecimal paidAmount = new BigDecimal(0);
		/*
		 * String capacity = getAmountForVehicleType(fsm.getVehicleType(), mdmsData); if
		 * (capacity ==null || !NumberUtils.isCreatable(capacity)) { throw new
		 * CustomException(CalculatorConstants.INVALID_CAPACITY,
		 * "Capacity is Invalid for the given vehicleType"); }
		 */

		List<Map<String, Object>> slumNameAllowed = JsonPath.read(mdmsData,
				CalculatorConstants.FSM_SLUM_OVERRIDE_ALLOWED);
		List<Map<String, Object>> tripAountAllowed = JsonPath.read(mdmsData,
				CalculatorConstants.FSM_TRIP_AMOUNT_OVERRIDE_ALLOWED);
		List<Map<String, Object>> noOftripsAllowed = JsonPath.read(mdmsData,
				CalculatorConstants.FSM_NO_OF_TRIPS_AMOUNT_OVERRIDE_ALLOWED);
		List<Map> advancePayment = JsonPath.read(mdmsData, CalculatorConstants.Advance_Payment_MODEL_JSON_PATH);

		SlumEnum slumName = null;
		if (!CollectionUtils.isEmpty(slumNameAllowed)) {
			slumName = ((StringUtils.isEmpty(fsm.getAddress().getSlumName())) ? SlumEnum.NO : SlumEnum.YES);
		} else {
			slumName = SlumEnum.NO;
		}

		// fetch advance amount from fsm application
		advance = fsm.getAdvanceAmount() != null ? new BigDecimal(fsm.getAdvanceAmount()) : new BigDecimal(0);

		if (!CollectionUtils.isEmpty(tripAountAllowed)) {
			Map<String, String> oldAdditionalDetails = fsm.getAdditionalDetails() != null
					? (Map<String, String>) fsm.getAdditionalDetails()
					: new HashMap<String, String>();
			if (oldAdditionalDetails != null || oldAdditionalDetails.get("tripAmount") != null) {
				amount = BigDecimal.valueOf(Double.valueOf((String) oldAdditionalDetails.get("tripAmount")));

				// fetch advance amount from fsm application
//				advance = BigDecimal.valueOf(Double.valueOf((String) oldAdditionalDetails.get("advanceAmount")));
			} else {
				List<BillingSlab> billingSlabs = billingSlabRepository.getBillingSlabData(BillingSlabSearchCriteria
						.builder().capacity(NumberUtils.toDouble(fsm.getVehicleCapacity())).slum(slumName)
						.propertyType(fsm.getPropertyUsage()).tenantId(fsm.getTenantId()).build());
				if (billingSlabs.size() > 0) {
					amount = billingSlabs.get(0).getPrice();
				}
			}

		} else {
			List<BillingSlab> billingSlabs = billingSlabRepository.getBillingSlabData(
					BillingSlabSearchCriteria.builder().capacity(NumberUtils.toDouble(fsm.getVehicleCapacity()))
							.slum(slumName).propertyType(fsm.getPropertyUsage()).tenantId(fsm.getTenantId()).build());
			if (billingSlabs.size() > 0) {
				amount = billingSlabs.get(0).getPrice();
			}
		}

		if (amount == null) {
			throw new CustomException(CalculatorConstants.INVALID_PRICE,
					"Price not found in Billing Slab for the given propertyType and slumName");
		}

//		if (advance == null) {
//			throw new CustomException(CalculatorConstants.INVALID_PRICE,
//					"Price not found in Billing Slab for the given propertyType and slumName");
//		}

		Integer noOfTrips = 1;
		if (!CollectionUtils.isEmpty(noOftripsAllowed)) {
			noOfTrips = calulationCriteria.getFsm().getNoOfTrips();
		}

		if (fsm.getAdvanceAmount() != null && fsm.getApplicationStatus().equalsIgnoreCase("DSO_INPROGRESS")) {

			boolean tripIncrease = false;
			BigDecimal remainingBillAmount = new BigDecimal(0);
			FSM oldfsm = fsmService.getFsmApplication(requestInfo, fsm.getTenantId(), fsm.getApplicationNo());
			BillResponse billResponse = billingService.fetchBill(fsm, requestInfo);
			log.debug("billResponse in calculator "+ billResponse+"oldfsm"+oldfsm);
			if (billResponse.getBill().size() > 0) {
				remainingBillAmount = billResponse.getBill().get(0).getTotalAmount();
			}
			Integer remainingNumberOfTrips = 0;
			if (oldfsm.getNoOfTrips() < fsm.getNoOfTrips() || oldfsm.getNoOfTrips() == fsm.getNoOfTrips()) {
				calculatedAmount = (BigDecimal.valueOf(noOfTrips).multiply(amount));
				tripIncrease = true;
			}

			else if (oldfsm.getNoOfTrips() > fsm.getNoOfTrips()) {
				remainingNumberOfTrips = fsm.getNoOfTrips();
				tripIncrease = false;
			}
			 paidAmount = BigDecimal.valueOf(oldfsm.getNoOfTrips()).multiply(amount).subtract(remainingBillAmount);;
			log.debug("remainingNumberOfTrips in calculator ::" +remainingNumberOfTrips+"amount==>"+BigDecimal.valueOf(remainingNumberOfTrips).multiply(amount));
			// Integer remainingNumberOfTrips = oldfsm.getNoOfTrips() - noOfTrips;
			if (remainingNumberOfTrips != 0 && !tripIncrease && (paidAmount
					.compareTo((BigDecimal.valueOf(remainingNumberOfTrips).multiply(amount))) == 1)) {

				throw new CustomException(CalculatorConstants.ADVANCE_AMOUNT_MORE_THAN_TOTAL_TRIP_AMOUNT,
						"Not allowed to reduce number of trips as amount is already collected");
			} else
				calculatedAmount = (BigDecimal.valueOf(noOfTrips).multiply(amount));

		} else {
			calculatedAmount = BigDecimal.valueOf(noOfTrips).multiply(amount);

		}

		if (calculatedAmount.compareTo(BigDecimal.ZERO) == -1)
			throw new CustomException(CalculatorConstants.INVALID_PRICE, "Tax amount is negative");

		// If advance amount if greater then the total trip amount
		log.debug("new Application Entry before ", fsm.getAdvanceAmount(), fsm.getApplicationStatus());
		if (fsm.getAdvanceAmount() != null && fsm.getApplicationStatus() == null) {
			log.debug("new Application Entry after", fsm.getAdvanceAmount(), fsm.getApplicationStatus());
			BigDecimal advanceAmount = advanceCalculate(calculatedAmount, fsm.getTenantId(), requestInfo);

			if (advance.compareTo(calculatedAmount) == 1) {
				// (advance + " is greater than " + calculatedAmout + ".");
				throw new CustomException(CalculatorConstants.INVALID_MAX_ADVANCE_AMOUNT,
						"AdvanceAmount should not be greater than the total trip amount :" + calculatedAmount);
			} else if (advanceAmount.compareTo(advance) == 1) {
				// (advanceAmount + " is greater than " + advance + ".");
				throw new CustomException(CalculatorConstants.INVALID_MIN_ADVANCE_AMOUNT,
						"Advance amount should not be less than :" + advanceAmount);
			}

			// claculation of due amount

		}
		estimate.setEstimateAmount(calculatedAmount);
		estimate.setCategory(Category.FEE);

		String taxHeadCode = utils.getTaxHeadCode(calulationCriteria.getFeeType());
		estimate.setTaxHeadCode(taxHeadCode);
		estimates.add(estimate);
		estimatesAndSlabs.setEstimates(estimates);
		return estimatesAndSlabs;
	}

	public String getAmountForVehicleType(String vehicleType, Object mdmsData) {
		String amount = null;
		List<Map> vehicleTypeList = JsonPath.read(mdmsData, CalculatorConstants.VEHICLE_MAKE_MODEL_JSON_PATH);
		for (Map vehicleTypeMap : vehicleTypeList) {
			if (vehicleTypeMap.get("code").equals(vehicleType)) {
				amount = (String) vehicleTypeMap.get("capacity");
				break;
			}
		}
		return amount;
	}

}
