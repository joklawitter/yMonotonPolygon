package yMonotonPolygon.AlgorithmObjects;

public class AddDiagonalSubEvent extends SubEvent{

	private Line diagonal; 
	
	public AddDiagonalSubEvent(int methodLine, Line diagonal) {
		super(methodLine);
		this.diagonal = diagonal;
	}

}
