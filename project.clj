(defproject clojure-2048 "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2156"]]

  :plugins [[lein-cljsbuild "1.0.2"]
            [lein-npm "0.3.2"]]


  :node-dependencies [["express" "3.x"]]

  :cljsbuild {
    :builds {
      :server {
        :source-paths ["src/clojure-2048/server"]
        :compiler {:output-to "resources/server.js"
                   :optimizations :simple
                   :target :nodejs
                   :pretty-print true}}

      :client {
        :source-paths ["src/clojure-2048/client"]
        :compiler {:output-to "resources/public/js/cljs.js"
                   :optimizations :simple
                   :pretty-print true}}}}

  :main "resources/server.js")

