package yMonotonePolygon.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.Vertex;

public class PolygonDrawPanel extends JPanel {

	private static final long serialVersionUID = 3139953215271841299L;
	
	private static final int POINT_SIZE = 2;
	private static final int LINE_SIZE = 2;
	
	// whether the panel is in drawing mode or not (drawing new polygon)
	private boolean inDrawMode = false;
	// the lines and points for during the drawing mode
	private HashSet<Point> drawingPoints;
	private HashSet<Line2D> drawingLines;

	// attributes 
	private Polygon p;
    private LinkedList<Edge> diagonals;
    private int numberOfDiagonals;
    private Vertex currentVertex;
    private TreeSet<Edge> activeEdges;
    
    private Vertex oldHelper;
    private Vertex newHelper;
    private Edge foundEdge;
    
	// write everything to paint also in here or get g2 to draw on
	// if it gets drawn here it will be repainted when windows size is changed
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if (inDrawMode) {
			drawRedBorder(g2);		
			for (Point p : drawingPoints) {
				g2.drawOval(p.x - 1, p.y - 1, POINT_SIZE, POINT_SIZE);
			}		
			for (Line2D l : drawingLines) {
				g2.draw(l);
			}
		} else if (p != null) {
				drawPolygon(g2);
				drawDiagonals(g2);
				if (currentVertex != null) {					
					drawSweepLine(g2);
					drawCurrentVertex(g2);
				}
				if (activeEdges != null) {
					drawActiveEdgesAndHelper(g2);
				}
		}
	}
	
	public PolygonDrawPanel() {
		this.setBackground(GUIColorConfiguration.POLYGON_BACKGROUND);
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		this.setMinimumSize(new Dimension(1600,600));
	}
	
	// -- setter methods for during the algorithm -- keep the things updated to have the right thins painted --
	public void setP(Polygon p) {
		clear();
		inDrawMode = false;
		this.p = p;
	}

	public void setDiagonals(LinkedList<Edge> diagonals) {
		this.diagonals = diagonals;
	}

	public void setNumberOfDiagonals(int numberOfDiagonals) {
		this.numberOfDiagonals = numberOfDiagonals;
	}
	
	public void setCurrentVertex(Vertex currentVertex) {
		this.currentVertex = currentVertex;
	}

	public void setActiveEdges(TreeSet<Edge> activeEdges) {
		this.activeEdges = activeEdges;
	}
	
	public void setOldHelper(Vertex oldHelper) {
		this.oldHelper = oldHelper;
	}

	public void setNewHelper(Vertex newHelper) {
		this.newHelper = newHelper;
	}

	public void setFoundEdge(Edge foundEdge) {
		this.foundEdge = foundEdge;
	}
	
	public void resetOldHelper() {
		this.oldHelper = null;
	}

	public void resetNewHelper() {
		this.newHelper = null;
	}

	public void resetFoundEdge() {
		this.foundEdge = null;
	}
	
	
	// -- drawing methods for during the algorithm --
	private void drawPolygon(Graphics2D g2) {
		g2.setColor(GUIColorConfiguration.POLYGON_INSIDE);
		g2.fillPolygon(p);
		g2.setColor(GUIColorConfiguration.EDGE_STD_COLOR);
		g2.drawPolygon(p);
	}
	
	private void drawActiveEdgesAndHelper(Graphics2D g2) {
		Stroke s = g2.getStroke();
		g2.setStroke(new BasicStroke(LINE_SIZE));
		//System.out.println(activeEdges.size());
		for (Edge e : activeEdges) {
			drawLine(g2, e);
			drawVertex(g2, e.getHelper());
		}
		g2.setStroke(s);
	}

	private void drawDiagonals(Graphics2D g2) {
		for (int i = 0; i < numberOfDiagonals; i++) {
			drawLine(g2, diagonals.get(i));
		}
	}

	private void drawLine(Graphics2D g2, Edge edge) {
		g2.setColor(edge.getColor());
		g2.drawLine(edge.getStartVertex().getX(), edge.getStartVertex().getY(), 
				edge.getEndVertex().getX(), edge.getEndVertex().getY());
	}

	private void drawCurrentVertex(Graphics2D g2) {
		drawVertex(g2, currentVertex);
	}

	private void drawVertex(Graphics2D g2, Vertex v) {
		Stroke s = g2.getStroke();
		g2.setStroke(new BasicStroke(3));
		g2.setColor(v.getColor());
		g2.drawOval(v.getX() - 1, v.getY() - 1, POINT_SIZE, POINT_SIZE);
		g2.setStroke(s);
	}

	private void drawSweepLine(Graphics2D g2) {
		Stroke s = g2.getStroke();
		g2.setStroke(new BasicStroke(1));
		g2.setColor(GUIColorConfiguration.SWEEP_LINE);
		int atY = currentVertex.getY();
		g2.drawLine(5, atY, this.getWidth() - 5, atY);		
		g2.setStroke(s);		
	}
	
	public void clear() {
		inDrawMode = false;
		// the lines and points for during the drawing mode
		drawingPoints = null;
		drawingLines = null;

		// attributes 
		p = null;
	    diagonals  = null;
	    numberOfDiagonals  = 0;
	    currentVertex  = null;
	    activeEdges  = null;
	    
		repaint();
	}

	// -- methods for during drawing mode! -- just for drawing mode! --
	public void drawPoint(int x, int y) {
		drawingPoints.add(new Point(x, y));
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		g2.drawOval(x - 1, y - 1, POINT_SIZE, POINT_SIZE);
	}

	public void drawLine(int x, int y, int i, int j) {
		Line2D.Double l = new Line2D.Double(x, y, i, j);
		drawingLines.add(l);
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		g2.draw(l);
	}

	public void setDrawMode() {
		clear();

		inDrawMode = true;
		drawingPoints = new HashSet<Point>();
		drawingLines = new HashSet<Line2D>();
	}

	private void drawRedBorder(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(5));
		g2.drawRect(3, 3, this.getWidth() - 8, this.getHeight() - 8);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
	}
	
}
