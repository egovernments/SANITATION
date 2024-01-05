import { Hamburger, Dropdown } from '@egovernments/digit-ui-react-components';
import React from 'react';
import ChangePlant from './ChangePlant';
const TextToImg = (props) => (
  <span className="user-img-txt" onClick={props.toggleMenu} title={props.name}>
    {props?.name?.[0]?.toUpperCase()}
  </span>
);

const TqmTopBar = ({
  t,
  stateInfo,
  toggleSidebar,
  isSidebarOpen,
  handleLogout,
  userDetails,
  CITIZEN,
  cityDetails,
  mobileView,
  userOptions,
  handleUserDropdownSelection,
  logoUrl,
  showLanguageChange,
  loggedin,
  ...props
}) => {
  //steps 1-> check whether plant operator is logged in, if no then render the default
  // 2-> if yes, then for mobileView render dropdown of plants and by default select the plant this user belongs to
  // 3-> if yes, for webview change the city dropdown to plant dropdown
  const [profilePic, setProfilePic] = React.useState(null);

  React.useEffect(async () => {
    const tenant = Digit.ULBService.getCurrentTenantId();
    const uuid = userDetails?.info?.uuid;
    if (uuid) {
      const usersResponse = await Digit.UserService.userSearch(
        tenant,
        { uuid: [uuid] },
        {}
      );
      if (usersResponse && usersResponse.user && usersResponse.user.length) {
        const userDetails = usersResponse.user[0];
        const thumbs = userDetails?.photo?.split(',');
        setProfilePic(thumbs?.at(0));
      }
    }
  }, [profilePic !== null, userDetails?.info?.uuid]);

  const ChangeCity = Digit.ComponentRegistryService?.getComponent('ChangeCity');
  const ChangeLanguage = Digit.ComponentRegistryService?.getComponent(
    'ChangeLanguage'
  );

  const isPlantOperatorLoggedIn = Digit.Utils.tqm.isPlantOperatorLoggedIn();

  if (isPlantOperatorLoggedIn && mobileView) {
    return (
      <div className="topbar topbar-custom-tqm-container">
        <Hamburger handleClick={toggleSidebar} color="#9E9E9E" />
        <img
          className="city"
          src={loggedin ? cityDetails?.logoId : stateInfo?.statelogo}
        />
        <span
          style={{
            display: 'flex',
            alignItems: 'center',
            // justifyContent: 'space-between',
            width: '100%',
          }}
        >
          { (
            <div
              className={
                mobileView ? 'right' : 'flex-right right w-80 column-gap-15'
              }
              style={!loggedin ? { width: '80%' } : {}}
            >
              <div className="left">
                {!window.location.href.includes('employee/user/login') &&
                  !window.location.href.includes(
                    'employee/user/language-selection'
                  ) && <ChangePlant mobileView={mobileView} />}
              </div>
            </div>
          )}
        </span>
      </div>
    );
  }
  if (isPlantOperatorLoggedIn && !mobileView) {
    return (
      <div className="topbar">
        {mobileView ? (
          <Hamburger handleClick={toggleSidebar} color="#9E9E9E" />
        ) : null}
        <img
          className="city"
          src={loggedin ? cityDetails?.logoId : stateInfo?.statelogo}
        />
        <span
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            width: '100%',
          }}
        >
          {loggedin &&
            (cityDetails?.city?.ulbGrade ? (
              <p
                className="ulb"
                style={
                  mobileView
                    ? { fontSize: '14px', display: 'inline-block' }
                    : {}
                }
              >
                {t(cityDetails?.i18nKey).toUpperCase()}{' '}
                {t(
                  `ULBGRADE_${cityDetails?.city?.ulbGrade
                    .toUpperCase()
                    .replace(' ', '_')
                    .replace('.', '_')}`
                ).toUpperCase()}
              </p>
            ) : (
              <img className="state" src={logoUrl} />
            ))}
          {!loggedin && (
            <p
              className="ulb"
              style={
                mobileView ? { fontSize: '14px', display: 'inline-block' } : {}
              }
            >
              {t(`MYCITY_${stateInfo?.code?.toUpperCase()}_LABEL`)}{' '}
              {t(`MYCITY_STATECODE_LABEL`)}
            </p>
          )}
          {!mobileView && (
            <div
              className={
                mobileView ? 'right' : 'flex-right right w-80 column-gap-15'
              }
              style={!loggedin ? { width: '80%' } : {}}
            >
              <div className="left">
                {!window.location.href.includes('employee/user/login') &&
                  !window.location.href.includes(
                    'employee/user/language-selection'
                  ) && <ChangePlant mobileView={mobileView} />}
              </div>
              <div className="left">
                {showLanguageChange && <ChangeLanguage dropdown={true} />}
              </div>
              {userDetails?.access_token && (
                <div className="left">
                  <Dropdown
                    option={userOptions}
                    optionKey={'name'}
                    select={handleUserDropdownSelection}
                    showArrow={true}
                    freeze={true}
                    style={mobileView ? { right: 0 } : {}}
                    optionCardStyles={{ overflow: 'revert', display: 'table' }}
                    topbarOptionsClassName={'topbarOptionsClassName'}
                    customSelector={
                      profilePic == null ? (
                        <TextToImg
                          name={
                            userDetails?.info?.name ||
                            userDetails?.info?.userInfo?.name ||
                            'Employee'
                          }
                        />
                      ) : (
                        <img
                          src={profilePic}
                          style={{
                            height: '48px',
                            width: '48px',
                            borderRadius: '50%',
                          }}
                        />
                      )
                    }
                  />
                </div>
              )}
              <img className="state" src={logoUrl} />
            </div>
          )}
        </span>
      </div>
    );
  }

  return (
    <div className="topbar">
      {mobileView ? (
        <Hamburger handleClick={toggleSidebar} color="#9E9E9E" />
      ) : null}
      <img
        className="city"
        src={loggedin ? cityDetails?.logoId : stateInfo?.statelogo}
      />
      <span
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          width: '100%',
        }}
      >
        {loggedin &&
          (cityDetails?.city?.ulbGrade ? (
            <p
              className="ulb"
              style={
                mobileView ? { fontSize: '14px', display: 'inline-block' } : {}
              }
            >
              {t(cityDetails?.i18nKey).toUpperCase()}{' '}
              {t(
                `ULBGRADE_${cityDetails?.city?.ulbGrade
                  .toUpperCase()
                  .replace(' ', '_')
                  .replace('.', '_')}`
              ).toUpperCase()}
            </p>
          ) : (
            <img className="state" src={logoUrl} />
          ))}
        {!loggedin && (
          <p
            className="ulb"
            style={
              mobileView ? { fontSize: '14px', display: 'inline-block' } : {}
            }
          >
            {t(`MYCITY_${stateInfo?.code?.toUpperCase()}_LABEL`)}{' '}
            {t(`MYCITY_STATECODE_LABEL`)}
          </p>
        )}
        {!mobileView && (
          <div
            className={
              mobileView ? 'right' : 'flex-right right w-80 column-gap-15'
            }
            style={!loggedin ? { width: '80%' } : {}}
          >
            <div className="left">
              {!window.location.href.includes('employee/user/login') &&
                !window.location.href.includes(
                  'employee/user/language-selection'
                ) && <ChangeCity dropdown={true} t={t} />}
            </div>
            <div className="left">
              {showLanguageChange && <ChangeLanguage dropdown={true} />}
            </div>
            {userDetails?.access_token && (
              <div className="left">
                <Dropdown
                  option={userOptions}
                  optionKey={'name'}
                  select={handleUserDropdownSelection}
                  showArrow={true}
                  freeze={true}
                  style={mobileView ? { right: 0 } : {}}
                  optionCardStyles={{ overflow: 'revert', display: 'table' }}
                  topbarOptionsClassName={'topbarOptionsClassName'}
                  customSelector={
                    profilePic == null ? (
                      <TextToImg
                        name={
                          userDetails?.info?.name ||
                          userDetails?.info?.userInfo?.name ||
                          'Employee'
                        }
                      />
                    ) : (
                      <img
                        src={profilePic}
                        style={{
                          height: '48px',
                          width: '48px',
                          borderRadius: '50%',
                        }}
                      />
                    )
                  }
                />
              </div>
            )}
            <img className="state" src={logoUrl} />
          </div>
        )}
      </span>
    </div>
  );
};

export default TqmTopBar;
