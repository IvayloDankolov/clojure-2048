(ns clojure-2048.core
  (:require [clojure-2048.util :refer [cartesian collapse-if]]))

(defn- can-collapse-cells? [cell1 cell2]
  (= (:value cell1) (:value cell2)))

(defn- collapse-cells [cell1 cell2]
  (update-in cell1 [:value] #(* 2 %)))


(defn- move-row [row dir]
  (let [row-field (dir {:left  :x
                        :right :x
                        :up    :y
                        :down  :y})
        row-direction (dir {:left  <
                            :right >
                            :up    <
                            :down  >})
        row-indices (dir {:left  (range 4)
                          :right (range 3 -1 -1)
                          :up    (range 4)
                          :down  (range 3 -1 -1)})]
    (->> row
         (sort-by row-field row-direction)
         (collapse-if can-collapse-cells? collapse-cells)
         (map #(assoc %2 row-field %1) row-indices))))

(defn move [game dir]
  (let [col-field (dir {:left  :y
                        :right :y
                        :up    :x
                        :down  :x})]
    (->> game
         (group-by col-field)
         (map second)
         (mapcat #(move-row % dir))
         vec)))

;;

(defn- tile-positions [game]
  (->> game
       (map (juxt :x :y))
       set))

(defn- random-empty-pos [game]
  (->> (cartesian (range 4) (range 4))
       (filter (complement (tile-positions game)))
       (shuffle)
       first))

(defn- random-tile-value []
  (if (<= (rand) 0.1)
    4
    2))

(defn- new-tile [game]
  (if-let [[x y] (random-empty-pos game)]
    {:value (random-tile-value)
     :x x
     :y y}))

(defn add-tile [game]
  (if-let [tile (new-tile game)]
    (conj game tile)
    game))

;;

(defn game-state [game]
  (cond

   (some #(>= (:value %) 2048) game)
   :won

   (= (move game :left) (move game :right) (move game :up) (move game :down))
   :lost

   :else
   :in-progress))

(defn step-game [game dir]
  (let [moved (move game dir)]
    (if (not= moved game)
      (add-tile moved)
      moved)))
