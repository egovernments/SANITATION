import React, { useEffect, useState } from "react";
import {
  FormStep,
  CardLabel,
  Dropdown,
  RadioButtons,
  LabelFieldPair,
  RadioOrSelect,
} from "@egovernments/digit-ui-react-components";
import Timeline from "../components/TLTimelineInFSM";
import { useLocation } from "react-router-dom";

const Digit = window.Digit;

const SelectAddress = ({ t, config, onSelect, userType, formData }) => {
  const allCities = Digit.Hooks.fsm.useTenants();
  let tenantId = Digit.ULBService.getCurrentTenantId();
  if (userType !== "employee") {
    tenantId = Digit.SessionStorage.get("CITIZEN.COMMON.HOME.CITY")?.code;
  }
  const home_city = Digit.SessionStorage.get("CITIZEN.COMMON.HOME.CITY");
  const location = useLocation();
  const isNewVendor = location.pathname.includes("new-vendor");

  const inputs = [
    {
      active: true,
      code: "WITHIN_ULB_LIMITS",
      i18nKey: "WITHIN_ULB_LIMITS",
      name: "Witnin ULB Limits",
    },
    {
      active: true,
      code: "FROM_GRAM_PANCHAYAT",
      i18nKey: "FROM_GRAM_PANCHAYAT",
      name: "From Gram Panchayat",
    },
  ];

  const { pincode, city } = formData?.address || "";
  const cities =
    userType === "employee"
      ? allCities.filter((city) => city.code === tenantId)
      : pincode
      ? allCities.filter((city) => city?.pincode?.some((pin) => pin == pincode))
      : // auto selecting city on the basis of city used as login
        allCities.filter((city) => city.code === home_city.code);

  const [selectedCity, setSelectedCity] = useState(
    () =>
      formData?.address?.city ||
      Digit.SessionStorage.get("fsm.file.address.city") ||
      null
  );
  const { data: fetchedLocalities } = Digit.Hooks.useBoundaryLocalities(
    selectedCity?.code,
    "revenue",
    {
      enabled: !!selectedCity,
    },
    t
  );
  const { data: urcConfig } = Digit.Hooks.fsm.useMDMS(
    tenantId,
    "FSM",
    "UrcConfig"
  );
  const isUrcEnable =
    urcConfig && urcConfig.length > 0 && urcConfig[0].URCEnable;
  const [selectLocation, setSelectLocation] = useState(inputs[0]);
  const [localities, setLocalities] = useState();
  const [selectedLocality, setSelectedLocality] = useState();
  const [selectedLocation, setSelectedLocation] = useState();

  useEffect(() => {
    if (cities) {
      if (cities.length === 1) {
        setSelectedCity(cities[0]);
      }
    }
  }, [cities]);

  useEffect(() => {
    if (selectedCity && fetchedLocalities) {
      let __localityList = fetchedLocalities;
      let filteredLocalityList = [];

      if (formData?.address?.locality) {
        setSelectedLocality(formData.address.locality);
      }


      if (formData?.address?.pincode) {
        filteredLocalityList = __localityList.filter((obj) =>
          obj.pincode?.find((item) => item == formData.address.pincode)
        );
        if (!formData?.address?.locality) setSelectedLocality();
      }

      if (userType === "employee") {
        onSelect(config.key, { ...formData[config.key], city: selectedCity });
      }
      setLocalities(() =>
        filteredLocalityList.length > 0 ? filteredLocalityList : __localityList
      );
      if (filteredLocalityList.length === 1) {
        setSelectedLocality(filteredLocalityList[0]);
        if (userType === "employee") {
          onSelect(config.key, {
            ...formData[config.key],
            locality: filteredLocalityList[0],
          });
        }
      }
      
    }
  }, [selectedCity, formData?.address?.pincode, fetchedLocalities]);

  function selectCity(city) {
    setSelectedLocality(null);
    setLocalities(null);
    Digit.SessionStorage.set("fsm.file.address.city", city);
    setSelectedCity(city);
  }
  function selectedValue(value) {
    setSelectLocation(value);
    Digit.SessionStorage.set("locationType", value);
    if (userType === "employee") {
      onSelect(config.key, {
        ...formData[config.key],
        propertyLocation: value,
      });
    }
  }

  function selectLocality(locality) {
    setSelectedLocality(locality);
    if (userType === "employee") {
      onSelect(config.key, { ...formData[config.key], locality: locality });
    }
  }

  function onSubmit() {
    onSelect(config.key, { city: selectedCity, locality: selectedLocality, 
      propertyLocation: Digit.SessionStorage.get("locationType")
      ? Digit.SessionStorage.get("locationType")
      : selectLocation, });
  }

  if (userType === "employee") {
    return (
      <div>
        <LabelFieldPair>
          <CardLabel className="card-label-smaller">
            {t("MYCITY_CODE_LABEL")}
            {config.isMandatory ? " * " : null}
          </CardLabel>
          <Dropdown
            className="form-field"
            isMandatory
            selected={cities?.length === 1 ? cities[0] : selectedCity}
            disable={cities?.length === 1}
            option={cities}
            select={selectCity}
            optionKey="code"
            t={t}
          />
        </LabelFieldPair>
        <LabelFieldPair>
          <CardLabel className="card-label-smaller">
            {t("ES_NEW_APPLICATION_LOCATION_MOHALLA")}
            {config.isMandatory ? " * " : null}
          </CardLabel>
          <Dropdown
            className="form-field"
            isMandatory
            selected={selectedLocality}
            option={localities}
            select={selectLocality}
            optionKey="i18nkey"
            t={t}
          />
        </LabelFieldPair>
        {!isUrcEnable || isNewVendor ? (
          <LabelFieldPair>
            <CardLabel className="card-label-smaller">
              {`${t("CS_CREATECOMPLAINT_MOHALLA")} *`}
              {/* {config.isMandatory ? " * " : null} */}
            </CardLabel>
            <Dropdown
              className="form-field"
              isMandatory
              selected={selectedLocality}
              option={localities}
              select={selectLocality}
              optionKey="i18nkey"
              t={t}
            />
          </LabelFieldPair>
        ) : (
          <LabelFieldPair>
            <CardLabel>{`${t("CS_PROPERTY_LOCATION")} *`}</CardLabel>
            <div className="field">
              <RadioButtons
                selectedOption={selectLocation}
                onSelect={selectedValue}
                style={{ display: "flex", marginBottom: 0 }}
                innerStyles={{ marginLeft: "10px" }}
                options={inputs}
                optionsKey="i18nKey"
                // disabled={editScreen}
              />
            </div>
          </LabelFieldPair>
        )}
      </div>
    );
  }
  return (
    <React.Fragment>
      <Timeline currentStep={1} flow="APPLY" />
      <div>
        <FormStep
          config={config}
          onSelect={onSubmit}
          t={t}
          isDisabled={selectedLocality ? false : true}
        >
          {isUrcEnable && (
          <>
            <CardLabel>{`${t("CS_PROPERTY_LOCATION")} *`}</CardLabel>
            <RadioOrSelect
              isMandatory={config.isMandatory}
              options={inputs}
              selectedOption={
                Digit.SessionStorage.get("locationType")
                  ? Digit.SessionStorage.get("locationType")
                  : selectLocation
              }
              optionKey="i18nKey"
              onSelect={selectedValue}
              t={t}
            />
          </>
        )}
        <CardLabel>{`${t("MYCITY_CODE_LABEL")} *`}</CardLabel>
        <RadioOrSelect
          options={cities}
          selectedOption={selectedCity}
          optionKey="i18nKey"
          onSelect={selectCity}
          t={t}
        />
          
          {/* {selectedCity && localities && (
            <CardLabel>{`${t("CS_CREATECOMPLAINT_MOHALLA")} *`}</CardLabel>
          )}
          {selectedCity && localities && (
            <RadioOrSelect
              isMandatory={config.isMandatory}
              options={localities}
              selectedOption={selectedLocality}
              optionKey="i18nkey"
              onSelect={selectLocality}
              t={t}
            />
          )} */}
        </FormStep>
      </div>
    </React.Fragment>
  );
};

export default SelectAddress;