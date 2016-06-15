#!/bin/sh
javac -Xlint:-options -Xlint:unchecked -O -source 1.6 -target 1.6 -cp ../libTUIO.jar TuioMouse.java
jar cfm ../TuioMouse.jar mouseManifest.inc *.class tuio.gif
rm -f *.class
javac -Xlint:-options -Xlint:unchecked -O -source 1.6 -target 1.6 -cp ../libTUIO.jar TuioTouchpad.java
jar cfm ../TuioTouchpad.jar touchpadManifest.inc *.class tuio.gif
rm -f *.class
