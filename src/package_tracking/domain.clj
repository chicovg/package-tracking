(ns package-tracking.domain
  "Contains functions for accessing customer data")

(defrecord Customer [id first_name last_name email_addresses])

(def customer-records 
  (atom 
    [
     (Customer. 1 "Bob" "Slidell" ["bob@initech.com"])
     (Customer. 2 "Tim" "Johnson" ["teetime@gmail.com"] )
     (Customer. 3 "Bill" "Kidd" ["billythekidd@hotmail.com"])]))

(defn customers [] @customer-records)

(defn customer [id]
  (first 
    (filter #(= (:id %) id) @customer-records)))

