-- PQM Database Schema V1

CREATE TABLE IF NOT EXISTS eg_pqm_tests
(
    id                character varying(64),
    tenantId          character varying(64),
    plantId           character varying(255),
    processId         character varying(255),
    stageId           character varying(255),
    materialId        character varying(255),
    deviceId          character varying(255),
    type              character varying(64),
    status            character varying(64),
    scheduledDate     bigint,
    resultCriteria json not null,
    additionalDetails json,
    isActive          boolean,
    createdBy         character varying(64),
    lastModifiedBy    character varying(64),
    createdTime       bigint,
    lastModifiedTime  bigint,
    CONSTRAINT pk_pqm_tests PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS eg_pqm_test_result_documents
(
    id               character varying(64),
    testId           character varying(64),
    documentUid      character varying(64),
    documentUri      character varying(2084),
    documentType     character varying(64),
    filestoreId      character varying(250),
    isActive         boolean,
    createdBy        character varying(64),
    lastModifiedBy   character varying(64),
    createdTime      bigint,
    lastModifiedTime bigint,
    CONSTRAINT pk_pqm_test_result_documents PRIMARY KEY (id),
    CONSTRAINT fk_documents_pqm_test_results FOREIGN KEY (testId) REFERENCES eg_pqm_tests (id)
        ON DELETE CASCADE
);