package sim.app.packing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Packing<T> {

	private List<Bin<T>> packing;
	
	public Packing() {
		packing = new ArrayList<Bin<T>>();
	}
	
	public void add(Bin<T> bin) {
		packing.add(bin);
	}
	
	/**
	 * Removes Empty Bins 
	 */
	public void clean() {
		Iterator<Bin<T>> iter = packing.iterator();
		while(iter.hasNext()) {
			Bin<T> bin = iter.next();
			if (bin.isEmpty()) {
				iter.remove();
			}
		}
	}

	public List<Bin<T>> bins() {
		return new ArrayList<Bin<T>>(packing);
	}
	
	public int size() {
		return packing.size();
	}
	
}
