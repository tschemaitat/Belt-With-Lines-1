package New_Belt_Package;

import New_Belt_Package.Belt;
import New_Belt_Package.IntWrap;

import static New_Belt_Package.First.Enum.curveToLeft;
import static New_Belt_Package.First.Enum.left;
import static New_Belt_Package.First.Images.*;

public class CurveToLeft extends Belt {

    public CurveToLeft(int orientation, int[] oAround, int x, int y) {
        super(orientation, oAround, x, y);
        shape = curveToLeft;
        shift_item_locations();
    }
    
    public int max_items(int side){
        if(side == 0)
            return items_short_side;
        return items_long_side;
    }


    public void setImage(){
        switch(orientation){
            case 0: image = beltLeftToUpImage; break;
            case 1: image = beltUpToRightImage; break;
            case 2: image = beltRightToDownImage; break;
            case 3: image = beltDownToLeftImage; break;
        }
    }

    public boolean getInputPriorityAndSide(Belt belt, int side, IntWrap newSide, IntWrap newPosition){
        newPosition.value = 0;
        newSide.value = side;
        return true;
    }

    public Belt getInputBeltAndSide(int side, IntWrap newSide){
        newSide.value = side;
        return beltsAround[(left + orientation)%4];
    }


    public int getLengthFromSide(int side){
        return 6 - 2 * side / 3;
    }
}
