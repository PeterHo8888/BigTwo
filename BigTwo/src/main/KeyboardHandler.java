package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyboardHandler implements KeyListener
{

	
	ArrayList<Integer> rl_stack  = new ArrayList<Integer>();
	ArrayList<Integer> ud_stack  = new ArrayList<Integer>();
	
	public void keyPressed(KeyEvent e)
	{
		int tmp = e.getKeyCode();
		if((tmp==37||tmp==39) && rl_stack.indexOf(tmp) == -1)
			rl_stack.add(tmp);
		else if((tmp==38||tmp==40) && ud_stack.indexOf(tmp) == -1)
			ud_stack.add(tmp);
	}
	
	public void keyReleased(KeyEvent e)
	{
		int tmp = e.getKeyCode();
		if(tmp==37||tmp==39)
			rl_stack.remove(rl_stack.indexOf(tmp));
		else if(tmp==38||tmp==40)
			ud_stack.remove(ud_stack.indexOf(tmp));
	}
	
	public int updateX()
	{
		if (rl_stack.size() != 0) {
			switch (rl_stack.get(rl_stack.size() - 1)) {
			case 37:
				
				break;
			case 39:
				
				break;
			default:
				
				break;
			}
		}
		else {
			
		}
		return 0;
	}
	
	public int updateY()
	{
		if (ud_stack.size() != 0) {
			switch (ud_stack.get(ud_stack.size() - 1)) {
			case 38:
				
				break;
			case 40:
				
				break;
			default:
				
				break;
			}
		}
		else {
			
		}
		return 0;
	}
	

	
	public void keyTyped(KeyEvent e)
	{
	}
	
}
