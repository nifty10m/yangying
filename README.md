# YangYing

YangYing is an extension for [Spock](https://www.spockframework.org) to support snapshot testing. Snapshot testing means comparing current result of a test with a previously store result.

## The idea of snapshot testing

The main idea of snapshot testing is to create a _snapshot_ as soon as you know your code is working as expected. After that the snapshot is stored to a file and in every new run your test assures your code is still creating the same data as before. 

## Release Notes

### 0.9 Changed default syntax for comparison

The original static method for Comparisons is no longer the default syntax. Use your own customizeable `Comparison` instances to compare current and snapshot.

### 0.8 Changed behaviour in continous integration server

### 0.7 Introduced image comparison by pixel

### 0.1 Initial release

## Basic Usage

Basic usage is done using the static `FileSnapshots` methods to create a snapshot from the latest api call and compare it with the the result store in the file systems.

Each method requires a `Comparison` to indicate when to objects are equals. There are several Comparison implementations inside yangying but you can easily use your own implementation.

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

## Behaviour and updating

The default behaviour of the library is:

1. If there is an existing snapshot, read it and compare it with the latest version.
2. If there is no snapshot create a new file in the file systems and pass test

The behaviour in continous integration environment is a little bit different. If running in such an environment (most are detected by default) the test will fail if there is no snapshot in the file system. If your system is not recognized you can add an environment variable _CI_ to express you are running in an continous integration mode.

If you have changed the logic and want to **update** the persistent representation you can provide an environment variable `SPOCK_UPDATE="true"` to force yangying to update all existing snapshot files.

## Build in comparisons

YingYang contains some build in comparisons:

### Binary Comparison
Object may be compared using BINARY comparison, in this comparison current and snapshot should be the same, meaning the `byte[]` implementation of snapshot und current must be equals.

### Json Comparison
Comparison can be done using JSON. Meaning the object is stored as json and map representation (after deserializing) must be equals. Your object will be automaticly transformed into a json string, if it is not a string. Keep in mind default json handling is done via `groovy.json.JsonSlurper` so your object must be supported by this class.
``` java
FileSnapshots.assertSnapshot(sample, new JsonComparison()
``` 
### PNG Comparison
PNG compares the object as PNG files, treating images of same size (or optional with same pixels) as equal.
To compare image pixel by pixel you should use:
``` java
FileSnapshots.assertSnapshot(sample, new PngComparison(comparisonMode:PngComparison.PIXEL)
``` 
Keep in mind the test might run a while when comparing large images.

### XML Comparison
XML compares the object as XML files, ignoring whitespace and order of attributes.

### Text Comparison
Thne TextComparison compares the object as plain text files, ignoring whitespace but checking all other stuff. 
``` java
FileSnapshots.assertSnapshot(sample, new TextComparsion())      
``` 
You can use these implementations as examples for custom comparison by implementing the Comparison interface.

## Default file storage

The library assumes a default maven or gradle file structure. The snapshots are stored in the local file system in a `test/resources` subfolder called `${spec}/snapshots` where `$spec` is a lower case representation of your spec name. The filename is derived from the name of the feature and a postfix of `-1` if there is more than one snapshot test in your feature. There might be a future version where the file location is configureable.
