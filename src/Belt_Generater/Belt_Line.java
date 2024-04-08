package Belt_Generater;

public class Belt_Line {
    public Belt belt;
    public int line_id;

    public Belt_Line(Belt belt, int line_id){
        this.belt = belt;
        this.line_id = line_id;
    }
    public Belt_Line_Connection output(){
        return belt.get_output(this);
    }
    public Belt_Line_Connection input(){
        return belt.get_input(this);
    }

    public Position item_location(int index){
        return belt.get_item_location(index, this);
    }

    public int length(){
        return belt.get_length(this);
    }

    public boolean equals(Object object){
        if(!(object instanceof Belt_Line line))
            return false;
        if(belt == line.belt && line_id == line.line_id)
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return belt.id*1000000 + line_id;
    }
}
