# Codurance

## Exercise
   * [CoduranceCodingExercise2017](CoduranceCodingExercise2017.pdf)

## Building the application
  * Linux: ./gradlew build
  * Windows: .\gradlew.bat build
  
## Packaging the application
  * Linux: ./gradlew distZip
  * Windows: .\gradlew.bat distZip

## Running the application
  * IDE: Use main class org.benhur.codurance.Codurance
  * Command line: java -jar build\libs\codurance-0.0.1-SNAPSHOT.jar
  * Distribution: 
     * unzip build/distributions/codurance-0.0.1-SNAPSHOT.zip
     * Linux: execute codurance-0.0.1-SNAPSHOT/bin/codurance
     * Windows: execute codurance-0.0.1-SNAPSHOT/bin/codurance.bat
     
## 2017-08-16: Codurance: Coding Exercise Feedback

Pros:
  * Application has EXIT command;
  * Candidate created a class for Message;
  * Usage of regular expression;
  * Application provides a help message;
  * The message time is properly pluralised;

Cons: 
  * The project name is codurance;
  * ServerTest is testing too much. It is not clear whether the intention was to be a Unit Test or Integration Test;
  * Tests names are generic and not descriptive (e.g. "testCreation");
  * Tests are a too long, not readable, not maintainable;
  * In general, all tests contain duplicate code and string, also magic numbers;
  * DatabaseTest is violating Law of Demeter;
  * CommandLineProcessor contains nested if/else statements;
  * CommandLineProcessor is violating SRP and OCP by:
     * Parsing user input;
     * Handling all commands;
     * Formatting date/time;
     * Formatting output message;
     * Printing message;
