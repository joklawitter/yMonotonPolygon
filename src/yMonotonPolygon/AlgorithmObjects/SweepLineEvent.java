package yMonotonPolygon.AlgorithmObjects;

import java.util.TreeSet;

public class SweepLineEvent {
	// first, what has already been drawn & sweep line status
	private int numberOfDiagonals;
	private SweepLineStatus sweepLine;
	
	// second, update binary search tree & the corresponding edge-helper-pairs
	private SearchTree tree;
	private TreeSet<EdgeHelperPairs> aktiveEdgeHelperPairs;
	
	// third , display correct method
	private VertexType type;
	private Method currentMethod;
	
	// 4th, the sub-events of this event
	TreeSet<SubEvent> subEvents;	
	private int numberOfSteps; // subEvents.length
}
