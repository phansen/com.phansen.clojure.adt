# `com.phansen.clojure.adt`

A dynamic algebraic data type implementation for Clojure.  This library is based on Clojure's hierarchy.  
The ADT hierarchy is accessed through the variable `adt-hierarchy`

## Usage

The main namespace for pattern matching is `com.phansen.clojure.adt.core`. 

    => (use 'com.phansen.clojure.adt.core)

## Defining types

To define your own ADT use the `def-adt` macro

    => (def-adt Tree
         (E)
         (T left value right))

This will define an ADT `Tree` that contains types `E` and `T`.  `E` is a nullary
type and has no values.  `T` has values `left`, `value`, and `right`.  

The following type-constructors are `def`'d by the `def-adt` macro

(defn E [] ...)

(defn T [left value right] ...)  

The ADT system supports the dynamic nature of Clojure's hierarchy.  You can dynamically 
extend and un-extend types.  You can extend a type by using the `extend-adt` macro

    => (extend-adt Tree
         (Leaf value))

This will extend ADT `Tree` to now include `Leaf` as a type of `Tree`.  To remove Leaf
from `Tree` use the `un-extend-adt` macro

    => (un-extend-adt Tree Leaf value)

## Installation



## Thanks

This project was inspired by Haskell, OCaml, etc... algebraic data types

## License

Released under the MIT License: <http://www.opensource.org/licenses/mit-license.php>

