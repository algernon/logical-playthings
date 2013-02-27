(ns logical-playthings.game-of-life
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic])
  (:require [clojure.core.logic.fd :as fd]))

(defn cell-step
  [living? neighbour-count result]

  (all
   (fd/in neighbour-count (fd/interval 0 8))
   (conde
    [(fd/< neighbour-count 2) (== result false)]
    [(fd/> neighbour-count 3) (== result false)]
    [(== neighbour-count 3) (== result true)]
    [(== neighbour-count 2) (== result living?)])))

(defn neighbouro
  [cell neighbour]

  (let [nb-table (for [x (range -1 2)
                       y (range -1 2)
                       :when (not (= [x y] [0 0]))] [x y])]
    (fresh [cell-x cell-y
            nb-x nb-y
            dx dy]
           (== cell [cell-x cell-y])
           (== neighbour [nb-x nb-y])

           (!= cell neighbour)

           (fd/- cell-x nb-x dx)
           (fd/- cell-y nb-y dy)
           (membero [dx dy] nb-table))))

(defn living-neighbouro
  [form cell neighbour]

  (all
   (neighbouro cell neighbour)
   (membero cell form)
   (membero neighbour form)))

(defnu lengtho [l n]
  ([[] 0])
  ([[_ . tail] _]
     (fresh [n1]
            (lengtho tail n1)
            (project [n n1]
                     (== n (+ n1 1))))))
