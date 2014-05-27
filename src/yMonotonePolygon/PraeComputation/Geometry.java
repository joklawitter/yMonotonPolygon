package yMonotonePolygon.PraeComputation;

import java.awt.Polygon;

import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.Vertex;

public class Geometry {

	private Geometry() {
		throw new AssertionError(); // should not be instantiated
	}
	
	public static boolean checkSimplePolygon(Polygon p) {
		// TODO
		return true;
	}

	public static boolean checkPolygonOrientation(Polygon p) {
		// TODO Auto-generated method stub
		return true;
	}

	public static Polygon turnOrientation(Polygon p) {
		// TODO Auto-generated method stub
		return p;
	}

	public static boolean liesLeftOfLine(Edge e, Vertex v) {
		int eX = (e.getEndVertex().getX() - e.getStartVertex().getX());
		int eY = (e.getEndVertex().getY() - e.getStartVertex().getY());
		int mX = (v.getX() - e.getStartVertex().getX());
		int mY = (v.getY() - e.getStartVertex().getY());
		
		return (((eX * mY) - (eY * mX)) < 0);
	}
	
	/**
	 * Returns whether vertex one lies lower (or more to the right) than vertex two
	 * @param one
	 * @param two
	 * @return whether vertex one lies lower (or more to the right) than vertex two
	 */
	public static boolean isLowerThan(Vertex one, Vertex two) {
		if (one.getY() < two.getY()) { // the wrong way since drawing begins at the top
			return false;
		} else if (one.getY() > two.getY()) {
			return true;
		} else {
			return (one.getX() > two.getX());
		}
	}
	
	
	public static double computeXOfEdgeAtY(Edge e, double y) {
		Vertex a = e.getStartVertex();
		Vertex b = e.getEndVertex();
		double alpha = (y - (double) a.getY()) / ((double) b.getY() - (double) a.getY());
		if ((alpha > 1) || (alpha < 0)) {
			throw new AssertionError("Does not lie in y-interval of edge.");
		}
		//System.out.println("y = " + y + ", alpha = " + alpha);
		return (a.getX() + alpha * (b.getX() - a.getX()));
	}
}
