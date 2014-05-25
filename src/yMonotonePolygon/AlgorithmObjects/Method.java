package yMonotonePolygon.AlgorithmObjects;

public enum Method {
	HANLDE_START ("handleStartVertex"), 
	HANLDE_END ("handleEndVertex"), 
	HANLDE_SPLIT ("handleSplitVertex"), 
	HANLDE_MERGE ("handleMergeVertex"), 
	HANLDE_REGULAR ("handleRegularVertex");
	
	private final String name;
	//private String[] lines;
	// TODO
	
	private Method(final String name) {
		this.name = name;
		//this.lines = lines;
	}

	public String getName() {
		return name;
	}
}
