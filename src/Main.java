import Main_and_Drawing.*;
import New_Belt_Package.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {
	public static void main(String []args){
		belt_game();
	}
	
	public static void belt_game(){
		//customize_print();
		Screen screen = new Screen(800, 800);
		Manager manager = new Manager(screen.get_parent_layout());
		Graphics2D grf =  (Graphics2D)screen.get_graphics();
		RenderingHints qualityHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON );
		qualityHints.put(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY );
		grf.setRenderingHints( qualityHints );
		
		//do_ticks(manager, grf);
		
		
		
		
		//manager.draw_Items_In_Belts_Old(grf);
		//manager.draw_belt_lines(grf);
		
		do_ticks(manager, grf, screen);
		
	}
	static int tick = 0;
	public static void do_ticks(Manager manager, Graphics grf, Screen screen){
		while(1==1){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			//manager.iterate_items();
			
			
			
			
			BufferedImage buffer = new BufferedImage(800, 800, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g_buffer = (Graphics2D)buffer.getGraphics();
			g_buffer.setColor(Color.gray);
			g_buffer.fillRect(0, 0, 800, 800);
			//manager.draw_Items_In_Belts_Old(g_buffer);
			screen.pop_mouse_event_to_observe();
			
			if(tick%4 == 0)
				manager.iterate_belt_lists();
			int iteration_into_tick = tick%4;//0,1,2,3
			int graphical_iteration = (4-1) - iteration_into_tick;
			manager.graphical_iteration = graphical_iteration;
			screen.update_screen();
			
			//grf.drawImage(buffer, 0, 32, null);
			tick++;
		}
	}
	
	public static void tester(){
		Screen screen = new Screen(800, 800);
		Graphics2D grf =  (Graphics2D)screen.get_graphics();
		RenderingHints qualityHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON );
		qualityHints.put(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY );
		grf.setRenderingHints( qualityHints );
		
		BufferedImage buffer = new BufferedImage(800, 800, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g_buffer = buffer.getGraphics();
		g_buffer.setColor(Color.gray);
		g_buffer.fillRect(0, 0, 800, 800);
		
		
		
		int[][][] cords_1 = setLocationOfBothSides();
		draw_locations_of_cord(g_buffer, cords_1, 100, 100);
		
		int[][][]cords_2 = new int[2*items_per_side*iterations_per_item][2][2];
		int iterations_left = items_per_side*3/2*iterations_per_item;
		int iterations_right = iterations_left/3;
		condensed_iteration_array_one_side(cords_2, 0, iterations_left, 16
		);
		condensed_iteration_array_one_side(cords_2, 1, iterations_right, 0);
		draw_locations_of_cord(g_buffer, cords_2, 200, 100);
		
		grf.drawImage(buffer, 0, 0, null);
	}
	
	static int items_per_side = 4;
	static int iterations_per_item = 4;
	
	public static int[][][] setLocationOfBothSides(){
		int length = 64;
		int horizontalItemPosition = 20;
		int arrayIndex = 0;
		int iteration_per_position_change = iterations_per_item;
		
		int[][][] itemCord = new int[items_per_side*2 * iteration_per_position_change][2][2];
		
		//left is index 0 right is 1
		
		//constants same for right and left curve
		
		
		int items_left_side = items_per_side*3/2;
		int items_right_side = items_per_side/2;
		
		int iterations = items_left_side*iteration_per_position_change;
		int y_change_position = length/items_per_side;
		int y_change = length/iterations;
		
		int item_start_offset = y_change_position/2;
		int radius = length - (horizontalItemPosition + y_change_position/2);
		
		int y_bottom = length - horizontalItemPosition;
		int y_top = item_start_offset;
		int x_right = length - item_start_offset;
		int x_left = horizontalItemPosition;
		
		
		
		int circleIterations = (items_left_side - 1) * iteration_per_position_change;
		int totalIteration = (items_left_side) * iteration_per_position_change;
		
		//consants for second side of belt
		int x_right_final = length - horizontalItemPosition;
		int iterations_curve_right = (items_right_side - 1) * iteration_per_position_change;
		int iterations_total_right = (items_right_side) * iteration_per_position_change;
		int curve_yChange_right = item_start_offset - horizontalItemPosition;
		
		if(arrayIndex == 0){
			System.out.println("iterations: " + iterations);
			System.out.println("(initial y: " + y_bottom + ") initial x: " + x_right);
			System.out.println("(radius: " + radius + ") (end of curve y: " + y_top +")");
			System.out.println("(y_change_position: " + y_change_position + ") (y_change: " + y_change + ")");
			System.out.println("(circle iterations: " + circleIterations + ") (iterations: " + iterations + ")");
		}
		
		double diffAngle = (3.14 / 2) / circleIterations;
		for(int i = 0; i < circleIterations; i++){//1 to 5 or 0 to 4
			int x = (int) (x_right - radius * (0 + Math.sin(diffAngle * i)));
			int y = (int) (y_bottom - radius * (1.0 - Math.cos(diffAngle * i)));
			itemCord[i][0][0] = x;
			itemCord[i][0][1] = y;
			//System.out.print("X: " + itemCord[i][0][1]);
		}
		for(int i = circleIterations; i < totalIteration; i++){//1 to 5 or 0 to 4
			int x = x_left;
			int y = y_top - (y_change_position * (i-circleIterations))/(totalIteration - circleIterations);
			itemCord[i][0][0] = x;
			itemCord[i][0][1] = y;
			//System.out.print("X: " + itemCord[i][0][1]);
		}
		//System.out.println();
		
		for(int i = 0; i < iterations_curve_right; i++){
			int x = x_right + (curve_yChange_right * i) / (iterations_curve_right);
			int y = x_left + (curve_yChange_right * i) / (iterations_curve_right);
			itemCord[i][1][0] = x;
			itemCord[i][1][1] = y;
		}
		for(int i = iterations_curve_right; i < iterations_total_right; i++){//1 to 5 or 0 to 4
			int x = x_right_final;
			int y = y_top - (y_change_position * (i-iterations_curve_right))/(iterations_total_right - iterations_curve_right);
			itemCord[i][1][0] = x;
			itemCord[i][1][1] = y;
			//System.out.print("X: " + itemCord[i][0][1]);
		}
		
		if(arrayIndex == 0){
			System.out.println("left: ");
			int item_size = 32;
			for(int i = 0; i < totalIteration; i++){
				if(i%iteration_per_position_change == 0)
					System.out.println();
				System.out.print("("+(itemCord[i][0][0]) + ", " + (itemCord[i][0][1]) + "), ");
				
			}
			System.out.println("\nright: ");
			for(int i = 0; i < totalIteration / 3; i++){
				if(i%iteration_per_position_change == 0)
					System.out.println();
				System.out.print("("+(itemCord[i][1][0]) + ", " + (itemCord[i][1][1]) + "), ");
			}
			System.out.println("\ndone");
		}
		
		return itemCord;
	}
	
	public static void condensed_iteration_array_one_side(int[][][] itemCord, int side, int iterations, int straight_length){
		int multiplier = 8;
		int[][][] temp = new int[iterations*multiplier][2][2];
		large_iteration_array_one_side(temp, side, iterations*multiplier, straight_length);
		for(int i = 0; i < iterations*multiplier; i++){
			int x = temp[i][side][0];
			int y = temp[i][side][1];
			if(i%8 == 0 && i!=0)
				System.out.println();
			System.out.print("("+x+","+y+"), ");
			
		}
		System.out.println();
		for(int i = 0; i < iterations; i++){
			itemCord[i][side][0] = temp[i*multiplier][side][0];
			itemCord[i][side][1] = temp[i*multiplier][side][1];
		}
	}
	
	public static void large_iteration_array_one_side(int[][][] itemCord, int side, int iterations, int straight_length){
		//this shows the equations for the curve
		//https://www.desmos.com/calculator/ty9wkq0dm6
		//System.out.println("one side~~~~~~~~~~~");
		int belt_size = 64;
		int item_size = 16;
		int item_offset = item_size/2;
		int horizontal_offset = 18;
		
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
	
	private static void draw_locations_of_cord(Graphics grf, int[][][] cord, int x_offset, int y_offset){
		grf.setColor(Color.black);
		
		
		int item_size = 16;
		int block_size = 2;
		
		int iterations = iterations_per_item*items_per_side;
		int items_on_left = iterations*3/2;
		int items_on_right = iterations/2;
		int[][] cords_left = new int[items_on_left][];
		int[][] cords_right = new int[items_on_right][];
		for(int i = 0; i < items_on_left; i++){
			cords_left[i] = cord[i][0];
		}
		for(int i = 0; i < items_on_right; i++){
			cords_right[i] = cord[i][1];
		}
		for(int i = 0; i < items_on_left; i++){
			int multiplier = 1;
			if(i%iterations_per_item == 0)
				multiplier = 1;
			int x = cords_left[i][0] + x_offset - block_size * multiplier;
			int y = cords_left[i][1] + y_offset - block_size * multiplier;
			grf.fillRect(x, y, block_size * multiplier, block_size * multiplier);
		}
		for(int i = 0; i < items_on_right; i++){
			int multiplier = 1;
			if(i%iterations_per_item == 0)
				multiplier = 1;
			int x = cords_right[i][0] + x_offset - block_size * multiplier;
			int y = cords_right[i][1] + y_offset - block_size * multiplier;
			grf.fillRect(x, y, block_size * multiplier, block_size * multiplier);
		}
		grf.drawRect(x_offset, y_offset, 64, 64);
	}
	
	public static void customize_print() {
		System.setOut(new java.io.PrintStream(System.out) {
			
			private StackTraceElement getCallSite() {
				for (StackTraceElement e : Thread.currentThread()
						.getStackTrace())
					if (!e.getMethodName().equals("getStackTrace")
							&& !e.getClassName().equals(getClass().getName()))
						return e;
				return null;
			}
			
			@Override
			public void println(String s) {
				println((Object) s);
			}
			
			@Override
			public void println(Object o) {
				StackTraceElement e = getCallSite();
				String callSite = e == null ? "??" :
						String.format("%s.%s(%s:%d)",
								e.getClassName(),
								e.getMethodName(),
								e.getFileName(),
								e.getLineNumber());
				super.println(o + "\t\tat " + callSite);
			}
		});
	}
}
