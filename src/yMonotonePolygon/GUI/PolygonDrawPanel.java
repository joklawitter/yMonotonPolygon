package yMonotonePolygon.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.PraeComputation.PraeComputer;


public class PolygonDrawPanel extends JPanel {

	private static final long serialVersionUID = 3139953215271841299L;
	
	private Polygon p;
	
	private Graphics g;
	
	public void setP(Polygon p) {
		this.p = p;
	}

	public PolygonDrawPanel() {
		this.setBackground(Color.GRAY);
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		this.setMinimumSize(new Dimension(1600,600));
		this.g = this.getGraphics();
	}
	
	
	public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 
		 if (p != null) {
			 drawPolygon(g);
			 PraeComputer pc = new PraeComputer();
			 pc.work(p);
			 LinkedList<Edge> diagonals = pc.getDiagonals();
			 g.setColor(Color.RED);
			 for (Edge e : diagonals) {
				 g.drawLine(e.getStartVertex().getX(), e.getStartVertex().getY(), e.getEndVertex().getX(), e.getEndVertex().getY());
			 }
		 }   
	}

	private void drawPolygon(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);
	}
	
	public void drawSweepLineAt(int yPosition) {
		// TODO draw the sweepline at the given y position
	}
	
	public void drawDiagonalsTo(LinkedList<Edge> diagonals, int numberOfDiagonalsToDraw) {
		// TODO  draw the diagonals of the given list up to the given index
	}
	
	public void drawActiveEdgesAndHelper(TreeSet<Edge> edges) {
		// TODO draw the active edges (those crossing the sweep line in their own color
		// TODO draw also their helper vertices in same color
	}
	
	
}
