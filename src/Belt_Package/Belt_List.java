package Belt_Package;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Belt_List {
	private ArrayList<Belt> belts;//the front most belt is the last index
	private ArrayList<Integer> sides;
	int self_index;
	public int[][][] cords;//this is to load the coordinate with inputs position
	//the front most positions will the first indexes
	//indexs: [position][iterations between position][xy]
	//with 4 iterations per position, maybe we do 3 is the first iteration and 0 is the final iteration
	public ArrayList<Belt> belts_from_position;//input position as index to get belt
	public ArrayList<Integer> side_from_position;
	public ArrayList<Integer> belt_position_from_position;
	private ArrayList<Item_In_List> items;
	private int amount_positions;
	public boolean has_iterated = false;
	public int current_iteration_id = -1;
	public String iteration_mode = "";
	private ArrayList<Belt> log_order_of_belts_added = new ArrayList<>();
	ArrayList<Belt_List> input_lists = new ArrayList<>();
	/*
	additem() O(n)
	iterate() O(n)
	
	list
	add O(n)
	iterate O(1)
	
	//0204510101010
	//101..
	
	//204510
	
	array
	add O(1)
	iterate O(n)
	
	
	*/
	
	boolean has_two_outputs = false;
	public LocationStruct output_locationStruct;
	private LocationStruct output_locationStruct_two;
	private ArrayList<Belt_List> belt_lists;
	private int moving_at_and_after_this_index = -1;
	
	public Belt_List(int self_index, ArrayList<Belt_List> belt_lists){
		this.belt_lists = belt_lists;
		this.self_index = self_index;
		belts = new ArrayList<>();
		sides = new ArrayList<>();
	}
	
	public void log_input_list(Belt_List list){
		input_lists.add(list);
	}
	
	//region compilation
	public void add_belt_front(Belt belt, int side){
		belts.add(belt);
		sides.add(side);
		belt.add_to_list(side, this);
		log_order_of_belts_added.add(belt);
	}
	public void add_belt_behind(Belt belt, int side){
		belts.add(0, belt);
		sides.add(0, side);
		belt.add_to_list(side, this);
		log_order_of_belts_added.add(belt);
	}
	public void set_output(LocationStruct output){
		output_locationStruct = output;
	}
	public void set_two_outputs(LocationStruct output1, LocationStruct output2){
		has_two_outputs = true;
		output_locationStruct = output1;
		output_locationStruct_two = output2;
	}
	public void compile(){
		//System.out.println("list ("+size()+") compiling~~~~~~~~~~~~~~~~");
		List<int[][]> cords_as_list = new ArrayList<>();
		belts_from_position = new ArrayList<>();
		side_from_position = new ArrayList<>();
		belt_position_from_position = new ArrayList<>();
		//constructs arrays that input stack position and outputs coordinates
		
		//last index belt becomes the first index of the stack
			//int index_list_position = 0;
			//belt((size-1) - belt_index) -> cord[(0, 4) * belt_index)][][]
				//for(int index_into_belt; index_into_belt < belt.max_items(side); index_into_belt++)
					
					//int[] cord = belt.get_cord(
				//position[(belt.max_items - 1) - index_list_position_startOfBelt] -> cord[index_list_position][][]
		int index_list_position = 0;
		int list_index_start_of_belt = 0;
		for(int index_belt = belts.size() - 1; index_belt >= 0; index_belt--){
			//System.out.println("index_starting at belt("+index_belt+"): "+list_index_start_of_belt);
			Belt belt = belts.get(index_belt);
			int side = sides.get(index_belt);
			Belt backward_belt;
			int backward_side;
			if(index_belt == 0){
				backward_belt = null;
				backward_side = -1;
			} else{
				backward_belt = belts.get(index_belt - 1);
				backward_side = sides.get(index_belt - 1);
			}
			//we get iterations_per_position - 1 cordinates from the backwards position
			//we get a final coordinate from our current position
//			if(index_belt == 0 && belt instanceof Belt_In_Balancer){
//				((Belt_In_Balancer) belt).getting_item_front = false;
//			}
//			if(index_belt == belts.size() - 1 && belt instanceof Belt_In_Balancer){
//				((Belt_In_Balancer) belt).getting_item_front = true;
//			}
			
			
			
			for(int index_position_in_belt = belt.max_items(side) - 1; index_position_in_belt >= 0; index_position_in_belt--){
				int list_index_start_of_position = list_index_start_of_belt + (belt.max_items(side) - 1) - index_position_in_belt;
				//System.out.println("index_starting at position("+index_position_in_belt+"): "+list_index_start_of_position);
				int[][] position_cords = new int[Belt.iterations_per_position][2];
				//the position we get 1 coordinate from
				Belt position_current_belt = belt;
				int position_current_position = index_position_in_belt;
				int position_current_side = side;
				
				//position where we get the graphics leading up to our current position
				Belt position_backwards_belt;
				int position_backwards_position;
				int position_backwards_side;
				
				int iter_per_position = Belt.iterations_per_position;
				int[] temp_cord = belt.get_item_location(index_position_in_belt*iter_per_position, side);
				//position_cords[0] = belt.get_item_location(index_position_in_belt*iter_per_position, side);
				position_cords[0][0] = temp_cord[0];// + Belt.itemSize/2;
				position_cords[0][1] = temp_cord[1];// + Belt.itemSize/2;
				
				if(index_position_in_belt == 0 && index_belt == 0){
					//we do not set where the item is before entering the line
					int[][] temp = new int[1][];
					temp[0] = position_cords[0];
					cords_as_list.add(temp);
					belts_from_position.add(belt);
					side_from_position.add(sides.get(index_belt));
					belt_position_from_position.add(index_position_in_belt);
					break;
				}
				
				if(index_position_in_belt == 0){
					position_backwards_belt = backward_belt;
					position_backwards_position = backward_belt.max_items(backward_side) - 1;
					//System.out.println("max backwards position: " + position_backwards_position);
					position_backwards_side = backward_side;
				} else{
					position_backwards_belt = belt;
					position_backwards_position = index_position_in_belt - 1;
					position_backwards_side = side;
				}
				
				
				//sets the iteration between entering a position, the actual position is at index 0, the other go from max_iter-1 -> 1 as the items approach the actual position
				for(int iteration_between_position = 1; iteration_between_position < Belt.iterations_per_position; iteration_between_position++){
					//if 4, we do 1,2,3->3,2,1
					//if 6, we do 5,4,3,2,1 -> 1,2,3,4,5 so max-index+2
					int current_index = iter_per_position - iteration_between_position;
					//position_cords[current_index] = position_backwards_belt.get_item_location(position_backwards_position*iter_per_position + iteration_between_position, position_backwards_side);
					temp_cord = position_backwards_belt.get_item_location(position_backwards_position*iter_per_position + iteration_between_position, position_backwards_side);
					position_cords[current_index][0] = temp_cord[0];// + Belt.itemSize/2;
					position_cords[current_index][1] = temp_cord[1];// + Belt.itemSize/2;
					//System.out.println("adding iteration: "+iteration_between_position + " at index: "+(iter_per_position - iteration_between_position));
				}
				
				cords_as_list.add(position_cords);
				belts_from_position.add(belt);
				side_from_position.add(sides.get(index_belt));
				belt_position_from_position.add(index_position_in_belt);
				//System.out.print("position array: ");
				for(int i = 0; i < position_cords.length; i++){
					//System.out.print("("+position_cords[i][0]+", "+position_cords[i][1] + ") ");
				}
				//System.out.println();
				
				
				
			}
			
			list_index_start_of_belt += belt.max_items(side);
		}
		//loads the positions
		amount_positions = cords_as_list.size();
		cords = new int[amount_positions][][];
		for(int i = 0; i < cords.length; i++){
			cords[i] = cords_as_list.get(i);
		}
		items = new ArrayList<>();
		System.out.println("printing list cord after compile: " + self_index);
		
		for(int i = 0; i < cords.length; i++){
			System.out.print("belt cords: ");
			for(int pos = 0; pos < cords[i].length; pos++){
				System.out.print("("+cords[i][pos][0]+", "+cords[i][pos][1]+"), ");
			}
			System.out.println();
		}
		
	}
	public void second_compile(){
		if(output_locationStruct.belt == null)
			return;
		//get_list_output().log_input_list(this);
	}
	//endregion
	
	public ArrayList<ItemLocationStruct> delete(){
		//System.out.println("deleting " + self_index);
		ArrayList<ItemLocationStruct> temp_item_list = new ArrayList<>();
		for(int i = 0; i < items.size(); i++){
			if(items.get(i).empty)
				continue;
			Item_In_List item = items.get(i);
			int position_in_belt = belt_position_from_position.get(i).intValue();
			int side = side_from_position.get(i).intValue();
			Belt belt = belts_from_position.get(i);
			temp_item_list.add(new ItemLocationStruct(item, position_in_belt, side, belt));
		}
		for(int i = 0; i < belts.size(); i++){
			belts.get(i).remove_list(sides.get(i));
		}
//		for(int i = 0; i < input_lists.size(); i++){
//			input_lists.get(i).set_output(new LocationStruct(-1, -1, null));
//		}
		return temp_item_list;
	}
	
	//region strings
	public String toString(){
		String result = ""+self_index;
		for(int i = 0; i < belts.size(); i++){
			result += "(b:" + belts.get(i);
			result += ", s:" + sides.get(i) + ")";
			if(i != belts.size() - 1)
				result+= ", ";
		}
		result+="\nlog: ";
		for(int i = 0; i < log_order_of_belts_added.size(); i++){
			result += log_order_of_belts_added.get(i).arrayIndex + ", ";
		}
		return result;
	}
	public String belt_index_and_side(){
		String result = "";
		for(int i = 0; i < belts.size(); i++){
			result += "(" + belts.get(i).arrayIndex;
			if(belts.get(i) instanceof Belt_In_Balancer)
				result += " "+((Belt_In_Balancer)belts.get(i)).hor() + " " + ((Belt_In_Balancer)belts.get(i)).vert();
			result += ", " + int_side_to_character(sides.get(i)) + ")";
			if(i != belts.size() - 1)
				result+= ", ";
		}
		return result;
	}
	public String int_side_to_character(int side){
		if(side == 0)
			return "L";
		if(side == 1)
			return "R";
		return "Nan";
	}
	public String stack_belt_side_and_position(boolean include_new_lines){
		String result = "";
		for(int i = 0; i < cords.length; i++){
			int[] cord_position = cords[i][0];
			Belt belt = belts_from_position.get(i);
			int side = side_from_position.get((cords.length - 1) - i);
			if(i%4 == 0 && i != 0 && include_new_lines)
				result += "\n";
			result += ("(belt: " + belt.arrayIndex + ", position: (" + cord_position[0]+","+cord_position[1]+"), side: "+side+"pos: " + belt_position_from_position.get(i) + ")");
		}
		result += "\nside list: " + sides;
		return result;
	}
	public String item_characters(){
		String result = "";
		for(int i = 0; i < items.size(); i++){
			result += items.get(i).name.substring(0, 1);
		}
		return result;
	}
	//endregion
	
	//region line graphics
	public void draw_line(Graphics2D grf, Color color){
		//System.out.println("drawing: ("+size()+")");
		int x_offset = (int) (Math.random()*10) - 5;
		int y_offset = (int) (Math.random()*10) - 5;
		grf.setColor(color);
		List<int[]> list_cord = new ArrayList<>();
		
		for(int i = 0; i < cords.length; i++){
			int[][] positions = cords[i];
			for(int cord = 0; cord < positions.length; cord++){
				list_cord.add(positions[cord]);
			}
		}
		//System.out.println("number of coordinates: " + list_cord.size());
		//Color[] colors = {Color.BLUE, Color.CYAN, Color.RED, Color.GREEN, Color.yellow, Color.BLACK, Color.ORANGE};
		//int color_index = 0;
		for(int i = 0; i < list_cord.size() - 1; i++){
			int x1 = list_cord.get(i)[0] + Belt.itemSize/2;
			int y1 = list_cord.get(i)[1] + Belt.itemSize/2;
			int x2 = list_cord.get(i+1)[0] + Belt.itemSize/2;
			int y2 = list_cord.get(i+1)[1] + Belt.itemSize/2;
			//color_index = (color_index + 1) % colors.length;
			//grf.setColor(colors[color_index]);
			//System.out.println("("+x1+", "+y1+", "+x2+", "+y2+"), ");
			grf.drawLine(x1 - Entity_Manager.cameraX /*+ x_offset*/, y1 - Entity_Manager.cameraY /*+ y_offset*/, x2 - Entity_Manager.cameraX /*+ x_offset*/, y2 - Entity_Manager.cameraY /*+ y_offset*/);
			/*
			if(i%4 == 0 && i != 0)
				System.out.println();
			System.out.print("("+x1+", "+y1+")");
			if(i == list_cord.size() - 2)
				System.out.print("("+x2+", "+y2+")");
			 */
		}
		//
		// System.out.println();
		
	}
	private void draw_line_from_belt(Graphics2D grf, Color color){
		grf.setColor(color);
		//System.out.println("drawing line");
		List<int[][]> list_lines = new ArrayList<>();
		int[] temp_cord = null;
		for(int i = 0; i < belts.size(); i++){
			Belt belt = belts.get(i);
			int side = sides.get(i);
			int[][] belt_graphical_line = belt.get_line(side);
			if(temp_cord != null){
				int x1 = temp_cord[0];
				int y1 = temp_cord[1];
				int x2 = belt_graphical_line[0][0];
				int y2 = belt_graphical_line[0][1];
				list_lines.add(new int[][]{{x1, y1},{x2, y2}});
			}
			for(int j = 0; j < (belt.max_items(side)) * Belt.iterations_per_position - 1; j++){
				int x1 = belt_graphical_line[j][0];
				int y1 = belt_graphical_line[j][1];
				int x2 = belt_graphical_line[j+1][0];
				int y2 = belt_graphical_line[j+1][1];
				list_lines.add(new int[][]{{x1, y1},{x2, y2}});
			}
			int last_index = (belt.max_items(side)) * Belt.iterations_per_position;
			temp_cord = new int[]{belt_graphical_line[last_index - 1][0], belt_graphical_line[last_index - 1][1]};
		}
		
		for(int i = 0; i < list_lines.size(); i++){
			int[][] line_grf = list_lines.get(i);
			grf.drawLine(line_grf[0][0], line_grf[0][1], line_grf[1][0], line_grf[1][1]);
		}
	}
	//endregion
	
	//region items
	public boolean add_item_by_position(int position, Item_In_List item){
		if(has_item(position))
			return false;
		
		//0,1,2,3
		//add 4
		//just add it on the end
		//0,1,2
		//add 4
		//do loop to add empty 3-3
		
		if(items.size() == position){
			items.add(item);
			return true;
		}
		
		if(items.size() < position){
			for(int i = items.size(); i < position; i++){
				items.add(Item_In_List.new_empty());
			}
			items.add(item);
			return true;
		}
		
		items.remove(position);
		items.add(position, item);
		return true;
	}
	public int get_list_position_from_beltPositionSide(LocationStruct location){
		int belt_index = -1;
		for(int i = 0; i < belts_from_position.size(); i++){
			if(belts_from_position.get(i) == location.belt && belt_position_from_position.get(i) == location.position && side_from_position.get(i) == location.side){
				belt_index = i;
			}
		}
		if(belt_index == -1)
			System.out.println("could not find location belt: "+location.belt+", pos: " + location.position + ", side: "+location.side);
		return belt_index;
	}
	public boolean has_item(int position){
		
		int position_counter = 0;
		if(items.size() <= position)
			return false;
		
		if(items.get(position).empty){
			return false;
		}
		return true;
	}
	public boolean is_last_item(int position){
		if(position == items.size() - 1)
			return true;
		return false;
	}
	public int iterate_items(int iteration_id){
		
		if(has_iterated)
			return -1;
		
		moving_at_and_after_this_index = items.size();
		//System.out.println("iterating items");
		has_iterated = true;
		current_iteration_id = iteration_id;
		/*
		if(self_index != 0){
			moving_at_and_after_this_index = 0;
			return;
		}
		*/
		
		//System.out.println("iterating items: ");
		if(items.size() == 0){
			//System.out.println("just empty list");
			moving_at_and_after_this_index = 0;
			iteration_mode = "no items";
			return -1;
		}
		if(items.get(0).empty){
			iteration_mode = "front empty";
			//System.out.println("all push because empty at front");
			items.remove(0);
			moving_at_and_after_this_index = 0;
			return 1;
		}
		moving_at_and_after_this_index = cords.length;
		//if there is empty spot at front, just push all items
		//if empty just return
		//if there an item, push all possible items and then check belt in front
			//if we were able to push to output, then delete first index and replace empty at index_pushed_at
		//1,1,0,1,1
		//1,1,1,1 deleted at 2 (pushed after 2)
		//1,1,1 pushed all items
		//1,0,1,1 replaced at 1
		//System.out.println("moving index before semi push attempt: "+moving_at_and_after_this_index);
		int index_pushed_at = -1;
		for(int i = 0; i < items.size(); i++){
			if(items.get(i).empty){
				iteration_mode = "semi-push";
				//System.out.println("semi push at: " + i);
				index_pushed_at = i;
				items.remove(i);
				moving_at_and_after_this_index = i;
				break;
			}
		}
		if(push_to_output(items.get(0))){
			iteration_mode = "pushed output";
			//System.out.println("was able to push to output");
			moving_at_and_after_this_index = 0;
			items.remove(0);
			if(index_pushed_at != -1)
				items.add(index_pushed_at - 1, Item_In_List.new_empty());
			return 1;
		}
		tryingcycle:
		if(output_locationStruct.belt != null){
			Belt_List list_output = output_locationStruct.belt.get_list_from_side(output_locationStruct.side);
			int output_list_position = list_output.get_list_position_from_beltPositionSide(output_locationStruct);
			if(!list_output.is_last_item(output_list_position)){
				break tryingcycle;
			}
			if(list_output.current_iteration_id != iteration_id){
				break tryingcycle;
			}
			iteration_mode = "cycle";
			list_output.add_item_by_position(output_list_position + 1, items.get(0));
			items.remove(0);
			moving_at_and_after_this_index = 0;
			return 1;
		}
		
		//cannot push, push to output, send our iteration id
		//they cannot push, push to output, send the same id
		//they return
		
		
		return 0;
		
		/*
		if(output_locationStruct.belt == null)
			return;
		Belt_List list_output = output_locationStruct.belt.get_list_from_side(output_locationStruct.side);
		int output_list_position = list_output.get_list_position_from_beltPositionSide(output_locationStruct);
		if(list_output.current_iteration_id == iteration_id){
			if(output_list_position == list_output.items.size() - 1){
				System.out.println("found circle");
				moving_at_and_after_this_index = 0;
			}
		}
		 */
		//System.out.println("did not push to output moving index: " + moving_at_and_after_this_index + "index pushed at: " + index_pushed_at);
	}
	public boolean push_to_output(Item_In_List item){
		//System.out.println("push to output");
		if(output_locationStruct.belt == null)
			return false;
		Belt_List list_output = output_locationStruct.belt.get_list_from_side(output_locationStruct.side);
		int output_list_position = list_output.get_list_position_from_beltPositionSide(output_locationStruct);
		if(!list_output.has_iterated){
			//System.out.println("output list has iterated: "+ list_output.has_iterated + " so we are iterating it");
			list_output.iterate_items(current_iteration_id);
		}
		if(list_output.has_item(output_list_position)){
			//System.out.println("still has item, returning false");
			return false;
		}
		//System.out.println("empty space, adding item");
		list_output.add_item_by_position(output_list_position, item);
		return true;
	}
	public Belt_List get_list_output(){
		if(output_locationStruct.belt == null)
			return null;
		return output_locationStruct.belt.get_list_from_side(output_locationStruct.side);
	}
	//endregion
	
	//region item graphics
	public void draw_items(Graphics2D grf, int graphical_iteration){
		if(items.size() != 0){
			//System.out.println("drawing items, list index: " + self_index + " moving index: " + moving_at_and_after_this_index);
			
			/*
			if(self_index == 0)
			for(int i = 0; i < items.size(); i++){
				System.out.println(items.get(i).name + ", " );
			}
			*/
			
		}
		if(items.size() == 0)
			return;
		if(moving_at_and_after_this_index == -1)
			moving_at_and_after_this_index = items.size();
		
		for(int i = 0; i < moving_at_and_after_this_index && i < items.size(); i++){
			
			Item_In_List item = items.get(i);
			if(item.empty)
				continue;
			int[] xy = cords[i][0];
			//System.out.println("drawing non-moving: " + i + " "+array_to_cord(xy));
			item.draw(grf, xy[0], xy[1]);
		}
		//System.out.println(self_index+", drawing moving items from " +moving_at_and_after_this_index+ " to, "+items.size());
		if(moving_at_and_after_this_index < items.size())
		for(int i = moving_at_and_after_this_index; i < items.size(); i++){
			Item_In_List item = items.get(i);
			if(item.empty)
				continue;
			int[] xy;
			if(cords[i].length == 1){
				xy = cords[i][0];
				//System.out.println("drawing moving: " + i+",  " + 0 + " "+array_to_cord(xy));
			}
			
			else{
				xy = cords[i][graphical_iteration];
				//System.out.println("drawing moving: " + i+",  "+ graphical_iteration + " "+array_to_cord(xy));
			}
			
			item.draw(grf, xy[0], xy[1]);
		}
	}
	private String array_to_cord(int[] xy){
		return "("+xy[0]+", "+xy[1]+")";
	}
	//endregion
	
	
	
	public int size(){
		return belts.size();
	}
}
