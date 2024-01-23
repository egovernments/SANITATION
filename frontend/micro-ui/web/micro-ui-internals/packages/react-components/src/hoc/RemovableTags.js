import React, { useState, useEffect, useContext } from 'react';
import RemoveableTagNew from '../atoms/RemovableTagNew';
import { useTranslation } from 'react-i18next';
import { Loader } from '../atoms/Loader';
import { InboxContext } from './InboxSearchComposerContext';
import _ from "lodash";

const generateTagsFromFields = (fields, sessionData, t) => {
  //filetering the fields

  const fieldsToShow = fields
    ?.filter((row) => row?.removableTagConf)
    ?.map((row) => row?.removableTagConf);

  const crumbs = [];
  fieldsToShow?.forEach((field, idx) => {
    //one field can have multiple crumbs
    // we need to fill

    //setting the text
    const value = _.get(sessionData, field.sessionJsonPath, '');
    if (!value || value?.length === 0) {
      return;
    }

    //convert this to switch case and write a separate fn for it
    switch (field?.type) {
      case 'multi':
        value?.forEach((val) => {
          crumbs?.push({
            label: t(field.label) || '',
            value: `${t(
              Digit.Utils.locale.getTransformedLocale(
                _.get(val, field.valueJsonPath, '')
              )
            )}`,
            removableTagConf: {
              ...field,
              value: val,
            },
          });
        });
        break;
      case 'single':
        if(_.get(value, field.valueJsonPath, '')){
        crumbs?.push({
          label: t(field.label) || '',
          value: `${t(
            Digit.Utils.locale.getTransformedLocale(
              _.get(value, field.valueJsonPath, '')
            )
          )}`,
          removableTagConf: { ...field, value },
        });
      }else if(typeof value === "string" && value){
        crumbs?.push({
          label: t(field.label) || '',
          value: `${t(
            Digit.Utils.locale.getTransformedLocale(
              value
            )
          )}`,
          removableTagConf: { ...field, value },
        });
      }
        break;
      case 'dateRange':
        if(_.get(value, field.valueJsonPath, '')){
        crumbs?.push({
          label: t(field.label) || '',
          value: _.get(value, field.valueJsonPath, ''),
          removableTagConf: { ...field, value },
        });
      }
        break;
      default:
        break;
    }

    // if (field?.type === 'multi') {
    //   value?.forEach((val) => {
    //     crumbs?.push({
    //       label: t(field.label) || '',
    //       value: `${t(
    //         Digit.Utils.locale.getTransformedLocale(
    //           _.get(val, field.valueJsonPath, '')
    //         )
    //       )}`,
    //       removableTagConf:{
    //         ...field,
    //         value:val
    //       }
    //     });
    //   });
    // } else if (field?.type === 'single') {
    //   crumbs?.push({
    //     label: t(field.label) || '',
    //     value: `${t(
    //       Digit.Utils.locale.getTransformedLocale(
    //         _.get(val, field.valueJsonPath, '')
    //       )
    //     )}`,
    //     removableTagConf:{...field,value:val}
    //   });
    // }
  });

  return crumbs;
};

const RemovableTags = ({ config, browserSession, fields, ...props }) => {
  const { t } = useTranslation();
  const [sessionData, setSessionData, clearSessionData] = browserSession;
  
  const [removableTags, setRemovableTags] = useState([]);
  const { state, dispatch } = useContext(InboxContext);

  //sessionData will be the single source of truth for showing removable tags
  //on click of tags we'll call dispatch and update the state which in turns updates the browser session

  // On onclick of removable tag you need to update the state accordingly so that browser session gets updated accordingly
  // map over fields in search and filter section
  // for each field, refer field.removableTagConf

  //an effect for generating selected filter tags
  useEffect(() => {
    setRemovableTags(generateTagsFromFields(fields, sessionData, t));
    return () => {};
  }, [fields, sessionData]);


  // function to handle deletion of tags
  //flow -> onClick -> Update state(dispatch with jsonPath) -> session gets updated automatically due to effect in parent
  const handleCrumbDeletion = (tag) => {
    dispatch(
      {
        type:"jsonPath",
        tag
      }
    )
  };

  if (removableTags?.length === 0) {
    return null;
  }

  return (
    <div className="inbox-tag-container">
      {removableTags?.map((tag, index) => {
        return (
          <RemoveableTagNew
            key={index}
            text={tag}
            onClick={() => {
              handleCrumbDeletion(tag);
            }}
          />
        );
      })}
    </div>
  );
};

export default RemovableTags;
