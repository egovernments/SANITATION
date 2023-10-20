import { useMutation } from "react-query";
import createService from "./services/createService";
const useCreate = (tenantId) => {
  return useMutation((testData) => createService(testData, tenantId));
};

export default useCreate;
