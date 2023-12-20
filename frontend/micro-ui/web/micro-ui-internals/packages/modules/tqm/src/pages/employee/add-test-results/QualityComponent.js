import React, { useState, useEffect } from "react";
import { CardLabel, LabelFieldPair, Toast, TextInput, LinkButton, CardLabelError, MobileNumber, DatePicker, Loader, Header, ImageUploadHandler, UploadFile, MultiUploadWrapper } from "@egovernments/digit-ui-react-components";
import { useTranslation } from "react-i18next";
import { useForm, Controller } from "react-hook-form";
import _ from "lodash";

const QualityParameter = ({onSelect,formData }) => {
    const { t } = useTranslation();
    const { control} = useForm();
    const [showComponent, setShowComponent] = useState(false);
    const {plantCode,processCode,stageCode,materialCode} = formData?.TestStandard || {}
    
    const [showToast, setShowToast] = useState(false);
    const tenant = Digit.ULBService.getStateId();
    const [allFieldsDefined, setallFieldsDefined] = useState(false);

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
    

    const { isLoading, data } = Digit.Hooks.tqm.useCustomMDMSV2({
        tenantId: tenant,
        "filters": {
            "plant": plantCode?.plantCode,
            "process": processCode?.process,
            "stage": stageCode?.stage,
            "material": materialCode?.material
        },
        schemaCode: "PQM.TestStandard",
        changeQueryName: `${plantCode?.code}${processCode?.code}${stageCode?.code}${materialCode?.code}`,
        config: {
            enabled: !!allFieldsDefined,
            staleTime: 0,
            cacheTime:0
        }
    })
    const closeToast = () => {
        setTimeout(() => {
            setShowToast(false);
        }, 5000)
    };
    useEffect(() => {
        if (!data) {
            setShowToast(false);
        }
        else if (!data || Object.keys(data).length === 0) {
            // setShowToast({
            //     label: t('TQM_QUALITY_CRITERIA_NOT_PRESENT'),
            //     isWarning: true
            // });
            closeToast();
            // setShowComponent(false);
        }
        // else setShowComponent(true);

        if(plantCode && processCode && stageCode  && materialCode){
            setShowComponent(true)
        }else{
            setShowComponent(false)
        }
    }, [data,formData]);

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

    if(isLoading) {
        return <Loader />
    }
    
    return (
        <div>
            {!showComponent && (
                <React.Fragment>
                    <div className="suitableOption">{t("ES_TQM_CHOOSE_OPTION")}</div>
                </React.Fragment>
            )
            }
            {showComponent && (

                <React.Fragment>
                    <Header> {t("ES_TQM_QUALITY_PARAMETERS")}</Header>
                    {qualityCriteria?.map((criterionList, index) => (
                        <div key={index}>
                            {criterionList.map((criteria, subindex) => (
                                <LabelFieldPair key={subindex}>
                                    <CardLabel style={CardLabelStyle}>{t(Digit.Utils.locale.getTransformedLocale(`${"PQM.TestStandard"}_${criteria}`))} {criterionList?.length === 1 ? "*" : ""}</CardLabel>
                                    
                                    <div className="field">
                                        <Controller
                                            control={control}
                                            name={`QualityParameter.${criteria}`}
                                            rules={{
                                                pattern: /^-?([0-9]+(\.[0-9]{1,2})?|\.[0-9]{1,2})$/,
                                            }}
                                            render={(props) => (
                                                <TextInput
                                                    value={props.value}
                                                    pattern="^-?([0-9]+(\.[0-9]{1,2})?|\.[0-9]{1,2})$"
                                                    title={t("ES_TQM_TEST_FORMAT_TIP")}
                                                    type={"text"}
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
            {showToast && !showComponent && <Toast warning={showToast.isWarning} label={showToast.label} isDleteBtn={"true"} onClose={() => setShowToast(false)} />}


        </div>
    )
}

export default QualityParameter;


