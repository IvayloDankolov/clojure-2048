(ns clojure-2048.client
  (:require [clojure-2048.core :refer [move add-tile game-state]]

            [clojure.browser.repl :as repl]

            [clojure.string :as str]

            [goog.dom.classes :as classes]))

(defn value-string [value]
  (if (<= value 2048)
                   (str value)
                   "super"))

(defn get-classes [{:keys [x y value]}]
  ["tile"
   (str "tile-position-" (inc x) "-" (inc y))
   (str "tile-" (value-string value))])


(defn make-js-tile [tile]
  (let [js-tile (.createElement js/document "div")
        inner   (.createElement js/document "div")]
    (doseq [cls (get-classes tile)]
      (classes/add js-tile cls))

    (classes/add inner "tile-inner")
    (aset inner "textContent" (value-string (:value tile)))
    (.appendChild js-tile inner)

    js-tile))


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


(def tile-container
  (. js/document querySelector ".tile-container"))

(doseq [tile test-board]
  (. tile-container appendChild (make-js-tile tile)))
