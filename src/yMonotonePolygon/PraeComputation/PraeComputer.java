package yMonotonePolygon.PraeComputation;

import java.awt.Color;
import java.awt.Polygon;
//import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import yMonotonePolygon.AlgorithmObjects.AddDiagonalSubEvent;
import yMonotonePolygon.AlgorithmObjects.BooleanSubEvent;
import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.SearchSubEvent;
import yMonotonePolygon.AlgorithmObjects.SearchTree;
import yMonotonePolygon.AlgorithmObjects.SubEvent;
import yMonotonePolygon.AlgorithmObjects.SweepLineEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateDeletionTreeSubEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateHelperSubEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateInsertTreeSubEvent;
import yMonotonePolygon.AlgorithmObjects.Vertex;
import yMonotonePolygon.GUI.GUIColorConfiguration;

/**
 * The PraeComputer is the main worker of the application.
 * Given a polygon it checks its correctness and then computes the
 * history of the algorithm for this polygon.
 */
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
	
	/** active edges, those crossing the sweep line and pointing down */
	private HashSet<Edge> activeEdges;

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
	 * @throws IllegalPolygonException 
	 */
	public boolean work(Polygon p) throws IllegalPolygonException {
		if (p == null) {
			throw new IllegalPolygonException("No polygon given!");
		}
		
		
		// construct own polygon and identify vertex types
		if (!createVertices(p)) {
			return false;
		}
		
		// check polygon to be simple and counterclockwise
		if (!Geometry.checkSimplePolygon(vertices)) {
			throw new IllegalPolygonException("Not a simple polygon!");
		}
		if (!Geometry.checkPolygonOrientation(vertices)) {
			vertices = Geometry.turnOrientation(vertices); 
		}
		
		if (!computeVertexType()) {
			return false;
		}
		
		
		// initialize fields
		tree = new SearchTree();
		diagonals = new LinkedList<Edge>();
		history = new LinkedList<SweepLineEvent>();
		activeEdges = new HashSet<Edge>();
		handledVertices = 0;
		
		// sort vertices y-x-lexicographical
		// - happens automatically with natural ordering and TreeSet

		// run algorithm and create corresponding events
		for (Vertex v : vertices) {
			handleVertexEvent(v);
			handledVertices++;
		}
		
		assert (handledVertices == vertices.size());
		

//		computeYMonotoneSamplePolygon();
		
		return true;
	}
	
	private void handleVertexEvent(Vertex v) {
		SweepLineEvent event = null;
		
		if (v.isStartVertex()) {
			event = handleStartVertex(v);
		} else if (v.isEndVertex()) {
			event = handleEndVertex(v);
		} else if (v.isSplitVertex()) {
			event = handleSplitVertex(v);
		} else if (v.isMergeVertex()) {
			event = handleMergeVertex(v);
		} else if (v.isRegularRightVertex()) {
			event = handleRegularRightVertex(v);
		} else if (v.isRegularLeftVertex()) {
			event = handleRegularLeftVertex(v);
		} 
		
		history.add(event);	
	}

	private SweepLineEvent handleStartVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v); 
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();
		Edge nextEdge = v.getNextEdge();
		
		// line 1: insert edge in SearchTree
		UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(nextEdge, 1);
		subEvents.add(treeUpdate);
		
		// line 2: set v as helper
		UpdateHelperSubEvent helperUpdate = updateHelper(nextEdge, v, 2);
		subEvents.add(helperUpdate);
		
		event.setSubEvents(subEvents);		
		return event;
	}

	private SweepLineEvent handleEndVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v); 
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();
		Edge prevEdge = v.getPrevEdge();
		
		// line 1: if helper is merge vertex
		assert (prevEdge.getHelper() != null);
		boolean helperIsMerge = prevEdge.getHelper().isMergeVertex();
		BooleanSubEvent boolEvent = new BooleanSubEvent(1, helperIsMerge);
		subEvents.add(boolEvent);
		
		// then line 2: insert diagonal v-helper 
		if (helperIsMerge) {
			AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, prevEdge, 2);
			subEvents.add(addDiagonalEvent);
		}
		
		// line 3: delete prevEdge from tree
		UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge, 3);
		subEvents.add(deletionEvent);		
		
		event.setSubEvents(subEvents);		
		return event;
	} 
	
	private SweepLineEvent handleSplitVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v); 
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();
		
		// line 1: search in tree to find edge left of v
		Edge leftOfVEdge = tree.searchEdge(v);
		assert (leftOfVEdge != null);
		SearchSubEvent searchEvent = new SearchSubEvent(1, leftOfVEdge);
		subEvents.add(searchEvent);		
		
		// line 2: add diagonal to helper of found edge and split vertex v
		AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, leftOfVEdge, 2);
		subEvents.add(addDiagonalEvent);
		
		// line 3: update helper - v is now helper
		UpdateHelperSubEvent helperUpdate = updateHelper(leftOfVEdge, v, 3);
		subEvents.add(helperUpdate);
		
		// line 4: insert next edge of v in t
		Edge nextEdge = v.getNextEdge();
		UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(nextEdge, 4);
		subEvents.add(treeUpdate);
		
		// line 5: set v as helper of this edge
		UpdateHelperSubEvent helperUpdate2 = updateHelper(nextEdge, v, 5);
		subEvents.add(helperUpdate2);
		
		event.setSubEvents(subEvents);
		return event;
	}

	private SweepLineEvent handleMergeVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v); 
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();
		Edge prevEdge = v.getPrevEdge();
		
		// line 1: if helper of edge right of v is merge vertex
		boolean helperIsMerge = prevEdge.getHelper().isMergeVertex();
		BooleanSubEvent boolEvent = new BooleanSubEvent(1, helperIsMerge);
		subEvents.add(boolEvent);
		
		// then line 2: insert diagonal v-helper 
		if (helperIsMerge) {
			AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, prevEdge, 2);
			subEvents.add(addDiagonalEvent);
		}
		
		// line 3: delete prevEdge from tree
		UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge, 3);
		subEvents.add(deletionEvent);	
		
		// line 4: search in tree to find edge left of v
		Edge leftOfVEdge = tree.searchEdge(v);
		SearchSubEvent searchEvent = new SearchSubEvent(4, leftOfVEdge);
		subEvents.add(searchEvent);
		
		// line 5: if helper of edge left of v is merge vertex
		boolean helperIsMerge2 = leftOfVEdge.getHelper().isMergeVertex();
		BooleanSubEvent boolEvent2 = new BooleanSubEvent(5, helperIsMerge2);
		subEvents.add(boolEvent2);
		
		// then line 6: insert diagonal v-helper 
		if (helperIsMerge2) {
			AddDiagonalSubEvent addDiagonalEvent2 = addDiagonal(v, leftOfVEdge, 6);
			subEvents.add(addDiagonalEvent2);
		}
		
		// line 7: set v as helper of this edge
		UpdateHelperSubEvent helperUpdate = updateHelper(leftOfVEdge, v, 7);
		subEvents.add(helperUpdate);
		
		
		event.setSubEvents(subEvents);
		return event;
	}

	private SweepLineEvent handleRegularLeftVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v); 
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();
						
		Edge prevEdge = v.getPrevEdge();
		// line 1: if helper of edge before v is merge vertex
		boolean helperIsMerge = prevEdge.getHelper().isMergeVertex();
		BooleanSubEvent boolEvent = new BooleanSubEvent(1, helperIsMerge);
		subEvents.add(boolEvent);
		
		// then line 2: insert diagonal v-helper 
		if (helperIsMerge) {
			AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, prevEdge, 2);
			subEvents.add(addDiagonalEvent);
		}
		
		// line 3: delete prevEdge from tree
		UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge, 3);
		subEvents.add(deletionEvent);	
		
		Edge nextEdge = v.getNextEdge();
		// line 4: insert edge in SearchTree
		UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(nextEdge, 4);
		subEvents.add(treeUpdate);
		
		// line 5: set v as helper
		UpdateHelperSubEvent helperUpdate = updateHelper(nextEdge, v, 5);
		subEvents.add(helperUpdate);
		
		event.setSubEvents(subEvents);	
		return event;
	}

	private SweepLineEvent handleRegularRightVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v); 
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();
					
		// line 1: search in tree to find edge left of v
		Edge leftOfVEdge = tree.searchEdge(v);
		SearchSubEvent searchEvent = new SearchSubEvent(1, leftOfVEdge);
		subEvents.add(searchEvent);
		
		// line 2: if helper of edge left of v is merge vertex
		boolean helperIsMerge = leftOfVEdge.getHelper().isMergeVertex();
		BooleanSubEvent boolEvent = new BooleanSubEvent(2, helperIsMerge);
		subEvents.add(boolEvent);
		
		// then line 3: insert diagonal v-helper 
		if (helperIsMerge) {
			AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, leftOfVEdge, 3);
			subEvents.add(addDiagonalEvent);
		}
		
		// line 4: set v as helper of this edge
		UpdateHelperSubEvent helperUpdate = updateHelper(leftOfVEdge, v, 4);
		subEvents.add(helperUpdate);
		
		event.setSubEvents(subEvents);	
		return event;
	}
	
	private SweepLineEvent initSweepLineEvent(Vertex v) {
		return new SweepLineEvent(v, diagonals.size(), handledVertices, 
				tree.getNodesForY(v.getY()), cloneHelper(activeEdges));
	}

	private AddDiagonalSubEvent addDiagonal(Vertex v, Edge edge, int methodline) {
		Edge newDiagonal = Edge.diagonalFactory(v, edge.getHelper());
		v.addDiagonal(newDiagonal);
		edge.getHelper().addDiagonal(newDiagonal);
		diagonals.add(newDiagonal);
		AddDiagonalSubEvent addDiagonalEvent = new AddDiagonalSubEvent(methodline, diagonals.size());
		return addDiagonalEvent;
	}
	
	private UpdateDeletionTreeSubEvent deleteEdgeFromTree(Edge toDelete, int methodline) {
		Vertex oldHelper = toDelete.releaseHelper();
		tree.delete(toDelete);
		activeEdges.remove(toDelete);
		UpdateDeletionTreeSubEvent deletionEvent = 
				new UpdateDeletionTreeSubEvent(methodline, tree.getNodesForY(toDelete.getEndVertex().getY()), 
						cloneHelper(activeEdges), toDelete, oldHelper);
		return deletionEvent;
	}
	
	private UpdateInsertTreeSubEvent insertEdgeInTree(Edge toInsert, int methodline) {
		if (toInsert == null) {
			throw new IllegalArgumentException();
		}
		
		tree.insert(toInsert);
		activeEdges.add(toInsert);
		toInsert.setColor(getNextColor());
		UpdateInsertTreeSubEvent treeUpdate = 
				new UpdateInsertTreeSubEvent(methodline, tree.getNodesForY(toInsert.getStartVertex().getY()), 
						cloneHelper(activeEdges), toInsert.clone());
		return treeUpdate;
	}
	
	private UpdateHelperSubEvent updateHelper(Edge edge, Vertex newHelper, int methodline) {
		Vertex oldHelper = edge.getHelper();
		edge.setHelper(newHelper);
		UpdateHelperSubEvent helperUpdate = new UpdateHelperSubEvent(methodline, newHelper.clone(), oldHelper);
		return helperUpdate;
	}
	
	private HashSet<Edge> cloneHelper(HashSet<Edge> toClone) {
		HashSet<Edge> cloned = new HashSet<Edge>();
		for (Edge e : toClone) {
			cloned.add(e.clone());
		}
		return cloned;
	}

	/**
	 * Returns a new color for a edge-helper pair.
	 * @return
	 */
	private Color getNextColor() {
		return GUIColorConfiguration.getRandomColor();
	}
	
	private boolean createVertices(Polygon poly) {
		vertices = new TreeSet<Vertex>();
		
		int[] xPoints = poly.xpoints;
		int[] yPoints = poly.ypoints;
		
		Vertex first = new Vertex(xPoints[0], yPoints[0]);
		vertices.add(first);
		Vertex current = first;
		Vertex prev;
		for (int i = 1; i < poly.npoints; i++) {
			prev = current;
			current = new Vertex(xPoints[i], yPoints[i]);
			current.setPrev(prev);
			prev.setNext(current);
			vertices.add(current);
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
	
	private boolean computeVertexType() {
		for (Vertex v : vertices) {
			if (!v.computeVertexType()) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return "PraeComputer [vertices=" + verticesToString() + ", " 
				+ diagonals.size() + " diagonals=" + diagonalsToString() 
				+ ", handledVertices="+ handledVertices + "]";
	}

	private String diagonalsToString() {
		String s = "[";
		for (Edge e : diagonals) {
			s += e.toString() + " | ";
		}
		s += "]";
		return s;
	}

	private String verticesToString() {
		String s = "[";
		for (Vertex v : vertices) {
			s += v.toShortString() + " | ";
		}
		s += "]";
		return s;
	}
	
//	private LinkedList<Point2D> yMontoneSamplePolygon;
//	
//	public LinkedList<Point2D> getSamplePolygon() {
//		return yMontoneSamplePolygon;
//	}
//	
//	private void computeYMonotoneSamplePolygon() {
//		System.out.println("Computing sample polygon.");
//		Vertex start = findBottomMostVertex();
//		System.out.println("new start: " + start.toShortString());
//		LinkedList<Point2D> points = new LinkedList<Point2D>();
//		points.add(start.getPoint2D());
//		
//		Edge left;
//		Vertex curLeft;
//		if (start.hasDiagonals()) {
//			left = findUpwardMostRightDiag(start);
//			curLeft = (left.getEndVertex() == start) ? left.getStartVertex() : left.getEndVertex();
//		} else {
//			left = start.getPrevEdge();
//			curLeft = left.getStartVertex();
//		}
//		System.out.println("new Left: " + curLeft.toShortString());
//		points.add(0, curLeft.getPoint2D()); // add left to beginning of list
//
//		Edge right = start.getNextEdge();
//		Vertex curRight = right.getEndVertex();
//		System.out.println("new Right: " + curRight.toShortString());
//		points.add(curRight.getPoint2D()); // add to end of list
//
//		boolean reachedLeftTop = false;
//		boolean reachedRightTop = false;
//		int round = 0;
//		while (curRight != curLeft && round < 20) { 
//			System.out.println("New Round.");
//			round++;
//			
//			// go either left or right up
//			if (Geometry.isLowerThan(curLeft, curRight)) {
//				if (!reachedLeftTop) {
//					// till reached end
//					Edge leftNext = null;
//					if (curLeft.hasDiagonals()) { // look if we have to go over diagonal on left side
//						leftNext = findUpwardMostRightDiag(curLeft);
//						if (leftNext != null) {
//							curLeft = (leftNext.getEndVertex() == curLeft) ? leftNext.getStartVertex() : leftNext.getEndVertex();
//							left = leftNext;
//							System.out.println("new Left: " + curLeft.toShortString());
//							points.add(0, curLeft.getPoint2D()); // add left to beginning of list
//						}
//					}
//					if (leftNext == null) {
//						if (Geometry.isLowerThan(curLeft, curLeft.getPrevEdge().getStartVertex())) {
//							left = curLeft.getPrevEdge();
//							curLeft = left.getStartVertex();
//							System.out.println("new Left: " + curLeft.toShortString());
//							points.add(0, curLeft.getPoint2D()); // add left to beginning of list
//						} else {
//							System.out.println("reached left top");
//							reachedLeftTop = true;					
//						}
//					}
//					
//				}	
//			} else {
//				if (!reachedRightTop) {
//					// till reached end
//					Edge rightNext = null;
//					if (curRight.hasDiagonals()) { // look if we have to go over diagonal on left side
//						rightNext = findUpwardMostLeftDiag(curRight);
//						if (rightNext != null) {
//							curRight = (rightNext.getEndVertex() == curRight) ? rightNext.getStartVertex() : rightNext.getEndVertex();
//							System.out.println("new Right: " + curRight.toShortString());
//							points.add(curRight.getPoint2D()); // add to end of list
//						}
//					}
//					if (rightNext == null) {
//						if (Geometry.isLowerThan(curRight, curRight.getNextEdge().getEndVertex())) {
//							right = curRight.getNextEdge();
//							curRight = right.getEndVertex();
//							System.out.println("new Right: " + curRight.toShortString());
//							points.add(curRight.getPoint2D()); // add to end of list
//						} else {
//							System.out.println("reached right top");
//							reachedRightTop = true;					
//						}
//					}
//					
//				}
//			}		
//		}	
//
//		points.removeLast(); // last left and last right were the same
//		
//		yMontoneSamplePolygon = points;
//		System.out.println("Computet polygon: ");
//		for (Point2D p : points) {
//			System.out.print(p + ", ");
//		}
//	}
//
//	private Edge findUpwardMostLeftDiag(Vertex lower) {
//		Vertex upper = null;
//		Edge curDiag = null;
//		Vertex other = null;
//		for (Edge e : lower.getDiagonals()) {
//			if ((e.getStartVertex() != lower) && (!Geometry.isLowerThan(e.getStartVertex(), lower))) {
//				other = e.getStartVertex();
//			} else if ((e.getEndVertex() != lower) && (!Geometry.isLowerThan(e.getEndVertex(), lower))){
//				other = e.getEndVertex();
//			} else {
//				continue;
//			}
//			
//			if (upper == null) {
//				upper = other;
//				curDiag = e;
//			} else if (!Geometry.liesLeftOfLine(new Edge(lower, other), other)) {
//				upper = other;
//				curDiag = e;
//			}
//		}
//		
//		if (upper == null) {
//			return lower.getNextEdge();
//		}
//		
//		if ((Geometry.isLowerThan(lower, lower.getNextEdge().getEndVertex())) 
//				&& (!Geometry.liesLeftOfLine(lower.getNextEdge(), upper))) {
//			curDiag = lower.getNextEdge();
//		}
//		
//		return curDiag;
//	}
//
//	private Edge findUpwardMostRightDiag(Vertex lower) {
//		Vertex upper = null;
//		Edge curDiag = null;
//		Vertex other = null;
//		for (Edge e : lower.getDiagonals()) {
//			if ((e.getStartVertex() != lower) && (!Geometry.isLowerThan(e.getStartVertex(), lower))) {
//				other = e.getStartVertex();
//			} else if ((e.getEndVertex() != lower) && (!Geometry.isLowerThan(e.getEndVertex(), lower))){
//				other = e.getEndVertex();
//			} else {
//				continue;
//			}
//			
//			if (upper == null) {
//				upper = other;
//				curDiag = e;
//			} else if (Geometry.liesLeftOfLine(new Edge(lower, other), other)) {
//				upper = other;
//				curDiag = e;
//			}
//		}
//		
//		if (upper == null) {
//			return lower.getPrevEdge();
//		}
//		
//		if ((Geometry.isLowerThan(lower, lower.getPrevEdge().getEndVertex())) 
//				&& (Geometry.liesLeftOfLine(lower.getPrevEdge(), upper))) {
//			curDiag = lower.getPrevEdge();
//		}
//		
//		return curDiag;
//	}
//
//	private Vertex findBottomMostVertex() {
//		Vertex bottom = vertices.first();
//		for (Vertex v : vertices) {
//			if (Geometry.isLowerThan(v, bottom)) {
//				bottom = v;
//			}
//		}
//		
//		return bottom;
//	}
}
