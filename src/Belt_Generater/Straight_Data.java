package Belt_Generater;
import static Belt_Package.First.Enum.*;
public class Straight_Data extends Belt_Data{
    public static int left = 4;
    public static int right = 4;
    public Straight_Data(){
    }
    @Override
    public int shape() {
        return straight;
    }

    @Override
    public int[][] item_positions() {
        return new int[0][];
    }

    @Override
    public int size(int side) {
        return 0;
    }

    @Override
    public int length(int side) {
        if(side == 0)
            return left;
        return right;
    }
}
