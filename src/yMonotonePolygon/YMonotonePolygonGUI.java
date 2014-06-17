package yMonotonePolygon;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;

import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.Method;
import yMonotonePolygon.AlgorithmObjects.SubEvent;
import yMonotonePolygon.AlgorithmObjects.SweepLineEvent;
import yMonotonePolygon.AlgorithmObjects.Vertex;
import yMonotonePolygon.GUI.MethodPanel;
import yMonotonePolygon.GUI.PolygonDrawPanel;
import yMonotonePolygon.GUI.TreeStatusPanel;
import yMonotonePolygon.PraeComputation.IllegalPolygonException;
import yMonotonePolygon.PraeComputation.PraeComputer;
import yMonotonePolygon.Tests.TestHelper;

public class YMonotonePolygonGUI extends JFrame implements ActionListener, MouseListener {
    private static final long serialVersionUID = -5073162102279789347L;

    // --- algorithm things --- algorithm things --- algorithm things ---
    // algorithm things ---
    // things to keep during the algorithm
    /** Says whether a polygon is set or not. */
    private boolean polygonSet = false;
    private Polygon p;
    private PraeComputer praeComputer;

    /**
     * The polygon as vertices and as set sorted by y-coordinate for the
     * algorithm as queue
     */
    private ArrayList<Vertex> currentVertices;
    private ArrayList<Vertex> historyVertices;
    /**
     * List of the diagonals as the result of the algorithm in order of addition
     */
    private LinkedList<Edge> diagonals;

    private LinkedList<SweepLineEvent> currentHistory;
    private LinkedList<SubEvent> currentSubEvents;

    /** number of handled vertices */
    private int handledVertices;

    /** active edges, those crossing the sweep line and pointing down */
    private HashSet<Edge> activeEdges;
    

    /** binary search tree of the active edges */
    // private SearchTree tree;

    public long time;

    private int currentSLPosition;
    private int currentMPosition;
    private int currentLinePosition;
    private Vertex currentVertex;
    private boolean nextStep;

    private Line2D leftMarkedEdge;
    private Line2D sweepStraightLine;

    private Ellipse2D.Double markedVertex;

    // --- GUI things --- GUI things --- GUI things --- GUI things ---
    // for the tree data structure status
    public TreeStatusPanel treeDataStructure;

    // menu and buttons
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
    private boolean isPaused;
    public JSlider velocity;
    // menu and buttons | load polygon and draw polygon control
    public JButton editBtn;
    private boolean inDrawMode = false;
    public JButton loadData;
    public ButtonGroup editButtonGroup;

    // method panel
    public MethodPanel methodPanel;
    // public QLabel methodTitle;
    // public QLabel treeTitle;

    // algorithm polygon sweepline panel

    private PolygonDrawPanel sweepLine;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    YMonotonePolygonGUI window = new YMonotonePolygonGUI();
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

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        // init main frame
        this.setTitle("Y-Monoton Polygon Algorithm Demonstrator");
        this.setBounds(100, 100, 1000, 800);
        // frame.setMinimumSize(new Dimension(800, 1000));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        // frame.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // init button panel
        initMenue();
        this.add(menue);

        // init algorithm panel
        initDrawPanel();
        this.add(sweepLine);

        // init tree status panel
        initTreeStatusPanel();
        this.add(treeDataStructure);

        // init method panel
        initMethodPanel();
        this.add(methodPanel);

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
        play.setToolTipText("Automatically step through algorithm");
        play.addActionListener(this);
        // pause = new QPushButton("||");
        // pause.setToolTip("pause Algorithm");
        // pause.clicked.connect(this, "pauseClicked()");
        algorithmController = new JPanel();
        algorithmController.add(skipBack);
        algorithmController.add(lineUp);
        algorithmController.add(stepBack);
        // algorithmController.addButton(pause);
        algorithmController.add(play);
        algorithmController.add(stepForward);
        algorithmController.add(lineDown);
        algorithmController.add(skipForward);

        velocity = new JSlider();
        velocity.setMaximum(10);
        velocity.setMinimum(1);
        // TODO config slider
        algorithmController.add(velocity);

        // loading input
        editBtn = new JButton("Draw");
        editBtn.addActionListener(this);
        loadData = new JButton("Load");
        loadData.addActionListener(this);

        editButtonGroup = new ButtonGroup();
        editButtonGroup.add(editBtn);
        editButtonGroup.add(loadData);

        menue.add(algorithmController);
        menue.add(editBtn);
        menue.add(loadData);

        menue.setMaximumSize(new Dimension(10000, 50));
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
        methodPanel.setMaximumSize(new Dimension(10000, 200));
        methodPanel.setPreferredSize(new Dimension(600, 200));
        methodPanel.setMinimumSize(new Dimension(1, 100));
    }

    private void initAlgorithm(Polygon p) {
        inDrawMode = false;
        polygonSet = true;
        isPaused = true;
        currentSLPosition = -1;
        currentMPosition = 0;
        currentLinePosition = 0;
        handledVertices = 0;
        time = 1000;
        currentSubEvents = new LinkedList<SubEvent>();
        resetSlider();

        try {
            praeComputer.work(p);
        } catch (IllegalPolygonException e) {
            methodPanel.setMethod(Method.ERROR);
            return;
        }

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

		// use ctrl + "/" to un-/comment whole block (in eclipse)
//		SweepLineEvent s = currentHistory.get(3);
//		treeDataStructure.setDataStructure(s.getVertexSetOfTree());
//		treeDataStructure.repaint();
//		sweepLine.setCurrentVertex(s.getVertex());
//		sweepLine.setNumberOfDiagonals(s.getNumberOfDiagonals());
//		sweepLine.setActiveEdges(s.getActiveEdges());
//		sweepLine.setNumberOfHandledEvents(s.getNumberOfHandledVertices());
//		methodPanel.setMethod(Method.HANDLE_MERGE);
//		
//		methodPanel.highlightLine(0);
//		methodPanel.setBooleanLineTrue(1);
//		methodPanel.setBooleanLineFalse(2);
    }

    private void resetSlider() {
        // TODO Auto-generated method stub
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
                playClicked();
            }
        }
        if (e.getSource() == editBtn) {
            editBtnClicked();
        } else if (e.getSource() == loadData) {
            loadDataClicked();
        }
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

    public void stepForwardClicked() {
        System.out.println("stepForward SL: " + currentSLPosition + " Method: " + currentMPosition + " Line: "
                + currentLinePosition);
        
        if (currentSLPosition == -1) {
            skipToNextEvent();
        } else if (currentSLPosition == praeComputer.getHistory().size() - 1) {
            // TODO: already reached the end
        } else {
            SweepLineEvent sle = praeComputer.getHistory().get(currentSLPosition);
            currentSubEvents.clear();
            currentSubEvents = sle.getSubEvents();
            System.out.println("subEventSize " + currentSubEvents.size() + " : " + sle.getSubEvents().size());
            currentLinePosition++;
            if (currentLinePosition > sle.getSubEvents().size() - 1) {
                skipToNextEvent();
            } else {
                methodPanel.setMethod(sle.getMethod());
                methodPanel.setTextlines(sle.getMethod().getLines());
            }
            sweepLine.setCurrentVertex(sle.getVertex());
            sweepLine.setNumberOfDiagonals(sle.getNumberOfDiagonals());
            sweepLine.setNumberOfHandledEvents(sle.getNumberOfHandledVertices());
            sweepLine.setActiveEdges(sle.getActiveEdges());
            sweepLine.repaint();
        }
        /*
         * currentLinePosition += 1; if (currentLinePosition >=
         * currentEvent.getNumberOfLines()) { // we finished this event and have
         * to go one event/vertex forward skipToNextEvent(); } else { // handle
         * just the next subevent
         * handleSubEvent(currentEvent.getSubEvents().get(currentLinePosition));
         * }
         */
    }

    public void stepBackClicked() {
        System.out.println("stepBack SL: " + currentSLPosition + " Method: " + currentMPosition + " Line: "
                + currentLinePosition);
        currentLinePosition -= 1;
        if (currentLinePosition < 0) {
            skipToPreviousEventAtEnd();
        } else { // reset all subevents in this method so far
            handleSubEventsUpToCurrentLine();
        }
        sweepLine.repaint();
    }

    public void skipForwardClicked() {
        System.out.println("End");
        drawEverything();
    }

    private void reset() {
        currentSLPosition = 0;
        currentLinePosition = 0;
        //sweepLine.setDiagonals(null); // do not set diagonals null, just set the number of painted to zero
        sweepLine.reset(praeComputer.getHistory().get(0).getVertex());        
        treeDataStructure.reset();
        SweepLineEvent sle = praeComputer.getHistory().get(0);
        sweepLine.setCurrentVertex(sle.getVertex());
        sweepLine.setNumberOfDiagonals(sle.getNumberOfDiagonals());
        sweepLine.setNumberOfHandledEvents(sle.getNumberOfHandledVertices());
        sweepLine.setActiveEdges(sle.getActiveEdges());
    }

    public void skipBackClicked() {
        System.out.println("reset");
        reset();

    }

    private void drawEverything() {
        currentSLPosition = praeComputer.getHistory().size() - 1;
        currentLinePosition = praeComputer.getHistory().get(currentSLPosition).getSubEvents().size() - 1;
        currentLinePosition = (currentLinePosition < 0) ? 0 : currentLinePosition;
        sweepLine.setDiagonals(praeComputer.getDiagonals());
        sweepLine.setNumberOfDiagonals(praeComputer.getHistory().get(currentSLPosition).getNumberOfDiagonals());
        sweepLine.setCurrentVertex(praeComputer.getHistory().getLast().getVertex());
        sweepLine.setNumberOfHandledEvents(praeComputer.getHistory().get(currentSLPosition).getNumberOfHandledVertices());
        sweepLine.setActiveEdges(praeComputer.getHistory().get(currentSLPosition).getActiveEdges());
        sweepLine.repaint();
        // TODO Auto-generated method stub

    }

    public void lineUpClicked() {
        System.out.println("lineUp");
        if (currentSLPosition > 1) {
            skipToPreviousEvent();
        } else {
            currentSLPosition = 0;
        }
        sweepLine.repaint();

    }

    public void lineDownClicked() {
        if (currentSLPosition < praeComputer.getHistory().size() - 1) {
            skipToNextEvent();
        } else {
            currentSLPosition = praeComputer.getHistory().size();
        }
        sweepLine.repaint();

    }

    private void skipToNextEvent() {
        System.out.println("nextEvent");
        currentSLPosition++;
        SweepLineEvent sle = praeComputer.getHistory().get(currentSLPosition);
        // sweepStraightLine.setY(sle.getYOfSweepLine());
        currentLinePosition = 0;
        currentSubEvents.clear();
        currentSubEvents = sle.getSubEvents();
        methodPanel.setMethod(sle.getMethod());
        methodPanel.setTextlines(sle.getMethod().getLines());
        sweepLine.setCurrentVertex(sle.getVertex());
        sweepLine.setNumberOfDiagonals(sle.getNumberOfDiagonals());
        sweepLine.setNumberOfHandledEvents(sle.getNumberOfHandledVertices());
        sweepLine.setActiveEdges(sle.getActiveEdges());
        
        repaint();

    }

    private void skipToPreviousEvent() {
        currentSLPosition = (currentSLPosition == 0) ? 0 : (currentSLPosition - 1);
        SweepLineEvent sle = praeComputer.getHistory().get(currentSLPosition);
        currentLinePosition = 0;
        currentSubEvents.clear();
        currentSubEvents = sle.getSubEvents();
        methodPanel.setMethod(sle.getMethod());
        methodPanel.setTextlines(sle.getMethod().getLines());
        sweepLine.setCurrentVertex(sle.getVertex());
        sweepLine.setNumberOfDiagonals(sle.getNumberOfDiagonals());
        sweepLine.setNumberOfHandledEvents(sle.getNumberOfHandledVertices());
        sweepLine.setActiveEdges(sle.getActiveEdges());
        repaint();
        // TODO check if there is a prev vertex and if start sweepline in FIRST
        // line of it

    }

    private void skipToPreviousEventAtEnd() {
        currentSLPosition--;
        if (currentSLPosition >= 0) {
            SweepLineEvent sle = praeComputer.getHistory().get(currentSLPosition);
            currentLinePosition = sle.getSubEvents().size() - 1;
            currentSubEvents.clear();
            currentSubEvents = sle.getSubEvents();
            methodPanel.setMethod(sle.getMethod());
            methodPanel.setTextlines(sle.getMethod().getLines());
            sweepLine.setCurrentVertex(sle.getVertex());
            sweepLine.setNumberOfDiagonals(sle.getNumberOfDiagonals());
            sweepLine.setNumberOfHandledEvents(sle.getNumberOfHandledVertices());
            sweepLine.setActiveEdges(sle.getActiveEdges());
        } else {
            currentSLPosition = 0;
            currentLinePosition = 0;
        }
        // TODO check if there is a prev vertex and if start sweepline in LAST
        // line of it

        repaint();
    }

    private void handleSubEvent(SubEvent subEvent) {
        // TODO Auto-generated method stub

    }

    private void handleSubEventsUpToCurrentLine() {
        for (int i = 0; i <= currentLinePosition; i++) {
            handleSubEvent(currentSubEvents.get(i));
        }
    }

    public void stepForwardClicked2() {

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
                while (!isPaused && currentHistory.size() > 0) {

                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            stepForwardClicked();
                            time = 1 / velocity.getValue() * 1000;
                            System.out.println("time " + time);
                            if (currentSLPosition == praeComputer.getHistory().size() - 1) {
                                isPaused = true;
                                play.setText("Play");
                                play.setToolTipText("Automatically step through algorithm.");
                            }
                        }
                    });

                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

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

		System.out.print("polygon ");
	}
	
    private void endDrawMode() {
        inDrawMode = false;
        if (p.npoints >= 3) {
            initAlgorithm(p);
        } else { // clear the previous drawn stuff (2 points and one edge)
        	sweepLine.clear();
        }

        System.out.println("");
        sweepLine.removeMouseListener(this);
        sweepLine.repaint();
        inDrawMode = false;
    }

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
        Polygon p = TestHelper.readPolygon(address);

        // init algorithm
        initAlgorithm(p);
    }
 
}
