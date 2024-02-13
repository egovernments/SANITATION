import utils from '../utils';
import { useCustomMDMSV2 } from './useCustomMDMSV2';
import { useViewTestResults } from './useViewTestResults';
import { useViewTestSummary } from './useViewTestSummary';
import { useSearchTest } from './useSearchTest';
import useTestUpdate from './useTestUpdate';
import useCreateTest from './useCreate';
import useRouteSubscription from './useRouteSubscription';

const tqm = {
  sampleTQMHook: () => {},
  useViewTestResults,
  useViewTestSummary,
  useCustomMDMSV2,
  useSearchTest,
  useTestUpdate,
  useCreateTest,
  useRouteSubscription,
};

const fsm = {
  useRouteSubscription,
};

const Hooks = {
  tqm,
  fsm,
};

const Utils = {
  browser: {
    sample: () => {},
  },
  tqm: {
    ...utils,
    sampleUtil: () => {},
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
};
