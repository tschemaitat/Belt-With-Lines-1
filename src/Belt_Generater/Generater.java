package Belt_Generater;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static Belt_Package.First.Enum.*;

public class Generater {
    public Generater(){

    }
    //belt data should be x, y, direction
    public Belt[] make_belt_bulk(HashMap<Position, Integer> belt_data){
        System.out.println("make belt bulk");
        Belt[] belts = new Belt[belt_data.size()];
        //Collection<Integer> values = belt_data.values();
        //Set<Integer[]> keys = belt_data.keySet();
        int belt_index = -1;
        System.out.println(belt_data);
        for(Position position : belt_data.keySet()){
            belt_index++;
            int direction = belt_data.get(position);
            System.out.println("iterating pos: " + position + ", direction: " + direction);
            int[] direction_of_around = new int[4];
            for(int i = 0; i < 4; i++) {
                Integer value = belt_data.get(position.add_direction(i));
                if(value == null){
                    direction_of_around[i] = -1;
                    System.out.println("\tcannot find direction of pos: " + position.add_direction(i));
                }
                else
                    direction_of_around[i] = value;
            }
            belts[belt_index] = make_belt(position, direction, direction_of_around);
        }
        return belts;
    }

    public Belt make_belt(Position position, int direction, int[] direction_of_around){

        int shape = checkBeltType(direction, direction_of_around);
        System.out.println("\toAround: " + direction_of_around[0] + ", " + direction_of_around[1] + ", " + direction_of_around[2] + ", " + direction_of_around[3] + ", " + ", shape: " + shape);
        switch(shape){
            case straight: return new Belt(new Straight_Data(), position, direction);
            case curveToLeft: return new Belt(new CurveLeft_data(), position, direction);
            case curveToRight: return new Belt(new CurveRight_Data(), position, direction);
        }
        throw new RuntimeException();
    }

    public static int checkBeltType(int direction, int[] oAround) {
        int shape = 0;

        boolean backBelt = false;
        boolean leftBelt = false;
        boolean rightBelt = false;

        if(oAround[(down + direction)%4] == (up + direction)%4){
            System.out.println("\thas back belt");
            backBelt = true;
            shape = straight;
            if(oAround[(left + direction)%4] == (right + direction)%4)
                leftBelt = true;
            if(oAround[(right + direction)%4] == (left + direction)%4)
                rightBelt = true;
        }
        else if(oAround[(left + direction)%4] == (right + direction)%4 && oAround[(right + direction)%4] == (left + direction)%4) {
            System.out.println("\thas both left and right belt");
            leftBelt = rightBelt = true;
            shape = straight;
        }
        else if(oAround[(left + direction)%4] == (right + direction)%4){
            System.out.println("\thas left and no back belt");
            backBelt = true;
            shape = curveToLeft;
            //System.out.println("In switch 2");

        }
        else if(oAround[(right + direction)%4] == (left + direction)%4){
            System.out.println("\thas right and no back belt");
            backBelt = true;
            shape = curveToRight;
            //System.out.println("In switch 3" + orientation +" "+ oAround[2]);
        }
        else {
            System.out.println("\tno cases found");
            shape = straight;
        }
        return shape;
    }
}
