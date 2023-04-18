package Main_and_Drawing.Layouts;

import java.awt.event.KeyEvent;

public class KeyEvent_Edited {
	public KeyEvent event;
	public boolean pressed;
	public KeyEvent_Edited(KeyEvent event, boolean pressed){
		this.event = event;
		this.pressed = pressed;
	}
	
	public char character(){
		return event.getKeyChar();
	}
}
