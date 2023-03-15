package New_Belt_Package;

import static New_Belt_Package.First.Enum.*;
import static New_Belt_Package.First.Enum.right;

public class Belt_In_Balancer extends StraightBelt {
	public Belt_In_Balancer sibling_belt;
	public boolean left_belt_of_balancer;
	boolean[][] in_list = new boolean[][]{{false, false},{false, false}};
	
	public Belt_In_Balancer(BeltGrid beltGrid, int orientation, int grid_x, int grid_y, boolean is_left) {
		super(beltGrid, orientation, grid_x, grid_y);
		left_belt_of_balancer = is_left;
	}
	
	public boolean has_list(boolean front, int side){
		int front_num = 0;
		if(!front)
			front_num = 1;
		
		return in_list[front_num][side];
	}
	@Override
	public int max_items(int side){
		return items_per_side_straight/2;
	}
	
	boolean getting_item_front;
	public void set_get_item_location_FrontOrBack(boolean front){
		getting_item_front = front;
	}
	
	public int[] get_item_location_balancer(int iteration, int side){
		//this make it look at the front of the belt
		if(getting_item_front)
			iteration += iterations_per_position * 4;
		
		try{
			int[] relative_pos = itemCordShifted[iteration][side];
			//System.out.println("rel pos: " + relative_pos[0]+", "+relative_pos[1]);
			return new int[]{relative_pos[0], relative_pos[1]};
		}catch(Exception e){
			//System.out.println("this: " + this);
			//System.out.println("side: " + side);
			//System.out.println("shape: " + shape);
			//System.out.println("length: " + itemCordShifted.length);
			throw(e);
		}
		
	}
	
	public void set_has_list(boolean front, int side){
		int front_num = 0;
		if(!front)
			front_num = 1;
		
		in_list[front_num][side] = true;
	}
	
	public void set_sibling(Belt_In_Balancer sibling){
		sibling_belt = sibling;
	}
	
	//this belt is unique in that it will only connect with the belts above and below it
	public boolean getInputPriorityAndSide(Belt belt, int side, IntWrap newSide, IntWrap newPosition, BooleanWrap can_output){
		//setAroundBooleans(getoAround());
		
		if(belt == beltsAround((down + orientation)%4)){
			can_output.set(true);
		}
		else{
			can_output.set(false);
			System.out.println(this.arrayIndex + " setting can_output to false");
		}
		int direction = 0;
		for(int i = 0; i < 4; i++){
			if(beltsAround(i) == belt){
				direction = i;
				break;
			}
			else if(i == 3)
				System.out.println("Belt could not find direction of backwardBelt ThisBelt: " + toString() + " other belt: " + belt);
		}
		if(direction == (left + orientation)%4){
			newSide.value = 0;
			
			if(side == 1){
				newPosition.value = 0;
				if(backBelt)
					return false;
				return true;
			}
			newPosition.value = items_per_side_straight/2 + 1;
			return false;
		}
		if(direction == (right + orientation)%4){
			newSide.value = 1;
			if(backBelt)
				return false;
			if(side == 0){
				newPosition.value = 0;
				return true;
			}
			newPosition.value = items_per_side_straight/2 + 1;
			return false;
		}
		newSide.value = side;
		return true;
	}
	public Belt getInputBeltAndSide(int side, IntWrap newSide) {
		setAroundBooleans(getoAround());
		//if we have a back belt, the priority input is the like sides of that back belt
		if(backBelt){
			newSide.value = side;
			//System.out.println("Straight belt going strsight behind");
			return beltsAround((down + orientation)%4);
		}
		return null;
	}
	
	public int front_num(boolean front){
		if(!front)
			return 1;
		return 0;
		
	}
}
