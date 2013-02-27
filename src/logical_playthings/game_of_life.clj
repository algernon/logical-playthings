(ns logical-playthings.game-of-life
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic])
  (:require [clojure.core.logic.fd :as fd]))

(defn- neighbourc
  [neighbour-count]

  (fd/in neighbour-count (fd/interval 0 8)))

(defn cell-step
  [living? neighbour-count result]

  (conde
   [(neighbourc neighbour-count) (fd/< neighbour-count 2) (== result false)]
   [(neighbourc neighbour-count) (fd/> neighbour-count 3) (== result false)]
   [(neighbourc neighbour-count) (== neighbour-count 3) (== result true)]
   [(neighbourc neighbour-count) (== neighbour-count 2) (== result living?)]))
