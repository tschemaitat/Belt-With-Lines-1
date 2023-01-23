package Main_and_Drawing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Soft_Square {
	Color black = Color.BLACK;
	int color_black = black.getRGB();
	
	public BufferedImage get_round_square(int width, int height, int color){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = new int[width*height];
		get_round_square_edges(pixels, width - 4, height - 4, 2, 2, width);
		//smooth_out_square(pixels, width);
		setPixelToImage(image, pixels, width);
		
		return image;
	}
	
	private void smooth_out_square(int[] pixels, int array_width){
		int[] result = new int[pixels.length];
		for(int y = 1; y < pixels.length/array_width - 1; y++){
			for(int x = 1; x < array_width - 1; x++){
				result[y*array_width + x] = average_diamond(pixels, array_width, x, y);
			}
		}
		for(int y = 0; y < pixels.length/array_width; y++){
			for(int x = 0; x < array_width; x++){
				pixels[y*array_width + x] = result[y*array_width + x];
			}
		}
	}
	
	private static int average_diamond(int[] pixels, int array_width, int x_center, int y_center){
		int num_pixels = 5;
		int sum = 0;
		int alpha = 0;
		int red = 0;
		int blue = 0;
		int green = 0;
		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				if(x != 0 && y != 0)
					continue;
				int color = pixels[(y_center + y)*array_width + (x_center + x)];
				red += (0xFF & ( color >> 16)) / num_pixels;
				alpha += (0xFF & (color >> 24)) / num_pixels;
				blue += (0xFF & (color >> 0 )) / num_pixels;
				green += (0xFF & (color >> 8 )) / num_pixels;
			}
		}
		sum = ((alpha & 0xff) << 24 | (red & 0xff) << 16 | (blue & 0xff) << 8 | (green & 0xff));
		return sum;
	}
	
	private static int average_square(int[] pixels, int array_width, int x_center, int y_center){
		int num_pixels = 9;
		int sum = 0;
		int alpha = 0;
		int red = 0;
		int blue = 0;
		int green = 0;
		for(int y = -1; y <= 1; y++){
			for(int x = -1; x <= 1; x++){
				int color = pixels[(y_center + y)*array_width + (x_center + x)];
				red += (0xFF & ( color >> 16)) / num_pixels;
				alpha += (0xFF & (color >> 24)) / num_pixels;
				blue += (0xFF & (color >> 0 )) / num_pixels;
				green += (0xFF & (color >> 8 )) / num_pixels;
			}
		}
		sum = ((alpha & 0xff) << 24 | (red & 0xff) << 16 | (blue & 0xff) << 8 | (green & 0xff));
		return sum;
	}
	
	private void setPixelToImage(BufferedImage image, int[] pixels, int array_width){
		for(int y = 0; y < pixels.length/array_width; y++){
			for(int x = 0; x < array_width; x++){
				image.setRGB(x, y, pixels[y*array_width + x]);
			}
		}
	}
	
	private void get_round_square_edges(int[] pixels, int width, int height, int left, int top, int array_width){
		//add_corners(pixels, width, height, width / 2, center_x, center_y, array_width);
		int radius = width / 4;
		int corner = (int)(radius / 1.4);
		Matrixi2d matrix = square_round_corners_mask(width, height, radius);
		matrix.mask(pixels, left, top, color_black, array_width);
		Matrixi2d square = get_square_mask(width - corner, height - corner);
		square.mask(pixels, left + corner/2, top + corner/2, color_black, array_width);
		
	}
	
	public Matrixi2d get_filled_in_mask(int width, int height){
		int radius = Math.min(width, height) / 4;
		int corner = (int)(radius / 1.4);
		Matrixi2d matrix = square_round_corners_mask(width, height, radius);
		Matrixi2d square = get_square_mask(width - corner, height - corner);
		int offset = corner/2;
		for(int y = 0; y < square.height; y++){
			for(int x = 0; x < square.width; x++){
				if(square.get(x, y) != 0){
					matrix.set(x + corner/2, y + corner/2, square.get(x, y));
				}
			}
		}
		return matrix;
	}
	
	private Matrixi2d get_square_mask(int width, int height){
		Matrixi2d matrix = new Matrixi2d(width, height);
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				matrix.set(x, y, 255);
			}
		}
		return matrix;
	}
	
	
	
	private void add_corners(int[] pixels, int width,int height, int radius, int center_x, int center_y, int array_width){
		
		
		//System.out.println("adding corners");
		int thickness = 20;
		//Vector<Integer> vector = new Vector<Integer>(2);
		Vector2d square_size = new Vector2d(width - radius, height - radius);
		Vector2d one_vector = new Vector2d(1, 1);
		
		for(int rotation = 0; rotation < 4; rotation++){
			one_vector.rotate(1);
			square_size.rotate(1);
			//System.out.println("rotation: " + one_vector.x +", "+one_vector.y);
			int corner_center_x = center_x + (width - radius) * one_vector.x;
			int corner_center_y = center_y + (height - radius) * one_vector.y;
			int y_start = corner_center_y, y_end = corner_center_y;
			int x_start = corner_center_x, x_end = corner_center_x;
			if(one_vector.x == -1)
				x_start -= radius;
			else
				x_end += radius;
			if(one_vector.y == -1)
				y_start -= radius;
			else
				y_end += radius;
			//System.out.println("****************");
			//System.out.println(x_start);
			//System.out.println(x_end);
			//System.out.println(y_start);
			//System.out.println(y_end);
			
			
			for(int y = y_start; y <= y_end; y++){
				for(int x = x_start; x <= x_end; x++){
					double value = Math.pow(x - corner_center_x, 2) + Math.pow(y - corner_center_y, 2);
					
					if( value > Math.pow(radius - thickness/2 - 0.05, 2) && value < Math.pow(radius + 0.05, 2)){
						pixels[y * array_width + x] = color_black;
						//System.out.print("1 ");
					}else{
						//System.out.print("0 ");
					}
				}
				//System.out.println();
			}
		}
		int temp_center_y = center_y;
		int temp_center_x = center_x;
		int temp_offset_y = height;
		int temp_offset_x = width;
		
		for(int y = temp_center_y + temp_offset_y - thickness / 2; y < temp_center_y + temp_offset_y; y++){
			for(int x = temp_center_x - temp_offset_x + radius; x < temp_center_x + temp_offset_x - radius; x++){
				pixels[y * array_width + x] = color_black;
			}
		}
		for(int y = temp_center_y - temp_offset_y; y < temp_center_y - temp_offset_y + thickness / 2; y++){
			for(int x = temp_center_x - temp_offset_x + radius; x < temp_center_x + temp_offset_x - radius; x++){
				pixels[y * array_width + x] = color_black;
			}
		}
		temp_center_y = center_x;
		temp_center_x = center_y;
		temp_offset_y = width;
		temp_offset_x = height;
		for(int y = temp_center_y + temp_offset_y - thickness / 2; y < temp_center_y + temp_offset_y; y++){
			for(int x = temp_center_x - temp_offset_x + radius; x < temp_center_x + temp_offset_x - radius; x++){
				pixels[x * array_width + y] = color_black;
			}
		}
		for(int y = temp_center_y - temp_offset_y; y < temp_center_y - temp_offset_y + thickness / 2; y++){
			for(int x = temp_center_x - temp_offset_x + radius; x < temp_center_x + temp_offset_x - radius; x++){
				pixels[x * array_width + y] = color_black;
			}
		}
	}
	
	private Matrixi2d square_round_corners_mask(int width,int height, int radius){
		int alpha = 255;
		int center_x = width/2;
		int center_y = height/2;
		//System.out.println("adding corners");
		//System.out.println("width:  "+width+"height"+height+"radius "+radius);
		Matrixi2d matrix = new Matrixi2d(width, height);
		matrix.set_all(0);
		int thickness = Math.min(width, height) / 4;
		//Vector<Integer> vector = new Vector<Integer>(2);
		Vector2d square_size = new Vector2d(width - radius, height - radius);
		Vector2d one_vector = new Vector2d(1, 1);
		
		for(int rotation = 0; rotation < 4; rotation++){
			one_vector.rotate(1);
			square_size.rotate(1);
			//System.out.println("rotation: " + one_vector.x +", "+one_vector.y);
			int corner_center_x = center_x + (width/2 - radius) * one_vector.x;
			int corner_center_y = center_y + (height/2 - radius) * one_vector.y;
			int y_start = corner_center_y, y_end = corner_center_y;
			int x_start = corner_center_x, x_end = corner_center_x;
			if(one_vector.x == -1)
				x_start -= radius;
			else
				x_end += radius;
			if(one_vector.y == -1)
				y_start -= radius;
			else
				y_end += radius;
			//System.out.println("****************");
			//System.out.println(x_start);
			//System.out.println(x_end);
			//System.out.println(y_start);
			//System.out.println(y_end);
			
			//System.out.println();
			for(int y = y_start; y < y_end; y++){
				//System.out.println();
				for(int x = x_start; x < x_end; x++){
					
					double value = Math.sqrt(Math.pow(x - corner_center_x, 2) + Math.pow(y - corner_center_y, 2));
					if(x > width || y > width){
						//System.out.println(x_start);
						//System.out.println(x_end);
						//System.out.println(y_start);
						//System.out.println(y_end);
					}
					/*
					if( value < radius - 0.05) {//value > radius - thickness/2 - 0.05 &&
						matrix.set(x, y, alpha);
						System.out.print("# ");
					}
					else{
						System.out.print("  ");
					}
					 */
					/*
					if(value - (radius - 0.05) < 2.0){
						int new_alpha = (int)((alpha) / (value - (radius - 0.05) + 1));
						matrix.set(x, y, new_alpha);
					}
					 */
					
				}
				//System.out.println();
			}
		}
		int temp_center_y = center_y;
		int temp_center_x = center_x;
		int temp_offset_y = height/2;
		int temp_offset_x = width/2;
		
		for(int y = temp_center_y + temp_offset_y - thickness / 2; y < temp_center_y + temp_offset_y; y++){
			for(int x = temp_center_x - temp_offset_x + radius; x < temp_center_x + temp_offset_x - radius; x++){
				matrix.set(x, y, alpha);
			}
		}
		for(int y = temp_center_y - temp_offset_y + 1; y < temp_center_y - temp_offset_y + thickness / 2 + 1; y++){
			for(int x = temp_center_x - temp_offset_x + radius; x < temp_center_x + temp_offset_x - radius; x++){
				matrix.set(x, y, alpha);
			}
		}
		temp_center_y = center_x;
		temp_center_x = center_y;
		temp_offset_y = width/2;
		temp_offset_x = height/2;
		for(int y = temp_center_y + temp_offset_y - thickness / 2; y < temp_center_y + temp_offset_y; y++){
			for(int x = temp_center_x - temp_offset_x + radius; x < temp_center_x + temp_offset_x - radius; x++){
				matrix.set(y, x, alpha);
			}
		}
		for(int y = temp_center_y - temp_offset_y + 1; y < temp_center_y - temp_offset_y + thickness / 2 + 1; y++){
			for(int x = temp_center_x - temp_offset_x + radius; x < temp_center_x + temp_offset_x - radius; x++){
				matrix.set(y, x, alpha);
			}
		}
		return matrix;
	}
}
