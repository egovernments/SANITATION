import Urls from "../atoms/urls";
import { ServiceRequest } from "../atoms/Utils/Request";

export const LocationService = {
  getLocalities: (tenantId) => {
    return ServiceRequest({
      serviceName: "getLocalities",
      url: Urls.location.localities,
      params: { tenantId: tenantId },
      useCache: true,
    });
  },
  getRevenueLocalities: async (tenantId) => {
    const response = await ServiceRequest({
      serviceName: "getRevenueLocalities",
      url: Urls.boundaryService,
      params: { tenantId: tenantId,hierarchyType:"REVENUE-LOCALITY",boundaryType:"Locality",includeChildren:true },
      useCache: true,
    });
    return response;
  },
  getGramPanchayats: async (tenantId) => {
    const response = await ServiceRequest({
      serviceName: "getGramPanchayats",
      url: Urls.boundaryService,
      params: { tenantId: tenantId,hierarchyType:'REVENUE-GP',boundaryType:'GP',includeChildren:true },
      
      useCache: true,
    });
    return response;
  },
};
