package sim.app.vehicleRouting;

import sim.engine.*;
import sim.display.*;
import sim.portrayal.grid.*;
import java.awt.*;
import javax.swing.*;

public class VehicleRoutingWithUI extends GUIState{
	
	private final int DISPLAY_HORIZONTAL_LEN = 400;
	private final int DISPLAY_VERTICAL_LEN = 400;
	
	public Display2D display;
    public JFrame displayFrame;

    FastValueGridPortrayal2D destinationPortrayal = new FastValueGridPortrayal2D("Destination", true);
    FastValueGridPortrayal2D sourcePortrayal = new FastValueGridPortrayal2D("Source", true);
    FastValueGridPortrayal2D obstaclePortrayal = new FastValueGridPortrayal2D("Obstacle", true);
    SparseGridPortrayal2D vehiclePortrayal = new SparseGridPortrayal2D();
                
    public static void main(String[] args)
    {
        new VehicleRoutingWithUI().createController();
    }
    
    public VehicleRoutingWithUI()
    {
    	super(new VehicleRouting(System.currentTimeMillis()));
    }
    
    public VehicleRoutingWithUI(SimState state)
    {
    	super(state);
    }
    
    public Object getSimulationInspectedObject()
    {
    	return state;
    }

    public static String getName()
    {
    	return "Vehicle Routing";
    }
    
    public void setupPortrayals()
    {
        VehicleRouting vr = (VehicleRouting)state;

        sourcePortrayal.setField(vr.source);
        sourcePortrayal.setMap(new sim.util.gui.SimpleColorMap(0,1,
                													new Color(0,0,0,0),
                													new Color(0,255,0,255)
        														)
        						);
        destinationPortrayal.setField(vr.destination);
        destinationPortrayal.setMap(new sim.util.gui.SimpleColorMap(0,1,
                													new Color(0,0,0,0),
                													new Color(255,0,0,255)
        														)
        						);
        obstaclePortrayal.setField(vr.obstacles);
        obstaclePortrayal.setMap(new sim.util.gui.SimpleColorMap(0,1,
                													new Color(0,0,0,0),
                													new Color(0,0,0,255) 
        														)
        						);
        vehiclePortrayal.setField(vr.vehicleGrid);
            
        // reschedule the displayer
        display.reset();

        // redraw the display
        display.repaint();
    }
    
    public void start()
    {
        super.start();
        setupPortrayals();
    }
            
    public void load(SimState state)
    {
        super.load(state);
        setupPortrayals();
    }

	
    public void init(Controller controller)
    {
	    super.init(controller);
	    
	    // Make the Display2D
	    display = new Display2D(DISPLAY_VERTICAL_LEN, DISPLAY_HORIZONTAL_LEN, this);
	    displayFrame = display.createFrame();
	    
	    // register the frame so it appears in the "Display" list
	    controller.registerFrame(displayFrame);
	    displayFrame.setVisible(true);
	    
	    // attach to display
	    display.attach(sourcePortrayal,"Source Locations");
	    display.attach(destinationPortrayal,"Destination Locations");
	    display.attach(obstaclePortrayal,"Obstacles");
	    display.attach(vehiclePortrayal,"Vehicles");
	    
	    display.setBackdrop(Color.white);
    }
	
	public void quit()
	{
		super.quit();
		
	    // Disposing the displayFrame automatically calls quit() 
		// on the display, so we don't need to do so ourselves here.
	    if (displayFrame!=null) displayFrame.dispose();
	    displayFrame = null;
	    display = null;
    }
	
}