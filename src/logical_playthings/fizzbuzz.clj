(refer 'clojure.core :exclude [==])
(use '[clojure.core.logic])
(require '[clojure.core.logic.fd :as fd])

(defn multo
  "Ensure that `n` is a multiplicate of `div`."
  [n div]

  (fresh [x]
         (fd/quot n div x)))

(defn fizzbuzzg
  [?n q]

  (conda
   [(multo ?n 15) (== q :fizzbuzz)]
   [(multo ?n 3) (== q :fizz)]
   [(multo ?n 5) (== q :buzz)]
   [(== q ?n)]))

(defn fizzbuzz
  [n]

  (first (run* [q]
               (fizzbuzzg n q))))

(defn fizzbuzz-seq
  [stop]

  (->> (range)
       (drop 1)
       (map fizzbuzz)
       (take stop)))
