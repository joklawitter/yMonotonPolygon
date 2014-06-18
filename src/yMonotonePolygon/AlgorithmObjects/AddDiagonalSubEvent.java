package yMonotonePolygon.AlgorithmObjects;

public class AddDiagonalSubEvent extends SubEvent{

	private int newNumberOfDiagonals; 
	
	public AddDiagonalSubEvent(int methodLine, int newNumberOfDiagonals) {
		super(methodLine);
		this.newNumberOfDiagonals = newNumberOfDiagonals;
	}

	public int getNewNumberOfDiagonals() {
		return newNumberOfDiagonals;
	}
}
