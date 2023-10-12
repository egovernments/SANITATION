import React, { useMemo, useContext, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import DetailsCard from '../../molecules/DetailsCard';
import { Link } from 'react-router-dom';
import NoResultsFound from '../../atoms/NoResultsFound';
import { Loader } from '../../atoms/Loader';
import _ from 'lodash';
import { useHistory } from 'react-router-dom';

// const sampleSearchResult = [
//   {
//     businessObject:{
//       testId:"AW28929",
//       treatmentProcess:"KA - 25235",
//       stage:"Jagadamba Cleaners",
//       outputType:"KA - 25235",
//       pendingDate:"12/02/2013",
//       status:"Pending results",
//       sla:12
//     }
//   }
// ]

const sampleSearchResult = {
  "responseInfo": null,
  "totalCount": 1,
  "nearingSlaCount": 0,
  "statusMap": [
      {
          "statusid": "8e659b74-b070-4e69-b819-71935dab52dc",
          "count": 1,
          "applicationstatus": null,
          "businessservice": "PQM"
      }
  ],
  "items": [
      {
          "ProcessInstance": {
              "id": "de2ad1d4-9621-4257-b2e8-53d16c25303b",
              "tenantId": "pb.amritsar",
              "businessService": "PQM",
              "businessId": "107-PQM-2023-05-23-000030",
              "action": "PAY",
              "moduleName": "fsm",
              "state": {
                  "auditDetails": null,
                  "uuid": "8e659b74-b070-4e69-b819-71935dab52dc",
                  "tenantId": "pb",
                  "businessServiceId": "287df80c-c80d-45b2-ba47-6e20c4e477d4",
                  "sla": null,
                  "state": "SCHEDULED",
                  "applicationStatus": "SCHEDULED",
                  "docUploadRequired": false,
                  "isStartState": false,
                  "isTerminateState": false,
                  "isStateUpdatable": null,
                  "actions": [
                      {
                          "auditDetails": null,
                          "uuid": "3f87f162-ab43-46e7-82d7-9f7075056135",
                          "tenantId": "pb.amritsar",
                          "currentState": "aff6692e-2e35-48d2-a50d-a6feffd9dcc6",
                          "action": "SUBMIT_SAMPLE",
                          "nextState": "7951ee30-60f7-4e53-8022-f1c760fff797",
                          "roles": [
                              "FSTPO",
                              "ULB_EMP"
                          ],
                          "active": true
                      }
                  ]
              },
              "comment": null,
              "documents": null,
              "assigner": {
                  "id": 211,
                  "userName": "COLLECTOR",
                  "name": "COLLECTOR",
                  "type": "EMPLOYEE",
                  "mobileNumber": "7677895003",
                  "emailId": "",
                  "roles": [
                      {
                          "id": null,
                          "name": "FSM Employee Application Viewer",
                          "code": "FSM_VIEW_EMP",
                          "tenantId": "pb.amritsar"
                      },
                      {
                          "id": null,
                          "name": "Employee",
                          "code": "EMPLOYEE",
                          "tenantId": "pb.amritsar"
                      },
                      {
                          "id": null,
                          "name": "FSM Payment Collector",
                          "code": "FSM_COLLECTOR",
                          "tenantId": "pb.amritsar"
                      }
                  ],
                  "tenantId": "pb.amritsar",
                  "uuid": "6da111ee-ba68-4326-9709-416cc17fdcea"
              },
              "assignes": null,
              "nextActions": [],
              "stateSla": null,
              "businesssServiceSla": -5922910820,
              "previousStatus": null,
              "entity": null,
              "auditDetails": {
                  "createdBy": "6da111ee-ba68-4326-9709-416cc17fdcea",
                  "lastModifiedBy": "6da111ee-ba68-4326-9709-416cc17fdcea",
                  "createdTime": 1685686291274,
                  "lastModifiedTime": 1685686291274
              },
              "rating": 0,
              "assignee": null,
              "escalated": false
          },
          "businessObject": {
              "applicationNo": "107-PQM-2023-05-23-000030",
              "processCode": "123",
              "auditDetails": {
                  "lastModifiedTime": 1684820153185,
                  "createdBy": "2788a23b-476c-4235-84fa-b675e28ea465",
                  "lastModifiedBy": "2788a23b-476c-4235-84fa-b675e28ea465",
                  "createdTime": 1684820153185
              },
              "tenantId": "pb.amritsar",
              "plantCode": "7897897899",
              "serviceSla": -135
          },
          "serviceObject": null
      }
  ]
} 



const convertRowToDetailCardData = (row,config,t,apiDetails,searchResult) => {
  const resultantObj = {
    apiResponse:{...row,hidden:true}
  }

  config.columns.map((column,idx) => {
    resultantObj[t(column.label)] = column.additionalCustomization ? Digit?.Customizations?.[apiDetails?.masterName]?.[apiDetails?.moduleName]?.additionalCustomizations(row,column?.label,column, _.get(row,column.jsonPath,""),t, searchResult) : String(_.get(row,column.jsonPath,"") ? column.translate? t(Digit.Utils.locale.getTransformedLocale(column.prefix?`${column.prefix}${_.get(row,column.jsonPath,"")}`:_.get(row,column.jsonPath,""))) : _.get(row,column.jsonPath,"") : t("ES_COMMON_NA")); 
  })

  return resultantObj
}

const convertDataForDetailsCard = (config,searchResult,t,apiDetails) => {
//map over columns and generate data accordingly

  const result = searchResult?.map((row,idx) => {
    return convertRowToDetailCardData(row,config,t,apiDetails,searchResult)
  } )

  return result
}

const MobileSearchResultsv1 = ({
  config,
  data,
  isLoading,
  isFetching,
  fullConfig,
}) => {
  const { t } = useTranslation();
  const history = useHistory()
  const { apiDetails } = fullConfig;
  const resultsKey = config.resultsJsonPath;

  let searchResult = _.get(data, resultsKey, []);
  
  //for sample result
  // let searchResult = _.get(sampleSearchResult, resultsKey, []);
  
  searchResult = searchResult?.length > 0 ? searchResult : [];
  searchResult = searchResult?.reverse();
  
  const tenantId = Digit.ULBService.getCurrentTenantId();

  const RenderResult = () => {
    const dataForDetailsCard =  convertDataForDetailsCard(config,searchResult,t,apiDetails) 
    const propsForDetailsCard = {
      t,
      data:dataForDetailsCard,
      showActionBar:config?.showActionBarMobileCard, // to show action button on detail card
      submitButtonLabel:config?.actionButtonLabelMobileCard,
      handleDetailCardClick:(obj)=>{ //fn when action button on card is clicked
        const linkToPushTo = Digit?.Customizations?.[apiDetails?.masterName]?.[apiDetails?.moduleName]?.onCardActionClick(obj)
        history.push(linkToPushTo)
      },
      handleSelect:(obj)=>{
       const linkToPushTo = Digit?.Customizations?.[apiDetails?.masterName]?.[apiDetails?.moduleName]?.onCardClick(obj)
       history.push(linkToPushTo)
        // Digit?.Customizations?.[apiDetails?.masterName]?.[apiDetails?.moduleName]?.onCardActionClick(obj)
      }, //fn when card container is clicked
      mode:"tqm",
      apiDetails,
    }
    
    return <DetailsCard {...propsForDetailsCard} />
  }

  if (isLoading || isFetching) {
    return <Loader />;
  }

  if (searchResult?.length === 0) {
    return ( <NoResultsFound/> );
  } 

  return (
    <React.Fragment>
      <RenderResult />
    </React.Fragment>
  );
};

export default MobileSearchResultsv1;
