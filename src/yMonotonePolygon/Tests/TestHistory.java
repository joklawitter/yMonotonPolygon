package yMonotonePolygon.Tests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import yMonotonePolygon.AlgorithmObjects.AddDiagonalSubEvent;
import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.SubEvent;
import yMonotonePolygon.AlgorithmObjects.SweepLineEvent;
import yMonotonePolygon.AlgorithmObjects.Vertex;
import yMonotonePolygon.PraeComputation.IllegalPolygonException;
import yMonotonePolygon.PraeComputation.PraeComputer;

public class TestHistory {

	PraeComputer pc;
	
	@Before
	public void init() throws IllegalPolygonException {
		pc = new PraeComputer();
		pc.work(TestHelper.readTestPolygon("example2"));
	}
	
	@Test
	public void testHistoryLength() {
		assertTrue(pc.getVertices().size() == pc.getHistory().size());
	}
	
	@Test
	public void testHistoryOrder() {
		LinkedList<SweepLineEvent> history = pc.getHistory();
		TreeSet<Vertex> vertices = pc.getVertices();
		
		int i = 0;
		for (Vertex v : vertices) {
			assertTrue((v.getX() == history.get(i).getVertex().getX()) && (v.getY() == history.get(i).getVertex().getY()));
			assertTrue((v.getY() == history.get(i).getYOfSweepLine()));
			assertTrue(history.get(i).getNumberOfHandledVertices() == i);
			i++;
		}
	}
	
	@Test
	public void testAddDiagonalSubEvents() {
		LinkedList<SweepLineEvent> history = pc.getHistory();
		LinkedList<Edge> diagonals = pc.getDiagonals();
		
		int i = 0;
		for (SweepLineEvent s : history) {
			LinkedList<SubEvent> subEvents = s.getSubEvents();
			for (SubEvent se : subEvents) {
				if (se instanceof AddDiagonalSubEvent) {
					se = (AddDiagonalSubEvent) se;
					assertTrue(diagonals.contains(((AddDiagonalSubEvent) se).getDiagonal()));
					i++;
				}
			}
		}
		
		assertTrue(i == diagonals.size());
	}
	
	@Test
	public void testNumberOfActiveEdges() {
		LinkedList<SweepLineEvent> history = pc.getHistory();
		
		int i = 0;
		for (SweepLineEvent s : history) {
			int j = s.getActiveEdges().size();
			assertTrue(Math.abs(j - i) <= 1); // may not vary more than 1 per step
			i = j;
		}
		
		assertTrue(i <= 1); // at the end one left (i does not get reset)
	}
	
	@Test
	public void testTree() {
		LinkedList<SweepLineEvent> history = pc.getHistory();
		
		int i = 0;
		for (SweepLineEvent s : history) {
			int j = s.getVertexSetOfTree().size();
			assertTrue(Math.abs(j - i) <= 1); // may not vary more than 1 per step
			assertNotNull(s.getVertexSetOfTree()); 
			i = j;
		}
	}
}
