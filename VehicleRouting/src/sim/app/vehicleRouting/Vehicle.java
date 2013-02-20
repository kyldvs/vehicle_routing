package sim.app.vehicleRouting;

import sim.portrayal.*;
import sim.portrayal.simple.*;
import sim.util.*;
import sim.engine.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Vehicle extends OvalPortrayal2D implements Steppable
{
	public boolean hasItem = false;
	public Destination dest;
	public Source src;
	public boolean idle = true;
	private Color noItemColor = Color.black;
    private Color itemColor = Color.red;

    public void act( final SimState state )
    {
    	
        final VehicleRouting vr = (VehicleRouting)state;
                
        Int2D location = vr.vehicleGrid.getObjectLocation(this);
        int curr_x = location.x;
        int curr_y = location.y;
        int dx = 1, dy = 1;
        
        if (hasItem) 
        {
        	// Choose a direction for the robot (a dx and dy) make sure vr.obstacles.field[_x][_y] != 1 and we are within the grid 
        	if(src == null || !this.equals(src.getAssignedVehicle())) {
        		for(Source s : vr.sources) {
        			if(!s.isAssigned()) {
        				src = s;
        				idle = false;
//        				s.setAssigned(true);
        				break;
        			} else {
        				idle = true;
        			}
        		}
        	}
 
        	if(idle) {
        		dx = 0;
        		dy = 0;
        	} else {
        		
        		if(curr_x < src.point.x) {
        			dx = 1;
        		} else if(curr_x == src.point.x) {
         			dx = 0;
         		} else {
         			dx = -1;
         		}
        		
        		if(curr_y < src.point.x) {
        			dy = 1;
        		} else if(curr_y == src.point.x) {
         			dy = 0;
         		} else {
         			dy = -1;
         		}
        	}
        	
    		if(vr.obstacles.field[curr_x+dx][curr_y+dy] == 1) {
    			dy = 1;
    			dx = 0;
    		}
    		
        	int new_x = curr_x + dx;
        	int new_y = curr_y + dy;
            vr.vehicleGrid.setObjectLocation(this, new Int2D(new_x, new_y));
            if (vr.source.field[new_x][new_y] == vr.SOURCE_AREA)
            {
            	hasItem = ! hasItem;
            	src.setAssigned(false);
            }
        }
        else
        {
        	if(dest == null || !this.equals(dest.getAssignedVehicle())) {
        		for(Destination d : vr.destinations) {
        			if(!d.isAssigned()) {
        				dest = d;
        				idle = false;
//        				d.setAssigned(true);
        				break;
        			} else {
        				idle = true;
        			}
        		}
        	}
        	// Choose a direction for the robot (a dx and dy) make sure vr.obstacles.field[_x][_y] != 1 and we are within the grid 
        	if(idle) {
        		dx = 0;
        		dy = 0;
        	} else {
        		if(curr_x < dest.point.x) {
        			dx = 1;
        		} else if(curr_x == dest.point.x) {
         			dx = 0;
         		} else {
         			dx = -1;
         		}
        		
        		if(curr_y < dest.point.x) {
        			dy = 1;
        		} else if(curr_y == dest.point.x) {
         			dy = 0;
         		} else {
         			dy = -1;
         		}
        		
        		if(vr.obstacles.field[curr_x+dx][curr_y+dy] == 1) {
        			dy = -1;
        			dx = 0;
        		}
        	}

        	int new_x = curr_x + dx;
        	int new_y = curr_y + dy;
            vr.vehicleGrid.setObjectLocation(this, new Int2D(new_x, new_y));
            if (vr.destination.field[new_x][new_y] == vr.DESTINATION_AREA)
            {
            	hasItem = ! hasItem;
            	dest.setAssigned(false);
            }
        }
    }

    
    
    
    
    
    
    
    public void step( final SimState state )
    {
        act(state);
    }

    public final void draw(Object object, Graphics2D graphics, DrawInfo2D info)
    {
        if( hasItem )
        {
            graphics.setColor( itemColor );
        }
        else
        {
            graphics.setColor( noItemColor );
        }

        // this code was stolen from OvalPortrayal2D
        int x = (int)(info.draw.x - info.draw.width / 2.0);
        int y = (int)(info.draw.y - info.draw.height / 2.0);
        int width = (int)(info.draw.width);
        int height = (int)(info.draw.height);
        graphics.fillOval(x,y,width, height);

    }
    
    public boolean hasItem()
    {
    	return hasItem;
    }

    public void setItem(boolean val)
    {
    	hasItem = val;
    }
}