import React, { useEffect, useState } from "react";
import { LabelFieldPair, CardLabel, TextInput, Dropdown, Loader, CardLabelError } from "@egovernments/digit-ui-react-components";
import { useParams, useLocation } from "react-router-dom";

const AdvanceCollection = ({ t, config, onSelect, formData, userType, FSMTextFieldStyle }) => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const state = Digit.ULBService.getStateId();
  const { pathname: url } = useLocation();
  let { id: applicationNumber } = useParams();
  const userInfo = Digit.UserService.getUser();
  const { isLoading: applicationLoading, isError, data: applicationData, error } = Digit.Hooks.fsm.useSearch(
    tenantId,
    { applicationNos: applicationNumber, uuid: userInfo.uuid },
    { staleTime: Infinity }
  );

  const [vehicle, setVehicle] = useState({ label: formData?.tripData?.vehicleCapacity });
  const [billError, setError] = useState(false);

  const { isLoading: isVehicleMenuLoading, data: vehicleData } = Digit.Hooks.fsm.useMDMS(state, "Vehicle", "VehicleType", { staleTime: Infinity });

  const { data: dsoData, isLoading: isDsoLoading, isSuccess: isDsoSuccess, error: dsoError } = Digit.Hooks.fsm.useDsoSearch(tenantId, {
    limit: -1,
    status: "ACTIVE",
  });

  const { data: paymentData, isLoading } = Digit.Hooks.fsm.useMDMS(state, "FSM", "PaymentType");

  const [paymentType, setPaymentType] = useState({});

  useEffect(() => {
    if (!isLoading && paymentData) {
      const preFilledPaymentType = paymentData.filter(
        (paymentType) => paymentType.code === (formData?.paymentPreference?.code || formData?.paymentPreference)
      )[0];
      preFilledPaymentType ? setPaymentType(preFilledPaymentType) : setPaymentType(paymentData.find((i) => i.code === "POST_PAY"));
    }
  }, [formData, formData?.paymentPreference?.code, formData?.paymentPreference, paymentData]);

  const { code } = paymentType;
  const inputs = [
    {
      label: "ES_NEW_APPLICATION_ADVANCE_COLLECTION",
      type: "number",
      name: "advanceAmount",
      validation: {
        isRequired: true,
      },

      default: formData?.advanceAmount,
      isMandatory: true,
    },
  ];

  function setValue(object) {
    onSelect(config.key, { ...formData[config.key], ...object });
  }
  function setAdvanceAmount(value) {
    onSelect(config.key, { ...formData[config.key], advanceAmount: value });
  }

  useEffect(() => {
    (async () => {
      if (formData?.tripData?.vehicleType !== vehicle) {
        setVehicle({ label: formData?.tripData?.vehicleType?.capacity });
      }

      if (formData?.propertyType && formData?.subtype && formData?.address && formData?.tripData?.vehicleType?.capacity) {
        const capacity = formData?.tripData?.vehicleType.capacity;
        const { slum: slumDetails } = formData.address;
        const slum = slumDetails ? "YES" : "NO";
        const billingDetails = await Digit.FSMService.billingSlabSearch(tenantId, {
          propertyType: formData?.subtype,
          capacity,
          slum,
        });

        const billSlab = billingDetails?.billingSlab?.length && billingDetails?.billingSlab[0];

        if (billSlab?.price && formData?.paymentPreference !== "POST_PAY") {
          const totalTripAmount = billSlab.price * formData.tripData.noOfTrips;

          const advanceBalanceAmount = await Digit.FSMService.advanceBalanceCalculate(tenantId, {
            TotalTripAmount: totalTripAmount,
          });
          Digit.SessionStorage.set("total_amount", totalTripAmount);
          Digit.SessionStorage.set("advance_amount", advanceBalanceAmount);

          if (code === "PRE_PAY") {
            setValue({
              advanceAmount: advanceBalanceAmount,
            });
          }
          setError(false);
        } else {
          sessionStorage.removeItem("Digit.total_amount");
          sessionStorage.removeItem("Digit.advance_amount");
          setError(true);
        }
      }
    })();
  }, [formData?.propertyType, formData?.subtype, formData?.address, formData?.tripData?.vehicleType?.capacity, formData?.tripData?.noOfTrips, code]);
  return isVehicleMenuLoading && isDsoLoading ? (
    <Loader />
  ) : (
    <div>
      {code === "PRE_PAY"
        ? inputs?.map((input, index) => {
            let currentValue = formData && formData[config.key] && formData[config.key][input.name];
            let max = Digit.SessionStorage.get("total_amount");
            let min = Digit.SessionStorage.get("advance_amount");

            return (
              <React.Fragment key={index}>
                <LabelFieldPair key={index}>
                  <CardLabel className="card-label-smaller">
                    {t(input.label)}
                    {input.isMandatory ? " * " : null}
                  </CardLabel>
                  <div className="field">
                    <TextInput
                      type={input.type}
                      key={input.name}
                      style={FSMTextFieldStyle}
                      onChange={(e) => setAdvanceAmount(e.target.value)}
                      value={input.default ? input.default : formData && formData[config.key] ? formData[config.key][input.name] : null}
                      {...input.validation}
                    />
                    {currentValue > max && (
                      <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "14px", marginBottom: "0px" }}>
                        {t("FSM_ADVANCE_AMOUNT_MAX")}
                      </CardLabelError>
                    )}
                    {currentValue < min && (
                      <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "14px", marginBottom: "-15px" }}>
                        {t("FSM_ADVANCE_AMOUNT_MIN")}
                      </CardLabelError>
                    )}
                  </div>
                </LabelFieldPair>
              </React.Fragment>
            );
          })
        : null}
    </div>
  );
};

export default AdvanceCollection;
