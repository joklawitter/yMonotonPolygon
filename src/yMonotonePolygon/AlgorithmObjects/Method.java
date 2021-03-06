package yMonotonePolygon.AlgorithmObjects;

/**
 * Enum to store all the handle methods and some info texts.
 * @author Jopunkt
 *
 */
public enum Method {
	HANDLE_START ("handleStartVertex(v)",
			new String[] 
					{"insert v.nextEdge in T",
					"set v.nextEdge.helper = v"}), 
	HANDLE_END ("handleEndVertex(v)",
			new String[] 
					{"if v.prevEdge.helper is merge vertex",
					"  then insert diagonal v-v.prevEdge.helper",
					"delete v.prevEdge from T"}), 
	HANDLE_SPLIT ("handleSplitVertex(v)",
			new String[] 
					{"search in T to find edge e directly left of v",
					"insert diagonal v-e.helper",
					"set e.helper = v",
					"insert v.nextEdge in T",
					"set v.nextEdge.helper = v"}), 
	HANDLE_MERGE ("handleMergeVertex(v)",
			new String[] 
					{"if v.prevEdge.helper is merge vertex",
					"  then insert diagonal v-v.prevEdge.helper",
					"delete v.prevEdge from T",
					"search in T to find edge e directly left of v",
					"if e.helper is merge vertex",
					"  then insert diagonal v-e.helper",
					"set e.helper = v"}), 
	HANDLE_LEFT_REGULAR ("handleLeftRegularVertex(v)",
			new String[] 
					{"if v.prevEdge.helper is merge vertex",
					"    then insert diagonal v-v.prevEdge.helper",
					"delete v.prevEdge from T",
					"insert v.nextEdge in T",
					"set v.nextEdge.helper = v"}),	
	HANDLE_RIGHT_REGULAR ("handleRightRegularVertex(v)",
			new String[] 
					{"search in T to find edge e directly left of v",
					"if e.helper is merge vertex",
					"    then insert diagonal v-e.helper",
					"set e.helper = v"}), 
	WELCOME ("Welcome!",
			new String[]
					{"This is the y-monotone polygon algorithm visualization.",
					"You can either open a polygon file or draw a polygon."}), 
	DRAW_MODE ("Draw mode!",
			new String[]
					{"This is the drawing mode for polygons.",
					"- Click into the the panel to add new points.",
					"- Click near the start point to close the polygon.",
					"- Click near last drawn point to remove it from polygon.",
					"Be aware that the polygon should be simple, i.e. there are no self intersections."}), 
	ERROR_NOT_SIMPLE ("Illegal Polygon",
			new String[]
					{"The polygon may not intersect itself!",
					"Make sure that the polygon is simple.",
					"To avoid intersections in draw mode click near the last set point to undo it."}),
	ERROR_ILLEGAL_FILE ("Illegal File",
			new String[]
					{"File must be in correct format!",
					"- .txt File",
					"- example: \"polygon 150,100 100,150 150,200 200,150\" "}),				
	START ("Y-Monotone Polygon Algorithm",
			new String[]
					{"Press PLAY or go step by step or event by event through the algorithm. "});
	
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
