package yMonotonPolygon.AlgorithmObjects;

import java.awt.Point;
import java.util.TreeSet;

public class SweepLineStatus {
	
	/**
	 * The points above the sweep line, already handled
	 */
	TreeSet<Point> handledPoints;
	
	/**
	 * The points below the sweep line, not yeat handled
	 */
	TreeSet<Point> unhandledPoints;
	
	/**
	 * The current point for the sweep line
	 */
	Point currentPoint;

}
