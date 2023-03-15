package New_Belt_Package;

import static New_Belt_Package.First.Enum.*;
import static New_Belt_Package.First.Images.*;

public class CurveToRight extends Belt {

    public CurveToRight(BeltGrid beltGrid, int orientation, int grid_x, int grid_y) {
        super(beltGrid, orientation, grid_x, grid_y);
        shape = curveToRight;
        shift_item_locations();
    }
    
    public int max_items(int side){
        if(side == 1)
            return items_short_side;
        return items_long_side;
    }


    public void setImage(){
            switch(orientation){
                case 0: image = beltRightToUpImage; break;
                case 1: image = beltDownToRightImage; break;
                case 2: image = beltLeftToDownImage; break;
                case 3: image = beltUpToLeftImage; break;
            }
    }

    public int[] getLocation(int position){
        return new int[]{0,0};
    }

    public boolean getInputPriorityAndSide(Belt belt, int side, IntWrap newSide, IntWrap newPosition, BooleanWrap can_output){
        if(belt == beltsAround((right + orientation)%4))
            can_output.set(true);
        else
            can_output.set(false);
        newPosition.value = 0;
        newSide.value = side;
        return true;
    }

    public Belt getInputBeltAndSide(int side, IntWrap newSide){
        newSide.value = side;
        return beltsAround((right + orientation)%4);
    }

    public int getLengthFromSide(int side){
        return 2 + 4 * (side / 3);
    }
}
