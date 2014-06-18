package yMonotonePolygon.AlgorithmObjects;

import java.util.HashSet;
import java.util.TreeSet;

public class UpdateDeletionTreeSubEvent extends UpdateTreeSubEvent {
	
	/** The removed edge */
	private final Edge oldEdge;
	
	/** The old helper vertex */
	private final Vertex oldHelper;
	
	public UpdateDeletionTreeSubEvent(int methodLine, TreeSet<Vertex> updatedVerticesOfTree, HashSet<Edge> activeEdges,
			Edge oldEdge, Vertex oldHelper) {
		super(methodLine, updatedVerticesOfTree, activeEdges);
		this.oldEdge = oldEdge;
		this.oldHelper = oldHelper;
	}

	public Edge getOldEdge() {
		return oldEdge;
	}

	public Vertex getOldHelper() {
		return oldHelper;
	}
}
