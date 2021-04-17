[![License LGPLv3][LGPLv3 badge]][LGPLv3]
[![License ASL 2.0][ASL 2.0 badge]][ASL 2.0]
[![Build Status][Travis badge]][Travis]
![Maven Central](https://img.shields.io/maven-central/v/com.github.java-json-tools/json-schema-validator.svg)
# SENG 401 Project: Group 16. Repo #1: JSON Schema Validator
***
### Members: Steve Khanna, Abid Al Labib, David Macababayao, Ragya Mittal, Cobe Reinbold, Long Tran
***
## System description
JSON Schema Validator takes some JSON Schema and String input to validate whether or not the input is correctly formatted JSON relative to the Schema. The current version provides automatic support for JSON Scheman Draft versions 3 and 4. It is possible to give the program a custom Schema to validate input against. In such a case, the Schema will also be validated for any errors or redunduncy.

## Design Patterns
Implementing two new design patterns for this software system was not an easy task, since the system code is already well written. We noticed that the code base uses lots of design patterns in conjuction with each other implying that specific design choices were made during its evolution. However, we have found a couple of sections that we could implement design patterns on.

The 2 design patterns we implemented are the **Factory method** and the **Decorator pattern**.
***
## 1. Factory Method (Worked on by Steve Khanna and David Macababayao)
Factory method pattern allows for providing an interface for creating subclass instances of an object where the subclasses have different implementations of the abstract class.

### What prompted us to choose this:
#### Context:
The first version of the code base provides support only for version 3 of the JSON Schema Draft. Later on, additional functionality was added to provide support for version 4. During our evaluation of the code base, it was clear to us that the same level of care taken to create the first version of the program wasn't present when updating the code base. Specifically, we noticed that instead of refactoring the codebase completely to fit more schema versions, the developers essentially copy-pasted the common functions between Draftv3 and Draftv4 by creating new Draftv4 specific classes. The main idea that prompted this choice was to reduce the amount of code duplication and use what we had learned in SENG 401 to refactor the code base for the better.

#### Details:
We initially saw quite a bit of similarities between DraftV3DependenciesDigester and DraftV4DependenciesDigester, so we decided to implement the factory method. We abstracted the similar code into an abstract class called the AbstractDigester and this is what the subclasses (DraftV3 and DraftV4 dependencies digester) use to instantiate their objects (The AbstractDigester contains the implementation of the Digesters). This lets the AbstractDigester defer its instantiation to the DraftV3DepDigest and DraftV4DepDigest.

At runtime the Dependencies Digesters are called to be instantiated in the DraftV3DigesterDictionary and DraftV4DigesterDictionary. We saw that the DraftV3DigesterDictionary and DraftV4DigesterDictionary also contained similar code and had similar functionalities. And these two could be merged into a factory class, so we created the DictionaryFactory that would contain DraftV3DigesterDictionary and DraftV4DigesterDictionary. We merged the two classes in the DictionaryFactory class with the Draft V3 and V4 Dictionaries represented through an enum and implementing a switch statement that would decide which of the two dictionaries is being used at runtime.

Implementing the Factory method on this code provided us an interface to create the Dependencies Digester objects without specifying their concrete class (AbstractDigester). This also reduced the duplicate code from the dependencies digesters and the digester dictionaries.

### Implementation and Refactoring

#### Classes Added
* **Dv3DepDigest**
  * Removed Similarities with DraftV4DependenciesDigester
  * Subclass of AbstractDigester
  * Implements the DraftV3DependenciesDigester
* **Dv4DepDigest**
  * Removed Similarities with DraftV3DependenciesDigester
  * Subclass of AbstractDigester
  * Implements the DraftV4DependenciesDigester
* **DictionaryFactory**
  * Contains DraftV3DigesterDictionary and DraftV4DigesterDictionary

#### Classes Deleted
* **DraftV3DependenciesDigester**
* **DraftV4DependenciesDigester**
* **DraftV3DigesterDictionary**
  * Merged with DraftV4DigesterDictionary into DictionaryFactory
* **DraftV4DigesterDictionary**
  * Merged with DraftV4DigesterDictionary into DictionaryFactory

#### Classes Changed
* **AbstractDigester**
  * Added similarities between DraftV3DependenciesDigester and DraftV4DependenciesDigester
***
## 2. Decorator Pattern (Worked on by Abid Al Labib and Cobe Reinbold)
The Decorator Pattern allows for adding new behaviours to objects in the system dynamically.

### What prompted us to choose this:
#### Context:
More than just being a library to validate some input, JSV also works a Command Line Interface. One of the options for the CLI allows the user a description of the validation. Specifically: DEFAULT, BRIEF, and QUIET. We wanted to add another option that is a combination of the Brief and Quiet option. Particularly, the Brief option for the Schema and the Quiet option for the input.

#### Details:
The Reporter is an interface that is used to output errors that are encountered when evaluating the given schema/instance. This ultimately reports on whether the input is valid or not. The Reporter interface has two methods, and these two methods are implemented by different types of (enum) reporters. These enumerations are DEFAULT, BRIEF, QUIET which have different implementations of the Reporter interface.

The reason for the Decorator pattern is that we wanted to add another behaviour. Specifically, we wanted to ensure that the user is able to specify two different behaviours - one for the schema and one for the instance - at runtime. We are trying to make the code such that we could add additional behaviours to the reporter interface dynamically, and to do this, we decided to use the decorator pattern because it's about adding additional behaviours dynamically. Note: we recognize that we could have just added another enum, however we added the decorator pattern to satisfy the constraints of this project.

We created the AbstractReporter class which would contain the implemented reporter types/behaviours. The Reporter behaviour/type that we wanted to add dynamically is called the NotJustBrief Reporter. The NotJustBrief Reporter contains a different implementation of the Reporter methods from the
implemented types/behaviours. We added a decorator class that would allow us to add the new Reporter type/behaviour (NotJustBrief) dynamically.

### Implementation and Refactoring

#### Classes Added
* **AbstractReporter**
  * Contains the already implemented Reporter types/behaviours
* **NotJustBriefDecorator**
  * Decorator class
  * Contains the new Reporter types/behaviours added at runtime

#### Classes Deleted
* **Reporters**
  * Refactored into AbstractReporter

#### Classes Changed
* **Main**
  * Changed how the Reporter is called (integrating the design pattern)

***
## Read me first

The **current** version of this project is licensed under both [LGPLv3] (or later) and [ASL 2.0]. The old version
(2.0.x) was licensed under [LGPL 3.0][LGPLv3] (or later) only.

**Version 2.2 is out**. See [here](https://github.com/java-json-tools/json-schema-validator/wiki/Whatsnew_22)
for the list of changes compared to 2.0. And of course, it still has [all the
features](https://github.com/java-json-tools/json-schema-validator/wiki/Features) of older versions.

## What this is

This is an implementation with complete validation support for the latest JSON Schema draft (v4,
including hyperschema syntax support) and the previous draft (v3 -- no hyperschema support though).
Its list of features would be too long to enumerate here; please refer to the links above!

Should you wonder about it, this library is reported to [work on
Android](http://stackoverflow.com/questions/14511468/java-android-validate-string-json-against-string-schema).
Starting with version 2.2.x, all APK conflicts have been resolved, so you can use this in this
context as well.

## Google Group

This project has a dedicated [Google
group](https://groups.google.com/forum/?fromgroups#!forum/json-schema-validator). For any questions
you have about this software package, feel free to post! The author (me) will try and respond in a
timely manner.

## Testing online

You can [test this library online](http://json-schema-validator.herokuapp.com); this web site is in
a [project of its own](https://github.com/fge/json-schema-validator-demo), which you can fork and
run by yourself.

## Versions

* current stable version: **2.2.14**
  ([ChangeLog](https://github.com/java-json-tools/json-schema-validator/wiki/ChangeLog_22x),
  [Javadoc](http://java-json-tools.github.io/json-schema-validator/2.2.x/index.html), [code
  samples](http://java-json-tools.github.io/json-schema-validator/2.2.x/index.html?com/github/fge/jsonschema/examples/package-summary.html)).
* old stable version: **2.0.4**
  ([ChangeLog](https://github.com/java-json-tools/json-schema-validator/wiki/ChangeLog_20x),
  [Javadoc](http://java-json-tools.github.io/json-schema-validator/2.0.x/index.html), [code
  samples](http://java-json-tools.github.io/json-schema-validator/2.0.x/index.html?com/github/fge/jsonschema/examples/package-summary.html)).

## Available downloads

### Gradle/maven

This package is available on Maven central; the artifact is as follows:

Gradle:

```groovy
dependencies {
    compile(group: "com.github.java-json-tools", name: "json-schema-validator", version: "2.2.14");
}
```

Maven:

```xml
<dependency>
    <groupId>com.github.java-json-tools</groupId>
    <artifactId>json-schema-validator</artifactId>
    <version>2.2.14</version>
</dependency>
```

### "Full" jar; command line
OUTDATED: Let me know if you need this in the issues section.

This jar contains the library plus all its dependencies. Download the **lib** jar (a little more
than 6 MiB) from [Bintray](https://bintray.com/fge/maven/json-schema-validator/view).

## Versioning scheme policy

The versioning scheme is defined by the **middle digit** of the version number:

* if this number is **even**, then this is the **stable** version; no new features will be
  added to such versions, and the user API will not change (save for some additions if requested).
* if this number is **odd**, then this is the **development** version; new features will be
  added to those versions only, **and the user API may change**.

## Relevant documents

This implementation is based on the following drafts:

* [JSON Schema Internet draft, version 4](http://tools.ietf.org/html/draft-zyp-json-schema-04)
  ([link to validation spec](http://tools.ietf.org/html/draft-fge-json-schema-validation-00));
* [JSON Schema Internet draft, version 3](http://tools.ietf.org/html/draft-zyp-json-schema-03);
* [JSON Reference Internet draft, version
  3](http://tools.ietf.org/html/draft-pbryan-zyp-json-ref-03);
* [JSON Pointer (RFC 6901)](http://tools.ietf.org/html/rfc6901).

## More...

For a detailed discussion of the implementation, see
[here](https://github.com/java-json-tools/json-schema-validator/wiki/Status).

Please see the [wiki](https://github.com/java-json-tools/json-schema-validator/wiki/) for more
details.

[LGPLv3 badge]: https://img.shields.io/:license-LGPLv3-blue.svg
[LGPLv3]: http://www.gnu.org/licenses/lgpl-3.0.html
[ASL 2.0 badge]: https://img.shields.io/:license-Apache%202.0-blue.svg 
[ASL 2.0]: http://www.apache.org/licenses/LICENSE-2.0.html
[Travis Badge]: https://travis-ci.com/java-json-tools/json-schema-validator.svg?branch=master
[Travis]: https://travis-ci.com/java-json-tools/json-schema-validator
