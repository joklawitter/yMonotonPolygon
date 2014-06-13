package yMonotonePolygon.GUI;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import yMonotonePolygon.AlgorithmObjects.Vertex;

public class TreeStatusPanel extends JPanel {

	private static final long serialVersionUID = -8798865147286388642L;
	
	public JLabel title;
	
	private TreeSet<Vertex> vertices;

	private Vertex searchPoint;
	
	public TreeStatusPanel() {
		this.setMaximumSize(new Dimension(10000, 40));
		this.setPreferredSize(new Dimension(600, 40));
		this.setMinimumSize(new Dimension(1, 100));
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		this.setBackground(GUIColorConfiguration.DATASTRUCTURE_BACKGROUND);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if (vertices != null) {
			for (Vertex v : vertices) {
				g2.setColor(v.getColor());
				drawVertex(g2, v);
			}
		}
		
		if (searchPoint != null) {
			g2.setColor(GUIColorConfiguration.CURRENT_EVENT);
			drawVertex(g2, searchPoint);
		}
		
	}
	
	private void drawVertex(Graphics2D g2, Vertex v) {
		Stroke s = g2.getStroke();
		g2.setStroke(new BasicStroke(3));
		g2.drawOval(v.getX(), 20, 2, 2);
		g2.setStroke(s);
	}

	public void setDataStructure(TreeSet<Vertex> xPoints) {
		this.vertices = xPoints;
	}
	
	public void setSearchPoint(Vertex x) {
		this.searchPoint = x;
	}
	
	public void clearSearchPoint() {
		this.searchPoint = null;
	}
}
