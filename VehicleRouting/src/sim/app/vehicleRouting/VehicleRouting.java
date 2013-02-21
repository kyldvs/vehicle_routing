package sim.app.vehicleRouting;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.field.grid.IntGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

@SuppressWarnings("serial")
public class VehicleRouting extends SimState
{
	public static final Random random = new Random();
	
	public final int GRID_HEIGHT 	= 100;
	public final int GRID_WIDTH 	= 101;

	public final int EMPTY_AREA			= 0;
	public final int OBSTACLE_AREA		= 1;
	public final int SOURCE_AREA 		= 1;
	public final int DESTINATION_AREA 	= 2;

	public int numVehicles 		= 40;
	public int numDestinations 	= 5;

	public IntGrid2D 	source 		= new IntGrid2D(GRID_WIDTH, GRID_HEIGHT, EMPTY_AREA);
	public IntGrid2D 	destination = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT, EMPTY_AREA);
	public IntGrid2D 	obstacles 	= new IntGrid2D(GRID_WIDTH, GRID_HEIGHT, EMPTY_AREA);
	public SparseGrid2D vehicleGrid = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);

	List<Vehicle> 		vehicles 		= new ArrayList<Vehicle>();
	List<Source> 		sources 		= new ArrayList<Source>();
	List<Destination> 	destinations 	= new ArrayList<Destination>();

	public VehicleRouting(long seed)
	{ 
		super(seed);
	}

	public void start()
	{
		// clear out the schedule
		super.start();

		// read in data and create structures
		int startx = GRID_WIDTH / 2 - (numDestinations / 2 * 4);
		for (int i = 0; i < numDestinations; i++) {
			destinations.add(new Destination(startx + (i * 4), startx + (i * 4) + 2, GRID_HEIGHT - 6, GRID_HEIGHT - 4));
		}

		for (int i = 0; i < numVehicles; i++) {
			vehicles.add(new Vehicle());
		}

		// define topology
		for( int x = 0 ; x < GRID_WIDTH ; x++ )
		{
			for( int y = 0 ; y < GRID_HEIGHT ; y++ )
			{
				if (x % 2 == 0 && y >= 2 && y < GRID_HEIGHT - 10) {
					obstacles.field[x][y] = OBSTACLE_AREA;
					sources.add(new Source(x, x, y, y));
				}
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
			vehicleGrid.setObjectLocation(v, offset, 0);
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

	public Source getRandomSource() {
		if (sources.isEmpty()) {
			return null;
		}
		int index = random.nextInt(sources.size());
		return sources.get(index);
	}
	
	public Destination getRandomDestination() {
		if (destinations.isEmpty()) {
			return null;
		}
		int index = random.nextInt(destinations.size());
		return destinations.get(index);
	}

	/**
	 * Does a bfs to find the best path from source to a point
	 * adjacent to dest, avoiding obstacles and other vehicles.
	 * 
	 * The path returned is exclusive, it does not include the
	 * source point or destination point
	 */
	public List<Point> bfs(Point start, Set<Point> dest) {
		boolean[][] v = new boolean[GRID_WIDTH][GRID_HEIGHT];
		boolean[][] vehicleArray = get2DVehicleArray();
		LinkedList<Node<Point>> q = new LinkedList<>();
		q.add(new Node<Point>(start));

		while(!q.isEmpty()) {
			Node<Point> n = q.remove();
			Point p = n.data;
			if (p.x < 0 || p.x >= GRID_WIDTH ||
				p.y < 0 || p.y >= GRID_HEIGHT || 
				v[p.x][p.y] ||
				obstacles.get(p.x, p.y) != EMPTY_AREA ||
				(!p.equals(start) && vehicleArray[p.x][p.y])) {
				continue;
			}
			v[p.x][p.y] = true;
			for (Point d : dest) {
				if (Utils.manhattanDistance(d, p) == 1) {
					LinkedList<Point> path = new LinkedList<>();
					while(n.parent != null) {
						path.addFirst(n.data);
						n = n.parent;
					}
					return path;
				}
			}
			
			q.add(new Node<Point>(new Point(p.x + 1, p.y), n));
			q.add(new Node<Point>(new Point(p.x - 1, p.y), n));
			q.add(new Node<Point>(new Point(p.x, p.y + 1), n));
			q.add(new Node<Point>(new Point(p.x, p.y - 1), n));
		}
		
		return null;
	}
	
	public boolean[][] get2DVehicleArray() {
		boolean[][] b = new boolean[GRID_WIDTH][GRID_HEIGHT];
		for (Vehicle v : vehicles) {
			Int2D loc = vehicleGrid.getObjectLocation(v);
			if (loc != null) {
				b[loc.x][loc.y] = true;
			}
		}
		return b;
	}

	public static class Node<T> {
		Node<T> parent;
		T data;
		
		public Node(T data) {
			this(data, null);
		}
		
		public Node(T data, Node<T> parent) {
			this.data = data;
			this.parent = parent;
		}
	}
}