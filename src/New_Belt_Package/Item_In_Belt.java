package New_Belt_Package;

public class Item_In_Belt {
	Belt belt;
	int position;
	int current_iteration;
	boolean moving_towards_a_position;
	int side;
	public int x = 0;
	public int y = 0;
	int[][] future_graphic_positions;
	
	public Item_In_Belt(Belt belt, int initial_pos, int initial_side){
		moving_towards_a_position = false;
		this.belt = belt;
		position = initial_pos;
		side = initial_side;
		future_graphic_positions = new int[Belt.iterations_per_position][2];
		
		
		//need this to start moving it
		position++;
		set_future_graphic_positions(position - 1, side, belt, position, side, belt);
		moving_towards_a_position = true;
		current_iteration = 0;
		set_coordinate();
	}
	
	private void set_future_graphic_positions(int old_position, int old_side, Belt old_belt, int new_position, int new_side, Belt new_belt){
		int iterations = Belt.iterations_per_position;
		
		for(int i = 1; i < iterations; i++){
			future_graphic_positions[i - 1] = old_belt.get_item_location(old_position*iterations + i, sidefix(old_side));
		}
		future_graphic_positions[iterations - 1] = new_belt.get_item_location(new_position*iterations, sidefix(new_side));
		//System.out.println("new position ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//System.out.println("old: (pos: " + old_position + " side: " + old_side + " belt: " + old_belt + ") new: (pos: " + new_position + " side: " + new_side + " belt: " + new_belt + ")");
	}
	
	private void set_coordinate(){
		int[] cord = future_graphic_positions[current_iteration];
		x = cord[0];
		y = cord[1];
	}
	
	private static int sidefix(int side){
		return side;//(side/2 - 1)*-1;
	}
	
	public void iterate(){
		if(moving_towards_a_position){
			//System.out.println("moving_towards_a_position");
			//if we are at iteration 0-2 we move to the next iterations
			//else we stay at the 3rd iteration until we can start moving to a new position
			if(current_iteration < Belt.iterations_per_position - 1){
				//System.out.println("advancing iterations");
				current_iteration++;
				set_coordinate();
			}else{
				//System.out.println("try new position");
				moving_towards_a_position = false;
				//try_to_move_to_new_position();
			}
		}
		//System.out.println("not moving");
	}
	/*
	private void try_to_move_to_new_position(){
		int max_position = belt.max_items(side);
		//if we are at the max position
		if(position == max_position - 1){
			//System.out.println("try new belt");
			Belt new_belt = belt.beltsAround(belt.orientation);
			if(new_belt == null){
				//System.out.println("no new belt");
				moving_towards_a_position = false;
				return;
			}
			//System.out.println("got new belt");
			
			IntWrap new_side = new IntWrap();
			IntWrap new_position = new IntWrap();
			new_belt.getInputPriorityAndSide(belt, side, new_side, new_position);
			
			set_future_graphic_positions(position, side, belt, new_position.value, new_side.value, new_belt);
			current_iteration = 0;
			set_coordinate();
			position = new_position.value;
			belt = new_belt;
			side = new_side.value;
			moving_towards_a_position = true;
		} else{//let move the item
			//System.out.println("new position in same belt");
			//start doing the first animation which is the first iteration moving away from the old position
			position++;
			set_future_graphic_positions(position - 1, side, belt, position, side, belt);
			current_iteration = 0;
			set_coordinate();
			moving_towards_a_position = true;
		}
	}
	*/
}
