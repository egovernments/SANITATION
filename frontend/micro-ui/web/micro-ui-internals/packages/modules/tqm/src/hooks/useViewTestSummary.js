import { useQuery } from "react-query";
import { tqmService } from "./services/tqmService";

export const useViewTestSummary = ({ t, id, config = {} }) => {
  return useQuery(["TQM_ADMIN_TEST_RESULTS"], () => tqmService.viewTestSummary({ t, id }), config);
};
