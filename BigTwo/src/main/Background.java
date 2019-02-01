package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import utilities.ImageUtil;

public class Background
{
    private BufferedImage img;

    public Background()
    {
        try {
            img = ImageUtil.toBufferedImage(
                    new ImageIcon(getClass().getResource("/slide-bg.jpg")));
            img = ImageUtil.getScaledImage(img, Frame.width, Frame.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g)
    {
        g.drawImage(img, 0, 0, null);
    }
}
