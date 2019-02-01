package utilities;

import main.Frame;
import main.Screen;

public class Coordinates
{
    private int X;
    private int Y;

    public Coordinates()
    {
        this(0, 0);
    }

    public Coordinates(int x, int y)
    {
        this.X = x;
        this.Y = y;
    }

    private static Coordinates convert(int x, int y)
    {
        int s_x = (int) (x * (Frame.width / (double)Screen.myWidth));
        int s_y = (int) (y * (Frame.height / (double)Screen.myHeight));

        return new Coordinates(s_x, s_y);
    }

    public int getX()
    {
        return this.X;
    }

    public int getY()
    {
        return this.Y;
    }

    public static Coordinates toScreen(int x, int y)
    {
        return convert(x, y);
    }
    
    public static Coordinates toInternal(int x, int y)
    {
    	int i_x = (int) ((double)x * (double)Screen.myWidth / (double)Frame.width);
    	int i_y = (int) ((double)y * (double)Screen.myHeight / (double)Frame.height);
    	return new Coordinates(i_x, i_y);
    }
}
