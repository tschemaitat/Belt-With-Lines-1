package New_Belt_Package;

import Main_and_Drawing.*;
import New_Belt_Package.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Main_Class {
	//public static My_Log log;
	public static void main(String []args){
		//log = new My_Log();
//		System.out.println("adding hello: ");
//		log.log_line("hello");
//		log.log_line("hello2");
//		log.tab();
//		log.log_line("tabbed");
//		log.untab();
//		log.log_line("hello3");
		belt_game();
	}
	
	public static void belt_game(){
		//customize_print();
		Screen screen = new Screen(800, 800);
		Manager manager = new Manager(screen.get_parent_layout(), screen);
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
		//log.log_line("10 ticks");
		//log.tab("10 ticks");
		boolean first_tab = true;
		while(1==1){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			
			//manager.iterate_items();
			
			BufferedImage buffer = new BufferedImage(1200, 800, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g_buffer = (Graphics2D)buffer.getGraphics();
			g_buffer.setColor(Color.gray);
			g_buffer.fillRect(0, 0, 800, 800);
			//manager.draw_Items_In_Belts_Old(g_buffer);
//			for(int i = 0; i < 30; i++){
//				int x = 400 + ((i%5)*40)%200;
//				int y = 200 + (i%6)*40%200;
//				screen.add_mouse_event(new MouseEvent_Edited(x, y, MouseEvent_Edited.type_click));
//			}
			
			
			if(tick%4 == 0){
				if(!first_tab){
					//log.untab("tick");
					if(tick%40 == 0 && tick != 0){
						//log.untab("10 ticks");
						//log.log_line("10 ticks: " + tick);
						//log.tab("10 ticks");
					}
				}else
					first_tab = false;
				
				//log.log_line("tick: " + tick);
				//log.tab("tick");
				manager.iterate_belt_lists();
			}
			
			//log.log_line("click tick: " + tick);
			//log.tab("click tick");
			screen.pop_mouse_event_to_observe();
			//log.untab("click tick");
			screen.observe_mouse_touch();
			
			int iteration_into_tick = tick%4;//0,1,2,3
			int graphical_iteration = (4-1) - iteration_into_tick;
			manager.graphical_iteration = graphical_iteration;
			
			screen.update_screen();
			
			//grf.drawImage(buffer, 0, 32, null);
			tick++;
		}
	}
	
	static int items_per_side = 4;
	static int iterations_per_item = 4;
	
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
