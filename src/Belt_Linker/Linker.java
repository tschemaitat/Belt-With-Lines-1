package Belt_Linker;

import Belt_Generater.Belt_Line;

import java.util.*;

public interface Linker {


    public abstract void link_line(Belt_Line line);
    public abstract void link_lines(Belt_Line[] line);
    public abstract void unlink_line(Belt_Line line);
    public HashMap<Belt_Line, Basic_Iterator> get_map();

    public ArrayList<Basic_Iterator> get_linked();

}
