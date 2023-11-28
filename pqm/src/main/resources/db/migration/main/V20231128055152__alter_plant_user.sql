-- Rename column in the main table
ALTER TABLE IF EXISTS eq_plant_user_map
RENAME COLUMN IF EXISTS individualId TO plantOperatorUuid;

-- Rename column in the audit log table (assuming it exists)
ALTER TABLE IF EXISTS eq_plant_user_map_auditlog
RENAME COLUMN IF EXISTS individualId TO plantOperatorUuid;