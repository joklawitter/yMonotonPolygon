package yMonotonePolygon.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;

import yMonotonePolygon.AlgorithmObjects.Vertex;

public class TreeStatusPanel extends JPanel {

	private static final long serialVersionUID = -8798865147286388642L;
	
	public JLabel title;
	public TreeStatusPanel() {
		this.setMaximumSize(new Dimension(10000, 50));
		this.setPreferredSize(new Dimension(600, 50));
		this.setMinimumSize(new Dimension(1, 100));
		this.setBackground(Color.green);
	}
	
	public void draw(TreeSet<Vertex> vertices) {
		// TODO
	}
}
