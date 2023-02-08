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
    Point mouse_point;
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
            stack.draw(grf);
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
    
    private boolean observe(MouseEvent_Edited event){
        return stack.observe(event);
    }
    
    public void update_screen(){
        mouse_point = MouseInfo.getPointerInfo().getLocation();
        frame.repaint();
    }
}
