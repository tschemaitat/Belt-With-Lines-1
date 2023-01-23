package Main_and_Drawing.Layouts;

import Main_and_Drawing.Layout;

import java.awt.*;

public class RectP extends LayoutParameters{
	
	int x;
	int y;
	
	Layout parent;
	
	public RectP(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public RectP copy(){
		return new RectP(x, y, width, height);
	}
	
	@Override
	public void setParent(Layout parent) {
		this.parent = parent;
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(x, y, width, height);
	}
	
	public void setAll(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean inside(int x, int y){
		return getRectangle().contains(x, y);
	}
	
	
	//region Simple get and sets
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	//endregion
}
