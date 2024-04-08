package Belt_Package.First;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Images {
    public static BufferedImage beltUpImage;
    public static BufferedImage beltDownImage;
    public static BufferedImage beltRightImage;
    public static BufferedImage beltLeftImage;

    public static BufferedImage beltDownToRightImage;
    public static BufferedImage beltLeftToDownImage;
    public static BufferedImage beltUpToLeftImage;
    public static BufferedImage beltRightToUpImage;

    public static BufferedImage beltDownToLeftImage;
    public static BufferedImage beltLeftToUpImage;
    public static BufferedImage beltUpToRightImage;
    public static BufferedImage beltRightToDownImage;
    
    public static BufferedImage iron;

    static{
        BufferedImage temp;
        temp = downloadImage("png_images/belt.png");
        
        beltUpImage = scaleImageBy2(temp);
        beltRightImage = rotateBy90(beltUpImage);
        beltDownImage = rotateBy90(beltRightImage);
        beltLeftImage = rotateBy90(beltDownImage);
        
        temp = downloadImage("png_images/beltDownToRight.png");
        beltDownToRightImage = scaleImageBy2(temp);
        beltLeftToDownImage = rotateBy90(beltDownToRightImage);
        beltUpToLeftImage = rotateBy90(beltLeftToDownImage);
        beltRightToUpImage = rotateBy90(beltUpToLeftImage);
        
        temp = downloadImage("png_images/beltDownToLeft.png");
        beltDownToLeftImage = scaleImageBy2(temp);
        beltLeftToUpImage = rotateBy90(beltDownToLeftImage);
        beltUpToRightImage = rotateBy90(beltLeftToUpImage);
        beltRightToDownImage = rotateBy90(beltUpToRightImage);
        
        iron = downloadImage("png_images/iron.png");
        iron = scaleImageBy2(iron);
    }
    public static BufferedImage downloadImage(String path){

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

    public static BufferedImage scaleImageBy2(BufferedImage image){
        final int w = image.getWidth();
        final int h = image.getHeight();
        BufferedImage scaledImage = new BufferedImage((w * 2),(h * 2), BufferedImage.TYPE_INT_ARGB);
        final AffineTransform at = AffineTransform.getScaleInstance(2.0, 2.0);
        final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        scaledImage = ato.filter(image, scaledImage);

        //final Scale scaler = new Scale(2);
        //BufferedImage scaledImage= scaler.apply(image);
        return scaledImage;
    }

    public static BufferedImage rotateBy90(BufferedImage image){
        final double rads = Math.toRadians(90);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads,0, 0);
        at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(image,rotatedImage);
        return rotatedImage;
    }
    
    public static BufferedImage setAlpha(BufferedImage image, int alpha) {
        BufferedImage newImage = new BufferedImage(
              image.getWidth(),
              image.getHeight(),
              BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = (Graphics2D) newImage.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha / 255));
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return newImage;
    }
}
