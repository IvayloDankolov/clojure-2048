(ns clojure-2048.matrix)

;;; Basic operations

(defn dot-product [v1 v2]
  (reduce + (map * v1 v2)))

(defn transform [mat v]
  (mapv (partial dot-product v) mat))

(defn transpose [mat]
  (apply mapv vector mat))

(defn multiply [mat1 mat2]
  (->> mat1
       (mapv (partial transform (transpose mat2)))))

(defn combine [& mats]
  (reduce #(multiply %2 %1) mats))

;;; Projective space

(defn euclidian->projective [v]
  (conj v 1))

(defn projective->euclidian [v]
  (let [w (peek v)]
    (mapv #(/ % w) (pop v))))

(defn transform-projective [mat v]
  (->> v
       euclidian->projective
       (transform mat)
       projective->euclidian))

(defn transform-projective-int [mat v]
  (->> v
       (transform-projective mat)
       (mapv #(Math/round %))))

;;; Operations in projective space

(defn translation [x y]
  [[1 0 x]
   [0 1 y]
   [0 0 1]])

(defn rotation [angle]
  [[(Math/cos angle) (-(Math/sin angle)) 0]
   [(Math/sin angle) (Math/cos angle)    0]
   [0                0                   1]])

(defn rotation-around [angle x y]
  (combine
   (translation (- x) (- y))
   (rotation angle)
   (translation x y)))
