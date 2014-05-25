package yMonotonePolygon.AlgorithmObjects;

import java.awt.Color;

import yMonotonePolygon.GUI.GUIColorConfiguration;

public class Edge {

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

	public Vertex getHelper() {
		return helper;
	}

	public void setHelper(Vertex helper) {
		if (getHelper() == null) {
			getHelper().setNotHelping();
		}
		this.helper = helper;
		helper.setHelpedEdge(this);
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
		cloned.setHelper(this.getHelper().clone());
		return cloned;
	}
}
