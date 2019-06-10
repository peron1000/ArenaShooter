![Game logo](/web/res/logo_small.png)
# Super Blep
A fun little 2D local-multiplayer game written in Java.

This project is not intended for distribution yet as it still contains copyrighted content.

The project will have a fancy website hosted on [Github pages](https://peron1000.github.io/ArenaShooter/)

## Installing a release
### Prerequisites
* Windows, GNU/Linux or Mac
* A JRE or JDK with Java 8 support
* OpenGL 3.2 support
### Installing
Grab a release on our [Github pages](https://peron1000.github.io/ArenaShooter/#download).
Unzip it and launch `Super Blep.jar` as an executable jar.

## Building from source
### Prerequisites
* A JDK with Java 8 support
### Building
Run `gradlew zipGame` from the [`/project`](/project) directory to produce an executable jar and zip it with game data into `/project/build/ditrubutions`.

Run `gradlew fatJar` from the [`/project`](/project) directory to only produce the jar and copy game data into `/project/build/libs`.

We are still working on cleaning Gradle script.

## Libraries
We are using Gradle to manage dependencies (libraries are are also available in [`/project/lib`](/project/lib)).

We are currently using:
* [LWJGL 3](https://www.lwjgl.org/) (GLFW, OpenGL, OpenAL, STB)
* [JBox-2D](https://github.com/jbox2d/jbox2d)
* [Log4j 2](https://logging.apache.org/log4j/2.x/)
