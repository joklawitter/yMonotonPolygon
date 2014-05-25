package yMonotonePolygon.AlgorithmObjects;

public abstract class UpdateTreeSubEvent extends SubEvent {

	/** binary search tree of the active edges */
	private SearchTree tree;
	
	public UpdateTreeSubEvent(int methodLine, SearchTree tree) {
		super(methodLine);
		this.tree = tree;
	}
}
