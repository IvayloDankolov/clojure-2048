(ns clojure-2048.core
  (:require [clojure-2048.util :refer [cartesian collapse-if]]
            [clojure-2048.matrix :as mat]))

(defn- can-collapse-cells? [cell1 cell2]
  (= (:value cell1) (:value cell2)))

(defn- collapse-cells [cell1 cell2]
  (update-in cell1 [:value] #(* 2 %)))


(defn- move-row-left [row]
  (->> row
       (sort-by (comp first :pos))
       (collapse-if can-collapse-cells? collapse-cells)
       (map #(assoc-in %2 [:pos 0] %1) (range))))

(defn- move-left [game]
  (->> game
       (group-by (comp second :pos))
       (map second)
       (mapcat move-row-left)
       set))

(def rotations
  {:none         (mat/rotation-around 0                 1.5 1.5)
   :quarter-down (mat/rotation-around (/ Math/PI 2)     1.5 1.5)
   :quarter-up   (mat/rotation-around (- (/ Math/PI 2)) 1.5 1.5)
   :half         (mat/rotation-around Math/PI           1.5 1.5)
   })

(defn- left-based-rotations [dir]
  (dir {:left  [(rotations :none) (rotations :none)]
        :right [(rotations :half) (rotations :half)]
        :up    [(rotations :quarter-up)   (rotations :quarter-down)]
        :down  [(rotations :quarter-down) (rotations :quarter-up)]}))

(defn- rotate-tile [rotation tile]
  (update-in tile [:pos] (partial mat/transform-projective-int
                                  rotation)))

(defn rotate [rotation game]
  (map (partial rotate-tile rotation) game))

(defn move [game dir]
  (let [[to-left from-left] (left-based-rotations dir)]
    (->> game
         (rotate to-left)
         (move-left)
         (rotate from-left)
         set)))

;;

(defn- tile-positions [game]
  (->> game
       (map :pos)
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
  (if-let [pos (random-empty-pos game)]
    {:value (random-tile-value)
     :pos pos}))

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

(defn start-game [] (add-tile (add-tile #{})))
