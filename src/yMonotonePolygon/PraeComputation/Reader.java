package yMonotonePolygon.PraeComputation;

import java.awt.Polygon;
import java.io.IOException;

import yMonotonePolygon.InputReading.FileInputReader;

public class Reader {

	public static Polygon readTestPolygon(String name) {
		Polygon p = null;
		try {
			p = FileInputReader.readInputFile("PolygonExamples/" + name + ".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*for (int i = 0; i < p.npoints; i++) {
			System.out.println("Point " + p.xpoints[i] + ", " + p.ypoints[i]);
		}*/
		return p;
	}
	
	public static Polygon readPolygon(String adress) throws IOException {
		Polygon p = null;
		System.out.println("readPolygon adress: " + adress);
		p = FileInputReader.readInputFile(adress);
		
		/*for (int i = 0; i < p.npoints; i++) {
			System.out.println("Point " + p.xpoints[i] + ", " + p.ypoints[i]);
		}*/
		return p;
	}
}
