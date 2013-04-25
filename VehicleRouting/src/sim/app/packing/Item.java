package sim.app.packing;

public class Item<T> implements Comparable<Item<T>> {

	public final T data;
	public final double weight;
	
	public Item(T data, double weight) {
		this.data = data;
		this.weight = weight;
	}

	@Override
	public int compareTo(Item<T> that) {
		return this.weight < that.weight ? -1 : this.weight > that.weight ? 1 : 0;
	}
	
}
