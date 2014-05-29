package yMonotonePolygon;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import yMonotonePolygon.GUI.PolygonDrawPanel;
import yMonotonePolygon.Tests.TestHelper;


public class YMonotonePolygonGUI extends JFrame {

	private static final long serialVersionUID = -5073162102279789347L;

	private JFrame frame;
	
	private PolygonDrawPanel polygonPanel;
	
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
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Y-Monoton Polygon Algorithm Demonstrator");
		
		frame.setBounds(100, 100, 1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// menu
		JPanel menu = new JPanel();
		initMenue(menu);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.0;
		c.gridwidth = 12;
		c.gridx = 0;
		c.gridy = 0;
		frame.add(menu, c);
		

		polygonPanel = new PolygonDrawPanel();
		polygonPanel.setP(TestHelper.readTestPolygon("testDiamond"));
		//c.ipady = 600;      //make this component tall
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.75;
		c.weighty = 1;
		c.gridwidth = 9;
		c.gridx = 0;
		c.gridy = 1;
		frame.add(polygonPanel, c);
		
		JPanel east = new JPanel(new GridLayout(2,1));
		initEast(east);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.25;
		c.gridwidth = 3;
		c.gridx = 9;
		c.gridy = 1;
		frame.add(east, c);		
	}

	private void initEast(JPanel east) {
		JPanel dataStructure = new JPanel();	
		JLabel lblDataStructure = new JLabel("data structure");
		dataStructure.add(lblDataStructure);
		
		JPanel code = new JPanel();	
		JLabel lblCode = new JLabel("code");
		code.add(lblCode);
		
		east.add(dataStructure);
		east.add(code);		
		//east.setSize(200, 600);
	}

	private void initMenue(JPanel menu) {	
		// loading input
		JLabel lblInput = new JLabel("Input:");
		menu.add(lblInput);
		
		JButton btnLoad = new JButton("Load");
		menu.add(btnLoad);
		
		JButton btnDraw = new JButton("Draw");
		menu.add(btnDraw);
		
		
		// handling flow
		JLabel lblNavigation = new JLabel("Navigation:");
		menu.add(lblNavigation);
		
		JButton btnPlayEtc = new JButton("Play etc.");
		menu.add(btnPlayEtc);
		
		JSlider slider = new JSlider();
		menu.add(slider);
	}

}
