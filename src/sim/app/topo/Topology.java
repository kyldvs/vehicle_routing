package sim.app.topo;


public interface Topology {

	// Immutable
	boolean[][] getSources();
	boolean[][] getDestinations();
	boolean[][] getObstacles();
	boolean[][] getVehicles();

}
