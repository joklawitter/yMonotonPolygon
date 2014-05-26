package yMonotonePolygon.AlgorithmObjects;

public class BooleanSubEvent extends SubEvent {

	private boolean whyIAmExisting;

	public BooleanSubEvent(int methodLine, boolean outcome) {
		super(methodLine);
		this.whyIAmExisting = outcome;
	}
		
	/**
	 * Returns the outcome of this boolean SubEvent.
	 * @return  the outcome of this boolean SubEvent.
	 */
	public boolean getBooleanEventOutcome() {
		return whyIAmExisting;
	}

}
