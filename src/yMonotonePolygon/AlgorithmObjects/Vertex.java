package yMonotonePolygon.AlgorithmObjects;

import java.awt.Color;

import yMonotonePolygon.GUI.GUIColorConfiguration;
import yMonotonePolygon.PraeComputation.Geometry;

public class Vertex implements Comparable<Vertex> {

	private int x;
	private int y;
	
	private VertexType type;
	
	private Vertex prev;
	private Vertex next;
	
	private Edge prevEdge;
	private Edge nextEdge;	
	
	private Color color = GUIColorConfiguration.VERTEX_STD_COLOR;
	private Edge helpedEdge;
	private boolean helping;
	
	
	public Vertex(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	
	// checking the type
	public VertexType getVertexType() {
		return type;
	}

	public boolean isStartVertex() {
		return (this.getVertexType() == VertexType.START);
	}
	public boolean isEndVertex() {
		return (this.getVertexType() == VertexType.END);
	}
	public boolean isSplitVertex() {
		return (this.getVertexType() == VertexType.SPLIT);
	}
	public boolean isMergeVertex() {
		return (this.getVertexType() == VertexType.MERGE);
	}
	public boolean isRegularLeftVertex() {
		return (this.getVertexType() == VertexType.REGULAR_LEFT);
	}
	public boolean isRegularRightVertex() {
		return (this.getVertexType() == VertexType.REGULAR_RIGHT);
	}
	
	public Method getCorrespondingMethod() {
		return this.getVertexType().getCorrespondingMethod();
	}
	
	public Vertex getPrev() {
		return prev;
	}

	public void setPrev(Vertex prev) {
		this.prev = prev;
	}

	public Vertex getNext() {
		return next;
	}

	public void setNext(Vertex next) {
		this.next = next;
	}
	
	
	public Edge getPrevEdge() {
		return prevEdge;
	}
	
	public void setPrevEdge(Edge prevEdge) {
		this.prevEdge = prevEdge;
	}

	public Edge getNextEdge() {
		return nextEdge;
	}

	public void setNextEdge(Edge nextEdge) {
		this.nextEdge = nextEdge;
	}

	public boolean computeVertexType() { 
		// it is ensured that we go counterclockwise through the polygon
		if (Geometry.isLowerThan(this, this.getPrev()) && Geometry.isLowerThan(this.getNext(), this)) {  // y-monotone down
			this.type = VertexType.REGULAR_LEFT;
		} else if (Geometry.isLowerThan(this.getPrev(), this) && Geometry.isLowerThan(this, this.getNext())) { // y-monotone up
			this.type = VertexType.REGULAR_RIGHT;
		} else if (Geometry.isLowerThan(this, this.getPrev()) && Geometry.isLowerThan(this, this.getNext())) { // end or merge
			if (Geometry.liesLeftOfLine(this.getPrevEdge(), this.getNext())) {
				this.type = VertexType.END;
			} else {
				this.type = VertexType.MERGE;
			}
		} else if (Geometry.isLowerThan(this.getPrev(), this) && Geometry.isLowerThan(this.getNext(), this)) { // start or split
			if (Geometry.liesLeftOfLine(this.getPrevEdge(), this.getNext())) {
				this.type = VertexType.START;
			} else {
				this.type = VertexType.SPLIT;
			}
		} else {
			System.out.println("VertexType could not be determined for Vertex : x=" + getX() + ", y=" + getY());
			printNeighbours();
			return false;
		}
		
		return true;		
	}



	public void setHelpedEdge(Edge e) {
		this.helpedEdge = e;
		this.color = e.getColor();
		this.helping = true;
	}

	public void setNotHelping() {
		this.helpedEdge = null;
		this.color = GUIColorConfiguration.VERTEX_STD_COLOR;
		this.helping = false;
	}
	
	@Override
	public int compareTo(Vertex other) {
		return (Geometry.isLowerThan(other, this)) ? -1 : 1;
	}

	/**
	 * Clones this vertex. 
	 * Clones are just used for the view, so they don't need all information.
	 * @return a clone of this vertex with x, y, color and helperEdge set
	 */
	public Vertex clone() {
		Vertex cloned = new Vertex(getX(), getY());
		cloned.color = this.color;
		cloned.helpedEdge = this.helpedEdge;
		cloned.helping = this.helping;
		
		return cloned;
	}

	@Override
	public String toString() {
		return getVertexType().toString() + " (" + x + ", " + y +")";
	}
	
	public String toShortString() {
		return "V(" + x + ", " + y + ")";
	}
	
	private void printNeighbours() {
		if (getPrev() != null) {
			System.out.println("Prev Vertex: " + getPrev().toShortString());
		} else {
			System.out.println("No previous Vertex set.");
		}
		
		if (getNext() != null) {
			System.out.println("Next Vertex: " + getNext().toShortString());
		} else {
			System.out.println("No next Vertex set.");
		}
		
		if (getNext() != null) {
			System.out.println("Next Vertex: " + getNext().toShortString());
		} else {
			System.out.println("No next Vertex set.");
		}
		
	}

}
