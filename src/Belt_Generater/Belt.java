package Belt_Generater;

import java.awt.Graphics2D;
import java.util.*;
import static Belt_Package.First.Enum.*;

public class Belt {
    public static int belt_id = 0;
    public static final int line_length = 4;
    public int id;
    public Belt_Data data;
    public Position position;
    public int direction;
    Belt_Line[] lines;

    public Belt(Belt_Data data, Position position, int direction) {
        id = belt_id;
        belt_id++;
        this.data = data;
        this.direction = direction;
        this.position = position;
        data.direction = direction;

        lines = new Belt_Line[]{new Belt_Line(this, 0), new Belt_Line(this, 1)};
    }

    public Position get_item_location(int index, int side){
        return Item_Cordinate.get_belt_item_positions(position, direction, data.shape())[side][index];
    }

    public Position get_item_location(int index, Belt_Line line){
        return Item_Cordinate.get_belt_item_positions(position, direction, data.shape())[line.line_id][index];
    }

    public Belt_Line line(int side){
        return lines[side];
    }



    public Belt_Line_Connection get_output(Belt_Line line){
        return null;
    }

    public Belt_Line_Connection get_input(Belt_Line line){
        return null;
    }

    public void draw(Graphics2D grf){
        grf.drawImage(data.drawable().image, position.x(), position.y(), null);
    }

    public int get_length(Belt_Line line) {
        int side = line.line_id;
        switch(data.shape()){
            case straight:
                return 4;
            case curveToLeft:
                if(side == 0)
                    return 2;
                return 6;
            case curveToRight:
                if(side == 0)
                    return 6;
                return 2;

        }
        throw new RuntimeException();
    }


}
