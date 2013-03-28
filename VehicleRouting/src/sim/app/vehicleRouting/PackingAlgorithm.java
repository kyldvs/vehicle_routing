package sim.app.vehicleRouting;
import java.util.Collection;
import java.util.Map;


public interface PackingAlgorithm {
	public <T> boolean pack(int capacity, Map<T, Integer> weights, Map<Collection<T>, Integer> res);
}
