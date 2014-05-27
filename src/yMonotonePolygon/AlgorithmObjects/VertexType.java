package yMonotonePolygon.AlgorithmObjects;

public enum VertexType {
	START (Method.HANLDE_START), 
	END (Method.HANLDE_END), 
	SPLIT (Method.HANLDE_SPLIT), 
	MERGE (Method.HANLDE_MERGE), 
	REGULAR_LEFT (Method.HANLDE_REGULAR), 
	REGULAR_RIGHT (Method.HANLDE_REGULAR);
	
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
