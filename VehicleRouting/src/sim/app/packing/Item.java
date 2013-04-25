package sim.app.packing;

public class Item<T> {

	public final T data;
	public final double weight;
	
	public Item(T data, double weight) {
		this.data = data;
		this.weight = weight;
	}
	
}
