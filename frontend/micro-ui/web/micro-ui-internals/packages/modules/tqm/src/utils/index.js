export default {
  isPlantOperatorLoggedIn: () => {
    return Digit.Utils.didEmployeeHasAtleastOneRole(Digit.Customizations.commonUiConfig.tqmRoleMapping?.plant)
  },
  isUlbAdminLoggedIn: () => {
    return Digit.Utils.didEmployeeHasAtleastOneRole(Digit.Customizations.commonUiConfig.tqmRoleMapping?.ulb)
  },
  convertDateRangeToEpochObj: (dateRange) => {
    //null check
    if(!dateRange || Object.keys(dateRange).length===0){
      return null
    }
    //convert to epoch and send {fromDate,toDate}
    const {range:{startDate,endDate}} = dateRange
    const fromDate = new Date(startDate).getTime()
    const toDate = new Date(endDate).getTime()

    return {
      fromDate,
      toDate
    }
  }
}

export const checkForEmployee = (roles) => {
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const userInfo = Digit.UserService.getUser();
  let rolesArray = [];

  const rolearray = userInfo?.info?.roles.filter((item) => {
    for (let i = 0; i < roles.length; i++) {
      if (item.code == roles[i] && item.tenantId === tenantId) rolesArray.push(true);
    }
  });

  return rolesArray?.length;
};