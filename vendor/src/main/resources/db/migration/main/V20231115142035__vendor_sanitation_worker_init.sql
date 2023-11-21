CREATE TABLE IF NOT EXISTS eg_vendor_sanitation_worker
(
    vendor_id        character varying(64) NOT NULL,
    individual_id    character varying(64) NOT NULL,
    vendor_sw_status VARCHAR(64),
    CONSTRAINT fk_eg_vendor_sanitation_worker FOREIGN KEY (vendor_id) REFERENCES eg_vendor (id)
);

CREATE INDEX IF NOT EXISTS idx_vendor_eg_vendor_sanitation_worker ON eg_vendor_sanitation_worker (vendor_id);
CREATE INDEX IF NOT EXISTS idx_sw_eg_vendor_sanitation_worker ON eg_vendor_sanitation_worker (individual_id);

