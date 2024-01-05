import { useQuery } from "react-query";
import { FSMService } from "../../services/elements/FSM";

const useWorkerSearch = (args) => {
  const { tenantId, params, details, config } = args;
  return useQuery(["FSM_WORKER_SEARCH", details], () => FSMService.workerSearch({ tenantId, params, details }), config);
};

export default useWorkerSearch;
