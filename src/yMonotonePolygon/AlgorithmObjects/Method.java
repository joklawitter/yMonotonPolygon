package yMonotonePolygon.AlgorithmObjects;

public enum Method {
	HANLDE_START ("handleStartVertex(v)",
			new String[] 
					{"insert v.nextEdge in T",
					"set v.nextEdge.helper = v"}), 
	HANLDE_END ("handleEndVertex(v)",
			new String[] 
					{"if v.prevEdge.helper is merge vertex",
					"  then insert diagonal v-v.prevEdge.helper",
					"delete v.prevEdge from T"}), 
	HANLDE_SPLIT ("handleSplitVertex(v)",
			new String[] 
					{"search in T to find edge e directly left of v",
					"insert diagonal v-e.helper",
					"set e.helper = v",
					"insert v.nextEdge in T",
					"set v.nextEdge.helper = v"}), 
	HANLDE_MERGE ("handleMergeVertex(v)",
			new String[] 
					{"if v.prevEdge.helper is merge vertex",
					"  then insert diagonal v-v.prevEdge.helper",
					"delete v.prevEdge from T",
					"search in T to find edge e directly left of v",
					"if e.helper is merge vertex",
					"  then insert diagonal v-e.helper",
					"set e.helper = v"}), 
	HANLDE_LEFT_REGULAR ("handleRegularVertex(v)",
			new String[] 
					{"if v.prevEdge.helper is merge vertex",
					"    then insert diagonal v-v.prevEdge.helper",
					"delete v.prevEdge from T",
					"insert v.nextEdge in T",
					"set v.nextEdge.helper = v"}),	
	HANLDE_RIGHT_REGULAR ("handleRegularVertex(v)",
			new String[] 
					{"search in T to find edge e directly left of v",
					"if e.helper is merge vertex",
					"    then insert diagonal v-e.helper",
					"set e.helper = v"});
	
	private final String name;
	private final String[] lines;
	
	private Method(final String name, final String[] lines) {
		this.name = name;
		this.lines = lines;
	}

	public String getName() {
		return name;
	}

	
	public String[] getLines() {
		return lines;
	}


}
