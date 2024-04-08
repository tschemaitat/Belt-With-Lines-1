package Belt_Generater;

import static Belt_Package.First.Enum.*;


public class CurveLeft_data extends Belt_Data{
    public static int left = 2;
    public static int right = 6;
    public CurveLeft_data(){

    }

    @Override
    public int shape() {
        return curveToLeft;
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
