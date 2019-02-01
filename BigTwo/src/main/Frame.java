package main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Frame extends JFrame implements ActionListener
{
    // Constants
    public final static String title   = "Big Two";
    public final static String version = "0.1";

    // Define 4:3 area where height = screen size/1.25
    public final static int height = (int) (Toolkit.getDefaultToolkit()
            .getScreenSize().getHeight() / 1.25);
    public final static int width  = (int) (height * 4.0 / 3.0);

    public static Dimension size = new Dimension(width, height);

    public static Screen screen = null;

    public static JMenuBar   menuBar = new JMenuBar();
    private static JMenu     mnFile;
    private static JMenuItem miAbout;
    private static JMenuItem miAvatar;
    private static JMenuItem miExit;
    
    
    private static JMenu     mnDebug;
    private static JMenuItem miDeal;

    public static Frame frame;

    public Frame()
    {
        setTitle(title);
        setSize(size);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setJMenuBar(menuBar);

        mnFile = new JMenu("File");
        menuBar.add(mnFile);

        miAbout = new JMenuItem("About");
        miAbout.addActionListener(this);
        mnFile.add(miAbout);

        miAvatar = new JMenuItem("Avatar");
        miAvatar.addActionListener(this);
        mnFile.add(miAvatar);

        miExit = new JMenuItem("Exit");
        miExit.addActionListener(this);
        mnFile.add(miExit);
        

        mnDebug = new JMenu("Debug");
        menuBar.add(mnDebug);
        
        miDeal = new JMenuItem("Reset Room");
        miDeal.addActionListener(this);;
        mnDebug.add(miDeal);
        

        init();
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == miAbout) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Written by Peter Ho", "About",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
        if (e.getSource() == miAvatar) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JPG, PNG, & GIF Images", "jpg", "jpeg", "png", "gif");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(
                    new File(System.getProperty("user.dir")));
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                if (screen != null) {
                    Player.setPlayerAvatarFile(chooser.getSelectedFile());
                }
            }
        }
        if (e.getSource() == miExit) {
            int i = javax.swing.JOptionPane.showConfirmDialog(this, "Exit?",
                    "Big Two", JOptionPane.YES_NO_OPTION);
            if (i == 0)
                System.exit(0);
        }
        
        if (e.getSource() == miDeal) {
        	Screen.room = new Room();
        }
    }

    public void init()
    {
        setLayout(new GridLayout(1, 1, 0, 0));

        screen = new Screen(this);
        add(screen);

        setVisible(true);
    }

    public static Screen getScreen()
    {
        return screen;
    }

    public static void main(String[] args)
    {
        frame = new Frame();
    }
}
