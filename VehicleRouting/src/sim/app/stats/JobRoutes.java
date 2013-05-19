package sim.app.stats;

import java.awt.Point;
import java.util.Map;

import sim.app.vehicleRouting.Job;
import sim.app.vehicleRouting.Vehicle;

import com.google.common.collect.Maps;

public class JobRoutes {

	private Map<Job, Checkpoint> start;
	private Map<Job, Checkpoint> end;
	
	public JobRoutes() {
		start = Maps.newHashMap();
		end = Maps.newHashMap();
	}
	
	public void start(Vehicle v, Job job, Point p) {
		start.put(job, new Checkpoint(v.getStep(), job, p));
	}
	
	public void end(Vehicle v, Job job, Point p) {
		end.put(job, new Checkpoint(v.getStep(), job, p));
	}
	
}
