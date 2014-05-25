package yMonotonePolygon.AlgorithmObjects;

public class UpdateDeletionTreeSubEvent extends UpdateTreeSubEvent {
	
	/** The removed edge */
	private final Edge oldEdge;
	
	/** The old helper vertex */
	private final Vertex oldHelper;
	
	public UpdateDeletionTreeSubEvent(int methodLine, SearchTree tree, Edge oldEdge, Vertex oldHelper) {
		super(methodLine, tree);
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
