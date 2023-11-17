import { useQuery } from "react-query";
import { FSMService } from "../../services/elements/FSM";

const useWorkerSearch = (args) => {
  const { tenantId, filters, params, config } = args;
  return useQuery(["FSM_WORKER_SEARCH", filters], () => FSMService.individualSearch({ tenantId, filters, params }), config);
};

export default useWorkerSearch;
