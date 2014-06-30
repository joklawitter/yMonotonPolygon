package yMonotonePolygon.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

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
		
		
		if (highlightedLine >= 0) {
		    lines[highlightedLine].setForeground(Color.black);
			lines[highlightedLine].setBackground(GUIColorConfiguration.HIGHLIGHTED_LINE);
		}
		if (trueLine >= 0) {
//		    StyleContext context = new StyleContext();
//		    Style style = context.addStyle("", null);
//		    StyleConstants.setForeground(style, Color.white);
		    
		    lines[trueLine].setForeground(Color.white);
			lines[trueLine].setBackground(GUIColorConfiguration.TRUE_LINE);
		}
		if (falseLine >= 0) {
		    lines[falseLine].setForeground(Color.white);
			lines[falseLine].setBackground(GUIColorConfiguration.FALSE_LINE);
		}
		
		//linesContainerPanel.revalidate();
	}
	
	private void init() {
        //this.setMaximumSize(new Dimension(10000, 200));
        this.setPreferredSize(new Dimension(1000, 200));
        this.setMinimumSize(new Dimension(1, 200));
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setBackground(GUIColorConfiguration.METHOD_BACKGROUND);
		titlePane = new JTextPane();
		titlePane.setBackground(GUIColorConfiguration.METHOD_BACKGROUND);
		linesContainerPanel = new JPanel();
		linesContainerPanel.setBackground(GUIColorConfiguration.METHOD_BACKGROUND);
		linesContainerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		linesContainerPanel.setLayout(new BoxLayout(linesContainerPanel, BoxLayout.Y_AXIS));
		
		setMethod(Method.WELCOME);
		resetLines();
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
		titlePane.setText(this.title);
	}

	public String[] getTextlines() {
		return text;
	}

	public void setTextlines(String[] text) {
		resetLines();
		checkTextlinesSize(text.length);
		this.text = text;
		setTextlines();
	}
	
	private void setTextlines() {
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
		linesContainerPanel.revalidate();
		linesContainerPanel.repaint();		
	}

	public void setMethod(Method method) {
		this.method = method;
		setTitle(this.method.getName());
		setTextlines(this.method.getLines());
	}

	private void resetLines() {
		trueLine = -1;
		falseLine = -1;
		highlightedLine = -1;
	}

	// -- visualization features -- visualization features --
	
	public void highlightLine(int lineNumber) {
		if ((highlightedLine >= 0) && (highlightedLine != trueLine) && (highlightedLine != falseLine)) {			
			lines[highlightedLine].setBackground(GUIColorConfiguration.METHOD_BACKGROUND);
		}
		checkLineNumber(lineNumber);
		highlightedLine =  lineNumber;
		lines[highlightedLine].setBackground(GUIColorConfiguration.HIGHLIGHTED_LINE);
	}

	public void setBooleanLine(int line, boolean booleanEventOutcome) {
		if (booleanEventOutcome) {
			setBooleanLineTrue(line);
		} else {
			setBooleanLineFalse(line);
		}
	}
	
	public void setBooleanLineTrue(int lineNumber) {
		checkLineNumber(lineNumber);
		trueLine = lineNumber;
		lines[trueLine].setForeground(Color.white);
		lines[trueLine].setBackground(GUIColorConfiguration.TRUE_LINE);
	}
	
	public void setBooleanLineFalse(int lineNumber) {
		checkLineNumber(lineNumber);
		falseLine = lineNumber;
		lines[falseLine].setForeground(Color.white);
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
