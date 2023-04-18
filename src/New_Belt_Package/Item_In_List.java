package New_Belt_Package;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Item_In_List {
	private BufferedImage image;
	String name;
	public boolean empty;
	
	public Item_In_List(String name, BufferedImage image){
		
		this.name = name;
		this.image = image;
		empty = false;
	}
	
	public static Item_In_List new_empty(){
		Item_In_List item = new Item_In_List("Empty", null);
		item.empty = true;
		return item;
	}
	
	public void draw(Graphics2D grf, int x, int y){
		grf.drawImage(image, x - Manager.cameraX, y - Manager.cameraY, null);
	}
}
