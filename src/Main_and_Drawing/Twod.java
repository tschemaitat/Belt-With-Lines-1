package Main_and_Drawing;

import Main_and_Drawing.Layouts.LayoutParameters;
import Main_and_Drawing.Layouts.RectP;

import java.awt.*;

public abstract class Twod {
	protected int visibility = 1;
	protected int UIvisibility = 1;
	
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
	
	protected abstract boolean observe(MouseEvent_Edited event);
	
	public abstract void draw(Graphics grf);
	
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
	
	public abstract void onClicked(MouseEvent_Edited event);
	public abstract void onTouched(MouseEvent_Edited event);
	public abstract void onMouseDown(MouseEvent_Edited event);
	public abstract void onMouseUp(MouseEvent_Edited event);
}
