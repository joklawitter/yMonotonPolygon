package yMonotonePolygon.QUI;


import yMonotonePolygon.PraeComputation.PraeComputer;

import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QButtonGroup;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsPolygonItem;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsView;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPen;
import com.trolltech.qt.gui.QPlainTextEdit;
import com.trolltech.qt.gui.QPolygonF;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSlider;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QGraphicsItem.GraphicsItemFlag;

public class YMonotonePolygonQUI extends QWidget {
    public QGraphicsScene sweepLine;
    public QGraphicsScene treeDataStructure;
    public QPlainTextEdit codeSegment;
    public QButtonGroup algorithmController;
    public QPushButton stepBack;
    public QPushButton stepForward;
    public QPushButton lineUp;
    public QPushButton lineDown;
    public QPushButton skipBack;
    public QPushButton skipForward;
    public QPushButton play;
    public QPushButton pause;
    public QSlider velocity;

    public QLabel methodTitle;
    public QLabel treeTitle;
    public QPushButton editBtn;
    public QPushButton loadData;
    public QButtonGroup editButtonGroup;

    public QGridLayout mainLayout;
    
    public PraeComputer praeComputer;
    private QPolygonF p;



    public YMonotonePolygonQUI() {
	sweepLine = new QGraphicsScene(new QRectF(0, 0, 500, 200));
	sweepLine.setBackgroundBrush(new QBrush(QColor.white));
	treeDataStructure = new QGraphicsScene(new QRectF(85, 10, 50, 20));
	treeDataStructure.setBackgroundBrush(new QBrush(QColor.green));
	codeSegment = new QPlainTextEdit();
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
	pause = new QPushButton("||");
	pause.setToolTip("pause Algorithm");
	pause.clicked.connect(this, "pauseClicked()");
	algorithmController.addButton(lineUp);
	algorithmController.addButton(skipBack);
	algorithmController.addButton(stepBack);
	algorithmController.addButton(pause);
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

	editButtonGroup = new QButtonGroup();
	editButtonGroup.addButton(editBtn);
	editButtonGroup.addButton(loadData);

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
	points2.add(100, 50);
	points2.add(140, 277);
	points2.add(100, 320);
	points2.add(200, 330);
	points2.add(400, 320);
	points2.add(200, 30);
	praeComputer = new PraeComputer();
	QGraphicsPolygonItem g = new QGraphicsPolygonItem(points2, null, sweepLine);
	g.setPen(new QPen(QColor.red));
	g.setBrush(new QBrush(QColor.yellow));
	g.setFlag(GraphicsItemFlag.ItemIsMovable, true);
	g.setFlag(GraphicsItemFlag.ItemStacksBehindParent, false);
	praeComputer.work(points2);
    }

    public void layoutView() {
	mainLayout = new QGridLayout(this);
	QGridLayout editButtonLayout = new QGridLayout();
	editButtonLayout.addWidget(editBtn, 0, 0);
	editButtonLayout.addWidget(loadData, 0, 1);
	editButtonLayout.addWidget(velocity, 0, 2);
	QGridLayout buttonLayout = new QGridLayout();
	buttonLayout.addWidget(lineUp, 0, 0);
	buttonLayout.addWidget(skipBack, 0, 1);
	buttonLayout.addWidget(stepBack, 0, 2);
	buttonLayout.addWidget(pause, 0, 3);
	buttonLayout.addWidget(play, 0, 4);
	buttonLayout.addWidget(stepForward, 0, 5);
	buttonLayout.addWidget(skipForward, 0, 6);
	buttonLayout.addWidget(lineDown, 0, 7);
	mainLayout.addLayout(buttonLayout, 0, 0);
	mainLayout.addLayout(editButtonLayout, 0, 1);
	QGraphicsView sweepLineView = new QGraphicsView(sweepLine);
	mainLayout.addWidget(sweepLineView, 1, 0);
	QGraphicsView treeView = new QGraphicsView(treeDataStructure);
	mainLayout.addWidget(treeView, 2, 0);
	mainLayout.addWidget(methodTitle, 3, 0);
	QGraphicsView codeSegmentView = new QGraphicsView(codeSegment);
	mainLayout.addWidget(codeSegmentView, 4, 0);
	

    }

    public void lineUpClicked() {

    }

    public void lineDownClicked() {
    }

    public void skipBackClicked() {
    }

    public void skipForwardClicked() {
    }

    public void stepBackClicked() {
    }

    public void stepForwardClicked() {
    }

    public void playClicked() {

    }

    public void pauseClicked() {

    }

    public void editBtnClicked() {

    }

    public void loadDataClicked() {

    }
    
    
    public void updateTextView(String method, String[] code, int colorID, int currentLine) {
	methodTitle.setText(method);
	String tmp = new String(code[0]);
	
	for (int i = 1; i < code.length; i++) {
	    tmp.concat("\n"+code[i]);
	}
	
	codeSegment.setPlainText(tmp);
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
