package sim.app.util;

import java.awt.Point;

import sim.util.Int2D;

public class Utils {
	
	public static int manhattanDistance(Point a, Point b) {
		return manhattanDistance(a.x, a.y, b.x, b.y);
	}
	
	public static int manhattanDistance(Int2D a, Int2D b) {
		return manhattanDistance(a.x, a.y, b.x, b.y);
	}
	
	public static int manhattanDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1-x2) + Math.abs(y1-y2);
	}
	
	public static boolean equals(Object o1, Object o2) {
		if (o1 == o2) return true;
		if (o1 == null || o2 == null) return false;
		return o1.equals(o2);
	}
	
}
