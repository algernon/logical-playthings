;; This is a REPL/LightTable playground. Nothing fancy, tests cover
;; everything already, but this namespace makes it easier to demo things,
;; at least with LightTable.
(ns logical-playthings.playground
  (:require [clojure.core.logic :as l]
            [clojure.core.logic.fd :as fd]
            [logical-playthings.game-of-life :as gol]
            [logical-playthings.fizzbuzz :as fbz]
            [logical-playthings.arithmetic :as arith]))

;; I have a cell, that oscillates between living and dying. How many neighbours does it have?
(println
 (set
  (l/run* [current-nc]
          (l/fresh [p-nc p-state
                    p-p-nc p-p-state
                    c-state
                    n-state]
                   (l/== c-state :alive)
                   (l/== p-state :dead)
                   (l/== p-p-state :alive)
                   (l/== n-state :dead)
                   (gol/cell-step p-p-state p-p-nc p-state)
                   (gol/cell-step p-state p-nc c-state)
                   (gol/cell-step c-state current-nc n-state)))))

;; I have a living cell, what state and neighbour count can lead to this state?
(println
 (l/run* [neighbour-count state]
         (gol/cell-step state neighbour-count :alive)))

;; I have a living cell, that was dead in the previous iteration. How many neighbours did it have?
(println
 (l/run* [neighbour-count]
         (gol/cell-step :dead neighbour-count :alive)))

;; I have a dead cell, what will it evolve to at various neighbour counts?
(println
	(l/run* [neighbour-count state]
          (gol/cell-step :dead neighbour-count state)))
