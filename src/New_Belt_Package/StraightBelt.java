package New_Belt_Package;


import static New_Belt_Package.First.Enum.*;
import static New_Belt_Package.First.Images.*;

public class StraightBelt extends Belt {
    
    boolean backBelt;
    boolean rightBelt;
    boolean leftBelt;

    public StraightBelt(Belt[][] beltGrid, int orientation, int grid_x, int grid_y) {
        super(beltGrid, orientation, grid_x, grid_y);
        setAroundBooleans(getoAround());
        shape = straight;
        shift_item_locations();
    }
    
    protected void setAroundBooleans(int[] oAround){
        backBelt = false;
        rightBelt = false;
        leftBelt = false;
        if(oAround[(down + orientation)%4] == (up + orientation)%4){
            backBelt = true;
            if(arrayIndex == 19){
                //System.out.println("BACKBELT IS TRUE");
            }
        }
        if(oAround[(left + orientation)%4] == (right + orientation)%4){
            leftBelt = true;
        }
        if(oAround[(right + orientation)%4] == (left + orientation)%4){
            rightBelt = true;
        }
    }


    public void setImage(){
        switch(orientation){
            case up: image = beltUpImage;
                break;
            case right: image = beltRightImage;
                break;
            case down: image = beltDownImage;
                break;
            case left: image = beltLeftImage;
                break;
        }
    }
    
    public int max_items(int side){
        return items_per_side_straight;
    }
    
    public boolean getInputPriorityAndSide(Belt belt, int side, IntWrap newSide, IntWrap newPosition, BooleanWrap can_output){
        setAroundBooleans(getoAround());
        
        if(belt != beltsAround((up + orientation)%4)){
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
    public Belt getInputBeltAndSide(int side, IntWrap newSide){
        setAroundBooleans(getoAround());
        //if we have a back belt, the priority input is the like sides of that back belt
        if(backBelt){
            newSide.value = side;
            //System.out.println("Straight belt going strsight behind");
            return beltsAround((down + orientation)%4);
        }
        //if left belt, our priority input on the left will be that left belt and comes from the right side
        if(side == 0 && leftBelt){
            newSide.value = 1;
            //System.out.println("Straight belt going left");
            return beltsAround((left + orientation)%4);
        }
        if(side == 1 && rightBelt){
            newSide.value = 0;
            //System.out.println("Straight belt going left");
            return beltsAround((right + orientation)%4);
        }
        //for a given side, we do not have a priority input
        //System.out.println("Straight belt no input");
        return null;
    }
    //64/(4*2) = 8  64-8-2=54

    public int getLengthFromSide(int side){
        return 4;
    }
}
