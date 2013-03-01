(ns logical-playthings.game-of-life-test
  (:use [clojure.test :only [deftest testing is]]
        [logical-playthings.game-of-life :only [cell-step neighbouro]])
  (:require [clojure.core.logic :as logic]
            [clojure.core.logic.fd :as fd]))

(deftest test:cell-step-rules
  (testing "Single cell rules"
    (testing "rule #1: live cell with fewer than 2 neighbours dies"
      (is (= (logic/run* [q]
                         (cell-step :alive 0 q))
             '(:dead)))
      (is (= (logic/run* [q]
                         (cell-step :alive 1 q))
             '(:dead)))

      (testing "...backwards"
        (is (= (set (logic/run* [q]
                                (fd/in q (fd/interval 0 2))
                                (cell-step :alive q :dead)))
               #{0 1}))))

    (testing "rule #2: live cell with 2 or 3 neighbours live on"
      (is (= (logic/run* [q]
                         (cell-step :alive 2 q))
             '(:alive)))
      (is (= (logic/run* [q]
                         (cell-step :alive 3 q))
             '(:alive)))

      (testing "..backwards"
        (is (= (set (logic/run* [q]
                                (fd/in q (fd/interval 2 3))
                                (cell-step :alive q :alive)))
               #{2 3}))))

    (testing "rule #3: live cell with more than 3 neighbours die"
      (is (= (logic/run* [q]
                         (cell-step :alive 4 q))
             '(:dead)))

      (testing "...backwards"
        (is (= (set (logic/run* [q]
                                (fd/in q (fd/interval 4 8))
                                (cell-step :alive q :dead)))
               #{4 5 6 7 8}))))

    (testing "rule #4: a dead cell with exactly 3 neighbours comes alive"
      (is (= (logic/run* [q]
                         (cell-step :dead 3 q))
             '(:alive)))

      (testing "...backwards"
        (is (= (logic/run* [q]
                           (cell-step :dead q :alive))
               '(3)))))))

(deftest test:cell-step
  (testing "Single cell rules, assumptions"
    (testing "A dead cell with less than three neighbours stays dead"
      (is (= (logic/run* [q]
                         (cell-step :dead 2 q))
             '(:dead)))
      (testing "...backwards"
        (is (= (set (logic/run* [q]
                                (fd/in q (fd/interval 0 2))
                                (cell-step :dead q :dead)))
               #{0 1 2}))))

    (testing "A dead cell with more than three neighbours stays dead"
      (is (= (logic/run* [q]
                         (cell-step :dead 4 q))
             '(:dead)))
      (testing "...backwards"
        (is (= (set (logic/run* [q]
                                (fd/in q (fd/interval 4 8))
                                (cell-step :dead q :dead)))
               #{4 5 6 7 8}))))

    (testing "Any dead cell with not exactly 3 neighbours stay dead, backwards"
      (is (= (set (logic/run* [q]
                              (cell-step :dead q :dead)))
             #{0 1 2 4 5 6 7 8})))))

(deftest test:cell-step-combined
  (testing "Single cell rules, complex questions"
    ;; Q: If I have a living cell, what states is it possible to arrive to this state?
    ;; A: If it has 3 neighbours, any state results in a living
    ;;    cell, or if the cell was alive, and had two neighbours, it
    ;;    will remain living.
    (is (= (set
            (logic/run* [living? neighbour-count]
                        (cell-step living? neighbour-count :alive)))
           #{['_0 3] [:alive 2]}))

    ;; Q: If I have a dead cell, what states is it possible to arrive to this tage?
    (is (= (set
            (logic/run* [living? neighbour-count]
                        (cell-step living? neighbour-count :dead)))
           #{['_0 0] [:dead 2] ['_0 1] ['_0 4] ['_0 5] ['_0 6] ['_0 7] ['_0 8]}))

    ;; Q: Show me all the possible states a cell can get!
    (is (= (set
            (logic/run* [living? neighbour-count result]
                        (cell-step living? neighbour-count :dead)))
           #{['_0 8 '_1] ['_0 7 '_1] ['_0 6 '_1] ['_0 5 '_1]
             ['_0 4 '_1] [:dead 2 '_0] ['_0 1 '_1] ['_0 0 '_1]}))

    ;; Q: If I have a living cell, what will it evolve to at various
    ;; neighbour counts?
    (is (= (set
            (logic/run* [neighbour-count result]
                        (cell-step :alive neighbour-count result)))
           #{[3 :alive] [2 :alive] [8 :dead] [7 :dead] [6 :dead]
             [5 :dead] [4 :dead] [1 :dead] [0 :dead]}))))

(deftest test:neighbouro
  (testing "How to find neighbours"
    (is (= (set
            (logic/run* [neighbour]
                        (neighbouro [0 0] neighbour)))
           #{[1 -1] [1 0] [-1 -1] [0 -1] [-1 0] [1 1] [-1 1] [0 1]}))

    (is (= (set
            (logic/run* [cell]
                        (neighbouro cell [1 1])))
           #{[2 1] [1 0] [2 2] [0 0] [0 1] [1 2] [0 2] [2 0]}))))
