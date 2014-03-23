(ns hello-clojurescript.server
  (:require [cljs.nodejs :as node]))

(defn start [& _]
  (println "Hello World!"))

(enable-console-print!)
(set! *main-cli-fn* start)
