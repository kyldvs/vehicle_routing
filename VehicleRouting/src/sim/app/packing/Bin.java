package sim.app.packing;

import java.util.Set;

public class Bin<T> {

	public final double capacity;
	
	private double currentWeight;
	private Set<Item<T>> items;
	
	public Bin(double capacity) {
		this.capacity = capacity;
		this.currentWeight = 0;
	}
	
	public boolean add(Item<T> item) {
		if (currentWeight + item.weight < capacity) {
			items.add(item);
			currentWeight += item.weight;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isEmpty() {
		return items.isEmpty();
	}
}
