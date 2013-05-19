package sim.app.topo;

import sim.app.vehicleRouting.VehicleRouting;

public interface Topology {

	void sources(VehicleRouting vr);
	void destinations(VehicleRouting vr);
	void vehicles(VehicleRouting vr);
	
}
