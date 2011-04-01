(ns com.phansen.clojure.adt.test.core
  (:use [com.phansen.clojure.adt.core])
  (:use [clojure.test])
  (:use [clojure.contrib.pprint]))

(def-adt Tree
  (Bud)
  (Branch left value right))

(is (contains? (descendants adt-hierarchy :adt) :Tree))
(is (= (parents adt-hierarchy :Tree) #{:adt}))
(is (isa? adt-hierarchy :Tree :adt))
(is (isa? adt-hierarchy :Bud :Tree))
(is (isa? adt-hierarchy :Branch :Tree))

(extend-adt Tree
  (Leaf value))

(is (contains? (descendants adt-hierarchy :Tree) :Leaf))
(is (isa? adt-hierarchy :Leaf :Tree))

(un-extend-adt Tree Leaf)

(is (not (isa? adt-hierarchy :Leaf :Tree)))

(let [tree (Branch (Bud) 5 (Bud))]
  (is (adt? tree))
  (is (= :Tree (adt-enclosing-type (type tree)))))