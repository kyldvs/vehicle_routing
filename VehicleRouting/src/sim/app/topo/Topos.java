package sim.app.topo;

import sim.app.vehicleRouting.Destination;
import sim.app.vehicleRouting.Source;
import sim.app.vehicleRouting.VehicleRouting;

public class Topos {

	public static Topology one() {
		return new Topology() {
			private static final int numDestinations = 12;
			private static final int numVehicles = 20;
			
			@Override
			public void vehicles(VehicleRouting vr) {
				int x = 1;
				int y = 101;
				for (int i = 0; i < numVehicles; i++)
				{
					vr.addVehicle(x, y);
					x += 2;
					if (x >= VehicleRouting.GRID_WIDTH - 1) 
					{
						y += 2;
						x = 1;
					}
				}
			}
			
			@Override
			public void sources(VehicleRouting vr) {
				for( int x = 0 ; x < VehicleRouting.GRID_WIDTH ; x++ )
				{
					for( int y = 0 ; y < VehicleRouting.GRID_HEIGHT ; y++ )
					{
						if (x % 2 == 0 && y >= 2 && y < VehicleRouting.GRID_HEIGHT - 10) {
							vr.addSource(new Source(x, x, y, y));
						}
					}
				}
			}
			
			@Override
			public void destinations(VehicleRouting vr) {
				int startx = VehicleRouting.GRID_WIDTH / 2 - (numDestinations / 2 * 4);
				for (int i = 0; i < numDestinations; i++)
				{
					Destination d = new Destination(startx + (i * 4), startx + (i * 4) + 2,
							VehicleRouting.GRID_HEIGHT - 6, VehicleRouting.GRID_HEIGHT - 4);
					vr.addDestination(d);
				}
			}
		};
	}
	
	public static Topology two() {
		return new Topology() {
			private static final int numDestinations = 12;
			private static final int numVehicles = 20;
			
			@Override
			public void vehicles(VehicleRouting vr) {
				int x = 1;
				int y = 101;
				for (int i = 0; i < numVehicles; i++)
				{
					vr.addVehicle(x, y);
					x += 2;
					if (x >= VehicleRouting.GRID_WIDTH - 1) 
					{
						y += 2;
						x = 1;
					}
				}
			}
			
			@Override
			public void sources(VehicleRouting vr) {
				for( int x = 0 ; x < VehicleRouting.GRID_WIDTH ; x++ )
				{
					for( int y = 0 ; y < VehicleRouting.GRID_HEIGHT ; y++ )
					{
						if (y % 2 == 0 && y < VehicleRouting.GRID_HEIGHT - 10 && y > 10 
								&& ((x < VehicleRouting.GRID_WIDTH - 2 && x > VehicleRouting.GRID_WIDTH/2 + 5)
										||(x <= VehicleRouting.GRID_WIDTH/2 - 5 && x >= 0 + 2))) {
							vr.addSource(new Source(x, x, y, y));
						}
					}
				}
			}
			
			@Override
			public void destinations(VehicleRouting vr) {
				int startx = VehicleRouting.GRID_WIDTH / 2 - (numDestinations / 4 * 4);
				for (int i = 0; i < numDestinations/2; i++)
				{
					Destination d = new Destination(startx + (i * 4), startx + (i * 4) + 2, VehicleRouting.GRID_HEIGHT - 6, VehicleRouting.GRID_HEIGHT - 4);
					vr.addDestination(d);
				}
				
				startx = VehicleRouting.GRID_WIDTH / 2 - (numDestinations / 4 * 4);
				for (int i = 0; i < numDestinations/2; i++)
				{
					Destination d = new Destination(startx + (i * 4), startx + (i * 4) + 2, 4, 6);
					vr.addDestination(d);
				}
			}
		};
	}
	
	public static Topology three() {
		return new Topology() {
			private static final int numDestinations = 12;
			private static final int numVehicles = 20;
			
			@Override
			public void vehicles(VehicleRouting vr) {
				int x = 1;
				int y = 101;
				for (int i = 0; i < numVehicles; i++)
				{
					vr.addVehicle(x, y);
					x += 2;
					if (x >= VehicleRouting.GRID_WIDTH - 1) 
					{
						y += 2;
						x = 1;
					}
				}
			}
			
			@Override
			public void sources(VehicleRouting vr) {
				for( int x = 0 ; x < VehicleRouting.GRID_WIDTH ; x++ )
				{
					for( int y = 0 ; y < VehicleRouting.GRID_HEIGHT ; y++ )
					{
						if (y % 2 == 0 && ((x < VehicleRouting.GRID_WIDTH - 2 && x > VehicleRouting.GRID_WIDTH/2 + 5)||(x <= VehicleRouting.GRID_WIDTH/2 - 5 && x >= 0 + 2))) {
							vr.addSource(new Source(x, x, y, y));
						}
					}
				}
			}
			
			@Override
			public void destinations(VehicleRouting vr) {
				int starty = VehicleRouting.GRID_HEIGHT / 2 - (numDestinations / 2 * 8);
				for (int i = 0; i < numDestinations; i++)
				{
					Destination d = new Destination(VehicleRouting.GRID_WIDTH/2 - 1, VehicleRouting.GRID_WIDTH/2 + 1, starty + (i * 8), starty + (i * 8) + 2);
					vr.addDestination(d);
				}
			}
		};
	}
	
	public static Topology four() {
		return new Topology() {
			private static final int numDestinations = 12;
			private static final int numVehicles = 20;
			
			@Override
			public void vehicles(VehicleRouting vr) {
				int x = 1;
				int y = 101;
				for (int i = 0; i < numVehicles; i++)
				{
					vr.addVehicle(x, y);
					x += 2;
					if (x >= VehicleRouting.GRID_WIDTH - 1) 
					{
						y += 2;
						x = 1;
					}
				}
			}
			
			@Override
			public void sources(VehicleRouting vr) {
				for( int x = 0 ; x < VehicleRouting.GRID_WIDTH ; x++ )
				{
					for( int y = 0 ; y < VehicleRouting.GRID_HEIGHT ; y++ )
					{
						if (y % 2 == 0 && x < VehicleRouting.GRID_WIDTH - 8 && x >= 0 + 8) {
							vr.addSource(new Source(x, x, y, y));
						}
					}
				}
			}
			
			@Override
			public void destinations(VehicleRouting vr) {
				int starty = VehicleRouting.GRID_HEIGHT / 2 - (numDestinations / 2 * 8);
				for (int i = 0; i < numDestinations; i++)
				{
					Destination d = new Destination(VehicleRouting.GRID_WIDTH - 4, VehicleRouting.GRID_WIDTH - 2, starty + (i * 8), starty + (i * 8) + 2);
					vr.addDestination(d);
				}
				
				starty = VehicleRouting.GRID_HEIGHT / 2 - (numDestinations / 2 * 8);
				for (int i = 0; i < numDestinations; i++)
				{
					Destination d = new Destination(2, 4, starty + (i * 8), starty + (i * 8) + 2);
					vr.addDestination(d);
				}
			}
		};
	}
	
}
