package yMonotonePolygon.InputReading;

import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileInputReader {
	
	public static Polygon readInputFile(String filename) throws IOException {
		if (filename == null) {
			throw new IOException(filename);
		}
		
		FileInputStream fstream = new FileInputStream(filename);
			
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
		String strLine = br.readLine();
		String[] tokens = strLine.split("\\s");
			
		Polygon polygon = new Polygon();
		
		if (tokens.length <= 4) {
			br.close();
			throw new IOException("illegal format or not enough points");
		}
		
		if (!tokens[0].equalsIgnoreCase("polygon")) {
			br.close();
			throw new IOException("illegal format");
		}
		
		for (int i = 1; i < tokens.length; i++) {
			String[] coordinates = tokens[i].split(",");
			if (coordinates.length != 2) {
				br.close();
				throw new IOException("illegal point format");
			}
			
			int x = Integer.parseInt(coordinates[0]);
			int y = Integer.parseInt(coordinates[1]);
			
			polygon.addPoint(x, y);
		}
		
		br.close();
		
		return polygon;
	}
}
