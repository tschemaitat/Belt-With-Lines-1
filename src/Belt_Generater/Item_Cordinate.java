package Belt_Generater;


import static Belt_Package.First.Enum.*;

public class Item_Cordinate {
    public static final int horizontalItemPosition = 19;
    public static final int itemSize = 32;
    public static final int items_per_belt = 8;
    public static final int items_long_side = 6;
    public static final int items_short_side = 2;
    public static final int iterations_per_position = 4;
    public static final int items_per_side_straight = 4;
    public static final int length = 64;

    static int[][][] curveRightCordRelative;
    static int[][][] curveLeftCordRelative;
    static int[][][] straightCordRelative;
    static{
        setup();
    }

    public static Position[][] get_belt_item_positions(Position belt_location, int direction, int shape){
        Position[][] positions_rel = get_relative_position(shape);
        for(int i = 0; i < positions_rel.length; i++){
            for(int j = 0; j < positions_rel.length; j++){
                positions_rel[i][j] = shiftItemCord(positions_rel[i][j], belt_location, direction, itemSize);
            }
        }
        return positions_rel;
    }

    public static Position[][] get_relative_position(int shape){
        int length_left;
        int length_right;
        int[][][] cords;
        switch(shape){
            case straight:
                cords = straightCordRelative;
                length_left = 4;
                length_right = 4;
                break;
            case curveToLeft:
                cords = curveLeftCordRelative;
                length_left = 2;
                length_right = 6;
                break;
            case curveToRight:
                cords = curveRightCordRelative;
                length_left = 6;
                length_right = 2;
                break;
            default:
                throw new RuntimeException();
        }
        Position[][] positions = new Position[2][];
        positions[0] = new Position[length_left * iterations_per_position];
        for(int i = 0; i < positions[0].length; i++){
            int[] cord = cords[i][0];
            positions[0][i] = new Position(cord[0], cord[1]);
        }
        positions[1] = new Position[length_right * iterations_per_position];
        for(int i = 0; i < positions[1].length; i++){
            int[] cord = cords[i][1];
            positions[1][i] = new Position(cord[0], cord[1]);
        }
        return positions;
    }

    public static Position shiftItemCord(Position old_position, Position belt_location, int orientation, int item_size){//if xy == 0 we want the x value, else we want the y value
        int x = old_position.x();
        int y = old_position.y();
        int new_x = belt_location.x() + xMatrix[orientation][0] * x + xMatrix[orientation][1] * y + rotateOffsetMatrix[orientation][0] * 64 - item_size/2;
        int new_y = belt_location.y() + yMatrix[orientation][0] * x + yMatrix[orientation][1] * y + rotateOffsetMatrix[orientation][1] * 64 - item_size/2;
        return new Position(new_x, new_y);
    }

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

    private static void setLocation_straight(int[][][] itemCord) {
        int x;
        int y;
        //left is index 0
        int num = horizontalItemPosition;
        int initial = length - 8;
        int iterations_per_side = items_per_side_straight * iterations_per_position;
        int Ychange = length / iterations_per_side;
        int temp;
        for (int i = 0; i < iterations_per_side; i++) {
            //System.out.println("setting location index: " + arrayIndex);
            x = horizontalItemPosition;
            y = initial - i * Ychange;
            itemCord[i][0][0] = x;
            itemCord[i][0][1] = y;
            //[pos][side][xy]
            //4 per side, 4 iter per pos
            //[16][2][2]
            //(pos 1, 0), (pos 2, 3)

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
