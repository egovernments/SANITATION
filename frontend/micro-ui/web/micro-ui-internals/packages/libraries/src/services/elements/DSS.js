import Urls from "../atoms/urls";
import { Request } from "../atoms/Utils/Request";

export const DSSService = {
  getDashboardConfig: (moduleCode, tenantInfo) =>
    Request({
      url: Urls.dss.dashboardConfig + `/${moduleCode}`,
      useCache: false,
      userService: false,
      method: "GET",
      authHeader: true,
      params: { tenantId: tenantInfo },
    }),
  getCharts: (data, tenantid) =>
    Request({
      url: Urls.dss.getCharts,
      useCache: false,
      userService: false,
      method: "POST",
      auth: true,
      data,
      params: data.headers,
    }),
};
