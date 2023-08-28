package Belt_Package;

import java.awt.image.BufferedImage;

public interface Placeable {
	public Placeable[] get_affected_around();
	public int[] space_taken();
	public BufferedImage get_image();
	public int get_row();
	public int get_column();
	public Placeable[] reconfigure_and_return_affected();
}
