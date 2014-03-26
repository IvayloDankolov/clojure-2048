(ns clojure-2048.client
  (:require [clojure-2048.core :as game]

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

(defn get-classes [{value :value [x y] :pos}]
  ["tile"
   (str "tile-position-" (inc x) "-" (inc y))
   (str "tile-" (value-string value))])


(defn make-js-tile [tile]
  (let [js-tile (.createElement js/document "div")
        inner   (.createElement js/document "div")]
    (doseq [cls (get-classes tile)]
      (classes/add js-tile cls))

    (classes/add inner "tile-inner")
    (aset inner "textContent" (str (:value tile)))
    (.appendChild js-tile inner)

    js-tile))


(def game
  (atom (game/start-game)))

(defn repaint-game! [game]
  (let [tile-container
        (. js/document querySelector ".tile-container")]
    (loop [c (.-firstChild tile-container)]
      (when c
        (.removeChild tile-container c)
        (recur (.-firstChild tile-container))))
    (doseq [tile game]
      (. tile-container appendChild (make-js-tile tile)))))


(def move-keys {37 :left
                38 :up
                39 :right
                40 :down})

(def rotate-keys {74 :quarter-up
                  75 :quarter-down})

(defn move-game! [key-code]
  (let [dir (move-keys key-code)]
    (swap! game #(game/step-game % dir))
    (repaint-game! @game)
    true))

(defn rotate-game! [key-code]
  (let [rot-name (rotate-keys key-code)
        rot (game/rotations rot-name)]
    (swap! game (partial game/rotate rot))
    (repaint-game! @game)
    true))


(events/listen js/window (.-KEYDOWN events/EventType)
  (fn [event]
    (let [key-code (.-keyCode event)]
      (if (cond
           (move-keys key-code) (move-game! key-code)
           (rotate-keys key-code) (rotate-game! key-code))
        (.preventDefault event)))))

(repaint-game! @game)

