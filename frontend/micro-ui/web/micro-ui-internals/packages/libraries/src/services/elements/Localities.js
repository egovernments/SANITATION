import { LocalizationService } from "./Localization/service";

const ADMIN_CODE = ({ tenantId, hierarchyType }) => {
  return tenantId.replace(".", "_").toUpperCase();
};

const getI18nKeys = (localitiesWithLocalizationKeys) => {
  return localitiesWithLocalizationKeys.map((locality) => ({
    code: locality.code,
    message: locality.name,
  }));
};

const getLocalities = (tenantBoundry) => {
  console.log(`*** LOG ***`,tenantBoundry);
  const adminCode = ADMIN_CODE(tenantBoundry);
  const localitiesWithLocalizationKeys = tenantBoundry.boundary.map(
    (boundaryObj) => ({
      ...boundaryObj,
      i18nkey: adminCode + "_" + boundaryObj.code,
    })
  );
  console.log(`*** LOG ***`,localitiesWithLocalizationKeys);
  return localitiesWithLocalizationKeys;
};

export const LocalityService = {
  get: (tenantBoundry) => getLocalities(tenantBoundry),
};
