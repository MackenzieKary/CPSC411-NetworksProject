ReadMe
Schedule is a application designed to help a student fill a time grid
(in this case a week) with lectures. This is done with a VMC model. The View 
is imported from the gui package. The Model is imported from the logic package. 
The controller uses listeners and event driven programming to link the two.
The model contains the text file test.txt which holds all the raw information for 
the lectures to be added in the form of a single line containing all of the information.
This text file can be replaced with any other, as long as it follows the correct format 
for the lecture(String) constructor. However this text file must be in the same directory
as the Controller class

To run this program go into the directory containing controller, in terminal or
command prompt compile all files in logic, gui, and the controller:

1) dir\javac gui\*.java
2) dir\javac logic\*.java
3) dir\javac Controller.java

Then run controller:

dir\java Controller

We attempted to use an additional mainproject package, however when this was done 
outside of eclipse, the program was unable to find the text file containing all of the 
lecture information.