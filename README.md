synesthesy-processing
=====================

A simple interface to synesthesy-core for Processing.
The synesthesy framework gives you a interface to analyze midi-based music events. It delivers functionality to map midi events to music notes, analyze music keys with a trained neuronal network and can determin rythmic changes.

For a feature list see https://github.com/DreamRocker/synesthesy-core

# Installation
1. Build synesthesy-core with maven:

    mvn install

2. Add processing-core library into your maven repo:

    mvn install:install-file -DgroupId=org.processing -DartifactId=processing -Dversion=2.1.1 -Dpackaging=jar -Dfile=/Applications/Processing.app/Contents/Java/core.jar

3. Build it

    mvn install

# Usage

You can find basic examples in `./examples`.

# License
LPGL
