# YangYin

YangYin is an extension for [Spock](https://www.spockframework.org) to support snapshot testing. Snapshot testing means comparing current result of a test with a previously store result.

## The idea of snapshot testing

The main idea of snapshot testing is to create a _snapshot_ as soon as you know your code works as expected. After that the _snapshot_ is stored to a file and in every new run your test assures your code is still creating the same data as before. 

## Release Notes

All releases before version 1.x should not be considred stable there might be breaking changes during minor releases.

### 0.92 Changed behaviour for SPOCK_UPDATE

The update behaviour of the system has changed. Now a Comparison is run before each update, only if comparisons fails a new file ist written to the file system. If
SPOCK_UPDATE is given as a parameter all obsolete files in the snaphot folders are removed from the filesystem.

### 0.9 Changed default syntax for comparison

The original static method for Comparisons is no longer the default syntax. Use your own customizeable `Comparison` instances to compare current and snapshot.

A new ArrayComparison for comparing arrays of primitives was added.

### 0.8 Changed behaviour in continous integration server

### 0.7 Introduced image comparison by pixel

### 0.1 Initial release

## Basic Usage

Basic usage is done using the static `FileSnapshots` methods to create a snapshot from the latest api call and compare it with the the result stored in the file systems.

Each method requires a `Comparison` to indicate if objects are equals. There are several Comparison implementations inside yangyin but you can easily use your own implementation.

``` java
def result = classUnderTest.call(...)
FileSnapshots.current(result, new JsonComparison()) == FileSnapshots.snapshot(result, new JsonComparison())
```

## Comparing using power assertion

You can use the power assert feature to reduce the code even further. Cause usually you want to use the same object and the same comparison for creating and retrieving your data. So you can reduce the code to:
``` java
def sample = classUnderTest.call(...)
FileSnapshots.assertSnapshot(sample, new JsonComparison())
``` 

## Design Goals

YangYin was developed having the following goals in mind:

* Even for a testing library the code should be lightweighted without large dependencies. For basic usage the presence of spock, groovy and an up to date JDK should be enough.
* Readable code is prefered over performance optimization. Even if execution time might be optimized or snapshot storage could be more effizient readable code and readable error messages are prefered.
* Snapshot should be human readable. YangYin is designed to store snapshots using common file formats (i.e. .json or .png) so you can use your default system editor to inspect the snapshots. 

## Behaviour and updating

The default behaviour of the library is:

1. If there is an **existing snapshot**, read it and compare it with the latest version.
2. If there is **no snapshot** create a new file in the file systems and pass test

The behaviour in continous integration environment is a little bit different. If running in such an environment (most are detected by default) the test will fail if there is no snapshot in the file system. If your system is not recognized you can add an environment variable _CI_ to express you are running in an continous integration mode.

If you have changed the logic and want to **update** the persistent representation you can provide an environment variable `SPOCK_UPDATE="true"` to force yangyin  to update all existing snapshot files.

## Build in comparisons

YangYin contains some build in comparisons:

### Binary Comparison
Object may be compared using BINARY comparison, in this comparison current and snapshot should be the same, meaning the `byte[]` implementation of snapshot und current must be equals.

### Json Comparison
Comparison can be done using JSON. Meaning the object is stored as json and map representation (after deserializing) must be equals. Your object will be automaticly transformed into a json string, if it is not a string. Keep in mind default json handling is done via `groovy.json.JsonSlurper` so your object must be supported by this class.
``` java
FileSnapshots.assertSnapshot(sample, new JsonComparison()
``` 

The `JsonComparison` has the following options:
* `String[] excludedProperties` A list of properties which should be excluded when comparing objects
* `String[] excludedTypes` A list of types which should be excluded when comparing objects
* `Map<Class,Closure> converter` A map of converters for given types

### PNG Comparison
PNG compares the object as PNG files, treating images of same size (or optional with same pixels) as equal.
To compare image pixel by pixel you should use:
``` java
FileSnapshots.assertSnapshot(sample, new PngComparison(comparisonMode:PngComparison.PIXEL)
``` 
Keep in mind the test might run a while when comparing large images.

The `PngComparison` has the following options:
* `MODE comparisonMode` Indicating that the image should be compared by SIZE (default) or by PIXEL. When comparing by pixel ensure you have enough memory to store the whole image in memory.

### XML Comparison
XML compares the object as XML files, ignoring whitespace and order of attributes.

### Text Comparison
TextComparison compares the object as plain text files, ignoring whitespace but checking all other stuff. 
``` java
FileSnapshots.assertSnapshot(sample, new TextComparison())      
``` 

The `TextComparison` has the following options:
* `boolean ignoreCase` Treat results as equals even if they have different upper/lowercase spelling.

### Array Comparison
ArrayComparison compares an one or two dimension arrya of primitives as CSV file, 

The `ArrayComparison` has the following options:
* `List ignoreValues` A list of all values which should be ignored for comparisons
* `String columnSeparator` The character which should be used as column separator in a csv file
* `int rounding` The number of decimal to keep when storing a float or double value


You can use these implementations as examples for custom comparison by implementing the Comparison interface.

## Default file storage

The library assumes a default maven or gradle file structure. The snapshots are stored in the local file system in a `test/resources` subfolder called `${spec}/snapshots` where `$spec` is a lower case representation of your spec name. The filename is derived from the name of the feature and a postfix of `-1` if there is more than one snapshot test in your feature. There might be a future version where the file location is configureable.
