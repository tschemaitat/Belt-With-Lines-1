package New_Belt_Package;

import java.util.List;

public class Belt_List_Factory {
	public static Belt_List construct_belt_list(Belt starting_belt, int starting_side, int list_index, List<Belt_List> belt_lists){
		Belt_List belt_list = new Belt_List(list_index, belt_lists);
		belt_list.add_belt_front(starting_belt, starting_side);
		
		//go forward
			//check if there is a belt in front (orientation)
				//if there is no belt leave loop
				//output belt is null
			//check if my belt is priority to belt in front (getInputPriorityAndSide)
				//if not priority (false) set output to that (belt, position, side)
		//go backward
			//find belt that is priority of the side were in (getInputBeltAndSide)
				//if null, leave loop (there is not belt inputting to this belt)
		
		Belt current_belt = starting_belt;
		int current_side = starting_side;
		boolean currently_adding = true;
		while(currently_adding){
			Belt forward = current_belt.beltsAround[current_belt.orientation];
			if(forward == null){
				belt_list.set_output(null, -1, -1);
				System.out.println("setting output: null because no belt");
				break;
			}
			IntWrap new_side = new IntWrap();
			IntWrap new_position = new IntWrap();
			boolean is_priority = forward.getInputPriorityAndSide(current_belt, current_side, new_side, new_position);
			if(is_priority == false){
				belt_list.set_output(forward, new_side.value, new_position.value);
				System.out.println("setting output: " + forward + " side: " + new_side.value + " pos: " + new_position.value);
				break;
			}
			belt_list.add_belt_front(forward, new_side.value);
			current_belt = forward;
			current_side = new_side.value;
			
			
		}
		currently_adding = true;
		
		current_belt = starting_belt;
		current_side = starting_side;
		while(currently_adding){
			IntWrap new_side = new IntWrap();
			Belt backwards_belt = current_belt.getInputBeltAndSide(current_side, new_side);
			if(backwards_belt == null)
				break;
			belt_list.add_belt_behind(backwards_belt, new_side.value);
			current_belt = backwards_belt;
			current_side = new_side.value;
		}
		
		System.out.println("finished building list: (" +belt_list.size() + ")\n" + belt_list.belt_index_and_side());
		
		
		
		
		return belt_list;
	}
}
