package yMonotonePolygon.AlgorithmObjects;

import java.awt.Color;

import yMonotonePolygon.GUI.GUIColorConfiguration;

public class Edge implements Comparable<Edge> {

	private Vertex start;
	private Vertex end;
	
	private Color color = GUIColorConfiguration.EDGE_STD_COLOR;
	
	private Vertex helper;
	
	public Edge(Vertex start, Vertex end) {
		if ((start == null) || (end == null)) {
			throw new IllegalArgumentException();
		} 
		
		this.start = start;
		this.end = end;
	}

	public static Edge diagonalFactory(Vertex start, Vertex end) {
		Edge e = new Edge(start, end);
		e.setColor(GUIColorConfiguration.DIAGONAL);

		return e;
	}
 
	public Vertex getHelper() {
		return helper;
	}

	public void setHelper(Vertex helper) {
		releaseHelper();
		this.helper = helper;
		helper.setHelpedEdge(this);
	}


	/**
	 * Releases a vertex of duty as helper. 
	 * @return the vertex released from helping
	 */
	public Vertex releaseHelper() {
		Vertex oldHelper = getHelper();
		if (oldHelper != null) {
			oldHelper.setNotHelping();
		}
		return oldHelper;
	}

		
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Vertex getStartVertex() {
		return start;
	}

	public Vertex getEndVertex() {
		return end;
	}
	
	public Edge clone() {
		Edge cloned = new Edge(getStartVertex(), getEndVertex());
		cloned.setColor(this.getColor());
		if (getHelper() != null) {			
			cloned.setHelper(this.getHelper().clone());
		}
		return cloned;
	}

	@Override
	public String toString() {
		String s = "Edge [start=" + start + ", end=" + end + ", color=" + color;
		if (helper != null) {
			s += ", helper=" + helper + "]";
		}
		s += "]";
		return s;
	}

	
	@Override
	public int compareTo(Edge o) {
		return 0;//Integer.compare(this.getStartVertex().getX(), o.getStartVertex().getX());
	}


	
	
}
