package yMonotonPolygon.AlgorithmObjects;

public abstract class SubEvent {
	/**
	 * The line of the method for which this SubEvent is.
	 */
	int methodLine;
	
	public SubEvent(int methodLine) {
		super();
		this.methodLine = methodLine;
	}
}
