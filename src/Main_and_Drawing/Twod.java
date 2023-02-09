package Main_and_Drawing;

import Main_and_Drawing.Layouts.LayoutParameters;
import Main_and_Drawing.Layouts.RectP;

import java.awt.*;

public abstract class Twod {
	protected int visibility = 1;
	protected int UIvisibility = 1;
	public String name = "";
	
	public static final int VISIBLE = 1;
	public static final int INVISIBLE = 0;
	
	//region dimension gets and sets
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	
	public void setX(int x) {
		RectP rect = (RectP)layoutParameters;
		rect.setX(x);
		update();
	}
	public void setY(int y) {
		RectP rect = (RectP)layoutParameters;
		rect.setY(y);
		update();
	}
	public void setHeight(int height) {
		layoutParameters.setHeight(height);
		update();
	}
	public void setWidth(int width) {
		layoutParameters.setWidth(width);
		update();
	}
	//endregion
	
	int x;
	int y;
	int height;
	int width;
	
	boolean mouse_touching = false;
	
	
	
	LayoutParameters layoutParameters;
	public Polygon bounds;
	
	public Layout parent;
	
	
	public Twod(LayoutParameters parameters, Layout parent){
		if(parent != null){
			this.parent = parent;
			parent.add(this);
		}
		layoutParameters = parameters;
		update();
	}
	
	
	
	
	//public List<Twod> getChildren();
	
	public Twod getParent() {
		return parent;
	}
	
	public void setVisibility(int num){
		visibility = num;
	}
	public void setVisible(){
		visibility = VISIBLE;
	}
	public void setInvisible(){
		visibility = INVISIBLE;
	}
	
	public void setUIVisibility(int num) {
		UIvisibility = num;
	}
	public void setUIVisible(){
		UIvisibility = VISIBLE;
	}
	public void setUIInvisible(){
		UIvisibility = INVISIBLE;
	}
	
	
	
	public boolean inBounds(int x, int y){
		return bounds.contains(x, y);
	}
	
	public boolean observe(MouseEvent_Edited event){
		
		//System.out.println("observing for twod: " + name);
		//System.out.println("event is: "+event.x()+","+event.y()+" twod is at: " + getX() + ", " + getY() + " width/height: " + width+", " + height);
		if(UIvisibility == 0)
			return false;
		int x = event.x();
		int y = event.y();
		if(inBounds(event.x(), event.y())){
			//System.out.println("it is in bounds");
			//System.out.println("got clicked");
			event.observer = this;
			return true;
		}
		return false;
	}
	
	public abstract void draw(Graphics2D grf);
	
	public void update(){
		x = layoutParameters.getX();
		y = layoutParameters.getY();
		width = layoutParameters.getWidth();
		height = layoutParameters.getHeight();
		setBounds();
	}
	
	private void setBounds(){
		bounds = new Polygon();
		bounds.addPoint(x, y);
		bounds.addPoint(x+width, y);
		bounds.addPoint(x+width, y+height);
		bounds.addPoint(x, y+height);
	}
	
	public void drawBounds(Graphics graphics){
		Rectangle rect = bounds.getBounds();
		graphics.drawRect(x, y, width, height);
	}
	
	public LayoutParameters getLayoutParameters() {
		return layoutParameters;
	}
	
	public void setLayoutParameters(LayoutParameters layoutParameters) {
		this.layoutParameters = layoutParameters;
	}
	
	public abstract void onMouseEvent(MouseEvent_Edited event);
	
}
