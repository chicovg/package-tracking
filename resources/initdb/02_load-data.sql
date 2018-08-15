COPY customer(id, first_name, last_name)
FROM '/tmp/data/CUSTOMER_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY customer_email_address(customer_id, value)
FROM '/tmp/data/CUSTOMER_EMAIL_ADDRESS_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY state (id, name, abbreviation)
FROM '/tmp/data/STATE_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY city (id, name, abbreviation, state_id)
FROM '/tmp/data/CITY_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY zip_code (id, value)
FROM '/tmp/data/ZIP_CODE_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY mailing_address (id, street_address, city_id, zip_code_id)
FROM '/tmp/data/MAILING_ADDRESS_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY service_level (id, name, description, base_price)
FROM '/tmp/data/SERVICE_LEVEL_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY shipment (id, tracking_id, submission_date, total_cost, customer_id, service_level_id, billing_address_id, origin_address_id, destination_address_id)
FROM '/tmp/data/SHIPMENT_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY package (id, tracking_id, size_sq_in, weight_oz, shipment_id)
FROM '/tmp/data/PACKAGE_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY facility (id, code, name, capacity, mailing_address_id)
FROM '/tmp/data/FACILITY_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY scan_event (id, datetime, type, facility_id, package_id)
FROM '/tmp/data/SCAN_EVENT_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';

COPY customer_mailing_address (customer_id, mailing_address_id)
FROM '/tmp/data/CUSTOMER_MAILING_ADDRESS_DATA_TABLE.csv' CSV DELIMITER ',' QUOTE '"';