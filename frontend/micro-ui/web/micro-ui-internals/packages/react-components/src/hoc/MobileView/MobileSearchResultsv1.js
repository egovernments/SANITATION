import React, { useMemo, useContext, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import DetailsCard from '../../molecules/DetailsCard';
import { Link } from 'react-router-dom';
import NoResultsFound from '../../atoms/NoResultsFound';
import { Loader } from '../../atoms/Loader';
import _ from 'lodash';

const convertDataForDetailsCard = (config,searchResult) => {
//map over columns and generate data accordingly

}

const MobileSearchResultsv1 = ({
  config,
  data,
  isLoading,
  isFetching,
  fullConfig,
}) => {
  const { t } = useTranslation();

  const sampleData = [
    {
      [t('REGISTER_NAME')]: 'NA',
      [t('REGISTER_ID')]: 'NA',
      [t('REGISTER_ORG_NAME')]: 'NA',
      [t('REGISTER_DATES')]: 'NA',
      [t('REGISTER_STATUS')]: 'NA',
      [t("ATM_MUSTER_ROLL_ID")]: (
        <div>
            <span className="link">
              <Link to={`view-attendance?tenantId=${tenantId}&musterRollNumber=${123}`}>
                { t("ES_COMMON_NA")}
              </Link>
            </span>
        </div> 
    ),
    },
    {
      [t('REGISTER_NAME')]: 'NA',
      [t('REGISTER_ID')]: 'NA',
      [t('REGISTER_ORG_NAME')]: 'NA',
      [t('REGISTER_DATES')]: 'NA',
      [t('REGISTER_STATUS')]: 'NA',
    },
    {
      [t('REGISTER_NAME')]: 'NA',
      [t('REGISTER_ID')]: 'NA',
      [t('REGISTER_ORG_NAME')]: 'NA',
      [t('REGISTER_DATES')]: 'NA',
      [t('REGISTER_STATUS')]: 'NA',
    },
  ];
  const { apiDetails } = fullConfig;
  const resultsKey = config.resultsJsonPath;

  let searchResult = _.get(data, resultsKey, []);
  searchResult = searchResult?.length > 0 ? searchResult : [];
  searchResult = searchResult.reverse();
  const tenantId = Digit.ULBService.getCurrentTenantId();

  const RenderResult = () => {
    const dataForDetailsCard = sampleData || convertDataForDetailsCard(config,searchResult) 
    const propsForDetailsCard = {
      t,
      data:dataForDetailsCard,
      showActionBar:config?.showActionBarMobileCard, // to show action button on detail card
      submitButtonLabel:config?.actionButtonLabelMobileCard,
      handleDetailCardClick:(obj)=>{ //fn when action button on card is clicked
        Digit?.Customizations?.[apiDetails?.masterName]?.[apiDetails?.moduleName]?.onCardActionClick(obj)
      },
      handleSelect:(obj)=>{
        Digit?.Customizations?.[apiDetails?.masterName]?.[apiDetails?.moduleName]?.onCardClick(obj)
      }, //fn when card container is clicked
      mode:"tqm",
      apiDetails,
    }
    
    return <DetailsCard {...propsForDetailsCard} />
  }

  // if (isLoading) {
  //   return <Loader />;
  // }

  return (
    <React.Fragment>
      <RenderResult />
    </React.Fragment>
  );
};

export default MobileSearchResultsv1;
