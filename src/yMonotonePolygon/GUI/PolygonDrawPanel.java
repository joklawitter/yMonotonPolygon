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
	private static final int EVENT_XPOSITION = 15;
	
	// whether the panel is in drawing mode or not (drawing new polygon)
	private boolean inDrawMode = false;
	// the lines and points for during the drawing mode
	private HashSet<Point> drawingPoints;
	private HashSet<Line2D> drawingLines;

	// attributes 
	private Polygon p;
    private LinkedList<Edge> diagonals;
    private int numberOfDiagonals;
    
    private TreeSet<Vertex> events;
    private int numberOfHandledEvents;
    
	private Vertex currentVertex;
    private HashSet<Edge> activeEdges;
    
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
				drawEvents(g2);
				
				if (activeEdges != null) {
					drawActiveEdges(g2);
				}
				
				// has to be drawn after active Edges
				drawVertices(g2);
				
				if (activeEdges != null) {
					drawActiveEdgesHelper(g2);
				}
				
				// but before helper
				
				if (currentVertex != null) {					
					drawSweepLine(g2);
					drawCurrentVertex(g2);
				}
				
				
				if (newHelper != null) {
					drawVertex(g2, newHelper);
					if (oldHelper != null) {
						drawVertex(g2, oldHelper);
					}
				}
				
				if (foundEdge != null) {
					signalFoundEdge(g2, foundEdge);
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
    public void setEvents(TreeSet<Vertex> events) {
		this.events = events;
	}
	public void setNumberOfHandledEvents(int numberOfHandledEvents) {
		this.numberOfHandledEvents = numberOfHandledEvents;
	}
	
	public void setCurrentVertex(Vertex currentVertex) {
		this.currentVertex = currentVertex;
	}

	public void setActiveEdges(HashSet<Edge> activeEdges) {
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
	
	public void reset(Vertex vertex) {
		this.setCurrentVertex(vertex);
		this.setNumberOfDiagonals(0);
		this.setActiveEdges(null);
		this.resetFoundEdge();
		this.resetNewHelper();
		this.resetOldHelper();
		this.setNumberOfHandledEvents(0);
		
		this.repaint();
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

	private void drawVertices(Graphics2D g2) {
		for (int i = 0; i < p.npoints; i++) {
		    drawVertex(g2, new Vertex(p.xpoints[i], p.ypoints[i], GUIColorConfiguration.EDGE_STD_COLOR));
		    //drawSweepLineEvent(g2, p.ypoints[i]);
		}
	}
	
	private void drawActiveEdges(Graphics2D g2) {
		Stroke s = g2.getStroke();
		g2.setStroke(new BasicStroke(LINE_SIZE));

		for (Edge e : activeEdges) {
			drawLine(g2, e);
		}
		g2.setStroke(s);
	}
	
	private void drawActiveEdgesHelper(Graphics2D g2) {	
		for (Edge e : activeEdges) {
			drawVertex(g2, e.getHelper());
		}
	}

	private void drawDiagonals(Graphics2D g2) {
		for (int i = 0; i < numberOfDiagonals; i++) {
			drawLine(g2, diagonals.get(i));
		}
	}

	private void drawEvents(Graphics2D g2) {
		g2.setColor(GUIColorConfiguration.HANDLED_EVENT);
		int i = 0;
		int oldY = -1;
		Vertex currentEventVertex = null;
		for (Vertex v : events) {
			if (i == numberOfHandledEvents) { // switch to not current event color
				currentEventVertex = v; // store the current event vertex to draw it last (so that it lies on top)
				oldY = currentEventVertex.getY();
				g2.setColor(GUIColorConfiguration.UNHANDLED_EVENT);
				i++;
				continue;
			}
			
			if ((v.getY() == oldY) && (i > numberOfHandledEvents)) {
				i++;
				continue; 		// we have more than one event on the current y position, 
								// to not overdraw the current event, we continue here 
			}
			
			drawEvent(g2, v);
			
			oldY = v.getY();
			i++;
		}		
		
		if (currentEventVertex != null) { // at last draw the current Event
			g2.setColor(GUIColorConfiguration.CURRENT_EVENT);
			drawEvent(g2, currentEventVertex);			
		}
	}
	
	private void drawEvent(Graphics2D g2, Vertex v) {
		Stroke s = g2.getStroke();
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(EVENT_XPOSITION, v.getY() - 1, POINT_SIZE, POINT_SIZE);
		g2.setStroke(s);
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
		g2.drawOval(v.getX(), v.getY(), POINT_SIZE, POINT_SIZE);
		g2.setStroke(s);
	}

	/*private void drawSweepLineEvent(Graphics2D g2, int yPos) {
	    Stroke s = g2.getStroke();
	    g2.setStroke(new BasicStroke(3));
	    g2.setColor(Color.black);
	    g2.drawRect(5, yPos - 1, POINT_SIZE, POINT_SIZE);
	    g2.setStroke(s);
	}*/
	
	private void drawSweepLine(Graphics2D g2) {
		Stroke s = g2.getStroke();
		g2.setStroke(new BasicStroke(1));
		g2.setColor(GUIColorConfiguration.SWEEP_LINE);
		int atY = currentVertex.getY();
		g2.drawLine(5, atY, this.getWidth() - 5, atY);		
		g2.setStroke(s);		
	}
	
	private void signalFoundEdge(Graphics2D g2, Edge foundEdge2) {
		
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
	    this.events = null;
	    numberOfHandledEvents = -1;
	    
		repaint();
	}

	// -- methods for during drawing mode! -- just for drawing mode! --
	public void drawPoint(int x, int y) {
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		Stroke s = g2.getStroke();
		g2.setStroke(new BasicStroke(3));
		drawingPoints.add(new Point(x, y));
		g2.drawOval(x, y, POINT_SIZE, POINT_SIZE);
		g2.setStroke(s);
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
