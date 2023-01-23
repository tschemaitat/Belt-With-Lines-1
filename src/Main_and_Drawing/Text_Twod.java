package Main_and_Drawing;

import Main_and_Drawing.Layouts.LayoutParameters;

import java.awt.image.BufferedImage;

public class Text_Twod extends Image_Twod {
	public Text_Twod(String text_string, LayoutParameters parameters, Layout parent){
		super(null, parameters, parent);
		if(text_string != null)
			set_text(text_string);
	}
	
	public void set_text(String text_string){
		BufferedImage text_image = BufferedImageFactory.text_multiline(text_string, height, width);
		if(text_image == null){
			set_drawable(null);
			return;
		}
		set_drawable(new Drawable(text_image));
		get_drawable().setScalingEnabled(false);
	}
	
	
}
