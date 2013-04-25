package sim.app.vehicleRouting;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Int2D;

@SuppressWarnings("serial")
public class Vehicle extends OvalPortrayal2D implements Steppable
{
	public boolean hasItem = false;
	public boolean collision = false;
	
	private Destination dest;
	private Source src;
	private Job job;
	
	private Stoppable stop;

	private Color noItemColor 	= Color.blue;
	private Color itemColor 	= Color.red;

	public void act( final SimState state )
	{
		final VehicleRouting vr = (VehicleRouting) state;
		List<Point> path;
		Int2D loc = vr.vehicleGrid.getObjectLocation(this);
		
		if(job == null)
		{
			setJob(vr.getJob(this));
			if (job == null) {
				stop.stop();
			}
		}
		else
		{
			if ( hasItem )
			{				
				dest = job.getDestination();
				path = vr.findPath(loc.toPoint(), dest.adjacentFunction(), collision);
				if (path == null) {
					collision = true;
					vr.collisions++;
					path = vr.findPath(loc.toPoint(), dest.adjacentFunction(), collision);
				}
				
				if (path != null)
				{
					vr.vehicleGrid.setObjectLocation(this, new Int2D(path.get(0)));
					if (path.size() == 1)
					{
						hasItem = false;
						job = null;
						collision = false;
					}
				}
			}
			else
			{
				src = job.getSource();
				path = vr.findPath(loc.toPoint(), src.adjacentFunction(), collision);
				if (path == null) {
					collision = true;
					vr.collisions++;
					path = vr.findPath(loc.toPoint(), src.adjacentFunction(), collision);
				}
				
				
				if (path != null)
				{
					vr.vehicleGrid.setObjectLocation(this, new Int2D(path.get(0)));
					if (path.size() == 1)
					{
						hasItem = true;
						collision = false;
					}
				}		
			}
		}
	}

	public void step( final SimState state )
	{
		act(state);
	}

	public final void draw( Object object, Graphics2D graphics, DrawInfo2D info )
	{
		if( hasItem )
		{
			graphics.setColor( itemColor );
		}
		else
		{
			graphics.setColor( noItemColor );
		}

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

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
	
	public void setStoppable(Stoppable stop) {
		this.stop = stop;
	}
}