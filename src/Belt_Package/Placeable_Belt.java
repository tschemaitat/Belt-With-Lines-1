package Belt_Package;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Placeable_Belt implements Placeable, Belt_Building{
	public Belt belt;
	public Placeable_Belt(BeltGrid grid, Belt belt){
		beltGrid = grid;
		this.belt = belt;
	}
	public Placeable_Belt(BeltGrid beltGrid, int orientation, int grid_x, int grid_y){
		belt = Belt.makeBelt(Belt.get_belts_from_grid(beltGrid, grid_x, grid_y, orientation), orientation, grid_x, grid_y);
		this.beltGrid = beltGrid;
		
	}
	public BeltGrid beltGrid;
	
	@Override
	public Placeable[] get_affected_around() {
		ArrayList<Placeable> buildings_list = new ArrayList<>();
		int[][] diff = Entity_Manager.diff;
		for(int i = 0; i < 4; i++){
			Placeable temp = beltGrid.get(get_row() + diff[i][0], get_column() + diff[i][1]);
			if(temp != null)
				buildings_list.add(temp);
		}
		Placeable[] buildings = new Placeable[buildings_list.size()];
		for(int i = 0; i < buildings_list.size(); i++){
			buildings[i] = buildings_list.get(i);
		}
		return buildings;
	}
	
	public Placeable[] reconfigure_and_return_affected(){
		belt.set_belts_around(beltGrid);
		if(belt.changed_shape()){
			Belt new_belt = remake_belt(belt);
			set(new_belt);
			return get_affected_around();
		}
		return null;
	}
	
	private Belt remake_belt(Belt belt_around){
		return Belt.makeBelt(beltGrid, belt_around.orientation, belt_around.grid_row, belt_around.grid_column);
	}
	
	public void set(Belt belt){
		beltGrid.remove_from_manager(this);
		this.belt = belt;
		beltGrid.add_to_manager(this);
	}
	
	@Override
	public int[] space_taken() {
		return new int[]{1, 1};
	}
	
	@Override
	public BufferedImage get_image() {
		return belt.image;
	}
	
	@Override
	public Belt[] get_belts() {
		return new Belt[]{belt};
	}
	
	@Override
	public int get_row() {
		return belt.grid_row;
	}
	
	@Override
	public int get_column() {
		return belt.grid_column;
	}
}
