(ns package-tracking.handlers
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]
            [ring.util.response :refer [response not-found]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.json :refer [wrap-json-params]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer :all]
            [cheshire.core :as json]
            [package-tracking.graphql :as graphql]))

(defroutes routes
  (GET "/graphql" [schema query variables :as request]
       (response 
          (graphql/execute query variables nil)))
  (POST "/graphql" [schema query variables operationName :as request]
      (response
        (graphql/execute query (json/parse-string variables) operationName)))
  (route/resources "/" {:root ""})
  (route/not-found "<h1>Page not found</h1>"))

(defn wrap-dir-index
  [handler]
  (fn [req]
    (handler
      (update-in req [:uri] #(if (= "/" %) "/index.html" %)))))

(def app 
  (-> routes
      wrap-json-response
      (wrap-cors :access-control-allow-origin [#"http://localhost:8080" #"http://.*"]
                 :access-control-allow-methods [:get :put :post :delete])
      (wrap-defaults api-defaults)
      (wrap-json-params)
      (wrap-dir-index)))
