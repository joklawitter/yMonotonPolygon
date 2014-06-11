package yMonotonePolygon.QUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

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
import yMonotonePolygon.GUI.GUIColorConfiguration;
import yMonotonePolygon.PraeComputation.PraeComputer;
import yMonotonePolygon.Tests.TestHelper;

import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QButtonGroup;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsEllipseItem;
import com.trolltech.qt.gui.QGraphicsItem.GraphicsItemFlag;
import com.trolltech.qt.gui.QGraphicsLineItem;
import com.trolltech.qt.gui.QGraphicsPolygonItem;
import com.trolltech.qt.gui.QGraphicsRectItem;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsView;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineF;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QPolygonF;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSlider;
import com.trolltech.qt.gui.QWidget;

public class YMonotonePolygonQUI extends QWidget {


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

	private LinkedList<QGraphicsLineItem> diagonalItems;
	private LinkedList<SubEvent> subEvents;
	// things to keep during the algorithm
	/** number of handled vertices */
	private int handledVertices;

	private SweepLineEvent currentEvent;

	/** binary search tree of the active edges */
	private SearchTree tree;

	/** active edges, those crossing the sweep line and pointing down */
	private TreeSet<Edge> activeEdges;

	public QGraphicsScene sweepLine;
	private QGraphicsEllipseItem markedVertex;
	public QGraphicsScene treeDataStructure;
	public QLabel codeSegment;
	private QLabel codeSegment1;
	private QLabel codeSegment2;
	private QLabel codeSegment3;
	private QLabel codeSegment4;
	private QLabel codeSegment5;
	private QLabel codeSegment6;
	public QButtonGroup algorithmController;
	public QPushButton stepBack;
	public QPushButton stepForward;
	public QPushButton lineUp;
	public QPushButton lineDown;
	public QPushButton skipBack;
	public QPushButton skipForward;
	public QPushButton play;
	// public QPushButton pause;
	private boolean isPaused;
	public QSlider velocity;

	public QLabel methodTitle;
	public QLabel treeTitle;
	public QPushButton editBtn;
	public QPushButton loadData;
	public QButtonGroup editButtonGroup;

	public QGridLayout mainLayout;
	public long time;

	public PraeComputer praeComputer;
	private LinkedList<SweepLineEvent> currentState;
	private LinkedList<SweepLineEvent> currentHistory;
	private QPolygonF p;
	private int currentSLPosition;
	private int currentMPosition;
	private int currentLinePosition;
	private Vertex currentVertex;
	private boolean nextStep;

	private QGraphicsLineItem leftMarkedEdge;
	private QGraphicsLineItem sweepStraightLine;

	public YMonotonePolygonQUI() {
		isPaused = true;
		currentSLPosition = 0;
		currentMPosition = 0;
		currentLinePosition = 0;
		sweepLine = new QGraphicsScene(new QRectF(0, 0, 500, 300));
		sweepLine.setBackgroundBrush(new QBrush(QColor.white));
		treeDataStructure = new QGraphicsScene(new QRectF(0, 0, 50, 20));
		treeDataStructure.setBackgroundBrush(new QBrush(QColor.green));
		codeSegment = new QLabel();
		codeSegment1 = new QLabel("");
		codeSegment2 = new QLabel("");
		codeSegment3 = new QLabel("");
		codeSegment4 = new QLabel("");
		codeSegment5 = new QLabel("");
		codeSegment6 = new QLabel("");
		algorithmController = new QButtonGroup();
		stepBack = new QPushButton("|<<");
		stepBack.setToolTip("go to previous line of code");
		stepBack.clicked.connect(this, "stepBackClicked()");
		stepForward = new QPushButton(">>|");
		stepForward.setToolTip("go to next line of code");
		stepForward.clicked.connect(this, "stepForwardClicked()");
		lineUp = new QPushButton("Up");
		lineUp.clicked.connect(this, "lineUpClicked()");
		lineUp.setToolTip("Go to previous sweepline event");
		lineDown = new QPushButton("Down");
		lineDown.setToolTip("Go to next sweepline event");
		lineDown.clicked.connect(this, "lineDownClicked()");
		skipBack = new QPushButton("|<");
		skipBack.setToolTip("skip to the beginning of the algorithm");
		skipBack.clicked.connect(this, "skipBackClicked()");
		skipForward = new QPushButton(">|");
		skipForward.setToolTip("skip to the end of the algorithm");
		skipForward.clicked.connect(this, "skipForwardClicked()");
		play = new QPushButton("play");
		play.setToolTip("Automatically step through algorithm");
		play.clicked.connect(this, "playClicked()");
		// pause = new QPushButton("||");
		// pause.setToolTip("pause Algorithm");
		// pause.clicked.connect(this, "pauseClicked()");
		algorithmController.addButton(lineUp);
		algorithmController.addButton(skipBack);
		algorithmController.addButton(stepBack);
		// algorithmController.addButton(pause);
		algorithmController.addButton(play);
		algorithmController.addButton(stepForward);
		algorithmController.addButton(skipForward);
		algorithmController.addButton(lineDown);

		methodTitle = new QLabel("MethodName");

		editBtn = new QPushButton("edit");
		editBtn.clicked.connect(this, "editBtnClicked()");
		loadData = new QPushButton("Load");
		loadData.clicked.connect(this, "loadDataClicked()");
		velocity = new QSlider(Orientation.Horizontal);
		velocity.setRange(2, 10);

		editButtonGroup = new QButtonGroup();
		editButtonGroup.addButton(editBtn);
		editButtonGroup.addButton(loadData);
		QLineF sweepStraight = new QLineF(0, 0, 500, 0);
		sweepStraightLine = new QGraphicsLineItem(sweepStraight, null,
				sweepLine);
		QPen pen = new QPen(QColor.black);
		pen.setStyle(Qt.PenStyle.DashLine);
		pen.setBrush(new QBrush(QColor.black));
		sweepStraightLine.setPen(pen);
		diagonals = new LinkedList<Edge>();
		handledVertices = 0;
		tree = new SearchTree();
		activeEdges = new TreeSet<Edge>();
		subEvents = new LinkedList<SubEvent>();
		diagonalItems = new LinkedList<QGraphicsLineItem>();
		layoutView();
		
		start();

	}

	public void setPolygon(QPolygonF p) {
		this.p = p;
	}

	public void initialize() {

	}

	public void start() {
		QPolygonF points2 = new QPolygonF();
		points2 = TestHelper.readTestQPolygonF("testOneSplit");
		// points2.add(150, 150);
		// points2.add(200, 200);
		// points2.add(140, 10);
		// points2.add(100, 180);
		// points2.add(150, 150);
		// points2.add(200, 30);
		tree = new SearchTree();
		praeComputer = new PraeComputer();

		QGraphicsPolygonItem g = new QGraphicsPolygonItem(points2, null,
				sweepLine);
		g.setPen(new QPen(QColor.red));
		g.setBrush(new QBrush(QColor.white));
		g.setFlag(GraphicsItemFlag.ItemIsMovable, false);
		g.setFlag(GraphicsItemFlag.ItemStacksBehindParent, false);
		praeComputer.work(points2);
		currentState = praeComputer.getHistory();
		currentHistory = new LinkedList<SweepLineEvent>();
		currentVertices = new ArrayList<Vertex>(praeComputer.getVertices());
		historyVertices = new ArrayList<Vertex>();
		leftMarkedEdge = new QGraphicsLineItem(0.0, 0.0, 3.0, 3.0, null,
				sweepLine);
		leftMarkedEdge.setPen(new QPen(QColor.darkGreen));
		for (int i = 0; i < currentVertices.size(); i++) {
			QGraphicsEllipseItem vertexPoint = new QGraphicsEllipseItem(
					currentVertices.get(i).getX() - 2.5, currentVertices.get(i)
							.getY() - 2.5, 5, 5, null, sweepLine);
			vertexPoint.setBrush(new QBrush(QColor.black));
			vertexPoint.setPen(new QPen(QColor.black));
			QRectF rect = new QRectF(new QPointF(0.0, 0.0), new QPointF(5.0,
					5.0));
			QGraphicsRectItem rectItem = new QGraphicsRectItem(rect, null,
					sweepLine);
			rectItem.setBrush(new QBrush(QColor.black));
			rectItem.setPen(new QPen(QColor.black));
			rectItem.setY(currentVertices.get(i).getY() - 2.5);
		}
		markedVertex = new QGraphicsEllipseItem(-5, -5, 5, 5, null, sweepLine);
		markedVertex.setBrush(new QBrush(QColor.red));
		markedVertex.setPen(new QPen(QColor.red));
		time = 1000;
	}

	public void layoutView() {
		mainLayout = new QGridLayout(this);
		mainLayout.setRowMinimumHeight(1, 300);
		QGridLayout editButtonLayout = new QGridLayout();
		editButtonLayout.addWidget(editBtn, 0, 0);
		editButtonLayout.addWidget(loadData, 0, 1);
		editButtonLayout.addWidget(velocity, 0, 2);
		QGridLayout buttonLayout = new QGridLayout();
		buttonLayout.addWidget(lineUp, 0, 0);
		buttonLayout.addWidget(skipBack, 0, 1);
		buttonLayout.addWidget(stepBack, 0, 2);
		// buttonLayout.addWidget(pause, 0, 3);
		buttonLayout.addWidget(play, 0, 4);
		buttonLayout.addWidget(stepForward, 0, 5);
		buttonLayout.addWidget(skipForward, 0, 6);
		buttonLayout.addWidget(lineDown, 0, 7);
		mainLayout.addLayout(buttonLayout, 0, 0);
		mainLayout.addLayout(editButtonLayout, 0, 1);
		QGraphicsView sweepLineView = new QGraphicsView(sweepLine);
		sweepLineView.setBackgroundBrush(new QBrush(new QColor(233, 233, 233)));
		mainLayout.addWidget(sweepLineView, 1, 0);
		QGraphicsView treeView = new QGraphicsView(treeDataStructure);
		mainLayout.addWidget(treeView, 2, 0);
		mainLayout.addWidget(methodTitle, 3, 0);
		mainLayout.addWidget(codeSegment, 4, 0);
		mainLayout.addWidget(codeSegment1, 5, 0);
		mainLayout.addWidget(codeSegment2, 6, 0);
		mainLayout.addWidget(codeSegment3, 7, 0);
		mainLayout.addWidget(codeSegment4, 8, 0);
		mainLayout.addWidget(codeSegment5, 9, 0);
		mainLayout.addWidget(codeSegment6, 10, 0);

		currentLinePosition = 0;
		currentSLPosition = 0;
	}

	public void lineUpClicked() {
		if (currentHistory.size() > 0) {
			SweepLineEvent sle = currentHistory.getLast();
			sweepStraightLine.setY(sle.getYOfSweepLine());
			currentState.add(currentHistory.getLast());
			currentHistory.removeLast();
			--currentSLPosition;
		}
	}

	public void lineDownClicked() {
		currentSLPosition++;
		if (currentSLPosition >= praeComputer.getHistory().size()) {
			currentSLPosition = praeComputer.getHistory().size() - 1;	
		}
		SweepLineEvent sle = praeComputer.getHistory().get(currentSLPosition);
		sweepStraightLine.setY(sle.getYOfSweepLine());
		currentLinePosition = 0;
		markedVertex.setPos(sle.getVertex().getX() + 2.5, sle.getVertex().getY() + 2.5);
		markedVertex.setPen(new QPen(new QColor(sle.getVertex().getColor().getRGB())));
		markedVertex.setBrush(new QBrush(new QColor(sle.getVertex().getColor().getRGB())));
		diagonalItems.clear();
		
		for (int i = 0; i < sle.getNumberOfDiagonals(); i++) {
			QGraphicsLineItem tmp = new QGraphicsLineItem(praeComputer.getDiagonals().get(i).getStartVertex().getX(), praeComputer.getDiagonals().get(i).getStartVertex().getY(), praeComputer.getDiagonals().get(i).getEndVertex().getX(), praeComputer.getDiagonals().get(i).getEndVertex().getY(), null, sweepLine);
			diagonalItems.add(tmp);
		}
		
		
//		if (currentVertices.size() != 0) {
//			currentVertex = currentVertices.get(0);
//			currentVertices.remove(0);
//			sweepStraightLine.setY(currentVertex.getY());
//			if (currentVertex.isStartVertex()) {
//				currentEvent = handleStartVertex(currentVertex);
//			} else if (currentVertex.isEndVertex()) {
//				currentEvent = handleEndVertex(currentVertex);
//			} else if (currentVertex.isSplitVertex()) {
//				currentEvent = handleSplitVertex(currentVertex);
//			} else if (currentVertex.isMergeVertex()) {
//				currentEvent = handleMergeVertex(currentVertex);
//			} else if (currentVertex.isRegularRightVertex()) {
//				currentEvent = handleRegularRightVertex(currentVertex);
//			} else if (currentVertex.isRegularLeftVertex()) {
//				currentEvent = handleRegularLeftVertex(currentVertex);
//			}
//			currentSLPosition++;
//			currentLinePosition = 0;
//		}
	}

	public void skipBackClicked() {
		SweepLineEvent sle = praeComputer.getHistory().getFirst();
		sweepStraightLine.setY(sle.getYOfSweepLine());
		currentState.clear();
		currentState.addAll((LinkedList<SweepLineEvent>) praeComputer
				.getHistory());
		currentHistory.clear();
		currentSLPosition = 0;
	}

	public void skipForwardClicked() {
		SweepLineEvent sle = praeComputer.getHistory().getLast();
		sweepStraightLine.setY(sle.getYOfSweepLine());
		currentSLPosition = praeComputer.getHistory().size() - 1;
	}

	public void stepBackClicked() {
	}

	public void stepForwardClicked() {
		System.out.println("stepForward SL: " + currentSLPosition + " Method: "
				+ currentMPosition + " Line: " + currentLinePosition);
		currentLinePosition++;
		SweepLineEvent sle = praeComputer.getHistory().get(currentSLPosition);
		if(currentLinePosition >= sle.getSubEvents().size()) {
			currentLinePosition = 0;
			currentSLPosition++;
			if (currentSLPosition >= praeComputer.getHistory().size()) {
				currentSLPosition = praeComputer.getHistory().size() - 1;
				currentLinePosition =  (praeComputer.getHistory().get(currentSLPosition).getSubEvents().size() == 0) ?  0 : (praeComputer.getHistory().get(currentSLPosition).getSubEvents().size() - 1);
			}
		} else {
			sle = praeComputer.getHistory().get(currentSLPosition);
		}
				
		updateTextView(sle.getVertexType().toString(), sle.getVertexType().getCorrespondingMethod().getLines(), -1, currentLinePosition);
		markedVertex.setPos(sle.getVertex().getX() + 2.5, sle.getVertex().getY() + 2.5);
		markedVertex.setPen(new QPen(new QColor(sle.getVertex().getColor().getRGB())));
		markedVertex.setBrush(new QBrush(new QColor(sle.getVertex().getColor().getRGB())));
		sweepStraightLine.setY(sle.getYOfSweepLine());
		diagonalItems.clear();
		
		
		for (int i = 0; i < sle.getNumberOfDiagonals(); i++) {
			Edge tmp = praeComputer.getDiagonals().get(i);
			QGraphicsLineItem tmpLine = new QGraphicsLineItem(tmp.getStartVertex().getX(), tmp.getStartVertex().getY(), tmp.getEndVertex().getX(), tmp.getEndVertex().getY(), null, sweepLine);
			tmpLine.setPen(new QPen(QColor.darkBlue));
			diagonalItems.add(tmpLine);
		}

//		if (currentSLPosition == 0) {
//			currentMPosition = Method.HANDLE_START.ordinal();
//			if (currentLinePosition == 0) {
//				updateTextView(VertexType.START.toString(),
//						Method.HANDLE_START.getLines(), -1, 0);
//				currentVertex = currentVertices.get(0);
//				markedVertex.setPos(currentVertex.getX() + 2.5,
//						currentVertex.getY() + 2.5);
//
//				historyVertices.add(currentVertex);
//				boolean removal = currentVertices
//						.remove(currentVertices.get(0));
//				System.out.println("removal " + removal);
//				currentEvent = initSweepLineEvent(currentVertex);
//				sweepStraightLine.setY(currentEvent.getYOfSweepLine());
//				UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(
//						currentVertex.getNextEdge(), 1);
//				subEvents.add(treeUpdate);
//				currentLinePosition++;
//			} else if (currentLinePosition == 1) {
//				updateTextView(VertexType.START.toString(),
//						Method.HANDLE_START.getLines(), -1, 1);
//				UpdateHelperSubEvent helperUpdate = updateHelper(
//						currentVertex.getNextEdge(), currentVertex, 2);
//				subEvents.add(helperUpdate);
//				currentEvent.setSubEvents(subEvents);
//				subEvents.clear();
//				currentLinePosition = 0;
//				currentSLPosition = 1;
//			}
//		} else if (currentSLPosition == praeComputer.getVertices().size()) {
//			currentMPosition = Method.HANDLE_END.ordinal();
//			if (currentLinePosition == 0) {
//				updateTextView(Method.HANDLE_END.getName(),
//						Method.HANDLE_END.getLines(), -1, 0);
//				currentVertex = currentVertices.get(0);
//				markedVertex.setPos(currentVertex.getX() + 2.5,
//						currentVertex.getY() + 2.5);
//				historyVertices.add(currentVertex);
//				currentVertices.remove(currentVertex);
//				currentEvent = initSweepLineEvent(currentVertex);
//				sweepStraightLine.setY(currentEvent.getYOfSweepLine());
//				assert (currentVertex.getPrevEdge().getHelper() != null);
//				boolean helperIsMerge = currentVertex.getPrevEdge().getHelper()
//						.isMergeVertex();
//				BooleanSubEvent boolEvent = new BooleanSubEvent(1,
//						helperIsMerge);
//				subEvents.add(boolEvent);
//				currentLinePosition++;
//			} else if (currentLinePosition == 1) {
//				updateTextView(Method.HANDLE_END.getName(),
//						Method.HANDLE_END.getLines(), -1, 1);
//				boolean helperIsMerge = currentVertex.getPrevEdge().getHelper()
//						.isMergeVertex();
//				if (helperIsMerge) {
//					AddDiagonalSubEvent addDiagonalEvent = addDiagonal(
//							currentVertex, currentVertex.getPrevEdge(), 2);
//					subEvents.add(addDiagonalEvent);
//				}
//				currentLinePosition++;
//			} else if (currentLinePosition == 2) {
//				updateTextView(Method.HANDLE_END.getName(),
//						Method.HANDLE_END.getLines(), -1, 2);
//				UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(
//						currentVertex.getPrevEdge(), 3);
//				subEvents.add(deletionEvent);
//
//				currentEvent.setSubEvents(subEvents);
//				subEvents.clear();
//
//				currentLinePosition = 0;
//				currentSLPosition = 0;
//			}
//
//		} else {
//			if (currentLinePosition == 0) {
//				currentVertex = currentVertices.get(0);
//				markedVertex.setPos(currentVertex.getX() + 2.5,
//						currentVertex.getY() + 2.5);
//				historyVertices.add(currentVertex);
//				currentVertices.remove(currentVertex);
//
//				currentEvent = initSweepLineEvent(currentVertex);
//				sweepStraightLine.setY(currentEvent.getYOfSweepLine());
//				if (currentVertex.isSplitVertex()) {
//					currentMPosition = Method.HANDLE_SPLIT.ordinal();
//					updateTextView(Method.HANDLE_SPLIT.getName(),
//							Method.HANDLE_SPLIT.getLines(), -1, 0);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					assert (leftOfVEdge != null);
//					SearchSubEvent searchEvent = new SearchSubEvent(1,
//							leftOfVEdge);
//					subEvents.add(searchEvent);
//					currentLinePosition++;
//				} else if (currentVertex.isMergeVertex()) {
//					currentMPosition = Method.HANDLE_MERGE.ordinal();
//					updateTextView(Method.HANDLE_MERGE.getName(),
//							Method.HANDLE_MERGE.getLines(), -1, 0);
//					Edge prevEdge = currentVertex.getPrevEdge();
//					boolean helperIsMerge = prevEdge.getHelper()
//							.isMergeVertex();
//					BooleanSubEvent boolEvent = new BooleanSubEvent(1,
//							helperIsMerge);
//					subEvents.add(boolEvent);
//					currentLinePosition++;
//				} else if (currentVertex.isRegularRightVertex()) {
//					currentMPosition = Method.HANDLE_RIGHT_REGULAR.ordinal();
//					updateTextView(Method.HANDLE_RIGHT_REGULAR.getName(),
//							Method.HANDLE_RIGHT_REGULAR.getLines(), -1, 0);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					SearchSubEvent searchEvent = new SearchSubEvent(1,
//							leftOfVEdge);
//					subEvents.add(searchEvent);
//					currentLinePosition++;
//				} else if (currentVertex.isRegularLeftVertex()) {
//					currentMPosition = Method.HANDLE_LEFT_REGULAR.ordinal();
//					updateTextView(Method.HANDLE_LEFT_REGULAR.getName(),
//							Method.HANDLE_LEFT_REGULAR.getLines(), -1, 0);
//					Edge prevEdge = currentVertex.getPrevEdge();
//					// line 1: if helper of edge before v is merge vertex
//					boolean helperIsMerge = prevEdge.getHelper()
//							.isMergeVertex();
//					BooleanSubEvent boolEvent = new BooleanSubEvent(1,
//							helperIsMerge);
//					subEvents.add(boolEvent);
//					currentLinePosition++;
//				} else if (currentVertex.isStartVertex()) {
//					System.out.println("start!!");
//				}
//
//			} else if (currentMPosition == Method.HANDLE_SPLIT.ordinal()) {
//				if (currentLinePosition == 1) {
//					updateTextView(Method.HANDLE_SPLIT.getName(),
//							Method.HANDLE_SPLIT.getLines(), -1, 1);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					AddDiagonalSubEvent addDiagonalEvent = addDiagonal(
//							currentVertex, leftOfVEdge, 2);
//					subEvents.add(addDiagonalEvent);
//					currentLinePosition++;
//				} else if (currentLinePosition == 2) {
//					updateTextView(Method.HANDLE_SPLIT.getName(),
//							Method.HANDLE_SPLIT.getLines(), -1, 2);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					UpdateHelperSubEvent helperUpdate = updateHelper(
//							leftOfVEdge, currentVertex, 3);
//					subEvents.add(helperUpdate);
//					currentLinePosition++;
//				} else if (currentLinePosition == 3) {
//					updateTextView(Method.HANDLE_SPLIT.getName(),
//							Method.HANDLE_SPLIT.getLines(), -1, 3);
//					Edge nextEdge = currentVertex.getNextEdge();
//					UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(
//							nextEdge, 4);
//					subEvents.add(treeUpdate);
//					currentLinePosition++;
//				} else if (currentLinePosition == 4) {
//					updateTextView(Method.HANDLE_SPLIT.getName(),
//							Method.HANDLE_SPLIT.getLines(), -1, 4);
//					Edge nextEdge = currentVertex.getNextEdge();
//					UpdateHelperSubEvent helperUpdate = updateHelper(nextEdge,
//							currentVertex, 5);
//					subEvents.add(helperUpdate);
//					currentEvent.setSubEvents(subEvents);
//					subEvents.clear();
//					currentLinePosition = 0;
//					currentSLPosition++;
//				}
//
//			} else if (currentMPosition == Method.HANDLE_MERGE.ordinal()) {
//				if (currentLinePosition == 1) {
//					updateTextView(Method.HANDLE_MERGE.getName(),
//							Method.HANDLE_MERGE.getLines(), -1, 1);
//					Edge prevEdge = currentVertex.getPrevEdge();
//					boolean helperIsMerge = prevEdge.getHelper()
//							.isMergeVertex();
//					if (helperIsMerge) {
//						AddDiagonalSubEvent addDiagonalEvent = addDiagonal(
//								currentVertex, prevEdge, 2);
//						subEvents.add(addDiagonalEvent);
//					}
//					currentLinePosition++;
//				} else if (currentLinePosition == 2) {
//					updateTextView(Method.HANDLE_MERGE.getName(),
//							Method.HANDLE_MERGE.getLines(), -1, 2);
//					SweepLineEvent tmp = praeComputer.getHistory().get(currentSLPosition);
//					Edge prevEdge = currentVertex.getPrevEdge();
//					UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(
//							prevEdge, 3);
//					subEvents.add(deletionEvent);
//					currentLinePosition++;
//				} else if (currentLinePosition == 3) {
//					updateTextView(Method.HANDLE_MERGE.getName(),
//							Method.HANDLE_MERGE.getLines(), -1, 3);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					SearchSubEvent searchEvent = new SearchSubEvent(4,
//							leftOfVEdge);
//					subEvents.add(searchEvent);
//					currentLinePosition++;
//				} else if (currentLinePosition == 4) {
//					updateTextView(Method.HANDLE_MERGE.getName(),
//							Method.HANDLE_MERGE.getLines(), -1, 4);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					boolean helperIsMerge = leftOfVEdge.getHelper()
//							.isMergeVertex();
//					BooleanSubEvent boolEvent = new BooleanSubEvent(5,
//							helperIsMerge);
//					subEvents.add(boolEvent);
//					currentLinePosition++;
//				} else if (currentLinePosition == 5) {
//					updateTextView(Method.HANDLE_MERGE.getName(),
//							Method.HANDLE_MERGE.getLines(), -1, 5);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					boolean helperIsMerge = leftOfVEdge.getHelper()
//							.isMergeVertex();
//					if (helperIsMerge) {
//						AddDiagonalSubEvent addDiagonalEvent = addDiagonal(
//								currentVertex, leftOfVEdge, 6);
//						subEvents.add(addDiagonalEvent);
//					}
//					currentLinePosition++;
//				} else if (currentLinePosition == 6) {
//					updateTextView(Method.HANDLE_MERGE.getName(),
//							Method.HANDLE_MERGE.getLines(), -1, 6);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					UpdateHelperSubEvent helperUpdate = updateHelper(
//							leftOfVEdge, currentVertex, 7);
//					subEvents.add(helperUpdate);
//					currentEvent.setSubEvents(subEvents);
//					subEvents.clear();
//					currentLinePosition = 0;
//					currentSLPosition++;
//				}
//
//			} else if (currentMPosition == Method.HANDLE_RIGHT_REGULAR
//					.ordinal()) {
//				if (currentLinePosition == 1) {
//					updateTextView(Method.HANDLE_RIGHT_REGULAR.getName(),
//							Method.HANDLE_RIGHT_REGULAR.getLines(), -1, 1);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					boolean helperIsMerge = leftOfVEdge.getHelper()
//							.isMergeVertex();
//					BooleanSubEvent boolEvent = new BooleanSubEvent(2,
//							helperIsMerge);
//					subEvents.add(boolEvent);
//					currentLinePosition++;
//				} else if (currentLinePosition == 2) {
//					updateTextView(Method.HANDLE_RIGHT_REGULAR.getName(),
//							Method.HANDLE_RIGHT_REGULAR.getLines(), -1, 2);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					boolean helperIsMerge = leftOfVEdge.getHelper()
//							.isMergeVertex();
//					if (helperIsMerge) {
//						AddDiagonalSubEvent addDiagonalEvent = addDiagonal(
//								currentVertex, leftOfVEdge, 3);
//						subEvents.add(addDiagonalEvent);
//					}
//					currentLinePosition++;
//				} else if (currentLinePosition == 3) {
//					updateTextView(Method.HANDLE_RIGHT_REGULAR.getName(),
//							Method.HANDLE_RIGHT_REGULAR.getLines(), -1, 3);
//					Edge leftOfVEdge = tree.searchEdge(currentVertex);
//					UpdateHelperSubEvent helperUpdate = updateHelper(
//							leftOfVEdge, currentVertex, 4);
//					subEvents.add(helperUpdate);
//					currentEvent.setSubEvents(subEvents);
//					currentLinePosition = 0;
//					currentSLPosition++;
//				}
//
//			} else if (currentMPosition == Method.HANDLE_LEFT_REGULAR.ordinal()) {
//				if (currentLinePosition == 1) {
//					updateTextView(Method.HANDLE_LEFT_REGULAR.getName(),
//							Method.HANDLE_LEFT_REGULAR.getLines(), -1, 1);
//
//					Edge prevEdge = currentVertex.getPrevEdge();
//					// line 1: if helper of edge before v is merge vertex
//					boolean helperIsMerge = prevEdge.getHelper()
//							.isMergeVertex();
//					if (helperIsMerge) {
//						AddDiagonalSubEvent addDiagonalEvent = addDiagonal(
//								currentVertex, prevEdge, 2);
//						subEvents.add(addDiagonalEvent);
//					}
//					currentLinePosition++;
//				} else if (currentLinePosition == 2) {
//					updateTextView(Method.HANDLE_LEFT_REGULAR.getName(),
//							Method.HANDLE_LEFT_REGULAR.getLines(), -1, 2);
//					Edge prevEdge = currentVertex.getPrevEdge();
//
//					// line 3: delete prevEdge from tree
//					UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(
//							prevEdge, 3);
//					subEvents.add(deletionEvent);
//					currentLinePosition++;
//				} else if (currentLinePosition == 3) {
//					updateTextView(Method.HANDLE_LEFT_REGULAR.getName(),
//							Method.HANDLE_LEFT_REGULAR.getLines(), -1, 3);
//					Edge nextEdge = currentVertex.getNextEdge();
//					// line 4: insert edge in SearchTree
//					UpdateInsertTreeSubEvent treeUpdate = insertEdgeInTree(
//							nextEdge, 4);
//					subEvents.add(treeUpdate);
//					currentLinePosition++;
//				} else if (currentLinePosition == 4) {
//					updateTextView(Method.HANDLE_LEFT_REGULAR.getName(),
//							Method.HANDLE_LEFT_REGULAR.getLines(), -1, 4);
//					// line 5: set v as helper
//					Edge nextEdge = currentVertex.getNextEdge();
//					UpdateHelperSubEvent helperUpdate = updateHelper(nextEdge,
//							currentVertex, 5);
//					subEvents.add(helperUpdate);
//
//					currentEvent.setSubEvents(subEvents);
//					subEvents.clear();
//					currentLinePosition = 0;
//					currentSLPosition++;
//				}
//			}
//
//		}
	}

	public void playClicked() {
		isPaused = !isPaused;
		if (!isPaused) {
			play.setText("||");
			play.setToolTip("pause algorithm");
		} else {
			play.setText("play");
			play.setToolTip("Automatically step through algorithm");
		}
		new Thread() {
			@Override
			public void run() {

				while (!isPaused && currentState.size() > 0) {

					QApplication.invokeLater(new Runnable() {
						@Override
						public void run() {
							stepForwardClicked();

							time = velocity.value() * 1000;
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
//    	inDrawMode = !inDrawMode;
//    	if (inDrawMode) {// set draw mode
//    		setDrawMode();
//    	} else {
//    		endDrawMode();
//    	}
    }

	private void setDrawMode() {
		// clear current 
		
    	// set button background color red to signal we are in draw mode
		// TODO ... 
    	// set border of main panel red to signal that we are in draw mode
    	
		
    	// set text in method field to instructions:
    	// - click to set new point
    	// - click near first point to close polygon
    	// - click [draw] to abort
    	
    	// catch points
    	
	}
	
    private void endDrawMode() {
		// TODO Auto-generated method stub

	}



	public void loadDataClicked() {

	}

	public void updateTextView(String method, String[] code, int colorID,
			int currentLine) {
		methodTitle.setText(method);
		String redSheet = new String("color: rgb(255, 0, 0);");
		String greenSheet = new String("color: rgb(35, 200, 35);");
		String blackSheet = new String("color: rgb(0, 0, 0);");

		if (code.length >= 1) {
			codeSegment.setText(new String(code[0]));
			if (currentLine == 0) {
				codeSegment.setStyleSheet(greenSheet);
			} else {
				codeSegment.setStyleSheet(blackSheet);
			}
		} else {
			codeSegment.setText("");
		}
		if (code.length >= 2) {
			codeSegment1.setText(new String(code[1]));
			if (currentLine == 1) {
				codeSegment1.setStyleSheet(greenSheet);
			} else {
				codeSegment1.setStyleSheet(blackSheet);
			}
		} else {
			codeSegment1.setText("");
		}
		if (code.length >= 3) {
			codeSegment2.setText(new String(code[2]));
			if (currentLine == 2) {
				codeSegment2.setStyleSheet(greenSheet);

			} else {
				codeSegment2.setStyleSheet(blackSheet);
			}
		} else {
			codeSegment2.setText("");
		}
		if (code.length >= 4) {
			codeSegment3.setText(new String(code[3]));
			if (currentLine == 3) {
				codeSegment3.setStyleSheet(greenSheet);
			} else {
				codeSegment3.setStyleSheet(blackSheet);
			}
		} else {
			codeSegment3.setText("");
		}
		if (code.length >= 5) {
			codeSegment4.setText(new String(code[4]));
			if (currentLine == 4) {
				codeSegment4.setStyleSheet(greenSheet);
			} else {
				codeSegment4.setStyleSheet(blackSheet);
			}
		} else {
			codeSegment4.setText("");
		}
		if (code.length >= 6) {
			codeSegment5.setText(new String(code[5]));
			if (currentLine == 5) {
				codeSegment5.setStyleSheet(greenSheet);
			} else {
				codeSegment5.setStyleSheet(blackSheet);
			}
		} else {
			codeSegment5.setText("");
		}
		if (code.length >= 7) {
			codeSegment6.setText(new String(code[6]));
			if (currentLine == 6) {
				codeSegment6.setStyleSheet(greenSheet);
			} else {
				codeSegment6.setStyleSheet(blackSheet);
			}
		} else {
			codeSegment6.setText("");
		}

	}

	private SweepLineEvent initSweepLineEvent(Vertex v) {
		return new SweepLineEvent(v, diagonals.size(), handledVertices,
				tree.getNodesForY(v.getY()), cloneHelper(activeEdges));
	}

	private AddDiagonalSubEvent addDiagonal(Vertex v, Edge edge, int methodline) {
		Edge newDiagonal = Edge.diagonalFactory(v, edge.getHelper());
		diagonals.add(newDiagonal);
		QGraphicsLineItem diagonal = new QGraphicsLineItem(newDiagonal
				.getStartVertex().getX(), newDiagonal.getStartVertex().getY(),
				newDiagonal.getEndVertex().getX(), newDiagonal.getEndVertex()
						.getY(), null, sweepLine);
		diagonal.setPen(new QPen(QColor.darkBlue));
		System.out.println("diagonal");

		AddDiagonalSubEvent addDiagonalEvent = new AddDiagonalSubEvent(2,
				newDiagonal);
		return addDiagonalEvent;
	}

	private UpdateDeletionTreeSubEvent deleteEdgeFromTree(Edge toDelete,
			int methodline) {
		Vertex oldHelper = toDelete.releaseHelper();
		tree.delete(toDelete);
		boolean b = activeEdges.remove(toDelete);
		System.out.println("active edges " + b);
		UpdateDeletionTreeSubEvent deletionEvent = new UpdateDeletionTreeSubEvent(
				methodline, tree.getNodesForY(toDelete.getEndVertex().getY()),
				toDelete, oldHelper);
		return deletionEvent;
	}

	private UpdateInsertTreeSubEvent insertEdgeInTree(Edge toInsert,
			int methodline) {
		if (toInsert == null) {
			throw new IllegalArgumentException();
		}

		tree.insert(toInsert);
		activeEdges.add(toInsert);
		toInsert.setColor(getNextColor());
		leftMarkedEdge
				.setPen(new QPen(new QColor(toInsert.getColor().getRGB())));
		leftMarkedEdge.setLine(toInsert.getStartVertex().getX(), toInsert
				.getStartVertex().getY(), toInsert.getEndVertex().getX(),
				toInsert.getEndVertex().getY());
		UpdateInsertTreeSubEvent treeUpdate = new UpdateInsertTreeSubEvent(
				methodline,
				tree.getNodesForY(toInsert.getStartVertex().getY()),
				toInsert.clone());
		return treeUpdate;
	}

	/**
	 * Returns a new color for a edge-helper pair.
	 * 
	 * @return
	 */
	private Color getNextColor() {
		return GUIColorConfiguration.getRandomColor();
	}

	private UpdateHelperSubEvent updateHelper(Edge edge, Vertex newHelper,
			int methodline) {
		Vertex oldHelper = edge.getHelper();
		edge.setHelper(newHelper);
		UpdateHelperSubEvent helperUpdate = new UpdateHelperSubEvent(2,
				newHelper.clone(), oldHelper);
		return helperUpdate;
	}

	private TreeSet<Edge> cloneHelper(TreeSet<Edge> toClone) {
		TreeSet<Edge> cloned = new TreeSet<Edge>();
		for (Edge e : toClone) {
			cloned.add(e.clone());
		}
		return cloned;
	}

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
		UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge,
				3);
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
		UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge,
				3);
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
			AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, leftOfVEdge,
					6);
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
		UpdateDeletionTreeSubEvent deletionEvent = deleteEdgeFromTree(prevEdge,
				3);
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

	YMonotonePolygonQUI app = new YMonotonePolygonQUI();
	QGraphicsView view = new QGraphicsView();
	view.setWindowTitle("Y-Monotone Polygon Visualisation");
	view.setLayout(app.mainLayout);
	view.show();
		// then line 3: insert diagonal v-helper
		if (helperIsMerge) {
			AddDiagonalSubEvent addDiagonalEvent = addDiagonal(v, leftOfVEdge,
					3);
			subEvents.add(addDiagonalEvent);
		}

		// line 4: set v as helper of this edge
		UpdateHelperSubEvent helperUpdate = updateHelper(leftOfVEdge, v, 4);
		subEvents.add(helperUpdate);

		event.setSubEvents(subEvents);
		return event;
	}

	public static void main(String[] args) {
		QApplication.initialize(args);

		YMonotonePolygonQUI app = new YMonotonePolygonQUI();
		QGraphicsView view = new QGraphicsView();
		view.setLayout(app.mainLayout);
		view.show();

		QApplication.exec();

		// TODO Auto-generated method stub

	}
}
