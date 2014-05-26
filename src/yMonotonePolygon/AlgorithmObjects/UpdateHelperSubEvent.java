package yMonotonePolygon.AlgorithmObjects;

public class UpdateHelperSubEvent extends SubEvent {

	/** The new helper vertex. */
	private Vertex newHelper;
	
	/** The old helper vertex. */
	private Vertex oldHelper;
	
	/** Whether the edge had a helper before. */
	private boolean hadHelperBefore = false;
	
	public UpdateHelperSubEvent(int methodLine, Vertex newHelper) {
		super(methodLine);
		this.newHelper = newHelper;
	}
	
	public UpdateHelperSubEvent(int methodLine, Vertex newHelper, Vertex oldHelper) {
		this(methodLine, newHelper);
		this.oldHelper = oldHelper;
		if (oldHelper != null) {
			this.hadHelperBefore = true;
		}
	}

	public Vertex getNewHelper() {
		return newHelper;
	}

	public Vertex getOldHelper() {
		return oldHelper;
	}

	public boolean hadHelperBefore() {
		return hadHelperBefore;
	}
}
