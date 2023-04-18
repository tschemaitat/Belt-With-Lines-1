package New_Belt_Package;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BeltGrid {
	
	public static void main(String []args){
		Belt[][] beltArray = new Belt[10][10];
		
		BeltGrid belts = new BeltGrid();
		//belts.add(2, 3, Belt.makeBelt(belts, 0, 2, 3));
		System.out.println(belts.has(2, 3));
		
		System.out.println(belts.has(0, 2));
	}
	
	
	public List<Belt> belts = new ArrayList<>();
	
//	public void remove_belt_from_manager(Placeable_Belt belt){
//		//if deleting a belt
//		if(belt != null){
//			remove(belt.belt.grid_row, belt.belt.grid_column);
//			belts.remove(belt.belt);
//			System.out.println("removing belt: " + belt.belt.arrayIndex + "from manager");
//		}
//
//	}
	
	public void remove_from_manager(Placeable building){
		Belt[] temp = building.get_belts();
		int[] space = building.space_taken();
		for(int i = 0; i < space[0]; i++){
			for(int j = 0; j < space[1]; j++){
				remove(building.get_row() + i, building.get_column() + j);
			}
		}
		for(int i = 0; i < temp.length; i++){
			belts.remove(temp[i]);
		}
	}
	public void add_to_manager(Placeable building){
		
		Belt[] temp = building.get_belts();
		int[] space = building.space_taken();
		for(int i = 0; i < space[0]; i++){
			for(int j = 0; j < space[1]; j++){
				add(building.get_row() + i, building.get_column() + j, building);
			}
		}
		for(int i = 0; i < temp.length; i++){
			belts.add(temp[i]);
		}
	}
	
//	public void add_belt_to_manager(Placeable_Belt new_belt){
//		//if deleting a belt
//		if(new_belt != null){
//			add(new_belt.belt.grid_row, new_belt.belt.grid_column, new_belt);
//			belts.add(new_belt.belt);
//			System.out.println("adding belt: " + new_belt.belt.arrayIndex + "to manager");
//		}
//	}
	
	
	
	HashMap<Point, Placeable> buildings;
	public Placeable get(int row, int column){
		Placeable building = buildings.get(new Point(row, column));
		return building;
	}
	
	public Placeable_Belt get_belt(int row, int column){
		Placeable building = buildings.get(new Point(row, column));
		if(building instanceof Placeable_Belt)
			return (Placeable_Belt)building;
		return null;
	}
	
	public void remove(int row, int column){
		buildings.remove(new Point(row, column));
	}
	
	public BeltGrid(){
		buildings = new HashMap<Point, Placeable>();
	}
	
	private void add(int row, int column, Placeable belt){
		buildings.put(new Point(row, column), belt);
	}
	
	public boolean has(int row, int column){
		return buildings.containsKey(new Point(row, column));
	}
}
