package yMonotonPolygon.GUI;


import com.trolltech.qt.core.QRectF;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsView;
import com.trolltech.qt.gui.QPushButton;

public class YMonotonePolygonQUI extends QDialog {
    public QGraphicsScene scene;
    
    
    public YMonotonePolygonQUI () {
	scene = new QGraphicsScene(new QRectF(0, 0, 600, 600));
	scene.setBackgroundBrush(new QBrush(QColor.white));
    }
    public static void main(String[] args) {
	QApplication.initialize(args);
	
	QPushButton push = new QPushButton("Hello World");
//	push.show();
	YMonotonePolygonQUI app = new YMonotonePolygonQUI();
	QGraphicsView view = new QGraphicsView(app.scene);
	view.show();
	
	QApplication.exec();
	
	// TODO Auto-generated method stub
	
}
}
