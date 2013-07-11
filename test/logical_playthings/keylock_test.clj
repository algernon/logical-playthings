(ns logical-playthings.keylock-test
  (:use [clojure.test :only [deftest testing is]]
        [logical-playthings.keylock :only [keylock]])
  (:require [clojure.core.logic :as logic]))

(deftest test:keylock
  (testing "The solution to the locked safe problem"
    (is (= (keylock) [7 4 6 5 8]))))
