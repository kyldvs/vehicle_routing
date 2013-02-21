package sim.app.vehicleRouting;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Source {
	
	private int xMin; 
	private int xMax;
	private int yMin;
	private int yMax;
	
	public Point point = new Point();
	
	public Source(int xMin, int xMax, int yMin, int yMax) {
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

	public Set<Point> points() {
		Set<Point> boundary = new HashSet<>();
		for (int i = xMin; i <= xMax; i++) {
			boundary.add(new Point(i, yMin));
			boundary.add(new Point(i, yMax));
		}
		for (int i = yMin; i <= yMax; i++) {
			boundary.add(new Point(xMin, i));
			boundary.add(new Point(xMax, i));
		}
		return boundary;
	}
}
