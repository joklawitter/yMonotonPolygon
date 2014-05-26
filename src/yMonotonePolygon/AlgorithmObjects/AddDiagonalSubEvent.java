package yMonotonePolygon.AlgorithmObjects;

public class AddDiagonalSubEvent extends SubEvent{

	private Edge diagonal; 
	
	public AddDiagonalSubEvent(int methodLine, Edge diagonal) {
		super(methodLine);
		this.diagonal = diagonal;
	}

	public Edge getDiagonal() {
		return diagonal;
	}
}
