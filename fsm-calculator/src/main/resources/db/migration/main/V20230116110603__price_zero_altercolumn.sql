DELETE  FROM eg_zero_pricing;
ALTER TABLE eg_zero_pricing
  RENAME COLUMN subPropertytype TO propertySubType;
  
ALTER TABLE eg_zero_pricing ADD COLUMN IF NOT EXISTS status character varying(64) DEFAULT 'ACTIVE' NOT NULL;
ALTER TABLE eg_zero_pricing ADD COLUMN IF NOT EXISTS  tenantid character varying(64) NOT NULL;

CREATE INDEX  IF NOT EXISTS  index_propertyType_eg_zero_pricing  ON eg_zero_pricing
(    propertyType
);
CREATE INDEX  IF NOT EXISTS  index_propertySubType_eg_zero_pricing ON eg_zero_pricing
(    propertySubType
);
CREATE INDEX  IF NOT EXISTS  index_status_eg_zero_pricing ON eg_zero_pricing
(    status
);
CREATE INDEX  IF NOT EXISTS  index_tenantId_eg_zero_pricing ON eg_zero_pricing
(    tenantId
);

CREATE TABLE IF NOT EXISTS  eg_zero_pricing_audit(
    propertyType character varying(256) NOT NULL,
    propertySubType character varying(64)NOT NULL,
    status character varying(64) NOT NULL,
    tenantId character varying(64)NOT NULL
);


CREATE INDEX  IF NOT EXISTS  index_propertyType_eg_zero_pricing_audit  ON eg_zero_pricing_audit
(    propertyType
);
CREATE INDEX  IF NOT EXISTS  index_propertySubType_eg_zero_pricing_audit ON eg_zero_pricing_audit
(    propertySubType
);
CREATE INDEX  IF NOT EXISTS  index_status_eg_zero_pricing_audit ON eg_zero_pricing_audit
(    status
);
CREATE INDEX  IF NOT EXISTS  index_tenantId_eg_zero_pricing_audit ON eg_zero_pricing_audit
(    tenantId
);