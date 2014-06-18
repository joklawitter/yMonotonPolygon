package yMonotonePolygon.AlgorithmObjects;

import java.util.HashSet;
import java.util.TreeSet;


public class UpdateInsertTreeSubEvent extends UpdateTreeSubEvent {

	/** 
	 * The new added edge.<br>
	 * Attention: This is just a clone and crossreference to tree does not work! 
	 */
	private final Edge newEdge;
	
	public UpdateInsertTreeSubEvent(int methodLine, TreeSet<Vertex> updatedVerticesOfTree,
			 HashSet<Edge> activeEdges, Edge newEdge) {
		super(methodLine, updatedVerticesOfTree, activeEdges);
		this.newEdge = newEdge;
	}

	public Edge getNewEdge() {
		return newEdge;
	}
}
