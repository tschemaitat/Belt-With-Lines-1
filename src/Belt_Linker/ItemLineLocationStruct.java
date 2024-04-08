package Belt_Linker;

import Belt_Generater.Belt_Line;

public class ItemLineLocationStruct {
    public int item;
    public Belt_Line line;
    public int position;

    public ItemLineLocationStruct(int item, Belt_Line line, int position){
        this.item = item;
        this.line = line;
        this.position = position;
    }
}
