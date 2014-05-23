package yMonotonPolygon.PraeComputation;

import java.awt.Polygon;
import java.util.LinkedList;

import yMonotonPolygon.AlgorithmObjects.Line;
import yMonotonPolygon.AlgorithmObjects.SweepLineEvent;

public class PraeComputer {
	private Polygon p;
	
	private LinkedList<SweepLineEvent> history;
	
	private LinkedList<Line> diagonals;
	
	/**
	 * Does the prae-computation. 
	 * Includes computing the whole history and diagonals to add.
	 * @param p Polygon on which to work
	 * @return whether it was successful
	 */
	public boolean work(Polygon p) {
		// TODO
		
		return false;
	}

	public Polygon getP() {
		return p;
	}

	public LinkedList<SweepLineEvent> getHistory() {
		return history;
	}

	public LinkedList<Line> getDiagonals() {
		return diagonals;
	}
	
}
