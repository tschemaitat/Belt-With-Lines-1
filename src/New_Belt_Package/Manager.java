package New_Belt_Package;

import Main_and_Drawing.*;
import Main_and_Drawing.Layouts.LayoutParameters;
import Main_and_Drawing.Layouts.RectP;
import New_Belt_Package.First.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Manager {
	
	//belt chunks
	//belt cycle
	//balancer
	//belt creater/deleter
	
	
	static int[][] diff = new int[][]{{-1, 0},{0, 1},{1, 0},{0, -1}};
	int belt_grid_top = 100;
	int belt_grid_left = 300;
	int belt_grid_width = 64;
	int[][] beltMap = {
			{-1, -1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1, 0, 3, 1, 1, 2,-1,-1,-1,-1},
			{-1, -1,-1, 0, 1, 1, 2,-1,-1,-1},
			{-1, -1, 1, 0, 0,-1, 2,-1,-1,-1},
			{-1, -1, 0, 1, 2, 3, 3,-1,-1,-1},
			{-1, -1, 0, 3, 3,-1,-1,-1,-1,-1},
			{-1, -1,-1,-1,-1,-1,-1,-1,-1,-1}
	};
	
	
	int cameraX = 0;
	int cameraY = 0;
	int tick = 0;
	int timesDraw = 0;
	Layout layout;
	
	List<Belt_List> belt_lists;
	List<Belt> belts = new ArrayList<>();
	public static Belt[][] beltGrid;
	public boolean touching_belt_layer = false;
	Screen screen;
	
	int add_belt_orientation = 0;
	public int graphical_iteration = 0;
	public static Manager main_dude;
	
	public static Manager getManger(){
		return main_dude;
	}
	
	public Manager(Layout layout, Screen screen){
		this.screen = screen;
		main_dude = this;
		this.layout = layout;
		Images.loadSprites();
		createBelts();
		create_UI();
		build_lists();
		add_items();
		
	}
	
	//region iterating
	public void iterate_belt_lists(){
		for(int i = 0; i < belt_lists.size(); i++){
			//System.out.println("calling iterating from manager");
			belt_lists.get(i).has_iterated = false;
		}
		for(int i = 0; i < belt_lists.size(); i++){
			//System.out.println("calling iterating from manager");
			belt_lists.get(i).iterate_items(belt_lists.get(i).self_index);
		}
	}
	//endregion
	
	//region setup
	private void add_items(){
		//System.out.println("adding items");
		add_item_to_list(belts.get(2), 1, 1);
		add_item_to_list(belts.get(2), 1, 1);
		add_item_to_list(belts.get(3), 0, 1);
		add_item_to_list(belts.get(3), 1, 1);
		add_item_to_list(belts.get(1), 0, 0);
		add_item_to_list(belts.get(1), 3, 0);
		add_item_to_list(belts.get(6), 0, 0);
		add_item_to_list(belts.get(6), 2, 0);
		add_item_to_list(belts.get(6), 3, 0);
		add_item_to_list(belts.get(6), 4, 0);
		
		add_item_to_list(belts.get(14), 0, 0);
		add_item_to_list(belts.get(14), 2, 0);
		add_item_to_list(belts.get(14), 3, 0);
		add_item_to_list(belts.get(14), 0, 1);
		add_item_to_list(belts.get(14), 2, 1);
		add_item_to_list(belts.get(14), 3, 1);
		
		add_item_to_list(belts.get(17), 0, 0);
		add_item_to_list(belts.get(17), 2, 0);
		//add_item_to_list(17, 3, 0);
		add_item_to_list(belts.get(12), 0, 0);
		add_item_to_list(belts.get(12), 1, 0);
		add_item_to_list(belts.get(17), 0, 1);
		add_item_to_list(belts.get(12), 2, 1);
		add_item_to_list(belts.get(12), 3, 1);
		add_item_to_list(belts.get(12), 0, 1);
		add_item_to_list(belts.get(12), 1, 1);
		add_item_to_list(belts.get(17), 4, 0);
		add_item_to_list(belts.get(17), 5, 0);
		
		
		
		
	}
	private void createBelts(){
		int width = 9;
		int height = 7;
		
		beltGrid = new Belt[height][width];
		int index = 0;
		for(int i = 1; i < height - 1; i++){
			for(int j = 1; j < width - 1; j++){
				
				if(beltMap[i][j] != -1){
					beltGrid[i][j] = make_belt_from_grid(i, j);
					//System.out.println(i+", "+j+" x: "+beltGrid[i][j].x+" y: "+beltGrid[i][j].y);
					belts.add(beltGrid[i][j]);
					//beltGrid[i][j].setLocation();
					index++;
				}
			}
		}
	}
	private void build_lists(){
		belt_lists = new ArrayList<>();
		//belt_lists.add(Belt_List_Factory.construct_belt_list(belts.get(2), 0));
		
		for(int i = 0; i < belts.size(); i++){
			if(belts.get(i).get_list(0) == null){
				//System.out.println("making list: (" + i +", 0)~~~~~~~~~~~");
				Belt_List belt_list = Belt_List_Factory.construct_belt_list(belts.get(i), 0, belt_lists);
				belt_lists.add(belt_list);
			}
			
		}
		for(int i = 0; i < belts.size(); i++){
			if(belts.get(i).get_list(1) == null){
				//System.out.println("making list: (" + i +", 1)~~~~~~~~~~~");
				Belt_List belt_list = Belt_List_Factory.construct_belt_list(belts.get(i), 1, belt_lists);
				belt_lists.add(belt_list);
			}
			
		}
		for(int i = 0; i < belt_lists.size(); i++)
			belt_lists.get(i).compile();
		for(int i = 0; i < belt_lists.size(); i++)
			belt_lists.get(i).second_compile();
		
	}
	private void create_UI(){
		LayoutParameters params_bottom_layer = new RectP(0, 0, layout.getWidth(), layout.getHeight());
		LayoutParameters params_turner = new RectP(10, 110, 80, 80);
		LayoutParameters params_list_debug = new RectP(10, 300, 300, 400);
		Twod bottom_layer = new Twod(params_bottom_layer, layout) {
			@Override
			public void draw(Graphics2D grf) {
				//grf.setColor(Color.black);
				//grf.drawRect(getX(), getY(), getWidth(), getHeight());
				getManger().draw((Graphics2D) grf, graphical_iteration);
			}
			
			@Override
			public void onMouseEvent(MouseEvent_Edited event) {
				if(event.type == MouseEvent_Edited.type_untouch){
					touching_belt_layer = false;
				}
				if(event.type == MouseEvent_Edited.type_touch){
					touching_belt_layer = true;
				}
				
				System.out.println(name+ " got mouse event " + event.type() + " (" + event.x() +", " + event.y()+")");
				if(event.type == MouseEvent_Edited.type_click) {
					System.out.println("adding or deleting a belt");
					int x = event.x();
					int y = event.y();
					int[] cord = cord_pixel_to_belt(x, y);
					System.out.println(new Point(cord[0], cord[1]));
					belt_deleteOrAdd_procedure(cord[0], cord[1], add_belt_orientation);
				}
			}
		};
		bottom_layer.name = "bottom_layer";
		
		Twod belt_turner = new Twod(params_turner, layout) {
			
			@Override
			public void draw(Graphics2D grf) {
				grf.setColor(Color.black);
				grf.drawRect(getX(), getY(), getWidth(), getHeight());
				grf.drawString("turner", getX(), getY());
			}
			
			@Override
			public void onMouseEvent(MouseEvent_Edited event) {
				System.out.println(name+ " got mouse event " + event.type() + " (" + event.x() +", " + event.y()+")");
				if(event.type == MouseEvent_Edited.type_touch)
					System.out.println("touched");
				
				
				if(event.type == MouseEvent_Edited.type_click) {
					add_belt_orientation = (add_belt_orientation+1) % 4;
					System.out.println(add_belt_orientation);
				}
			}
		};
		
		belt_turner.name = "turner";
		
		Twod list_debug = new Twod(params_list_debug, layout) {
			@Override
			public void draw(Graphics2D grf) {
				grf.setColor(Color.white);
				grf.fillRect(getX(), getY(), getWidth(), getHeight());
				grf.setColor(Color.black);
				grf.drawRect(getX(), getY(), getWidth(), getHeight());
				grf.setColor(Color.black);
				
				grf.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				grf.setRenderingHint(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_QUALITY);
				int line_total_height = 0;
				for(int i = 0; i < belt_lists.size(); i++){
					String[] debug_print = new String[2];
					
					
					Belt_List list = belt_lists.get(i);
					debug_print[0] = "";
					debug_print[0] += list.self_index + ": (mode: "+list.iteration_mode+ "), "+ list.belt_index_and_side();
					debug_print[1] = list.item_characters();
					for(int j = 0; j < debug_print.length; j++){
						BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
						Graphics2D image_grf = (Graphics2D) image.getGraphics();
						image_grf.setColor(Color.black);
						int height = drawString(image_grf, debug_print[j], getWidth(), 0);
						grf.drawImage(image, getX(), getY() + line_total_height, null);
						line_total_height += height + 4;
					}
					line_total_height += 6;
				}
				
				
				
				
				
			}
			
			@Override
			public void onMouseEvent(MouseEvent_Edited event) {
				System.out.println(name+ " got mouse event " + event.type() + " (" + event.x() +", " + event.y()+")");
			}
		};
		list_debug.name = "list_debug";
		layout.to_bottom(bottom_layer);
		
		
	}
	//endregion
	
	//region items
	private void add_item_to_list(Belt belt, int belt_position, int side){
		//System.out.println("adding one item: belt: " +belt.arrayIndex+", pos: "+belt_position+", side: "+side);
		BufferedImage image = Images.iron;
		Belt_List belt_list = belt.get_list_from_side(side);
		//System.out.println("belt: "+belt.arrayIndex+" is found in list: " + belt_list);
		//System.out.println("got list: " + belt_list);
		int list_position = belt_list.get_list_position_from_beltPositionSide(new LocationStruct(belt_position, side, belt));
		if(list_position == -1){
			//System.out.println("list position is wrong???");
		}
		boolean added_item = belt_list.add_item_by_position(list_position, new Item_In_List("iron", image));
		if(!added_item){
			//System.out.println("FAILED TO ADD ITEM");
		}
	}
	//endregion
	
	//region add/deleting belts and lists
	private Belt make_belt_from_grid(int i, int j){
		return Belt.makeBelt(beltMap[i][j], new int[]{
						beltMap[i - 1][j], beltMap[i][j + 1],
						beltMap[i + 1][j], beltMap[i][j - 1]},
				belt_grid_left + belt_grid_width * (j-1) , belt_grid_top+belt_grid_width * (i-1), i, j);
	}
	private Belt make_belt_from_map(int row, int column, int orientation){
		int[] oAround = new int[4];
		for(int i = 0; i < 4; i++){
			oAround[i] = -1;
			int temp_row = row + diff[i][0];
			int temp_column = column + diff[i][1];
			if(beltGrid[temp_row][temp_column] != null)
				oAround[i] = beltGrid[temp_row][temp_column].orientation;
		}
		
		
		return Belt.makeBelt(orientation, oAround,
				belt_grid_left + belt_grid_width * (column-1) ,
				belt_grid_top+belt_grid_width * (row-1), row, column);
	}
	private void remove_belt_from_manager(Belt belt, Belt new_belt){
		//if deleting a belt
		if(belt != null){
			beltGrid[belt.grid_row][belt.grid_column] = null;
			belts.remove(belt);
			System.out.println("removing belt: " + belt.arrayIndex + "from manager");
		}
		if(new_belt != null){
			beltGrid[new_belt.grid_row][new_belt.grid_column] = new_belt;
			belts.add(new_belt);
			System.out.println("adding belt: " + new_belt.arrayIndex + "to manager");
		}
	}
	private void check_around_for_changed_shape(List<Belt> belts_whom_list_delete, Belt center_belt){
		//System.out.println("check around for changed shape: " + center_belt.arrayIndex);
		//we are adding or deleting a belt
		//we need to check for changed shapes
		//first we add to list even if shape didn't change
		//if shape changed
		//add around around for lists to delete
		//replace belt
		for(int i = 0; i < 4; i++){
			int[] oppositeDirection_array = {-1*diff[i][0], -1*diff[i][1]};
			int oppositeDirection = Belt.get_direction(oppositeDirection_array[0], oppositeDirection_array[1]);
			//System.out.println("direction of deleted belt from adjacent belt per: " + oppositeDirection);
			Belt belt_around = beltGrid[center_belt.grid_row + diff[i][0]][center_belt.grid_column + diff[i][1]];
			if(belt_around == null)
				continue;
			belts_whom_list_delete.add(belt_around);
			if(belt_around.changed_shape()){
				//System.out.println("belt: "+belt_around.arrayIndex + " changed shape");
				Belt[] belt_around_around = belt_around.getBeltsAround();
				for(int j = 0; j < 4; j++){
					if(belt_around_around[j] == null)
						continue;
					//if shape changed add around around to delete list
					belts_whom_list_delete.add(belt_around_around[j]);
					//System.out.println("adding to line delete list: " + belt_around_around[j]);
				}
				
				//System.out.println("adding to line delete list: " + belt_around);
				Belt new_belt = remake_belt(belt_around);
				remove_belt_from_manager(belt_around, new_belt);
			}
			
		}
	}
	private Belt remake_belt(Belt belt_around){
		return Belt.makeBelt(belt_around.orientation, belt_around.getoAround(), belt_around.x, belt_around.y, belt_around.grid_row, belt_around.grid_column);
	}
	private void belt_deleteOrAdd_procedure(int grid_row, int grid_column, int direction_new_belt){
		//delete belt
		//delete lists in belt and adjacent belts
		//check for changed shape in all adjacent belts
		//if changed, delete lists in adjacent belts of changed belt
		Belt belt_delete = beltGrid[grid_row][grid_column];
		Belt new_belt;
		List<Belt> belts_whom_list_delete = new ArrayList<>();
		if(belt_delete != null){//delete old belt
			belts_whom_list_delete.add(belt_delete);
			remove_belt_from_manager(belt_delete, null);
			System.out.println("checking around deleted belt");
			check_around_for_changed_shape(belts_whom_list_delete, belt_delete);
		}
		else{//if empty add new_belt
			new_belt = make_belt_from_map(grid_row, grid_column, direction_new_belt);
			remove_belt_from_manager(null, new_belt);
			System.out.println("checking around added belt");
			check_around_for_changed_shape(belts_whom_list_delete, new_belt);
		}
		
		
		
		
		//System.out.println("deleting lists");
		List<List<ItemLocationStruct>> itemToBeReplaced = delete_lists_of_belts(belts_whom_list_delete);
		
		List<Belt_List> new_lines = new ArrayList<>();
		//System.out.println("rebuilding lists");
		for(int i = 0; i < belts.size(); i++){
			//System.out.println("checking belt: " + belts.get(i).arrayIndex + " index: " + i);
			if(belts.get(i).get_list(0) == null){
				Belt_List line = Belt_List_Factory.construct_belt_list(belts.get(i), 0, belt_lists);
				new_lines.add(line);
				belt_lists.add(line);
			}
			
			if(belts.get(i).get_list(1) == null){
				Belt_List line = Belt_List_Factory.construct_belt_list(belts.get(i), 1, belt_lists);
				new_lines.add(line);
				belt_lists.add(line);
			}
		}
		//System.out.println("compiling lists");
		for(int i = 0; i < new_lines.size(); i++){
			new_lines.get(i).compile();
		}
		for(int i = 0; i < belt_lists.size(); i++){
			belt_lists.get(i).second_compile();
		}
		//System.out.println("finished procedure");
		
		for(int i = 0; i < itemToBeReplaced.size(); i++){
			List<ItemLocationStruct> list_temp = itemToBeReplaced.get(i);
			for(int j = 0; j < list_temp.size(); j++) {
				//System.out.print(list_temp.get(i));
			}
		}
		
		for(int i = 0; i < itemToBeReplaced.size(); i++){
			List<ItemLocationStruct> list_temp = itemToBeReplaced.get(i);
			for(int j = 0; j < list_temp.size(); j++){
				System.out.println("adding back item: ");
				ItemLocationStruct item = list_temp.get(j);
				if(!belts.contains(item.belt))
					continue;
				add_item_to_list(item.belt, item.position, item.side);
			}
		}
		
	}
	private List<List<ItemLocationStruct>> delete_lists_of_belts(List<Belt> belts){
		List<List<ItemLocationStruct>> itemToBeReplaced = new ArrayList<>();
		List<Belt_List> lines_to_delete = new ArrayList<>();
		for(int i = 0; i < belts.size(); i++){
			//System.out.print("(i: "+i+", " + belts.get(i) + ") ");
		}
		//System.out.println();
		for(int i = 0; i < belts.size(); i++){
			Belt_List line = belts.get(i).get_list(0);
			if(!lines_to_delete.contains(line) && line != null)
				lines_to_delete.add(line);
			line = belts.get(i).get_list(1);
			if(!lines_to_delete.contains(line) && line != null)
				lines_to_delete.add(line);
		}
		System.out.println("deleting lines: ");
		for(int i = lines_to_delete.size() - 1; i >= 0; i--){
			System.out.print(lines_to_delete.get(i).self_index + ", ");
			itemToBeReplaced.add(remove_list_from_manager(lines_to_delete.get(i)));
			belt_lists.remove(lines_to_delete.get(i));
		}
		System.out.println();
		return itemToBeReplaced;
	}
	private List<ItemLocationStruct> remove_list_from_manager(Belt_List line){
		
		belt_lists.remove(line);
		return line.delete();
	}
	//endregion
	
	//region graphics
	public void draw_belt_ghost(Graphics2D grf){
		//System.out.println("drawing ghost");
		if(screen.mouse_point == null)
			return;
		int grid_cord[] = cord_pixel_to_belt(screen.mouse_point.x, screen.mouse_point.y);
		int grid_row = grid_cord[0];
		int grid_column = grid_cord[1];
		//System.out.println("grid: " + new Point(grid_cord[0], grid_cord[1]));
		if(grid_cord[0] < 0 || grid_cord[1] < 0 || grid_cord[0] > beltGrid.length || grid_cord[1] < beltGrid[0].length){
			return;
		}
		
		if(beltGrid[grid_row][grid_column] != null)
			return;
		int rotation = add_belt_orientation;
		BufferedImage belt = Images.beltUpImage;
		for(int i = 0; i < rotation; i++)
			belt = Images.rotateBy90(belt);
		belt = Images.setAlpha(belt, 128);
		int belt_x = belt_grid_left + (grid_column - 1) *belt_grid_width;
		int belt_y = belt_grid_top + (grid_row - 1)*belt_grid_width;
		System.out.println("ghost at: " + new Point(belt_x, belt_y));
		grf.drawImage(belt, belt_x,belt_y,null);
		
	}
	public void draw(Graphics2D grf, int graphical_iteration){
		
		draw_belts(grf);
		draw_belt_lines(grf);
		draw_items_in_list(grf, graphical_iteration);
		if(touching_belt_layer)
			draw_belt_ghost(grf);
	}
	private void draw_items_in_list(Graphics2D grf, int graphical_iteration){
		for(int i = 0; i < belt_lists.size(); i++){
			belt_lists.get(i).draw_items(grf, graphical_iteration);
		}
	}
	private void draw_belt_lines(Graphics2D grf){
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
	private void draw_belts(Graphics2D grf){
		
		for(int i = 0; i < belts.size(); i++){
			grf.drawImage(belts.get(i).image, belts.get(i).x - cameraX,belts.get(i).y - cameraY,null);
			
			grf.setColor(Color.white);
			grf.fillRect(belts.get(i).x - cameraX + 28, belts.get(i).y - cameraY + 20, 24, 24);
			grf.setColor(Color.black);
			grf.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			
			grf.drawString(String.valueOf(belts.get(i).arrayIndex), belts.get(i).x - cameraX + 32,  belts.get(i).y - cameraY + 32);
			if(belts.get(i).image == null)
				System.out.println("image is null");
		}
	}
	//endregion
	
	//region UI
	public void click(int x, int y){
		print_state();
		//System.out.println("pixel cords x: " +x+" y: "+y);
		int[] belt_cord = cord_pixel_to_belt(x, y);
		//System.out.println("pixel cords x: " +belt_cord[0]+" y: "+belt_cord[1]);
		int grid_x = belt_cord[1];
		int grid_y = belt_cord[0];
		
		belt_deleteOrAdd_procedure(grid_x, grid_y, add_belt_orientation);
		
		
		
		//print_state();
		
	}
	public int[] cord_pixel_to_belt(int x, int y){
		return new int[]{
				
				((y-belt_grid_top)/belt_grid_width) + 1,
				((x-belt_grid_left)/belt_grid_width) + 1
		};
	}
	//endregion
	
	public static int drawString(Graphics2D g, String text, int width, int height) {
		FontMetrics fm = g.getFontMetrics();
		int lineHeight = fm.getHeight();
		
		int x = 0;
		int y = fm.getAscent();
		
		String[] words = text.split(" ");
		
		for (String word : words) {
			int wordWidth = fm.stringWidth(word + " ");
			
			if (x + wordWidth >= width) {
				x = 0;
				y += lineHeight;
			}
			
			g.drawString(word, x, y);
			//System.out.println("drawing word: " + word + " at " + x + ", " + y);
			x += wordWidth;
		}
		return y;
	}
	
	public void print_state(){
		System.out.println("printing state");
		System.out.println("lines: "+belt_lists.size()+" ~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < belt_lists.size(); i++){
			System.out.println(""+belt_lists.get(i));
		}
		System.out.println("belts: "+belts.size()+" ~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < belts.size(); i++){
			System.out.println(""+belts.get(i));
			for(int j = 0; j < 4; j++){
				if(belts.get(i).beltsAround(j) != null)
					System.out.print(belts.get(i).beltsAround(j).arrayIndex + ", ");
				else
					System.out.print("N, ");
			}
			System.out.println();
		}
		System.out.println("\tstate done~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
}
