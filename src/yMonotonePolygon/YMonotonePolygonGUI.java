package yMonotonePolygon;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import yMonotonePolygon.AlgorithmObjects.AddDiagonalSubEvent;
import yMonotonePolygon.AlgorithmObjects.BooleanSubEvent;
import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.Method;
import yMonotonePolygon.AlgorithmObjects.SearchSubEvent;
import yMonotonePolygon.AlgorithmObjects.SubEvent;
import yMonotonePolygon.AlgorithmObjects.SweepLineEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateDeletionTreeSubEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateHelperSubEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateInsertTreeSubEvent;
import yMonotonePolygon.AlgorithmObjects.Vertex;
import yMonotonePolygon.GUI.MethodPanel;
import yMonotonePolygon.GUI.PolygonDrawPanel;
import yMonotonePolygon.GUI.TreeStatusPanel;
import yMonotonePolygon.PraeComputation.IllegalPolygonException;
import yMonotonePolygon.PraeComputation.PraeComputer;
import yMonotonePolygon.PraeComputation.Reader;
import algo.Triangulator;

public class YMonotonePolygonGUI extends JFrame implements ActionListener, MouseListener {
    private static final long serialVersionUID = -5073162102279789347L;

    // --- algorithm things --- algorithm things --- algorithm things ---
    // things to keep during the algorithm
    /** Says whether a polygon is set or not. */
    private boolean polygonSet = false;
    private Polygon p;
    private PraeComputer praeComputer;

    /**
     * List of the diagonals as the result of the algorithm in order of addition
     */
    private LinkedList<Edge> diagonals;
 
    public long time;

    private int currentSLPosition;
    private int currentLinePosition;
    private LinkedList<SweepLineEvent> currentHistory;
    private LinkedList<SubEvent> currentSubEvents;
    private SweepLineEvent currentSweepLineEvent;
    private Vertex currentVertex;
    
    private boolean reachedEnd = false;

    // --- GUI things --- GUI things --- GUI things --- GUI things ---
    // --- for the tree data structure status
    public TreeStatusPanel treeDataStructure;

    // --- menu and buttons
    public JPanel menue;
    // menu and buttons | algorithm control
    public JPanel algorithmController;
    // public QButtonGroup algorithmController;
    public JButton stepBack;
    public JButton stepForward;
    public JButton lineUp;
    public JButton lineDown;
    public JButton skipBack;
    public JButton skipForward;
    public JButton play;
    public JButton triangulate;
    private boolean isPaused;
    public JSlider velocity;
    // menu and buttons | load polygon and draw polygon control
    public JButton editBtn;
    private boolean inDrawMode = false;
    private boolean polygonDrawn = false;
    private String drawnPolygonText;
    private final JButton saveBtn = new JButton("Save");
    public JButton loadData;

    // --- method panel
    public MethodPanel methodPanel;

    // --- algorithm polygon sweepline panel
    private PolygonDrawPanel sweepLine;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new YMonotonePolygonGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public YMonotonePolygonGUI() {
        initialize();
        setVisible(true);
    }
    
    // -- initialize -- initialize -- initialize -- initialize -- initialize --
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        // init main frame
        this.setTitle("Y-Monoton Polygon Algorithm Demonstrator");
        this.setBounds(100, 100, 1200, 768);//dimension of Beamer + 2 so Load-Btn doesnot disappear + alot to show TriangulateBtn

        // frame.setMinimumSize(new Dimension(800, 1000));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());//new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        // frame.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // init button panel
        initMenue();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0;
        c.weightx = 1.0;
        this.add(menue, c);

        c.weighty = 1.0;
        c.gridy = 1;
        // init algorithm panel
        initDrawPanel();
        this.add(sweepLine, c);

        c.weighty = 0;
        c.gridy = 2;

        JTextPane dataStructureTitle = new JTextPane();
        dataStructureTitle.setText("sinnvoller Titel");
        this.add(dataStructureTitle,c);
       
        
        c.gridy = 3;
        // init tree status panel
        initTreeStatusPanel();
        this.add(treeDataStructure, c);

        
        c.gridy = 4;
        // init method panel
        initMethodPanel();
        this.add(methodPanel, c);

        // init praecomputer
        praeComputer = new PraeComputer();
    }

    private void initMenue() {
        menue = new JPanel();

        stepBack = new JButton("|<<");
        stepBack.setToolTipText("go to previous line of code");
        stepBack.addActionListener(this);
        stepForward = new JButton(">>|");
        stepForward.setToolTipText("go to next line of code");
        stepForward.addActionListener(this);
        lineUp = new JButton("Up");
        lineUp.setToolTipText("Go to previous sweepline event");
        lineUp.addActionListener(this);
        lineDown = new JButton("Down");
        lineDown.setToolTipText("Go to next sweepline event");
        lineDown.addActionListener(this);
        skipBack = new JButton("Reset");
        skipBack.setToolTipText("skip to the beginning of the algorithm");
        skipBack.addActionListener(this);
        skipForward = new JButton("To End");
        skipForward.setToolTipText("skip to the end of the algorithm");
        skipForward.addActionListener(this);
        play = new JButton("Play");
        //play = new JButton("\u25b6");
        play.setToolTipText("Automatically step through algorithm");
        play.addActionListener(this);
        algorithmController = new JPanel();
        algorithmController.add(skipBack);
        algorithmController.add(lineUp);
        algorithmController.add(stepBack);
        algorithmController.add(play);
        algorithmController.add(stepForward);
        algorithmController.add(lineDown);
        algorithmController.add(skipForward);

        velocity = new JSlider();
        velocity.setMaximum(100);
        velocity.setMinimum(1);
        velocity.setValue(50);
        velocity.setToolTipText("adjust speed of play");
        algorithmController.add(velocity);

        triangulate = new JButton("Triangulate");
        triangulate.addActionListener(this);
        triangulate.setToolTipText("triangulate y-monoton Polygon");
        
        // loading input
        editBtn = new JButton("Draw");
        editBtn.addActionListener(this);
        editBtn.setToolTipText("Draw a polygon");
        saveBtn.addActionListener(this);
        saveBtn.setEnabled(false);
        saveBtn.setToolTipText("Save your drawn polygon");
        loadData = new JButton("Load");
        loadData.addActionListener(this);
        loadData.setToolTipText("Load a polygon");

        menue.add(algorithmController);
        menue.add(editBtn);
        menue.add(saveBtn);
        menue.add(loadData);
        menue.add(triangulate);

        //menue.setMaximumSize(new Dimension(10000, 50));
        menue.setPreferredSize(new Dimension(600, 50));
        menue.setMinimumSize(new Dimension(1, 100));
    }

    private void initDrawPanel() {
        sweepLine = new PolygonDrawPanel();
    }

    private void initTreeStatusPanel() {
        treeDataStructure = new TreeStatusPanel();
    }

    private void initMethodPanel() {
        methodPanel = new MethodPanel();

    }

    // -- initialize algorithm -- reset -- initialize algorithm -- reset --
    private void initAlgorithm(Polygon p) {
        reachedEnd = false;
        polygonSet = true;
        isPaused = true;
        currentSLPosition = -1; // starts with no current event
        currentLinePosition = -1; // start with line -1
        time = 100;
        currentSubEvents = new LinkedList<SubEvent>();
        
        try {
            praeComputer.work(p);
        } catch (IllegalPolygonException e) {
        	sweepLine.clear();
        	resetSave(); // there is nothing to save
            methodPanel.setMethod(Method.ERROR);
            return;
        }
        
        
        if (polygonDrawn) {
        	saveBtn.setEnabled(true);
        }
        
        
        methodPanel.setMethod(Method.START);     

        // draw the polygon
        sweepLine.setP(p);
        sweepLine.repaint();

        // set the diagonals in the sweepLine Panel, later just update the
        // number of painted ones
        currentHistory = praeComputer.getHistory();
        diagonals = praeComputer.getDiagonals();
        sweepLine.setDiagonals(diagonals);
        sweepLine.setEvents(praeComputer.getVertices());
		polygonSet = true;
    }

    private void reset() {
        currentSLPosition = -1;
        currentLinePosition = -1;
        currentSweepLineEvent = null;
        currentSubEvents = null;
        sweepLine.reset();        
        treeDataStructure.reset();
        reachedEnd = false;
        methodPanel.setMethod(Method.START);
    }
    
    // -- handling algorithm flow -- handling algorithm flow -- handling algorithm flow --
    @Override
    public void actionPerformed(ActionEvent e) {
    	boolean pausing = isPaused;
    	isPaused = true; // stop flow
        if (polygonSet) {
            if (e.getSource() == stepBack) {
                stepBackClicked();
            } else if (e.getSource() == stepForward) {
                stepForwardClicked();
            } else if (e.getSource() == lineUp) {
                lineUpClicked();
            } else if (e.getSource() == lineDown) {
                lineDownClicked();
            } else if (e.getSource() == skipBack) {
                skipBackClicked();
            } else if (e.getSource() == skipForward) {
                skipForwardClicked();
            } else if (e.getSource() == play) {
            	isPaused = pausing;
                playClicked();
            } else if (e.getSource() == triangulate) {
                triangulate();
            }
        }
        if (e.getSource() == editBtn) {
            editBtnClicked();
        } else if (e.getSource() == loadData) {
            loadDataClicked();
        } else if (polygonDrawn && (e.getSource() == saveBtn)) {
        	saveBtnClicked();
        }
    }

    public void triangulate() {
        LinkedList<Point2D> points = new LinkedList<Point2D>();
        for (int i = 0; i < p.npoints; i++) {
            Point2D pt = new Point(p.xpoints[i], p.ypoints[i]);
            points.add(pt);
        }
        Triangulator.triangulateMonotone(points);
        //TODO: ??
    }
    
	public void playClicked() {
        isPaused = !isPaused;
        if (!isPaused) {
            play.setText("||");
            play.setToolTipText("Pause algorithm.");
        } else {
            play.setText("Play");
            play.setToolTipText("Automatically step through algorithm.");
        }

        new Thread() {
            @Override
            public void run() {
                while (!isPaused) {
                	if (reachedEnd) {
                		resetPlay();
                		return;
                	}
                	
                	stepForwardClicked();
                	repaint();
                	methodPanel.repaint();
                    time = (int) ((velocity.getMaximum() - velocity.getValue() + 1) * 15.0);
                    System.out.println("time " + time);
                    if (currentSLPosition == currentHistory.size()) {
                    	drawEverything();
                    	resetPlay();
                    }
                                       
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void resetPlay() {
    	isPaused = true;
    	play.setText("Play");
        play.setToolTipText("Automatically step through algorithm.");
	}
    
    public void stepForwardClicked() {  
    	if (reachedEnd) {
    		return;
    	}
        if (currentSLPosition == -1) { // if we are at the beginning go to first event
            skipToNextEvent();
        } else {	// else go one line further      	
        	System.out.println("stepForward SL: " + (currentSLPosition + 1) + " Line: " + (currentLinePosition + 1));
        	currentLinePosition += 1;
        	// check if we are at the last line
        		
        	if ((currentSLPosition == currentHistory.size() - 1) && (currentLinePosition == currentSweepLineEvent.getNumberOfSteps())) {
        		drawEverything(); // we reached the end
        		return;
        	} else if ((currentLinePosition == currentSweepLineEvent.getNumberOfSteps())) { 
        		skipToNextEvent(); // we reached the last line, skip to next event
        	} else {
        		// else we just handle the next subevent
        		handleSubEvent(currentSubEvents.get(currentLinePosition));
                repaint();

        }
    	
        }
    }

    public void stepBackClicked() {
    	reachedEnd = false;
        System.out.println("stepBack SL: " + (currentSLPosition + 1) + " Line: " + (currentLinePosition));
        if (currentSLPosition < 0) {
        	return;
        }
        
        if (currentLinePosition <= -1) {
            skipToPreviousEventAtEnd();
        } else { // reset all subevents in this method so far
        	currentLinePosition -= 1;
            handleSubEventsUpToCurrentLine();
        }
        repaint();
    }

    public void lineUpClicked() {
    	reachedEnd = false;
    	
        System.out.println("lineUp");
        if (currentSLPosition >= 1) {
            skipToPreviousEvent();
        } else {
            currentSLPosition = 0;
            reset();
        }
        repaint();
    }

    public void lineDownClicked() {
        if (currentSLPosition < currentHistory.size() - 1) {
            skipToNextEvent();
        } else {
            drawEverything();
        }
        repaint();
    }

    private void skipToNextEvent() {
        currentSLPosition++;
        System.out.println("nextEvent: " + (currentSLPosition + 1));
        currentSweepLineEvent = currentHistory.get(currentSLPosition);
        currentLinePosition = -1; // start with line -1, then in next step skip to line 1
        currentSubEvents = currentSweepLineEvent.getSubEvents();
        currentVertex = currentSweepLineEvent.getVertex();
 
        sweepLine.setCurrentVertex(currentVertex);
        sweepLine.setNumberOfDiagonals(currentSweepLineEvent.getNumberOfDiagonals());
        sweepLine.setNumberOfHandledEvents(currentSweepLineEvent.getNumberOfHandledVertices());
        sweepLine.setActiveEdges(currentSweepLineEvent.getActiveEdges());
      
        treeDataStructure.setDataStructure(currentSweepLineEvent.getVertexSetOfTree());
        treeDataStructure.setSearchPoint(currentVertex);
      
        methodPanel.setMethod(currentSweepLineEvent.getMethod());
        
        repaint();
    }

    private void skipToPreviousEvent() {
        currentSLPosition = (currentSLPosition == 0) ? 0 : (currentSLPosition - 1);
        currentSweepLineEvent = currentHistory.get(currentSLPosition);
        currentLinePosition = -1;
        currentSubEvents = currentSweepLineEvent.getSubEvents();
        currentVertex = currentSweepLineEvent.getVertex();
        methodPanel.setMethod(currentSweepLineEvent.getMethod());
        sweepLine.setCurrentVertex(currentVertex);
        sweepLine.setNumberOfDiagonals(currentSweepLineEvent.getNumberOfDiagonals());
        sweepLine.setNumberOfHandledEvents(currentSweepLineEvent.getNumberOfHandledVertices());
        sweepLine.setActiveEdges(currentSweepLineEvent.getActiveEdges());
        repaint();
    }

    private void skipToPreviousEventAtEnd() {
        currentSLPosition--;
        if (currentSLPosition < 0) {
        	reset(); // reached beginning
        } else if (currentSLPosition >= 0) {
        	currentLinePosition = currentHistory.get(currentSLPosition).getNumberOfSteps() - 1;
        	handleSubEventsUpToCurrentLine();
        }

        repaint();
    }

    private void handleSubEvent(SubEvent subEvent) {
        System.out.println("> nextSubEvent: " + (subEvent.getLine() + 1));
    	// highlight the current line
    	methodPanel.highlightLine(subEvent.getLine()); 	
    	methodPanel.repaint();
        
    	sweepLine.resetSearchAndHelper();
    	
        if (subEvent instanceof AddDiagonalSubEvent) {
        	AddDiagonalSubEvent addDiag = (AddDiagonalSubEvent) subEvent;
        	sweepLine.setNumberOfDiagonals(addDiag.getNewNumberOfDiagonals());
        } else if (subEvent instanceof BooleanSubEvent) {
        	BooleanSubEvent booli = (BooleanSubEvent) subEvent;
        	methodPanel.setBooleanLine(booli.getLine(), booli.getBooleanEventOutcome()); 
        } else if (subEvent instanceof SearchSubEvent) {
        	SearchSubEvent searchi = (SearchSubEvent) subEvent;
        	sweepLine.setFoundEdge(searchi.getFoundEdge());
        } else if (subEvent instanceof UpdateDeletionTreeSubEvent) {
        	UpdateDeletionTreeSubEvent deletion = (UpdateDeletionTreeSubEvent) subEvent;
        	treeDataStructure.setDataStructure(deletion.getUpdatedVerticesOfTree());
        	sweepLine.setActiveEdges(deletion.getActiveEdges());
        } else if (subEvent instanceof UpdateInsertTreeSubEvent) {
        	UpdateInsertTreeSubEvent insertion = (UpdateInsertTreeSubEvent) subEvent;
        	treeDataStructure.setDataStructure(insertion.getUpdatedVerticesOfTree());
        	sweepLine.setActiveEdges(insertion.getActiveEdges());
        } else if (subEvent instanceof UpdateHelperSubEvent) {
        	UpdateHelperSubEvent helperUpdate = (UpdateHelperSubEvent) subEvent;
        	sweepLine.setNewHelper(helperUpdate.getNewHelper());
        	sweepLine.setOldHelper(helperUpdate.getOldHelper());
        }
    }

    private void handleSubEventsUpToCurrentLine() {
    	//System.out.println("Handle subEvents up to last line.");
    	int curLine = currentLinePosition;
    	currentSLPosition -= 1;
    	skipToNextEvent();
    	currentLinePosition = curLine;
        for (int i = 0; i <= currentLinePosition; i++) {
            handleSubEvent(currentSubEvents.get(i));
        }
        repaint();
    }

    // --- skip back and forward
    public void skipBackClicked() {
        System.out.println("reset");
        reset();
    }
    
    public void skipForwardClicked() {
        System.out.println("End");
        drawEverything();
    }

    private void drawEverything() {
    	resetPlay();
    	
    	reachedEnd = true;

        currentSLPosition = currentHistory.size() - 1;
        currentSweepLineEvent = currentHistory.get(currentSLPosition);
        currentLinePosition = currentSweepLineEvent.getNumberOfSteps();
        //currentLinePosition = (currentLinePosition < 0) ? 0 : currentLinePosition;
        sweepLine.setNumberOfDiagonals(diagonals.size());
        sweepLine.setNumberOfHandledEvents(currentHistory.size()); // all handled
        sweepLine.setActiveEdges(null); // no active edges left
        sweepLine.setCurrentVertex(null);
        sweepLine.repaint();
        treeDataStructure.reset();
        
        methodPanel.setTitle(END_OF_ALGORITHM);
        methodPanel.setTextlines(getEndLines());
    }

    private static final String END_OF_ALGORITHM = "Reached the end of the algorithm!";
	private String[] getEndLines() {
		String[] lines = new String[2];
		lines[0] = "Input: Polygon with " + currentHistory.size() + " vertices";
		lines[1] = "Output: added " + diagonals.size() + " diagonals";
		return lines;
	}

	// -- drawing modus -- drawing modus -- drawing modus -- drawing modus -- 
    public void editBtnClicked() {
        if (!inDrawMode) {// set draw mode
            inDrawMode = !inDrawMode;
            setDrawMode();
        } else {
            endDrawMode();
        }
    }

    private void setDrawMode() {
		// clear current 
		sweepLine.clear();
		polygonSet = false;
		saveBtn.setEnabled(false);
		treeDataStructure.reset();		
		
    	// set text in method field to instructions:
    	// - click to set new point
    	// - click near first point to close polygon
    	// - click [draw] to abort
		methodPanel.setMethod(Method.DRAW_MODE);
		
		p = new Polygon();
    	// catch points
		sweepLine.addMouseListener(this);
		sweepLine.setDrawMode();

		drawnPolygonText = "polygon ";
		System.out.print("polygon ");
	}
	
    private void endDrawMode() {
    	inDrawMode = false;
    	polygonDrawn = true;

    	if (p.npoints >= 3) {
            initAlgorithm(p);
        } else { // clear the previous drawn stuff (2 points and one edge)
        	sweepLine.clear();
        }

        System.out.println("");
        sweepLine.removeMouseListener(this);
        sweepLine.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (inDrawMode) {
            int eX = e.getX();
            int eY = e.getY();
            if (p.npoints >= 3) {
                int x = p.xpoints[0];
                int y = p.ypoints[0];

                if ((Math.abs(eX - x) + Math.abs(eY - y)) < 10) {
                    sweepLine.drawLine(p.xpoints[0], p.ypoints[0], p.xpoints[p.npoints - 1], p.ypoints[p.npoints - 1]);
                    endDrawMode();
                    return;
                }

                // TODO check self intersection
            }
            addPoint(eX, eY);
        }
    }

    private void addPoint(int eX, int eY) {
    	drawnPolygonText += eX + "," + eY + " ";
        System.out.print(eX + "," + eY + " ");
        p.addPoint(eX, eY);
        sweepLine.drawPoint(eX, eY);
        if (p.npoints >= 2) {
            sweepLine.drawLine(eX, eY, p.xpoints[p.npoints - 2], p.ypoints[p.npoints - 2]);
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
    
    // -- loading a polygon -- loading a polygon -- loading a polygon -- 
    public void loadDataClicked() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text", "txt");
        chooser.setFileFilter(filter);
        File workingDirectory = new File(System.getProperty("user.dir") + File.separator + "PolygonExamples");
        chooser.setCurrentDirectory(workingDirectory);
        int returnVal = chooser.showOpenDialog(this);
        String address = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            address = chooser.getSelectedFile().getName();
        }

        if (address == null) {
            return;
        }

        // load file
        Polygon p = Reader.readPolygon(address);

        // we loaded something, so we do not have to save it
        resetSave();
        
        // init algorithm
        initAlgorithm(p);
    }
    
    private void saveBtnClicked() {
        JFileChooser chooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir") + File.separator + "PolygonExamples");
        chooser.setCurrentDirectory(workingDirectory);
        int retrival = chooser.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try {
            	if (drawnPolygonText == null) {
            		System.out.println("Nothing to save...");
            		return;
            	}
                FileWriter fw = new FileWriter(chooser.getSelectedFile()+".txt");
                fw.write(drawnPolygonText);
                fw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
	}
    
	private void resetSave() {
    	polygonDrawn = false;
    	saveBtn.setEnabled(false);
	}
 
}
