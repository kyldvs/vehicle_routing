package sim.app.vehicleRouting;

import sim.app.geom.Rect;

public class Destination extends Rect {
	
	private boolean assigned = false;
	private Vehicle assignedVehicle;
	
	public Destination(int xMin, int xMax, int yMin, int yMax) {
		super(xMin, xMax, yMin, yMax);
	}

	public Vehicle getAssignedVehicle() {
		return this.assignedVehicle;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
}
