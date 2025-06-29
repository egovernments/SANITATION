import React, { useContext, useEffect, useState,useMemo } from "react";
import { useForm,useWatch} from "react-hook-form";
import { useTranslation } from "react-i18next";
import { InboxContext } from "../hoc/InboxSearchComposerContext";
import RenderFormFields from "../molecules/RenderFormFields";
import Header from "../atoms/Header";
import LinkLabel from '../atoms/LinkLabel';
import SubmitBar from "../atoms/SubmitBar";
import Toast from "../atoms/Toast";
import { FilterIcon, RefreshIcon } from "./svgindex";
import HorizontalNavV2 from "./HorizontalNavV2";

const setUIConf = (uiConfig) => {
  if(uiConfig.additionalTabs)
    return [{uiConfig},...uiConfig?.additionalTabs]
  return [{uiConfig}]
}

const SearchComponent = ({ uiConfig, header = "", screenType = "search", fullConfig, data,activeLink,setActiveLink,browserSession}) => {
  
  //whenever activeLink changes we'll change uiConfig
  // const [activeLink,setActiveLink] = useState(uiConfig?.configNavItems?.filter(row=>row.activeByDefault)?.[0]?.name)
  const [navConfig,setNavConfig] = useState(uiConfig?.configNavItems)
  const [allUiConfigs,setAllUiConfigs] = useState(setUIConf(uiConfig))
  const { t } = useTranslation();
  const { state, dispatch } = useContext(InboxContext)
  const [showToast,setShowToast] = useState(null)
  let updatedFields = [];
  const {apiDetails} = fullConfig
  const [session,setSession,clearSession] = browserSession || []
  
  if (fullConfig?.postProcessResult){
    //conditions can be added while calling postprocess function to pass different params
    Digit?.Customizations?.[apiDetails?.masterName]?.[apiDetails?.moduleName]?.postProcess(data, uiConfig) 
  }

  const defValuesFromSession = uiConfig?.type === "search" ? session?.searchForm : session?.filterForm
  
  	
  const {
    register,
    handleSubmit,
    setValue,
    getValues,
    reset,
    watch,
    trigger,
    control,
    formState,
    errors,
    setError,
    clearErrors,
    unregister,
  } = useForm({
    defaultValues: uiConfig?.defaultValues,
    // defaultValues: {...uiConfig?.defaultValues,...defValuesFromSession}
    // defaultValues:defaultValuesFromSession
  });
  
  const formData = watch();

  const checkKeyDown = (e) => {
    const keyCode = e.keyCode ? e.keyCode : e.key ? e.key : e.which;
    if (keyCode === 13) {
      // e.preventDefault();
    }
  };

  useEffect(() => {
    updatedFields = Object.values(formState?.dirtyFields)
  }, [formState])

  useEffect(() => {
    clearSearch()
  }, [activeLink])

  useEffect(() => {
    if (state.tableForm.limit > 10 && state.tableForm.offset !== 0) {
      dispatch({
        type: "tableForm",
        state: { ...state.tableForm, offset: 0 },
      });
    }
  }, [state.tableForm.limit]);
  
  const onSubmit = (data) => {
    
    //here -> added a custom validator function, if required add in UICustomizations
    const isAnyError = Digit?.Customizations?.[apiDetails?.masterName]?.[apiDetails?.moduleName]?.customValidationCheck ? Digit?.Customizations?.[apiDetails?.masterName]?.[apiDetails?.moduleName]?.customValidationCheck(data) : false 
    if(isAnyError) {
      setShowToast(isAnyError)
      setTimeout(closeToast,3000)
      return
    }

    if(updatedFields?.length >= (activeLink ? allUiConfigs?.filter(uiConf => activeLink?.name === uiConf.uiConfig.navLink)?.[0]?.uiConfig?.minReqFields : uiConfig?.minReqFields)) {
     // here based on screenType call respective dispatch fn
      dispatch({
        type: uiConfig?.type === "filter" ? "filterForm" : "searchForm",
        state: {
          ...data
        }
      })
      //here reset tableForm as well when search
      dispatch({
        type: "tableForm",
        state: { limit:10,offset:0 }
      })
    } else {
      setShowToast({ warning: true, label: t("ES_COMMON_MIN_SEARCH_CRITERIA_MSG") })
      setTimeout(closeToast, 3000);
    }
  }

  const clearSearch = () => {
    
    reset(uiConfig?.defaultValues)
    dispatch({
      type: uiConfig?.type === "filter"?"clearFilterForm" :"clearSearchForm",
      state: { ...uiConfig?.defaultValues }
      //need to pass form with empty strings 
    })
    //here reset tableForm as well
    dispatch({
      type: "tableForm",
      state: { limit:10,offset:0 }
      //need to pass form with empty strings 
    })
  }

  //call this fn whenever session gets updated
  const setDefaultValues = () => {
    reset({...uiConfig?.defaultValues,...defValuesFromSession})
  }

  //adding this effect because simply setting session to default values is not working
  useEffect(() => {
    setDefaultValues()
  }, [session])
  
 
  const closeToast = () => {
    setShowToast(null);
  }

  const handleFilterRefresh = () => {
    reset(uiConfig?.defaultValues)
    dispatch({
      type: "clearFilterForm",
      state: { ...uiConfig?.defaultValues }
      //need to pass form with empty strings 
    })
  }

  const renderHeader = () => {
    switch(uiConfig?.type) {
      case "filter" : {
        return (
          <div className="filter-header-wrapper">
            <div className="icon-filter"><FilterIcon></FilterIcon></div>
            <div className="label">{t(header)}</div>
            <div className="icon-refresh" onClick={handleFilterRefresh}><RefreshIcon></RefreshIcon></div>
          </div>
        )
      }
      default : {
        return <Header styles={uiConfig?.headerStyle}>{t(header)}</Header>
      }
    }
  }

  return (
    <HorizontalNavV2 configNavItems={navConfig?.length > 0 ? navConfig : []} showNav={navConfig?.length > 0 ? true : false} activeLink={activeLink} setActiveLink={setActiveLink} fromSearchComp={true} horizontalLine={uiConfig?.horizontalLine}>
      <div className={'search-wrapper'}>
        {header && renderHeader()}
        <form onSubmit={handleSubmit(onSubmit)} onKeyDown={(e) => checkKeyDown(e)}>
          <div>
            {uiConfig?.showFormInstruction && <p className="search-instruction-header">{t(uiConfig?.showFormInstruction)}</p>}
            <div className={`search-field-wrapper ${screenType} ${uiConfig?.formClassName ? uiConfig?.formClassName:""}`}>
              <RenderFormFields 
                // fields={uiConfig?.fields} 
                fields={activeLink ? allUiConfigs?.filter(uiConf => activeLink?.name === uiConf.uiConfig.navLink)?.[0]?.uiConfig?.fields : uiConfig?.fields}
                control={control} 
                formData={formData}
                errors={errors}
                register={register}
                setValue={setValue}
                getValues={getValues}
                setError={setError}
                clearErrors={clearErrors}
                labelStyle={{fontSize: "16px"}}
                apiDetails={apiDetails}
                data={data}
              />  
              <div className={`search-button-wrapper ${screenType} ${uiConfig?.type} ${uiConfig?.searchWrapperClassName}`} style={uiConfig?.searchWrapperStyles} >
                { uiConfig?.secondaryLabel && <LinkLabel style={{marginBottom: 0, whiteSpace: 'nowrap'}} onClick={clearSearch}>{t(uiConfig?.secondaryLabel)}</LinkLabel> }
                { uiConfig?.primaryLabel && <SubmitBar label={t(uiConfig?.primaryLabel)} submit="submit" disabled={false}/> }
              </div>
            </div>
          </div> 
        </form>
        { showToast && <Toast 
          error={showToast.error}
          warning={showToast.warning}
          label={t(showToast.label)}
          isDleteBtn={true}
          onClose={closeToast} />
        }
      </div>
    </HorizontalNavV2>
  )
}

export default SearchComponent
