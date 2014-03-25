(ns clojure-2048.client
  (:require [clojure-2048.core :refer [move game-state step-game]]

            [clojure.browser.repl :as repl]

            [clojure.string :as str]

            [goog.dom.classes :as classes]
            [goog.events :as events]))

(enable-console-print!)
(repl/connect "http://localhost:9000/repl")

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

(defn repaint-game [game]
  (let [tile-container
        (. js/document querySelector ".tile-container")]
    (loop [c (.-firstChild tile-container)]
      (when c
        (.removeChild tile-container c)
        (recur (.-firstChild tile-container))))
    (doseq [tile game]
      (. tile-container appendChild (make-js-tile tile)))))


(def game
  (atom
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
    {:value 4 :x 3 :y 3}]))

(def move-keys {37 :left
                38 :up
                39 :right
                40 :down})

(events/listen js/window (.-KEYDOWN events/EventType)
  (fn [event]
    (let [code (.-keyCode event)
          dir (move-keys code)]
      (when dir
        (swap! game #(step-game % dir))
        (repaint-game @game)
        (.preventDefault event)))))

(repaint-game @game)

