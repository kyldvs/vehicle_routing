package sim.app.topo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.j3d.NioImageBuffer.ImageType;

import sim.app.vehicleRouting.Destination;
import sim.app.vehicleRouting.Source;
import sim.app.vehicleRouting.VehicleRouting;

public class Topologies {
	
	// Writes a topology to a file
	public static void main(String[] args) throws Exception {
		int w = 100;
		int h = 100;
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		// set it all to black
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				image.setRGB(i, j, 0);
			}
		}
		
		// build columns
		for (int i = 10; i <= w - 10; i += 2) {
			for (int j = 30; j < h - 10; j++) {
				image.setRGB(i, j, 0xFFFFFF);
			}
		}
		
		// put down sources
		for (int i = 9; i <= w - 9; i += 2) {
			for (int j = 30; j < h; j++) {
				image.setRGB(i, j, 0x00FF00);
			}
		}
		
		// build lower loop
		for (int i = 5; i <= w - 5; i++) {
			image.setRGB(i, 29, 0xFFFFFF);
			image.setRGB(i, 10, 0xFFFFFF);
		}
		
		// connect loop
		for (int j = 11; j < 29; j++) {
			image.setRGB(5, j, 0xFFFFFF);
			image.setRGB(w - 5, j, 0xFFFFFF);
		}
		
		// build vertical highways
		for (int i = 10; i <= w - 10; i += 5) {
			for (int j = 11; j < 29; j++) {
				image.setRGB(i, j, 0xFFFFFF);
			}
		}
		
		// put down destinations
		for (int i = 9; i <= w - 9; i += 10) {
			image.setRGB(i, 9, 0xFF0000);
		}
		
		// put down some vehicles
		for (int i = 0; i < 30; i++) {
			image.setRGB(20 + i, 10, 0x0000FF);
		}
		
		ImageIO.write(image, "PNG", new File("topo1.png"));
	}

	public static Topology fromImage(String filename) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		final int w = image.getWidth();
		final int h = image.getHeight();
		
		final int[][] colors = new int[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				colors[i][j] = image.getRGB(i, j) & 0xFFFFFF;
			}
		}
		
		return new Topology() {
			
			@Override
			public boolean[][] getSources() {
				boolean[][] sources = new boolean[w][h];
				for (int i = 0; i < w; i++) {
					for (int j = 0; j < h; j++) {
						sources[i][j] = colors[i][j] == 0x00FF00;
					}
				}
				return sources;
			}
			
			@Override
			public boolean[][] getObstacles() {
				boolean[][] obstacles = new boolean[w][h];
				for (int i = 0; i < w; i++) {
					for (int j = 0; j < h; j++) {
						obstacles[i][j] = colors[i][j] == 0x000000;
					}
				}
				return obstacles;
			}
			
			@Override
			public boolean[][] getDestinations() {
				boolean[][] destinations = new boolean[w][h];
				for (int i = 0; i < w; i++) {
					for (int j = 0; j < h; j++) {
						destinations[i][j] = colors[i][j] == 0xFF0000;
					}
				}
				return destinations;
			}

			@Override
			public boolean[][] getVehicles() {
				boolean[][] vehicles = new boolean[w][h];
				for (int i = 0; i < w; i++) {
					for (int j = 0; j < h; j++) {
						vehicles[i][j] = colors[i][j] == 0x0000FF;
					}
				}
				return vehicles;
			}
		};
	}
	
}
