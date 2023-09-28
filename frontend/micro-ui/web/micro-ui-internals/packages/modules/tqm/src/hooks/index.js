
import utils from "../utils";


const tqm = {
  sampleTQMHook: ()=> {}
};

const Hooks = {
  tqm
};

const Utils = {
  browser: {
    sample: () => {},
  },
  tqm: {
    ...utils,
    sampleUtil:()=> {}
  },
};

export const CustomisedHooks = {
  Hooks,
  Utils,
};
