synesthesy-processing
=====================

A simple interface to synesthesy-core for Processing.

# Installation
1. Build synesthesy-core with maven:
   mvn install
2. Add processing-core library into your maven repo:
   mvn install:install-file -DgroupId=org.processing -DartifactId=processing -Dversion=2.0.8 -Dpackaging=jar -Dfile=<PathToProcessing>/core.jar
3. Build it
   mvn install

# License
LPGL
