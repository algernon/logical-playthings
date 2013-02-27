(ns logical-playthings.game-of-life-test
  (:use [clojure.test]
        [logical-playthings.game-of-life])
  (:require [clojure.core.logic :as logic]
            [clojure.core.logic.fd :as fd]))

(deftest test:cell-step-rules
  (testing "Single cell rules"
    (testing "rule #1: live cell with fewer than 2 neighbours dies"
      (is (= (logic/run* [q]
                         (cell-step true 0 q))
             '(false)))
      (is (= (logic/run* [q]
                         (cell-step true 1 q))
             '(false)))

      (testing "...backwards"
        (is (= (set (logic/run* [q]
                                (fd/in q (fd/interval 0 2))
                                (cell-step true q false)))
               #{0 1}))))

    (testing "rule #2: live cell with 2 or 3 neighbours live on"
      (is (= (logic/run* [q]
                         (cell-step true 2 q))
             '(true)))
      (is (= (logic/run* [q]
                         (cell-step true 3 q))
             '(true)))

      (testing "..backwards"
        (is (= (set (logic/run* [q]
                                (fd/in q (fd/interval 2 3))
                                (cell-step true q true)))
               #{2 3}))))

    (testing "rule #3: live cell with more than 3 neighbours die"
      (is (= (logic/run* [q]
                         (cell-step true 4 q))
             '(false)))

      (testing "...backwards"
        (is (= (set (logic/run* [q]
                                (fd/in q (fd/interval 4 8))
                                (cell-step true q false)))
               #{4 5 6 7 8}))))

    (testing "rule #4: a dead cell with exactly 3 neighbours comes alive"
      (is (= (logic/run* [q]
                         (cell-step false 3 q))
             '(true)))

      (testing "...backwards"
        (is (= (logic/run* [q]
                           (cell-step false q true))
               '(3)))))))

(deftest test:cell-step
  (testing "Single cell rules, assumptions"
    (testing "A dead cell with less than three neighbours stays dead"
      (is (= (logic/run* [q]
                         (cell-step false 2 q))
             '(false)))
      (testing "...backwards"
        (is (= (set (logic/run* [q]
                                (fd/in q (fd/interval 0 2))
                                (cell-step false q false)))
               #{0 1 2}))))

    (testing "A dead cell with more than three neighbours stays dead"
      (is (= (logic/run* [q]
                         (cell-step false 4 q))
             '(false)))
      (testing "...backwards"
        (is (= (set (logic/run* [q]
                                (fd/in q (fd/interval 4 8))
                                (cell-step false q false)))
               #{4 5 6 7 8}))))

    (testing "Any dead cell with not exactly 3 neighbours stay dead, backwards"
      (is (= (set (logic/run* [q]
                              (cell-step false q false)))
             #{0 1 2 4 5 6 7 8})))))

(deftest test:cell-step-combined
  (testing "Single cell rules, complex questions"
    ;; Q: If I have a living cell, what states is it possible to arrive to this state?
    ;; A: If it has 3 neighbours, any state results in a living
    ;;    cell, or if the cell was alive, and had two neighbours, it
    ;;    will remain living.
    (is (= (set
            (logic/run* [living? neighbour-count]
                        (cell-step living? neighbour-count true)))
           #{['_0 3] [true 2]}))

    ;; Q: If I have a dead cell, what states is it possible to arrive to this tage?
    (is (= (set
            (logic/run* [living? neighbour-count]
                        (cell-step living? neighbour-count false)))
           #{['_0 0] [false 2] ['_0 1] ['_0 4] ['_0 5] ['_0 6] ['_0 7] ['_0 8]}))

    ;; Q: Show me all the possible states a cell can get!
    (is (= (set
            (logic/run* [living? neighbour-count result]
                        (cell-step living? neighbour-count false)))
           #{['_0 8 '_1] ['_0 7 '_1] ['_0 6 '_1] ['_0 5 '_1]
             ['_0 4 '_1] [false 2 '_0] ['_0 1 '_1] ['_0 0 '_1]}))

    ;; Q: If I have a living cell, what will it evolve to at various
    ;; neighbour counts?
    (is (= (set
            (logic/run* [neighbour-count result]
                        (cell-step true neighbour-count result)))
           #{[3 true] [2 true] [8 false] [7 false] [6 false]
             [5 false] [4 false] [1 false] [0 false]}))))
