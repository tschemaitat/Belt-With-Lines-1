package Belt_Generater;

public class Position implements Comparable<Position>{
    private int x;
    private int y;
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int x(){
        return x;
    }
    public int y(){
        return y;
    }

    public Position add_direction(int direction){
        switch (direction){
            case 0: return new Position(x, y - 1);
            case 1: return new Position(x + 1, y);
            case 2: return new Position(x, y + 1);
            case 3: return new Position(x - 1, y);
        }
        throw new RuntimeException();
    }

    public Position mult(int num){
        return new Position(x*num, y*num);
    }
    @Override
    public boolean equals(Object anObject){
        if(!(anObject instanceof Position other))
            return false;
        if(other.x == x && other.y == y)
            return true;
        return false;
    }
    public String toString(){
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int compareTo(Position o) {
        boolean equals = equals(o);
        if(equals){
            return 0;
        }
        else
            return 1;
    }

    @Override
    public int hashCode(){
        int num = 10000000;
        return num * x + y;
    }
}