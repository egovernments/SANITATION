import { useQuery } from "react-query";
import { FSMService } from "../../services/elements/FSM";

const useTripTrack = async (args) => {
  const { tenantId, filters, config } = args;
  const params = new URLSearchParams(filters);
  const options = {
    method: "POST",
  };
  try {
    const response = await fetch(`http://167.71.225.156:8080/api/v3/trip/_searchfsm?${params}`, options);
    const tripData = await response.json();
    return tripData;
  } catch (error) {
    console.error("Error:", error);
  }
  // return useQuery(["FSM_VEHICLE_ALERTS", filters], () => FSMService.alertsSearch(tenantId, filters), config);
};

export default useTripTrack;
