package Belt_Package;

import java.awt.*;
import java.util.*;

public class BeltGrid {
	public ArrayList<Belt> belts = new ArrayList<>();
	public ArrayList<Balancer> balancers = new ArrayList<>();
	HashMap<Point, Placeable> buildings;
	HashMap<Point, ArrayList<Placeable>> affected;
	
	public static void main(String []args){
		Belt[][] beltArray = new Belt[10][10];
		
		BeltGrid belts = new BeltGrid();
		
		//belts.add(2, 3, Belt.makeBelt(belts, 0, 2, 3));
		System.out.println(belts.has(2, 3));
		
		System.out.println(belts.has(0, 2));
	}
	
	public BeltGrid(){
		affected = new HashMap();
		buildings = new HashMap();
	}
	
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
		if(building instanceof Balancer)
			balancers.remove((Balancer)building);
		Belt[] temp = ((Belt_Building)building).get_belts();
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
		if(building instanceof Balancer)
			balancers.add((Balancer)building);
		Belt[] temp = ((Belt_Building)building).get_belts();
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
	
	//region affected grid
	public void add_affected(int row, int column, Placeable building){
		Point point = new Point(row, column);
		if(affected.get(point) == null)
			affected.put(point, new ArrayList<>());
		affected.get(point).add(building);
	}
	
	public void remove_affected(int row, int column, Placeable building){
		ArrayList<Placeable> affected_list = affected.get(new Point(row, column));
		if(!affected_list.contains(building))
			throw new RuntimeException();
		affected_list.remove(building);
	}
	
	public ArrayList<Placeable> get_affected(int row, int column){
		return affected.get(new Point(row, column));
	}
	//endregion
	//region get buildings
	public Placeable get(int row, int column){
		Placeable building = buildings.get(new Point(row, column));
		return building;
	}
	public Placeable_Belt get_belt_building(int row, int column){
		Placeable building = buildings.get(new Point(row, column));
		if(building instanceof Placeable_Belt)
			return (Placeable_Belt)building;
		return null;
	}
	
	public Belt get_belt(int row, int column, int direction_from){
		Placeable building = buildings.get(new Point(row, column));
		if(building instanceof Placeable_Belt)
			return ((Placeable_Belt) building).belt;
		if(building instanceof Balancer){
			if(direction_from == 0)
				return ((Balancer)building).get_belt(row, column, direction_from);
		}
		return null;
	}
	public void remove(int row, int column){
		buildings.remove(new Point(row, column));
	}
	private void add(int row, int column, Placeable belt){
		buildings.put(new Point(row, column), belt);
	}
	public boolean has(int row, int column){
		return buildings.containsKey(new Point(row, column));
	}
	//endregion
	
	
}
