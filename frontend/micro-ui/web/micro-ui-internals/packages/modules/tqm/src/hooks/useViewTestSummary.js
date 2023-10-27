import { useQuery } from "react-query";
import { tqmService } from "./services/tqmService";

export const useViewTestSummary = ({ t, id, config = {} }) => {
  return useQuery(["TQM_ADMIN_TEST_RESULTS_SUMMARY",id], () => tqmService.viewTestSummary({ t, id }), config);
};
