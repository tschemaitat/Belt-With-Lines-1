package Belt_Package;

import static Belt_Package.First.Enum.*;
import static Belt_Package.First.Enum.right;

public class Belt_In_Balancer extends StraightBelt {
	public boolean left_belt_of_balancer;
	public boolean front_of_balancer;
	
	public Belt_In_Balancer(int orientation, int grid_x, int grid_y, boolean is_left, boolean is_front){
		super(null, orientation, grid_x, grid_y);
		left_belt_of_balancer = is_left;
		front_of_balancer = is_front;
		
		if(!making_ghost){
			System.out.println("made balancer: " + this + "is left? " + is_left + ", is front? " + is_front);
			int count = 0;
			for(int i = 0; i < itemCordShifted.length; i++){
				System.out.print("("+itemCordShifted[i][0][0]+", "+itemCordShifted[i][0][1]+"), ");
				count = (count + 1)%4;
				if(count == 0)
					System.out.println();
			}
		}
		
	}
	
	
	@Override
	public int max_items(int side){
		return items_per_side_straight/2;
	}
	
	public int[] get_item_location(int iteration, int side){
		//System.out.print("asking for item loc at: " + iteration);
		//this make it look at the front of the belt
		if(front_of_balancer)
			iteration += iterations_per_position * 2;
		//System.out.print(", new iteration: " + iteration);
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
	
	//this belt is unique in that it will only connect with the belts above and below it
	public boolean getInputPriorityAndSide(Belt belt, int side, IntWrap newSide, IntWrap newPosition, BooleanWrap can_output){
		setAroundBooleans(getoAround());
		
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
	public String vert(){
		if(front_of_balancer){
			return "top";
		}
		return "bot";
	}
	
	public String hor() {
		if (left_belt_of_balancer)
			return "left";
		return "right";
	}
	public int[][] get_line(int side){
		int difference = 0;
		if(front_of_balancer)
			difference += iterations_per_position * 4;
		int[][] line = new int[max_items(side)][2];
		for(int i = 0; i < max_items(side); i++){
			line[i][0] = itemCordShifted[i + difference][side][0] + itemSize/2;
			line[i][1] = itemCordShifted[i + difference][side][1] + itemSize/2;
		}
		return line;
	}
	public String toString(){
		return "<balancer belt, "+vert()+", "+hor()+"  "+grid_row+", "+grid_column+">";
	}
}
