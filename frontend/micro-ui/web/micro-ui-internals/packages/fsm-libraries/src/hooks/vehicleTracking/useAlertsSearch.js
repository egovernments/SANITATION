import { useQuery } from "react-query";
import { FSMService } from "../../services/elements/FSM";

const useAlertsSearch = async (args) => {
  const { tenantId, filters, config } = args;
  const response = await fetch("http://167.71.225.156:8080/api/v3/trip/_alerts");
  const alerts = await response.json();
  return alerts;
  // return useQuery(["FSM_VEHICLE_ALERTS", filters], () => FSMService.alertsSearch(tenantId, filters), config);
};

export default useAlertsSearch;
