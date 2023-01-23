package Main_and_Drawing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.io.*;

public class Drawable {
	public static final Color default_color = Color.black;
	static boolean setup2 = true;
	
	static boolean setup = false;
	
	public static final int mode_rectangleFill = 1000;
	public static final int mode_picture = 1001;
	BufferedImage image;
	Rectangle rect;
	boolean fill;
	
	private int mode;
	public Color color = default_color;
	
	public Drawable(String path) {
		image = downloadImage(path);
		mode = mode_picture;
	}
	
	public Drawable(int mode){
		this.mode = mode;
	}
	
	public Drawable(BufferedImage image){
		mode = mode_picture;
		this.image = image;
	}
	
	private boolean scaleOffset = false;
	private double scaleOffset_x = 1.0;
	private double scaleOffset_y = 1.0;
	public void scaleOffset(double x, double y){
		scaleOffset = true;
		scaleOffset_x = x;
		scaleOffset_y = y;
	}
	private boolean locationOffset = false;
	private int locationOffset_x = 0;
	private int locationOffset_y = 0;
	public void locationOffset(int x, int y){
		locationOffset = true;
		locationOffset_x = x;
		locationOffset_y = y;
	}
	double opacity = 1;
	public void set_opacity(double set_opacity){
		opacity = set_opacity;
	}
	
	boolean scalingEnabled = false;
	public void setScalingEnabled(boolean bool){
		scalingEnabled = bool;
	}
	
	public void draw(Graphics grf, Image_Twod parent) {
		
		grf.setColor(color);
		BufferedImage edited_image = null;
		switch(mode){
			case mode_picture -> {
				edited_image = image;
			}
			case mode_rectangleFill -> {
				edited_image = create_rectangle_image(grf.getColor(), parent);
			}
		}
		if(scalingEnabled)
			edited_image = scaleImage(edited_image, (int)(parent.width * scaleOffset_x), (int)(parent.height * scaleOffset_y));
		if(edited_image == null){
			System.out.println("no mode found while drawing.");
		}
		//edited_image = darken(edited_image, 0.9f);
		modAlpha(edited_image, opacity);
		grf.drawImage(edited_image, parent.x + locationOffset_x, parent.y + locationOffset_y, null);
	}
	
	private BufferedImage create_rectangle_image(Color color, Image_Twod parent){
		BufferedImage rectangle = new BufferedImage(parent.width, parent.height, BufferedImage.TYPE_INT_ARGB);
		Graphics grf = rectangle.getGraphics();
		//grf.setColor(Color.white);
		//grf.fillRect(-1,-1,parent.width*2, parent.height*2);
		grf.setColor(color);
		grf.fillRect(-1, -1, parent.width + 10, parent.height + 10);
		return rectangle;
	}
	
	private BufferedImage darken(BufferedImage bufferedImage, float percent){
		RescaleOp op = new RescaleOp(percent, 0, null);
		BufferedImage new_image = op.filter(bufferedImage, null);
		return new_image;
	}
	
	public void set_color(Color color){
		this.color = color;
	}
	
	public BufferedImage getImage() {
		return image;
		
	}
	
	public static BufferedImage downloadImage(String path) {
		
		File ImageFile = new File(path);
		BufferedImage image;
		try {
			image = ImageIO.read(ImageFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return image;
	}
	
	private BufferedImage scaleImageBy2(BufferedImage image) {
		final int w = image.getWidth();
		final int h = image.getHeight();
		BufferedImage scaledImage = new BufferedImage((w * 2), (h * 2), BufferedImage.TYPE_INT_ARGB);
		final AffineTransform at = AffineTransform.getScaleInstance(2.0, 2.0);
		final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		scaledImage = ato.filter(image, scaledImage);
		
		//final Scale scaler = new Scale(2);
		//BufferedImage scaledImage= scaler.apply(image);
		return scaledImage;
	}
	
	private BufferedImage scaleImage(BufferedImage image, int new_width, int new_height) {
		int w2 = new_width, h2 = new_height;
		final int w = image.getWidth();
		final int h = image.getHeight();
		
		double scale;
		double scale_w = (new_width * 1.0) / w;
		double scale_h = (new_height * 1.0) / h;
		if (scale_w < scale_h)
			scale = scale_w;
		else
			scale = scale_h;
		

		BufferedImage scaledImage = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_ARGB);
		final AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
		final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		scaledImage = ato.filter(image, scaledImage);
		
		//final Scale scaler = new Scale(2);
		//BufferedImage scaledImage= scaler.apply(image);
		
		return scaledImage;
	}
	
	private BufferedImage rotateBy90(BufferedImage image) {
		final double rads = Math.toRadians(90);
		final double sin = Math.abs(Math.sin(rads));
		final double cos = Math.abs(Math.cos(rads));
		final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
		final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
		final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
		final AffineTransform at = new AffineTransform();
		at.translate(w / 2, h / 2);
		at.rotate(rads, 0, 0);
		at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
		final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		rotateOp.filter(image, rotatedImage);
		return rotatedImage;
	}
	
	public static void modAlpha(BufferedImage modMe, double modAmount) {
		//
		for (int x = 0; x < modMe.getWidth(); x++) {
			for (int y = 0; y < modMe.getHeight(); y++) {
				//
				int argb = modMe.getRGB(x, y); //always returns TYPE_INT_ARGB
				int alpha = (argb >> 24) & 0xff;  //isolate alpha
				
				alpha *= modAmount; //similar distortion to tape saturation (has scrunching effect, eliminates clipping)
				alpha &= 0xff;      //keeps alpha in 0-255 range
				
				argb &= 0x00ffffff; //remove old alpha info
				argb |= (alpha << 24);  //add new alpha info
				modMe.setRGB(x, y, argb);
			}
		}
	}
	
	/*
	
	float alpha = 1 * 0.7f;
		AlphaComposite alcom = AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha);
		//grf.setComposite(alcom);
	 */
}


