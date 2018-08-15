CREATE TABLE customer (
    id NUMERIC(9) CONSTRAINT customer_pk PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
);
    
CREATE TABLE customer_email_address ( 
    customer_id NUMERIC(9),
    value VARCHAR(254),
    PRIMARY KEY (customer_id, value),
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);

CREATE TABLE state (
    id NUMERIC(3) CONSTRAINT state_pk PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    abbreviation VARCHAR(2) NOT NULL
);

CREATE TABLE city (
    id NUMERIC(5) CONSTRAINT city_pk PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    abbreviation VARCHAR(5) NOT NULL,
    state_id NUMERIC(3) NOT NULL,
    FOREIGN KEY (state_id) REFERENCES state(id) ON DELETE CASCADE
);

CREATE TABLE zip_code (
    id NUMERIC(9) CONSTRAINT zip_code_pk PRIMARY KEY,
    value VARCHAR(9) NOT NULL
);

CREATE TABLE mailing_address (
    id NUMERIC(10) CONSTRAINT mailing_address_pk PRIMARY KEY,
    street_address VARCHAR(100) NOT NULL,
    city_id NUMERIC(5) NOT NULL,
    zip_code_id NUMERIC (9) NOT NULL,
    FOREIGN KEY (city_id) REFERENCES city(id) ON DELETE CASCADE,
    FOREIGN KEY (zip_code_id) REFERENCES zip_code(id) ON DELETE CASCADE
);

CREATE TABLE service_level (
    id NUMERIC(2) CONSTRAINT service_level_pk PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL,
    description VARCHAR(200) NOT NULL,
    base_price NUMERIC(8,4) NOT NULL
);

CREATE TABLE shipment (
    id NUMERIC(12) CONSTRAINT shipment_pk PRIMARY KEY,
    tracking_id VARCHAR(20) UNIQUE NOT NULL,
    submission_date TIMESTAMP NOT NULL,
    total_cost NUMERIC(10,2) DEFAULT 0.00 NOT NULL,
    customer_id NUMERIC(9) NOT NULL,
    service_level_id NUMERIC(2) NOT NULL, 
    billing_address_id NUMERIC(10) NOT NULL,
    origin_address_id NUMERIC(10) NOT NULL,
    destination_address_id NUMERIC(10) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer ON DELETE CASCADE,
    FOREIGN KEY (service_level_id) REFERENCES service_level,
    FOREIGN KEY (billing_address_id) REFERENCES mailing_address,
    FOREIGN KEY (origin_address_id) REFERENCES mailing_address,
    FOREIGN KEY (destination_address_id) REFERENCES mailing_address
);

CREATE TABLE package (
    id NUMERIC(15) CONSTRAINT package_pk PRIMARY KEY,
    tracking_id VARCHAR(20) UNIQUE NOT NULL,
    size_sq_in NUMERIC(8,4) NOT NULL,
    weight_oz NUMERIC(12,4) NOT NULL,
    shipment_id NUMERIC(12) NOT NULL,
    FOREIGN KEY (shipment_id) REFERENCES shipment ON DELETE CASCADE
);

CREATE TABLE facility (
    id NUMERIC(6) CONSTRAINT facility_pk PRIMARY KEY,
    code VARCHAR(5) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    capacity NUMERIC(10) NOT NULL,
    mailing_address_id NUMERIC(10) NOT NULL,
    FOREIGN KEY (mailing_address_id) REFERENCES mailing_address
);

CREATE TABLE scan_event (
    id NUMERIC(18) CONSTRAINT scan_event_pk PRIMARY KEY,
    datetime TIMESTAMP NOT NULL,
    type VARCHAR(12) NOT NULL,
    facility_id NUMERIC(6) NOT NULL,
    package_id NUMERIC(15) NOT NULL,
    FOREIGN KEY (facility_id) REFERENCES facility ON DELETE CASCADE,
    FOREIGN KEY (package_id) REFERENCES package ON DELETE CASCADE
);

CREATE TABLE customer_mailing_address (
    customer_id NUMERIC(12) NOT NULL,
    mailing_address_id NUMERIC(10) NOT NULL,
    PRIMARY KEY (customer_id, mailing_address_id),
    FOREIGN KEY (customer_id) REFERENCES customer ON DELETE CASCADE,
    FOREIGN KEY (mailing_address_id) REFERENCES mailing_address
);
