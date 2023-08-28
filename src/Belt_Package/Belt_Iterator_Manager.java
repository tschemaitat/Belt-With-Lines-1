package Belt_Package;

import Main_and_Drawing.Twod;

import java.awt.*;
import java.util.List;

public interface Belt_Iterator_Manager {
	public void iterate();
	public void draw_debug(int x, int y, int width, int height, Graphics2D grf);
	public void build_from_belts(List<Belt> belts, BeltGrid beltGrid);
	public List<List<ItemLocationStruct>> delete_belt_iterators(List<Belt> belts);
	public void print_iterators();
	public void draw_items_in_list(Graphics2D grf, int graphical_iteration);
	public void draw_belt_lines(Graphics2D grf);
	public void print_state();
}
