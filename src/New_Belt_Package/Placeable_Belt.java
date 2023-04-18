package New_Belt_Package;

import java.awt.image.BufferedImage;
import java.util.List;

public class Placeable_Belt implements Placeable{
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
	public int[][] get_affected_around() {
		return belt.get_affected_around();
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
