# Architecture

## UML

To see the links between the different java classes and what they do, click on the following link 

![UML](/uml/arenashooterUML.png)

link to the folder : [UML](/uml/)


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
