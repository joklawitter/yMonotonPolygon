package yMonotonePolygon.PraeComputation;

import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.TreeSet;

import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.Vertex;

public class Geometry {

	private Geometry() {
		throw new AssertionError(); // should not be instantiated
	}
	
	public static boolean checkSimplePolygon(TreeSet<Vertex> vertices) {
		HashSet<Edge> edges  = new HashSet<Edge>(vertices.size());
		for (Vertex v : vertices) {
			edges.add(v.getNextEdge());
		}
		
		for (Edge e : edges) {
			for (Edge f : edges) {
				if (e == f) {
					continue;
				}
				if (!shareEndPoint(e, f) && intersect(e, f)) {
					return false;
				}
			}
			
		}
		
		return true;
	}

	/**
	 * Returns whether the two edges intersect, end points included
	 * @param e 
	 * @param f
	 * @return whether the two edges intersect, end points included
	 */
	private static boolean intersect(Edge e, Edge f) {
		return Line2D.linesIntersect(e.getStartVertex().getX(), e.getStartVertex().getY(), 
				e.getEndVertex().getX(), e.getEndVertex().getY(), 
				f.getStartVertex().getX(), f.getStartVertex().getY(), 
				f.getEndVertex().getX(), f.getEndVertex().getY());
	}

	private static boolean shareEndPoint(Edge e, Edge f) {
		if (e.getEndVertex() == f.getStartVertex()) {
			return true;
		} else if (e.getStartVertex() == f.getEndVertex()) {
			return true;
		}
		return false;
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
		return (a.getX() + alpha * (b.getX() - a.getX()));
	}
}
