import React, { useState, useEffect } from "react";
import {
  FormStep,
  Loader,
  RadioOrSelect,
  CitizenInfoLabel,
  LabelFieldPair,
  CardLabel,
  TextInput,
  CardLabelError,
} from "@egovernments/digit-ui-react-components";
import Timeline from "../components/TLTimelineInFSM";

const SelectPaymentPreference = ({ config, formData, t, onSelect, userType }) => {
  const tenantId = Digit.ULBService.getCitizenCurrentTenant();
  const stateId = Digit.ULBService.getStateId();
  const { data: PaymentTypeData, isLoading } = Digit.Hooks.fsm.useMDMS(stateId, "FSM", "PaymentType");
  const [paymentType, setPaymentType] = useState({});
  const [advanceAmount, setAdvanceAmount] = useState();

  const [billError, setError] = useState(false);

  const { code } = paymentType;
  useEffect(() => {
    if (!isLoading && PaymentTypeData) {
      const preFilledPaymentType = PaymentTypeData.filter(
        (paymentType) => paymentType.code === (formData?.selectPaymentPreference?.paymentType?.code || formData?.selectPaymentPreference?.paymentType)
      )[0];
      preFilledPaymentType ? setPaymentType(preFilledPaymentType) : setPaymentType(PaymentTypeData.find((i) => i.code === "POST_PAY"));
    }
  }, [formData, formData?.selectPaymentPreference?.paymentType?.code, formData?.selectPaymentPreference?.paymentType, PaymentTypeData]);

  const inputs = [
    {
      label: "ES_NEW_APPLICATION_ADVANCE_COLLECTION",
      type: "number",
      name: "advanceAmount",
      validation: {
        isRequired: true,
      },
      disable: false,
      default: formData?.selectPaymentPreference?.advanceAmount,
      isMandatory: true,
    },
  ];

  const setAdvanceAmountValue = (value) => {
    setAdvanceAmount(value);
  };
  const selectPaymentType = (value) => {
    setPaymentType(value);
    if (userType === "employee") {
      onSelect(config.key, value);
      onSelect("paymentDetail", null);
    }
  };

  const onSkip = () => {
    onSelect({});
  };

  const onSubmit = () => {
    onSelect(config.key, { paymentType, advanceAmount });
  };

  useEffect(() => {
    (async () => {
      if (formData?.propertyType && formData?.subtype && formData?.address && formData?.selectTripNo?.vehicleCapacity.capacity) {
        const capacity = formData?.selectTripNo?.vehicleCapacity.capacity;
        const { slum: slumDetails } = formData.address;
        const slum = slumDetails ? "YES" : "NO";
        const billingDetails = await Digit.FSMService.billingSlabSearch(tenantId, {
          propertyType: formData?.subtype?.code,
          capacity,
          slum,
        });

        const billSlab = billingDetails?.billingSlab?.length && billingDetails?.billingSlab[0];

        if (billSlab?.price && code === "PRE_PAY") {
          let totalTripAmount = billSlab.price * formData?.selectTripNo?.tripNo?.code;
          const advanceBalanceAmount = await Digit.FSMService.advanceBalanceCalculate(tenantId, {
            TotalTripAmount: totalTripAmount,
          });
          Digit.SessionStorage.set("total_amount", totalTripAmount);
          Digit.SessionStorage.set("advance_amount", advanceBalanceAmount);
          setAdvanceAmount(advanceBalanceAmount);

          setError(false);
        } else {
          sessionStorage.removeItem("Digit.total_amount");
          sessionStorage.removeItem("Digit.advance_amount");
          setError(true);
        }
      }
    })();
  }, [
    formData?.propertyType,
    formData?.subtype,
    formData?.address,
    formData?.selectTripNo?.vehicleCapacity.capacity,
    formData?.selectTripNo?.tripNo?.code,
    code,
  ]);

  if (isLoading) {
    return <Loader />;
  }
  if (userType === "employee") {
    return null;
  }
  let currentValue = advanceAmount;
  let max = Digit.SessionStorage.get("total_amount");
  let min = Digit.SessionStorage.get("advance_amount");

  return (
    <React.Fragment>
      <Timeline currentStep={3} flow="APPLY" />
      <FormStep
        config={config}
        onSelect={onSubmit}
        onSkip={onSkip}
        isDisabled={!paymentType || currentValue > max ? true : false || currentValue < min ? true : false}
        t={t}
      >
        <RadioOrSelect
          options={PaymentTypeData}
          selectedOption={paymentType}
          optionKey="i18nKey"
          onSelect={selectPaymentType}
          t={t}
          isMandatory={config.isMandatory}
        />
        {paymentType &&
          paymentType.code === "PRE_PAY" &&
          inputs?.map((input, index) => {
            return (
              <React.Fragment key={index}>
                <LabelFieldPair key={index}>
                  <CardLabel>
                    {t(input.label)}
                    {input.isMandatory ? " * " : null}
                  </CardLabel>
                  <div>
                    <TextInput
                      type={input.type}
                      key={input.name}
                      disable={input.disable}
                      onChange={(e) => setAdvanceAmountValue(e.target.value)}
                      value={advanceAmount}
                      {...input.validation}
                    />
                    {currentValue > max && (
                      <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "14px", marginBottom: "0px" }}>
                        {t("FSM_ADVANCE_AMOUNT_MAX")}
                      </CardLabelError>
                    )}
                    {currentValue < min && (
                      <CardLabelError style={{ width: "100%", marginTop: "-15px", fontSize: "14px", marginBottom: "0px" }}>
                        {t("FSM_ADVANCE_AMOUNT_MIN")}
                      </CardLabelError>
                    )}
                  </div>
                </LabelFieldPair>
              </React.Fragment>
            );
          })}
      </FormStep>
      {paymentType && code === "PRE_PAY" && (
        <CitizenInfoLabel info={t("CS_FILE_APPLICATION_INFO_LABEL")} text={t("CS_CHECK_INFO_PAY_NOW", paymentType)} />
      )}
      {paymentType && code === "POST_PAY" && (
        <CitizenInfoLabel info={t("CS_FILE_APPLICATION_INFO_LABEL")} text={t("CS_CHECK_INFO_PAY_LATER", paymentType)} />
      )}
    </React.Fragment>
  );
};

export default SelectPaymentPreference;
