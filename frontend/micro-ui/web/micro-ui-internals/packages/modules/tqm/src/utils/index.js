export default {
  isPlantOperatorLoggedIn: () => {
    return Digit.Utils.didEmployeeHasAtleastOneRole(Digit.Customizations.commonUiConfig.tqmRoleMapping.plant)
  },
  isUlbAdminLoggedIn: () => {
    return Digit.Utils.didEmployeeHasAtleastOneRole(Digit.Customizations.commonUiConfig.tqmRoleMapping.ulb)
  }
}