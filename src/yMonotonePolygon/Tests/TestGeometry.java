package yMonotonePolygon.Tests;

import static org.junit.Assert.*;

import java.awt.Polygon;

import org.junit.Test;

import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.Vertex;
import yMonotonePolygon.PraeComputation.Geometry;
import yMonotonePolygon.PraeComputation.IllegalPolygonException;
import yMonotonePolygon.PraeComputation.PraeComputer;
import yMonotonePolygon.PraeComputation.Reader;

public class TestGeometry {

	@Test
	public void testLiesLeftOfLine() {
		Vertex up = new Vertex(100, 10);
		Vertex down = new Vertex(100, 120);
		Edge e = new Edge(up, down);
		Vertex left = new Vertex(120, 50);
		Vertex left2 = new Vertex(300, 130);
		Vertex left3 = new Vertex(120, 10);
		Vertex left4 = new Vertex(120, 120);
		Vertex left5 = new Vertex(200, 5);
		Vertex right = new Vertex(10, 55);
		assertTrue(Geometry.liesLeftOfLine(e, left));
		assertTrue(Geometry.liesLeftOfLine(e, left2));
		assertTrue(Geometry.liesLeftOfLine(e, left3));
		assertTrue(Geometry.liesLeftOfLine(e, left4));
		assertFalse(Geometry.liesLeftOfLine(e, right));
		
		Edge f = new Edge(down, up);
		assertFalse(Geometry.liesLeftOfLine(f, left));
		assertFalse(Geometry.liesLeftOfLine(f, left2));
		assertFalse(Geometry.liesLeftOfLine(f, left3));
		assertFalse(Geometry.liesLeftOfLine(f, left4));
		assertTrue(Geometry.liesLeftOfLine(f, right));
		
		Edge g = new Edge(up, left3);
		assertFalse(Geometry.liesLeftOfLine(g, left));
		assertFalse(Geometry.liesLeftOfLine(g, left2));
		assertFalse(Geometry.liesLeftOfLine(g, up));
		assertFalse(Geometry.liesLeftOfLine(g, left4));
		assertTrue(Geometry.liesLeftOfLine(g, left5));
	}
	
	@Test
	public void testLowerThan() {
		Vertex up = new Vertex(100, 10);
		Vertex down = new Vertex(100, 120);
		Vertex downRight = new Vertex(100, 140);
		
		assertTrue(Geometry.isLowerThan(down, up));
		assertTrue(Geometry.isLowerThan(downRight, up));
		assertTrue(Geometry.isLowerThan(downRight, down));
	}
	
	@Test
	public void testComputeXOfEdgeAtY() {
		Vertex first = new Vertex(100, 0);
		Vertex second = new Vertex(100, 100);
		Edge e = new Edge(first, second);
		
		assertTrue(Geometry.computeXOfEdgeAtY(e, 50) == 100);
	}

	@Test (expected = AssertionError.class)
	public void testComputeXOfEdgeAtYException() {
		Vertex first = new Vertex(100, 0);
		Vertex second = new Vertex(100, 100);
		Edge e = new Edge(first, second);
		
		assertTrue(Geometry.computeXOfEdgeAtY(e, 50) == 101);
	}
	
	@Test
	public void testOrientationCheck() throws IllegalPolygonException {
		Polygon p = Reader.readTestPolygon("testWrongOrientation");
		PraeComputer pc = new PraeComputer();
		pc.work(p);
		assertTrue(Geometry.checkPolygonOrientation(pc.getVertices()));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testNotSimplePolygon() throws IllegalPolygonException {
		Polygon p = Reader.readTestPolygon("testNonSimplePolygon");
		PraeComputer pc = new PraeComputer();
		pc.work(p);
	}
}
