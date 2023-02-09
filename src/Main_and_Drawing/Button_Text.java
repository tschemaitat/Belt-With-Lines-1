package Main_and_Drawing;

import Main_and_Drawing.Layouts.LayoutParameters;
import Main_and_Drawing.Layouts.RectP;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Button_Text extends Image_Twod {
	Image_Twod rect_twod;
	Image_Twod text_twod;
	Layout parent;
	String text_string;
	
	
	
	public Button_Text(String text_string, LayoutParameters parameters, Layout parent, Command onClick) {
		super(null, parameters, parent);
		this.text_string = text_string;
		this.onClick = onClick;
		setup();
	}
	
	private void setup(){
		int text_height = (int)(height * 0.8);
		BufferedImage text = BufferedImageFactory.text(text_string, text_height);
		width = text.getWidth() + height/4;
		BufferedImage hue = Drawable.downloadImage("png_images/pink_hue.PNG");
		BufferedImage hue_soft_square = BufferedImageFactory.rounded_square(width, height, hue);
		
		rect_twod = new Image_Twod(new Drawable(hue_soft_square), layoutParameters, null);
		text_twod = new Image_Twod(new Drawable(text), new RectP(x + width/2 - text.getWidth()/2, y + height / 2 - text.getHeight()/2, text.getWidth(), text.getHeight()), null);
		layoutParameters.setWidth(width);
		
	}
	
	public void draw(Graphics grf){
		//grf.drawRect(x, y, width, height);
		if(visibility == VISIBLE){
			if(rect_twod != null)
				rect_twod.draw((Graphics2D) grf);
			if(text_twod != null){
				text_twod.draw((Graphics2D) grf);
				//System.out.println("drawing text");
			}
			
			
			
		}
			//drawable.draw(grf);
			
	}
	
	
}
