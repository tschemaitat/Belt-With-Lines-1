package Main_and_Drawing;

public class Vector2d {
	int x;
	int y;
	public Vector2d(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void rotate(int amount_rotations){
		int temp_x = rotation_matrix_x[amount_rotations][0] * x + rotation_matrix_y[amount_rotations][0] * y;
		int temp_y = rotation_matrix_x[amount_rotations][1] * x + rotation_matrix_y[amount_rotations][1] * y;
		x = temp_x;
		y = temp_y;
	}
	
	private int[][] rotation_matrix_x = {
			{1,0},
			{0,1},
			{-1,0},
			{0,-1}
	};
	private int[][] rotation_matrix_y = {
			{0,1},
			{-1,0},
			{0,-1},
			{1,0}
	};
}
