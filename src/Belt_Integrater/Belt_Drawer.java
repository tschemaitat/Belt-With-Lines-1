package Belt_Integrater;

import Belt_Generater.Belt;

import java.awt.*;

public class Belt_Drawer {
    public static void draw_belts(Graphics2D grf, Belt[] belts){
        int belt_size = 64;
        for(int i = 0; i < belts.length; i++){
            Belt belt = belts[i];
            int x = belt_size * belt.position.x();
            int y = belt_size * belt.position.y();
            grf.drawImage(belt.data.drawable().image, x, y, null);
        }
    }
}
