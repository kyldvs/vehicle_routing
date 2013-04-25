package sim.app.topo;

import java.util.List;

import sim.app.vehicleRouting.Destination;
import sim.app.vehicleRouting.Source;
import sim.app.vehicleRouting.Vehicle;
import sim.field.grid.IntGrid2D;

public interface Topology {

	void buildSources(TopoVariables variables, IntGrid2D obstacleGrid, List<Source> sources, IntGrid2D sourceGrid);
	void buildDestinations(TopoVariables variables, List<Destination> destinations, IntGrid2D destinationGrid);
	void buildVehicles(TopoVariables variables, List<Vehicle> vehicles, IntGrid2D vehicleGrid);
	
}
