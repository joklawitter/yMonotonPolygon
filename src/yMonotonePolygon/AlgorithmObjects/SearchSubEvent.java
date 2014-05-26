package yMonotonePolygon.AlgorithmObjects;

public class SearchSubEvent extends SubEvent {

	private Edge foundEdge;
	
	public SearchSubEvent(int methodLine, Edge foundEdge) {
		super(methodLine);
		this.foundEdge = foundEdge;
	}

	public Edge getFoundEdge() {
		return foundEdge;
	}
	
}
