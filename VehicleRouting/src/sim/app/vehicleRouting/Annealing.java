package sim.app.vehicleRouting;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Annealing<T> implements PackingAlgorithm<T> {
	
	@Override
	public boolean pack(int capacity, final Map<T, Integer> weights, final Map<Collection<T>, Integer> result) {

		final int n = weights.size();

		@SuppressWarnings("unchecked")
		final T[] item = (T[]) Array.newInstance(getOne(weights).getClass(), n);
		final int[] weight = new int[n];
		for (int i = 0; i < n; i++) {
			weight[i] = weights.get(item[i]);
		}
		
		// FFD
		List<Set<Integer>> ffdBins = new ArrayList<>();
		List<Integer> ffdTotals = new ArrayList<Integer>();
		ffdBins.add(new HashSet<Integer>());
		ffdTotals.add(0);

		Integer[] ffdItems = new Integer[n];
		for (int i = 0; i < n; i++) {
			ffdItems[i] = i;
		}
		
		Arrays.sort(ffdItems, new Comparator<Integer>() {
			@Override
			public int compare(Integer arg0, Integer arg1) {
				return weight[arg1] - weight[arg0];
			}
		});

		for (int ffdItem : ffdItems) {
			if (weight[ffdItem] > capacity) {
				return false;
			}
			
			boolean added = false;
			for (int i = 0; i < ffdBins.size() && !added; i++) {
				if (ffdTotals.get(i) + weight[ffdItem] <= capacity) {
					ffdTotals.set(i, ffdTotals.get(i) + weight[ffdItem]);
					ffdBins.get(i).add(ffdItem);
					added = true;
				}
				
				if (!added && i == ffdBins.size() - 1) {
					ffdBins.add(new HashSet<Integer>());
				}
			}
		}
		
		// Set up initial instance of the problem, using arrays for easy swapping.
		int[] at = new int[ffdBins.size()]; 		// number of items in each bin
		int[][] bin = new int[ffdBins.size()][n];	// the index of the items in the bins
		
		int binIndex = 0;
		for (Set<Integer> b : ffdBins) {
			for (Integer i : b) {
				bin[binIndex][at[binIndex]++] = i;
			}
			binIndex++;
		}

		// Now time for some annealing
		
		int nloop = 50;
		double K = 0.05, T = 1.0, tred = 0.95;

		for (int ct = 0; ct < nloop; ct++) {
			int[] fill = new int[bin.length];
			double[] w = new double[bin.length];
			for (int i = 0; i < bin.length; i++) {
				for (int j = 0; j < at[i]; j++) {
					fill[i] += bin[i][j];
				}
				w[i] = Math.pow(1.0 + K * ((double) (capacity - fill[i]) / capacity), T);
			}
			
			double obj = 0;
			double[] af = new double[bin.length]; // adjusted fill
			for (int i = 0; i < bin.length; i++) {
				af[i] += w[i] * fill[i];
				obj += Math.pow((double) af[i], 2);
			}
			
			for (int a = 0; a < af.length; a++) {
				for (int b = 0; b < af.length; b++) {
					if (a == b)	continue;
					
					// swap(1,0)
					for (int i = 0; i < at[a]; i++) {
						// possible check
						if (fill[b] + weight[bin[a][i]] <= capacity) {
							double updated = obj - Math.pow((double) af[a] , 2) 
												 - Math.pow((double) af[b] , 2)
												 + Math.pow((double) (af[a] - weight[bin[a][i]] * w[a]) , 2)
												 + Math.pow((double) (af[b] + weight[bin[a][i]] * w[a]) , 2);
						}
					}
					
				}
			}
			
			
			T *= tred;
		}
		
		return true;
	}
	
	private T getOne(Map<T, Integer> map) {
		for (T item : map.keySet()) {
			if (item != null) {
				return item;
			}
		}
		return null;
	}
	
	private class Item {
		int weight;
		double adjusted;
	}
	
	private class Bin {
		int fill;
		List<Item> items = new ArrayList<>();
	}
}
