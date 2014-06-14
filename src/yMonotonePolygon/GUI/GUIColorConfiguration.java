package yMonotonePolygon.GUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public final class GUIColorConfiguration {

	public static final Color EDGE_STD_COLOR = Color.BLACK;
	
	public static final Color VERTEX_STD_COLOR = Color.BLACK;
	
	public static final Color POLYGON_BACKGROUND = Color.GRAY;
	
	public static final Color POLYGON_INSIDE = Color.WHITE;
	
	public static final Color DIAGONAL = Color.BLUE;
	
	public static final Color NEW_DIAGONAL = Color.RED;
	
	public static final Color HANDLED_EVENT = Color.DARK_GRAY;
	
	public static final Color CURRENT_EVENT = Color.RED;
	
	public static final Color UNHANDLED_EVENT = Color.BLACK;
	
	public static final Color SWEEP_LINE = Color.RED; // TODO: or something else + dotted/dashed?
	
	public static final Color DATASTRUCTURE_BACKGROUND = Color.GRAY;

	// colors for lines in method
	public static final Color TRUE_LINE = Color.GREEN;
	public static final Color FALSE_LINE = Color.RED;
	public static final Color HIGHLIGHTED_LINE = Color.GRAY;
	
	
	
	public static final ArrayList<Color> COLORS;
	
	static {
		COLORS = new ArrayList<Color>();
		COLORS.add(Color.PINK);
		COLORS.add(Color.GREEN);
		COLORS.add(Color.MAGENTA);
		COLORS.add(Color.CYAN);
		COLORS.add(new Color(176, 125, 55)); // brown
		COLORS.add(new Color(53, 133, 64)); // dark green
		COLORS.add(new Color(125, 255, 142)); // other green
		COLORS.add(new Color(172, 240, 0)); // other green
		COLORS.add(new Color(179, 255, 48)); // other green
		COLORS.add(new Color(78, 230, 136)); // other green
		COLORS.add(new Color(41, 191, 255)); // other blue
		COLORS.add(new Color(58, 201, 201)); // other blue
		COLORS.add(new Color(255, 191, 0)); // gold
		COLORS.add(new Color(132, 0, 255)); // lila
		
	}
	
	public static Color getRandomColor() {
		Random random = new Random();

		int i = Math.abs(random.nextInt()) % COLORS.size();

		return COLORS.get(i);
	}
}
