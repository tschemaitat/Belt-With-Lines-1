package Main_and_Drawing;

import java.awt.event.MouseEvent;

public class MouseEvent_Edited {
	public final static int type_click = 1000;
	public final static int type_pressed = 1001;
	public final static int type_released = 1002;
	private MouseEvent event;
	Twod observer;
	public int type;
	public MouseEvent_Edited(MouseEvent e, int type){
		event = e;
		this.type = type;
	}
	
	public int x(){
		return event.getX();
	}
	
	public int y(){
		return event.getY();
	}
	
	public int getXOnScreen(){
		return event.getXOnScreen();
	}
	public int getYOnScreen(){
		return event.getYOnScreen();
	}
	
	
}
