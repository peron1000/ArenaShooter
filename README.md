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
The game is an Eclipse project found in the [`/project`](/project)  directory.
Export the project as a runnable jar and place the [`/project/data`](/project/data) directory next to it.

## Libraries
Libraries are bundled with the project and can be found in [`/project/lib`](/project/lib).
We are currently using:
* [LWJGL 3](https://www.lwjgl.org/) (GLFW, OpenGL, OpenAL, STB)
* [JBox-2D](https://github.com/jbox2d/jbox2d)
* [Log4j 2](https://logging.apache.org/log4j/2.x/)
