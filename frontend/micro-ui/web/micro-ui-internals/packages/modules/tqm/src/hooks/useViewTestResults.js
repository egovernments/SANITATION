import { useQuery } from "react-query";
import { searchTestResultData } from "./services/searchTestResultData";

export const useViewTestResults = ({ id, config = {} }) => {
  return useQuery(["TQM_ADMIN_TEST_RESULTS"], () => searchTestResultData({ id }), config);
};
