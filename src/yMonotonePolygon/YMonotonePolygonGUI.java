package yMonotonePolygon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QButtonGroup;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsEllipseItem;
import com.trolltech.qt.gui.QGraphicsLineItem;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineF;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QPolygonF;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSlider;

import yMonotonePolygon.AlgorithmObjects.AddDiagonalSubEvent;
import yMonotonePolygon.AlgorithmObjects.BooleanSubEvent;
import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.Method;
import yMonotonePolygon.AlgorithmObjects.SearchSubEvent;
import yMonotonePolygon.AlgorithmObjects.SearchTree;
import yMonotonePolygon.AlgorithmObjects.SubEvent;
import yMonotonePolygon.AlgorithmObjects.SweepLineEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateDeletionTreeSubEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateHelperSubEvent;
import yMonotonePolygon.AlgorithmObjects.UpdateInsertTreeSubEvent;
import yMonotonePolygon.AlgorithmObjects.Vertex;
import yMonotonePolygon.AlgorithmObjects.VertexType;
import yMonotonePolygon.GUI.MethodPanel;
import yMonotonePolygon.GUI.PolygonDrawPanel;
import yMonotonePolygon.GUI.TreeStatusPanel;
import yMonotonePolygon.PraeComputation.IllegalPolygonException;
import yMonotonePolygon.PraeComputation.PraeComputer;
import yMonotonePolygon.Tests.TestHelper;


public class YMonotonePolygonGUI extends JFrame  implements ActionListener, MouseListener {
	private static final long serialVersionUID = -5073162102279789347L;
	
	// --- algorithm things --- algorithm things --- algorithm things --- algorithm things ---
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
    /** List of the diagonals as the result of the algorithm in order of addition  */
    private LinkedList<Edge> diagonals;

    private LinkedList<SweepLineEvent> currentHistory;
    private SweepLineEvent currentEvent;
    private LinkedList<SubEvent> currentSubEvents;
    
    /** number of handled vertices */
    private int handledVertices;

    /** active edges, those crossing the sweep line and pointing down */
    private TreeSet<Edge> activeEdges;
    
    /** binary search tree of the active edges */
    //private SearchTree tree;

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
    //public QButtonGroup algorithmController;
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
    //public QLabel methodTitle;
    //public QLabel treeTitle;
    
    
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
		//frame.setMinimumSize(new Dimension(800, 1000));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		//frame.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
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
		
		isPaused = true;
		currentSLPosition = -1;
		currentMPosition = 0;
		currentLinePosition = 0;
		handledVertices = 0;
		time = 1000;
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
		
		// set the diagonals in the sweepLine Panel, later just update the number of painted ones
		currentHistory = praeComputer.getHistory();
		diagonals = praeComputer.getDiagonals();
		sweepLine.setDiagonals(diagonals);
		
		/*SweepLineEvent s = currentHistory.get(3);
		treeDataStructure.setDataStructure(s.getVertexSetOfTree());
		treeDataStructure.repaint();
		sweepLine.setCurrentVertex(s.getVertex());
		sweepLine.setNumberOfDiagonals(diagonals.size());
		sweepLine.setActiveEdges(s.getActiveEdges());*/
	}
	
	private void resetSlider() {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (polygonSet) {
			if (e.getSource() == stepBack){ 
		          stepBackClicked(); 
		        } else if (e.getSource() == stepForward){ 
		        	stepForwardClicked(); 
		        } else if (e.getSource() == lineUp){ 
		        	lineUpClicked(); 
		        } else if (e.getSource() == lineDown){ 
		        	lineDownClicked(); 
		        } else if (e.getSource() == skipBack){ 
		        	skipBackClicked(); 
		        } else if (e.getSource() == skipForward){ 
		        	skipForwardClicked(); 
		        } else if (e.getSource() == play){ 
		        	playClicked(); 
		        }
		}
		if (e.getSource() == editBtn){ 
        	editBtnClicked(); 
        } else if (e.getSource() == loadData){ 
        	loadDataClicked(); 
        }
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (inDrawMode) {
			int eX = e.getX();
			int eY = e.getY();
			if (p.npoints > 3) {
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
    	System.out.println("stepForward SL: " + currentSLPosition + " Method: " + currentMPosition + " Line: " + currentLinePosition);
    	currentLinePosition += 1;
    	if (currentLinePosition >= currentEvent.getNumberOfLines()) {
    		// we finished this event and have to go one event/vertex forward
    		skipToNextEvent();
    	} else { // handle just the next subevent
    		handleSubEvent(currentEvent.getSubEvents().get(currentLinePosition));
    	}
    }
    
    public void stepBackClicked() {
    	System.out.println("stepBack SL: " + currentSLPosition + " Method: " + currentMPosition + " Line: " + currentLinePosition);
    	currentLinePosition -= 1;
    	if (currentLinePosition < 0) {
    		skipToPreviousEventAtEnd();
    	} else { // reset all subevents in this method so far
    		handleSubEventsUpToCurrentLine();
    	}
    }

	public void skipForwardClicked() {
    	reset();
    }
    
    private void reset() {
		// TODO Auto-generated method stub
		
	}
    
	public void skipBackClicked() {
    	drawEverything();
    }
    
	private void drawEverything() {
		// TODO Auto-generated method stub
		
	}

	public void lineUpClicked() {
		skipToNextEvent();
    	/*if (currentHistory.size() > 0) {
    		--currentSLPosition;
    		currentVertex = 
    		redrawSweepLine();
    		SweepLineEvent sle = currentHistory.getLast();
    		sweepStraightLine.s(sle.getYOfSweepLine());
    		currentState.add(currentHistory.getLast());
    		currentHistory.removeLast();
    		--currentSLPosition;
    	}*/
    }

    public void lineDownClicked() {
    	skipToPreviousEvent();
		/*if (currentVertices.size() != 0) {
		    currentVertex = currentVertices.get(0);
		    currentVertices.remove(0);
		    sweepStraightLine.setY(currentVertex.getY());
		    if (currentVertex.isStartVertex()) {
			currentEvent = handleStartVertex(currentVertex);
		    } else if (currentVertex.isEndVertex()) {
			currentEvent = handleEndVertex(currentVertex);
		    } else if (currentVertex.isSplitVertex()) {
			currentEvent = handleSplitVertex(currentVertex);
		    } else if (currentVertex.isMergeVertex()) {
			currentEvent = handleMergeVertex(currentVertex);
		    } else if (currentVertex.isRegularRightVertex()) {
			currentEvent = handleRegularRightVertex(currentVertex);
		    } else if (currentVertex.isRegularLeftVertex()) {
			currentEvent = handleRegularLeftVertex(currentVertex);
		    }
		    currentSLPosition++;
		    currentLinePosition = 0;
		}*/
    }

	private void skipToNextEvent() {
		currentSLPosition++;
		if (currentSLPosition >= praeComputer.getHistory().size()) {
			currentSLPosition = praeComputer.getHistory().size() - 1;	
		}
		SweepLineEvent sle = praeComputer.getHistory().get(currentSLPosition);
		//sweepStraightLine.setY(sle.getYOfSweepLine());
		currentLinePosition = 0;
		methodPanel.setMethod(sle.getMethod());
		methodPanel.setTextlines(sle.getMethod().getLines());
		
		/*markedVertex.setPos(sle.getVertex().getX() + 2.5, sle.getVertex().getY() + 2.5);
		markedVertex.setPen(new QPen(new QColor(sle.getVertex().getColor().getRGB())));
		markedVertex.setBrush(new QBrush(new QColor(sle.getVertex().getColor().getRGB())));
		diagonalItems.clear();*/
		
		for (int i = 0; i < sle.getNumberOfDiagonals(); i++) {
			//QGraphicsLineItem tmp = new QGraphicsLineItem(praeComputer.getDiagonals().get(i).getStartVertex().getX(), praeComputer.getDiagonals().get(i).getStartVertex().getY(), praeComputer.getDiagonals().get(i).getEndVertex().getX(), praeComputer.getDiagonals().get(i).getEndVertex().getY(), null, sweepLine);
			//diagonalItems.add(tmp);
		}
		// TODO check if there is a next vertex and if start sweepline in first line
		
	}
	
    private void skipToPreviousEvent() {
    	currentSLPosition = (currentSLPosition == 0) ?  0 : (currentSLPosition - 1);
    	SweepLineEvent sle = praeComputer.getHistory().get(currentSLPosition);
    	currentLinePosition = sle.getSubEvents().size() - 1;
    	methodPanel.setMethod(sle.getMethod());
    	methodPanel.setTextlines(sle.getMethod().getLines());
		// TODO check if there is a prev vertex and if start sweepline in FIRST line of it
		
	}
    
    private void skipToPreviousEventAtEnd() {
		// TODO check if there is a prev vertex and if start sweepline in LAST line of it
		
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
		/*if (currentSLPosition == 0) {
		    currentMPosition = Method.HANDLE_START.ordinal();
		    if (currentLinePosition == 0) {
			updateTextView(VertexType.START.toString(), Method.HANDLE_START.getLines(), -1, 0);
			currentVertex = currentVertices.get(0);
			markedVertex.setPos(currentVertex.getX() + 2.5, currentVertex.getY() + 2.5);
	
			historyVertices.add(currentVertex);
			boolean removal = currentVertices.remove(currentVertices.get(0));
			System.out.println("removal " + removal);
			currentEvent = initSweepLineEvent(currentVertex);
			sweepStraightLine.setY(currentEvent.getYOfSweepLine());
			UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(currentVertex.getNextEdge(), 1);
			subEvents.add(treeUpdate);
			currentLinePosition++;
		 } else if (currentLinePosition == 1) {
			updateTextView(VertexType.START.toString(), Method.HANDLE_START.getLines(), -1, 1);
			UpdateHelperSubEvent helperUpdate = updateHelper(currentVertex.getNextEdge(), currentVertex, 2);
			subEvents.add(helperUpdate);
			currentEvent.setSubEvents(subEvents);
			subEvents.clear();
			currentLinePosition = 0;
			currentSLPosition = 1;
		    }
		} else if (currentSLPosition == praeComputer.getVertices().size()) {
		    currentMPosition = Method.HANDLE_END.ordinal();
		    if (currentLinePosition == 0) {
			updateTextView(Method.HANDLE_END.getName(), Method.HANDLE_END.getLines(), -1, 0);
			currentVertex = currentVertices.get(0);
			markedVertex.setPos(currentVertex.getX() + 2.5, currentVertex.getY() + 2.5);
			historyVertices.add(currentVertex);
			currentVertices.remove(currentVertex);
			currentEvent = initSweepLineEvent(currentVertex);
			sweepStraightLine.setY(currentEvent.getYOfSweepLine());
			assert (currentVertex.getPrevEdge().getHelper() != null);
			boolean helperIsMerge = currentVertex.getPrevEdge().getHelper().isMergeVertex();
			BooleanSubEvent boolEvent = new BooleanSubEvent(1, helperIsMerge);
			subEvents.add(boolEvent);
			currentLinePosition++;
		    } else if (currentLinePosition == 1) {
			updateTextView(Method.HANDLE_END.getName(), Method.HANDLE_END.getLines(), -1, 1);
			boolean helperIsMerge = currentVertex.getPrevEdge().getHelper().isMergeVertex();
			if (helperIsMerge) {
			    AddDiagonalSubEvent addDiagonalEvent = addDiagonal(currentVertex, currentVertex.getPrevEdge(), 2);
			    subEvents.add(addDiagonalEvent);
			}
			currentLinePosition++;
		    } else if (currentLinePosition == 2) {
			updateTextView(Method.HANDLE_END.getName(), Method.HANDLE_END.getLines(), -1, 2);
			UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(currentVertex.getPrevEdge(), 3);
			subEvents.add(deletionEvent);
	
			currentEvent.setSubEvents(subEvents);
			subEvents.clear();
	
			currentLinePosition = 0;
			currentSLPosition = 0;
		    }
	
		} else {
		    if (currentLinePosition == 0) {
			currentVertex = currentVertices.get(0);
			markedVertex.setPos(currentVertex.getX() + 2.5, currentVertex.getY() + 2.5);
			historyVertices.add(currentVertex);
			currentVertices.remove(currentVertex);
	
			currentEvent = initSweepLineEvent(currentVertex);
			sweepStraightLine.setY(currentEvent.getYOfSweepLine());
			if (currentVertex.isSplitVertex()) {
			    currentMPosition = Method.HANDLE_SPLIT.ordinal();
			    updateTextView(Method.HANDLE_SPLIT.getName(), Method.HANDLE_SPLIT.getLines(), -1, 0);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    assert (leftOfVEdge != null);
			    SearchSubEvent searchEvent = new SearchSubEvent(1, leftOfVEdge);
			    subEvents.add(searchEvent);
			    currentLinePosition++;
			} else if (currentVertex.isMergeVertex()) {
			    currentMPosition = Method.HANDLE_MERGE.ordinal();
			    updateTextView(Method.HANDLE_MERGE.getName(), Method.HANDLE_MERGE.getLines(), -1, 0);
			    Edge prevEdge = currentVertex.getPrevEdge();
			    boolean helperIsMerge = prevEdge.getHelper().isMergeVertex();
			    BooleanSubEvent boolEvent = new BooleanSubEvent(1, helperIsMerge);
			    subEvents.add(boolEvent);
			    currentLinePosition++;
			} else if (currentVertex.isRegularRightVertex()) {
			    currentMPosition = Method.HANDLE_RIGHT_REGULAR.ordinal();
			    updateTextView(Method.HANDLE_RIGHT_REGULAR.getName(), Method.HANDLE_RIGHT_REGULAR.getLines(), -1, 0);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    SearchSubEvent searchEvent = new SearchSubEvent(1, leftOfVEdge);
			    subEvents.add(searchEvent);
			    currentLinePosition++;
			} else if (currentVertex.isRegularLeftVertex()) {
			    currentMPosition = Method.HANDLE_LEFT_REGULAR.ordinal();
			    updateTextView(Method.HANDLE_LEFT_REGULAR.getName(), Method.HANDLE_LEFT_REGULAR.getLines(), -1, 0);
			    Edge prevEdge = currentVertex.getPrevEdge();
			    // line 1: if helper of edge before v is merge vertex
			    boolean helperIsMerge = prevEdge.getHelper().isMergeVertex();
			    BooleanSubEvent boolEvent = new BooleanSubEvent(1, helperIsMerge);
			    subEvents.add(boolEvent);
			    currentLinePosition++;
			} else if (currentVertex.isStartVertex()) {
			    System.out.println("start!!");
			}
	
		    } else if (currentMPosition == Method.HANDLE_SPLIT.ordinal()) {
			if (currentLinePosition == 1) {
			    updateTextView(Method.HANDLE_SPLIT.getName(), Method.HANDLE_SPLIT.getLines(), -1, 1);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    AddDiagonalSubEvent addDiagonalEvent = addDiagonal(currentVertex, leftOfVEdge, 2);
			    subEvents.add(addDiagonalEvent);
			    currentLinePosition++;
			} else if (currentLinePosition == 2) {
			    updateTextView(Method.HANDLE_SPLIT.getName(), Method.HANDLE_SPLIT.getLines(), -1, 2);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    UpdateHelperSubEvent helperUpdate = updateHelper(leftOfVEdge, currentVertex, 3);
			    subEvents.add(helperUpdate);
			    currentLinePosition++;
			} else if (currentLinePosition == 3) {
			    updateTextView(Method.HANDLE_SPLIT.getName(), Method.HANDLE_SPLIT.getLines(), -1, 3);
			    Edge nextEdge = currentVertex.getNextEdge();
			    UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(nextEdge, 4);
			    subEvents.add(treeUpdate);
			    currentLinePosition++;
			} else if (currentLinePosition == 4) {
			    updateTextView(Method.HANDLE_SPLIT.getName(), Method.HANDLE_SPLIT.getLines(), -1, 4);
			    Edge nextEdge = currentVertex.getNextEdge();
			    UpdateHelperSubEvent helperUpdate = updateHelper(nextEdge, currentVertex, 5);
			    subEvents.add(helperUpdate);
			    currentEvent.setSubEvents(subEvents);
			    subEvents.clear();
			    currentLinePosition = 0;
			    currentSLPosition++;
			}
	
		    } else if (currentMPosition == Method.HANDLE_MERGE.ordinal()) {
			if (currentLinePosition == 1) {
			    updateTextView(Method.HANDLE_MERGE.getName(), Method.HANDLE_MERGE.getLines(), -1, 1);
			    Edge prevEdge = currentVertex.getPrevEdge();
			    boolean helperIsMerge = prevEdge.getHelper().isMergeVertex();
			    if (helperIsMerge) {
				AddDiagonalSubEvent addDiagonalEvent = addDiagonal(currentVertex, prevEdge, 2);
				subEvents.add(addDiagonalEvent);
			    }
			    currentLinePosition++;
			} else if (currentLinePosition == 2) {
			    updateTextView(Method.HANDLE_MERGE.getName(), Method.HANDLE_MERGE.getLines(), -1, 2);
			    Edge prevEdge = currentVertex.getPrevEdge();
			    UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge, 3);
			    subEvents.add(deletionEvent);
			    currentLinePosition++;
			} else if (currentLinePosition == 3) {
			    updateTextView(Method.HANDLE_MERGE.getName(), Method.HANDLE_MERGE.getLines(), -1, 3);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    SearchSubEvent searchEvent = new SearchSubEvent(4, leftOfVEdge);
			    subEvents.add(searchEvent);
			    currentLinePosition++;
			} else if (currentLinePosition == 4) {
			    updateTextView(Method.HANDLE_MERGE.getName(), Method.HANDLE_MERGE.getLines(), -1, 4);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    boolean helperIsMerge = leftOfVEdge.getHelper().isMergeVertex();
			    BooleanSubEvent boolEvent = new BooleanSubEvent(5, helperIsMerge);
			    subEvents.add(boolEvent);
			    currentLinePosition++;
			} else if (currentLinePosition == 5) {
			    updateTextView(Method.HANDLE_MERGE.getName(), Method.HANDLE_MERGE.getLines(), -1, 5);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    boolean helperIsMerge = leftOfVEdge.getHelper().isMergeVertex();
			    if (helperIsMerge) {
				AddDiagonalSubEvent addDiagonalEvent = addDiagonal(currentVertex, leftOfVEdge, 6);
				subEvents.add(addDiagonalEvent);
			    }
			    currentLinePosition++;
			} else if (currentLinePosition == 6) {
			    updateTextView(Method.HANDLE_MERGE.getName(), Method.HANDLE_MERGE.getLines(), -1, 6);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    UpdateHelperSubEvent helperUpdate = updateHelper(leftOfVEdge, currentVertex, 7);
			    subEvents.add(helperUpdate);
			    currentEvent.setSubEvents(subEvents);
			    subEvents.clear();
			    currentLinePosition = 0;
			    currentSLPosition++;
			}
	
		    } else if (currentMPosition == Method.HANDLE_RIGHT_REGULAR.ordinal()) {
			if (currentLinePosition == 1) {
			    updateTextView(Method.HANDLE_RIGHT_REGULAR.getName(), Method.HANDLE_RIGHT_REGULAR.getLines(), -1, 1);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    boolean helperIsMerge = leftOfVEdge.getHelper().isMergeVertex();
			    BooleanSubEvent boolEvent = new BooleanSubEvent(2, helperIsMerge);
			    subEvents.add(boolEvent);
			    currentLinePosition++;
			} else if (currentLinePosition == 2) {
			    updateTextView(Method.HANDLE_RIGHT_REGULAR.getName(), Method.HANDLE_RIGHT_REGULAR.getLines(), -1, 2);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    boolean helperIsMerge = leftOfVEdge.getHelper().isMergeVertex();
			    if (helperIsMerge) {
				AddDiagonalSubEvent addDiagonalEvent = addDiagonal(currentVertex, leftOfVEdge, 3);
				subEvents.add(addDiagonalEvent);
			    }
			    currentLinePosition++;
			} else if (currentLinePosition == 3) {
			    updateTextView(Method.HANDLE_RIGHT_REGULAR.getName(), Method.HANDLE_RIGHT_REGULAR.getLines(), -1, 3);
			    Edge leftOfVEdge = tree.searchEdge(currentVertex);
			    UpdateHelperSubEvent helperUpdate = updateHelper(leftOfVEdge, currentVertex, 4);
			    subEvents.add(helperUpdate);
			    currentEvent.setSubEvents(subEvents);
			    currentLinePosition = 0;
			    currentSLPosition++;
			}
	
		    } else if (currentMPosition == Method.HANDLE_LEFT_REGULAR.ordinal()) {
			if (currentLinePosition == 1) {
			    updateTextView(Method.HANDLE_LEFT_REGULAR.getName(), Method.HANDLE_LEFT_REGULAR.getLines(), -1, 1);
	
			    Edge prevEdge = currentVertex.getPrevEdge();
			    // line 1: if helper of edge before v is merge vertex
			    boolean helperIsMerge = prevEdge.getHelper().isMergeVertex();
			    if (helperIsMerge) {
				AddDiagonalSubEvent addDiagonalEvent = addDiagonal(currentVertex, prevEdge, 2);
				subEvents.add(addDiagonalEvent);
			    }
			    currentLinePosition++;
			} else if (currentLinePosition == 2) {
			    updateTextView(Method.HANDLE_LEFT_REGULAR.getName(), Method.HANDLE_LEFT_REGULAR.getLines(), -1, 2);
			    Edge prevEdge = currentVertex.getPrevEdge();
	
			    // line 3: delete prevEdge from tree
			    UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge, 3);
			    subEvents.add(deletionEvent);
			    currentLinePosition++;
			} else if (currentLinePosition == 3) {
			    updateTextView(Method.HANDLE_LEFT_REGULAR.getName(), Method.HANDLE_LEFT_REGULAR.getLines(), -1, 3);
			    Edge nextEdge = currentVertex.getNextEdge();
			    // line 4: insert edge in SearchTree
			    UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(nextEdge, 4);
			    subEvents.add(treeUpdate);
			    currentLinePosition++;
			} else if (currentLinePosition == 4) {
			    updateTextView(Method.HANDLE_LEFT_REGULAR.getName(), Method.HANDLE_LEFT_REGULAR.getLines(), -1, 4);
			    // line 5: set v as helper
			    Edge nextEdge = currentVertex.getNextEdge();
			    UpdateHelperSubEvent helperUpdate = updateHelper(nextEdge, currentVertex, 5);
			    subEvents.add(helperUpdate);
	
			    currentEvent.setSubEvents(subEvents);
			    subEvents.clear();
			    currentLinePosition = 0;
			    currentSLPosition++;
			}
		    }
	
		}*/
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

	    					time = velocity.getValue() * 1000;
	    					System.out.println("time " + time);
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
		
    	// set border of main panel red to signal that we are in draw mode
		// TODO ... 
		
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
    	}
    	
    	System.out.println("");
    	sweepLine.removeMouseListener(this);
    	sweepLine.repaint();
    	inDrawMode = false;
	}

	public void loadDataClicked() {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Text", "txt");
	    chooser.setFileFilter(filter);
	    File workingDirectory = new File(System.getProperty("user.dir") + File.separator 
	    		+ "PolygonExamples");
	    chooser.setCurrentDirectory(workingDirectory);
	    int returnVal = chooser.showOpenDialog(this);
	    String address = null;
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
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


 
	/*
	private SweepLineEvent handleStartVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v);
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();
		Edge nextEdge = v.getNextEdge();

		// line 1: insert edge in SearchTree
		UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(nextEdge, 1);
		subEvents.add(treeUpdate);

		// line 2: set v as helper
		UpdateHelperSubEvent helperUpdate = updateHelper(nextEdge, v, 2);
		subEvents.add(helperUpdate);

		event.setSubEvents(subEvents);
		return event;
	    }

	private SweepLineEvent handleEndVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v);
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();
		Edge prevEdge = v.getPrevEdge();

		// line 1: if helper is merge vertex
		assert (prevEdge.getHelper() != null);
		boolean helperIsMerge = prevEdge.getHelper().isMergeVertex();
		BooleanSubEvent boolEvent = new BooleanSubEvent(1, helperIsMerge);
		subEvents.add(boolEvent);

		// then line 2: insert diagonal v-helper
		if (helperIsMerge) {
		    AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, prevEdge, 2);
		    subEvents.add(addDiagonalEvent);
		}

		// line 3: delete prevEdge from tree
		UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge, 3);
		subEvents.add(deletionEvent);

		event.setSubEvents(subEvents);
		return event;
	    }

	private SweepLineEvent handleSplitVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v);
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();

		// line 1: search in tree to find edge left of v
		Edge leftOfVEdge = tree.searchEdge(v);
		assert (leftOfVEdge != null);
		SearchSubEvent searchEvent = new SearchSubEvent(1, leftOfVEdge);
		subEvents.add(searchEvent);

		// line 2: add diagonal to helper of found edge and split vertex v
		AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, leftOfVEdge, 2);
		subEvents.add(addDiagonalEvent);

		// line 3: update helper - v is now helper
		UpdateHelperSubEvent helperUpdate = updateHelper(leftOfVEdge, v, 3);
		subEvents.add(helperUpdate);

		// line 4: insert next edge of v in t
		Edge nextEdge = v.getNextEdge();
		UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(nextEdge, 4);
		subEvents.add(treeUpdate);

		// line 5: set v as helper of this edge
		UpdateHelperSubEvent helperUpdate2 = updateHelper(nextEdge, v, 5);
		subEvents.add(helperUpdate2);

		event.setSubEvents(subEvents);
		return event;
	    }

	private SweepLineEvent handleMergeVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v);
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();
		Edge prevEdge = v.getPrevEdge();

		// line 1: if helper of edge right of v is merge vertex
		boolean helperIsMerge = prevEdge.getHelper().isMergeVertex();
		BooleanSubEvent boolEvent = new BooleanSubEvent(1, helperIsMerge);
		subEvents.add(boolEvent);

		// then line 2: insert diagonal v-helper
		if (helperIsMerge) {
		    AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, prevEdge, 2);
		    subEvents.add(addDiagonalEvent);
		}

		// line 3: delete prevEdge from tree
		UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge, 3);
		subEvents.add(deletionEvent);

		// line 4: search in tree to find edge left of v
		Edge leftOfVEdge = tree.searchEdge(v);
		SearchSubEvent searchEvent = new SearchSubEvent(4, leftOfVEdge);
		subEvents.add(searchEvent);

		// line 5: if helper of edge left of v is merge vertex
		boolean helperIsMerge2 = leftOfVEdge.getHelper().isMergeVertex();
		BooleanSubEvent boolEvent2 = new BooleanSubEvent(5, helperIsMerge2);
		subEvents.add(boolEvent2);

		// then line 6: insert diagonal v-helper
		if (helperIsMerge2) {
		    AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, leftOfVEdge, 6);
		    subEvents.add(addDiagonalEvent);
		}

		// line 7: set v as helper of this edge
		UpdateHelperSubEvent helperUpdate = updateHelper(leftOfVEdge, v, 7);
		subEvents.add(helperUpdate);

		event.setSubEvents(subEvents);
		return event;
	    }

	 private SweepLineEvent handleRegularLeftVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v);
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();

		Edge prevEdge = v.getPrevEdge();
		// line 1: if helper of edge before v is merge vertex
		boolean helperIsMerge = prevEdge.getHelper().isMergeVertex();
		BooleanSubEvent boolEvent = new BooleanSubEvent(1, helperIsMerge);
		subEvents.add(boolEvent);

		// then line 2: insert diagonal v-helper
		if (helperIsMerge) {
		    AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, prevEdge, 2);
		    subEvents.add(addDiagonalEvent);
		}

		// line 3: delete prevEdge from tree
		UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge, 3);
		subEvents.add(deletionEvent);

		Edge nextEdge = v.getNextEdge();
		// line 4: insert edge in SearchTree
		UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(nextEdge, 4);
		subEvents.add(treeUpdate);

		// line 5: set v as helper
		UpdateHelperSubEvent helperUpdate = updateHelper(nextEdge, v, 5);
		subEvents.add(helperUpdate);

		event.setSubEvents(subEvents);
		return event;
	    }

	private SweepLineEvent handleRegularRightVertex(Vertex v) {
		SweepLineEvent event = initSweepLineEvent(v);
		LinkedList<SubEvent> subEvents = new LinkedList<SubEvent>();

		// line 1: search in tree to find edge left of v
		Edge leftOfVEdge = tree.searchEdge(v);
		SearchSubEvent searchEvent = new SearchSubEvent(1, leftOfVEdge);
		subEvents.add(searchEvent);

		// line 2: if helper of edge left of v is merge vertex
		boolean helperIsMerge = leftOfVEdge.getHelper().isMergeVertex();
		BooleanSubEvent boolEvent = new BooleanSubEvent(2, helperIsMerge);
		subEvents.add(boolEvent);

		// then line 3: insert diagonal v-helper
		if (helperIsMerge) {
		    AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, leftOfVEdge, 3);
		    subEvents.add(addDiagonalEvent);
		}

		// line 4: set v as helper of this edge
		UpdateHelperSubEvent helperUpdate = updateHelper(leftOfVEdge, v, 4);
		subEvents.add(helperUpdate);

		event.setSubEvents(subEvents);
		return event;
	    }

	
*/


}
