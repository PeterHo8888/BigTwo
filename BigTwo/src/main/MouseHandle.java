package main;

import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import utilities.Coordinates;

public class MouseHandle implements MouseMotionListener, MouseListener
{

    public static boolean clicked = false;

    private void setMse(MouseEvent e)
    {
        Insets i = Frame.frame.getInsets();
        int x_a = e.getX();
        int y_a = e.getY() - i.top - Frame.menuBar.getHeight();

        Coordinates internal = Coordinates.toInternal(x_a, y_a);
        int x = internal.getX();
        int y = internal.getY();
        Screen.mse = new Point(x, y);
    }

    // No use
    public void mouseDragged(MouseEvent e)
    {
        setMse(e);

    }

    // Used for enlarging card?
    public void mouseMoved(MouseEvent e)
    {
        setMse(e);
    }

    // Used for selecting and pushing
    // either this or clicked, not sure yet
    // But click is probably wrong
    public void mousePressed(MouseEvent e)
    {
        setMse(e);
        clicked = true;
    }


    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    	clicked = false;
    }
}
