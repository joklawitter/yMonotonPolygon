package yMonotonePolygon.AlgorithmObjects;

public enum VertexType {
	START (Method.HANDLE_START), 
	END (Method.HANDLE_END), 
	SPLIT (Method.HANDLE_SPLIT), 
	MERGE (Method.HANDLE_MERGE), 
	REGULAR_LEFT (Method.HANDLE_LEFT_REGULAR), 
	REGULAR_RIGHT (Method.HANDLE_RIGHT_REGULAR);
	
	private final Method m;
	
	private VertexType(Method m) {
		this.m = m;
	}

	public Method getCorrespondingMethod() {
		return m;
	}
	
	public String toString() {
		if (this == START) {
			return "StartVertex";
		} else if (this == END) {
			return "EndVertex";
		} else if (this == SPLIT) {
			return "SplitVertex";
		} else if (this == MERGE) {
			return "MergeVertex";
		} else if (this == REGULAR_LEFT) {
			return "RegularLeftVertex";
		} else {
			return "RegularRightVertex";
		}
	}
}
