package sim.app.packing;

import java.util.Map;

public interface PackingAlgorithm<T> {

	Packing<T> pack(Map<T, Double> items, double binCapacity);
	
}
