package Belt_Linker;


import Belt_Generater.Belt_Line;
import Belt_Generater.Position;

public abstract class Item_Iterator implements Line_To_Line {
    Belt_Line[] lines;
    boolean iterated = false;
    Line_To_Line output = null;
    Line_To_Line[] inputs = null;
    int length;
    public Item_Iterator(int length, Belt_Line[] lines){
        this.lines = lines;
        this.length = length;
    }

    @Override
    public void set_output(Line_To_Line lines){
        output = lines;
    }
    @Override
    public void set_input(Line_To_Line[] lines){
        inputs = lines;
    }

    public ItemPositionStruct[] items(){
        Belt_Line line = lines[0];
        ItemLineLocationStruct[] linked_pos = item_pos_of_linked();
        ItemPositionStruct[] structs = new ItemPositionStruct[linked_pos.length];
        for(int i = 0; i < linked_pos.length; i++){
            structs[i] = new ItemPositionStruct(linked_pos[i].item, line.item_location(linked_pos[i].position));
        }
        return structs;
    }
}
