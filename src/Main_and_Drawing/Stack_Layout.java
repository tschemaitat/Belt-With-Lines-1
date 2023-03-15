package Main_and_Drawing;

import Main_and_Drawing.Layouts.RectP;

import java.awt.*;

public class Stack_Layout extends Layout {
    
    public Stack_Layout(int x, int y, int width, int height) {
        super(new RectP(x, y, width, height), null);
    }
    
    protected void draw_custom(Graphics2D grf){
        if(list.size() == 0)
            return;
        for(int i = 0; i < list.size(); i++){
            Twod obj = list.get(i);
            if(obj.visibility == VISIBLE)
                obj.draw(grf);
            //obj.drawBounds(grf);
        }
    }
    
    public boolean observe(MouseEvent_Edited event){
        if(list.size() == 0)
            return false;
        for(int i = list.size() - 1; i >= 0; i--){
            Twod obj = list.get(i);
            if(obj.observe(event))
                return true;
        }
        return false;
    }

    protected void add_custom(Twod twod){
    
    }
    
    protected void remove_custom(Twod twod) {
    
    }
    
    protected void to_top(Twod obj){
    
    }
    
    
    @Override
    public void onMouseEvent(MouseEvent_Edited event) {
    
    }
    
    
}
