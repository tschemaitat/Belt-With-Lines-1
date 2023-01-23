package New_Belt_Package;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Belt_List {
	private List<Belt> belts;//the front most belt is the last index
	private List<Integer> sides;
	int self_index;
	private int[][][] cords;//this is to load the coordinate with inputs position
	//the front most positions will the first indexes
	//indexs: [position][iterations between position][xy]
	//with 4 iterations per position, maybe we do 3 is the first iteration and 0 is the final iteration
	private List<Belt> belts_from_position;//input position as index to get belt
	private List<Integer> side_from_position;
	private List<Integer> belt_position_from_position;
	private List<Item_In_List> items;
	private int amount_positions;
	
	private Belt output_belt;
	private int output_side;
	private int output_position;
	
	private List<Belt_List> belt_lists;
	
	private int moving_at_and_after_this_index = -1;
	
	public Belt_List(int self_index, List<Belt_List> belt_lists){
		this.belt_lists = belt_lists;
		this.self_index = self_index;
		belts = new ArrayList<>();
		sides = new ArrayList<>();
	}
	
	//region compilation
	public void add_belt_front(Belt belt, int side){
		belts.add(belt);
		sides.add(side);
		belt.checked_side_for_list[side] = true;
		belt.list_from_side[side] = self_index;
		
	}
	
	public void add_belt_behind(Belt belt, int side){
		belts.add(0, belt);
		sides.add(0, side);
		belt.checked_side_for_list[side] = true;
		belt.list_from_side[side] = self_index;
	}
	
	public void set_output(Belt b, int s, int p){
		output_belt = b;
		output_side = s;
		output_position = p;
	}
	
	public void compile(){
		System.out.println("list ("+size()+") compiling~~~~~~~~~~~~~~~~");
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
			System.out.println("index_starting at belt("+index_belt+"): "+list_index_start_of_belt);
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
			
			
			for(int index_position_in_belt = belt.max_items(side) - 1; index_position_in_belt >= 0; index_position_in_belt--){
				int list_index_start_of_position = list_index_start_of_belt + (belt.max_items(side) - 1) - index_position_in_belt;
				System.out.println("index_starting at position("+index_position_in_belt+"): "+list_index_start_of_position);
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
					System.out.println("max backwards position: " + position_backwards_position);
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
					System.out.println("adding iteration: "+iteration_between_position + " at index: "+(iter_per_position - iteration_between_position));
				}
				
				cords_as_list.add(position_cords);
				belts_from_position.add(belt);
				side_from_position.add(sides.get(index_belt));
				belt_position_from_position.add(index_position_in_belt);
				System.out.print("position array: ");
				for(int i = 0; i < position_cords.length; i++){
					System.out.print("("+position_cords[i][0]+", "+position_cords[i][1] + ") ");
				}
				System.out.println();
				
				
				
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
		
		
	}
	
	//endregion
	
	//region strings
	
	public String toString(){
		String result = "";
		for(int i = 0; i < belts.size(); i++){
			result += "(belt: " + belts.get(i);
			result += ", side: " + sides.get(i) + ")";
			if(i != belts.size() - 1)
				result+= ", ";
		}
		return result;
	}
	
	public String belt_index_and_side(){
		String result = "";
		for(int i = 0; i < belts.size(); i++){
			result += "(b: " + belts.get(i).arrayIndex;
			result += ", s: " + sides.get(i) + ")";
			if(i != belts.size() - 1)
				result+= ", ";
		}
		return result;
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
	
	//endregion
	
	//region line graphics
	public void draw_line(Graphics2D grf, Color color){
		//draw_line_from_belt(grf, color);
		draw_line_from_position_list(grf, color);
	}
	
	private void draw_line_from_position_list(Graphics2D grf, Color color){
		//System.out.println("drawing: ("+size()+")");
		grf.setColor(color);
		List<int[]> list_cord = new ArrayList<>();
		
		for(int i = 1; i < cords.length; i++){
			int[][] positions = cords[i];
			for(int cord = 0; cord < positions.length; cord++){
				list_cord.add(positions[cord]);
			}
		}
		//System.out.println("number of coordinates: " + list_cord.size());
		
		for(int i = 0; i < list_cord.size() - 1; i++){
			int x1 = list_cord.get(i)[0] + Belt.itemSize/2;
			int y1 = list_cord.get(i)[1] + Belt.itemSize/2;
			int x2 = list_cord.get(i+1)[0] + Belt.itemSize/2;
			int y2 = list_cord.get(i+1)[1] + Belt.itemSize/2;
			grf.drawLine(x1, y1, x2, y2);
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
	
	public int get_list_position_from_beltPositionSide(Belt belt, int position, int side){
		int belt_index = -1;
		for(int i = 0; i < belts_from_position.size(); i++){
			if(belts_from_position.get(i) == belt && belt_position_from_position.get(i) == position && side_from_position.get(i) == side){
				belt_index = i;
			}
		}
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
	
	public void iterate_items(){
		/*
		if(self_index != 0){
			moving_at_and_after_this_index = 0;
			return;
		}
		*/
		
		System.out.println("iterating items: ");
		if(items.size() == 0){
			System.out.println("just empty list");
			moving_at_and_after_this_index = 0;
			return;
		}
		if(items.get(0).empty){
			System.out.println("all push because empty at front");
			items.remove(0);
			moving_at_and_after_this_index = 0;
			return;
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
				//System.out.println("semi push at: " + i);
				index_pushed_at = i;
				items.remove(i);
				moving_at_and_after_this_index = i;
				break;
			}
		}
		if(push_to_output(items.get(0))){
			//System.out.println("was able to push to output");
			moving_at_and_after_this_index = 0;
			items.remove(0);
			if(index_pushed_at != -1)
				items.add(index_pushed_at - 1, Item_In_List.new_empty());
			return;
		}
		//System.out.println("did not push to output moving index: " + moving_at_and_after_this_index + "index pushed at: " + index_pushed_at);
	}
	
	public boolean push_to_output(Item_In_List item){
		if(output_belt == null)
			return false;
		int output_list_index = output_belt.get_list_from_side(output_side);
		Belt_List list_output = belt_lists.get(output_list_index);
		int output_list_position = list_output.get_list_position_from_beltPositionSide(output_belt, output_position, output_side);
		if(list_output.has_item(output_list_position)){
			return false;
		}
		list_output.add_item_by_position(output_list_position, item);
		return true;
	}
	//endregion
	
	//region item graphics
	public void draw_items(Graphics2D grf, int graphical_iteration){
		if(items.size() != 0){
			//System.out.println("drawing items, list index: " + self_index + " moving index: " + moving_at_and_after_this_index);
			if(self_index == 0)
			for(int i = 0; i < items.size(); i++){
				System.out.println(items.get(i).name + ", " );
			}
			
		}
		
		for(int i = 0; i < moving_at_and_after_this_index && i < items.size(); i++){
			
			Item_In_List item = items.get(i);
			if(item.empty)
				continue;
			int[] xy = cords[i][0];
			//System.out.println("drawing non-moving: " + i + " "+array_to_cord(xy));
			item.draw(grf, xy[0], xy[1]);
		}
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
