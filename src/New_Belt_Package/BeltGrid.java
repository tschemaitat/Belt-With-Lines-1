package New_Belt_Package;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeltGrid {
	
	public static void main(String []args){
		Belt[][] beltArray = new Belt[10][10];
		
		BeltGrid belts = new BeltGrid();
		belts.add(2, 3, Belt.makeBelt(belts, 0, new int[]{-1,-1,-1,-1}, 2, 3));
		System.out.println(belts.has(2, 3));
		System.out.println(belts.has(0, 2));
	}
	
	
	HashMap<Point, Belt> belts;
	public Belt get(int row, int column){
		Belt belt = belts.get(new Point(row, column));
		return belt;
	}
	
	public void remove(int row, int column){
		belts.remove(new Point(row, column));
	}
	
	public BeltGrid(){
		belts = new HashMap<Point, Belt>();
	}
	
	public void add(int row, int column, Belt belt){
		belts.put(new Point(row, column), belt);
	}
	
	public boolean has(int row, int column){
		return belts.containsKey(new Point(row, column));
	}
}
