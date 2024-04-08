package Belt_Generater;
import java.awt.image.BufferedImage;

import static Belt_Package.First.Enum.*;
import static Belt_Package.First.Images.*;
public class Belt_Drawable_Factory {
    public static Drawable drawable(int shape, int direction){
        return new Drawable(image(shape, direction));
    }

    public static BufferedImage image(int shape, int direction){
        switch(shape){
            case curveToLeft: return curveLeft(direction);
            case curveToRight: return curveRight(direction);
            case straight: return straight(direction);
        }
        throw new RuntimeException();
    }

    public static BufferedImage curveLeft(int direction){
        switch(direction){
            case 0: return beltLeftToUpImage;
            case 1: return beltUpToRightImage;
            case 2: return beltRightToDownImage;
            case 3: return beltDownToLeftImage;
        }
        throw new RuntimeException();
    }
    public static BufferedImage curveRight(int direction){
        switch(direction){
            case 0: return beltRightToUpImage;
            case 1: return beltDownToRightImage;
            case 2: return beltLeftToDownImage;
            case 3: return beltUpToLeftImage;
        }
        throw new RuntimeException();
    }
    public static BufferedImage straight(int direction){
        switch(direction){
            case 0: return beltUpImage;
            case 1: return beltRightImage;
            case 2: return beltDownImage;
            case 3: return beltLeftImage;
        }
        throw new RuntimeException();
    }
}
