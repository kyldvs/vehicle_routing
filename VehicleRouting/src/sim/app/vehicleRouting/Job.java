package sim.app.vehicleRouting;

import sim.app.util.Utils;


public class Job implements Comparable<Job> {
	
	private static int counter = 0;
	private final int uid = counter++;
	
	private Source source;
	private Destination destination;
	private int priority;
	private int weight;

	public Job(Source source, Destination destination, int priority)
	{
			this.setSource(source);
			this.setDestination(destination);
			this.setPriority(priority);
			this.setWeight(Utils.manhattanDistance(source.point, destination.point));
	}
	
	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int compareTo(Job j) {
		return this.getPriority() - j.getPriority();
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Job) {
			return ((Job) obj).uid == uid;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return uid;
	}
	
}
