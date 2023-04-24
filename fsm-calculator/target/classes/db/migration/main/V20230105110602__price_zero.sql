CREATE TABLE IF NOT EXISTS  eg_zero_pricing(
    propertyType character varying(256) NOT NULL,
    subPropertytype character varying(64)NOT NULL
);



CREATE INDEX  IF NOT EXISTS  index_propertyType_eg_zero_priciing  ON eg_zero_pricing
(    propertyType
);
CREATE INDEX  IF NOT EXISTS  index_subPropertytype_eg_zero_priciing ON eg_zero_pricing
(    subPropertytype
);
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Temple');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Mosque');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Church');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Gurudwara');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Monastery');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'School');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'College');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'University');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Anganwadi');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Training Institutes');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Hospital');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Nursing home');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Community health center');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Jail');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Institutional', 'Police station');


INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Community Toilets');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Hotel');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Restaurant');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Shopping mall');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Community hall');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Bank');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Private office');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Market');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Hostel');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Warehouse');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Petrol pumps');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Resort');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Theme park');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Commercial', 'Sports center');


INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Residential', 'Independent house');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Residential', 'Apartment');
INSERT INTO eg_zero_pricing(propertyType, subPropertytype)
VALUES ('Residential', 'Rowhouses');