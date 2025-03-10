import { useQuery } from "react-query";
import { DSSService } from "../../services/elements/DSS";

const useDashoardConfig = (moduleCode) => {
  const tenantInfo =
    Digit.SessionStorage.get("userType") === "citizen"
      ? Digit.ULBService.getStateId()
      : Digit.ULBService.getCurrentTenantId() || Digit.ULBService.getStateId();

  return useQuery(`DSS_DASHBOARD_CONFIG_${moduleCode}`, () =>
    DSSService.getDashboardConfig(moduleCode, tenantInfo)
  );
};

export default useDashoardConfig;
