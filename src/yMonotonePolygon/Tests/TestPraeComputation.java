package yMonotonePolygon.Tests;

import static org.junit.Assert.*;

import java.awt.Polygon;

import org.junit.Test;

import yMonotonePolygon.AlgorithmObjects.Vertex;
import yMonotonePolygon.PraeComputation.PraeComputer;

public class TestPraeComputation {

	@Test
	public void readTest() {
		Polygon p = TestHelper.readTestPolygon("testDiamond");
		PraeComputer pc = new PraeComputer();
		assertTrue(pc.work(p));
		
		p = TestHelper.readTestPolygon("testSquare");
		assertTrue(pc.work(p));
	}
	
	@Test
	public void printTest() {
		System.out.println(">> Read test:");

		Polygon p = TestHelper.readTestPolygon("testDiamond");
		PraeComputer pc = new PraeComputer();
		pc.work(p);

		System.out.println(pc.toString());

		Vertex v = pc.getVertices().first();
		assertTrue(v.toString().equalsIgnoreCase("StartVertex (" + 150 + ", " + 100 + ")"));
	}
	
	@Test
	public void oneMergeTest() {
		System.out.println(">> Merge test:");
		Polygon p = TestHelper.readTestPolygon("testOneMerge");
		PraeComputer pc = new PraeComputer();
		pc.work(p);
		System.out.println(pc.toString());
		for (Vertex x : pc.getVertices()) {
			System.out.println(x.toString());
		}
		
		assertTrue(pc.getDiagonals().size() == 1);
	}
	
	@Test
	public void oneSplitTest() {
		System.out.println(">> Split test:");
		Polygon p = TestHelper.readTestPolygon("testOneSplit");
		PraeComputer pc = new PraeComputer();
		pc.work(p);
		System.out.println(pc.toString());
		for (Vertex x : pc.getVertices()) {
			System.out.println(x.toString());
		}
		
		assertTrue(pc.getDiagonals().size() == 1);
	}
	
	@Test
	public void twoSplitTwoMergeTest() {
		System.out.println(">> 2-2 test:");
		Polygon p = TestHelper.readTestPolygon("example2");
		PraeComputer pc = new PraeComputer();
		pc.work(p);
		System.out.println(pc.toString());
		for (Vertex x : pc.getVertices()) {
			System.out.println(x.toString());
		}
		
		assertTrue(pc.getDiagonals().size() == 2);
	}
	
	

}
