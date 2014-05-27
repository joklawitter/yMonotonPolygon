package yMonotonePolygon.Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.Vertex;
import yMonotonePolygon.AlgorithmObjects.VertexType;

public class TestVertex {

	@Test
	public void testStartVertex() {
		Vertex prev = new Vertex(200, 100);
		Vertex toTest = new Vertex(100, 50);
		Vertex next = new Vertex(100, 100);
		
		Edge e = new Edge(prev, toTest);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		toTest.setPrevEdge(e);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.START);
	}
	
	@Test
	public void testStartVertex2() {
		Vertex prev = new Vertex(200, 50);
		Vertex toTest = new Vertex(100, 50);
		Vertex next = new Vertex(200, 100);
		
		Edge e = new Edge(prev, toTest);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		toTest.setPrevEdge(e);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.START);
	}
	
	@Test
	public void testSplitVertex() {
		Vertex next = new Vertex(200, 100);
		Vertex toTest = new Vertex(100, 50);
		Vertex prev = new Vertex(100, 100);
		
		Edge e = new Edge(prev, toTest);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		toTest.setPrevEdge(e);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.SPLIT);
	}
	
	@Test
	public void testSplitVertex2() {
		Vertex next = new Vertex(200, 50);
		Vertex toTest = new Vertex(100, 50);
		Vertex prev = new Vertex(200, 100);
		
		Edge e = new Edge(prev, toTest);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		toTest.setPrevEdge(e);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.SPLIT);
	}
	
	@Test
	public void testEndVertex() {
		Vertex prev = new Vertex(200, 100);
		Vertex toTest = new Vertex(100, 150);
		Vertex next = new Vertex(300, 100);
		
		Edge e = new Edge(prev, toTest);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		toTest.setPrevEdge(e);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.END);
	}
	
	@Test
	public void testEndVertex2() {
		Vertex prev = new Vertex(50, 100);
		Vertex toTest = new Vertex(100, 100);
		Vertex next = new Vertex(100, 50);
		
		Edge e = new Edge(prev, toTest);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		toTest.setPrevEdge(e);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.END);
	}
	
	@Test
	public void testMergeVertex() {
		Vertex next = new Vertex(200, 100);
		Vertex toTest = new Vertex(100, 150);
		Vertex prev = new Vertex(300, 100);
		
		Edge e = new Edge(prev, toTest);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		toTest.setPrevEdge(e);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.MERGE);
	}
	
	@Test
	public void testMergeVertex2() {
		Vertex next = new Vertex(50, 150);
		Vertex toTest = new Vertex(100, 150);
		Vertex prev = new Vertex(100, 100);
		
		Edge e = new Edge(prev, toTest);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		toTest.setPrevEdge(e);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.MERGE);
	}
	
	@Test
	public void testLeftRegularVertex() {
		Vertex prev = new Vertex(150, 100);
		Vertex toTest = new Vertex(100, 150);
		Vertex next = new Vertex(150, 200);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.REGULAR_LEFT);
	}

	@Test
	public void testLeftRegularVertex2() {
		Vertex prev = new Vertex(10, 10);
		Vertex toTest = new Vertex(100, 10);
		Vertex next = new Vertex(109, 10);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.REGULAR_LEFT);
	}
	
	@Test
	public void testLeftRegularVertex3() {
		Vertex prev = new Vertex(100, 5);
		Vertex toTest = new Vertex(100, 10);
		Vertex next = new Vertex(90, 10);
		Edge e = new Edge(prev, toTest);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		toTest.setPrevEdge(e);
		
		toTest.computeVertexType();
		assertFalse(toTest.getVertexType() == VertexType.REGULAR_LEFT);
	}
	
	
	@Test
	public void testRightRegularVertex() {
		Vertex next = new Vertex(102, 0);
		Vertex toTest = new Vertex(100, 50);
		Vertex prev = new Vertex(102, 100);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.REGULAR_RIGHT);
	}
	

	@Test
	public void testRightRegularVertex2() {
		Vertex next = new Vertex(10, 10);
		Vertex toTest = new Vertex(100, 10);
		Vertex prev = new Vertex(111, 10);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		
		toTest.computeVertexType();
		assertTrue(toTest.getVertexType() == VertexType.REGULAR_RIGHT);
	}
	
	
	@Test
	public void testRightRegularVertex3() {
		Vertex next = new Vertex(100, 5);
		Vertex toTest = new Vertex(100, 10);
		Vertex prev = new Vertex(90, 10);
		Edge e = new Edge(prev, toTest);
		
		toTest.setPrev(prev);
		toTest.setNext(next);
		toTest.setPrevEdge(e);
		
		toTest.computeVertexType();
		assertFalse(toTest.getVertexType() == VertexType.REGULAR_RIGHT);
	}


}
