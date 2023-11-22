import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { FormComposer, Toast, Header, FormComposerV2 } from "@egovernments/digit-ui-react-components";
import { useHistory } from "react-router-dom";
import WorkerConfig from "../../configs/WorkerConfig";
import { useQueryClient } from "react-query";

const AddWorker = ({ parentUrl, heading }) => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const stateId = Digit.ULBService.getStateId();
  const [showToast, setShowToast] = useState(null);
  const history = useHistory();
  const queryClient = useQueryClient();
  const { t } = useTranslation();
  const [canSubmit, setSubmitValve] = useState(false);
  const Config = WorkerConfig({ t });

  const { isLoading: isLoading, isError: vendorCreateError, data: updateResponse, error: updateError, mutate } = Digit.Hooks.fsm.useWorkerCreate(tenantId);

  const defaultValues = {};

  const onFormValueChange = (setValue, formData) => {
    if (
      !isNaN(formData?.SelectEmployeePhoneNumber?.mobileNumber?.length) &&
      formData?.SelectEmployeePhoneNumber?.mobileNumber?.length === 10 &&
      formData?.name &&
      formData?.selectGender &&
      formData?.dob &&
      formData?.address?.city &&
      formData?.address?.locality &&
      formData?.skills &&
      formData?.employer &&
      formData?.AddWorkerRoles?.length > 0
    ) {
      setSubmitValve(true);
    } else {
      setSubmitValve(false);
    }
  };

  const closeToast = () => {
    setShowToast(null);
  };

  const onSubmit = (data) => {
    const name = data?.name;
    const mobileNumber = data?.SelectEmployeePhoneNumber?.mobileNumber;
    const gender = data?.selectGender?.code;
    const dob = new Date(`${data.dob}`).getTime() || new Date(`1/1/1970`).getTime();
    const photograph = data?.documents?.img_measurement_book?.[0]?.[1]?.fileStoreId?.fileStoreId || null;
    const pincode = data?.pincode;
    const city = data?.address?.city?.name;
    const locality = data?.address?.locality?.code;
    const doorNo = data?.doorNo;
    const landmark = data?.landmark;
    const skills = data?.skills?.map((i) => {
      return { type: i?.name, level: "UNSKILLED" };
    });
    const employer = data?.employer?.code;
    const vendor = data?.vendor;
    const roleDetails = data?.AddWorkerRoles;
    const restructuredData = [];

    roleDetails.forEach((item) => {
      const restructuredItem = {};
      restructuredItem["FUNCTIONAL_ROLE"] = item.fn_role.code;
      restructuredItem["EMPLOYMENT_TYPE"] = item.emp_Type.name;
      restructuredItem["SYSTEM_ROLE"] = item.sys_role;
      restructuredItem["PLANT"] = item?.plant;
      restructuredData.push(restructuredItem);
    });

    const driverLicenses = roleDetails?.filter((entry) => entry.fn_role && entry.fn_role.code === "Driver" && entry.licenseNo).map((entry) => entry.licenseNo);

    const formData = {
      Individual: {
        tenantId: tenantId,
        name: {
          givenName: name,
        },
        dateOfBirth: dob,
        gender: gender,
        mobileNumber: mobileNumber,
        address: [
          {
            tenantId: tenantId,
            pincode: pincode,
            city: tenantId,
            street: null,
            doorNo: doorNo,
            locality: {
              code: locality,
            },
            landmark: landmark,
          },
        ],
        identifiers: driverLicenses
          ? [
              {
                identifierType: "DRIVING_LICENSE_NUMBER",
                identifierId: driverLicenses?.[0],
              },
            ]
          : null,
        skills: skills,
        photo: photograph,
        additionalFields: {
          fields: restructuredData,
        },
      },
    };

    mutate(formData, {
      onError: (error, variables) => {
        setShowToast({ key: "error", action: error });
        setTimeout(closeToast, 5000);
      },
      onSuccess: (data, variables) => {
        setShowToast({ key: "success", action: "ADD_WORKER" });
        setTimeout(closeToast, 5000);
        queryClient.invalidateQueries("FSM_WORKER_SEARCH");
        setTimeout(() => {
          closeToast();
          history.push(`/${window?.contextPath}/employee/fsm/registry?selectedTabs=WORKER`);
        }, 5000);
      },
    });
  };
  const isMobile = window.Digit.Utils.browser.isMobile();

  return (
    <React.Fragment>
      <div>
        <Header>{t("FSM_REGISTRY_ADD_WORKER_HEADING")}</Header>
      </div>
      <div style={!isMobile ? { marginLeft: "-15px" } : {}}>
        <FormComposerV2
          isDisabled={!canSubmit}
          label={t("ES_COMMON_APPLICATION_SUBMIT")}
          config={Config.filter((i) => !i.hideInEmployee).map((config) => {
            return {
              ...config,
              body: config.body.filter((a) => !a.hideInEmployee),
            };
          })}
          fieldStyle={{ marginRight: 0 }}
          sectionHeaderClassName="fsm-registry"
          onSubmit={onSubmit}
          defaultValues={defaultValues}
          onFormValueChange={onFormValueChange}
          noBreakLine={true}
        />
        {showToast && (
          <Toast
            error={showToast.key === "error" ? true : false}
            label={t(showToast.key === "success" ? `ES_FSM_REGISTRY_${showToast.action}_SUCCESS` : showToast.action)}
            onClose={closeToast}
          />
        )}
      </div>
    </React.Fragment>
  );
};

export default AddWorker;
