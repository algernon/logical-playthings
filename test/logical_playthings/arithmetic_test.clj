(ns logical-playthings.arithmetic-test
  (:use [clojure.core.logic :only [run*]]
        [clojure.test :only [deftest testing is run-tests]])
  (:require [logical-playthings.arithmetic :as a]
            [clojure.core.logic.fd :as fd]))

(deftest test:remo
  (testing "remo"
    (testing "being able to find the remnant of a division"
      (is (= (run* [r]
                   (fd/in r (fd/interval 0 10))
                   (a/remo 1 1 r))
             '(0)))

      (is (= (run* [r]
                   (fd/in r (fd/interval 0 10))
                   (a/remo 15 4 r))
             '(3)))

      (is (= (run* [r]
                   (fd/in r (fd/interval 0 1))
                   (a/remo 15 4 r))
             '())))

    (testing "being able to find the divisor"
      (is (= (run* [d]
                   (a/remo 15 d 3))
             '(4 6 12))))))
