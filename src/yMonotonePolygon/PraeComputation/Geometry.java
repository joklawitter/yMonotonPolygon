package yMonotonePolygon.PraeComputation;

import java.util.TreeSet;

import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.Vertex;

public class Geometry {

	private Geometry() {
		throw new AssertionError(); // should not be instantiated
	}
	
	public static boolean checkSimplePolygon(TreeSet<Vertex> vertices) {
		// TODO
		return true;
	}

	/**
	 * Returns true if the orientation is counterclockwise.
	 * @param vertices polygon to test
	 * @return whether the orientation is counterclockwise
	 */
	public static boolean checkPolygonOrientation(TreeSet<Vertex> vertices) {
		Vertex left = vertices.first();
		for (Vertex v : vertices) { // find most left vertex
			if (v.getX() <= left.getX()) {
				left = v;
			}
		}
		
		// if next point lies left of the edge pointing to left most vertex
		// then the orientation is counterclockwise
		return Geometry.liesLeftOfLine(left.getPrevEdge(), left.getNext());
	}

	public static TreeSet<Vertex> turnOrientation(TreeSet<Vertex> vertices) {
		Vertex prev;
		Vertex next;
		Vertex first = vertices.first();
		Vertex current = first;
		do { // turn next and prev vertex
			prev = current.getPrev();
			next = current.getNext();
			current.setNext(prev);
			current.setPrev(next);
			
			current = next;			
		} while (current != first);
		
		current = first;
		do { // turn next and prev vertex
			next = current.getNext();
			Edge newEdge = new Edge(current, next);
			current.setNextEdge(newEdge);
			next.setPrevEdge(newEdge);
			
			current = next;			
		} while (current != first);
		
		return vertices;
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
			System.out.println(y + "-> " + e.toString());
			throw new AssertionError("Does not lie in y-interval of edge: " + alpha);
		}
		//System.out.println("y = " + y + ", alpha = " + alpha);
		return (a.getX() + alpha * (b.getX() - a.getX()));
	}
}
