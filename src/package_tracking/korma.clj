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
         scan_event
         facility)

(defentity customer
           (pk :id)
           (table :customer)
           (database db))

(defentity customer_email_address
           (table :customer_email_address)
           (database db))

(defentity customer_mailing_address
           (table :customer_mailing_address)
           (database db))

(defentity mailing_address
           (table :mailing_address)
           (database db)
           (belongs-to city)
           (belongs-to zip_code))

(defentity city
           (table :city)
           (database db)
           (belongs-to state))

(defentity state
           (table :state)
           (database db))

(defentity zip_code
           (table :zip_code)
           (database db))

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

(defentity facility
           (table :facility)
           (database db)
           (belongs-to mailing_address))

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

(def shipment-base
  (-> (select* shipment)
      (with package
            (with scan_event
                  (fields [:datetime :date_time] :type :facility_id)))))

(defn shipment-by-tracking-id
  [tracking_id]
  (-> shipment-base
      (where (= :tracking_id tracking_id))
      (exec)
      (first)))

(defn customer-shipments
  [customer_id]
  (-> shipment-base
      (where (= :customer_id customer_id))
      (exec)))

(def mailing-address-base
  (-> (select* mailing_address)
      (with city
            (fields [:name :city])
            (with state
                  (fields [:abbreviation :state])))
      (with zip_code
            (fields [:value :zip_code]))))

(defn mailing-address-by-id
  [id]
  (-> mailing-address-base
      (where (= :id id))
      (exec)
      (first)))

(defn customer-mailing-addresses
  [customer_id]
  (-> mailing-address-base
      (join customer_mailing_address (= :mailing_address.id :customer_mailing_address.mailing_address_id))
      (where (= :customer_mailing_address.customer_id customer_id))
      (exec)))

(defn facility-by-id
  [id]
  (-> (select* facility)
      (with mailing_address
            (fields :street_address)
            (with city
                  (fields [:name :city])
                  (with state
                        (fields [:abbreviation :state])))
            (with zip_code
                  (fields [:value :zip_code])))
      (where (= :id id))
      (exec)
      (first)))


