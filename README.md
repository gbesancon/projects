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