package Main_and_Drawing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
	public Screen parent;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println("mouseClicked" + getInfoFromMouse(e));
		MouseEvent_Edited event = new MouseEvent_Edited(e, MouseEvent_Edited.type_click);
		parent.add_mouse_event(event);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		//System.out.println("pressed" + getInfoFromMouse(e));
		MouseEvent_Edited event = new MouseEvent_Edited(e, MouseEvent_Edited.type_pressed);
		parent.add_mouse_event(event);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println("released" + getInfoFromMouse(e));
		MouseEvent_Edited event = new MouseEvent_Edited(e, MouseEvent_Edited.type_released);
		parent.add_mouse_event(event);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
	}
	
	private String getInfoFromMouse(MouseEvent e){
		String s = "";
		s+= " (x: "+e.getX() + ")";
		s+= " (y: "+e.getY() + ")";
		//s+= " x on screen: "+e.getXOnScreen();
		//s+= " y on screen: "+e.getYOnScreen();
		return s;
	}
}
