package sim.app.ordering;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import sim.app.vehicleRouting.Job;
import sim.app.vehicleRouting.Vehicle;

public class RandomOrdering implements Ordering{

	@Override
	public void order(Map<Vehicle, List<Job>> ass) {
		for (List<Job> l : ass.values()) {
			Collections.shuffle(l);
		}
	}

}
