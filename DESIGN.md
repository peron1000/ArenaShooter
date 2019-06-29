# Architecture

## UML

Here is a (simplified) UML diagram of the project

![Simplified UML](/uml/arenashooterUML.png)

Link to the folder : [UML](/uml/)


## Packages structure

The project is structured in 3 main packages:

| engine       |     entities     |        game |
| :------------ | :-------------: | -------------: |

* engine : 
  * animation 
  * audio 
  * events (input and UI related events)
  * graphics 
    * particles
      * modules (parts of particle emitters)
  * input 
  * math (Vectors, Quaternions, Matrices)
  * physic 
  * ui 
  * util 
  * XML reader/writer
* entities  (Arena, Entity...) : 
  * spatial (Character, Mesh, SoundEffect, Light, Physic bodies... )
    * items
* game : 
  * GameStates (MenuStart, CharacterChooser...)
    * editor
    * loading
    * engineParam

    
## Libraries
We are using Gradle to manage dependencies (libraries are are also available in [`/project/lib`](/project/lib)).

We are currently using:
* [LWJGL 3](https://www.lwjgl.org/) (GLFW, OpenGL, OpenAL, STB)
* [JBox-2D](https://github.com/jbox2d/jbox2d)
* [Log4j 2](https://logging.apache.org/log4j/2.x/)
