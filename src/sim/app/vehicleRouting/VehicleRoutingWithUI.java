package sim.app.vehicleRouting;

import java.awt.Color; 
import sim.util.gui.*;
import javax.swing.JFrame;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;

public class VehicleRoutingWithUI extends GUIState{
	
	private final int DISPLAY_HORIZONTAL_LEN 	= 400;
	private final int DISPLAY_VERTICAL_LEN 		= 400;
	
	public Display2D display;
    public JFrame displayFrame;

    FastValueGridPortrayal2D destinationPortrayal 	= new FastValueGridPortrayal2D("Destination", true);
    FastValueGridPortrayal2D sourcePortrayal 		= new FastValueGridPortrayal2D("Source", true);
    FastValueGridPortrayal2D obstaclePortrayal 		= new FastValueGridPortrayal2D("Obstacle", true);
    
    SparseGridPortrayal2D vehiclePortrayal 			= new SparseGridPortrayal2D();
    
    public void setupPortrayals()
    {
        VehicleRouting vr = (VehicleRouting)state;
        
        sourcePortrayal.setField(vr.sourceGrid);
        destinationPortrayal.setField(vr.destinationGrid);
        obstaclePortrayal.setField(vr.obstacleGrid);
        
        sourcePortrayal		.setMap(new SimpleColorMap(0, 1, new Color(0,0,0,0), new Color(0,255,0,255)));
        destinationPortrayal.setMap(new SimpleColorMap(0, 1, new Color(0,0,0,0), new Color(255,0,0,255)));
        obstaclePortrayal	.setMap(new SimpleColorMap(0, 1, new Color(0,0,0,0), new Color(0,0,0,255)));
        
        vehiclePortrayal.setField(vr.vehicleGrid);
            
        display.reset();
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
	    
	    display = new Display2D(DISPLAY_VERTICAL_LEN, DISPLAY_HORIZONTAL_LEN, this);
	    displayFrame = display.createFrame();
	    
	    controller.registerFrame(displayFrame);
	    displayFrame.setVisible(true);
	    
	    display.attach(sourcePortrayal,"Source Locations");
	    display.attach(destinationPortrayal,"Destination Locations");
	    display.attach(obstaclePortrayal,"Obstacles");
	    display.attach(vehiclePortrayal,"Vehicles");
	    
	    display.setBackdrop(Color.white);
    }
	
	public void quit()
	{
		super.quit();
		
	    if (displayFrame!=null)
	    {
	    	displayFrame.dispose();
	    }
	    
	    displayFrame = null;
	    display = null;
    }
	
    public static void main(String[] args)
    {
    	new VehicleRoutingWithUI().createController();
    }
    
    public VehicleRoutingWithUI()
    {
    	super(new VehicleRouting(1));
    }
    
    public Object getSimulationInspectedObject()
    {
    	return state;
    }

    public static String getName()
    {
    	return "Vehicle Routing";
    }
}