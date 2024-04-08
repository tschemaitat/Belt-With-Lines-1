package Belt_Linker;

import Belt_Generater.Belt_Line;
import Belt_Generater.Item_Cordinate;
import Belt_Generater.Position;

import java.util.ArrayList;

public class Basic_Iterator extends Item_Iterator {
    int[] items;

    public Basic_Iterator(int length, Belt_Line[] lines){
        super(length, lines);
        items = new int[length];
    }

    @Override
    public boolean insert(int index, int item) {
        if(items[index] != -1)
            return false;
        items[index] = item;
        return true;
    }


    @Override
    public void iterate() {
        if(iterated)
            return;
        outputting:
        if(items[0] != -1){
            if(output == null)
                break outputting;
            output.iterate();
            if(!output.insert(0, items[0]))
                break outputting;
            for(int i = 1; i < items.length; i++){
                items[i-1] = items[i];
            }
            return;
        }

        int first_empty_index = -1;
        for(int i = 1; i < items.length; i++){
            if(items[i] != -1){
                first_empty_index = i;
                break;
            }
        }
        if(first_empty_index == -1)
            return;
        for(int i = first_empty_index + 1; i < items.length; i++){
            items[i-1] = items[i];
        }
    }

    @Override
    public ItemLineLocationStruct[] item_pos_of_linked() {
        ArrayList<ItemLineLocationStruct> list = new ArrayList<>();
        for(int i = 0; i < items.length; i++){
            if(items[i] != -1){
                list.add(new ItemLineLocationStruct(items[i], lines[0], i));
            }
        }
        ItemLineLocationStruct[] ints = list.toArray(new ItemLineLocationStruct[0]);
        return ints;
    }
}
