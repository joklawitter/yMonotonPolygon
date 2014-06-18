package yMonotonePolygon.AlgorithmObjects;

import java.util.HashSet;
import java.util.TreeSet;

public abstract class UpdateTreeSubEvent extends SubEvent {

	/** binary search tree of the active edges */
	private TreeSet<Vertex> treeUpdate;
	private HashSet<Edge> activeEdges;
	
	public UpdateTreeSubEvent(int methodLine, TreeSet<Vertex> updatedVerticesOfTree, HashSet<Edge> activeEdges) {
		super(methodLine);
		this.treeUpdate = updatedVerticesOfTree;
		this.activeEdges = activeEdges;
	}
	
	public TreeSet<Vertex> getUpdatedVerticesOfTree() {
		return treeUpdate;
	}

	public HashSet<Edge> getActiveEdges() {
		return activeEdges;
	}
}
