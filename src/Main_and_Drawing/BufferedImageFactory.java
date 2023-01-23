package Main_and_Drawing;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BufferedImageFactory {
	
	public static BufferedImage text_multiline(String text, int size, int max_width){
		Font font = new Font("TimesRoman", Font.BOLD, (int)(size / 1.12));
		String delimiter = " ";
		String[] words = text.split(delimiter);
		//for(int i = 0; i < words.length; i++)
			//System.out.println(words[i] + ", ");
		List<List<String>> lines_as_list = new ArrayList<>();
		List<String> line;
		int index = 0;
		int current_width = 0;
		int width_delimiter = getWidthOfText(font, delimiter);
		while(index < words.length){
			line = new ArrayList<>();
			while(1==1){
				if(index >= words.length)
					break;
				int add_width = getWidthOfText(font, words[index]);
				//System.out.println(add_width);
				if(current_width + add_width > max_width && current_width != 0)
					break;
				current_width += add_width + width_delimiter;
				line.add(words[index]);
				index++;
			}
			current_width = 0;
			lines_as_list.add(line);
		}
		//System.out.println(lines_as_list);
		List<String> lines_as_string = new ArrayList<>();
		for(int i = 0; i < lines_as_list.size(); i++)
			lines_as_string.add(list_strings_to_string(lines_as_list.get(i), delimiter));
		
		int width = 0;
		for(int i = 0; i < lines_as_string.size(); i++){
			int temp = getWidthOfText(font, lines_as_string.get(i));
			if(temp > width)
				width = temp;
			//System.out.println("width of line: "+temp);
		}
		if(width == 0)
			return null;
		//System.out.println("width: "+width);
		int height = size * lines_as_string.size();
		//System.out.println("height: "+height);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D grf = image.createGraphics();
		Color c=new Color(1f,0.0f,0.0f, 1f );
		
		grf.setFont(font);
		grf.setColor(c);
		grf.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		for(int i = 0; i < lines_as_string.size(); i++){
			int y = (int)((size / 1.12) * (4.4/5.0) + size * i);
			grf.drawString(lines_as_string.get(i), 0, y);
			//System.out.println("drawing y: "+y);
		}
		grf.dispose();
		return image;
	}
	
	private static String list_strings_to_string(List<String> list, String delimiter){
		String result = "";
		for(int i = 0; i < list.size(); i++){
			if(i > 0)
				result += delimiter;
			result += list.get(i);
		}
		return result;
	}
	
	public static BufferedImage text(String text, int size){
		Font font = new Font("TimesRoman", Font.BOLD, (int)(size / 1.12));
		int width = getWidthOfText(font, text);
		int height = size;//(int)(1.6 * (35.0/50.0) * size);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D grf = image.createGraphics();
		Color c=new Color(1f,0.0f,0.0f, 1f );
		
		grf.setFont(font);
		grf.setColor(c);
		grf.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		grf.drawString(text, 0, (int)((size / 1.12) * (4.4/5.0) ));
		
		return image;
	}
	
	public static int getWidthOfText(Font font, String text){
		BufferedImage image = new BufferedImage(100, 100,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D grf = image.createGraphics();
		grf.setFont(font);
		return grf.getFontMetrics().stringWidth(text);
	}
	
	public static void testing_text(Graphics2D grf, Font font, String text){
		System.out.println("testing text");
		FontRenderContext context = grf.getFontRenderContext();
		LineMetrics lineMetrics = font.getLineMetrics(text, context);
		GlyphVector glyphVector = font.createGlyphVector(context, text);
		glyphVector.getNumGlyphs();
		List<GlyphMetrics> list = new ArrayList<>();
		for(int i = 0; i < glyphVector.getNumGlyphs(); i++){
			list.add(glyphVector.getGlyphMetrics(i));
		}
		int width = 0;
		for(int i = 0; i < list.size(); i++){
			System.out.println(list.get(i).getBounds2D());
			System.out.println(list.get(i).getAdvanceX());
			width += list.get(i).getBounds2D().getWidth();
		}
		System.out.println(width);
		GlyphMetrics glyphMetrics = glyphVector.getGlyphMetrics(glyphVector.getNumGlyphs() - 1);
		
		System.out.println(glyphMetrics.getBounds2D().toString());
		System.out.println(glyphVector.getNumGlyphs());
		System.out.println();
		System.out.println();
	}
	
	public static BufferedImage rounded_square(int width, int height, BufferedImage image){
		Matrixi2d square_mask = new Soft_Square().get_filled_in_mask(width, height);
		BufferedImage hue_soft_square = square_mask.mask_image(image);
		return hue_soft_square;
	}
	
	public static BufferedImage fill(int width, int height, Color color){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics grf = image.getGraphics();
		grf.setColor(color);
		grf.fillRect(0, 0, width, height);
		return image;
	}
	
	
	
	
	
}
