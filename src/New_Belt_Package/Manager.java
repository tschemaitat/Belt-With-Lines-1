package New_Belt_Package;

import New_Belt_Package.First.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Manager {
	int cameraX = 0;
	int cameraY = 0;
	int tick = 0;
	public Manager(){
		Images.loadSprites();
		createBelts();
		setup_items();
	}
	List<Item_In_Belt> items = new ArrayList<>();
	private void setup_items(){
		items.add(new Item_In_Belt(belts.get(2), 0, 1));
		items.add(new Item_In_Belt(belts.get(15), 0, 1));
	}
	
	public void iterate_items(){
		for(int i = 0; i < items.size(); i++){
			items.get(i).iterate();
		}
		tick++;
	}
	
	List<Belt> belts = new ArrayList<>();
	Belt[][] beltGrid;
	
	int timesDraw = 0;
	public void draw_Items_In_Belts_Old(Graphics grf){
		boolean inBetweenTick = false;
		if(tick%2 == 1)
			inBetweenTick = true;
		for(int i = 0; i < items.size(); i++)
			grf.drawImage(Images.iron, items.get(i).x, items.get(i).y, null);
		System.out.println("item x: " + items.get(0).x + ", y: " + items.get(0).y);
		//draw_locations_on_curveToRight(grf);
	}
	
	public void draw_belts(Graphics2D grf){
		for(int i = 0; i < belts.size(); i++){
			grf.drawImage(belts.get(i).image, belts.get(i).x - cameraX,belts.get(i).y - cameraY,null);
			
			grf.setColor(Color.white);
			grf.fillRect(belts.get(i).x - cameraX + 28, belts.get(i).y - cameraY + 20, 24, 24);
			grf.setColor(Color.black);
			grf.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			
			grf.drawString(String.valueOf(i), belts.get(i).x - cameraX + 32,  belts.get(i).y - cameraY + 32);
			if(belts.get(i).image == null)
				System.out.println("image is null");
		}
	}
	
	public void iterate_belt_lists(){
		for(int i = 0; i < belt_lists.size(); i++){
			belt_lists.get(i).iterate_items();
		}
	}
	
	List<Belt_List> belt_lists;
	public void build_lists(){
		belt_lists = new ArrayList<>();
		//belt_lists.add(Belt_List_Factory.construct_belt_list(belts.get(2), 0));
		
		for(int i = 0; i < belts.size(); i++){
			if(!belts.get(i).checked_side_for_list[0]){
				//System.out.println("making list: (" + i +", 0)~~~~~~~~~~~");
				Belt_List belt_list = Belt_List_Factory.construct_belt_list(belts.get(i), 0, belt_lists.size(), belt_lists);
				belt_lists.add(belt_list);
			}
			
		}
		for(int i = 0; i < belts.size(); i++){
			if(!belts.get(i).checked_side_for_list[1]){
				//System.out.println("making list: (" + i +", 1)~~~~~~~~~~~");
				Belt_List belt_list = Belt_List_Factory.construct_belt_list(belts.get(i), 1, belt_lists.size(), belt_lists);
				belt_lists.add(belt_list);
			}
			
		}
		for(int i = 0; i < belt_lists.size(); i++)
			belt_lists.get(i).compile();
		
	}
	
	public void draw_belt_lines(Graphics2D grf){
		//System.out.println("manager drawing lines");
		Color[] colors = {Color.BLUE, Color.CYAN, Color.RED, Color.GREEN};
		for(int i = 0; i < belt_lists.size(); i++){
			belt_lists.get(i).draw_line(grf, colors[i% colors.length]);
		}
		for(int i = 0; i < belt_lists.size(); i++){
			//System.out.println("belt_list size: " + belt_lists.get(i).size());
			String s = belt_lists.get(i).stack_belt_side_and_position(true);
			//System.out.println(s);
			
			//System.out.println(belt_lists.get(i).belt_index_and_side());
		}
		//System.out.println("manager done drawing lines");
	}
	
	public void add_items_to_lists(){
		System.out.println("adding items");
		add_item_to_list(2, 1, 1);
		add_item_to_list(2, 1, 1);
		add_item_to_list(3, 0, 1);
		add_item_to_list(3, 1, 1);
		add_item_to_list(1, 0, 0);
		add_item_to_list(1, 3, 0);
		add_item_to_list(6, 0, 0);
		add_item_to_list(6, 2, 0);
		add_item_to_list(6, 3, 0);
		add_item_to_list(6, 4, 0);
		
		add_item_to_list(14, 0, 0);
		add_item_to_list(14, 2, 0);
		add_item_to_list(14, 3, 0);
		add_item_to_list(14, 0, 1);
		add_item_to_list(14, 2, 1);
		add_item_to_list(14, 3, 1);
		
		add_item_to_list(17, 0, 0);
		add_item_to_list(17, 2, 0);
		//add_item_to_list(17, 3, 0);
		add_item_to_list(12, 0, 0);
		add_item_to_list(12, 1, 0);
		add_item_to_list(17, 0, 1);
		add_item_to_list(12, 2, 1);
		add_item_to_list(12, 3, 1);
		add_item_to_list(12, 0, 1);
		add_item_to_list(12, 1, 1);
		add_item_to_list(17, 4, 0);
		add_item_to_list(17, 5, 0);
		
		
		
		
	}
	
	public void add_item_to_list(int belt_index, int belt_position, int side){
		System.out.println("adding one item: belt: " +belt_index+", pos: "+belt_position+", side: "+side);
		BufferedImage image = Images.iron;
		Belt belt = belts.get(belt_index);
		Belt_List belt_list = belt_lists.get(belt.get_list_from_side(side));
		System.out.println("got list: " + belt_list);
		int list_position = belt_list.get_list_position_from_beltPositionSide(belt, belt_position, side);
		if(list_position == -1){
			System.out.println("list position is wrong???");
		}
		boolean added_item = belt_list.add_item_by_position(list_position, new Item_In_List("iron", image));
		if(!added_item){
			System.out.println("FAILED TO ADD ITEM");
		}
	}
	
	public void draw_items_in_list(Graphics2D grf, int graphical_iteration){
		for(int i = 0; i < belt_lists.size(); i++){
			belt_lists.get(i).draw_items(grf, graphical_iteration);
		}
	}
	
	private void draw_locations_on_curveToRight(Graphics grf){
		grf.setColor(Color.black);
		Belt belt = belts.get(0);
		int item_size = 16;
		int block_size = 2;
		int items_per_side = 4;
		int iterations_per_item = 4;
		int iterations = iterations_per_item*items_per_side;
		int x_offset = 100;
		int items_on_left = iterations*3/2;
		int items_on_right = iterations/2;
		int[][] cords_left = new int[items_on_left][];
		int[][] cords_right = new int[items_on_right][];
		for(int i = 0; i < items_on_left; i++){
			cords_left[i] = belt.get_item_location(i, 0);
		}
		for(int i = 0; i < items_on_right; i++){
			cords_right[i] = belt.get_item_location(i, 1);
		}
		for(int i = 0; i < items_on_left; i++){
			int multiplier = 1;
			if(i%iterations_per_item == 0)
				multiplier = 1;
			int x = cords_left[i][0] - x_offset + item_size - block_size * multiplier;
			int y = cords_left[i][1] - 00000000 + item_size - block_size * multiplier;
			grf.fillRect(x, y, block_size * multiplier, block_size * multiplier);
		}
		for(int i = 0; i < items_on_right; i++){
			int multiplier = 1;
			if(i%iterations_per_item == 0)
				multiplier = 1;
			int x = cords_right[i][0] - x_offset + item_size - block_size * multiplier;
			int y = cords_right[i][1] - 00000000 + item_size - block_size * multiplier;
			grf.fillRect(x, y, block_size * multiplier, block_size * multiplier);
		}
		grf.drawRect(belt.x - 100, belt.y, 64, 64);
	}
	
	public void createBelts(){
		int width = 9;
		int height = 7;
		int[][] beltMap = {
				{-1, -1,-1,-1,-1,-1,-1,-1,-1,-1},
				{-1, 0, 3, 1, 1, 2,-1,-1,-1,-1},
				{-1, -1,-1, 0, 1, 1, 2,-1,-1,-1},
				{-1, -1, 1, 0, 0,-1, 2,-1,-1,-1},
				{-1, -1, 0, 1, 2, 3, 3,-1,-1,-1},
				{-1, -1, 0, 3, 3,-1,-1,-1,-1,-1},
				{-1, -1,-1,-1,-1,-1,-1,-1,-1,-1}
		};
		beltGrid = new Belt[height][width];
		int index = 0;
		for(int i = 1; i < height - 1; i++){
			for(int j = 1; j < width - 1; j++){
				if(beltMap[i][j] != -1){
					beltGrid[i][j] = Belt.makeBelt(beltMap[i][j], new int[]{beltMap[i - 1][j], beltMap[i][j + 1], beltMap[i + 1][j], beltMap[i][j - 1]}, 64 * j + 200, 64 * i + 200);
					belts.add(beltGrid[i][j]);
					beltGrid[i][j].arrayIndex = index;
					//beltGrid[i][j].setLocation();
					index++;
				}
			}
		}
		
		for(int i = 1; i < height - 1; i++){
			for(int j = 1; j < width - 1; j++){
				if(beltGrid[i][j] != null){
					if(beltGrid[i+1][j] != null){
						beltGrid[i][j].beltsAround[2] = beltGrid[i+1][j];
					}
					if(beltGrid[i][j+1] != null){
						beltGrid[i][j].beltsAround[1] = beltGrid[i][j+1];
					}
					if(beltGrid[i-1][j] != null){
						beltGrid[i][j].beltsAround[0] = beltGrid[i-1][j];
					}
					if(beltGrid[i][j-1] != null){
						beltGrid[i][j].beltsAround[3] = beltGrid[i][j-1];
					}
				}
			}
		}
	}
}
