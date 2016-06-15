/*
    TUIO Trackpad Driver - part of the reacTIVision project
    http://reactivision.sourceforge.net/

    Copyright (c) 2005-2016 Martin Kaltenbrunner <martin@tuio.org>

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
*/

import java.awt.*;
import java.awt.event.*;
import TUIO.*;

public class TuioTouchpad implements TuioListener {
	
	private Robot robot = null;
	private int width = 0;
	private int height = 0;
	private long mouse = -1;

	public void addTuioObject(TuioObject tobj) {}
	public void updateTuioObject(TuioObject tobj) {}	
	public void removeTuioObject(TuioObject tobj) {}
	public void addTuioBlob(TuioBlob tblb) {}
	public void updateTuioBlob(TuioBlob tblb) {}	
	public void removeTuioBlob(TuioBlob tblb) {}
	public void refresh(TuioTime bundleTime) {}
	
	public void addTuioCursor(TuioCursor tcur) {
		
		Point pos = MouseInfo.getPointerInfo().getLocation();
		int xpos = pos.x+(int)Math.round(tcur.getXSpeed()*Math.sqrt(width));
		if(xpos<0) xpos=0; if(xpos>width) xpos=width;
		int ypos = pos.y+(int)Math.round(tcur.getYSpeed()*Math.sqrt(height));
		if(ypos<0) ypos=0; if(ypos>height) ypos=height;
		
		if (mouse<0) {
			mouse = tcur.getSessionID();
			if (robot!=null) robot.mouseMove(xpos,ypos);
		} else {
			if (robot!=null) robot.mousePress(InputEvent.BUTTON1_MASK);
		}
	}

	public void updateTuioCursor(TuioCursor tcur) {
		Point pos = MouseInfo.getPointerInfo().getLocation();
		int xpos = pos.x+(int)Math.round(tcur.getXSpeed()*Math.sqrt(width));
		if(xpos<0) xpos=0; if(xpos>width) xpos=width;
		int ypos = pos.y+(int)Math.round(tcur.getYSpeed()*Math.sqrt(height));
		if(ypos<0) ypos=0; if(ypos>height) ypos=height;
		if (mouse==tcur.getSessionID()) {
			if (robot!=null) robot.mouseMove(xpos,ypos);
		} 
	}
	
	public void removeTuioCursor(TuioCursor tcur) {
		if (mouse==tcur.getSessionID()) {
			mouse=-1;
		} else {
			if (robot!=null) robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		
	}
	
	public TuioTouchpad() {
		try { robot = new Robot(); }
		catch (Exception e) {
			System.out.println("failed to initialize mouse robot");
			System.exit(0);
		}
		
		width  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}

	public static void main(String argv[]) {
	
		int port = 3333;
 
		if (argv.length==1) {
			try { port = Integer.parseInt(argv[1]); }
			catch (Exception e) { System.out.println("usage: java TuioTouchpad [port]"); }
		}

 		TuioTouchpad trackpad = new TuioTouchpad();
		TuioClient client = new TuioClient(port);

		System.out.println("listening to TUIO messages at port "+port);
		client.addTuioListener(trackpad);
		client.connect();
	}
}
