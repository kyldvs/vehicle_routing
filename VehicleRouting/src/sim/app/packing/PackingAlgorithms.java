package sim.app.packing;

import java.util.Map;

public class PackingAlgorithms {

	public static boolean packingExists(Map<?, Double> items, double binCapacity) {
		for (double d : items.values()) {
			if (d > binCapacity) {
				return false;
			}
		}
		return true;
	}
	
	public static <T> double minimizeCapacity(int maxBins, PackingAlgorithm<T> pa, Map<T, Double> items) {
		double lo = 0.0, hi = 0.0, mid = 0.0;
		for (double d : items.values()) {
			lo = Math.max(lo, d);
			hi += d;
		}
		
		for (int i = 0; i < 30; i++) {
			mid = (lo + hi) / 2;
			Packing<T> p = pa.pack(items, mid);
			if (p.size() > maxBins) {
				lo = mid;
			} else {
				hi = mid;
			}
			
		}
		
		return Math.ceil(mid);
	}
}
