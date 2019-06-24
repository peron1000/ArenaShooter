# **Architecture**

## _UML_

To see the links between the different java classes and what they do, click on the following link 

![UML](/uml/arenashooterUML.png)

link to the folder : [UML](/uml/)


## _Packages/Hierachy_ 

3 packages structure our project

| engine       |     entities     |        game |
| :------------ | :-------------: | -------------: |

* **engine** : 
  * animation, audio, event, graphics, input, math , physic, ui, util, XML reader/writer,
* **entities**  (Arena,Entity...) : 
  * spatial (character , items, Mesh , soundEffect , light/Rigid/Static bodyContainer... )
* **game** : 
  * GameStates (MenuStart, CharacterChooser,...)
    * editor
    * loading
    * (  engineParam )
    
 
