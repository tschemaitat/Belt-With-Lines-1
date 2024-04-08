package Belt_Generater;

public abstract class Belt_Data {

    public static int right = 0;
    public int direction;
    public abstract int shape();
    public abstract int[][] item_positions();
    public abstract int size(int side);
    public Drawable drawable(){
        return Belt_Drawable_Factory.drawable(shape(), direction);
    }
    public abstract int length(int side);


}
