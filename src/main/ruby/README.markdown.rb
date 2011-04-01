require 'rdiscount'
require 'fileutils'

doc = <<-END 
# `clojure.adt`

A dynamic algebraic data type implementation for Clojure.  This library is based on Clojure's hierarchy.  
The ADT hierarchy is accessed through the variable `adt-hierarchy`

## Usage

The main namespace for pattern matching is `clojure.adt.core`. 

    => (use 'clojure.adt.core)

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

Currently the `clj.pattern.match.jar` is located in the bin folder.  If someone would like
to make `clj.pattern.match` available as a Maven artifact via [Clojars](http://clojars.org/) I
would gladly accept that work. Just let me know when it is up and I'll update the docs.

## Development

Running the tests:

    $ lein deps
    $ lein test

## Thanks

This project was inspired by Haskell and OCaml and the various other Clojure
pattern matching libraries.  

## License

Released under the MIT License: <http://www.opensource.org/licenses/mit-license.php>

END

html = RDiscount.new(doc).to_html()

markupFile = File.new("../markdown/README.markdown", "w")
markupFile.write(doc)
markupFile.close
puts "wrote markup to #{File.basename(markupFile.path)}"

readmeFile = File.new("../../../README.markdown", "w")
FileUtils.cp(markupFile, readmeFile)
puts "wrote markup to #{File.basename(readmeFile.path)}"

FileUtils.mkdir_p('../../../target/docs')

testFile = File.new("../../../target/docs/index.html", "w")
testFile.write(html)
testFile.close
puts "wrote html to #{File.basename(testFile.path)}"