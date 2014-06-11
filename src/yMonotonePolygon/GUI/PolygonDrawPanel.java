package yMonotonePolygon.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import yMonotonePolygon.AlgorithmObjects.Edge;

public class PolygonDrawPanel extends JPanel {

	private static final long serialVersionUID = 3139953215271841299L;
	
	private Polygon p;
		
	private boolean inDrawMode = false;
	
	private HashSet<Point> points = new HashSet<Point>();
	private HashSet<Line2D> lines = new HashSet<Line2D>();

	
	private static final int POINT_SIZE = 2;
	private static final int LINE_SIZE = 1;
		
	// write everything to paint also in here or get g2 to draw on
	// if it gets drawn here it will be repainted when windows size is changed
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if (inDrawMode) {
			drawRedBorder(g2);
			
			for (Point p : points) {
				g2.drawOval(p.x - 1, p.y - 1, POINT_SIZE, POINT_SIZE);
			}
			
			for (Line2D l : lines) {
				g2.draw(l);
			}
		}
		

		if (p != null) {
			drawPolygon(g2);
			// TODO draw diagonals, active edges and helper, ...
		}
		
		
	}
	
	
	public void setP(Polygon p) {
		inDrawMode = false;
		this.p = p;
	}

	public PolygonDrawPanel() {
		this.setBackground(Color.GRAY);
		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		this.setMinimumSize(new Dimension(1600,600));

	}
	
	private void drawPolygon(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.fillPolygon(p);
		g2.setColor(Color.BLACK);
		g2.drawPolygon(p);
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

	
	public void clear() {
		// TODO	
	}

	public void drawPoint(int x, int y) {
		points.add(new Point(x, y));
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		g2.drawOval(x - 1, y - 1, POINT_SIZE, POINT_SIZE);
	}

	public void drawLine(int x, int y, int i, int j) {
		Line2D.Double l = new Line2D.Double(x, y, i, j);
		lines.add(l);
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		g2.draw(l);
	}

	public void setDrawMode() {
		inDrawMode = true;
	}


	
	private void drawRedBorder(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(5));
		g2.drawRect(3, 3, this.getWidth() - 8, this.getHeight() - 8);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
	}
	
	
	
	
}
