package sim.app.geom;

import java.awt.Point;

import com.google.common.base.Function;

public class Rect {

	protected int xMin; 
	protected int xMax;
	protected int yMin;
	protected int yMax;
	
	public Point point = new Point();

	public Rect(int xMin, int xMax, int yMin, int yMax) {
		this.xMax = xMax;
		this.xMin = xMin;
		this.yMax = yMax;
		this.yMin = yMin;
		this.point.x = (xMin + xMax)/2;
		this.point.y = (yMin + yMax)/2;
	}

	public int getxMin() {
		return xMin;
	}

	public int getxMax() {
		return xMax;
	}

	public int getyMin() {
		return yMin;
	}

	public int getyMax() {
		return yMax;
	}

	public boolean adjacent(Point p) {
		return  (p.x + 1 == xMin && p.y >= yMin && p.y <= yMax) ||
				(p.x - 1 == xMax && p.y >= yMin && p.y <= yMax) ||
				(p.y + 1 == yMin && p.x >= xMin && p.x <= xMax) ||
				(p.y - 1 == yMax && p.x >= xMin && p.x <= xMax);
	}
	
	public Function<Point, Boolean> adjacentFunction() {
		return new Function<Point, Boolean>() {
			@Override
			public Boolean apply(Point p) {
				return adjacent(p);
			}
		};
	}
}
