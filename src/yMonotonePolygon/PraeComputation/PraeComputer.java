package yMonotonePolygon.PraeComputation;

import java.awt.Color;
import java.awt.Polygon;
import java.util.LinkedList;
import java.util.TreeSet;

import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.SearchTree;
import yMonotonePolygon.AlgorithmObjects.SubEvent;
import yMonotonePolygon.AlgorithmObjects.SweepLineEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateHelperSubEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateInsertTreeSubEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateTreeSubEvent;
import yMonotonePolygon.AlgorithmObjects.Vertex;
import yMonotonePolygon.GUI.GUIColorConfiguration;

public class PraeComputer {
	
	/** input polygon */
	private Polygon p;

	/**
	 * The polygon as vertices 
	 * and as set sorted by y-coordinate for the algorithm as queue
	 */
	private TreeSet<Vertex> vertices;
	
	/** List of all SweepLineEvents during the algorithm */
	private LinkedList<SweepLineEvent> history;
	
	/** List of the diagonals as the result of the algorithm in order of addition */
	private LinkedList<Edge> diagonals;
	
	// things to keep during the algorithm
	/** number of handled vertices */
	private int handledVertices;
	
	/** binary search tree of the active edges */
	private SearchTree tree;

	public Polygon getP() {
		return p;
	}

	public TreeSet<Vertex> getVertices() {
		return vertices;
	}
	
	public LinkedList<SweepLineEvent> getHistory() {
		return history;
	}

	public LinkedList<Edge> getDiagonals() {
		return diagonals;
	}

	
	
	/**
	 * Does the prae-computation. 
	 * Includes computing the whole history and diagonals to add.
	 * @param p Polygon on which to work
	 * @return whether it was successful
	 */
	public boolean work(Polygon p) {
		if (p == null) {
			throw new IllegalArgumentException("No polygon given...");
		}
		
		// check polygon to be simple and counterclockwise
		if (!Geometry.checkSimplePolygon(p)) {
			throw new IllegalArgumentException("Not a simple polygon!");
		}
		if (!Geometry.checkPolygonOrientation(p)) {
			p = Geometry.turnOrientation(p);
		}
		
		// construct own polygon and identify vertex types
		createVertices(p);
		computeVertexType();
		
		
		// initialize fields
		tree = new SearchTree();
		diagonals = new LinkedList<Edge>();
		history = new LinkedList<SweepLineEvent>();
		
		
		// sort vertices y-x-lexicographical
		// - happens automatically with natural ordering and TreeSet

		
		// run algorithm and create corresponding events
		for (Vertex v : vertices) {
			handleVertexEvent(v);
			handledVertices++;
		}
		
		assert (handledVertices == vertices.size());
		
		return false;
	}


	private void handleVertexEvent(Vertex v) {
		SweepLineEvent event = null;
		
		if (v.isStartVertex()) {
			event = handleStartVertex(v);
		} 
		
		// TODO
		history.add(event);	
	}

	private SweepLineEvent handleStartVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v); 
		
		// line 1: insert edge in SearchTree
		tree.insert(v.getNextEdge(), v.getY());
		v.getNextEdge().setColor(getNextColor());
		UpdateInsertTreeSubEvent treeUpdate = new UpdateInsertTreeSubEvent(1, tree.clone(), v.getNextEdge().clone());
		
		// line 2: set v as helper
		v.getNextEdge().setHelper(v);
		UpdateHelperSubEvent helperUpdate = new UpdateHelperSubEvent(2, v.clone());
		
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();
		subEvents.add(treeUpdate);
		subEvents.add(helperUpdate);
		event.setSubEvents(subEvents);
		
		history.add(event);
		
		return event;
	}

	private SweepLineEvent initSweepLineEvent(Vertex v) {
		return new SweepLineEvent(v, diagonals.size(), handledVertices);
	}

	/**
	 * Returns a new color for a edge-helper pair.
	 * @return
	 */
	private Color getNextColor() {
		// TODO change
		return GUIColorConfiguration.DIAGONAL;
	}
	
	private boolean createVertices(Polygon poly) {
		vertices = new TreeSet<Vertex>();
		
		int[] xPoints = poly.xpoints;
		int[] yPoints = poly.ypoints;
		
		Vertex first = new Vertex(xPoints[0], yPoints[0]);
		Vertex current = first;
		Vertex prev;
		for (int i = 1; i < poly.npoints; i++) {
			prev = current;
			current = new Vertex(xPoints[i], yPoints[i]);
			current.setPrev(current);
			prev.setNext(current);
		}
		first.setPrev(current); // current is now last one
		current.setNext(first);
		
		// now set the edges between them
		current = first;
		do { 
			Edge e = new Edge(current, current.getNext());
			current.setNextEdge(e);
			current = current.getNext();
			current.setPrevEdge(e);			
		} while (current != first);

		return true;
	}
	
	private void computeVertexType() {
		for (Vertex v : vertices) {
			v.computeVertexType();
		}
	}

}
