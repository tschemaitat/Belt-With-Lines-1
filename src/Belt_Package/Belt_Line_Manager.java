package Belt_Package;

import Main_and_Drawing.Twod;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Belt_Line_Manager implements Belt_Iterator_Manager {
	ArrayList<Belt_List> belt_lists;
	ArrayList<Belt_List> lines_with_no_output;
	//BeltGrid beltGrid;
	public Belt_Line_Manager(BeltGrid beltGrid) {
		lines_with_no_output = new ArrayList<>();
		//this.beltGrid = beltGrid;
		build(beltGrid);
	}
	
	@Override
	public void iterate() {
		for(int i = 0; i < belt_lists.size(); i++){
			//System.out.println("calling iterating from manager");
			belt_lists.get(i).has_iterated = false;
		}
		for(int i = 0; i < belt_lists.size(); i++){
			//System.out.println("calling iterating from manager");
			belt_lists.get(i).iterate_items(belt_lists.get(i).self_index);
		}
	}
	
	public void build(BeltGrid beltGrid){
		belt_lists = new ArrayList<>();
		//belt_lists.add(Belt_List_Factory.construct_belt_list(belts.get(2), 0));
		
		for(int i = 0; i < beltGrid.belts.size(); i++){
			if(beltGrid.belts.get(i).get_list(0) == null){
				//System.out.println("making list: (" + i +", 0)~~~~~~~~~~~");
				Belt_List belt_list = Belt_List_Factory.construct_belt_list(beltGrid.belts.get(i), 0, belt_lists);
				belt_lists.add(belt_list);
			}
			
		}
		for(int i = 0; i < beltGrid.belts.size(); i++){
			if(beltGrid.belts.get(i).get_list(1) == null){
				//System.out.println("making list: (" + i +", 1)~~~~~~~~~~~");
				Belt_List belt_list = Belt_List_Factory.construct_belt_list(beltGrid.belts.get(i), 1, belt_lists);
				belt_lists.add(belt_list);
			}
			
		}
		for(int i = 0; i < belt_lists.size(); i++)
			belt_lists.get(i).compile();
		for(int i = 0; i < belt_lists.size(); i++)
			belt_lists.get(i).second_compile();
	}
	
	public void draw_debug(int x, int y, int width, int height, Graphics2D grf){
		int line_total_height = 0;
		for(int i = 0; i < belt_lists.size(); i++){
			String[] debug_print = new String[2];
			
			
			Belt_List list = belt_lists.get(i);
			debug_print[0] = "";
			debug_print[0] += list.self_index + ": (mode: "+list.iteration_mode+ "), "+ list.belt_index_and_side()+", output: "+list.output_locationStruct;
			debug_print[1] = list.item_characters();
			int font_size = 15;
			for(int j = 0; j < debug_print.length; j++){
				//BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
				//Graphics2D image_grf = (Graphics2D) image.getGraphics();
				//image_grf.setColor(Color.black);
				int printed_height = Entity_Manager.drawString(grf, debug_print[j], x+5, y + line_total_height, width, height, Color.black, font_size);
				//grf.drawImage(image, getX(), getY() + line_total_height, null);
				line_total_height += printed_height + 4;
			}
			line_total_height += 6;
		}
	}
	
	@Override
	public void build_from_belts(List<Belt> belts, BeltGrid beltGrid) {
		List<Belt_List> new_lines = new ArrayList<>();
		//System.out.println("rebuilding lists");
		for(int i = 0; i < belts.size(); i++){
			//System.out.println("checking belt: " + belts.get(i).arrayIndex + " index: " + i);
			if(belts.get(i).get_list(0) == null){
				System.out.println("building list from belt: " + belts.get(i).arrayIndex + " index: " + i + "side: " + 0);
				Belt_List line = Belt_List_Factory.construct_belt_list(belts.get(i), 0, belt_lists);
				new_lines.add(line);
				belt_lists.add(line);
			}
			
			if(belts.get(i).get_list(1) == null){
				System.out.println("building list from belt: " + belts.get(i).arrayIndex + " index: " + i + "side: " + 1);
				Belt_List line = Belt_List_Factory.construct_belt_list(belts.get(i), 1, belt_lists);
				new_lines.add(line);
				belt_lists.add(line);
			}
		}
		//System.out.println("compiling lists");
		for(int i = 0; i < new_lines.size(); i++){
			new_lines.get(i).compile();
		}
		for(int i = 0; i < belt_lists.size(); i++){
			belt_lists.get(i).second_compile();
		}
		for(int i = 0; i < lines_with_no_output.size(); i++){
			Belt_List line = lines_with_no_output.get(i);
			LocationStruct location = line.output_locationStruct;
			Belt_List new_output = location.belt.get_list(location.side);
		}
	}
	
	public List<List<ItemLocationStruct>> delete_belt_iterators(List<Belt> belts){
		List<List<ItemLocationStruct>> itemToBeReplaced = new ArrayList<>();
		List<Belt_List> lines_to_delete = new ArrayList<>();
		for(int i = 0; i < belts.size(); i++){
			//System.out.print("(i: "+i+", " + belts.get(i) + ") ");
		}
		//System.out.println();
		for(int i = 0; i < belts.size(); i++){
			Belt_List line = belts.get(i).get_list(0);
			if(!lines_to_delete.contains(line) && line != null)
				lines_to_delete.add(line);
			line = belts.get(i).get_list(1);
			if(!lines_to_delete.contains(line) && line != null)
				lines_to_delete.add(line);
		}
		System.out.println("deleting lines: ");
		for(int i = lines_to_delete.size() - 1; i >= 0; i--){
			System.out.print(lines_to_delete.get(i).self_index + ", ");
			itemToBeReplaced.add(remove_list_from_manager(lines_to_delete.get(i)));
			belt_lists.remove(lines_to_delete.get(i));
		}
		System.out.println();
		return itemToBeReplaced;
	}
	public List<ItemLocationStruct> remove_list_from_manager(Belt_List line){
		ArrayList<Belt_List> inputs = line.input_lists;
		for(int i = 0; i < inputs.size(); i++)
			lines_with_no_output.add(inputs.get(i));
		if(lines_with_no_output.contains(line))
			lines_with_no_output.remove(line);
		belt_lists.remove(line);
		return line.delete();
	}
	public void print_iterators(){
		for(int i = 0; i < belt_lists.size(); i++){
			System.out.println("list: " + belt_lists.get(i).self_index);
			Belt_List list = belt_lists.get(i);
			for(int pos = 0; pos < list.belts_from_position.size(); pos++){
				System.out.print("[belt: "+list.belts_from_position.get(pos) + ", side: " + list.side_from_position.get(pos)+"] ");
				
			}
			System.out.println();
			//print_cords(list);
			//System.out.println("\n");
		}
	}
	public void print_cords(Belt_List list){
		int[][][] list_cords = list.cords;
		for(int belt_cord = 0; belt_cord < list_cords.length; belt_cord++){
			System.out.print("\nbelt: ");
			int[][] belt_cord_temp = list_cords[belt_cord];
			for(int pos_cord = 0; pos_cord < belt_cord_temp.length; pos_cord++){
				int[] cord_temp = belt_cord_temp[pos_cord];
				System.out.print("<"+cord_temp[0]+","+cord_temp[1]+">");
			}
		}
	}
	
	public void draw_items_in_list(Graphics2D grf, int graphical_iteration){
		for(int i = 0; i < belt_lists.size(); i++){
			belt_lists.get(i).draw_items(grf, graphical_iteration);
		}
	}
	public void draw_belt_lines(Graphics2D grf){
		//System.out.println("manager drawing lines");
		Color[] colors = {Color.BLUE, Color.CYAN, Color.RED, Color.GREEN, Color.yellow, Color.BLACK, Color.ORANGE};
		for(int i = 0; i < belt_lists.size(); i++){
			belt_lists.get(i).draw_line(grf, colors[i% colors.length]);
		}
		for(int i = 0; i < belt_lists.size(); i++){
			//System.out.println("belt_list size: " + belt_lists.get(i).size());
			String s = belt_lists.get(i).stack_belt_side_and_position(true);
			//System.out.println(s);
			
			//System.out.println(belt_lists.get(i).belt_index_and_side());
		}
		//System.out.println("manager done drawing lines");
	}
	public void print_state(){
		System.out.println("lines: "+belt_lists.size()+" ~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < belt_lists.size(); i++){
			System.out.println(""+belt_lists.get(i));
		}
	}
}
