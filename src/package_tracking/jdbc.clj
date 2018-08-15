(ns package-tracking.jdbc
  (:import com.mchange.v2.c3p0.ComboPooledDataSource)
  (:require [clojure.java.jdbc :as jdbc]
            [jdbc.pool.c3p0 :as pool]
            [package-tracking.domain :refer :all]))

(def db-uri
  (java.net.URI.
    (or (System/getenv "DATABASE_URL")
        "oracle://package_tracking:pass123@localhost:1521/xe")))

(defn get-db-spec [uri]
  (let [host (.getHost uri)
        port (.getPort uri)
        path (.getPath uri)
        user-info (.getUserInfo uri)]
      {:classname "oracle.jdbc.driver.OracleDriver"
       :subprotocol "oracle"
       :subname (if (= -1 port)
                  (format "thin:@%s%s" host path)
                  (format "thin:@%s:%s%s" host port path))
       :user (if (nil? user-info)
               nil
               (get (clojure.string/split user-info #":") 0))
       :password (if (nil? user-info)
                   nil
                   (get (clojure.string/split user-info #":") 1))}))

(def pool
  (pool/make-datasource-spec (get-db-spec db-uri)))

; Customer queries
(def select-all-customers
  "select c.id, c.first_name, c.last_name, e.value as email_address 
   from customer c
   join customer_email_address e on c.id = e.customer_id")

(def select-one-customer
  (str select-all-customers " where c.id = ?"))

; Customer mapping
(defn map-customer [row]
  (let [{:keys [id first_name last_name email_address]} row]
    (->Customer id first_name last_name #{email_address})))

(defn aggregate-email-values [c1 c2]
  (let [{:keys [id first_name last_name]} c1
        email_addresses (concat (:email_addresses c1) (:email_addresses c2))]
    (->Customer id first_name last_name email_addresses)))

(defn customer-values [customer-email-values]
  (map 
    #(reduce aggregate-email-values %) 
    (vals (group-by :id customer-email-values))))

; Customer functions
(defn all-customers []
  (let [results (jdbc/query pool select-all-customers {:row-fn map-customer})]
    (customer-values results)))

(defn one-customer [id]
  (let [results (jdbc/query pool [select-one-customer id] {:row-fn map-customer})]
    (first (customer-values results))))


