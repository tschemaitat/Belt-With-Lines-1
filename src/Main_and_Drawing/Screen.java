package Main_and_Drawing;

import javax.swing.*;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Screen extends JPanel {
    JFrame frame;
    Stack_Layout stack;
    List<MouseEvent_Edited> mouse_events;
    
    Command external_drawer;
    Graphics current_graphics = null;
    Mouse mouse;
    public Point mouse_point;
    public Screen(int width, int height){
        mouse_events = new ArrayList<>();
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //adds this "drawer" object to frame
        frame.add(this);
        frame.setSize(width, height);
        frame.setLocation(200, 10);
        frame.setVisible(true);

        //this calls paintComponent
        //frame.repaint

        stack = new Stack_Layout(0, 0, width, height);
    
        mouse = new Mouse();
        this.addMouseListener(mouse);

    }
    public void addMouseListener(Mouse mouse){
        super.addMouseListener(mouse);
        mouse.parent = this;
    }
    
    public Layout get_parent_layout(){
        return stack;
    }

    
    protected void paintComponent(Graphics grf) {
        current_graphics = grf;
        super.paintComponent(grf);
        Graphics2D graph = (Graphics2D) grf;
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(stack != null)
            stack.draw((Graphics2D) grf);
        //grf.setColor(Color.white);
        //grf.fillRect(600, 100, 50, 50);
        if(external_drawer != null)
            external_drawer.execute();
        current_graphics = null;
    }
    public void set_paint_method(Command command){
        external_drawer = command;
    }
    public Graphics get_graphics(){
        return frame.getGraphics();
    }
    
    public void addTwoD(Image_Twod twod){
        stack.add(twod);
    }
    public void removeTwoD(Image_Twod twod){
        stack.remove(twod);
    }
    
    public synchronized void add_mouse_event(MouseEvent_Edited e){
        //System.out.println("adding event");
        if(e == null){
            System.out.println("it gave us a null event");
            return;
        }
        
        mouse_events.add(e);
        //System.out.println("did not recieve event");
    }
    
    public synchronized MouseEvent_Edited pop_mouse_event(){
        if(mouse_events.size() != 0)
            return mouse_events.remove(mouse_events.size() - 1);
        return null;
    }
    public synchronized void pop_mouse_event_to_observe(){
        if(mouse_events.size() == 0)
            return;
        //System.out.println("popping event to observe");
        if(mouse_events.size() != 0){
            MouseEvent_Edited event = mouse_events.remove(mouse_events.size() - 1);
            boolean found = observe(event);
            if(found){
                event.observer.onMouseEvent(event);
            }
        }
    }
    Twod mouse_touching_this = null;
    public void observe_mouse_touch(){
        //System.out.println("sending touch event");
        
        Point mouse_point = getMouse_point();
        int x = mouse_point.x;
        int y = mouse_point.y;
        
        MouseEvent_Edited touch_event = new MouseEvent_Edited(x, y, MouseEvent_Edited.type_touch);
        MouseEvent_Edited untouch_event = new MouseEvent_Edited(x, y, MouseEvent_Edited.type_untouch);
        boolean found = observe(touch_event);
        if(found){
            //System.out.println("found observer: " + touch_event.observer.name + "point: "+mouse_point + "event: " + touch_event.x() + ", " + touch_event.y());
            if(mouse_touching_this != null){
                if(mouse_touching_this != touch_event.observer){
                    mouse_touching_this.mouse_touching = false;
                    //System.out.println("sending untouch: " + untouch_event.type());
                    mouse_touching_this.onMouseEvent(untouch_event);
                    mouse_touching_this = null;
                }
            }
            //System.out.println("observed touch");
            if(touch_event.observer.mouse_touching == false){
                mouse_touching_this = touch_event.observer;
                touch_event.observer.mouse_touching = true;
                touch_event.observer.onMouseEvent(touch_event);
            }
            
            return;
        }
        else{
            if(mouse_touching_this != null){
                mouse_touching_this.mouse_touching = false;
                mouse_touching_this.onMouseEvent(untouch_event);
                mouse_touching_this = null;
            }
        }
        //System.out.println("nothing observed touch");
        
    }
    
    private Point getMouse_point(){
        Point windowPosition = this.getLocationOnScreen();
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        int x = mousePosition.x - windowPosition.x;
        int y = mousePosition.y - windowPosition.y;
        mouse_point = new Point(x, y);
        return new Point(x, y);
        
    }
    
    private boolean observe(MouseEvent_Edited event){
        return stack.observe(event);
    }
    
    public void update_screen(){
        //mouse_point = MouseInfo.getPointerInfo().getLocation();
        frame.repaint();
    }
}
