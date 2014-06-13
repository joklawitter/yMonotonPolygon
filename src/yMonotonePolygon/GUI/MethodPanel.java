package yMonotonePolygon.GUI;

import java.awt.Color;

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
	
	public MethodPanel() {
		init();
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
		this.method = method;
		setTitle(method.getName());
		setTextlines(method.getLines());
	}

	
	// -- visualization features -- visualization features --
	
	public void highlightLine(int lineNumber) {
		checkLineNumber(lineNumber);
		// TODO highlight the line at the given number
	}

	public void setBooleanLineTrue(int lineNumber) {
		checkLineNumber(lineNumber);
		// TODO set the line green to show that their outcome was true
	}
	
	public void setBooleanLineFalse(int lineNumber) {
		checkLineNumber(lineNumber);
		// TODO set the line red to show that their outcome was false
	}
	
	public void grayOutLine(int lineNumber) {
		checkLineNumber(lineNumber);
		// TODO maybe! gray out this line which will not be executed because bool event was false
	}
	
	// -- test helper methods -- check helper methods --
	private void checkTextlinesSize(int length) {
		if (length >= MAX_NUMBER_LINES) {
			throw new IllegalArgumentException("To many lines to display in method section.");
		}
	}

	private void checkLineNumber(int lineNumber) {
		if ((lineNumber < 0) || (lineNumber >= text.length)) {
			throw new IllegalArgumentException("Illegal line number.");
		}
	}





}
