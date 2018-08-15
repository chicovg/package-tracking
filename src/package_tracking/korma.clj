(ns package-tracking.korma
  (:require [korma.core :refer :all]
            [korma.db :refer :all]
            [environ.core :refer [env]]))

(defdb db (postgres {:host (env :db-host)
                     :port (env :db-port)
                     :db (env :db-name)
                     :user (env :db-user)}))

;; Entities
(declare customer
         customer_email_address
         customer_mailing_address
         mailing_address
         state
         city
         zip_code
         shipment
         package
         scan_event)

(defentity customer
           (pk :id)
           (table :customer)
           (database db)
           (entity-fields
             :id
             :first_name
             :last_name))

(defentity customer_email_address
           (table :customer_email_address)
           (database db)
           (entity-fields
             :customer_id
             :value))

(defentity customer_mailing_address
           (table :customer_mailing_address)
           (database db)
           (entity-fields
             :customer_id
             :mailing_address_id)
           (belongs-to customer)
           (belongs-to mailing_address))

(defentity mailing_address
           (table :mailing_address)
           (database db)
           (entity-fields
             :id
             :street_address
             :city_id
             :zip_code_id)
           (belongs-to city)
           (belongs-to zip_code))

(defentity city
           (table :city)
           (database db)
           (entity-fields
             :id
             :name
             :abbreviation
             :state_id)
           (belongs-to state))

(defentity state
           (table :state)
           (database db)
           (entity-fields
             :id
             :name
             :abbreviation))

(defentity zip_code
           (table :zip_code)
           (database db)
           (entity-fields
             :id
             :value))

(defentity shipment
           (table :shipment)
           (database db)
           (has-many package))

(defentity package
           (table :package)
           (database db)
           (has-many scan_event))

(defentity scan_event
           (table :scan_event)
           (database db))

;; Queries
(defn customers
  []
  (-> (select* customer)
      (fields :id :first_name :last_name)
      (exec)))

(defn customer-by-id
  [id]
  (-> (select* customer)
      (where (= :id id))
      (exec)
      (first)))

(defn customer-email-addresses
  [customer_id]
  (-> (select* customer_email_address)
      (where (= :customer_id customer_id))
      (exec)))

(defn customer-mailing-addresses
  [customer_id]
  (-> (select* customer_mailing_address)
      (with mailing_address
            (with city
                  (fields [:name :city])
                  (with state
                        (fields [:abbreviation :state])))
            (with zip_code
                  (fields [:value :zip_code])))
      (where (= :customer_id customer_id))
      (exec)))

(defn shipment-by-tracking-id
  [tracking_id]
  (-> (select* shipment)
      (with package
            (with scan_event
                  (fields [:datetime :date_time])))
      (where (= :tracking_id tracking_id))
      (exec)
      (first)))

(defn customer-shipments
  [customer_id]
  (-> (select* shipment)
      (with package
            (with scan_event
                  (fields [:datetime :date_time] :type)))
      (where (= :customer_id customer_id))
      (exec)))

