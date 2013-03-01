(ns logical-playthings.arithmetic
  (:use [clojure.core.logic :only [fresh]])
  (:require [clojure.core.logic.fd :as fd]))

;; Counterpart of fd/quot: remo.
(defn remo
  [n d r]

  (fresh [q x]
           (fd/in q d x (fd/interval 0 Integer/MAX_VALUE))
           (fd/quot q d x)
           (fd/!= q 0)
           (fd/- n r q)
           (fd/< r d)))
