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

  (fresh [cell-x cell-y
          nb-x nb-y]
         (== cell [cell-x cell-y])
         (== neighbour [nb-x nb-y])

         (!= cell neighbour)

         ;; FIXME: This conde here is terrible, and should be replaced
         ;; by something much more logical.
         (conde
          [(fd/- cell-x 1 nb-x) (fd/- cell-y 1 nb-y)]
          [(== cell-x nb-x) (fd/- cell-y 1 nb-y)]
          [(fd/+ cell-x 1 nb-x) (fd/- cell-y 1 nb-y)]

          [(fd/- cell-x 1 nb-x) (== cell-y nb-y)]
          ; ---
          [(fd/+ cell-x 1 nb-x) (== cell-y nb-y)]

          [(fd/- cell-x 1 nb-x) (fd/+ cell-y 1 nb-y)]
          [(== cell-x nb-x) (fd/+ cell-y 1 nb-y)]
          [(fd/+ cell-x 1 nb-x) (fd/+ cell-y 1 nb-y)])))
