package Main_and_Drawing;

import javax.swing.*;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Screen extends JPanel {
    JFrame frame;
    Stack_Layout stack;
    List<MouseEvent_Edited> mouse_events;
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
    
        Mouse mouse = new Mouse();
        this.addMouseListener(mouse);

    }
    public void addMouseListener(Mouse mouse){
        super.addMouseListener(mouse);
        mouse.parent = this;
    }
    
    public Layout get_parent_layout(){
        return stack;
    }

    Command external_drawer;
    Graphics current_graphics = null;
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
        mouse_events.add(e);
        if(observe(e)){
            //System.out.println("child recieved event");
            //for now we commit the event right away, later with commit the event during the tick
            e.observer.onClicked(e);
        }
        //System.out.println("did not recieve event");
        
        
    }
    
    private boolean observe(MouseEvent_Edited event){
        return stack.observe(event);
    }
    
    public void update_screen(){
        frame.repaint();
    }
}
