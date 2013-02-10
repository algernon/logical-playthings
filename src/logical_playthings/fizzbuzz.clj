(refer 'clojure.core :exclude [==])
(use '[clojure.core.logic])
(require '[clojure.core.logic.fd :as fd])

(defn multo
  "Ensure that `n` is a multiplicate of `div`."
  [n div]

  (fresh [x]
         (fd/quot n div x)))

(defnc not-multo
  [n div]

  (when (number? n)
    (not (zero? (rem n div)))))

(defn fizzbuzzo
  [?n q]

  (conde
   [(multo ?n 3) (multo ?n 5) (== q :fizzbuzz)]
   [(multo ?n 3) (not-multo ?n 5) (== q :fizz)]
   [(multo ?n 5) (not-multo ?n 3) (== q :buzz)]
   [(not-multo ?n 3) (not-multo ?n 5) (== q ?n)]))

(defn fizzbuzz
  [n]

  (first (run* [q]
               (fizzbuzzo n q))))

(defn fizzbuzz-seq
  [stop]

  (->> (range)
       (drop 1)
       (map fizzbuzz)
       (take stop)))
