# YangYing

YangYing is an extension for [Spock](https://www.spockframework.org) to support snapshot testing. Snapshot testing means comparing current result of a test with a previously store result.

## Basic Usage

Basic usage is done using the static `FileSnapshost` methods to create a snapshot from the latest api call and compare it with the the result store in the file systems.

Each method requires a `Comparison` to indicate when to objects are equals. A basic implmentation comparison objects as json is provided using the `Comparisons` class.

``` java
def result = classUnderTest.call(...)
FileSnapshots.current(result, Comparisons.JSON) == FileSnapshots.snapshot(result, Comparisons.JSON)
```

If you use static import you can just write
``` java
def result = classUnderTest.call(...)
current(resultm, JSON) == snapshot(result, JSON)
```

## Default behaviour and updating

The default behaviour of the library is:

1. If there is an existing snapshot, read it and compare it with the latest version.
2. If there is no snapshot create a new file in the file systems and pass test

If you have changed the logic and want to **update** the persistent representation you can privde an environment variable `SPOCK_UPDATE="true"` to force yangying to update all existing snapshot files.

## Build in comparisons

YingYang contains some build in comparisons:
1. Object may be compared using BINARY comparison, in this comparison current and snapshot should be the same, meaning the `byte[]` implementation of snapshot und current must be equals.
2. Comparison can be done using JSON. Meaning the object is stored as json and map representation (after deserializing) must be equals. Your object will be automaticly transformed into a json string, if it is not a string. Keep in mind default json handling is done via `groovy.json.JsonSlurper` so your object must be supported by this class. If your
3. JSON_API is almost the same as JSON but does ignore properties like `id or `createdDate` in comparison.
4. PNG compares the object as PNG files, treating images of same size as equal.

You can use these implementations as examples for custom comparison by implementing the Comparison interface.

## Default file storage

The library assumes a default maven or gradle file structure. The snapshots are stored in the local file system in a `test/resources` subfolder called `${spec}/snapshots` where `$spec` is a lower case representation of your spec name. The filename is derived from the name of the feature and a postfix of `-1` if there is more than one snapshot test in your feature.
