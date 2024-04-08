package Belt_Linker;

public interface Line_To_Line {
    public boolean insert(int index, int item);
    public void iterate();

    public void set_output(Line_To_Line line);
    public void set_input(Line_To_Line[] line);
    public ItemLineLocationStruct[] item_pos_of_linked();
}