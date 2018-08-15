(ns package-tracking.graphql
  (require [graphql-clj.resolver :as resolver]
           [graphql-clj.executor :as executor] 
           [graphql-clj.query-validator :as qv]
           [graphql-clj.schema-validator :as sv]
           [clojure.core.match :as match]
           [package-tracking.korma :refer [customers
                                           customer-by-id
                                           customer-email-addresses
                                           customer-mailing-addresses
                                           customer-shipments
                                           shipment-by-tracking-id]]))

(def schema
  "
  type MailingAddress {
    id: Int
    street_address: String
    city: String
    state: String
    zip_code: String
  }

  type Facility {
    id: Int
    code: String
    name: String
    mailing_address: MailingAddress
    scan_events: [ScanEvent]
  }

  type ScanEvent {
    id: Int
    date_time: String
    type: String
    facility: Facility
  }

  type Package {
    id: Int
    tracking_id: String
    size_sq_in: Float
    weight_oz: Float
    scan_events: [ScanEvent]
  }

  type Shipment {
    id: Int
    submission_date: String
    tracking_id: String
    total_cost: Float
    packages: [Package]
    billing_address: MailingAddress
    origin_address: MailingAddress
    destination_address: MailingAddress
  }

  type Customer {
    id: Int
    first_name: String
    last_name: String
    email_addresses: [String]
    mailing_addresses: [MailingAddress]
    shipments: [Shipment]
  }

  type Query {
    customers: [Customer]
    customer(id: Int): Customer
    shipment(tracking_id: String): Shipment
  }

  schema {
    query: Query
  }")

(defn get-all-customers
  [_ _ _]
  (customers))

(defn get-customer
  [_ _ args]
  (customer-by-id (bigdec (get args "id"))))

(defn get-customer-email-addresses
  [_ parent _]
  (->> (customer-email-addresses (get parent :id))
       (map :value)))

(defn get-customer-mailing-addresses
  [_ parent _]
  (customer-mailing-addresses (get parent :id)))

(defn get-customer-shipments
  [_ parent _]
  (customer-shipments (get parent :id)))

(defn get-shipment-by-tracking-id
  [_ _ args]
  (shipment-by-tracking-id (get args "tracking_id")))

(defn get-packages
  [_ parent _]
  (get parent :package))

(defn get-scan-events
  [_ parent _]
  (get parent :scan_event))

(defn resolver-fn [type-name field-name]
  (match/match [type-name field-name]
               ["Query" "customers"] get-all-customers
               ["Query" "customer"] get-customer
               ["Query" "shipment"] get-shipment-by-tracking-id
               ["Customer" "email_addresses"] get-customer-email-addresses
               ["Customer" "mailing_addresses"] get-customer-mailing-addresses
               ["Customer" "shipments"] get-customer-shipments
               ["Shipment" "packages"] get-packages
               ["Package" "scan_events"] get-scan-events
               :else nil))

(def validated-schema (sv/validate-schema schema))

(defn execute
  [query variables operation-name]
  (executor/execute nil validated-schema resolver-fn query variables operation-name))
