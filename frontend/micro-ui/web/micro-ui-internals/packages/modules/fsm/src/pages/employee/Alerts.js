import React, { useState, useCallback, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { CardHeader, Header, Loader } from "@egovernments/digit-ui-react-components";
import DesktopInbox from "../../components/DesktopInbox";
import MobileInbox from "../../components/MobileInbox";
import { Link, useHistory, useLocation } from "react-router-dom";

const config = {
  select: (response) => {
    return {
      totalCount: response?.totalCount,
      vehicleLog: response?.vehicleTrip.map((trip) => {
        const owner = trip.tripOwner;
        const displayName = owner.name;
        const tripOwner = { ...owner, displayName };
        return { ...trip, tripOwner };
      }),
    };
  },
};

const Alerts = () => {
  // const history = useHistory();
  const { t } = useTranslation();
  // const location = useLocation();
  // const vehicleNumber = location.pathname.split("/").at(-1);
  const tenantId = Digit.ULBService.getCurrentTenantId();
  // const [searchParams, setSearchParams] = useState({ applicationStatus: "WAITING_FOR_DISPOSAL" });
  const [searchParamsApplication, setSearchParamsApplication] = useState(null);
  // const [filterParam, setFilterParam] = useState();
  // const [sortParams, setSortParams] = useState([{ id: "applicationNo", desc: true }]);
  // const [pageOffset, setPageOffset] = useState(0);
  // const [pageSize, setPageSize] = useState(100);
  // const [isVehicleSearchCompleted, setIsVehicleSearchCompleted] = useState(false);
  // const [tripDetail, setTripDetail] = useState(null);
  // const userInfo = Digit.UserService.getUser();
  let isMobile = window.Digit.Utils.browser.isMobile();

  // const { isLoading: isVehiclesLoading, data: vehicles } = Digit.Hooks.fsm.useVehiclesSearch({
  //   tenantId,
  //   filters: { registrationNumber: vehicleNumber },
  //   config: { enabled: vehicleNumber?.length > 0 },
  // });

  let filters = {
    //   businessService: "FSM_VEHICLE_TRIP",
    //   vehicleIds: vehicles !== undefined && vehicles?.vehicle[0]?.id.length > 0 ? vehicles?.vehicle[0]?.id || "null" : "null",
    //   applicationStatus: searchParams?.applicationStatus,
    // sortOrder: sortParams[0]?.desc === false ? "ASC" : "DESC",
  };

  // const { isLoading, data: { totalCount, vehicleLog } = {}, isSuccess } = Digit.Hooks.fsm.useVehicleSearch({
  //   tenantId,
  //   filters,
  //   config,
  //   options: { searchWithDSO: true },
  // });
  const [alerts, setAlerts] = useState(null);

  const alertsData = Digit.Hooks.fsm.useAlertsSearch();
  const { isLoading: isSearchLoading, data: { totalCount, vehicleLog } = {}, isSuccess } = Digit.Hooks.fsm.useVehicleSearch({
    tenantId,
    filters: searchParamsApplication,
    config,
    options: { searchWithDSO: false },
  });
  // const { isLoading: isSearchLoading, isIdle, data: { data: { table: ApplicationData } = {}, totalCount } = {} } = Digit.Hooks.fsm.useSearchAll(tenantId, searchParamsApplication, null);

  useEffect(() => {
    alertsData
      .then((result) => {
        setAlerts(result);
      })
      .catch((error) => {
        console.error("Error fetching alerts:", error);
      });
  }, []);

  useEffect(() => {
    if (alerts) {
      const applicationNos = alerts?.map((i) => i?.applicationNo).join(",");
      setSearchParamsApplication({
        refernceNos: applicationNos ? applicationNos : "null",
        // sortOrder: sortParams[0]?.desc === false ? "ASC" : "DESC",
      });
    }
  }, [alerts, isSearchLoading]);


  // let applicationNoList = [];
  // vehicleLog?.map((i) => {
  //   applicationNoList.push(i?.tripDetails[0]?.referenceNo);
  // });

  const onSearch = (params = {}) => {
    const isSearch = params?.applicationNos?.length > 0 ? true : false;
    const filterAlerts = isSearch ? alerts.filter((i) => i.applicationNo === params.applicationNos) : null;
    isSearch && filterAlerts !== null ? setAlerts(filterAlerts) : null;
  };

  // const fetchNextPage = () => {
  //   setPageOffset((prevState) => prevState + pageSize);
  // };

  // const fetchPrevPage = () => {
  //   setPageOffset((prevState) => prevState - pageSize);
  // };

  // const handlePageSizeChange = (e) => {
  //   setPageSize(Number(e.target.value));
  // };

  // const handleFilterChange = (filterParam) => {
  //   // filtering application based on locality from UI side
  //   let tempTripDetails = tripDetails;
  //   let filterTripDetails = tempTripDetails.filter((i) => i?.address?.locality?.name.includes(filterParam?.locality?.[0]?.name ? filterParam?.locality?.[0].name : ""));
  //   setTripDetail(filterTripDetails);
  //   setFilterParam(filterParam);
  // };

  const searchFields = [
    {
      label: t("ES_SEARCH_APPLICATION_APPLICATION_NO"),
      name: "applicationNos",
    },
  ];

  const handleSort = useCallback((args) => {
    if (args?.length === 0) return;
    setSortParams(args);
  }, []);

  // if (isSuccess && totalCount === 0 && !isLoading) {
  //     history.push('/digit-ui/employee/fsm/fstp/new-vehicle-entry/')
  // }

  // if (isLoading && !isSuccess && isSearchLoading && isVehiclesLoading && !isIdle && !isVehicleSearchCompleted) {
  //   return <Loader />;
  // }

  // if (vehicleLog?.length === 0 && tripDetails?.length === 0 && isSuccess && !isSearchLoading && tripDetail?.length === 0 && !isVehiclesLoading) {
  //   history.push(`/${window?.contextPath}/employee/fsm/fstp/new-vehicle-entry/${vehicleNumber}`);
  // }

  // let citizenInfo = [];
  // tripDetail?.map((vehicle) => {
  //   citizenInfo.push(vehicleLog?.find((i) => i?.tripDetails[0]?.referenceNo === vehicle?.applicationNo));
  // });

  if (isMobile) {
    return (
      <div>
        {/* <Header>{t("ES_FSM_ALERTS")}</Header>
        <MobileInbox
          onFilterChange={handleFilterChange}
          vehicleLog={vehicleLog}
          fstprequest={tripDetail}
          isFSMRequest={true}
          isLoading={isLoading}
          userRole={"FSM_ALERTS"}
          linkPrefix={`/${window?.contextPath}/employee/fsm/vehicle-tracking/alerts/`}
          onSearch={onSearch}
          searchFields={searchFields}
          onSort={handleSort}
          sortParams={sortParams}
        /> */}
      </div>
    );
  } else {
    return (
      <div>
        <Header>{t("ES_FSM_ALERTS")}</Header>
        <DesktopInbox
          data={{ table: vehicleLog }}
          alertsData={alerts}
          isLoading={isSearchLoading || !alerts}
          onSort={handleSort}
          isAlertsData={true}
          disableSort={true}
          // sortParams={sortParams}
          userRole={"FSM_ALERTS"}
          // onFilterChange={handleFilterChange}
          searchFields={searchFields}
          onSearch={onSearch}
          // onNextPage={fetchNextPage}
          // onPrevPage={fetchPrevPage}
          // currentPage={Math.floor(pageOffset / pageSize)}
          // pageSizeLimit={pageSize}
          // onPageSizeChange={handlePageSizeChange}
          totalRecords={0}
          isPaginationRequired={false}
          // searchParams={filterParam}
        />
      </div>
    );
  }
  // }
};

export default Alerts;
