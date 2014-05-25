package yMonotonePolygon.AlgorithmObjects;


public class UpdateInsertTreeSubEvent extends UpdateTreeSubEvent {

	/** 
	 * The new added edge.<br>
	 * Attention: This is just a clone and crossreference to tree does not work! 
	 */
	private final Edge newEdge;
	
	public UpdateInsertTreeSubEvent(int methodLine, SearchTree tree, Edge newEdge) {
		super(methodLine, tree);
		this.newEdge = newEdge;
	}

	public Edge getNewEdge() {
		return newEdge;
	}
}
