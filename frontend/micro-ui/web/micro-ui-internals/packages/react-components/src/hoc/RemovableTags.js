import React,{useState} from 'react';
import RemoveableTag from '../atoms/RemoveableTag';
import { useTranslation } from 'react-i18next';
const RemovableTags = ({
  config,
  browserSession,
  dispatch,
  fields,
  ...props
}) => {
  const {t} = useTranslation()
  const [sessionData, setSessionData, clearSessionData] = browserSession;
  //sessionData will be the single source of truth for showing removable tags
  //on click of tags we'll call dispatch and update the state which in turns updates the browser session
  
  // console.log('fields',fields);
  //filetering the fields
  
  const fieldsToShow = fields
    ?.filter((row) => row?.removableTagConf)
    ?.map((row) => row?.removableTagConf);
  // On onclick of removable tag you need to update the state accordingly so that browser session gets updated accordingly
  // map over fields in search and filter section
  // for each field, refer field.removableTagConf

  //this will be an array containing all the info required for every tag
  const removableTags = fieldsToShow?.map((field,idx) => {
    debugger
    const obj = {
      key:idx
    }

    console.log('BS', sessionData);
    //setting the text
    const value = _.get(sessionData,field.sessionJsonPath,"")
    if(!value){
      return ""
    }
    let text = t(field.label) || ""
    if(field?.type === "multi"){
      value?.forEach(val => {
        text += ` ${t(Digit.Utils.locale.getTransformedLocale(_.get(val,field.valueJsonPath,"")))} `  
      })
    } else if(field?.type === "single") {
      text = _.get(value,field.valueJsonPath,"")
    }
    obj.text = text
    // debugger
    return obj
  })
  
  console.log("rt",removableTags);

  return (
    // <div>
    //   Removable Tags
    // </div>
    <div className="tag-container">
      {removableTags?.map((tag, index) => {
        return (
          <RemoveableTag
            key={index}
            text={tag?.text}
            onClick={() => {console.log("delete")}}
          />
        );
      })}
    </div>
  );
};

export default RemovableTags;
