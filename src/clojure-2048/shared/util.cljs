(ns clojure-2048.util)

(defn cartesian [coll1 coll2]
  (for [x coll1 y coll2]
    [x y]))

(defn collapse-if [pred f coll]
  (->> coll
       (reduce (fn [processed curr]
                 (let [prev (peek processed)]
                   (if (and prev (pred prev curr))
                     (conj (pop processed) (f prev curr) nil)
                     (conj processed curr))))
               [])
       (keep identity)))
