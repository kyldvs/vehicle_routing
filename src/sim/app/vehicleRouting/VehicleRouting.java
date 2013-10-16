package sim.app.vehicleRouting;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sim.app.ordering.Ordering;
import sim.app.ordering.RandomOrdering;
import sim.app.ordering.SAOrdering;
import sim.app.packing.Bin;
import sim.app.packing.FirstFit;
import sim.app.packing.Packing;
import sim.app.packing.PackingAlgorithm;
import sim.app.packing.PackingAlgorithms;
import sim.app.stats.Statistics;
import sim.app.topo.Topology;
import sim.app.topo.Topos;
import sim.engine.MakesSimState;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.IntGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

import com.google.common.base.Function;

@SuppressWarnings("serial")
public class VehicleRouting extends SimState
{
	// Immutable
	public static final int GRID_HEIGHT = 110;
	public static final int GRID_WIDTH = 101;
	
	public static final int EMPTY_AREA = 0;
	public static final int OBSTACLE_AREA = 1;
	public static final int SOURCE_AREA = 2;
	public static final int DESTINATION_AREA = 3;
	
	public static final int NUM_JOBS = 200;
	
	// SimState variables
	public final IntGrid2D sourceGrid = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT, EMPTY_AREA);
	public final IntGrid2D obstacleGrid = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT, EMPTY_AREA);
	public final IntGrid2D destinationGrid = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT, EMPTY_AREA);
	public final SparseGrid2D vehicleGrid = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
	
	private final List<Vehicle> vehicles = new ArrayList<Vehicle>();
	private final List<Source> sources = new ArrayList<Source>();
	private final List<Destination> destinations = new ArrayList<Destination>();
	private final List<Job> unassignedJobs = new ArrayList<Job>();

	// Mutable
	private final Map<Vehicle, List<Job>> assignments = new HashMap<Vehicle, List<Job>>();
	
	// Changes to algorithm
	private final PackingAlgorithm<Job> scheduler;
	private final Topology topology;
	private final Ordering ordering;
	
	private final Statistics stats;
	
	public VehicleRouting(long seed)
	{ 
		super(seed);
		this.scheduler = new FirstFit<Job>();
		this.topology = Topos.one();
		this.ordering = new SAOrdering();
		
		this.stats = new Statistics();
	}
	
	@Override
	public void kill() {
		System.out.printf("%d\t%d\t%d\n", stats.getJobsCompleted(), stats.getSteps(), stats.getStuck());
		super.kill();
	}

	public void start()
	{
		super.start();

		topology.sources(this);
		topology.destinations(this);
		topology.vehicles(this);
		
		fillSources();
		fillDestinations();
		
		initializeUnassignedJobs();

		scheduleJobs();
		ordering.order(assignments);
	}
	

	private void scheduleJobs() {
		// Schedule the jobs
		Map<Job, Double> items = new HashMap<Job, Double>();
		for (Job j : unassignedJobs) {
			items.put(j, (double) j.getWeight());
		}
		
		double capacity = PackingAlgorithms.minimizeCapacity(vehicles.size(), scheduler, items);
		Packing<Job> packing = scheduler.pack(items, capacity);
		
		int i = 0;
		for (Bin<Job> bin : packing.bins()) {
			if (i >= vehicles.size()) break;
			assignments.put(vehicles.get(i++), new ArrayList<Job>(bin.keys()));
		}
	}
	
	private void initializeUnassignedJobs()
	{
		for(int i = 0; i < NUM_JOBS; i++)
		{
			Source s = sources.get(random.nextInt(sources.size()));
			Destination d = destinations.get(random.nextInt(destinations.size()));
			unassignedJobs.add(new Job(s, d));
		}
	}
	
	public void addDestination(Destination d) {
		destinations.add(d);
	}
	
	private void fillDestinations() {
		for(Destination d : destinations)
		{
			for( int x = d.getxMin() ; x <= d.getxMax() ; x++ )
			{
				for( int y = d.getyMin() ; y <= d.getyMax() ; y++ )
				{
					destinationGrid.field[x][y] = DESTINATION_AREA;
				}
			}
		}
	}
	
	public void addSource(Source s) {
		sources.add(s);
	}
	
	private void fillSources() {
		for(Source s : sources)
		{
			for( int x = s.getxMin() ; x <= s.getxMax() ; x++ )
			{
				for( int y = s.getyMin() ; y <= s.getyMax() ; y++ )
				{
					sourceGrid.field[x][y] = SOURCE_AREA;
					obstacleGrid.field[x][y] = OBSTACLE_AREA;
				}
			}
		}
	}
	
	public void addVehicle(int x, int y) {
		Vehicle v = new Vehicle();
		vehicleGrid.setObjectLocation(v, x, y);
		Stoppable stop = schedule.scheduleRepeating(Schedule.EPOCH, 0, v, 1);
		v.setStoppable(stop);
		v.setStats(stats);
		vehicles.add(v);
	}
	
	public int getStuck() {
		return stats.getStuck();
	}
	
	public static void main(String[] args)
	{
		for (int i = 0; i < 10; i++)
		doLoop(new MakesSimState() {
			@SuppressWarnings("rawtypes")
			@Override
			public Class simulationClass() {
				return VehicleRouting.class;
			}
			
			@Override
			public SimState newInstance(long seed, String[] args) {
				return new VehicleRouting(seed);
			}
		}, args);
		
		System.exit(0);
	}

	public List<Point> findPath(Point start, Function<Point, Boolean> goal, boolean considerVehicles)
	{
//		if (!considerVehicles) {
//			List<Point> ignore = bfsIgnoreVehicles(start, goal);
//			Point p = ignore.get(0);
//			boolean[][] vehicleArray = get2DVehicleArray();
//			if (vehicleArray[p.x][p.y]) {
//				return null;
//			} else {
//				return ignore;
//			}
//		} else {
			return bfs(start, goal);
//		}
	}
	
	/**
	 * Does a bfs to find the best path from source to a point
	 * adjacent to dest, avoiding obstacles and other vehicles.
	 * 
	 * The path returned is exclusive, it does not include the
	 * source point or destination point
	 */
	public List<Point> bfsIgnoreVehicles(Point start, Function<Point, Boolean> goal) {
		boolean[][] v = new boolean[GRID_WIDTH][GRID_HEIGHT];
		LinkedList<Node<Point>> q = new LinkedList<Node<Point>>();
		q.add(new Node<Point>(start));

		while(!q.isEmpty()) {
			Node<Point> n = q.remove();
			Point p = n.data;
			if (p.x < 0 || p.x >= GRID_WIDTH ||
				p.y < 0 || p.y >= GRID_HEIGHT || 
				v[p.x][p.y] ||
				obstacleGrid.get(p.x, p.y) != EMPTY_AREA) {
				continue;
			}
			v[p.x][p.y] = true;
			
			if (goal.apply(p)) {
				LinkedList<Point> path = new LinkedList<Point>();
				while(n.parent != null) {
					path.addFirst(n.data);
					n = n.parent;
				}
				return path;
			}
			
			q.add(new Node<Point>(new Point(p.x + 1, p.y), n));
			q.add(new Node<Point>(new Point(p.x - 1, p.y), n));
			q.add(new Node<Point>(new Point(p.x, p.y + 1), n));
			q.add(new Node<Point>(new Point(p.x, p.y - 1), n));
		}
		
		return null;
	}
	
	/**
	 * Does a bfs to find the best path from source to a point
	 * adjacent to dest, avoiding obstacles and other vehicles.
	 * 
	 * The path returned is exclusive, it does not include the
	 * source point or destination point
	 */
	public List<Point> bfs(Point start, Function<Point, Boolean> goal) {
		boolean[][] v = new boolean[GRID_WIDTH][GRID_HEIGHT];
		boolean[][] vehicleArray = get2DVehicleArray();
		LinkedList<Node<Point>> q = new LinkedList<Node<Point>>();
		q.add(new Node<Point>(start));

		while(!q.isEmpty()) {
			Node<Point> n = q.remove();
			Point p = n.data;
			if (p.x < 0 || p.x >= GRID_WIDTH ||
				p.y < 0 || p.y >= GRID_HEIGHT || 
				v[p.x][p.y] ||
				obstacleGrid.get(p.x, p.y) != EMPTY_AREA ||
				(!p.equals(start) && vehicleArray[p.x][p.y])) {
				continue;
			}
			v[p.x][p.y] = true;
			
			if (goal.apply(p)) {
				LinkedList<Point> path = new LinkedList<Point>();
				while(n.parent != null) {
					path.addFirst(n.data);
					n = n.parent;
				}
				return path;
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
	
	public Job getJob(Vehicle v)
	{
		if(assignments.get(v) == null || assignments.get(v).isEmpty())
		{
			return null;
		}
		return assignments.get(v).remove(0);
	}
}