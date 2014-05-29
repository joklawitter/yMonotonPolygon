package yMonotonePolygon.AlgorithmObjects;

import java.util.TreeSet;

public abstract class UpdateTreeSubEvent extends SubEvent {

	/** binary search tree of the active edges */
	private TreeSet<Vertex> treeUpdate;
	
	public UpdateTreeSubEvent(int methodLine, TreeSet<Vertex> updatedVerticesOfTree) {
		super(methodLine);
		this.treeUpdate = updatedVerticesOfTree;
	}
	
	public TreeSet<Vertex> getUpdatedVerticesOfTree() {
		return treeUpdate;
	}
}
