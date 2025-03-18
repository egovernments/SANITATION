import React, { useState } from "react";
import { Loader } from "../atoms/Loader";
import Dropdown from "../atoms/Dropdown";
import { useTranslation } from "react-i18next";

const Localities = ({ selectLocality, tenantId, boundaryType, keepNull, selected, optionCardStyles, className, style, disable, disableLoader, sortFn }) => {
  const { t } = useTranslation();
  const [selectedCity, setSelectedCity] = useState(
    Digit.SessionStorage.get("Employee.tenantId") || null
  );

  function flattenBoundaryData(data) {
    if (!data || !Array.isArray(data)) {
      return [];
    }

    const result = [];

    for (const parent of data) {
      result.push({
        id: parent.id,
        code: parent.code,
        i18nkey: parent.i18nkey,
      });

      if (parent.children && Array.isArray(parent.children)) {
        for (const child of parent.children) {
          result.push({
            id: child.id,
            code: child.code,
            i18nkey: child.i18nkey,
          });
        }
      }
    }

    return result;
  }

  const { data: tenantlocalties, isLoading } = Digit.Hooks.useBoundaryLocalities(tenantId, boundaryType, { enabled: !disable }, t);
  const { data: fetchedGramPanchayats, isLoadingGP } = Digit.Hooks.useBoundaryLocalities(
    selectedCity,
    "gramPanchayats",
    {
      enabled: !!selectedCity,
    },
    t
  );

  if (isLoading && isLoadingGP && !disableLoader) {
    return <Loader />;
  }

  const mergedLocalities = [
    ...(tenantlocalties || []),
    ...(flattenBoundaryData(fetchedGramPanchayats) || []),
  ];


  return (
    <Dropdown
      option={sortFn ? mergedLocalities.sort(sortFn) : mergedLocalities}
      keepNull={keepNull === false ? false : true}
      selected={selected}
      select={selectLocality}
      optionCardStyles={optionCardStyles}
      optionKey="code"
      style={style}
      disable={!mergedLocalities.length || disable}
      className={className}
    />
  );
};

export default Localities;

// import React,{useState} from "react";
// import { Loader } from "../atoms/Loader";
// import Dropdown from "../atoms/Dropdown";
// import { useTranslation } from "react-i18next";

// const Localities = ({ selectLocality, tenantId, boundaryType, keepNull, selected, optionCardStyles, className, style, disable, disableLoader, sortFn }) => {
//   const { t } = useTranslation();
//   const [selectedCity, setSelectedCity] = useState(
//     Digit.SessionStorage.get("Employee.tenantId") || null
//   );
//   function flattenBoundaryData(data) {
//     if (!data || !Array.isArray(data)) {
//       return [];
//     }
  
//     const result = [];
  
//     for (const parent of data) {
//       result.push({
//         id: parent.id,
//         code: parent.code,
//         i18nkey: parent.i18nkey,
//       });
  
//       if (parent.children && Array.isArray(parent.children)) {
//         for (const child of parent.children) {
//           result.push({
//             id: child.id,
//             code: child.code,
//           });
//         }
//       }
//     }
  
//     return result;
//   }

//   const { data: tenantlocalties, isLoading } = Digit.Hooks.useBoundaryLocalities(tenantId, boundaryType, { enabled: !disable }, t);
//   const { data: fetchedGramPanchayats, isLoadingGP } = Digit.Hooks.useBoundaryLocalities(
//     selectedCity,
//     "gramPanchayats",
//     {
//       enabled: !!selectedCity,
//     },
//     t
//   );
 
//   if (isLoading && isLoadingGP && !disableLoader ) {
//     return <Loader />;
//   }


//   console.log(`*** LOG TT***`,flattenBoundaryData(fetchedGramPanchayats));

//   return (
//     <Dropdown
//       option={sortFn ? tenantlocalties?.sort(sortFn) : tenantlocalties}
//       keepNull={keepNull === false ? false : true}
//       selected={selected}
//       select={selectLocality}
//       optionCardStyles={optionCardStyles}
//       optionKey="code"
//       style={style}
//       disable={!tenantlocalties?.length || disable}
//       className={className}
//     />
//   );
//   //  <h1>ABCD</h1>
// };

// export default Localities;
