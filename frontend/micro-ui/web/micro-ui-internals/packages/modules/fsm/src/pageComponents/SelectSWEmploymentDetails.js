import React, { useEffect, useState } from "react";
import { CardLabel, Dropdown, LabelFieldPair, RadioButtons } from "@egovernments/digit-ui-react-components";

const SelectSWEmploymentDetails = ({ t, config, onSelect, userType, formData, setValue }) => {
  const stateId = Digit.ULBService.getStateId();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [selectedEmployer, setSelectedEmployer] = useState(null);
  const [selectedVendor, setSelectedVendor] = useState(null);
  const { isLoading: ismdms, data: mdmsOptions } = Digit.Hooks.useCustomMDMS(
    stateId,
    "FSM",
    [
      {
        name: "SanitationWorkerEmployer",
      },
    ],
    {
      select: (data) => {
        return data?.FSM;
      },
    }
  );

  useEffect(() => {
    if (mdmsOptions && formData[config.key]?.employer) {
      const temp = mdmsOptions?.SanitationWorkerEmployer.find((i) => i.code === formData[config.key]?.employer.name);
      setSelectedEmployer(temp);
    }
  }, [mdmsOptions]);
  const requestCriteria = {
    url: "/vendor/v1/_search",
    params: { tenantId, sortBy: "name", status: "ACTIVE" },
    body: {},
    config: {
      enabled: true,
      select: (data) => {
        return data?.vendor;
      },
    },
  };

  const { isLoading: isVendorLoading, data } = Digit.Hooks.useCustomAPIHook(requestCriteria);
  const selectVendor = (type) => {
    setSelectedVendor(type);
    onSelect(config.key, { ...formData[config.key], vendor: type });
  };

  const selectEmployer = (type) => {
    setSelectedEmployer(type);
    onSelect(config.key, { ...formData[config.key], employer: type });
  };

  return (
    <div>
      <LabelFieldPair>
        <CardLabel className="card-label-smaller">
          {t("FSM_REGISTRY_WORKER_EMPLOYER")}
          {config.isMandatory ? " * " : null}
        </CardLabel>
        <RadioButtons
          style={{ display: "flex", gap: "5rem" }}
          onSelect={(d) => selectEmployer(d)}
          selectedOption={selectedEmployer || formData[config.key]?.employer}
          optionsKey="code"
          options={mdmsOptions?.SanitationWorkerEmployer}
        />
      </LabelFieldPair>
      {selectedEmployer?.code !== "ULB" && (
        <LabelFieldPair>
          <CardLabel className="card-label-smaller">
            {t("FSM_REGISTRY_WORKER_SELECT_VENDOR")}
            {config.isMandatory ? " * " : null}
          </CardLabel>
          <Dropdown
            className="form-field"
            isMandatory
            selected={selectedVendor || formData[config.key]?.vendor}
            disable={false}
            option={data}
            select={selectVendor}
            optionKey="name"
            t={t}
          />
        </LabelFieldPair>
      )}
    </div>
  );
};

export default SelectSWEmploymentDetails;
