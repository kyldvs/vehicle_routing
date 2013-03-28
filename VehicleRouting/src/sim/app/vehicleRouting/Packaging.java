package sim.app.vehicleRouting;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Packaging implements Scheduler {

	@Override
	public void run(List<Vehicle> vehicles, List<Job> unassignedJobs, HashMap<Vehicle, PriorityQueue<Job>> assignments)
	{
		PackingAlgorithm packer = new Annealing();
		int currCapacity = 2, prevCapacity = 0, avg, bots = 40;
		boolean feasiblePacking = false;
		Map<Job, Integer> jobWeights = new HashMap<Job, Integer>();
		Map<Collection<Job>, Integer> res = new HashMap<Collection<Job>,Integer>();
		for(Job j : unassignedJobs)
		{
			jobWeights.put(j, j.getWeight());
		}
		
		while(res.size()>bots || !feasiblePacking)
		{
			prevCapacity = currCapacity;
			currCapacity = (int) Math.pow(currCapacity, 2);
			feasiblePacking = packer.pack(currCapacity, jobWeights, res);
		}
		
		Map<Collection<Job>, Integer> checkAbove = new HashMap<Collection<Job>,Integer>();
		packer.pack(currCapacity-1, jobWeights, checkAbove);
		feasiblePacking = packer.pack(currCapacity, jobWeights, res);
		while(!(res.size()<=bots) || !(checkAbove.size()>=bots) || !feasiblePacking)
		{
//			if(Does not exist)
			
			if(res.size()<bots)
			{
				// Decrease capacity
				if(prevCapacity<currCapacity)
				{
					avg = (prevCapacity + currCapacity)/2;
					prevCapacity = currCapacity;
					currCapacity = avg;
				}
				else
				{
					avg = (2 + currCapacity)/2;
					prevCapacity = currCapacity;
					currCapacity = avg;					
				}
			}
			
			if(res.size()>bots)
			{
				// increase capacity
				if(prevCapacity>currCapacity)
				{
					avg = (prevCapacity + currCapacity)/2;
					prevCapacity = currCapacity;
					currCapacity = avg;
				}
				else
				{
					avg = ( (int) Math.pow(currCapacity, 2) + currCapacity)/2;
					prevCapacity = currCapacity;
					currCapacity = avg;					
				}
			}

			checkAbove = new HashMap<Collection<Job>,Integer>();
			packer.pack(currCapacity-1, jobWeights, checkAbove);
			feasiblePacking = packer.pack(currCapacity, jobWeights, res);			
		}
		
		// put res into assignments according to some priority
		int count = 0;
		for(Collection<Job> collection : res.keySet())
		{
			assignments.put(vehicles.get(count), new PriorityQueue<Job>());
			for(Job j : collection)
			{
				assignments.get(vehicles.get(count)).add(j);
			}
			count++;
		}
	}
}
