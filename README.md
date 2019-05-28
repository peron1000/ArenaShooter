![Game logo](/web/res/logo_small.png)
# Super Blep
A fun little 2D local-multiplayer game written in Java. This project is not intended for distribution yet as it still contains copyrighted content
The project will have a fancy website hosted on [Github pages](https://peron1000.github.io/ArenaShooter/)

## Installing a release
Grab a release on our [Github pages](https://peron1000.github.io/ArenaShooter/#download).
Unzip it and launch `Super Blep.jar` as an executable jar.

### Prerequisites
Windows, GNU/Linux or Mac, and a Java 8+ runtime environment

## Building from source
Run `gradlew fatJar` from the [`/project`](/project) directory to produce a build into [`/project/build/libs`](/project/build/libs). We are still working on cleaning Gradle scripts.

## Libraries
We are using Gradle to manage dependencies (libraries are are also available in [`/project/lib`](/project/lib)).
We are currently using:
* [LWJGL 3](https://www.lwjgl.org/) (GLFW, OpenGL, OpenAL, STB)
* [JBox-2D](https://github.com/jbox2d/jbox2d)
* [Log4j 2](https://logging.apache.org/log4j/2.x/)
