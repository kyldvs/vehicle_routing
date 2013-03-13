package sim.app.vehicleRouting;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Scheduler {
	public static void greedy(List<Vehicle> vehicles, List<Job> unassignedJobs, HashMap<Vehicle, PriorityQueue<Job>> assignments)
	{
		if(unassignedJobs != null)
		{				
			int minWorkload = Integer.MAX_VALUE;
			int currWorkload;
			Vehicle minLoadedVehicle = vehicles.get(0);
			PriorityQueue<Job> currJobs;
			
			for(Job j : unassignedJobs)
			{
				for(Vehicle v : vehicles)
				{
					currJobs = assignments.get(v);
					if(currJobs != null)
					{
						currWorkload = 0;
						for(Job currJ: currJobs)
						{
							currWorkload += currJ.getWeight();
						}
						if(currWorkload < minWorkload)
						{
							minWorkload = currWorkload;
							minLoadedVehicle = v;
						}
					}
					else
					{
						assignments.put(v, new PriorityQueue<Job>());
						minWorkload = 0;
						minLoadedVehicle = v;
					}
				}
				assignments.get(minLoadedVehicle).add(j);
				minLoadedVehicle = vehicles.get(0);
				minWorkload = Integer.MAX_VALUE;
			}
			unassignedJobs.clear();
		}
	}
	
	public static void packing(List<Vehicle> vehicles, List<Job> unassignedJobs, HashMap<Vehicle, PriorityQueue<Job>> assignments)
	{
		
	}
}
