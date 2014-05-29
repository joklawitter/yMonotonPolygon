package yMonotonePolygon.AlgorithmObjects;

import java.util.TreeSet;


public class UpdateInsertTreeSubEvent extends UpdateTreeSubEvent {

	/** 
	 * The new added edge.<br>
	 * Attention: This is just a clone and crossreference to tree does not work! 
	 */
	private final Edge newEdge;
	
	public UpdateInsertTreeSubEvent(int methodLine, TreeSet<Vertex> updatedVerticesOfTree, Edge newEdge) {
		super(methodLine, updatedVerticesOfTree);
		this.newEdge = newEdge;
	}

	public Edge getNewEdge() {
		return newEdge;
	}
}
