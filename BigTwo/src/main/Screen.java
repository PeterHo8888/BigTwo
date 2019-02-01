package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Screen extends JPanel implements Runnable
{
	// Constants
	
	// INTERNAL Coordinates
	public static int myWidth  = 800;
	public static int myHeight = 600;
	
	// Objects
	private Thread thread = new Thread(this);
	public static Room   room	  = null;
	
	public static KeyboardHandler kh  = new KeyboardHandler();
	public static Point			  mse = new Point(0, 0);
	
	private boolean isDefined = false;
	public static boolean network = true;
	
	public static String ipAddr = "bigtwo.writemycs.com";
	
	
	public Screen(Frame frame)
	{
		setBackground(Color.PINK);
		define();
		frame.addKeyListener(kh);
		frame.addMouseListener(new MouseHandle());
		frame.addMouseMotionListener(new MouseHandle());
		thread.start();
	}
	
	public void define()
	{
		if (!isDefined) {
		    ipAddr = javax.swing.JOptionPane.showInputDialog(this, "Server Address", ipAddr);
		    if (ipAddr == null)
		        System.exit(0);
		    //System.out.println(ipAddr);
			room = new Room();
			isDefined = true;
		}
	}
	
	public void paintComponent(Graphics g)
	{
		// We draw to real coordinates here
		g.clearRect(0, 0, Frame.width, Frame.height);
		room.draw(g);
	}
	
	public void run()
	{
		define();
		while (true) {
			room.physics();
			repaint();
			try {
				Thread.sleep(67);
			} catch (Exception e) {
				
			}
		}
	}
}
