package sim.app.ordering;

import java.util.List;
import java.util.Map;

import sim.app.vehicleRouting.Job;
import sim.app.vehicleRouting.Vehicle;

public interface Ordering {

	void order(Map<Vehicle, List<Job>> ass);
	
}
