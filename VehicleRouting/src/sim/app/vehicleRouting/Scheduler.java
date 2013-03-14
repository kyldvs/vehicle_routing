package sim.app.vehicleRouting;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public interface Scheduler {
	public void run(List<Vehicle> vehicles, List<Job> unassignedJobs, HashMap<Vehicle, PriorityQueue<Job>> assignments);
}
