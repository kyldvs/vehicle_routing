package sim.app.stats;

import java.awt.Point;
import java.util.Map;

import sim.app.vehicleRouting.Job;
import sim.app.vehicleRouting.Vehicle;

import com.google.common.collect.Maps;

public class Statistics {

	private int steps;
	private int stuck;
	private int jobsCompleted;
	
	private Map<Vehicle, JobRoutes> routes; 
	
	public Statistics() {
		this.steps = 0;
		this.stuck = 0;
		this.jobsCompleted = 0;
		this.routes = Maps.newHashMap();
	}
	
	public int getSteps() {
		return steps;
	}
	
	public int getJobsCompleted() {
		return jobsCompleted;
	}
	
	public void updateSteps(int steps) {
		this.steps = Math.max(this.steps, steps);
	}
	
	public void stuck() {
		stuck++;
	}
	
	public int getStuck() {
		return stuck;
	}
	
	public void deliver(Vehicle v, Job job, Point p) {
		if (routes.get(v) == null) {
			routes.put(v, new JobRoutes());
		}
		
		routes.get(v).end(v, job, p);
		jobsCompleted++;
	}
	
	public void pickup(Vehicle v, Job job, Point p) {
		if (routes.get(v) == null) {
			routes.put(v, new JobRoutes());
		}
		
		routes.get(v).start(v, job, p);
	}
}
