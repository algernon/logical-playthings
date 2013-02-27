(ns logical-playthings.game-of-life
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic])
  (:require [clojure.core.logic.fd :as fd]))

(defn cell-step
  [living? neighbour-count result]

  (comp
   (fd/in neighbour-count (fd/interval 0 8))
   (conde
    [(fd/< neighbour-count 2) (== result false)]
    [(fd/> neighbour-count 3) (== result false)]
    [(== neighbour-count 3) (== result true)]
    [(== neighbour-count 2) (== result living?)])))
