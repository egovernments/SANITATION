import React, { useEffect, useState, Fragment } from "react";
import {
    FormStep,
    CardLabel,
    Dropdown,
    RadioButtons,
    LabelFieldPair,
    RadioOrSelect,
    TextInput,
} from "@egovernments/digit-ui-react-components";
import Timeline from "../components/TLTimelineInFSM";
import { useLocation } from "react-router-dom";

const AddWorkerAddress = ({ t, config, onSelect, userType, formData }) => {
    const allCities = Digit.Hooks.fsm.useTenants();
    let tenantId = Digit.ULBService.getCurrentTenantId();
    if (userType !== "employee") {
        tenantId = Digit.SessionStorage.get("CITIZEN.COMMON.HOME.CITY")?.code;
    }
    const location = useLocation();
    //   const isNewWorker = location?.pathname?.includes("new-worker");
    //   const isEditWorker = location?.pathname?.includes("modify-worker");
    const isNewVendor = location?.pathname?.includes("new-vendor");
    const isEditVendor = location?.pathname?.includes("modify-vendor");


    const [gramPanchayats, setGramPanchayats] = useState();
    const [selectedGp, setSelectedGp] = useState(() =>
        formData?.address?.additionalDetails?.gramPanchayat
            ? formData?.address?.additionalDetails?.gramPanchayat
            : {}
    );
    const [villages, setVillages] = useState([]);
    const [selectedVillage, setSelectedVillage] = useState(() =>
        formData?.address?.additionalDetails?.village
            ? formData?.address?.additionalDetails?.village
            : {}
    );




    function selectGramPanchayat(value) {
        setSelectedGp(value);
        const filteredVillages = fetchedGramPanchayats.filter(
            (items) => items?.code === value?.code
        )[0].children;
        const localitiesWithLocalizationKeys = filteredVillages?.map((obj) => ({
            ...obj,
            i18nkey:
                tenantId.replace(".", "_").toUpperCase() + "_REVENUE_" + obj?.code,
        }));
        if (localitiesWithLocalizationKeys?.length > 0) {
            setVillages(localitiesWithLocalizationKeys);
        }
        if (userType === "employee") {
            onSelect(config.key, { ...formData[config.key], gramPanchayat: value });

        }
        Digit.SessionStorage.del("locationType");
    }

    const [newVillage, setNewVillage] = useState();

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

    if (formData && formData.address) {
        // Check if propertyLocation does not exist in address
        if (!formData.address.hasOwnProperty("propertyLocation")) {
            // Assign default value to propertyLocation
            formData.address.propertyLocation = inputs[0];
        }
    }

    const { pincode, city, propertyLocation } = formData?.address || "";
    const cities =
        userType === "employee"
            ? allCities.filter((city) => city.code === tenantId)
            : pincode
                ? allCities.filter((city) => city?.pincode?.some((pin) => pin == pincode))
                : allCities;

    const [selectedCity, setSelectedCity] = useState(
        () =>
            formData?.address?.city ||
            Digit.SessionStorage.get("fsm.file.address.city") ||
            Digit.SessionStorage.get("CITIZEN.COMMON.HOME.CITY")
    );
    const [newLocality, setNewLocality] = useState();

    var { data: fetchedGramPanchayats } = Digit.Hooks.useBoundaryLocalities(
        selectedCity?.code,
        "gramPanchayats",
        {
            enabled: !!selectedCity,
        },
        t
    );

    const { data: fetchedLocalities } = Digit.Hooks.useBoundaryLocalities(
        selectedCity?.code,
        "revenue",
        {
            enabled: !!selectedCity,
        },
        t
    );
    console.log(`*** LOG fetchedLocalities ***`,fetchedLocalities);
    const { data: urcConfigData } = Digit.Hooks.fsm.useMDMS(
        tenantId,
        "FSM",
        "UrcConfig"
    );
    const urcConfig = urcConfigData?.FSM?.UrcConfig;

    var isUrcEnable =
        urcConfig && urcConfig.length > 0 && urcConfig[0].URCEnable;



// "WITHIN_ULB_LIMITS"
    const [selectLocation, setSelectLocation] = useState(() =>
        formData?.address?.propertyLocation
            ? formData?.address?.propertyLocation
            : Digit.SessionStorage.get("locationType")
                ? Digit.SessionStorage.get("locationType")
                : inputs[0]
    );

    const [localities, setLocalities] = useState();
    const [selectedLocality, setSelectedLocality] = useState();

    useEffect(() => {
        if (fetchedGramPanchayats) {
            if (fetchedGramPanchayats && fetchedGramPanchayats.length > 0) {
                setGramPanchayats(fetchedGramPanchayats);
            }
            if (formData?.address?.additionalDetails?.gramPanchayat?.code) {
                const filteredGramPanchayat = fetchedGramPanchayats.filter(
                    (obj) =>
                        obj?.code ===
                        formData?.address?.additionalDetails?.gramPanchayat?.code
                )[0];
                setSelectedGp(filteredGramPanchayat);
                setNewGp(formData?.address?.additionalDetails?.newGp);
                var villageUnderGp = filteredGramPanchayat?.children.filter(
                    (obj) =>
                        obj?.code === formData?.address?.additionalDetails?.village?.code
                );
                if (villageUnderGp.length > 0) {
                    villageUnderGp[0].i18nkey =
                        tenantId.replace(".", "_").toUpperCase() +
                        "_REVENUE_" +
                        villageUnderGp[0]?.code;
                    setSelectedVillage(villageUnderGp[0]);
                    setVillages(villageUnderGp);
                } else {
                    setNewVillage(
                        typeof formData?.address?.additionalDetails?.village === "string"
                            ? formData?.address?.additionalDetails?.village
                            : ""
                    );
                }
            }
        }
    }, [
        fetchedGramPanchayats,
        formData?.address?.additionalDetails?.gramPanchayat?.code,
    ]);

    const onChangeVillage = (value) => {
        setNewVillage(value);
        if (userType === "employee") {
            onSelect(config.key, { ...formData[config.key], newVillage: value });
        }
    };

    useEffect(() => {
        if (cities) {
            if (cities.length === 1) {
                setSelectedCity(cities[0]);
            }
        }
    }, [cities]);


 


    useEffect(() => {
        if (selectedCity && selectLocation) {
            if (userType === "employee") {
                onSelect(config.key, {
                    ...formData[config.key],
                    city: selectedCity,
                    propertyLocation: selectLocation,
                });
            }
        }
        if (
            (!isUrcEnable || isNewVendor || isEditVendor) &&
            selectedCity &&
            fetchedLocalities
        ) {
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
            // console.log(`*** LOG  localities ***`, localities);
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
    }, [selectedCity, selectLocation, fetchedLocalities]);

    useEffect(() => {
        if (formData?.address?.propertyLocation?.code === 'WITHIN_ULB_LIMITS') {
            if (Array.isArray(fetchedLocalities)) {
                const matchedLocality = fetchedLocalities.find(locality => 
                    locality.code === formData?.address?.locality?.code
                );
                if (matchedLocality) {
                    setSelectedLocality(matchedLocality);
                } else {
                    console.warn("No matching locality found for the given code.");
                }
            } else {
                console.warn("fetchedLocalities is not defined or is not an array.");
            }
        } else if (formData?.address?.propertyLocation?.code === 'FROM_GRAM_PANCHAYAT') {
            // Handle logic for 'FROM_GRAM_PANCHAYAT' case here, if needed
        }
    }, [formData, fetchedLocalities]);
    

    if (
        userType !== "employee" &&
        propertyLocation?.code === "FROM_GRAM_PANCHAYAT"
    ) {
        config.texts.cardText =
            "CS_FILE_APPLICATION_PROPERTY_LOCATION_GRAM_PANCHAYAT_TEXT";
    }

    function selectVillage(value) {
        setSelectedVillage(value);
        if (userType === "employee") {
            onSelect(config.key, { ...formData[config.key], village: value });
        }
    }

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
            if (value.code === "FROM_GRAM_PANCHAYAT") {
                onSelect("tripData", {
                    ...formData["tripData"],
                    amountPerTrip: "",
                    amount: "",
                });
                onSelect(config.key, {
                    ...formData[config.key],
                    propertyLocation: value,
                });
            } else {
                onSelect(config.key, {
                    ...formData[config.key],
                    propertyLocation: value,
                });
            }
        }
    }

    function selectLocality(locality) {
        setSelectedLocality(locality);
        if (userType === "employee") {
            onSelect(config.key, { ...formData[config.key], locality: locality });
        }
    }

    const onNewLocality = (value) => {
        setNewLocality(value);
        if (userType === "employee") {
            onSelect(config.key, { ...formData[config.key], newLocality: value });
        }
    };

    function onSubmit() {
        onSelect(config.key, {
            city: selectedCity,
            propertyLocation: Digit.SessionStorage.get("locationType")
                ? Digit.SessionStorage.get("locationType")
                : selectLocation,
        });
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
                        option={cities?.sort((a, b) => a.name.localeCompare(b.name))}
                        select={selectCity}
                        optionKey="code"
                        t={t}
                    />
                </LabelFieldPair>
                {!isUrcEnable || isNewVendor || isEditVendor ? (
                    <div>
                        <LabelFieldPair>
                            <CardLabel className="card-label-smaller">
                                {`${t("ES_NEW_APPLICATION_LOCATION_MOHALLA")} *`}
                                {/* {config.isMandatory ? " * " : null} */}
                            </CardLabel>
                            <Dropdown
                                className="form-field"
                                isMandatory
                                selected={selectedLocality}
                                option={localities?.sort((a, b) =>
                                    a?.name?.localeCompare(b?.name)
                                )}
                                select={selectLocality}
                                optionKey="i18nkey"
                                t={t}
                            />
                        </LabelFieldPair>
                        {!isNewVendor &&
                            !isEditVendor &&
                            !isUrcEnable &&
                            formData?.address?.locality?.name === "Other" && (
                                <LabelFieldPair>
                                    <CardLabel className="card-label-smaller">{`${t(
                                        "ES_INBOX_PLEASE_SPECIFY_LOCALITY"
                                    )} *`}</CardLabel>
                                    <div className="field">
                                        <TextInput
                                            id="newLocality"
                                            key="newLocality"
                                            value={newLocality}
                                            onChange={(e) => onNewLocality(e.target.value)}
                                        />
                                    </div>
                                </LabelFieldPair>
                            )}
                    </div>
                ) : (
                    <div>
                        <LabelFieldPair>
                            <CardLabel>{`${t("ES_NEW_PROPERTY_LOCATION")} *`}</CardLabel>
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

                        {/* GRAMA PANCHAYATH*/}
                        {propertyLocation?.code === "FROM_GRAM_PANCHAYAT" ? (
                            <div>
                                <LabelFieldPair>
                                    <CardLabel className="card-label-smaller">
                                        {t("CS_GRAM_PANCHAYAT")}
                                        {config.isMandatory ? " * " : null}
                                    </CardLabel>
                                    <Dropdown
                                        className="form-field"
                                        isMandatory
                                        selected={selectedGp}
                                        option={gramPanchayats?.sort((a, b) =>
                                            a.name.localeCompare(b.name)
                                        )}
                                        select={selectGramPanchayat}
                                        optionKey="i18nkey"
                                        t={t}
                                    />
                                </LabelFieldPair>
                                {selectedGp?.name === "Other" && (
                                    <LabelFieldPair>
                                        <CardLabel className="card-label-smaller">{`${t(
                                            "ES_INBOX_PLEASE_SPECIFY_GRAM_PANCHAYAT"
                                        )} *`}</CardLabel>
                                        <div className="field">
                                            <TextInput
                                                id="newGp"
                                                key="newGp"
                                                value={newGp}
                                                onChange={(e) => onNewGpChange(e.target.value)}
                                            />
                                        </div>
                                    </LabelFieldPair>
                                )}
                                {villages.length > 0 && (
                                    <LabelFieldPair>
                                        <CardLabel className="card-label-smaller">
                                            {t("CS_VILLAGE_NAME")}
                                        </CardLabel>
                                        <Dropdown
                                            className="form-field"
                                            isMandatory
                                            selected={selectedVillage}
                                            option={villages?.sort((a, b) =>
                                                a.name.localeCompare(b.name)
                                            )}
                                            select={selectVillage}
                                            optionKey="i18nkey"
                                            t={t}
                                        />
                                    </LabelFieldPair>
                                )}
                                {villages.length === 0 && (
                                    <LabelFieldPair>
                                        <CardLabel className="card-label-smaller">
                                            {t("CS_VILLAGE_NAME")}
                                        </CardLabel>
                                        <div className="field">
                                            <TextInput
                                                id="village"
                                                key="village"
                                                value={newVillage}
                                                onChange={(e) => onChangeVillage(e.target.value)}
                                            />
                                        </div>
                                    </LabelFieldPair>
                                )}
                            </div>
                        ) : <LabelFieldPair>

                            {/* ULB LIMITS */}
                            <CardLabel className="card-label-smaller">
                                {`${t("ES_NEW_APPLICATION_LOCATION_MOHALLA")} *`}
                                {/* {config.isMandatory ? " * " : null} */}
                            </CardLabel>
                            <Dropdown
                                className="form-field"
                                isMandatory
                                selected={selectedLocality}
                                option={fetchedLocalities?.sort((a, b) =>
                                    a?.name?.localeCompare(b?.name)
                                )}
                                select={selectLocality}
                                optionKey="i18nkey"
                                t={t}
                            />
                        </LabelFieldPair>}

                    </div>
                )



                }
            </div>
        );
    }
    return (
        <React.Fragment>
            <Timeline currentStep={1} flow="APPLY" />
            <FormStep
                config={config}
                onSelect={onSubmit}
                t={t}
                isDisabled={selectLocation ? false : true}
            >
                {isUrcEnable && (
                    <>
                        <CardLabel>{`${t("ES_NEW_PROPERTY_LOCATION")} *`}</CardLabel>
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
                    options={cities?.sort((a, b) => a.name.localeCompare(b.name))}
                    selectedOption={cities?.length === 1 ? cities[0] : selectedCity}
                    optionKey="i18nKey"
                    onSelect={selectCity}
                    t={t}
                />
            </FormStep>
        </React.Fragment>
    );
};

export default AddWorkerAddress;
