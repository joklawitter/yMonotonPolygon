package yMonotonePolygon.Tests;

import java.awt.Polygon;
import java.io.IOException;

import yMonotonePolygon.InputReading.FileInputReader;

public class TestHelper {

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
}
