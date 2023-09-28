import React, { useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { PDFSvg } from "@egovernments/digit-ui-react-components";

function DocumentsPreview({ documents, svgStyles = {} }) {
  const { t } = useTranslation();
  const tenantId = Digit.ULBService.getCurrentTenantId();
  const [filesArray, setFilesArray] = useState(null);
  const [pdfFiles, setPdfFiles] = useState({});

  useEffect(() => {
    let acc = documents?.map((i) => i.value);
    setFilesArray(acc);
  }, [documents]);

  useEffect(() => {
    if (filesArray?.length) {
      Digit.UploadServices.Filefetch(filesArray, Digit.ULBService.getStateId()).then((res) => {
        setPdfFiles(res?.data);
      });
    }
  }, [filesArray]);

  return (
    <div style={{ marginTop: "19px" }}>
      <div style={{ display: "flex", flexWrap: "wrap", justifyContent: "flex-start" }}>
        {documents.length > 0 ? (
          documents?.map((document, index) => (
            <React.Fragment key={index}>
              <a target="_" href={pdfFiles[document?.value]?.split(",")[0]} style={{ minWidth: "80px", marginRight: "10px", maxWidth: "100px", height: "auto" }} key={index}>
                <div style={{ display: "flex", justifyContent: "center" }}>
                  <PDFSvg />
                </div>
                <p style={{ textAlign: "center" }}>{t(document?.title)}</p>
              </a>
            </React.Fragment>
          ))
        ) : (
          <div>
            <p>{t("ES_TQM_NO_DOCUMENTS_AVAILABLE")}</p>
          </div>
        )}
      </div>
    </div>
  );
  // 🚧 WIP: DONT REMOVE IT 👇
  //   return (
  //     <div style={{ marginTop: "19px" }}>
  //       {documents?.map((document, index) => (
  //         <React.Fragment key={index}>
  //           {document?.title ? <CardSubHeader style={{ marginTop: "32px", marginBottom: "8px", color: "#505A5F", fontSize: "24px" }}>{t(document?.title)}</CardSubHeader> : null}
  //           <div style={{ display: "flex", flexWrap: "wrap", justifyContent: "flex-start" }}>
  //             {document?.values && document?.values.length > 0 ? (
  //               document?.values?.map((value, index) => (
  //                 <a target="_" href={pdfFiles[value.fileStoreId]?.split(",")[0]} style={{ minWidth: "80px", marginRight: "10px", maxWidth: "100px", height: "auto" }} key={index}>
  //                   <div style={{ display: "flex", justifyContent: "center" }}>
  //                     <PDFSvg />
  //                   </div>
  //                   <p
  //                     style={
  //                       checkLocation ? { marginTop: "8px", fontWeight: "bold", fontSize: "16px", lineHeight: "19px", color: "#505A5F", textAlign: "center" } : { marginTop: "8px", fontWeight: "bold" }
  //                     }
  //                   >
  //                     {t(value?.title)}
  //                   </p>
  //                   <p style={{ textAlign: "center" }}>{t(value?.documentType)}</p>
  //                 </a>
  //               ))
  //             ) : (
  //               <div>
  //                 <p>{t("BPA_NO_DOCUMENTS_UPLOADED_LABEL")}</p>
  //               </div>
  //             )}
  //           </div>
  //         </React.Fragment>
  //       ))}
  //     </div>
  //   );
}

export default DocumentsPreview;
