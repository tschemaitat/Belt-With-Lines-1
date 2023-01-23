package Main_and_Drawing;

import java.awt.event.MouseEvent;

public class MouseEvent_Edited {
	public final static int type_click = 1000;
	private MouseEvent event;
	Image_Twod observer;
	int type;
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
	
	
}
