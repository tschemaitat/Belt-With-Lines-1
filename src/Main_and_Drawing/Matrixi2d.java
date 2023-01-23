package Main_and_Drawing;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Matrixi2d {
	int[][] pixels;
	
	int width;
	int height;
	public Matrixi2d(int width, int height){
		pixels = new int[width][height];
		this.width = width;
		this.height = height;
	}
	
	public int get(int x, int y){
		return pixels[x][y];
	}
	
	public void set(int x, int y, int value){
		pixels[x][y] = value;
	}
	
	public void set_all(int value){
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				pixels[x][y] = value;
			}
		}
	}
	
	public int[] getArray(){
		int[] copy = new int[width*height];
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				copy[y*width + x] = pixels[x][y];
			}
		}
		return copy;
	}
	
	public boolean inBounds(int x, int y){
		return (x > -1 && x < width && y > -1 && y < height);
	}
	
	public void mask(int[] pixels, int left, int top, int color, int array_width){
		for(int x = left, mask_x = 0; x < left + width; x++, mask_x++){
			for(int y = top, mask_y = 0; y < top + height; y++, mask_y++){
				if(get(mask_x, mask_y) > 0)
					pixels[y*array_width + x] = color;//black;
			}
		}
	}
	
	public BufferedImage mask_image(BufferedImage input){
		//BufferedImage result = new BufferedImage(width + edge*2, height + edge*2, BufferedImage.TYPE_INT_ARGB);
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		//for(int x = edge, mask_x = 0; x < width; x++, mask_x++){
			//for(int y = edge, mask_y = 0; y < height; y++, mask_y++){
		for(int x = 0, mask_x = 0; x < width; x++, mask_x++){
			for(int y = 0, mask_y = 0; y < height; y++, mask_y++){
				if(get(mask_x, mask_y) != 0){
					int mask_alpha = get(mask_x, mask_y);
					int color = input.getRGB(x, y);
					int red = (0xFF & ( color >> 16));
					int alpha = (0xFF & (color >> 24));
					int blue = (0xFF & (color >> 0 ));
					int green = (0xFF & (color >> 8 ));
					//System.out.println("mask value: "+get(mask_x, mask_y));
					//System.out.println("initial alpha: " + alpha);
					alpha = (int)(alpha * mask_alpha / 255.0);
					int sum = ((alpha & 0xff) << 24 | (red & 0xff) << 16 | (blue & 0xff) << 8 | (green & 0xff));
					//System.out.println("alpha: " + alpha);
					//System.out.println("mask alpha: " + mask_alpha);
					result.setRGB(x, y, sum);
					//System.out.println("setting result: "+sum);
				}
			}
		}
		return result;
	}
	
	public Matrixi2d add_shadow(int radius){
		//int radius = 5;
		Matrixi2d result = new Matrixi2d(width, height);
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(get(x, y) == 0){
					result.set(x, y, average_diamond(x, y, radius));
					//continue;
				}
			}
		}
		return result;
	}
	
	public Matrixi2d copy_and_add_edges(int width_diff, int height_diff){
		Matrixi2d result = new Matrixi2d(width + width_diff*2, height+height_diff*2);
		for(int y = height_diff; y < width + height_diff; y++){
			for(int x = width_diff; x < height + width_diff; x++){
				result.set(x, y, get(x - width_diff, y - height_diff));
			}
		}
		return result;
	}
	
	
	
	private int average_diamond(int x_center, int y_center, int radius){
		//0, 1, 0
		//1, 5, 4
		//2, 13, 12
		//3, 25, 24
		int num_pixels = 2*radius*radius + 2*radius + 1;
		int sum = 0;
		int alpha = 0;
		for(int y = -1*radius + y_center; y <= radius + y_center; y++){
			for(int x = -1*radius + x_center; x <= radius + x_center; x++){
				if(Math.abs(x - x_center) + Math.abs(y - y_center) > radius || x < 0 || y < 0 || x >= width || y >= height){
					continue;
				}
				alpha += get(x, y) / num_pixels;
			}
		}
		sum = alpha;
		return sum;
	}
	
	public Matrixi2d smooth_out_and_add_edge(int edge){
		int radius = 5;
		Matrixi2d result = new Matrixi2d(width + edge * 2, height + edge*2);
		for(int y = 0, mask_y = -1*edge; y < height + edge * 2; y++, mask_y++){
			for(int x = 0, mask_x = -1*edge; x < width + edge * 2; x++, mask_x++){
				if(!inBounds(mask_x, mask_y)){
					result.set(x, y, average_circle_5(mask_x, mask_y));
					continue;
				}
				if(get(mask_x, mask_y) == 0){
					result.set(x, y, average_circle_5(mask_x, mask_y));
					continue;
				}
				result.set(x, y, get(mask_x, mask_y));
				
				
			}
		}
		return result;
	}
	
	private int average_circle_5(int x_center, int y_center){
		int radius = 5;
		int num_pixels = 2*(radius*radius + radius) + 1;
		//System.out.println("num pixels: " + num_pixels);
		double divider = (Math.pow(num_pixels, 0.8));
		double normalizer = 0.4;
		//System.out.println("power: "+num_pixels);
		int sum = 0;
		for(int y = -1*radius; y <= radius; y++){
			for(int x = -1*radius; x <= radius; x++){
				double distance = (Math.pow(1 + x*x + y*y, 0.5));
				if(distance > radius)
					continue;
				if(inBounds(x + x_center, y + y_center)){
					sum += (int)(get(x + x_center, y + y_center) / (distance * divider * normalizer));
				}
				//System.out.println("sum: "+sum +" distance: "+distance+" pixels: "+num_pixels);
			}
		}
		if(sum > 255)
			return 255;
		return sum;
	}
}
