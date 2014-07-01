package yMonotonePolygon.GUI;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import yMonotonePolygon.AlgorithmObjects.Vertex;

public class TreeStatusPanel extends JPanel {

    private static final long serialVersionUID = -8798865147286388642L;

    private static final int DIAMETER = 11;
    private static final int Y_POSITION = 15;

    public JLabel title;

    private TreeSet<Vertex> vertices;

    private Vertex searchPoint;

    public TreeStatusPanel() {
        this.setPreferredSize(new Dimension(600, 40));
        this.setMinimumSize(new Dimension(1, 40));
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setBackground(GUIColorConfiguration.DATASTRUCTURE_BACKGROUND);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (searchPoint != null) {
            g2.setColor(GUIColorConfiguration.CURRENT_EVENT);
            drawSearchLine(g2, searchPoint);
        }

        if (vertices != null) {
            for (Vertex v : vertices) {
                g2.setColor(v.getColor());
                drawVertex(g2, v);
            }
        }
    }

    private void drawSearchLine(Graphics2D g2, Vertex v) {
        g2.fillRect(v.getX(), 0, 3, this.getHeight());
    }

    private void drawVertex(Graphics2D g2, Vertex v) {
        Stroke s = g2.getStroke();
        g2.setStroke(new BasicStroke(5));
        Ellipse2D.Double circle = new Ellipse2D.Double(v.getX() - (DIAMETER/2.0) + 1.5, Y_POSITION, DIAMETER, DIAMETER);
        g2.fill(circle);
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

    public void reset() {
        vertices = null;
        searchPoint = null;
        this.repaint();
    }
}
