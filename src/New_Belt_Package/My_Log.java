package New_Belt_Package;
//	public static void Log_Line(String name, int num_tabs){
//		System.out.println("creating line: " + name + " num tabs: " + num_tabs);
//		this.num_tabs = num_tabs;
//		//open = true;
//		this.name = name;
//		BufferedImage temp = makeStringImage(name, width - 10*num_tabs, font_size);
//		log_image = new BufferedImage(width, temp.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
//		System.out.println("twod x pos: " + num_tabs*10);
//		log_image.getGraphics().drawImage(temp, num_tabs*10, 0, null);
//		RectP params = new RectP(num_tabs*10, 0, 8, 8);
//		Twod twod = new Twod(params, screen.get_parent_layout()) {
//			@Override
//			public void draw(Graphics2D grf) {
//				if(tab_touched)
//					grf.setColor(Color.lightGray);
//				else
//					grf.setColor(Color.black);
//				grf.fillRect(getX(), getY(), getWidth(), getHeight());
//
//			}
//
//			@Override
//			public void onMouseEvent(MouseEvent_Edited event) {
//				if(event.type == MouseEvent_Edited.type_click){
//					System.out.println("got click: " + name);
//					set_needs_UI_update();
//					set_needs_image_update();
//					if(open){
//						open = false;
//					}else{
//						open = true;
//					}
//				}
//				if(event.type == MouseEvent_Edited.type_touch)
//					tab_touched = true;
//				if(event.type == MouseEvent_Edited.type_untouch)
//					tab_touched = false;
//			}
//		};
//		twod.setInvisible();
//		twod.setUIInvisible();
//	}

import Main_and_Drawing.*;
import Main_and_Drawing.Layouts.RectP;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class My_Log{
	public static void main(String []args){
		java java = java().can().you().run().please();
		
		My_Log log = new My_Log();
		log.log_line("line 1");
		log.log_line("line 2");
		log.log_tab("line 3");
		log.log_tab("line 4");
		log.log_line("line 5");
		log.log_tab("line 7");
		log.log_line("hello");
		log.untab("line 7");
		log.log_line("line 5");
		log.update_log();
		
	}
	
	
	int width = 400;
	int height = 400;
	int font_size = 30;
	Log_Line current_line;
	public List<Log_Line> logs;
	Twod[] twods = new Twod[height/font_size];
	Log_Line[] lines_attached_to_UI = new Log_Line[height/font_size];
	
	Screen screen;
	List<String> tab_stack;
	
	public My_Log(){
		logs = new ArrayList<>();
		logs.add(new Log_Line("parent", null));
		current_line = logs.get(0);
		screen = new Screen(width, height);
		Layout layout = screen.get_parent_layout();
		RectP params = new RectP(0, 0, width, height);
		Layout button_layout = new Stack_Layout(0, 0, width, height);
		
		construct_UI(button_layout);
		
		Thread thread = new Thread(){
			@Override
			public void run() {
				//super.run();
				while(1==1){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					
					screen.update_screen();
					screen.pop_mouse_event_to_observe();
					screen.observe_mouse_touch();
					System.out.println("updating");
				}
			}
		};
		thread.start();
		
		
		Twod twod = new Twod(params, layout) {
			@Override
			public void draw(Graphics2D grf) {
				BufferedImage image = print();
				grf.drawImage(image, 0, 0, null);
			}
			
			@Override
			public void onMouseEvent(MouseEvent_Edited event) {
			
			}
		};
		layout.add(button_layout);
		
		
		
	}
	
	private void update_log(){
		screen.update_screen();
	}
	
	private void construct_UI(Layout parent){
		for(int i = 0; i < twods.length; i++){
			RectP rectP = new RectP(0, 0, 8, 8);
			int finalI = i;
			twods[i] = new Twod(rectP, parent) {
				boolean touched = false;
			
				@Override
				public void draw(Graphics2D grf) {
					grf.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
					grf.addRenderingHints((new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)));
					if(touched ^ !lines_attached_to_UI[finalI].open && lines_attached_to_UI[finalI].logs.size() > 0)
						grf.fillRect(getX(), getY(), getWidth(), getHeight());
					else
						grf.drawRect(getX(), getY(), getWidth(), getHeight());
				}
				
				@Override
				public void onMouseEvent(MouseEvent_Edited event) {
					if(event.type == MouseEvent_Edited.type_click){
						if(lines_attached_to_UI[finalI].open)
							lines_attached_to_UI[finalI].open = false;
						else
							lines_attached_to_UI[finalI].open = true;
						update_log();
					}
					if(event.type == MouseEvent_Edited.type_touch)
						touched = true;
					if(event.type == MouseEvent_Edited.type_untouch)
						touched = false;
					update_log();
				}
			};
			twods[i].setUIInvisible();
			twods[i].setInvisible();
		}
	}
	
	public BufferedImage print(){
		int tab = 0;
		int current_height = 0;
		boolean my_bool = true;
		List<Log_Line> log_stack = new ArrayList<>();
		List<Integer> child_index_stack = new ArrayList<>();
		
		log_stack.add(logs.get(0));
		child_index_stack.add(0);
		
		List<Log_Line> line_pointers = new ArrayList<>();
		List<String> lines = new ArrayList<>();
		List<Integer> num_tabs = new ArrayList<>();
		
		String result = "";
		boolean going_back;
		while(log_stack.size() > 0){
			System.out.println("while loop");
			
			int index = log_stack.size() - 1;
			//add children
			//if we have a children line, add child to stack
			if(log_stack.get(index).has_child(child_index_stack.get(index))){
				System.out.println("log stack: " + index + " has child: " + child_index_stack.get(index));
				Log_Line next = log_stack.get(index).logs.get(child_index_stack.get(index));
				log_stack.add(next);
				
				//add child to screen lines
				num_tabs.add(index);
				lines.add(next.name);
				line_pointers.add(next);
				
				//increment index of parent (not at top of stack anymore)
				int temp = child_index_stack.remove(index);
				child_index_stack.add(temp + 1);
				child_index_stack.add(0);
				continue;
			}
			else
				System.out.println("log stack: " + index + " does not have child: " + child_index_stack.get(index));
			//pop parent if they don't have any more children
			log_stack.remove(index);
			child_index_stack.remove(index);
			
			
		}
		for(int i = 0; i < lines.size(); i++){
			for(int j = 0; j < num_tabs.get(i); j++){
				result += "\t";
			}
			result += lines.get(i) + "\n";
		}
		for(int i = 0; i < twods.length; i++){
			twods[i].setInvisible();
			twods[i].setUIInvisible();
			lines_attached_to_UI[i] = null;
		}
		
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D grf = image.createGraphics();
		grf.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		grf.addRenderingHints((new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)));
		grf.addRenderingHints((new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)));
		int button_height = 8;
		
		for(int i = 0; i < lines.size(); i++){
			int offset = 20 + num_tabs.get(i) * 10;
			if(i < twods.length){
				System.out.println("making " + i + " visible");
				twods[i].setVisible();
				twods[i].setUIVisible();
				//System.out.println("set x: )
				twods[i].setX(8+num_tabs.get(i) * 10);
				twods[i].setY(font_size/2 - 4+current_height);
				lines_attached_to_UI[i] = line_pointers.get(i);
			}
			current_height += drawString(grf, lines.get(i), offset, current_height, width, 0, Color.BLACK, font_size);
			
			
			
			
		}
		
		
		System.out.println(result);
		return image;
	}
	
	public void log_line(String line){
		current_line.add_child(line);
	}
	
	public void tab(String line){
		current_line = current_line.get_last_child();
	}
	public void untab(String line){
		current_line = current_line.parent;
	}
	
	
	
	public void log_tab(String line){
		log_line(line);
		tab(line);
	}
	
	private class Log_Line{
		Log_Line parent;
		private boolean open = true;
		private List<Log_Line> logs;
		String name;
		int name_height;
		private boolean needs_update = false;
		private int total_height;
		public Log_Line(String name, Log_Line parent){
			this.parent = parent;
			this.name = name;
			logs = new ArrayList<>();
			name_height = calculate_name_height();
		}
		
		public int get_total_height(){
			if(!open)
				return name_height;
			if(!needs_update)
				return total_height;
			total_height = name_height;
			for(int i = 0; i < logs.size(); i++){
				total_height += logs.get(i).get_total_height();
			}
			return total_height;
		}
		
		
		private int calculate_name_height(){
			return get_string_height(name, width, font_size);
		}
		
		public void toggle_open(){
			if(open)
				open = false;
			else
				open = true;
		}
		
		public void set_needs_update(){
			needs_update = true;
			parent.set_needs_update();
		}
		
		public boolean has_child(int index){
			if(!open)
				return false;
			if(logs.size() - 1 >= index)
				return true;
			return false;
		}
		
		public void add_child(String name){
			logs.add(new Log_Line(name, this));
		}
		
		public Log_Line get_last_child(){
			return logs.get(logs.size() - 1);
		}
		
		private int calculate_total_height(){
			int total = name_height;
			if(open)
				for(int i = 0; i < logs.size(); i++){
					total += logs.get(i).calculate_total_height();
				}
			return total;
		}
		
		
		
	}
	
	public static java java(){
		return new java();
	}
	private static class java{
		public java can(){
			return this;
		}
		public java you(){
			return this;
		}
		public java run(){
			return this;
		}
		public java please(){return this;}
	}
	
	
	public static int drawString(Graphics2D grf, String string, int left, int top, int width, int height, Color color, int size){
		BufferedImage image = new BufferedImage(width, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics2D image_grf = (Graphics2D) image.getGraphics();
		image_grf.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		image_grf.addRenderingHints((new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)));
		image_grf.addRenderingHints((new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)));
		
		
		image_grf.setColor(color);
		int total_height = drawStringAtTopLeft(image_grf, string, width-10, 0, size);
		grf.drawImage(image, left, top, null);
		return total_height;
		
	}
	public static int drawStringAtTopLeft(Graphics2D g, String text, int width, int height, int letter_height) {
		FontMetrics fm = g.getFontMetrics();
		float lineHeight = fm.getHeight();
		float ratio = (float)(letter_height)/lineHeight;
		Font font = g.getFont();
		Font new_font = font.deriveFont(font.getStyle(), font.getSize()*ratio);
		g.setFont(new_font);
		fm = g.getFontMetrics();
		lineHeight = fm.getHeight();
		
		
		
		int x = 0;
		int y = fm.getAscent();
		
		String[] words = text.split(" ");
		
		for (String word : words) {
			int wordWidth = fm.stringWidth(word + " ");
			
			if (x + wordWidth >= width && x != 0) {
				x = 0;
				y += lineHeight;
			}
			
			g.drawString(word, x, y);
			//System.out.println("drawing word: " + word + " at " + x + ", " + y);
			x += wordWidth;
		}
		return y;
	}
	
	public static int get_string_height(String text, int width, int letter_height) {
		BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D) image.getGraphics();
		FontMetrics fm = g.getFontMetrics();
		float lineHeight = fm.getHeight();
		float ratio = (float)(letter_height)/lineHeight;
		Font font = g.getFont();
		Font new_font = font.deriveFont(font.getStyle(), font.getSize()*ratio);
		g.setFont(new_font);
		fm = g.getFontMetrics();
		lineHeight = fm.getHeight();
		
		
		
		int x = 0;
		int y = fm.getAscent();
		
		String[] words = text.split(" ");
		
		for (String word : words) {
			int wordWidth = fm.stringWidth(word + " ");
			
			if (x + wordWidth >= width && x != 0) {
				x = 0;
				y += lineHeight;
			}
			
			//System.out.println("drawing word: " + word + " at " + x + ", " + y);
			x += wordWidth;
		}
		return y;
	}
	
	
}