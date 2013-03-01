(ns logical-playthings.fizzbuzz-test
  (:use [clojure.test :only [deftest testing is]]
        [logical-playthings.fizzbuzz :only [fizzbuzz fizzbuzz-seq fizzbuzz-seq* find-fizzbuzz-index-for]])
  (:require [clojure.core.logic :as logic]))

(deftest test:fizzbuzz
  (testing "The fizzbuzz function"
    (testing "with numbers returning themselves"
      (is (= (fizzbuzz 1) 1))
      (is (= (fizzbuzz 2) 2))
      (is (= (fizzbuzz 4) 4))
      (is (= (fizzbuzz 98) 98)))
    (testing "with numbers returning :fizz"
      (is (= (fizzbuzz 3) :fizz))
      (is (= (fizzbuzz 6) :fizz)))
    (testing "with numbers returning :buzz"
      (is (= (fizzbuzz 5) :buzz))
      (is (= (fizzbuzz 10) :buzz)))
    (testing "with numbers returning :fizzbuzz"
      (is (= (fizzbuzz 15) :fizzbuzz))
      (is (= (fizzbuzz 30) :fizzbuzz)))))

(deftest test:fizzbuzz-seq
  (testing "The fizzbuzz-seq function"
    (is (= (fizzbuzz-seq 15)
             [1 2 :fizz 4 :buzz :fizz 7 8 :fizz :buzz
              11 :fizz 13 14 :fizzbuzz]))))

(deftest test:fizzbuzz-seq*
  (testing "The fizzbuzz-seq* function"
    (is (= (logic/run* [q]
                       (fizzbuzz-seq* 1 16 q))
           (list [1 2 :fizz 4 :buzz :fizz 7 8 :fizz :buzz
                  11 :fizz 13 14 :fizzbuzz])))

    (is (= (logic/run 1 [q]
                      (fizzbuzz-seq* 1 q
                                     [1 2 :fizz 4 :buzz :fizz 7 8 :fizz :buzz
                                      11 :fizz 13 14 :fizzbuzz]))
           (list 16)))))

(deftest test:fizzbuzz-backwards
  (testing "Running fizzbuzz backwards"
    (testing "and finding numbers that return 1"
      (is (= (find-fizzbuzz-index-for 1 10)
               [1])))
    (testing "and finding numbers that return :fizz"
      (is (= (find-fizzbuzz-index-for :fizz 30)
               [3 6 9 12 18 21 24 27])))
    (testing "and finding numbers that return :buzz"
      (is (= (find-fizzbuzz-index-for :buzz 30)
               [5 10 20 25])))
    (testing "and finding numbers that return :fizzbuzz"
      (is (= (find-fizzbuzz-index-for :fizzbuzz 30)
               [15 30]))))

  (testing "Running fizzbuzz back and forth"
    (is (= (map fizzbuzz (find-fizzbuzz-index-for :fizz 30))
             (filter #{:fizz} (fizzbuzz-seq 30))))
    (is (= (map fizzbuzz (find-fizzbuzz-index-for :buzz 30))
             (filter #{:buzz} (fizzbuzz-seq 30))))
    (is (= (map fizzbuzz (find-fizzbuzz-index-for :fizzbuzz 30))
             (filter #{:fizzbuzz} (fizzbuzz-seq 30))))))
