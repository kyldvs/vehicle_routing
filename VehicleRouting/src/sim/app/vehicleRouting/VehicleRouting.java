package sim.app.vehicleRouting;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import sim.app.packing.Bin;
import sim.app.packing.FirstFit;
import sim.app.packing.Packing;
import sim.app.packing.PackingAlgorithm;
import sim.app.packing.PackingAlgorithms;
import sim.app.topo.TopoVariables;
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
	//############################################//
	//############################################//
	
	private Map<Vehicle, List<Job>> assignments = new HashMap<Vehicle, List<Job>>();
	public static final Random random = new Random();
	
	public int collisions = 0;
	
	public final int GRID_HEIGHT 		= 110;
	public final int GRID_WIDTH 		= 101;

	public final int EMPTY_AREA			= 0;
	public final int OBSTACLE_AREA		= 1;
	public final int SOURCE_AREA 		= 1;
	public final int DESTINATION_AREA 	= 2;
	
	//############################################//
	//############ CHANGABLE VARIABLES ############//
	
	public final int NUM_JOBS			= 1000;
	public final double LAMBDA			= -0.04;
	public final int CURVE_TIGHTNESS	= 40;
	
	public int numVehicles 				= 20;
	public int numDestinations 			= 12;
	
	public PackingAlgorithm<Job> scheduler = new FirstFit<Job>();
	public TopoVariables variables = new TopoVariables(numVehicles, numDestinations, GRID_HEIGHT, GRID_WIDTH);
	
	public IntGrid2D 	sourceGrid 		= new IntGrid2D(GRID_WIDTH, GRID_HEIGHT, EMPTY_AREA);
	public IntGrid2D 	obstacleGrid 	= new IntGrid2D(GRID_WIDTH, GRID_HEIGHT, EMPTY_AREA);
	public IntGrid2D 	destinationGrid = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT, EMPTY_AREA);
	public SparseGrid2D vehicleGrid 	= new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
	
	List<Vehicle> 		vehicles 		= new ArrayList<Vehicle>();
	List<Source> 		sources 		= new ArrayList<Source>();
	List<Destination> 	destinations 	= new ArrayList<Destination>();
	
	List<Job> 			unassignedJobs 	= new ArrayList<Job>();
	
	public VehicleRouting(long seed)
	{ 
		super(seed);
	}
	
	public int getCollisions()
	{
		return collisions;
	}
	
	public void start()
	{
		super.start();

		// read in data and create structures
		initializeVehicles();
		initializeSources();
		initializeDestinations();
		initializeObstacles();
		
		initializeVehicleLocations();
		
		initializeUnassignedJobs();

		scheduleJobs();
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
		int rand_x = 0, rand_y = 0;
		for(int i = 0; i < NUM_JOBS; i++)
		{
			rand_x = (int)((random.nextGaussian())*CURVE_TIGHTNESS + GRID_WIDTH / 2.0);
			rand_y = GRID_HEIGHT - (int)((Math.log(1-random.nextDouble()))/LAMBDA);
			unassignedJobs.add(new Job(getNearestSource(rand_x, rand_y), 
										destinations.get(random.nextInt(destinations.size()))
										, random.nextInt())
							  );
		}
	}
	
	private Source getNearestSource(int x, int y)
	{
		Source closest = sources.get(0);
		double minDist = Double.MAX_VALUE;
		double tempDist = 0;
		for(Source s : sources)
		{
			tempDist = Math.sqrt(Math.pow((x-s.point.x),2.0) + Math.pow((y - s.point.y),2.0));
			if(tempDist < minDist)
			{
				closest = s;
				minDist = tempDist;
			}
		}
		return closest;
	}
	
	private void initializeVehicles()
	{
		for (int i = 0; i < numVehicles; i++) {
			vehicles.add(new Vehicle());
		}
	}
	
	private void initializeDestinations()
	{
//####################################################################################################
//		TOPO #1
		int startx = GRID_WIDTH / 2 - (numDestinations / 2 * 4);
		for (int i = 0; i < numDestinations; i++)
		{
			destinations.add(new Destination(startx + (i * 4), startx + (i * 4) + 2, GRID_HEIGHT - 6, GRID_HEIGHT - 4));
		}
//####################################################################################################
		
//####################################################################################################
//		TOPO 2
//		int startx = GRID_WIDTH / 2 - (numDestinations / 4 * 4);
//		for (int i = 0; i < numDestinations/2; i++)
//		{
//			destinations.add(new Destination(startx + (i * 4), startx + (i * 4) + 2, GRID_HEIGHT - 6, GRID_HEIGHT - 4));
//		}
//		
//		startx = GRID_WIDTH / 2 - (numDestinations / 4 * 4);
//		for (int i = 0; i < numDestinations/2; i++)
//		{
//			destinations.add(new Destination(startx + (i * 4), startx + (i * 4) + 2, 4, 6));
//		}
//####################################################################################################
		
//####################################################################################################
//		TOPO 3
//		int starty = GRID_HEIGHT / 2 - (numDestinations / 2 * 8);
//		for (int i = 0; i < numDestinations; i++)
//		{
//			destinations.add(new Destination(GRID_WIDTH/2 - 1, GRID_WIDTH/2 + 1, starty + (i * 8), starty + (i * 8) + 2));
//		}
//####################################################################################################		
		
//####################################################################################################
//		TOPO 4
//		int starty = GRID_HEIGHT / 2 - (numDestinations / 2 * 8);
//		for (int i = 0; i < numDestinations; i++)
//		{
//			destinations.add(new Destination(GRID_WIDTH - 4, GRID_WIDTH - 2, starty + (i * 8), starty + (i * 8) + 2));
//		}
//		
//		starty = GRID_HEIGHT / 2 - (numDestinations / 2 * 8);
//		for (int i = 0; i < numDestinations; i++)
//		{
//			destinations.add(new Destination(2, 4, starty + (i * 8), starty + (i * 8) + 2));
//		}
//
//####################################################################################################		
		
		
		
		
		
		
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
	
	private void initializeSources()
	{
//####################################################################################################
// topo 1
		for( int x = 0 ; x < GRID_WIDTH ; x++ )
		{
			for( int y = 0 ; y < GRID_HEIGHT ; y++ )
			{
				if (x % 2 == 0 && y >= 2 && y < GRID_HEIGHT - 10) {
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
					sourceGrid.field[x][y] = SOURCE_AREA;
				}
			}
		}
	}
//####################################################################################################
		
//####################################################################################################
//topo 2
//		for( int x = 0 ; x < GRID_WIDTH ; x++ )
//		{
//			for( int y = 0 ; y < GRID_HEIGHT ; y++ )
//			{
//				if (y % 2 == 0 && y < GRID_HEIGHT - 10 && y > 10 && ((x < GRID_WIDTH - 2 && x > GRID_WIDTH/2 + 5)||(x <= GRID_WIDTH/2 - 5 && x >= 0 + 2))) {
//					sources.add(new Source(x, x, y, y));
//				}
//			}
//		}
//
//		for(Source s : sources)
//		{
//			for( int x = s.getxMin() ; x <= s.getxMax() ; x++ )
//			{
//				for( int y = s.getyMin() ; y <= s.getyMax() ; y++ )
//				{
//					sourceGrid.field[x][y] = SOURCE_AREA;
//				}
//			}
//		}
//	}
//####################################################################################################
		
//####################################################################################################
//topo 3
//				for( int x = 0 ; x < GRID_WIDTH ; x++ )
//				{
//					for( int y = 0 ; y < GRID_HEIGHT ; y++ )
//					{
//						if (y % 2 == 0 && ((x < GRID_WIDTH - 2 && x > GRID_WIDTH/2 + 5)||(x <= GRID_WIDTH/2 - 5 && x >= 0 + 2))) {
//							sources.add(new Source(x, x, y, y));
//						}
//					}
//				}
//
//				for(Source s : sources)
//				{
//					for( int x = s.getxMin() ; x <= s.getxMax() ; x++ )
//					{
//						for( int y = s.getyMin() ; y <= s.getyMax() ; y++ )
//						{
//							sourceGrid.field[x][y] = SOURCE_AREA;
//						}
//					}
//				}
//			}
//####################################################################################################
		
//####################################################################################################
//topo 4
//				for( int x = 0 ; x < GRID_WIDTH ; x++ )
//				{
//					for( int y = 0 ; y < GRID_HEIGHT ; y++ )
//					{
//						if (y % 2 == 0 && x < GRID_WIDTH - 8 && x >= 0 + 8) {
//							sources.add(new Source(x, x, y, y));
//						}
//					}
//				}
//
//				for(Source s : sources)
//				{
//					for( int x = s.getxMin() ; x <= s.getxMax() ; x++ )
//					{
//						for( int y = s.getyMin() ; y <= s.getyMax() ; y++ )
//						{
//							sourceGrid.field[x][y] = SOURCE_AREA;
//						}
//					}
//				}
//			}
//####################################################################################################
	
	private void initializeObstacles()
	{
//####################################################################################################
// topo 1
		for( int x = 0 ; x < GRID_WIDTH ; x++ )
		{
			for( int y = 0 ; y < GRID_HEIGHT ; y++ )
			{
				if (x % 2 == 0 && y >= 2 && y < GRID_HEIGHT - 10) {
					obstacleGrid.field[x][y] = OBSTACLE_AREA;
				}
			}
		}
	}
//####################################################################################################
		

//####################################################################################################
// Topo 2
//		for( int x = 0 ; x < GRID_WIDTH ; x++ )
//		{
//			for( int y = 0 ; y < GRID_HEIGHT ; y++ )
//			{
//				if (y % 2 == 0 && y < GRID_HEIGHT - 10 && y > 10 && ((x < GRID_WIDTH - 2 && x > GRID_WIDTH/2 + 5)||(x <= GRID_WIDTH/2 - 5 && x >= 0 + 2))) {
//					obstacleGrid.field[x][y] = OBSTACLE_AREA;
//				}
//			}
//		}
//	}
//####################################################################################################
	
//####################################################################################################
// Topo 3
//		for( int x = 0 ; x < GRID_WIDTH ; x++ )
//		{
//			for( int y = 0 ; y < GRID_HEIGHT ; y++ )
//			{
//				if (y % 2 == 0 && ((x < GRID_WIDTH - 2 && x > GRID_WIDTH/2 + 5)||(x <= GRID_WIDTH/2 - 5 && x >= 0 + 2))) {
//					obstacleGrid.field[x][y] = OBSTACLE_AREA;
//				}
//			}
//		}
//	}
//####################################################################################################

//####################################################################################################
// Topo 4
//		for( int x = 0 ; x < GRID_WIDTH ; x++ )
//		{
//			for( int y = 0 ; y < GRID_HEIGHT ; y++ )
//			{
//				if (y % 2 == 0 && x < GRID_WIDTH - 8 && x >= 0 + 8) {
//					obstacleGrid.field[x][y] = OBSTACLE_AREA;
//				}
//			}
//		}
//	}
//####################################################################################################
	
	private void initializeVehicleLocations()
	{
		// initialize vehicle locations
		int vx = 1;
		int vy = 101;
		for(Vehicle v : vehicles)
		{
			vehicleGrid.setObjectLocation(v, vx, vy);
			Stoppable stop = schedule.scheduleRepeating(Schedule.EPOCH, 0, v, 1);
			v.setStoppable(stop);
			vx += 2;
			if (vx >= GRID_WIDTH - 1) 
			{
				vy += 2;
				vx = 1;
			}
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

	public List<Point> findPath(Point start, Function<Point, Boolean> goal, boolean considerVehicles)
	{
		if (!considerVehicles) {
			List<Point> ignore = bfsIgnoreVehicles(start, goal);
			Point p = ignore.get(0);
			boolean[][] vehicleArray = et2DVehicleArray();
			if (vehicleArray[p.x][p.y]) {
				return null;
			} else {
				return ignore;
			}
		} else {
			return bfs(start, goal);
		}
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
		boolean[][] vehicleArray = et2DVehicleArray();
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
	
	public boolean[][] et2DVehicleArray() {
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