package yMonotonePolygon.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;






import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.SweepLineEvent;
import yMonotonePolygon.PraeComputation.PraeComputer;


public class PolygonDrawPanel extends JPanel {

	private static final long serialVersionUID = 3139953215271841299L;
	
	private Polygon p;
	
	public void setP(Polygon p) {
		this.p = p;
	}

	public PolygonDrawPanel() {
		setBackground(Color.GRAY);
		setBorder(BorderFactory.createLoweredBevelBorder());
		setSize(1600,600);
	}
	
	
	public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 	    
		 drawPolygon(g);
		 PraeComputer pc = new PraeComputer();
		 pc.work(p);
		 LinkedList<Edge> diagonals = pc.getDiagonals();
		 g.setColor(Color.RED);
		 for (Edge e : diagonals) {
			 g.drawLine(e.getStartVertex().getX(), e.getStartVertex().getY(), e.getEndVertex().getX(), e.getEndVertex().getY());
		 }
		   
	}



	private void drawPolygon(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);
	}
	

}
