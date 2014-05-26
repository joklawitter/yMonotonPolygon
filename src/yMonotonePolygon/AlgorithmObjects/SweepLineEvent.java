package yMonotonePolygon.AlgorithmObjects;

import java.util.LinkedList;
import java.util.TreeSet;

import yMonotonePolygon.AlgorithmObjects.VertexType;

public class SweepLineEvent {
	// first, what has already been drawn & sweep line status
	private int numberOfDiagonals;
	private int numberOfHandledVertices;
	
	// second, update binary search tree & the corresponding edge-helper-pairs
	private SearchTree tree;
	private TreeSet<Edge> activeEdges;
	
	// third , display correct method
	private Vertex vertex;
	
	// 4th, the sub-events of this event
	private LinkedList<SubEvent> subEvents;	
	
	public SweepLineEvent(Vertex v, int numberOfDiagonals, int numberOfHandledVertices, SearchTree searchTree, TreeSet<Edge> aktiveEdges) {
		this.vertex = v;
		this.numberOfDiagonals = numberOfDiagonals;
		this.numberOfHandledVertices = numberOfHandledVertices;
		this.tree = searchTree;
		this.activeEdges = aktiveEdges;
	}
	
	public Vertex getVertex() {
		return vertex;
	}
	
	public Method getMethod() {
		return vertex.getCorrespondingMethod();
	}
	
	public VertexType getVertexType() {
		return vertex.getVertexType();
	}
	
	public int getNumberOfHandledVertices() {
		return numberOfHandledVertices;
	}
	
	public int getYOfSweepLine() {
		return vertex.getY();
	}
		
	public int getNumberOfDiagonals() {
		return numberOfDiagonals;
	}
	
	public SearchTree getTree() {
		return tree;
	}

	public TreeSet<Edge> getActiveEdges() {
		return activeEdges;
	}
	
	public LinkedList<SubEvent> getSubEvents() {
		return subEvents;
	}

	public void setSubEvents(LinkedList<SubEvent> subEvents) {
		this.subEvents = subEvents;
	}

	public int getNumberOfSteps() {
		return subEvents.size();
	}
	
	
}
