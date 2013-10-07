package sim.app.stats;

import java.awt.Point;

import sim.app.util.Utils;
import sim.app.vehicleRouting.Job;

public class Checkpoint {

	public final int step;
	public final Job job;
	public final Point p;
	
	public Checkpoint(int step, Job job, Point p) {
		this.step = step;
		this.job = job;
		this.p = p;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Checkpoint) {
			Checkpoint c = (Checkpoint) obj;
			return step == c.step && Utils.equals(job, c.job) && Utils.equals(p, c.p);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return step ^ (job == null ? 0 : job.hashCode()) ^ (p == null ? 0 : p.hashCode());
	}
	
}
