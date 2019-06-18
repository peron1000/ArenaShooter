# Installation and building instructions
## Installing a release
### Prerequisites
* Windows, GNU/Linux or Mac
* A JRE or JDK with Java 8 support
* OpenGL 3.2 support
### Installing
Grab a release on [`/release`](/release).
Unzip it and launch `Super Blep.jar` as an executable jar.

## Building from source
### Prerequisites
* A JDK with Java 8 support
### Building
After cloning the repository,
* Run `gradlew zipGame` (`gradlew.bat zipGame` on Windows) from the [`/project`](/project) directory to produce an executable jar and zip it with game data into `/project/build/ditrubutions`.

or
* Run `gradlew fatJar` (`gradlew.bat fatJar` on Windows) from the [`/project`](/project) directory to only produce the jar and copy game data into `/project/build/libs`.
