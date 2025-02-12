import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import {
  FormComposer,
  Loader,
  Header,
} from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom";
import { config } from "./config";

const isConventionalSpecticTank = (tankDimension) => tankDimension === "lbd";

export const NewApplication = ({ parentUrl, heading }) => {
  // const __initPropertyType__ = window.Digit.SessionStorage.get("propertyType");
  // const __initSubType__ = window.Digit.SessionStorage.get("subType");
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const stateId = Digit.ULBService.getStateId();
  const checkvehicletrack = Digit.Hooks.fsm.useVehicleTrackingCheck(tenantId);
  // const { data: commonFields, isLoading } = useQuery('newConfig', () => fetch(`http://localhost:3002/commonFields`).then(res => res.json()))
  // const { data: postFields, isLoading: isTripConfigLoading } = useQuery('tripConfig', () => fetch(`http://localhost:3002/tripDetails`).then(res => res.json()))
  const { data: commonFields, isLoading } = Digit.Hooks.fsm.useMDMS(
    stateId,
    "FSM",
    "CommonFieldsConfig"
  );
  const {
    data: preFields,
    isLoading: isApplicantConfigLoading,
  } = Digit.Hooks.fsm.useMDMS(stateId, "FSM", "PreFieldsConfig");
  const {
    data: postFields,
    isLoading: isTripConfigLoading,
  } = Digit.Hooks.fsm.useMDMS(stateId, "FSM", "PostFieldsConfig");

  const [
    mutationHappened,
    setMutationHappened,
    clear,
  ] = Digit.Hooks.useSessionStorage("FSM_MUTATION_HAPPENED", false);
  const [errorInfo, setErrorInfo, clearError] = Digit.Hooks.useSessionStorage(
    "FSM_ERROR_DATA",
    false
  );
  const [
    successData,
    setsuccessData,
    clearSuccessData,
  ] = Digit.Hooks.useSessionStorage("FSM_MUTATION_SUCCESS_DATA", false);

  useEffect(() => {
    setMutationHappened(false);
    clearSuccessData();
    clearError();
  }, []);
  // const { data: vehicleMenu } = Digit.Hooks.fsm.useMDMS(state, "Vehicle", "VehicleType", { staleTime: Infinity });
  // const { data: channelMenu } = Digit.Hooks.fsm.useMDMS(tenantId, "FSM", "EmployeeApplicationChannel");
  const { t } = useTranslation();
  const history = useHistory();

  const [canSubmit, setSubmitValve] = useState(false);

  // const [channel, setChannel] = useState(null);

  const defaultValues = {
    tripData: {
      noOfTrips: 1,
      amountPerTrip: null,
      amount: null,
    },
  };

  const onFormValueChange = (setValue, formData) => {
    if (
      formData?.propertyType &&
      formData?.subtype &&
      (formData?.address?.locality?.code ||
        (formData?.address?.propertyLocation?.code === "FROM_GRAM_PANCHAYAT" &&
          formData?.address?.gramPanchayat?.code)) &&
      formData?.tripData?.vehicleType &&
      formData?.channel &&
      (formData?.tripData?.amountPerTrip ||
        formData?.tripData?.amountPerTrip === 0)
    ) {
      setSubmitValve(true);
      const pitDetailValues = formData?.pitDetail
        ? Object.values(formData?.pitDetail).filter((value) => value > 0)
        : null;
      let max = Digit.SessionStorage.get("total_amount");
      let min = Digit.SessionStorage.get("advance_amount");
      if (formData?.pitType) {
        if (pitDetailValues === null || pitDetailValues?.length === 0) {
          setSubmitValve(true);
        } else if (
          isConventionalSpecticTank(formData?.pitType?.dimension) &&
          pitDetailValues?.length >= 3
        ) {
          setSubmitValve(true);
        } else if (
          !isConventionalSpecticTank(formData?.pitType?.dimension) &&
          pitDetailValues?.length >= 2
        ) {
          setSubmitValve(true);
        } else setSubmitValve(false);
      }
      if (
        formData?.tripData?.amountPerTrip !== 0 &&
        (formData?.advancepaymentPreference?.advanceAmount < min ||
          formData?.advancepaymentPreference?.advanceAmount > max ||
          formData?.advancepaymentPreference?.advanceAmount === "")
      ) {
        setSubmitValve(false);
      }
    } else {
      setSubmitValve(false);
    }
  };

  // useEffect(() => {
  //   (async () => {

  //   })();
  // }, [propertyType, subType, vehicle]);

  const onSubmit = (data) => {
    const applicationChannel = data.channel;
    const sanitationtype = data?.pitType?.code;
    const pitDimension = data?.pitDetail;
    const applicantName = data.applicationData.applicantName;
    const mobileNumber = data.applicationData.mobileNumber;
    const pincode = data?.address?.pincode;
    const street = data?.address?.street?.trim();
    const doorNo = data?.address?.doorNo?.trim();
    const slum = data?.address?.slum;
    const landmark = data?.address?.landmark?.trim();
    const noOfTrips = data?.tripData?.noOfTrips;
    const amount = data.tripData.amountPerTrip;
    const cityCode = data?.address?.city?.code;
    const city = data?.address?.city?.name;
    const state = data?.address?.city?.state;
    const localityCode = data?.address?.locality?.code;
    const localityName = data?.address?.locality?.name;
    const gender = data.applicationData.applicantGender;
    const paymentPreference =
      amount === 0
        ? null
        : data?.paymentPreference
        ? data?.paymentPreference
        : null;
    const advanceAmount =
      amount === 0 ? null : data?.advancepaymentPreference?.advanceAmount;

    const gramPanchayat = data?.address.gramPanchayat;
    const village = data?.address.village;
    const propertyLocation = data?.address?.propertyLocation?.code;

    const formData = {
      fsm: {
        citizen: {
          name: applicantName,
          mobileNumber,
          gender: gender,
        },
        tenantId: tenantId,
        sanitationtype: sanitationtype,
        source: applicationChannel.code,
        additionalDetails: {
          tripAmount: JSON.stringify(amount),
        },
        propertyUsage: data?.subtype,
        vehicleCapacity: data?.tripData?.vehicleType?.capacity,
        pitDetail: {
          ...pitDimension,
          distanceFromRoad: data?.distanceFromRoad,
        },
        address: {
          tenantId: cityCode,
          landmark,
          doorNo,
          street,
          city,
          state,
          pincode,
          slumName: slum,
          locality: {
            code:
              data?.address?.propertyLocation?.code === "WITHIN_ULB_LIMITS"
                ? localityCode
                : village?.code
                ? village?.code
                : gramPanchayat?.code,
            name:
              data?.address?.propertyLocation?.code === "WITHIN_ULB_LIMITS"
                ? localityName
                : village?.name
                ? village?.name
                : gramPanchayat?.name,
          },
          geoLocation: {
            latitude: data?.address?.latitude,
            longitude: data?.address?.longitude,
          },
          additionalDetails: {
            boundaryType:
              propertyLocation === "FROM_GRAM_PANCHAYAT"
                ? village?.code
                  ? "Village"
                  : "GP"
                : "Locality",
            gramPanchayat: {
              code: gramPanchayat?.code,
              name: gramPanchayat?.name,
            },
            village: {
              code: village?.code ? village?.code : "",
              name: village?.name ? village?.name : village,
            },
          },
        },
        noOfTrips,
        paymentPreference,
        advanceAmount:
          typeof advanceAmount === "number"
            ? JSON.stringify(advanceAmount)
            : advanceAmount === null
            ? "0"
            : advanceAmount,
      },
      workflow: null,
    };

    window.Digit.SessionStorage.set("propertyType", null);
    window.Digit.SessionStorage.set("subType", null);
    Digit.SessionStorage.set("city_property", null);
    Digit.SessionStorage.set("selected_localities", null);
    Digit.SessionStorage.set("locality_property", null);
    history.push(`/${window?.contextPath}/employee/fsm/response`, formData);
  };

  if (isLoading || isTripConfigLoading || isApplicantConfigLoading) {
    return <Loader />;
  }

  const configs = [...preFields, ...commonFields];

  return (
    <React.Fragment>
      <div style={{ marginLeft: "15px" }}>
        <Header>{t("ES_TITLE_NEW_DESULDGING_APPLICATION")}</Header>
      </div>
      <FormComposer
        isDisabled={!canSubmit}
        label={t("ES_COMMON_APPLICATION_SUBMIT")}
        config={configs
          .filter((i) => !i.hideInEmployee)
          .map((config) => {
            return {
              ...config,
              body: config.body.filter((a) => !a.hideInEmployee),
            };
          })}
        fieldStyle={{ marginRight: 0 }}
        formCardStyle={true}
        onSubmit={onSubmit}
        defaultValues={defaultValues}
        onFormValueChange={onFormValueChange}
        noBreakLine={true}
        fms_inline
      />
    </React.Fragment>
  );
};
