package sim.app.vehicleRouting;

import java.awt.Color; 
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import sim.app.stats.Statistics;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Int2D;

@SuppressWarnings("serial")
public class Vehicle extends OvalPortrayal2D implements Steppable {
	
	
	public int priority;
	
	private static int counter = 0;
	private final int uid = counter++;
	
	private Statistics stats;
	private Stoppable stop;
	
	private int step = 0;

	public boolean hasItem = false;
	
	private Destination dest;
	private Source src;
	private Job job;

	private Color noItemColor = Color.blue;
	private Color itemColor = Color.red;

	public Vehicle(int priority) {
		this.priority = priority;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vehicle) {
			return ((Vehicle) obj).uid == uid;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return uid;
	}
	
	public void step(final SimState state) {
		stats.updateSteps(++step);
		act(state);
	}

	private void act(final SimState state) {
		final VehicleRouting vr = (VehicleRouting) state;
		List<Point> path;
		Int2D loc = vr.vehicleGrid.getObjectLocation(this);

		if (job == null) {
			setJob(vr.getJob(this));
			if (job == null) {
				stop.stop();
			}
		} else {
			if (hasItem) {
				dest = job.getDestination();
				path = vr.findPath(loc.toPoint(), dest.adjacentFunction());
								
				if(path == null || path.size() == 0)
				{
					return;
				}
				
				if(pushBack(path.get(0), path, state))
				{
					vr.vehicleGrid.setObjectLocation(this, new Int2D(path.get(0)));
					if (path.size() == 1) {
						stats.deliver(this, job, path.get(0));
						hasItem = false;
						job = null;
					}
				}
				else
				{
					this.priority = VehicleRouting.priorityCount++;
				}
			} else {
				src = job.getSource();
				path = vr.findPath(loc.toPoint(), src.adjacentFunction());
				
				if(path == null || path.size() == 0)
				{
					return;
				}
				
				if(pushBack(path.get(0), path, state))
				{
					vr.vehicleGrid.setObjectLocation(this, new Int2D(path.get(0)));
					if (path.size() == 1) {
						stats.pickup(this, job, path.get(0));
						hasItem = true;
					}
				}
				else
				{
					this.priority = VehicleRouting.priorityCount++;
				}
			}
		}
	}
	
	public boolean pushBack(Point currentPoint, List<Point> path, SimState state)
	{
		VehicleRouting vr = (VehicleRouting) state;
		
		if(vr.vehicleGrid.getObjectsAtLocation(currentPoint.x, currentPoint.y) == null)
		{
			return true;
		}
		
		Vehicle currentVehicle = (Vehicle)vr.vehicleGrid.getObjectsAtLocation(currentPoint.x, currentPoint.y).get(0);
		
		if(currentVehicle.priority > this.priority)
		{	
			int loc = path.indexOf(currentPoint);
			Point nextPoint = null;
			if (loc != path.size()-1)
			{
				nextPoint = path.get(loc + 1);
			}
			else
			{
				// Try and choose what is not on a path...currently just chooses the first valid condition 
				if(!path.contains(new Point(currentPoint.x + 1, currentPoint.y)) && vr.obstacleGrid.get(currentPoint.x + 1, currentPoint.y) == VehicleRouting.EMPTY_AREA)
				{
					nextPoint = new Point(currentPoint.x + 1, currentPoint.y);
				}
				else if(!path.contains(new Point(currentPoint.x - 1, currentPoint.y)) && vr.obstacleGrid.get(currentPoint.x - 1, currentPoint.y) == VehicleRouting.EMPTY_AREA)
				{
					nextPoint = new Point(currentPoint.x - 1, currentPoint.y);
				}
				else if(!path.contains(new Point(currentPoint.x, currentPoint.y + 1)) && vr.obstacleGrid.get(currentPoint.x, currentPoint.y + 1) == VehicleRouting.EMPTY_AREA)
				{
					nextPoint = new Point(currentPoint.x, currentPoint.y + 1);
				}
				else if(!path.contains(new Point(currentPoint.x, currentPoint.y - 1)) && vr.obstacleGrid.get(currentPoint.x, currentPoint.y - 1) == VehicleRouting.EMPTY_AREA)
				{
					nextPoint = new Point(currentPoint.x, currentPoint.y - 1);
				}
				else
				{
					return false;
				}
				
				path = new ArrayList<Point>();
				path.add(currentPoint);
				path.add(nextPoint);
			}
			
			
			if(pushBack(nextPoint, path, state))
			{
				vr.vehicleGrid.setObjectLocation(currentVehicle, new Int2D(nextPoint));
				return true;
			}
		}
		return false;
	}


	public final void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		if (hasItem) {
			graphics.setColor(itemColor);
		} else {
			graphics.setColor(noItemColor);
		}

		int x = (int) (info.draw.x - info.draw.width / 2.0);
		int y = (int) (info.draw.y - info.draw.height / 2.0);
		int width = (int) (info.draw.width);
		int height = (int) (info.draw.height);
		graphics.fillOval(x, y, width, height);

	}

	public boolean hasItem() {
		return hasItem;
	}

	public void setItem(boolean val) {
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
	
	public void setStats(Statistics stats) {
		this.stats = stats;
	}
	
	public int getStep() {
		return step;
	}
}