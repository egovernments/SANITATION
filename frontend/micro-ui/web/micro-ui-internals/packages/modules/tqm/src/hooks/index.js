import utils from "../utils";
import { useCustomMDMSV2 } from "./useCustomMDMSV2";
import { useViewTestResults } from "./useViewTestResults";

const tqm = {
  sampleTQMHook: () => {},
  useViewTestResults,
  useCustomMDMSV2,
};

const Hooks = {
  tqm,
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
