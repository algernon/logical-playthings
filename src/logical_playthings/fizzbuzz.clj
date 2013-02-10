(refer 'clojure.core :exclude [==])
(use '[clojure.core.logic])
(require '[clojure.core.logic.fd :as fd])
(require '[clojure.test :as t])

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

  (map second
       (sort (fn [[a _] [b _]] (compare a b))
             (run* [q]
                   (fresh [n r]
                          (fd/in n (fd/interval 1 stop))
                          (fizzbuzzo n r)
                          (== q [n r]))))))

(defn find-fizzbuzz-index-for
  [v max]

  (run* [q]
        (fizzbuzzo q v)
        (fd/in q (fd/interval 1 max))))

;; --| Tests |--

(t/deftest test:fizzbuzz
  (t/testing "The fizzbuzz function"
    (t/testing "with numbers returning themselves"
      (t/is (= (fizzbuzz 1) 1))
      (t/is (= (fizzbuzz 2) 2))
      (t/is (= (fizzbuzz 4) 4))
      (t/is (= (fizzbuzz 98) 98)))
    (t/testing "with numbers returning :fizz"
      (t/is (= (fizzbuzz 3) :fizz))
      (t/is (= (fizzbuzz 6) :fizz)))
    (t/testing "with numbers returning :buzz"
      (t/is (= (fizzbuzz 5) :buzz))
      (t/is (= (fizzbuzz 10) :buzz)))
    (t/testing "with numbers returning :fizzbuzz"
      (t/is (= (fizzbuzz 15) :fizzbuzz))
      (t/is (= (fizzbuzz 30) :fizzbuzz)))))

(t/deftest test:fizzbuzz-seq
  (t/testing "The fizzbuzz-seq function"
    (t/is (= (fizzbuzz-seq 15)
             [1 2 :fizz 4 :buzz :fizz 7 8 :fizz :buzz
              11 :fizz 13 14 :fizzbuzz]))))

(t/deftest test:fizzbuzz-backwards
  (t/testing "Running fizzbuzz backwards"
    (t/testing "and finding numbers that return 1"
      (t/is (= (find-fizzbuzz-index-for 1 10)
               [1])))
    (t/testing "and finding numbers that return :fizz"
      (t/is (= (find-fizzbuzz-index-for :fizz 30)
               [3 6 9 12 18 21 24 27])))
    (t/testing "and finding numbers that return :buzz"
      (t/is (= (find-fizzbuzz-index-for :buzz 30)
               [5 10 20 25])))
    (t/testing "and finding numbers that return :fizzbuzz"
      (t/is (= (find-fizzbuzz-index-for :fizzbuzz 30)
               [15 30]))))

  (t/testing "Running fizzbuzz back and forth"
    (t/is (= (map fizzbuzz (find-fizzbuzz-index-for :fizz 30))
             (filter #{:fizz} (fizzbuzz-seq 30))))
    (t/is (= (map fizzbuzz (find-fizzbuzz-index-for :buzz 30))
             (filter #{:buzz} (fizzbuzz-seq 30))))
    (t/is (= (map fizzbuzz (find-fizzbuzz-index-for :fizzbuzz 30))
             (filter #{:fizzbuzz} (fizzbuzz-seq 30))))))

(comment
  (t/run-tests)
)
