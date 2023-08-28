package Belt_Package;

import Belt_Package.First.Images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Balancer implements Placeable, Belt_Building{
	BeltGrid belt_grid;
	int grid_row;
	int grid_column;
	int orientation;
	Belt_In_Balancer left_belt;
	Belt_In_Balancer right_belt;
	BufferedImage image;
	int x;
	int y;
	int[][][] belt_position_grid = {
					{{0, 0},
					{1, 0},
					{0, 1},
					{0, 0}},
					{{0, 1},
					{0, 0},
					{0, 0},
					{1, 0}}};
	int[][] right_belt_pos = {
			{0, 1},
			{-1, 0},
			{0, -1},
			{1, 0}
	};
	Belt_In_Balancer[][] belts = new Belt_In_Balancer[2][2];
	
	
	public Balancer(BeltGrid belt_grid, int grid_row, int grid_column, int orientation){
		this.belt_grid = belt_grid;
		this.grid_row = grid_row;
		this.grid_column = grid_column;
		this.orientation = orientation;
		int[] cord_temp = Entity_Manager.grid_to_pixel(grid_row, grid_column);
		x = cord_temp[1];
		y = cord_temp[0];
		int[] space_created = new int[2];
		//going to the right
		if(orientation % 2 == 0){
			space_created[0] = 1;
			space_created[1] = 2;
		} else{
			space_created[0] = 2;
			space_created[1] = 1;
		}
		construct_image();
		make_belts();
	}
	
	public Belt[] get_belts(){
		Belt[] result = new Belt[4];
		int count = 0;
		for(int row = 0; row < 2; row++){
			for(int column = 0; column < 2; column++){
				//System.out.println("belt from row: " + row + ", column: " + column +", is: " + belts[row][column] +"going to index :" + count);
				result[count] = belts[row][column];
				count++;
			}
		}
		return result;
	}
	
	@Override
	public int get_row() {
		return grid_row;
	}
	
	@Override
	public int get_column() {
		return grid_column;
	}
	
	@Override
	public Placeable[] reconfigure_and_return_affected() {
		reset_belts_around();
		return new Placeable[0];
	}
	
	public void make_belts(){
		for(int row = 0; row < 2; row++){
			for(int column = 0; column < 2; column++){
				int belt_row = grid_row + belt_position_grid[column][orientation][0];
				int belt_col = grid_column + belt_position_grid[column][orientation][1];
				belts[row][column] = new Belt_In_Balancer(orientation, belt_row, belt_col, column == 0, row==0);
			}
			
		}
		reset_belts_around();
		//0,0 0,1
		//1,0 1,1
		
		//0,0  2,1
		//0,1  2,3
		//1,0  0,1
		//1,1  0,3
		
	}
	
	private void reset_belts_around(){
		for(int row = 0; row < 2; row++){
			for(int column = 0; column < 2; column++){
				Belt belt = belts[row][column];
				Belt[] belts_around = new Belt[4];
				if(row == 0){
					belts_around[2] = belts[1][column];
					Belt temp = belt_grid.get_belt(belt.grid_row - 1, belt.grid_column, 2);
					if(temp != null)
						belts_around[0] = temp;
				}
				else {
					belts_around[0] = belts[0][column];
					Belt temp = belt_grid.get_belt(belt.grid_row + 1, belt.grid_column, 0);
					if(temp != null)
						belts_around[2] = temp;
				}
				
				if(column == 0){
					belts_around[1] = belts[row][1];
					Belt temp = belt_grid.get_belt(belt.grid_row, belt.grid_column - 1, 1);
					if(temp != null)
						belts_around[3] = temp;
				}
				else{
					belts_around[3] = belts[row][0];
					Belt temp = belt_grid.get_belt(belt.grid_row, belt.grid_column+1, 3);
					if(temp != null)
						belts_around[1] = temp;
				}
				belt.set_belts_around(belts_around);
			}
		}
	}
	
	public Belt get_belt(int row, int column, int direction_coming_from){
		int column_diff = column - grid_column;
		direction_coming_from = (direction_coming_from + orientation)%4;
		if(direction_coming_from == 1 || direction_coming_from == 3)
			return null;
		if(column_diff != 0 && column_diff != 1){
			System.out.println("balancer: " + this);
			System.out.println("row: " + row + ", column: " + column + ", direction: " + direction_coming_from);
			throw new RuntimeException();
		}
		System.out.println("row: " + row + ", column: " + column + ", direction: " + direction_coming_from);
		return belts[direction_coming_from/2][column_diff];
	}
	
	public void construct_image(){
		image = new BufferedImage(128, 64, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D image_grf = (Graphics2D) image.getGraphics();
		
		BufferedImage straight_up = Images.beltUpImage;
		image_grf.drawImage(straight_up, 0, 0, null);
		image_grf.drawImage(straight_up, 64, 0, null);
		image_grf.setColor(Color.black);
		image_grf.fillRect(0 + 12, 0 + 24, 104, 16);
		for(int i = 0; i < orientation; i++){
			image = Images.rotateBy90(image);
		}
		//System.out.println(image.getWidth() +" "+image.getHeight());
	}
	
	public Placeable[] get_affected_around() {
		int[][] rel;
		int[][] result = new int[4][2];
		int[][] diff_even = new int[][]{
				{ 1, 0},
				{ 1, 1},
				{-1, 0},
				{-1, 1},
		};
		int[][] diff_odd = new int[][]{
				{ 0, 1},
				{-1, 1},
				{ 0,-1},
				{-1,-1},
		};
		if(orientation % 2 == 0)
			rel = diff_even;
		else
			rel = diff_odd;
		for(int i = 0; i < 4; i++){
			result[i][0] = rel[i][0] + grid_row;
			result[i][1] = rel[i][1] + grid_column;
		}
		ArrayList<Placeable> buildings_list = new ArrayList<>();
		for(int i = 0; i < result.length; i++){
			Placeable building = belt_grid.get(result[i][0], result[i][1]);
			if(building != null)
				buildings_list.add(building);
		}
		Placeable[] buildings = new Placeable[buildings_list.size()];
		System.out.println("buildings around balancer: ");
		for(int i = 0; i < buildings_list.size(); i++){
			buildings[i] = buildings_list.get(i);
			System.out.print(buildings[i]+", ");
		}
		System.out.println();
		return buildings;
	}
	
	public int[] space_taken(){
		if(orientation % 2 == 0)
			return new int[]{1, 2};
		return new int[]{2, 1};
	}
	
	@Override
	public BufferedImage get_image() {
		return image;
	}
	
	public static int[] get_extra_space_taken(int orientation){
		if(orientation % 2 == 0){
			return new int[]{0, 1};
		}
		return new int[]{1, 0};
	}
	
	public String toString(){
		return "<balancer  "+grid_row+", "+grid_column+">";
	}
}
