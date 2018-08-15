(defproject package-tracking "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.0"]
                 [ring "1.5.0"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.8"]
                 [graphql-clj "0.2.6"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [korma "0.4.0"]
                 [environ "1.1.0"]]
  :main package-tracking.core
  :plugins [[lein-environ "1.1.0"]
            [lein-ring "0.9.7"]
            [lein-localrepo "0.5.4"]]
  :ring {:handler package-tracking.handlers/app
         :auto-reload? true
         :auto-refresh? true
         :port 3002}
  :profiles {:dev {:env {:db-host "localhost" :db-port 5432 :db-name "" :db-user "postgres"}}})
