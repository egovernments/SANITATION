import { useQuery } from "react-query";
import WorkerDetails from "../../services/molecules/FSM/WorkerDetails";

const useWorkerDetails = (args) => {
  const { tenantId, params, details, config } = args;
  return useQuery(["DRIVER_SEARCH", details], () => WorkerDetails({ tenantId, params, details }), config);
};

export default useWorkerDetails;
