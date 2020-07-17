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
import java.net.*;
import java.io.*;
import java.util.*;
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
		System.setProperty("apple.awt.UIElement", "true");
		java.awt.Toolkit.getDefaultToolkit();
 
		if (argv.length==1) {
			try { port = Integer.parseInt(argv[1]); }
			catch (Exception e) { System.out.println("usage: java TuioTouchpad [port]"); }
		}

 		TuioTouchpad touchpad = new TuioTouchpad();
		final TuioClient client = new TuioClient(port);

		System.out.println("listening to TUIO messages at port "+port);
		client.addTuioListener(touchpad);
		client.connect();
		
		if (SystemTray.isSupported()) {
			
			final PopupMenu popup = new PopupMenu();
			final TrayIcon trayIcon =
			new TrayIcon(Toolkit.getDefaultToolkit().getImage(touchpad.getClass().getResource("tuio.gif")));
			trayIcon.setToolTip("Tuio Touchpad");
			final SystemTray tray = SystemTray.getSystemTray();

            final MenuItem ipAddress = new MenuItem("IP Address:");
            popup.add(ipAddress);

            try {
                Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
                while (nics.hasMoreElements()) {
                    NetworkInterface nic = nics.nextElement();
                    Enumeration<InetAddress> addrs = nic.getInetAddresses();
                    while (addrs.hasMoreElements()) {
                        InetAddress addr = addrs.nextElement();
                        if (!addr.isLoopbackAddress() && (!addr.isLinkLocalAddress() || addr.toString().startsWith("/169.254"))) {
                            popup.add(new MenuItem("    " + addr.getHostAddress()));
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }

            popup.addSeparator();
            popup.add("Port: " + port);

			final CheckboxMenuItem pauseItem = new CheckboxMenuItem("Pause");
			final MenuItem exitItem = new MenuItem("Exit");
			
			popup.add(pauseItem);
			pauseItem.addItemListener( new ItemListener() { public void itemStateChanged(ItemEvent evt) {
				
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					if (client.isConnected()) client.disconnect();
				} else {
					if (!client.isConnected()) client.connect();
				}
			} } );
			popup.add(exitItem);
			exitItem.addActionListener( new ActionListener() { public void actionPerformed(ActionEvent evt) {
				client.disconnect();
				System.exit(0);
			} } );
			
			trayIcon.setPopupMenu(popup);
			
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.out.println("SystemTray could not be added.");
			}
			
		} else System.out.println("SystemTray is not supported");

	}
}
