package New_Belt_Package;

import java.awt.image.BufferedImage;

public interface Placeable {
	public int[][] get_affected_around();
	public int[] space_taken();
	public BufferedImage get_image();
	public Belt[] get_belts();
	public int get_row();
	public int get_column();
}
