package yMonotonePolygon.Tests;

import static org.junit.Assert.*;

import java.awt.Polygon;

import org.junit.Test;

import yMonotonePolygon.AlgorithmObjects.Vertex;
import yMonotonePolygon.PraeComputation.IllegalPolygonException;
import yMonotonePolygon.PraeComputation.PraeComputer;
import yMonotonePolygon.PraeComputation.Reader;

public class TestPraeComputation {

	@Test
	public void readTest() throws IllegalPolygonException {
		Polygon p = Reader.readTestPolygon("testDiamond");
		PraeComputer pc = new PraeComputer();
		assertTrue(pc.work(p));
		
		p = Reader.readTestPolygon("testSquare");
		assertTrue(pc.work(p));
	}
	
	@Test
	public void printTest() throws IllegalPolygonException {
		System.out.println(">> Read test:");

		Polygon p = Reader.readTestPolygon("testDiamond");
		PraeComputer pc = new PraeComputer();
		pc.work(p);

		System.out.println(pc.toString());

		Vertex v = pc.getVertices().first();
		assertTrue(v.toString().equalsIgnoreCase("StartVertex (" + 150 + ", " + 100 + ")"));
	}
	
	@Test
	public void oneMergeTest() throws IllegalPolygonException {
		System.out.println(">> Merge test:");
		Polygon p = Reader.readTestPolygon("testOneMerge");
		PraeComputer pc = new PraeComputer();
		pc.work(p);
		System.out.println(pc.toString());
		for (Vertex x : pc.getVertices()) {
			System.out.println(x.toString());
		}
		
		assertTrue(pc.getDiagonals().size() == 1);
	}
	
	@Test
	public void oneSplitTest() throws IllegalPolygonException {
		System.out.println(">> Split test:");
		Polygon p = Reader.readTestPolygon("testOneSplit");
		PraeComputer pc = new PraeComputer();
		pc.work(p);
		System.out.println(pc.toString());
		for (Vertex x : pc.getVertices()) {
			System.out.println(x.toString());
		}
		
		assertTrue(pc.getDiagonals().size() == 1);
	}
	
	@Test
	public void twoSplitTwoMergeTest() throws IllegalPolygonException {
		System.out.println(">> 2-2 test:");
		Polygon p = Reader.readTestPolygon("example2");
		PraeComputer pc = new PraeComputer();
		pc.work(p);
		System.out.println(pc.toString());
		for (Vertex x : pc.getVertices()) {
			System.out.println(x.toString());
		}
		
		assertTrue(pc.getDiagonals().size() == 2);
	}
	
	

}
