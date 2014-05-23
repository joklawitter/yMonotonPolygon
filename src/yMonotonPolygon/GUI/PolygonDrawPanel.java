package yMonotonPolygon.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.BorderFactory;
import javax.swing.JPanel;



import yMonotonPolygon.AlgorithmObjects.SweepLineEvent;


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
	
	public void paint(Polygon p, SweepLineEvent s) {

	}
	
	public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 	    
		 drawPolygon(g);
		 drawSweepLineStatus(g);		   
	}



	private void drawPolygon(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);
	}
	
	private void drawSweepLineStatus(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}
