package sim.app.vehicleRouting;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Int2D;

@SuppressWarnings("serial")
public class Vehicle extends OvalPortrayal2D implements Steppable
{
	public boolean hasItem = false;

	public Destination dest;
	public Source src;

	private Color noItemColor = Color.blue;
	private Color itemColor = Color.red;

	public void act( final SimState state )
	{
		final VehicleRouting vr = (VehicleRouting) state;

		Int2D loc = vr.vehicleGrid.getObjectLocation(this);

		if (hasItem) {
			if (src == null) {
				src = vr.getRandomSource();
				dest = null;
			} else {
				List<Point> path = vr.bfs(loc.toPoint(), src.points());
				if (path != null) {
					vr.vehicleGrid.setObjectLocation(this, new Int2D(path.get(0)));
					if (path.size() == 1) {
						hasItem = false;
					}
				}
			}
		} else {
			if (dest == null) {
				dest = vr.getRandomDestination();
				src = null;
			} else {
				List<Point> path = vr.bfs(loc.toPoint(), dest.points());
				if (path != null) {
					vr.vehicleGrid.setObjectLocation(this, new Int2D(path.get(0)));
					if (path.size() == 1) {
						hasItem = true;
					}
				}
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