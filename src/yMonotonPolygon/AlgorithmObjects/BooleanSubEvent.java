package yMonotonPolygon.AlgorithmObjects;

public class BooleanSubEvent extends SubEvent {

	private boolean whyIAmExisting;

	public BooleanSubEvent(int methodLine, boolean outcome) {
		super(methodLine);
		this.whyIAmExisting = outcome;
	}
	
	
	public boolean isBooleanEvent() {
		return true;
	}
	
	public boolean getBooleanEventOutcome() {
		return whyIAmExisting;
	}

}
