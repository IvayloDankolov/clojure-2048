(ns hello-clojurescript.server
  (:require [cljs.nodejs :as node]))

(def express (node/require "express"))
(def app (express.))

(defn configure []
  (doto app
    (.use (. express (static "./resources/public")))
    (.use (. express (logger "dev")))
    (.use (. express (bodyParser)))
    (.use (. express (methodOverride)))))

(defn default [req res]
  (.redirect res "/index.html"))

(defn start [& _]
  (doto app
    (.configure configure)
    (.get "/" default)
    (.listen 8080)))

(enable-console-print!)
(set! *main-cli-fn* start)
