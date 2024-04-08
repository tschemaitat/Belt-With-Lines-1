package Belt_Integrater;

import Belt_Linker.*;
import Belt_Package.ItemLocationStruct;
import Data_Structure_Benchmark.Belt_To_Belt;
import Belt_Generater.*;
import Main_and_Drawing.Layouts.RectP;
import Main_and_Drawing.MouseEvent_Edited;
import Main_and_Drawing.Screen;
import Main_and_Drawing.Stack_Layout;
import Main_and_Drawing.Twod;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Integrater {
    int window_width = 800;
    public static void main(String[] args){
        Integrater integrater = new Integrater();
    }
    //public Belt_To_Belt belt_data_structure;
    public Linker  linker;

    public Generater generater;

    Belt[] belts;
    public Integrater(){
        HashMap<Position, Integer> map = generate_belt_map();
        belts = new Generater().make_belt_bulk(map);
        for(int i = 0; i < belts.length; i++){
            System.out.println("shape: " + belts[i].data.shape() + ", pos: " + belts[i].position + ", dir: " + belts[i].direction);
        }
        Screen screen = create_screen();

        ArrayList<Item_Iterator> list_linked = new ArrayList<>();
        linker = new Basic_Linker();
        Belt_Line[] lines = new Belt_Line[belts.length * 2];
        for(int i = 0; i < belts.length; i++){
            lines[2*i] = belts[i].line(0);
            lines[2*i + 1] = belts[i].line(1);
        }
        linker.link_lines(lines);
        HashMap<Belt_Line, Basic_Iterator> line_to_linked = linker.get_map();
        insert_items(line_to_linked);


        //AbstractList<? extends Object> list = new ArrayList<String>();
        screen.update_screen();
    }

    public void insert_items(HashMap<Belt_Line, Basic_Iterator> line_to_linked){
        Belt belt = belts[0];
        Belt_Line line = belt.line(0);
        ItemLineLocationStruct item_location = new ItemLineLocationStruct(1, line, 0);
        Item_Iterator iterator = line_to_linked.get(line);
        boolean worked = iterator.insert(item_location.position, item_location.item);
        System.out.println("item insert worked: " + worked);
    }

    public ArrayList<ItemPositionStruct> get_item_positions(Linker linker){
        ArrayList<Basic_Iterator> linked = linker.get_linked();
        ArrayList<ItemPositionStruct> positions = new ArrayList<>();
        for(int i = 0; i < linked.size(); i++){
            ItemPositionStruct[] position_array = linked.get(i).items();
            positions.addAll(Arrays.asList(position_array));
        }
        return positions;
    }

    public void draw(Graphics2D grf){
        Belt_Drawer.draw_belts(grf, belts);
        draw_items_in_corner(grf);
        System.out.println("drawing twod");

        ArrayList<ItemPositionStruct> item_positions = get_item_positions(linker);
        draw_items(grf, item_positions);
    }

    public void draw_items(Graphics2D grf, ArrayList<ItemPositionStruct> item_positions){
        int item_size = 32;
        for(int i = 0; i < item_positions.size(); i++){
            ItemPositionStruct position = item_positions.get(i);
            grf.drawRect(position.position.x(), position.position.y(), item_size, item_size);
        }
    }

    public Screen create_screen(){
        Screen screen = new Screen(window_width, window_width);
        Stack_Layout layout = new Stack_Layout(0, 0, window_width, window_width);
        screen.get_parent_layout().add(layout);
        Twod twod = new Twod(new RectP(0, 0, window_width, window_width), layout) {
            @Override
            public void draw(Graphics2D grf) {
                Integrater.this.draw(grf);
            }

            @Override
            public void onMouseEvent(MouseEvent_Edited event) {

            }
        };
        return screen;
    }

    public void draw_items_in_belt(Belt[] belts, Graphics2D grf){
        Belt belt = belts[0];
        Position[][] items = Item_Cordinate.get_belt_item_positions(belt.position.mult(64), belt.direction, belt.data.shape());
        Color[] colors = {Color.BLUE, Color.GREEN};
        for(int i = 0; i < 2; i++){
            Color color = colors[i];
            grf.setColor(color);
            int size = 4;
            for(int j = 0; j < items[i].length; j++){
                Position pos = items[i][j];
                grf.drawRect(pos.x() - size/2, pos.y() - size/2, size, size);
            }
        }
    }

    public void draw_items_in_corner(Graphics2D grf){
        Position[][][] positions = new Position[3][][];
        for(int i = 0; i < 3; i++){
            positions[i] = Item_Cordinate.get_relative_position(i);
        }
        Color[][] colors = new Color[][]{
                {Color.RED, Color.PINK},
                {Color.BLUE, Color.CYAN},
                {Color.yellow, Color.orange}
        };
        int belt_x = 64;
        int item_size = 4;
        for(int orientation = 0; orientation < 4; orientation++){
            belt_x = 64*orientation + 64;
            for(int i = 0; i < 3; i++){
                int belt_y = 128*i + 64;
                Position belt_position = new Position(belt_x, belt_y);
                grf.setColor(Color.BLACK);
                grf.drawRect(belt_x, belt_y, 64, 64);
                for(int j = 0; j < 2; j++){
                    grf.setColor(colors[i][j]);
                    for(int k = 0; k < positions[i][j].length; k++){
                        Position position = positions[i][j][k];
                        int x = belt_x - item_size + positions[i][j][k].x();
                        int y = belt_y - item_size + positions[i][j][k].y();
                        Position position_shifted = Item_Cordinate.shiftItemCord(position, belt_position, orientation, item_size);
                        grf.drawRect(position_shifted.x(), position_shifted.y(), item_size, item_size);
                        //grf.drawRect(x, y, item_size, item_size);

                    }
                }
            }
        }

    }





    public HashMap<Position, Integer> generate_belt_map(){
        int[][] directions = new int[][]{
                {-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1},
                {-1, 2, 1, 1, 2,-1,-1,-1},
                {-1, 1, 0,-1, 3, 2,-1,-1},
                {-1, 0,-1, 1, 2, 3,-1,-1},
                {-1, 0, 3, 3, 3,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1},
                {-1,-1,-1,-1,-1,-1,-1,-1}
        };
        HashMap<Position, Integer> map = new HashMap<>();
        for(int i = 0; i < directions.length; i++){
            for(int j = 0; j < directions[i].length; j++){
                if(directions[i][j] == -1)
                    continue;
                Position position = new Position(j, i);
                int direction = directions[i][j];
                map.put(position, direction);
            }
        }
        return map;
    }
}
