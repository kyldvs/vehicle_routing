package sim.app.packing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FirstFit<T> implements PackingAlgorithm<T> {

	@Override
	public Packing<T> pack(Map<T, Double> items, double binCapacity) {
		List<Item<T>> list = new ArrayList<>();
		for (Entry<T, Double> item : items.entrySet()) {
			list.add(new Item<>(item.getKey(), item.getValue()));
		}

		Collections.sort(list);
		Collections.reverse(list);

		Packing<T> packing = new Packing<>();
		Bin<T> b = new Bin<>(binCapacity);

		for (Item<T> item : list) {
			if (!b.add(item)) {
				packing.add(b);
				b = new Bin<>(binCapacity);
				if (!b.add(item)) {
					throw new RuntimeException(String.format(
						"No valid packing. Item \"%s\" has weight %.2f which is greater than the bin capacity of %.2f.",
						item.data, item.weight, binCapacity));
				}
			}
		}

		packing.add(b);
		packing.clean();
		return packing;
	}

}
