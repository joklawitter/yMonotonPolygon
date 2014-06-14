package yMonotonePolygon.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import yMonotonePolygon.AlgorithmObjects.Method;

public class MethodPanel extends JPanel {
	
	private static final long serialVersionUID = 7889627964419596500L;

	private final int MAX_NUMBER_LINES = 7;
	
	private Method method;
	private String title;
	private JTextPane titlePane;
	
	private String[] text;
	private JPanel linesContainerPanel;
	private JTextPane[] lines;
	
	private int trueLine;
	private int falseLine;
	private int highlightedLine;	
	
	public MethodPanel() {
		init();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if (trueLine >= 0) {
			lines[trueLine].setBackground(GUIColorConfiguration.TRUE_LINE);
		}
		if (falseLine >= 0) {
			lines[falseLine].setBackground(GUIColorConfiguration.FALSE_LINE);		
		}
		if (highlightedLine >= 0) {
			lines[highlightedLine].setBackground(GUIColorConfiguration.HIGHLIGHTED_LINE);
		}
	}
	
	private void init() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setBackground(Color.WHITE);
		titlePane = new JTextPane();
		titlePane.setBackground(Color.WHITE);
		linesContainerPanel = new JPanel();
		linesContainerPanel.setBackground(Color.WHITE);
		linesContainerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		linesContainerPanel.setLayout(new BoxLayout(linesContainerPanel, BoxLayout.Y_AXIS));
		
		setMethod(Method.WELCOME);
		this.add(titlePane);
		this.add(linesContainerPanel);
		//setSize(this.getSize().width, 150);
	}

	// -- getter -- setter --
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		setTitlePane();
	}
	
	private void setTitlePane() {
		titlePane.setText(method.getName());
	}

	public String[] getTextlines() {
		return text;
	}

	public void setTextlines(String[] text) {
		checkTextlinesSize(text.length);
		this.text = text;
		setTextlines();
	}
	
	private void setTextlines() {
		resetLines();
		
		linesContainerPanel.removeAll();
		lines = new JTextPane[MAX_NUMBER_LINES];
		for (int i = 0; i < MAX_NUMBER_LINES; i++) {
			lines[i] = new JTextPane();
			if (i < text.length) { 
				lines[i].setText(text[i]);
			} else {
				lines[i].setText("");
			}
			linesContainerPanel.add(lines[i]);
		}
	}

	public void setMethod(Method method) {
		resetLines();

		this.method = method;
		setTitle(method.getName());
		setTextlines(method.getLines());
	}

	private void resetLines() {
		trueLine = -1;
		falseLine = -1;
		highlightedLine = -1;
	}

	// -- visualization features -- visualization features --
	
	public void highlightLine(int lineNumber) {
		checkLineNumber(lineNumber);
		highlightedLine =  lineNumber;
		lines[highlightedLine].setBackground(GUIColorConfiguration.HIGHLIGHTED_LINE);
	}

	public void setBooleanLineTrue(int lineNumber) {
		checkLineNumber(lineNumber);
		trueLine = lineNumber;
		
		lines[trueLine].setBackground(GUIColorConfiguration.TRUE_LINE);
	}
	
	public void setBooleanLineFalse(int lineNumber) {
		checkLineNumber(lineNumber);
		falseLine = lineNumber;
		lines[falseLine].setBackground(GUIColorConfiguration.FALSE_LINE);	
	}
	
	public void grayOutLine(int lineNumber) {
		checkLineNumber(lineNumber);
		// TODO maybe! gray out this line which will not be executed because bool event was false
	}
	
	// -- test helper methods -- check helper methods --
	private void checkTextlinesSize(int length) {
		if (length > MAX_NUMBER_LINES) {
			throw new IllegalArgumentException("To many lines to display in method section.");
		}
	}

	private void checkLineNumber(int lineNumber) {
		if ((lineNumber < 0) || (lineNumber >= text.length)) {
			throw new IllegalArgumentException("Illegal line number.");
		}
	}





}
