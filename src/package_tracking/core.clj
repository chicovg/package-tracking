(ns package-tracking.core
  (:use compojure.core [ring.adapter.jetty :only [run-jetty]])
  (:require [package-tracking.handlers :refer [app]]))

(defn -main []
  (let [port-str (or (System/getenv "PORT") "3002")
        port (Integer/parseInt port-str)]
    (run-jetty app {:port port})))
