package New_Belt_Package;



import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static New_Belt_Package.First.Enum.*;

abstract public class Belt {
    public static final int length = 64;
    public static final int horizontalItemPosition = 19;
    public static final int itemSize = 32;
    public static final int items_per_belt = 8;
    public static final int items_per_side_straight = items_per_belt/2;
    public static final int items_long_side = items_per_belt*3/4;
    public static final int items_short_side = items_per_belt/4;
    public static final int iterations_per_position = 4;
    BufferedImage image;
    int shape;
    int orientation;
    Belt[] beltsAround;
    int x;
    int y;
    boolean[] checked_side_for_list;
    int[] list_from_side;
    int arrayIndex;
    int[][][] itemCordShifted;
    
    
    static int[][][] curveRightCordRelative;
    static int[][][] curveLeftCordRelative;
    static int[][][] straightCordRelative;
    
    static boolean setup = false;

    public Belt(int orientation, int[] oAround, int x, int y) {
        beltsAround = new Belt[4];
        this.orientation = orientation;
        this.x = x;
        this.y = y;
        setImage();
        checked_side_for_list = new boolean[]{false,false};
        itemCordShifted = new int[items_per_belt*iterations_per_position][2][2];
        list_from_side = new int[2];
        if(!setup){
            setup = true;
            setup();
        }
    }
    
    public abstract int max_items(int side);
    
    private static void setup(){
        int straight_length_short = 0;
        int straight_length_long = 24;
        int long_iterations = items_long_side*iterations_per_position;
        int short_iterations = items_short_side*iterations_per_position;
        
        curveLeftCordRelative = new int[items_per_belt * iterations_per_position][2][2];
        curveRightCordRelative = new int[items_per_belt * iterations_per_position][2][2];
        straightCordRelative = new int[items_per_belt * iterations_per_position][2][2];
        
        condensed_iteration_array_one_side(curveRightCordRelative, 0, long_iterations, straight_length_long);
        condensed_iteration_array_one_side(curveRightCordRelative, 1, short_iterations, straight_length_short);
        
        for(int i = 0; i < long_iterations; i++){
            curveLeftCordRelative[i][1][0] = length - curveRightCordRelative[i][0][0];
            curveLeftCordRelative[i][1][1] = curveRightCordRelative[i][0][1];
        }
    
        for(int i = 0; i < short_iterations; i++){
            curveLeftCordRelative[i][0][0] = length - curveRightCordRelative[i][1][0];
            curveLeftCordRelative[i][0][1] = curveRightCordRelative[i][1][1];
        }
        setLocation_straight(straightCordRelative);
    }
    
    public void shift_item_locations(){
        int[][][] rel_itemCord;
        if(shape == straight)
            rel_itemCord = straightCordRelative;
        else if (shape == curveToLeft)
            rel_itemCord = curveLeftCordRelative;
        else
            rel_itemCord = curveRightCordRelative;
        for(int i = 0; i < itemCordShifted.length; i++){
            int x = rel_itemCord[i][0][0];
            int y = rel_itemCord[i][0][1];
            itemCordShifted[i][0][0] = shiftItemCord(0, x, y);
            itemCordShifted[i][0][1] = shiftItemCord(1, x, y);
            
            x = rel_itemCord[i][1][0];
            y = rel_itemCord[i][1][1];
            itemCordShifted[i][1][0] = shiftItemCord(0, x, y);
            itemCordShifted[i][1][1] = shiftItemCord(1, x, y);
        }
        
    }
    
    public int get_list_from_side(int side){
        return list_from_side[side];
    }

    public int[][] get_line(int side){
        int[][] line = new int[itemCordShifted.length][2];
        for(int i = 0; i < itemCordShifted.length; i++){
            line[i][0] = itemCordShifted[i][side][0] + itemSize/2;
            line[i][1] = itemCordShifted[i][side][1] + itemSize/2;
        }
        return line;
    }
    
    public static Belt makeBelt(int orientation, int[] oAround, int x, int y){
        int shape = checkBeltType(orientation, oAround);
        switch(shape){
            case straight: return new StraightBelt(orientation, oAround, x, y);
            case curveToLeft: return new CurveToLeft(orientation, oAround, x, y);
            case curveToRight: return new CurveToRight(orientation, oAround, x, y);
        }
        return new StraightBelt(orientation, oAround, x, y);
    }
    
    public int[] get_item_location(int iteration, int side){
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
    public static int checkBeltType(int orientation, int[] oAround) {
        int shape = 0;

        boolean backBelt = false;
        boolean leftBelt = false;
        boolean rightBelt = false;

        if(oAround[(down + orientation)%4] == (up + orientation)%4){
            backBelt = true;
            shape = straight;
            if(oAround[(left + orientation)%4] == (right + orientation)%4)
                leftBelt = true;
            if(oAround[(right + orientation)%4] == (left + orientation)%4)
                rightBelt = true;
        }
        else if(oAround[(left + orientation)%4] == (right + orientation)%4 && oAround[(right + orientation)%4] == (left + orientation)%4) {
            leftBelt = rightBelt = true;
            shape = straight;
        }
        else if(oAround[(left + orientation)%4] == (right + orientation)%4){
            backBelt = true;
            shape = curveToLeft;
            //System.out.println("In switch 2");

        }
        else if(oAround[(right + orientation)%4] == (left + orientation)%4){
            backBelt = true;
            shape = curveToRight;
            //System.out.println("In switch 3" + orientation +" "+ oAround[2]);

        }
        else {
            shape = straight;
        }
        return shape;
    }

    abstract public void setImage();

    abstract public boolean getInputPriorityAndSide(Belt belt, int side, IntWrap newSide, IntWrap newPosition);
    abstract public Belt getInputBeltAndSide(int side, IntWrap newSide);

    abstract public int getLengthFromSide(int side);
    
    protected int shiftItemCord(int xy, int x, int y){
        if(xy == 0)
            return this.x + xMatrix[orientation][0] * x + xMatrix[orientation][1] * y + rotateOffsetMatrix[orientation][0] * 64 - 16;
        return this.y + yMatrix[orientation][0] * x + yMatrix[orientation][1] * y + rotateOffsetMatrix[orientation][1] * 64 - 16;
    }
    public void linkBelts(Belt [] belts){
        beltsAround = belts;
    }

    @Override
    public String toString() {
        String num = String.valueOf(arrayIndex);
        String className = this.getClass().getSimpleName();
        return className + " " + num;
    }
    
    
    private static void setLocation_straight(int[][][] itemCord){
        int x;
        int y;
        //left is index 0
        int num = horizontalItemPosition;
        int initial = length - 8;
        int iterations_per_side = items_per_side_straight*iterations_per_position;
        int Ychange = length / iterations_per_side;
        int temp;
        for(int i = 0; i < iterations_per_side; i++){
            //System.out.println("setting location index: " + arrayIndex);
            x = horizontalItemPosition;
            y = initial - i * Ychange;
            itemCord[i][0][0] = x;
            itemCord[i][0][1] = y;
        
            x = length - horizontalItemPosition;
            //y = initial - i * Ychange;
            itemCord[i][1][0] = x;
            itemCord[i][1][1] = y;
        }
    }
    
    private static void condensed_iteration_array_one_side(int[][][] itemCord, int side, int iterations, int straight_length){
        int multiplier = 8;
        int[][][] temp = new int[iterations*multiplier][2][2];
        large_iteration_array_one_side(temp, side, iterations*multiplier, straight_length);
        for(int i = 0; i < iterations*multiplier; i++){
            int x = temp[i][side][0];
            int y = temp[i][side][1];
            /*
            if(i%8 == 0 && i!=0)
                System.out.println();
            System.out.print("("+x+","+y+"), ");
            */
        }
        System.out.println();
        for(int i = 0; i < iterations; i++){
            
            try{
                itemCord[i][side][0] = temp[i*multiplier][side][0];
                itemCord[i][side][1] = temp[i*multiplier][side][1];
            }catch(Exception exception){
                exception.printStackTrace();
                System.out.println("side: " + side);
                System.out.println("i: " + i);
                throw(exception);
            }
        }
    }
    
    private static void large_iteration_array_one_side(int[][][] itemCord, int side, int iterations, int straight_length){
        //this shows the equations for the curve
        //https://www.desmos.com/calculator/ty9wkq0dm6
        //System.out.println("one side~~~~~~~~~~~");
        int belt_size = length;
        int item_size = 16;
        int item_offset = item_size/2;
        int horizontal_offset = horizontalItemPosition;
        
        int custom_horizontal_offset;
        if(side == 0)
            custom_horizontal_offset = horizontal_offset;
        else
            custom_horizontal_offset = belt_size - horizontal_offset;
        
        int radius = belt_size - (item_offset + straight_length + custom_horizontal_offset);
        int radius_center_x = belt_size - (item_offset + straight_length);
        int radius_center_y = item_offset + straight_length;
        
        int horizontal_straight_start = belt_size - item_offset;
        int horizontal_straight_length = straight_length;
        int vertical_straight_start = straight_length + item_offset;
        int vertical_straight_length = straight_length + 2*item_offset;
        
        int iterations_left = iterations;
        int circle_length = (int)(radius * 3.14 / 2.0);
        int length = circle_length + vertical_straight_length + horizontal_straight_length;
        int total_length = length;
        int horizontal_straight_iterations = (int)((1.0 * iterations_left * horizontal_straight_length)/length);
        iterations_left = iterations - horizontal_straight_iterations;
        
        length = (int)(radius * 3.14 / 2.0) + vertical_straight_length;
        int vertical_straight_iterations = (int)((1.0 * iterations_left * vertical_straight_length)/length);
        int circleIterations = iterations_left - vertical_straight_iterations;
		
		/*
		System.out.println("straight length: " + straight_length + " total length: " + total_length);
		System.out.println("radius: "+radius+" (center x: "+radius_center_x+") (center y: "+radius_center_y);
		System.out.println("circle iterations: " + circleIterations);
		System.out.println("circle length: " + circle_length);
		System.out.println("horizontal straight iterations: " + horizontal_straight_iterations);
		System.out.println("vertical straight iterations: " + vertical_straight_iterations);
		System.out.println("horizontal: ");
		System.out.println("start x: " + horizontal_straight_start);
		System.out.println("start y: " + (belt_size - custom_horizontal_offset));
		System.out.println("length: " + horizontal_straight_length);
		System.out.println("vertical: ");
		System.out.println("start x: " + custom_horizontal_offset);
		System.out.println("start y: " + vertical_straight_start);
		System.out.println("length: " + vertical_straight_length);
		
		*/
        int count = 0;
        
        for(int i = count; i < count + horizontal_straight_iterations; i++){
            int x = horizontal_straight_start - ( horizontal_straight_length*(i - count + 0) ) / (horizontal_straight_iterations);
            int y = belt_size - custom_horizontal_offset;
            itemCord[i][side][0] = x;
            itemCord[i][side][1] = y;
        }
        count = horizontal_straight_iterations;
        double diffAngle = (3.14 / 2) / circleIterations;
        for(int i = count; i < count + circleIterations; i++){//1 to 5 or 0 to 4
            int x = (int) (radius_center_x - radius * (Math.sin(diffAngle * (i - count))));
            int y = (int) (radius_center_y + radius * (Math.cos(diffAngle * (i - count))));
            itemCord[i][side][0] = x;
            itemCord[i][side][1] = y;
        }
        count = horizontal_straight_iterations + circleIterations;
        for(int i = count; i < count + vertical_straight_iterations; i++){
            int x = custom_horizontal_offset;
            int y = vertical_straight_start - ( vertical_straight_length*(i - count + 0) ) / (vertical_straight_iterations);
            itemCord[i][side][0] = x;
            itemCord[i][side][1] = y;
        }
    }


}
