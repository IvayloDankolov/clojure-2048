(ns clojure-2048.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :as resources]
            [ring.util.response :as response]
            [ring.middleware.reload :as reload]
            [ring.middleware.stacktrace :as stacktrace])
  (:gen-class))

(defn handler [request]
  (if (= "/" (:uri request))
    (response/redirect "/index.html")))

(def app
  (-> #'handler
      (resources/wrap-resource "public")
      (reload/wrap-reload 'clojure-2048.server)
      (stacktrace/wrap-stacktrace)))

(defn -main [& args]
  (jetty/run-jetty app {:port 8080}))

