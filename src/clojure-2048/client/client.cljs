(ns clojure-2048.client
  (:require [clojure-2048.core :refer [move add-tile game-state]]
            [clojure.browser.repl :as repl]))

(repl/connect "http://localhost:9000/repl")

(def test-board
  [{:value 2 :x 0 :y 0}
   {:value 2 :x 1 :y 0}
   {:value 2 :x 2 :y 0}
   {:value 2 :x 3 :y 0}

   {:value 4 :x 1 :y 1}
   {:value 4 :x 3 :y 1}

   {:value 2 :x 0 :y 2}
   {:value 4 :x 2 :y 2}
   {:value 2 :x 3 :y 2}

   {:value 2 :x 0 :y 3}
   {:value 2 :x 1 :y 3}
   {:value 4 :x 3 :y 3}])
