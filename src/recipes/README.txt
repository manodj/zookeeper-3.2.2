1) This source directory contains various Zookeeper recipe implementations. 

2) The recipe directory name should specify the name of the recipe you are implementing - eg. lock/.

3) It would be great if you can provide both the java and c recipes for the zookeeper recipes.
The c recipes go in to recipe-name/src/c and the java implementation goes into recipe-name/src/java.

4) The recipes hold high standards like our zookeeper c/java libraries, so make sure that you include
some unit testing with both the c and java recipe code.

5) Also, please name your c client public methods as
zkr_recipe-name_methodname
(eg. zkr_lock_lock in lock/src/c)

6) The various recipes are in ../../docs/recipes.html or
../../docs/reciped.pdf. Also, this is not an exhaustive list by any chance.
Zookeeper is used (and can be used) for more than what we have listed in the docs.
