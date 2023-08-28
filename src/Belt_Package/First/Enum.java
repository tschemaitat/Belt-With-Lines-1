package Belt_Package.First;

public class Enum {
    //public static final int left = 0;
    //public static final int right = 1;
    public static final int straight = 0;
    public static final int curveToLeft = 1;
    public static final int curveToRight = 2;
    public static final int up = 0;
    public static final int right = 1;
    public static final int down = 2;
    public static final int left = 3;

    public static final int[][] yMatrix = {
            {0, 1},
            {1, 0},
            {0, -1},
            {-1, 0}
    };
    public static final int[][] xMatrix = {
            {1, 0},
            {0, -1},
            {-1, 0},
            {0, 1}
    };
    public static final int[][] rotateOffsetMatrix = {
            {0, 0},
            {1, 0},
            {1, 1},
            {0, 1}
    };


}
