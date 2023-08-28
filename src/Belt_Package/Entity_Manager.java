package Belt_Package;

import Main_and_Drawing.*;
import Main_and_Drawing.Layouts.LayoutParameters;
import Main_and_Drawing.Layouts.RectP;
import Belt_Package.First.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Entity_Manager {
	
	//belt chunks
	//belt cycle
	//balancer
	//belt creater/deleter
	
	
	static int[][] diff = new int[][]{{-1, 0},{0, 1},{1, 0},{0, -1}};
	static int belt_grid_top = 100;
	static int belt_grid_left = 300;
	static int belt_grid_width = 64;
	int[][] beltMap = {
			{-1, -1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1, 0, 3, 1, 1, 2,-1,-1,-1,-1},
			{-1, -1,-1, 0, 1, 1, 2,-1,-1,-1},
			{-1, -1, 1, 0, 0,-1, 2,-1,-1,-1},
			{-1, -1, 0, 1, 2, 3, 3,-1,-1,-1},
			{-1, -1, 0, 3, 3,-1,-1,-1,-1,-1},
			{-1, -1,-1,-1,-1,-1,-1,-1,-1,-1}
	};
	
	
	public static int cameraX = 0;
	public static int cameraY = 0;
	
	public static int debug_scroll = 0;
	int tick = 0;
	int timesDraw = 0;
	Layout layout;
	
	boolean key_down = false;
	char key_that_is_down = 'z';
	
	Belt_Iterator_Manager belt_iterator;
	
	public static BeltGrid beltGrid;
	public boolean touching_belt_layer = false;
	Screen screen;
	
	int add_belt_orientation = 0;
	int add_belt_type = add_belt_type_regular;
	static final int add_belt_type_regular = 900;
	static final int add_belt_type_balancer = 901;
	
	public int graphical_iteration = 0;
	public static Entity_Manager main_dude;
	
	public static Entity_Manager getManager(){
		return main_dude;
	}
	
	public Entity_Manager(Layout layout, Screen screen){
		this.screen = screen;
		main_dude = this;
		this.layout = layout;
		Images.loadSprites();
		beltGrid = new BeltGrid();
		createBelts();
		create_UI();
		build_lists();
		add_items();
		
	}
	
	//region iterating
	public void iterate_belt_lists(){
		belt_iterator.iterate();
	}
	public void do_graphical_tick(){
		if(key_down){
			switch(key_that_is_down){
				case 'w':{
					//System.out.println("key pressed is w");
					cameraY = cameraY - 10;
					break;
				}
				case 's':{
					//System.out.println("key pressed is s");
					cameraY += 10;
					break;
				}
				case 'a':{
					cameraX -= 10;
					break;
				}
				case 'd':{
					//System.out.println("d is pressed");
					cameraX += 10;
					break;
				}
			}
		}
		//System.out.println("key down?" + key_down + ", key: " +key_that_is_down);
		//System.out.println("cameraX: " + cameraX + ", cameraY" + cameraY);
	}
	//endregion
	
	//region setup
	private void add_items(){
		//System.out.println("adding items");
		add_item_to_list(beltGrid.belts.get(2), 1, 1);
		add_item_to_list(beltGrid.belts.get(2), 1, 1);
		add_item_to_list(beltGrid.belts.get(3), 0, 1);
		add_item_to_list(beltGrid.belts.get(3), 1, 1);
		add_item_to_list(beltGrid.belts.get(1), 0, 0);
		add_item_to_list(beltGrid.belts.get(1), 3, 0);
		add_item_to_list(beltGrid.belts.get(6), 0, 0);
		add_item_to_list(beltGrid.belts.get(6), 2, 0);
		add_item_to_list(beltGrid.belts.get(6), 3, 0);
		add_item_to_list(beltGrid.belts.get(6), 4, 0);
		
		add_item_to_list(beltGrid.belts.get(14), 0, 0);
		add_item_to_list(beltGrid.belts.get(14), 2, 0);
		add_item_to_list(beltGrid.belts.get(14), 3, 0);
		add_item_to_list(beltGrid.belts.get(14), 0, 1);
		add_item_to_list(beltGrid.belts.get(14), 2, 1);
		add_item_to_list(beltGrid.belts.get(14), 3, 1);
		
		add_item_to_list(beltGrid.belts.get(17), 0, 0);
		add_item_to_list(beltGrid.belts.get(17), 2, 0);
		//add_item_to_list(17, 3, 0);		add_item_to_list(belts.get(12).belt, 0, 0);
		add_item_to_list(beltGrid.belts.get(12), 1, 0);
		add_item_to_list(beltGrid.belts.get(17), 0, 1);
		add_item_to_list(beltGrid.belts.get(12), 2, 1);
		add_item_to_list(beltGrid.belts.get(12), 3, 1);
		add_item_to_list(beltGrid.belts.get(12), 0, 1);
		add_item_to_list(beltGrid.belts.get(12), 1, 1);
		add_item_to_list(beltGrid.belts.get(17), 4, 0);
		add_item_to_list(beltGrid.belts.get(17), 5, 0);
		
		
		
		
	}
	private void createBelts(){
		int width = 9;
		int height = 7;
		
		
		int index = 0;
		for(int i = 1; i < height - 1; i++){
			for(int j = 1; j < width - 1; j++){
				
				if(beltMap[i][j] != -1){
					int[] oAround = new int[4];
					for(int around = 0; around < 4; around++){
						oAround[around] = beltMap[i + Entity_Manager.diff[around][0]][j + Entity_Manager.diff[around][1]];
					}
					Placeable_Belt belt = new Placeable_Belt(beltGrid, Belt.makeBelt(beltMap[i][j], oAround, i, j));
					beltGrid.add_to_manager(belt);
					
					//System.out.println(i+", "+j+" x: "+beltGrid[i][j].x+" y: "+beltGrid[i][j].y);
					
					//beltGrid[i][j].setLocation();
					index++;
				}
			}
		}
		for(int i = 1; i < height - 1; i++){
			for(int j = 1; j < width - 1; j++){
				
				if(beltMap[i][j] != -1){
					beltGrid.get_belt_building(i, j).belt.set_belts_around(beltGrid);
				}
			}
		}
	}
	private void build_lists(){
		//Main_Class.log.log_line("building lists");
		//Main_Class.log.tab("building lists");
		belt_iterator = new Belt_Line_Manager(beltGrid);
	}
	private void create_UI(){
		LayoutParameters params_bottom_layer = new RectP(0, 0, layout.getWidth(), layout.getHeight());
		LayoutParameters params_turner = new RectP(10, 110, 80, 80);
		
		Twod bottom_layer = new Twod(params_bottom_layer, layout) {
			@Override
			public void draw(Graphics2D grf) {
				//grf.setColor(Color.black);
				//grf.drawRect(getX(), getY(), getWidth(), getHeight());
				getManager().draw((Graphics2D) grf, graphical_iteration);
			}
			
			@Override
			public void onMouseEvent(MouseEvent_Edited event) {
				if(event.type == MouseEvent_Edited.type_untouch){
					touching_belt_layer = false;
				}
				if(event.type == MouseEvent_Edited.type_touch){
					touching_belt_layer = true;
				}
				
				//System.out.println(name+ " got mouse event " + event.type() + " (" + event.x() +", " + event.y()+")");
				if(event.type == MouseEvent_Edited.type_click) {
					System.out.println("adding or deleting a belt");
					int x = event.x() + cameraX;
					int y = event.y() + cameraY;
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
				//System.out.println(name+ " got mouse event " + event.type() + " (" + event.x() +", " + event.y()+")");
				//if(event.type == MouseEvent_Edited.type_touch)
					//System.out.println("touched");
				
				
				if(event.type == MouseEvent_Edited.type_click) {
					add_belt_orientation = (add_belt_orientation+1) % 4;
					System.out.println(add_belt_orientation);
				}
			}
		};
		
		belt_turner.name = "turner";
		
		LayoutParameters params_list_debug = new RectP(10, 250, 500, 450);
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
				
				BufferedImage image_debug = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
				Graphics2D image_grf = image_debug.createGraphics();
				image_grf.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				image_grf.setRenderingHint(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_QUALITY);
				
				belt_iterator.draw_debug(0, debug_scroll * -1, getWidth(), getHeight(), image_grf);
				grf.drawImage(image_debug, getX(), getY(), null);
				
				
				
				
				
			}
			
			@Override
			public void onMouseEvent(MouseEvent_Edited event) {
				if(event.type == MouseEvent_Edited.type_click)
					debug_scroll += 10;
				//System.out.println(name+ " got mouse event " + event.type() + " (" + event.x() +", " + event.y()+")");
			}
		};
		list_debug.name = "list_debug";
		layout.to_bottom(bottom_layer);
		
		int selector_top = 10;
		int selector_left = 10;
		
		
		RectP select_balancer_params = new RectP(selector_left, selector_top, 60, 20);
		Twod select_balancer = new Twod(select_balancer_params, layout) {
			@Override
			public void draw(Graphics2D grf) {
				boolean on = add_belt_type == add_belt_type_balancer;
				
				draw_button(on, grf, this, name);
			}
			
			@Override
			public void onMouseEvent(MouseEvent_Edited event) {
				if(event.type == MouseEvent_Edited.type_click){
					add_belt_type = add_belt_type_balancer;
				}
			}
		};
		
		RectP select_belt_params = new RectP(selector_left, selector_top + 30, 60, 20);
		Twod select_belt = new Twod(select_belt_params, layout) {
			@Override
			public void draw(Graphics2D grf) {
				grf.setColor(Color.black);
				if(add_belt_type == add_belt_type_regular)
					drawDropShadow(grf, getX(), getY(), getWidth(), getHeight(), 2, 70);
				
				grf.setColor(Color.white);
				grf.fillRect(getX(), getY(), getWidth(), getHeight());
				grf.setColor(Color.black);
				grf.drawRect(getX(), getY(), getWidth(), getHeight());
				
				//BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
				//Graphics2D image_grf = (Graphics2D) image.getGraphics();
				//image_grf.setColor(Color.black);
				drawString(grf, "belt", getX()+2, getY(), getWidth(), getHeight(), Color.black, 15);
			}
			
			@Override
			public void onMouseEvent(MouseEvent_Edited event) {
				if(event.type == MouseEvent_Edited.type_click){
					add_belt_type = add_belt_type_regular;
				}
			}
		};
	}
	private void draw_button(boolean on, Graphics2D grf, Twod twod, String text){
		if(on)
			drawDropShadow(grf, twod.getX(), twod.getY(), twod.getWidth(), twod.getHeight(), 2, 70);
		grf.setColor(Color.white);
		grf.fillRect(twod.getX(), twod.getY(), twod.getWidth(), twod.getHeight());
		grf.setColor(Color.black);
		grf.drawRect(twod.getX(), twod.getY(), twod.getWidth(), twod.getHeight());
		drawString(grf, "balancer", twod.getX(), twod.getY(), twod.getWidth(), twod.getHeight(), Color.black, 15);
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
	private void check_around_for_changed_shape(ArrayList<Belt> belts_whom_list_delete, Placeable building){
		//System.out.println("check around for changed shape: " + center_belt.arrayIndex);
		//we are adding or deleting a belt
		//we need to check for changed shapes
		//first we add to list even if shape didn't change
		//if shape changed
		//add around around for lists to delete
		//replace belt
		
		Placeable[] affected = building.get_affected_around();
		
		for(int i = 0; i < affected.length; i++){
			
			log_belts(belts_whom_list_delete, affected[i]);
			Placeable[] affected_affected = affected[i].reconfigure_and_return_affected();
			if(affected_affected == null)
				continue;
			//System.out.println("checking affected affected of: "+((Placeable_Belt)affected[i]).belt);
			for(int j = 0; j < affected_affected.length; j++){
				log_belts(belts_whom_list_delete, affected[i]);
				reconfigure_loop(belts_whom_list_delete, affected_affected[j]);
			}
		}
		System.out.println("printing balancer contents after add/delete");
		ArrayList<Balancer> balancers = beltGrid.balancers;
		for(int i = 0; i < balancers.size(); i++){
			System.out.println(balancers.get(i));
			Belt[] belts = balancers.get(i).get_belts();
			for(int j = 0; j < belts.length; j++){
				System.out.print(belts[j] + ":    ");
				Belt[] around = belts[j].getBeltsAround();
				for(int around_belt = 0; around_belt < 4; around_belt++){
					System.out.print(around[around_belt] + ", ");
				}
				System.out.println();
			}
		}
//		for(int i = 0; i < around.length; i++){
//			System.out.println("direction of deleted belt from adjacent belt per: " + around[i][0] + ", " + around[i][1]);
//			Placeable_Belt belt_around = beltGrid.get_belt(around[i][0], around[i][1]);
//
//			if(belt_around == null)
//				continue;
//			belt_around.belt.set_belts_around(beltGrid);
//			belts_whom_list_delete.add(belt_around.belt);
//			if(belt_around.belt.changed_shape()){
//				System.out.println("belt: "+belt_around.belt.arrayIndex + " changed shape");
//				Belt[] belt_around_around = belt_around.belt.getBeltsAround();
//				System.out.println("adding to line delete list: " + belt_around);
//				Belt new_belt = remake_belt(belt_around.belt);
//				belt_around.set(new_belt);
//				for(int j = 0; j < 4; j++){
//					if(belt_around_around[j] == null)
//						continue;
//					//if shape changed add around around to delete list
//					belts_whom_list_delete.add(belt_around_around[j]);
//					belt_around_around[j].set_belts_around(beltGrid);
//					System.out.println("adding to line delete list: " + belt_around_around[j]);
//				}
//			}
//
//		}
	}
	
	private void reconfigure_loop(ArrayList<Belt> belts_whom_list_delete, Placeable building){
		Placeable[] affected_affected = building.reconfigure_and_return_affected();
		if(affected_affected == null)
			return;
		//System.out.println("checking affected affected of: "+((Placeable_Belt)affected[i]).belt);
		for(int j = 0; j < affected_affected.length; j++){
			log_belts(belts_whom_list_delete, affected_affected[j]);
			reconfigure_loop(belts_whom_list_delete, affected_affected[j]);
		}
	}
	
	private void log_belts(ArrayList<Belt> belts, Placeable building){
		if(building instanceof Belt_Building){
			Belt[] to_log = ((Belt_Building) building).get_belts();
			for(int i = 0; i < to_log.length; i++){
				belts.add(to_log[i]);
			}
		}
	}
	
	public Placeable placeable_from_id(int id, int grid_row, int grid_column, int orientation){
		Placeable building;
		switch(id){
			case add_belt_type_regular -> building = new Placeable_Belt(beltGrid, orientation, grid_row, grid_column);
			case add_belt_type_balancer -> building = new Balancer(beltGrid, grid_row, grid_column, orientation);
			default -> throw new RuntimeException();
		}
		return building;
	}
	private void belt_deleteOrAdd_procedure(int grid_row, int grid_column, int direction_new_belt){
		System.out.println("start delete/add proc");
		//delete belt
		//delete lists in belt and adjacent belts
		//check for changed shape in all adjacent belts
		//if changed, delete lists in adjacent belts of changed belt
		Placeable belt_delete = beltGrid.get_belt_building(grid_row, grid_column);
		Placeable new_belt;
		ArrayList<Belt> belts_whom_list_delete = new ArrayList<>();
		int[][] around;
		if(belt_delete != null){//delete old belt
			Belt[] temp = ((Belt_Building)belt_delete).get_belts();
			for(int i = 0; i < temp.length; i++)
				belts_whom_list_delete.add(temp[i]);
			beltGrid.remove_from_manager(belt_delete);
			System.out.println("checking around deleted belt");
			//around = belt_delete.get_affected_around();
			check_around_for_changed_shape(belts_whom_list_delete, belt_delete);
		}
		else{//if empty add new_belt
			new_belt = placeable_from_id(add_belt_type, grid_row, grid_column, direction_new_belt);
			beltGrid.add_to_manager(new_belt);
			//System.out.println("index 21: "+belts.get(21).belts_around);
			System.out.println("checking around added belt");
			//around = new_belt.get_affected_around();
			check_around_for_changed_shape(belts_whom_list_delete, new_belt);
		}
		
		System.out.println("deleting lists: ");
		for(int i = 0; i < belts_whom_list_delete.size(); i++)
			System.out.print(belts_whom_list_delete.get(i) + ", ");
		System.out.println();
		List<List<ItemLocationStruct>> itemToBeReplaced = belt_iterator.delete_belt_iterators(belts_whom_list_delete);
		//System.out.println("index 21: "+belts.get(21).belts_around);
		
		belt_iterator.build_from_belts(beltGrid.belts, beltGrid);
		
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
				//System.out.println("adding back item: ");
				ItemLocationStruct item = list_temp.get(j);
				if(!beltGrid.belts.contains(item.belt))
					continue;
				add_item_to_list(item.belt, item.position, item.side);
			}
		}
		
//		int[][]  cords = {
//				{1, 1},
//				{1, 2},
//				{2, 1},
//				{2, 2}
//		};
//		for(int i = 0; i < cords.length; i++){
//			Placeable building = beltGrid.get(cords[i][0], cords[i][1]);
//			if(building == null)
//				continue;
//			System.out.println("building at: " + building.get_row() + ", " + building.get_column());
//			Belt[] belts = ((Belt_Building)building).get_belts();
//			for(int j = 0; j < belts.length; j++){
//				System.out.print("belt: " +belts[j]+"    ");
//				Belt[] around_temp = belts[j].belts_around;
//				for(int around_index = 0; around_index < around_temp.length; around_index++){
//					if(around_temp[around_index] == null){
//						System.out.print("<null>, ");
//						continue;
//					}
//
//					System.out.print("<"+around_temp[around_index]+">, ");
//				}
//				System.out.println();
//			}
//		}
		System.out.println();
		belt_iterator.print_iterators();
		System.out.println("end delete/add proc");
	}
	
	//endregion
	
	//region balancers
	private void add_balancer(int grid_row, int grid_column, int orientation){
	
	}
	//endregion
	
	//region graphics
	public void draw_belt_ghost(Graphics2D grf){
		Belt.making_ghost = true;
		int[] space_taken;
		if(add_belt_type == add_belt_type_balancer){
			space_taken = Balancer.get_extra_space_taken(add_belt_orientation);
		}
		else
			space_taken = new int[]{0,0};
		//System.out.println("space taken: " + new Point(space_taken[0], space_taken[1]));
		//System.out.println("drawing ghost");
		if(screen.mouse_point == null)
			return;
		int grid_cord[] = cord_pixel_to_belt(screen.mouse_point.x+cameraX, screen.mouse_point.y+cameraY);
		int grid_row = grid_cord[0];
		int grid_column = grid_cord[1];
		//System.out.println("grid: " + new Point(grid_cord[0], grid_cord[1]));
		if(building_doesnt_fit(space_taken, grid_cord[0], grid_cord[1], beltGrid)){
			return;
		}
		
		
		
		
		int ghost_X = 64 * (space_taken[1] + 1);
		int ghost_Y = 64 * (space_taken[0] + 1);
		BufferedImage ghost;
			ghost = new BufferedImage(ghost_X, ghost_Y, BufferedImage.TYPE_INT_ARGB);
		//BufferedImage ghost = new BufferedImage(128, 64 * (space_taken[0] + 1), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D ghost_grf = (Graphics2D) ghost.getGraphics();
		int rotation = add_belt_orientation;
		
		if(add_belt_type == add_belt_type_regular){
			if(beltGrid.has(grid_row, grid_column))
				return;
			Belt belt = Belt.makeBelt(rotation, new int[]{-1,-1,-1,-1}, grid_row, grid_column);
			Belt.revert_after_building_fake();
			ghost_grf.drawImage(belt.image, 0, 0, null);
			ghost = Images.setAlpha(ghost, 128);
			grf.drawImage(ghost, belt.x - cameraX, belt.y - cameraY, null);
			
			return;
		}
		if(beltGrid.has(grid_row, grid_column) || beltGrid.has(grid_row + space_taken[0], grid_column + space_taken[1]))
			return;
		//System.out.println("drawing balancer ghost");
		Balancer balancer = new Balancer(beltGrid, grid_row, grid_column, rotation);
		for(int i = 0; i < 4; i++)
			Belt.revert_after_building_fake();
		ghost_grf.drawImage(balancer.image, 0, 0, null);
		
		grf.drawImage(ghost, balancer.x - cameraX,balancer.y - cameraY,null);
		Belt.making_ghost = false;
//		BufferedImage belt = Images.beltUpImage;
//		for(int i = 0; i < rotation; i++)
//			belt = Images.rotateBy90(belt);
//		belt = Images.setAlpha(belt, 128);
//		int belt_x = belt_grid_left + (grid_column - 1) *belt_grid_width;
//		int belt_y = belt_grid_top + (grid_row - 1)*belt_grid_width;
//		System.out.println("ghost at: " + new Point(belt_x, belt_y));
//		grf.drawImage(belt, belt_x,belt_y,null);
		
	}
	
	public static boolean building_doesnt_fit(int[] space_taken, int row, int column, BeltGrid beltGrid){
		return false;
	}
	public void draw(Graphics2D grf, int graphical_iteration){
		//BufferedImage image = new BufferedImage(screen.getWidth(), screen.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		
		//Main_Class.log.log_line("drawing belts");
		//Main_Class.log.tab("drawing belts");
		//Graphics2D image_grf = image.createGraphics();
		draw_belts(grf);
		//Main_Class.log.untab("drawing belts");
		//Main_Class.log.log_line("drawing belt lines");
		//Main_Class.log.tab("drawing belt lines");
		belt_iterator.draw_belt_lines(grf);
		//Main_Class.log.untab("drawing belt lines");
		//Main_Class.log.log_line("drawing items in list");
		//Main_Class.log.tab("drawing items in list");
		belt_iterator.draw_items_in_list(grf, graphical_iteration);
		//Main_Class.log.untab("drawing items in list");
		//Main_Class.log.log_line("drawing ghost");
		//Main_Class.log.tab("drawing ghost");
		
		//grf.drawImage(image, 400, 0, null);
		if(touching_belt_layer)
			draw_belt_ghost(grf);
		//Main_Class.log.untab("drawing ghost");
	}
	
	
	private void draw_locations_on_curveToRight(Graphics grf){
		grf.setColor(Color.black);
		Belt belt = beltGrid.belts.get(0);
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
		//Balancer balancer = new Balancer(beltGrid, 8, 5, 1);
		//grf.drawImage(balancer.image, balancer.x - cameraX, balancer.y - cameraY, null);
		for(int i = 0; i < beltGrid.balancers.size(); i++){
			Balancer balancer = beltGrid.balancers.get(i);
			grf.drawImage(balancer.get_image(), balancer.x - cameraX, balancer.y - cameraY, null);
		}
		for(int i = 0; i < beltGrid.belts.size(); i++){
			if(beltGrid.belts.get(i) instanceof Belt_In_Balancer)
				continue;
			draw_belt(grf, beltGrid.belts.get(i), false);
		}
	}
	
	private void draw_belt(Graphics2D grf, Belt belt, boolean at_zero){
		int left = belt.x;
		int top = belt.y;
		if(at_zero){
			left = 0;
			top = 0;
		}
		grf.drawImage(belt.image, left - cameraX,top - cameraY,null);
		
		grf.setColor(Color.white);
		grf.fillRect(left - cameraX + 28, top - cameraY + 20, 24, 24);
		grf.setColor(Color.black);
		grf.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		grf.drawString(String.valueOf(belt.arrayIndex), left - cameraX + 32,  top - cameraY + 32);
		if(belt.image == null)
			System.out.println("image is null");
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
	public static int[] cord_pixel_to_belt(int x, int y){
		return new int[]{
				
				((y-belt_grid_top)/belt_grid_width) + 1,
				((x-belt_grid_left)/belt_grid_width) + 1
		};
	}
	public static int[] grid_to_pixel(int row, int column){
		return new int[]{
				(belt_grid_top + belt_grid_width * (row - 1)),
				(belt_grid_left + belt_grid_width * (column - 1))};
	}
	//endregion
	
	public static int drawString(Graphics2D grf, String string, int left, int top, int width, int height, Color color, int size){
		BufferedImage image = new BufferedImage(width, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics2D image_grf = (Graphics2D) image.getGraphics();
		image_grf.setColor(color);
		int total_height = drawStringAtTopLeft(image_grf, string, width-10, 0, size);
		grf.drawImage(image, left, top, null);
		return total_height;
		
	}
	public static int drawStringAtTopLeft(Graphics2D g, String text, int width, int height, int letter_height) {
		FontMetrics fm = g.getFontMetrics();
		float lineHeight = fm.getHeight();
		float ratio = (float)(letter_height)/lineHeight;
		Font font = g.getFont();
		Font new_font = font.deriveFont(font.getStyle(), font.getSize()*ratio);
		g.setFont(new_font);
		fm = g.getFontMetrics();
		lineHeight = fm.getHeight();
		
		
		
		int x = 0;
		int y = fm.getAscent();
		
		String[] words = text.split(" ");
		
		for (String word : words) {
			int wordWidth = fm.stringWidth(word + " ");
			
			if (x + wordWidth >= width && x != 0) {
				x = 0;
				y += lineHeight;
			}
			
			g.drawString(word, x, y);
			//System.out.println("drawing word: " + word + " at " + x + ", " + y);
			x += wordWidth;
		}
		return y;
	}
	
	
	public static BufferedImage createDropShadow(int image_width, int image_height, int shadowSize, int shadowOpacity) {
		int width = image_width + shadowSize * 2;
		int height = image_height + shadowSize * 2;
		
		BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = shadow.createGraphics();
		
		//g2d.drawImage(image, shadowSize, shadowSize, null);
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, (float) shadowOpacity / 255.0f));
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		
		g2d.dispose();
		
		return shadow;
	}
	
	public static void drawDropShadow(Graphics2D grf, int left, int top, int image_width, int image_height, int shadowSize, int shadowOpacity){
		int width = image_width + shadowSize * 2;
		int height = image_height + shadowSize * 2;
		
		BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D image_grf = (Graphics2D) shadow.getGraphics();
		Composite composite = grf.getComposite();
		image_grf.setColor(Color.black);
		image_grf.fillRect(0, 0, width, height);
		image_grf.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, (float) shadowOpacity / 255.0f));
		image_grf.setColor(Color.BLACK);
		image_grf.fillRect(0, 0, width, height);
		grf.drawImage(shadow, left-shadowSize, top - shadowSize, null);
		grf.setComposite(composite);
		grf.setColor(Color.black);
		
	}
	
	public void print_state(){
		System.out.println("printing state");
		belt_iterator.print_state();
		System.out.println("belts: "+beltGrid.belts.size()+" ~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < beltGrid.belts.size(); i++){
			System.out.println(""+beltGrid.belts.get(i));
			for(int j = 0; j < 4; j++){
				if(beltGrid.belts.get(i).beltsAround(j) != null)
					System.out.print(beltGrid.belts.get(i).beltsAround(j).arrayIndex + ", ");
				else
					System.out.print("N, ");
			}
			System.out.println();
		}
		System.out.println("\tstate done~~~~~~~~~~~~~~~~~~~~~~~");
	}
	
}
