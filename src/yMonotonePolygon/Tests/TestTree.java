package yMonotonePolygon.Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import yMonotonePolygon.AlgorithmObjects.Edge;
import yMonotonePolygon.AlgorithmObjects.SearchTree;
import yMonotonePolygon.AlgorithmObjects.Vertex;

public class TestTree {

	@Test
	public void testInsert() {
		Vertex v1 = new Vertex(10, 10);
		Vertex v2 = new Vertex(10, 100);
		Edge e1 = new Edge(v1, v2);
		Vertex v3 = new Vertex(20, 10);
		Vertex v4 = new Vertex(20, 100);
		Edge e2 = new Edge(v3, v4);
		Vertex v5 = new Vertex(30, 10);
		Vertex v6 = new Vertex(30, 100);
		Edge e3 = new Edge(v5, v6);
		Vertex v7 = new Vertex(40, 15);
		Vertex v8 = new Vertex(40, 100);
		Edge e4 = new Edge(v7, v8);
		Vertex v9 = new Vertex(600, 15);
		Vertex v10 = new Vertex(100, 100);
		Edge e5 = new Edge(v9, v10);
		
		SearchTree tree = new SearchTree();
		
		tree.insert(e1);
		assertTrue(tree.height() == 0);
		assertTrue(tree.countLeaves() == 1);
		
		tree.insert(e3);
		assertTrue(tree.height() == 1);
		assertTrue(tree.width() == 1);
		assertTrue(tree.countLeaves() == 1);
		
		tree.insert(e2);
		assertTrue(tree.height() == 2);
		assertTrue(tree.countLeaves() == 1);
		
		tree.insert(e4);
		assertTrue(tree.height() == 2);
		assertTrue(tree.width() == 2);
		
		tree.insert(e5);
		assertTrue(tree.height() == 3);
		assertTrue(tree.countLeaves() == 2);
	}

	
	@Test
	public void testSearch() {
		Vertex v1 = new Vertex(10, 10);
		Vertex v2 = new Vertex(12, 100);
		Edge e1 = new Edge(v1, v2);
		Vertex v3 = new Vertex(20, 10);
		Vertex v4 = new Vertex(16, 100);
		Edge e2 = new Edge(v3, v4);
		Vertex v5 = new Vertex(30, 10);
		Vertex v6 = new Vertex(30, 100);
		Edge e3 = new Edge(v5, v6);
		Vertex searched1 = new Vertex(11, 40);
		Vertex searched2 = new Vertex(19, 95);
		Vertex searched22 = new Vertex(25, 85);
		Vertex searched3 = new Vertex(600, 15);
		Vertex searched0 = new Vertex(10, 99);

		SearchTree tree = new SearchTree();
		
		tree.insert(e2);
		tree.insert(e3);
		tree.insert(e1);

		assertNull(tree.searchEdge(searched0));
		assertTrue(e1.equals(tree.searchEdge(searched1)));
		assertTrue(e2.equals(tree.searchEdge(searched2)));
		assertTrue(e2.equals(tree.searchEdge(searched22)));
		assertTrue(e3.equals(tree.searchEdge(searched3)));
	}

	@Test
	public void testDelete() {
		Vertex v1 = new Vertex(10, 10);
		Vertex v2 = new Vertex(10, 100);
		Edge e1 = new Edge(v1, v2);
		Vertex v3 = new Vertex(20, 10);
		Vertex v4 = new Vertex(20, 100);
		Edge e2 = new Edge(v3, v4);
		Vertex v5 = new Vertex(30, 15);
		Vertex v6 = new Vertex(30, 100);
		Edge e3 = new Edge(v5, v6);
		Vertex v7 = new Vertex(40, 15);
		Vertex v8 = new Vertex(40, 100);
		Edge e4 = new Edge(v7, v8);
		Vertex v9 = new Vertex(600, 15);
		Vertex v10 = new Vertex(100, 100);
		Edge e5 = new Edge(v9, v10);
		
		SearchTree tree = new SearchTree();
		
		tree.insert(e2);
		tree.insert(e1);
		tree.insert(e4);
		tree.insert(e3);	
		tree.insert(e5);
		assertTrue(tree.height() == 2);
		assertTrue(tree.countLeaves() == 3);
		
		tree.delete(e5);
		tree.delete(e4);
		assertTrue(tree.height() == 1);
		assertTrue(tree.countLeaves() == 2);
		
		tree.delete(e2);
		assertTrue(tree.height() == 1);
		assertTrue(tree.countLeaves() == 1);

		
	}

}
