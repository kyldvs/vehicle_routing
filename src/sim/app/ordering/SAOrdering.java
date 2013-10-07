package sim.app.ordering;

import java.util.List;
import java.util.Map;
import java.util.Random;

import sim.app.util.Utils;
import sim.app.vehicleRouting.Job;
import sim.app.vehicleRouting.Vehicle;

import com.google.common.collect.Lists;

public class SAOrdering implements Ordering {

	@Override
	public void order(Map<Vehicle, List<Job>> ass) {
		Random r = new Random();
		List<Vehicle> vehicles = Lists.newArrayList(ass.keySet());
		double f = fitness(vehicles, ass);
		for (int i = 0; i < 2 * vehicles.size(); i++) {
			Vehicle v = vehicles.get(r.nextInt(vehicles.size()));
			List<Job> l = ass.get(v);
			for (int j = 0; j < l.size(); j++) {
				for (int k = 0; k < l.size(); k++) {
					if (j != k) {
						Job tmp = l.get(j);
						l.set(j, l.get(k));
						l.set(k, tmp);
						double nf = fitness(vehicles, ass);
						if (nf > f) {
							f = nf;
						} else {
							tmp = l.get(j);
							l.set(j, l.get(k));
							l.set(k, tmp);
						}
					}
				}
			}
		}
	}
	
	private double fitness(List<Vehicle> vehicles, Map<Vehicle, List<Job>> ass) {
		double f = 0.0;
		int max = 0;
		for (Vehicle v : vehicles) {
			max = Math.max(max, ass.get(v).size());
		}
		for (int i = 0; i < max; i++) {
			for (int j = 0; j < vehicles.size(); j++) {
				for (int k = 0; k < vehicles.size(); k++) {
					List<Job> lj = ass.get(vehicles.get(j));
					List<Job> lk = ass.get(vehicles.get(k));
					if (j != k && i < lj.size() && i < lk.size()) {
						Job jobj = lj.get(i);
						Job jobk = lk.get(i);
						f += Math.pow(Utils.manhattanDistance(jobj.getSource().getCenter(), jobk.getSource().getCenter()), 2);
						f += Math.pow(Utils.manhattanDistance(jobj.getDestination().getCenter(), jobk.getDestination().getCenter()), 2);
					}
				}
			}
		}
		return f;
	}

}
