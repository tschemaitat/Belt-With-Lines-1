package Main_and_Drawing;

import Main_and_Drawing.Layouts.LayoutParameters;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Layout extends Twod {
	List<Twod> list = new ArrayList<>();
	Object parent;
	public String name = null;
	
	public Layout(LayoutParameters parameters, Layout parent) {
		super(parameters, parent);
	}
	
	public void update() {
		super.update();
		if(list == null)
			return;
		for(int i = 0; i < list.size(); i++){
			list.get(i).update();
		}
	}
	
	//region dimension gets and sets
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
	//endregion
	
	
	
	
	
	protected void update_parent(){
		if(parent == null)
			return;
		if(parent instanceof Screen){
			Screen screen = (Screen)parent;
			screen.update_screen();
		}else{
			Layout parent_layout = (Layout) parent;
			parent_layout.update_parent();
		}
	}
	
	public abstract boolean observe(MouseEvent_Edited event);
	
	public void draw(Graphics2D grf){
		for(int i = 0; i < list.size(); i++){
			list.get(i).update();
			//list.get(i).drawBounds(grf);
		}
		draw_custom(grf);
	}
	
	abstract protected void draw_custom(Graphics2D grf);
	
	public final void add(Twod twod){
		if(has(twod)){
			//System.out.println("error added twice");
			return;
		}
		
		list.add(twod);
		twod.parent = this;
		add_custom(twod);
	}
	
	public boolean has(Twod twod){
		for(int i = 0; i < list.size(); i++){
			if(list.get(i) == twod)
				return true;
		}
		return false;
	}
	
	abstract protected void add_custom(Twod twod);
	
	public final void remove(Twod twod){
		list.remove(twod);
		twod.parent = null;
		remove_custom(twod);
		//System.out.println("removing twod from layout");
		//update_parent();
	}
	abstract protected void remove_custom(Twod twod);
	
	public final void empty(){
		for(int i = list.size() - 1; i >= 0; i--){
			remove(list.get(i));
		}
	}
	
	
	public void to_bottom(Twod obj){
		//System.out.println(list.indexOf(obj));
		
		list.remove(obj);
		//put it at top so it is observed last
		list.add(0, obj);
		//System.out.println(list.indexOf(obj));
	}
	
	public String toString(){
		String result = "";
		result += "name: " + name + " twods: ";
		int printed_index = 0;
		for(int i = 0; i < list.size(); i++){
			String temp = list.get(i).toString();
			if(temp != null)
				if(temp.equals("highlight") || temp.equals("square") || temp.contains("pw"))
					continue;
			if(printed_index%10 == 0 && printed_index > 0)
				result += "\n";
			result += "("+temp + "), ";
			printed_index++;
		}
		return result;
	}
}
