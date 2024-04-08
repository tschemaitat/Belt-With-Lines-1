package Belt_Linker;

import Belt_Generater.Belt_Line;

import java.util.ArrayList;
import java.util.HashMap;

public class Basic_Linker implements Linker {
    ArrayList<Belt_Line> lines = new ArrayList<>();
    ArrayList<Basic_Iterator> list_linked = new ArrayList<>();

    HashMap<Belt_Line, Basic_Iterator> line_to_linked = new HashMap<>();
    @Override
    public void link_line(Belt_Line line) {

        throw new RuntimeException();
    }

    public HashMap<Belt_Line, Basic_Iterator> get_map(){
        return line_to_linked;
    }

    @Override
    public ArrayList<Basic_Iterator> get_linked() {
        return list_linked;
    }

    public void link_lines(Belt_Line[] new_lines){
        Basic_Iterator[] new_iterator_list = new Basic_Iterator[new_lines.length];
        for(int i = 0; i < new_lines.length; i++){
            Belt_Line line = new_lines[i];
            lines.add(line);
            Basic_Iterator linked = new Basic_Iterator(line.length(), new Belt_Line[]{line});
            list_linked.add(linked);
            line_to_linked.put(line, linked);
        }
        for(int i = 0; i < new_lines.length; i++){
            Belt_Line line = new_lines[i];
            Basic_Iterator linked = new_iterator_list[i];
            Basic_Iterator output_linked = line_to_linked.get(line.output().belt_lines[0]);
            Belt_Line[] input_lines = line.input().belt_lines;
            Basic_Iterator[] list_input_linked = new Basic_Iterator[input_lines.length];
            for(int j = 0; j < input_lines.length; j++){
                list_input_linked[j] = line_to_linked.get(input_lines[j]);
            }
            linked.set_input(list_input_linked);
            linked.set_output(output_linked);
        }
    }

    @Override
    public void unlink_line(Belt_Line line) {

    }
}
