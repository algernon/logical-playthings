(ns logical-playthings.fizzbuzz
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic])
  (:require [clojure.core.logic.fd :as fd]))

(defn multc
  "Ensure that `n` is a multiplicate of `div`."
  [n div]

  (fresh [x]
         (fd/quot n div x)))

(defnc not-multc
  [n div]

  (when (number? n)
    (not (zero? (rem n div)))))

(defn fizzbuzzo
  [?n q]

  (conde
   [(multc ?n 3) (multc ?n 5) (== q :fizzbuzz)]
   [(multc ?n 3) (not-multc ?n 5) (== q :fizz)]
   [(multc ?n 5) (not-multc ?n 3) (== q :buzz)]
   [(not-multc ?n 3) (not-multc ?n 5) (== q ?n)]))

(defn fizzbuzz
  [n]

  (first (run* [q]
               (fizzbuzzo n q))))

(defn fizzbuzz-seq*
  [stop]

  (run* [q]
        (fresh [n r]
               (fd/in n (fd/interval 1 stop))
               (fizzbuzzo n r)
               (== q [n r]))))

(defn fizzbuzz-seq
  [stop]

  (map #(get (apply hash-map (mapcat concat (fizzbuzz-seq* stop))) %)
       (range 1 (inc stop))))

(defn find-fizzbuzz-index-for
  [v max]

  (run* [q]
        (fizzbuzzo q v)
        (fd/in q (fd/interval 1 max))))
