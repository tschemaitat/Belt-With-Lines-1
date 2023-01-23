package Main_and_Drawing.Layouts;

import Main_and_Drawing.Layout;
import Main_and_Drawing.Twod;

import java.awt.*;

public abstract class LayoutParameters {
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	int width;
	int height;
	public abstract LayoutParameters copy();
	
	public abstract int getX();
	public abstract int getY();
	
	
	
	public Layout parent;
	public Twod twod;
	
	public void setParent(Layout parent){
		this.parent = parent;
	}
	
	public void setTwod(Twod twod){
		this.twod = twod;
	}
}
