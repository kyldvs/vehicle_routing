package sim.app.vehicleRouting;

import java.util.LinkedList; 

import sim.engine.*;
import sim.field.grid.*;

@SuppressWarnings("serial")
public class VehicleRouting extends SimState
{
    public final int GRID_HEIGHT 	= 100;
    public final int GRID_WIDTH 	= 100;
            
    public final int SOURCE_AREA 		= 1;
    public final int DESTINATION_AREA 	= 2;
    
    public int numVehicles 		= 10;
    public int numDestinations 	= 5;
    public int numSources 		= 1;

    public IntGrid2D 	source 		= new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    public IntGrid2D 	destination = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    public IntGrid2D 	obstacles 	= new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    public SparseGrid2D vehicleGrid = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
    
    LinkedList<Vehicle> 	vehicles 		= new LinkedList<Vehicle>();
    LinkedList<Source> 		sources 		= new LinkedList<Source>();
    LinkedList<Destination> destinations 	= new LinkedList<Destination>();
    
    public VehicleRouting(long seed)
    { 
        super(seed);
    }
        
    public void start()
    {
    	// clear out the schedule
        	super.start();
        
        	
        	
        	
        // read in data and create structures
	        
	        for (int i = 0; i < numDestinations; i++) {
				destinations.add(new Destination(40+i*5, 40+i*5+2, 94, 96));
			}
	        
	        for (int i = 0; i < numSources; i++) {
				sources.add(new Source(10, 12, 10, 12));
			}
	        
	        for (int i = 0; i < numVehicles; i++) {
				vehicles.add(new Vehicle());
			}
        
	        
	        
	        
        // define topology
	        for( int x = 0 ; x < GRID_WIDTH ; x++ )
	        {
	            for( int y = 0 ; y < GRID_HEIGHT ; y++ )
	            {
	                obstacles.field[x][y] = 0;
	                if(x == 50 && y>10 && y<GRID_HEIGHT-10 
	                		|| x == 25 && y>10 && y<GRID_HEIGHT-10 
	                		|| x == 75 && y>10 && y<GRID_HEIGHT-10 
	                		)
	                {
	                    obstacles.field[x][y] = 1;
	                }
	            }
	        }
        
	        
	        
	        
        // initialize source areas
	        for( int x = 0 ; x < GRID_WIDTH ; x++ )
	        {
	            for( int y = 0 ; y < GRID_HEIGHT ; y++ )
	            {
	                source.field[x][y] = 0;
	            }
	        }
	        for(Source s : sources)
	        {
		        for( int x = s.getxMin() ; x <= s.getxMax() ; x++ )
		        {
		            for( int y = s.getyMin() ; y <= s.getyMax() ; y++ )
		            {
		                source.field[x][y] = SOURCE_AREA;
		            }
		        }
	        }
        
	        
	        
	        
        // initialize destination areas
	        for( int x = 0 ; x < GRID_WIDTH ; x++ )
	        {
	            for( int y = 0 ; y < GRID_HEIGHT ; y++ )
	            {
	                destination.field[x][y] = 0;
	            }
	        }
	        for(Destination d : destinations)
	        {
	        	for( int x = d.getxMin() ; x <= d.getxMax() ; x++ )
		        {
		            for( int y = d.getyMin() ; y <= d.getyMax() ; y++ )
		            {
		                destination.field[x][y] = DESTINATION_AREA;
		            }
		        }
	        }
        
	        
	        
	        
        // initialize vehicle locations
	        int offset = 0;
	        for(Vehicle v : vehicles)
	        {
	            vehicleGrid.setObjectLocation(v, 4 + offset, 4);
	            schedule.scheduleRepeating(Schedule.EPOCH, 0, v, 1);
	            offset++;
	        }

    }

    public static void main(String[] args)
    {
        doLoop(VehicleRouting.class, args);
        System.exit(0);
    }
    
    public int getNumVehicles()
    {
    	return numVehicles;
    }
    
    public void setNumVehicles(int val)
    {
    	if (val > 0) numVehicles = val;
    }
}