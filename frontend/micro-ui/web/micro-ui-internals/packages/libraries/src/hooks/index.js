import { useInitStore } from "./store";
import useWorkflowDetails from "./workflow";
import useSessionStorage from "./useSessionStorage";
import useQueryParams from "./useQueryParams";
import useDocumentSearch from "./useDocumentSearch";
import useClickOutside from "./useClickOutside";
import {
  useFetchPayment,
  usePaymentUpdate,
  useFetchCitizenBillsForBuissnessService,
  useFetchBillsForBuissnessService,
  useGetPaymentRulesForBusinessServices,
  useDemandSearch,
  useRecieptSearch,
  usePaymentSearch,
  useBulkPdfDetails,
} from "./payment";
import { useUserSearch } from "./userSearch";
import { useApplicationsForBusinessServiceSearch } from "./useApplicationForBillSearch";
import useBoundaryLocalities from "./useLocalities";
import useCommonMDMS from "./useMDMS";
import useCustomMDMS from "./useCustomMDMS";
import useCustomAPIHook from "./useCustomAPIHook";
import useInboxGeneral from "./useInboxGeneral/useInboxGeneral";
import useApplicationStatusGeneral from "./useStatusGeneral";
import useModuleTenants from "./useModuleTenants";
import useStore from "./useStore";
import { useTenants } from "./useTenants";
import useInbox from "./useInbox";
import { useEvents, useClearNotifications, useNotificationCount } from "./events";
import useCreateEvent from "./events/useCreateEvent";
import useUpdateEvent from "./events/useUpdateEvent";
import useNewInboxGeneral from "./useInboxGeneral/useNewInbox";
import useDynamicData from "./useDynamicData";

import useEmployeeSearch from "./useEmployeeSearch";

import useDssMdms from "./dss/useMDMS";
import useDashboardConfig from "./dss/useDashboardConfig";
import useDSSDashboard from "./dss/useDSSDashboard";
import useGetChart from "./dss/useGetChart";

import useHRMSSearch from "./hrms/useHRMSsearch";
import useHrmsMDMS from "./hrms/useHRMSMDMS";
import useHRMSCreate from "./hrms/useHRMScreate";
import useHRMSUpdate from "./hrms/useHRMSUpdate";
import useHRMSCount from "./hrms/useHRMSCount";
import useHRMSGenderMDMS from "./hrms/useHRMSGender";

import useReceiptsSearch from "./receipts/useReceiptsSearch";
import useReceiptsMDMS from "./receipts/useReceiptsMDMS";
import useReceiptsUpdate from "./receipts/useReceiptsUpdate";

import useEventInbox from "./events/useEventInbox";
import useEventDetails from "./events/useEventDetails";
import { useEngagementMDMS } from "./engagement/useMdms";
import useDocSearch from "./engagement/useSearch";
import useDocCreate from "./engagement/useCreate";
import useDocUpdate from "./engagement/useUpdate";
import useDocDelete from "./engagement/useDelete";

import useSurveyCreate from "./surveys/useCreate";
import useSurveyDelete from "./surveys/useDelete";
import useSurveyUpdate from "./surveys/useUpdate";
import useSurveySearch from "./surveys/useSearch";
import useSurveyShowResults from "./surveys/useShowResults";
import useSurveySubmitResponse from "./surveys/useSubmitResponse";
import useSurveyInbox from "./surveys/useSurveyInbox";

import useGetHowItWorksJSON from "./useHowItWorksJSON";
import useGetFAQsJSON from "./useGetFAQsJSON";
import useGetDSSFAQsJSON from "./useGetDSSFAQsJSON";
import useGetDSSAboutJSON from "./useGetDSSAboutJSON";
import useStaticData from "./useStaticData";
import { usePrivacyContext } from "./usePrivacyContext";

const pgr = {
  useComplaintDetails,
  useComplaintsList,
  useComplaintsListByMobile,
  useComplaintStatus,
  useComplaintTable,
  useComplaintTypes,
  useEmployeeFilter,
  useInboxData,
  useLocalities,
  useServiceDefs,
  useTenants: usePGRTenants,
  useComplaintSubType,
  usePropertyMDMS,
  useComplaintStatusCount,
  useTradeLicenseBillingslab,
  useMDMS: usePGRMDMS,
};

const fsm = {
  useTenants: useTenantsFSM,
  useDesludging: useDesludging,
  useMDMS: useMDMS,
  useSearch,
  useRouteSubscription,
  useSearchAll,
  useInbox: useFSMInbox,
  useApplicationUpdate,
  useApplicationStatus,
  useWorkflowData,
  useDsoSearch,
  useApplicationDetail,
  useApplicationActions,
  useApplicationAudit,
  useSearchForAuditData,
  useVehicleSearch,
  useVehicleUpdate,
  useVendorDetail,
  useVehiclesSearch,
  useConfig,
  useSlum,
  usePaymentHistory,
  useVendorCreate,
  useVendorUpdate,
  useVehicleDetails,
  useVehicleCreate,
  useVendorCreate,
  useVendorUpdate,
  useVehicleDetails,
  useVehicleCreate,
  useUpdateVehicle,
  useDriverSearch,
  useDriverCreate,
  useDriverUpdate,
  useDriverDetails,
  useVehicleTripCreate,
  useVendorSearch,
  useAdvanceBalanceCalulation,
};

const pt = {
  usePropertySearch,
  usePropertySearchNew,
  usePropertyPayment,
  usePropertyMDMS,
  usePropertySearchWithDue,
  usePropertyAPI,
  usePropertyCreateNUpdateAPI,
  usePropertyDocumentSearch,
  useTenants: useTenantsPT,
  useApplicationDetail: usePtApplicationDetail,
  useApplicationActions: usePtApplicationActions,
  useMDMS: usePtMDMS,
  usePropertyAssessment,
  usePtCalculationEstimate,
  useGenderMDMS,
  usePTGenderMDMS,
  useMyPropertyPayments,
  useGenericViewProperty,
};

const dss = {
  useMDMS: useDssMdms,
  useDashboardConfig,
  useDSSDashboard,
  useGetChart,
};

const mcollect = {
  useCommonMDMS,
  useMCollectMDMS,
  useMCollectSearch,
  useMcollectSearchBill,
  usemcollectTenants,
  useMCollectCount,
  useMCollectCategory,
  useMCollectCategoryTypes,
  useMCollectTaxHeads,
  useMcollectFormConfig,
};

const hrms = {
  useHRMSSearch,
  useHrmsMDMS,
  useHRMSCreate,
  useHRMSUpdate,
  useHRMSCount,
  useHRMSGenderMDMS,
};
const tl = {
  useTenants: useTenantsTL,
  useTradeLicenseMDMS,
  useTLDocumentSearch,
  useTradeLicenseAPI,
  useTLSearchApplication,
  useTLPaymentHistory,
  useTradeLicenseSearch,
  useTLGenderMDMS,
  useTradeLicenseBillingslab,
  useInbox: useTLInbox,
  useMDMS: useTLMDMS,
  useSearch: useTLSearch,
  useApplicationDetail: useTLApplicationDetail,
  useApplicationActions: useTLApplicationActions,
  useFetchBill: useTLFetchBill,
  useTLApplicationDetails,
  useTLWorkflowData,
};

const receipts = {
  useReceiptsMDMS,
  useReceiptsSearch,
  useReceiptsUpdate,
};

const obps = {
  useMDMS: useOBPSMDMS,
  useScrutinyDetails,
  useTenants: useTenantsOBPS,
  useNocDetails: useNocDetails,
  useOBPSDocumentSearch,
  useObpsAPI,
  useBPADetails,
  useBPASearch,
  useBPAREGgetbill,
  useStakeholderAPI,
  useBPAREGSearch,
  useOCEdcrSearch,
  useLicenseDetails,
  useBPAREGApplicationActions,
  useBPADetailsPage,
  useEmpBPAREGSearch,
  useBPAInbox,
  useEDCRInbox,
  useArchitectInbox,
  SearchMdmsTypes,
  useServiceTypeFromApplicationType,
  useApplicationActions: useBPAApplicationActions,
  useOBPSSearch,
  useBusinessServiceBasedOnServiceType,
  useBusinessServiceData,
  useBPATaxDocuments,
};

const events = {
  useInbox: useEventInbox,
  useCreateEvent,
  useEventDetails,
  useUpdateEvent,
};

const engagement = {
  useMDMS: useEngagementMDMS,
  useDocCreate,
  useDocSearch,
  useDocDelete,
  useDocUpdate,
};

const survey = {
  useCreate: useSurveyCreate,
  useUpdate: useSurveyUpdate,
  useDelete: useSurveyDelete,
  useSearch: useSurveySearch,
  useSubmitResponse: useSurveySubmitResponse,
  useShowResults: useSurveyShowResults,
  useSurveyInbox,
};

const noc = {
  useNOCDetails,
  useNOCApplicationActions,
  useInbox: useNOCInbox,
  useNOCSearchApplication,
};

const ws = {
  WSSearchMdmsTypes,
  usewsTenants,
  useWaterSearch,
  useSewarageSearch,
  useMyBillsWaterSearch,
  useMyBillsSewarageSearch,
  useMyApplicationSearch,
  useWSDocumentSearch,
  useWSMDMSWS,
  WSuseSearch,
  useSewSearch,
  useSearchWS,
  useWSDetailsPage,
  useWSApplicationActions,
  useApplicationActionsBillAmendUpdate,
  useWSConsumptionSearch,
  useConnectionDetail,
  useMDMS: useWSMDMS,
  useWaterCreateAPI,
  useSewerageCreateAPI,
  useWSUpdateAPI,
  useMypaymentWS,
  useCreateBillAmendment,
  useWSApplicationDetailsBillAmendment,
  useInbox: useWSInbox,
  useOldValue,
  useMeterReadingCreateAPI,
  useGetMeterStatusList,
  useGetBillingPeriodValidation,
  useWaterPropertySearch,
  useDisconnectionWorkflow,
  useDisConnectionDetails,
  useWSModifyDetailsPage,
  useToCheckPrivacyEnablement,
  useWSConfigMDMS: useWSConfigMDMS,
};

const reports = {
  useReportMeta,
};

const Hooks = {
  useSessionStorage,
  useQueryParams,
  useFetchPayment,
  usePaymentUpdate,
  useFetchCitizenBillsForBuissnessService,
  useFetchBillsForBuissnessService,
  useGetPaymentRulesForBusinessServices,
  useWorkflowDetails,
  useInitStore,
  useClickOutside,
  useUserSearch,
  useApplicationsForBusinessServiceSearch,
  useDemandSearch,
  useInboxGeneral,
  useEmployeeSearch,
  useBoundaryLocalities,
  useCommonMDMS,
  useApplicationStatusGeneral,
  useModuleTenants,
  useRecieptSearch,
  usePaymentSearch,
  useNewInboxGeneral,
  useEvents,
  useClearNotifications,
  useNotificationCount,
  useStore,
  useDocumentSearch,
  useTenants,
  useInbox: useTLInbox,
  useAccessControl,
  useBillSearch,
  useCancelBill,
  useTenantsBills,
  usePrivacyContext,
  pgr,
  fsm,
  pt,
  dss,
  mcollect,
  hrms,
  tl,
  receipts,
  obps,
  events,
  engagement,
  survey,
  noc,
  ws,
  useCustomMDMS,
  useCustomAPIHook,
  reports,
  useGetHowItWorksJSON,
  useGetFAQsJSON,
  useGetDSSFAQsJSON,
  useGetDSSAboutJSON,
  useStaticData,
  useDynamicData,
  useBulkPdfDetails,
  useBillAmendmentInbox,
  useAudit,
};

export default Hooks;
