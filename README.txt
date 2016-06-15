TUIO MOUSE & TOUCHPAD DRIVER
----------------------------
Copyright (c) 2005-2016 Martin Kaltenbrunner <martin@tuio.org>
This software is part of reacTIVision, an open source fiducial
tracking and multi-touch framework based on computer vision. 
http://reactivision.sourceforge.net/

Usage :
-------
On most platforms you can start the application just by double-clicking the TuioMouse.jar file.
If you want to specify an alternative TUIO port or start the application from the command line,
just type: java -jar TuioMouse.jar [port]
substitute [port] with the desired UDP port number.
The application will run in the background without any GUI.

TuioMouse will use the first available TuioCursor as the principal mouse pointer.
Additional TuioCursor ADD and REMOVE events are treated as mouse button PRESS and RELEASE events.

TuioTouchpad behaves like a notebook touchpad by interpreting 
the cursor velocity as relative displacements.

Source Code:
------------
You can find the source code of the two classes TuioMouse and TuioTouchpad
in the source archive, along with a simple script for compilation.

License:
--------
This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

References:
-----------
This application uses the TUIO Java library.
See http://reactivision.sourceforge.net/
for more information and the source code.
