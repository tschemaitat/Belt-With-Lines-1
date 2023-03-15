package New_Belt_Package;

import New_Belt_Package.First.Images;

import java.awt.*;
import java.awt.image.BufferedImage;
import static New_Belt_Package.First.Enum.*;

public class Balancer {
	BeltGrid belt_grid;
	int grid_row;
	int grid_column;
	int orientation;
	Belt_In_Balancer left_belt;
	Belt_In_Balancer right_belt;
	BufferedImage image;
	int x;
	int y;
	
	public Balancer(BeltGrid belt_grid, int grid_row, int grid_column, int orientation){
		this.belt_grid = belt_grid;
		this.grid_row = grid_row;
		this.grid_column = grid_column;
		this.orientation = orientation;
		int[] cord_temp = Manager.grid_to_pixel(grid_row, grid_column);
		x = cord_temp[1];
		y = cord_temp[0];
		int[] space_created = new int[2];
		left_belt = new Belt_In_Balancer(belt_grid, orientation, grid_row, grid_column, true);
		//going to the right
		if(orientation % 2 == 0){
			space_created[0] = 1;
			space_created[1] = 2;
		} else{
			space_created[0] = 2;
			space_created[1] = 1;
		}
		int[] second_belt_grid = {grid_row + space_created[0] - 1, grid_column + space_created[1] - 1};
		cord_temp = Manager.grid_to_pixel(second_belt_grid[0], second_belt_grid[1]);
		right_belt = new Belt_In_Balancer(belt_grid, orientation, second_belt_grid[0], second_belt_grid[1], false);
		construct_image();
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
	
	public int[][] get_affected_around() {
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
			return diff_even;
		return diff_odd;
	}
	
	public int[] get_extra_space_taken(){
		if(orientation % 2 == 0)
			return new int[]{0, 1};
		return new int[]{1, 0};
	}
	
	public static int[] get_extra_space_taken(int orientation){
		if(orientation % 2 == 0){
			return new int[]{0, 1};
		}
		return new int[]{1, 0};
	}
}
