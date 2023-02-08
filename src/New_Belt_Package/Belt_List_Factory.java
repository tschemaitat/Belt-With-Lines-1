package New_Belt_Package;

import java.util.List;

public class Belt_List_Factory {
	public static int list_count = 0;
	public static Belt_List construct_belt_list(Belt starting_belt, int starting_side, List<Belt_List> belt_lists){
		System.out.println("making list: " + list_count+" ---------");
		System.out.println("starting at: " + starting_belt.arrayIndex);
		//System.out.println("making list: "+list_count+" ---------");
		Belt_List belt_list = new Belt_List(list_count, belt_lists);
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
		System.out.println("adding forwards");
		while(currently_adding){
			Belt forward = current_belt.beltsAround(current_belt.orientation);
			
			System.out.println("adding forward, belt: " + forward);
			if(forward == null){
				belt_list.set_output(null, -1, -1);
				System.out.println("forward null");
				break;
			}
			System.out.println("next belt: " + forward.arrayIndex);
			IntWrap new_side = new IntWrap();
			IntWrap new_position = new IntWrap();
			BooleanWrap can_output = new BooleanWrap(false);
			boolean is_priority = forward.getInputPriorityAndSide(current_belt, current_side, new_side, new_position, can_output);
			
			//belts are facing eachother I think
			if(can_output.value() == false){
				System.out.println("cannot output");
				belt_list.set_output(null, -1, -1);
				break;
			}
			
			//if we aren't the priority, end the list
			if(is_priority == false){
				System.out.println("not priority");
				belt_list.set_output(forward, new_side.value, new_position.value);
				System.out.println("setting output: " + forward + " side: " + new_side.value + " pos: " + new_position.value);
				break;
			}
			//if the belt belongs in a list (circle i think) just set it as the output
			if(forward.get_list(new_side.value) != null){
				System.out.println("already in list");
				belt_list.set_output(forward, new_side.value, new_position.value);
				break;
			}
			belt_list.add_belt_front(forward, new_side.value);
			current_belt = forward;
			current_side = new_side.value;
		}
		System.out.println("finished forwards");
		currently_adding = true;
		
		current_belt = starting_belt;
		current_side = starting_side;
		System.out.println("adding backwards");
		while(currently_adding){
			IntWrap new_side = new IntWrap();
			Belt backwards_belt = current_belt.getInputBeltAndSide(current_side, new_side);
			System.out.println("adding backwards, belt: " + backwards_belt);
			if(backwards_belt == null){
				System.out.println("no back belt");
				break;
			}
			System.out.println("back belt: " + backwards_belt.arrayIndex);
			
			if(backwards_belt.get_list(new_side.value) != null){
				System.out.println("in list already");
				break;
			}
			belt_list.add_belt_behind(backwards_belt, new_side.value);
			current_belt = backwards_belt;
			current_side = new_side.value;
		}
		
		System.out.println("finished backwards");
		
		//System.out.println("finished building list: (" +belt_list.size() + ")\n" + belt_list.belt_index_and_side());
		
		
		
		list_count++;
		return belt_list;
	}
}
