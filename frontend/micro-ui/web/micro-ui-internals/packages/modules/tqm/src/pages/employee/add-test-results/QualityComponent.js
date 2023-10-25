import React, { useState, useEffect } from "react";
import { CardLabel, LabelFieldPair, Toast, TextInput, LinkButton, CardLabelError, MobileNumber, DatePicker, Loader, Header, ImageUploadHandler, UploadFile, MultiUploadWrapper } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useForm, Controller } from "react-hook-form";
import _ from "lodash";
import { useCustomMDMSV2 } from "../../../hooks/useCustomMDMSV2";


const QualityParameter = ({ config, onSelect, userType, formData, setError, formState, clearErrors }) => {
    const { t } = useTranslation();
    const [focusIndex, setFocusIndex] = useState({ index: -1, type: "" });
    const { control, formState: localFormState, watch, setError: setLocalError, clearErrors: clearLocalErrors, setValue, trigger, getValues } = useForm();
    const formValue = watch();
    const { errors } = localFormState;
    const [isErrors, setIsErrors] = useState(false);
    const [showComponent, setShowComponent] = useState(false);
    const [plantCode, setplantCode] = useState(null);
    const [processCode, setprocessCode] = useState(null);
    const [stageCode, setstageCode] = useState(null);
    const [materialCode, setmaterialCode] = useState(null);
    const [showToast, setShowToast] = useState(false);
    const tenant = Digit.ULBService.getStateId();

    useEffect(() => {
        setplantCode(formData?.plantCode?.code)
    }, [formData?.plantCode?.code])

    useEffect(() => {
        setprocessCode(formData?.processCode?.code)
    }, [formData?.processCode?.code])

    useEffect(() => {
        setstageCode(formData?.stageCode?.code)
    }, [formData?.stageCode?.code])

    useEffect(() => {
        setmaterialCode(formData?.materialCode?.code)
    }, [formData?.materialCode?.code])

    const [allFieldsDefined, setallFieldsDefined] = useState(false);
    const [value, setValues] = useState();

    useEffect(() => {
        const excludedField = "QualityParameter";
        let allFieldsDefined = false;
        for (const key in formData) {
            if (key !== excludedField && formData[key] === undefined) {
                setallFieldsDefined(false);
                break;
            }
            else {
                setallFieldsDefined(formData);
            }
        }
    }, [formData])

    useEffect(() => {
        if (allFieldsDefined) {
            setShowComponent(true);
        }
        else setShowComponent(false);
    }, [allFieldsDefined]);

    const { isLoading, data, isError } = useCustomMDMSV2({
        tenantId: tenant,
        "filters": {
            "plant": plantCode,
            "process": processCode,
            "stage": stageCode,
            "material": materialCode
        },
        schemaCode: "PQM.TestStandard",
        changeQueryName: `${plantCode}${processCode}${stageCode}${materialCode}`,
        config: {
            enabled: !!allFieldsDefined,
            staleTime: 0
        }
    })
    const closeToast = () => {
        setTimeout(() => {
            setShowToast(false);
        }, 5000)
    };
    useEffect(() => {
        if (data === undefined) {
            setShowToast(false);
        }
        else if (!data || Object.keys(data).length === 0) {
            setShowToast({
                label: t('TQM_QUALITY_CRITERIA_NOT_PRESENT'),
                isWarning: true
            });
            closeToast();
            setShowComponent(false);
        }
        else setShowComponent(true);
    }, [data]);


    const qualityCriteria = data?.map(item => item.qualityCriteria);
    const errorStyle = { width: "70%", marginLeft: "30%", fontSize: "12px", marginTop: "-21px" };
    const CardLabelStyle = { marginTop: "-5px" }
    const [quality, setQuality] = useState({});

    function displayValue(newValue, criteria, index) {
        let temp = quality
        temp[criteria] = newValue;
        setQuality(temp)
    }

    useEffect(() => {
        onSelect("QualityParameter", quality);
    }, [quality])

    return (
        <div>
            {showComponent && (

                <React.Fragment>
                    <Header> {t("ES_TQM_QUALITY_PARAMETERS")}</Header>
                    {qualityCriteria?.map((criterionList, index) => (
                        <div key={index}>
                            {criterionList.map((criteria, subindex) => (
                                <LabelFieldPair key={subindex}>
                                    <CardLabel style={CardLabelStyle}>{t(criteria)}</CardLabel>
                                    <div className="field">
                                        <Controller
                                            control={control}
                                            name={`QualityParameter`}
                                            rules={{
                                                validate: {
                                                    pattern: (v) => (/^$|^[a-zA-Z0-9]+$/.test(v) ? true : t("ERR_DEFAULT_INPUT_FIELD_MSG")),
                                                }
                                            }}
                                            render={(props) => (
                                                <TextInput
                                                    value={props.value}
                                                    type={"number"}
                                                    onChange={(e) => {
                                                        const newValue = e.target.value;
                                                        displayValue(newValue, criteria, subindex);
                                                    }}
                                                />
                                            )}
                                        />
                                    </div>
                                </LabelFieldPair>

                            ))}
                        </div>
                    ))}

                    <LabelFieldPair>
                        <CardLabel style={CardLabelStyle}>{`${t("ES_TQM_TEST_PARAM_ATTACH_DOCUMENTS")}`}</CardLabel>
                        <div className="field">
                            <Controller
                                name={`photo`}
                                control={control}
                                rules={{}}
                                render={({ onChange, ref, value = [] }) => {
                                    function getFileStoreData(filesData) {
                                        const numberOfFiles = filesData.length;
                                        let finalDocumentData = [];
                                        if (numberOfFiles > 0) {
                                            filesData.forEach((value) => {
                                                finalDocumentData.push({
                                                    fileName: value?.[0],
                                                    fileStoreId: value?.[1]?.fileStoreId?.fileStoreId,
                                                    documentType: value?.[1]?.file?.type,
                                                });
                                            });
                                        }
                                        let temp = quality;
                                        temp = { ...temp, document: finalDocumentData?.[0]?.fileStoreId }
                                        setQuality(temp)
                                        onChange(numberOfFiles > 0 ? filesData : []);
                                    }
                                    return (
                                        <MultiUploadWrapper
                                            t={t}
                                            module="works"
                                            tenantId={Digit.ULBService.getCurrentTenantId()}
                                            getFormState={getFileStoreData}
                                            showHintBelow={false}
                                            setuploadedstate={value || []}
                                            allowedFileTypesRegex={/(jpg|jpeg|png|pdf)$/i}
                                            allowedMaxSizeInMB={2}
                                            maxFilesAllowed={1}
                                        />
                                    );
                                }}
                            />
                        </div>
                    </LabelFieldPair>

                </React.Fragment>

            )}
            {showToast && !showComponent && <Toast warning={showToast.isWarning} label={showToast.label} isDleteBtn={"true"} onClose={() => setShowToast(false)} style={{ bottom: "8%" }} />}


        </div>
    )
}

export default QualityParameter;


