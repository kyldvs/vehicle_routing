package sim.app.packing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bin<T> {

	public final double capacity;
	
	private double currentWeight;
	private Set<Item<T>> items;
	
	public Bin(double capacity) {
		this.capacity = capacity;
		this.currentWeight = 0;
		this.items = new HashSet<Item<T>>();
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
	
	public List<Item<T>> items() {
		return new ArrayList<Item<T>>(items);
	}
	
	public List<T> keys() {
		List<T> list = new ArrayList<T>();
		for (Item<T> item : items) {
			list.add(item.data);
		}
		return list;
	}
	
	public boolean isEmpty() {
		return items.isEmpty();
	}
}
