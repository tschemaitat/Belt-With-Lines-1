package New_Belt_Package;

import static New_Belt_Package.First.Enum.curveToRight;
import static New_Belt_Package.First.Enum.right;
import static New_Belt_Package.First.Images.*;

public class CurveToRight extends Belt {

    public CurveToRight(int orientation, int[] oAround, int x, int y) {
        super(orientation, oAround, x, y);
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

    public boolean getInputPriorityAndSide(Belt belt, int side, IntWrap newSide, IntWrap newPosition){
        newPosition.value = 0;
        newSide.value = side;
        return true;
    }

    public Belt getInputBeltAndSide(int side, IntWrap newSide){
        newSide.value = side;
        return beltsAround[(right + orientation)%4];
    }

    public int getLengthFromSide(int side){
        return 2 + 4 * (side / 3);
    }
}
