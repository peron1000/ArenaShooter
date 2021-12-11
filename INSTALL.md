# Installation and building instructions
## Installing a release
### Prerequisites
* Windows, GNU/Linux or Mac
* A JRE or JDK with Java 8 support
* OpenGL 3.2 support
### Installing
Grab a release on the [releases](https://github.com/peron1000/ArenaShooter/releases) page.

Unzip it and launch `Super Blep.jar` as an executable jar.

If you encounter issues running the game, check our [wiki](https://github.com/peron1000/ArenaShooter/wiki/Troubleshooting).

## Building from source
### Prerequisites
* A JDK with Java 8 support
### Building
After cloning the repository,
* Run `gradlew fatjar` (`gradlew.bat fatjar` on Windows) from the [`/project`](/project) directory to produce an executable jar into `/project/build/libs`.

If you previously built another version, it is recommended to run `gradlew clean` (`gradlew.bat clean` on Windows) before building.
