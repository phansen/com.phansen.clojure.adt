(ns com.phansen.clojure.adt.core
  "Contains functions and macros to work with algebraic data types"
  (:use [clojure.contrib.core :only (dissoc-in)]))

(def 
  ^{:doc "The hierarchy used to store Algebraic Data Type relationships"}
  adt-hierarchy (make-hierarchy))

(def
  ^{:doc "A map that stores for each Algebraic Data Type the type constructors 
          and a vector of the type constructor arguments as keywords (in the order 
          provided by type constructor definition"}
   adt-types {})

(defn- emit-derives
  "Generates a derive statemtent that makes the type-constructor a sub-type of its ADT"
  [name type-constructors]
  (let [adt-type (keyword name)]
    (->> type-constructors
      (map first )
      (map keyword )
      (map (fn [instance-type] `(alter-var-root #'adt-hierarchy derive ~instance-type ~adt-type)) ))))

(defn- emit-type-constructor-fns
  "Generates a (defn ...) which produces a map that represents
   the ADT instance.  The map has meta-data on it that represents the instance type."
  [type-constructors]
  (->> type-constructors
    (map (fn [[name & args]]
           `(defn ~name [~@args]
              (let [meta-map# {:type ~(keyword name)}]
                (with-meta (hash-map ~@(interleave (map keyword args) args))
                  meta-map#)))) )))

(defn- emit-type-constructor-info 
  "Generates statements that update the adt-types map for each type-constructor"
  [name type-constructors]
  (let [adt-type (keyword name)]
    (->> type-constructors
      (map (fn [[instance-type & args]] 
             `(alter-var-root #'adt-types assoc-in [~adt-type ~(keyword instance-type)] [~@(map keyword args)])) ))))

(defmacro def-adt 
  "Defines an Algebraic Data Type in terms of its type constructors and hierarchy.
   For example, the ADT Tree (def-adt Tree
                               (E)
                               (T left value right))
   defines two type constructors that become the following functions
   (defn E []
     ...)
   (defn T [left value right]
     ...)

   The hierarchy made by the Tree modifies the adt-hierarchy by deriving :Tree from :adt.  
   :adt is the root of all algebraic data types.
   
   The type constructors also modify the default global hierarchy by 
   deriving :E from :Tree and :T from :Tree.
  "
  [name & type-constructors]
    `(do 
       (alter-var-root #'adt-hierarchy derive ~(keyword name) :adt)
       (extend-adt ~name ~@type-constructors)))

(defmacro extend-adt 
  "Given an existing Algebraic Data Type (name) extends the data type to include 
   the provided types and their constructors"
  [name & type-constructors]
  `(do
     ~@(emit-derives name type-constructors)
     ~@(emit-type-constructor-fns type-constructors)
     ~@(emit-type-constructor-info name type-constructors)))

(defn- emit-underives
  "Generates statements to underive type-names from name"
  [name type-names]
  (->> type-names
    (map keyword )
    (map (fn [instance-type]
           `(alter-var-root #'adt-hierarchy underive ~instance-type ~(keyword name))) )))

(defn- emit-remove-type-constructors
  "Generates statements that remove the type-constructors from adt-types"
  [name type-names]
  (->> type-names
    (map keyword )
    (map (fn [instance-type]
           `(alter-var-root #'adt-types dissoc-in [~(keyword name) ~instance-type])) )))

(defmacro un-extend-adt
  "Given an existing Algebraic Data Type (name) and a list of type-constructor names
   underives and removes the type-constructors from adt-types"
  [name & type-names]
  `(do
     ~@(emit-underives name type-names)
     ~@(emit-remove-type-constructors name type-names)))

(defn adt-enclosing-type [adt-type]
  (->> adt-types 
    (filter (fn [[enclosing-type value-types]] (get value-types adt-type)) )
    ((fn [[[enclosing-type value-type] & _]] enclosing-type) )))
  
(defn adt? 
  "true if adt isa? member of adt-hierarchy"
  [adt]
  (isa? adt-hierarchy (type adt) :adt))