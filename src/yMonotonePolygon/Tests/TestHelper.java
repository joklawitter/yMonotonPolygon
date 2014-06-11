package yMonotonePolygon.Tests;

import java.awt.Polygon;
import java.io.IOException;

import yMonotonePolygon.InputReading.FileInputReader;

import com.trolltech.qt.gui.QPolygonF;

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
	
	public static Polygon readPolygon(String adress) {
		Polygon p = null;
		try {
			p = FileInputReader.readInputFile("PolygonExamples/" +  adress);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*for (int i = 0; i < p.npoints; i++) {
			System.out.println("Point " + p.xpoints[i] + ", " + p.ypoints[i]);
		}*/
		return p;
	}
	
	public static QPolygonF readTestQPolygonF(String name) {
		QPolygonF p = null;
		try {
			p = FileInputReader.readInputFileQF("PolygonExamples/" + name + ".txt");
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
