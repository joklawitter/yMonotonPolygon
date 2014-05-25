package yMonotonePolygon.AlgorithmObjects;

import yMonotonePolygon.PraeComputation.Geometry;


/**
 * BST for the sweep line data structure.<br>
 * Contains all edges which cross sweep line and point down,
 * (this means inside of polygon lies right of them (left from their own pov)).
 * 
 * You can search for given x and y for the edge at this y, which lies directly left of x.
 * 
 * @author JoKlawitter, inspired by BST of V.S.Adamchik 2010
 *
 */
public class SearchTree {
   private Node root;

   public SearchTree()  {
      root = null;
   }

   /*private int compareAtY(Node one, Node two, int y) {
      return compareAtY(one.getData(), two.getData(), y);
   }*/
   private int compareAtY(Edge one, Edge two, int y) {
	   return Integer.compare(Geometry.computeXOfEdgeAtY(one, y), 
				 			  Geometry.computeXOfEdgeAtY(two, y)); 
   }

   // insert
   public void insert(Edge data, int y) {
      root = insert(root, data, y);
   }
   
   private Node insert(Node p, Edge toInsert, int y) {
      if (p == null) {
         return new Node(toInsert);
      }
      
      if (compareAtY(toInsert, p.getData(), y) == 0)
      	return p;

      if (compareAtY(toInsert, p.getData(), y) < 0) {
    	  p.left = insert(p.left, toInsert, y);  	  
      } else {
    	  p.right = insert(p.right, toInsert, y);  	  
      }

      return p;
   }

   // searching at position x for the next edge to the left
   public Edge searchEdgeAtX(int x, int y) {
      return searchEdgeAtX(root, x, y);
   }
   
   private Edge searchEdgeAtX(Node p, int x, int y) {   
      if (p == null) { // on a search to the right, we did not found something left of x
    	  return null;
      }
	  
      // this node to much to the right?
	  if (Geometry.computeXOfEdgeAtY(p.getData(), y) > x) { // x lies to the left of this node
    	  return searchEdgeAtX(p.left, x, y);
      } 
      
      // then search in right subtree
      Edge rightSearch = searchEdgeAtX(p.right, x, y);
      if (rightSearch == null) { // if there was none more to the right but left of x
    	  return p.getData();    // this is the searched edge
      }
      
      return rightSearch;
   }

   // deleting
   public void delete(Edge toDelete) {
	   if (toDelete == null) {
		   throw new IllegalArgumentException();
	   }
	   
       root = delete(root, toDelete, toDelete.getEndVertex().getY());
   }
   
   private Node delete(Node p, Edge toDelete, int y) {
	  if (p == null) {
		  throw new AssertionError("No edge to delete found.");
	  }
	  
	  if (p.getData().equals(toDelete)) {
		  if (p.left == null) {
	        	 return p.right; // right child will take place of node p
	         } else if (p.right == null) {
	        	 return p.left; // left child will take place of node p
	         } else { // we get rightmost/leftmost node in the left/right subtree
	        	 
	        	 if (height(p.left) >= height(p.right)) {
	        		// replace data of this with rightmost child in left subtree and delete that one 
	 	            p.setData(retrieveDataRight(p.left));
	 	            p.left =  delete(p.left, p.getData(), y) ;
	        	 } else {
	        		// replace data of this with leftmost child in right subtree and delete that one
		 	        p.setData(retrieveDataLeft(p.right));
		 	        p.right =  delete(p.right, p.getData(), y) ;
	        	 }
	         }
	  }
	  
      if (compareAtY(toDelete, p.getData(), y) < 0) {
    	  p.left = delete(p.left, toDelete, y);
      } else if (compareAtY(toDelete, p.getData(), y)  > 0) { 	  
    	  p.right = delete(p.right, toDelete, y);
      } 
      
      return p;
   }
   
   private Edge retrieveDataRight(Node p) {
      while (p.right != null) {
    	  p = p.right;
      }

      return p.getData();
   }
   
   private Edge retrieveDataLeft(Node p) {
	      while (p.left != null) {
	    	  p = p.left;
	      }

	      return p.getData();
	   }

   // clone
   public SearchTree clone() {
	   SearchTree twin = new SearchTree();

      twin.root = cloneHelper(root);
      
      return twin;
   }
   
   private Node cloneHelper(Node p) {
      if(p == null)
         return null;
      else
         return new Node(p.getData().clone(), cloneHelper(p.left), cloneHelper(p.right));
   }

   // misc
   public int height() {
      return height(root);
   }
   
   private int height(Node p) {
      if(p == null) {
    	  return -1;
      } else { 
    	  return 1 + Math.max(height(p.left), height(p.right));
      }
   }

   public int countLeaves()  {
      return countLeaves(root);
   }
   
   private int countLeaves(Node p) {
      if(p == null) {
    	  return 0;
      } else if (p.left == null && p.right == null) {
    	  return 1;
      } else {
    	  return countLeaves(p.left) + countLeaves(p.right);
      }
   }

   public int width() {
      int max = 0;
      
      for(int i = 0; i <= height(); i++) {
         int tmp = width(root, i);
         if (tmp > max) {
        	 max = tmp;
         }
      }
      
      return max;
   }
   
   // returns the number of nodes on a given level
   public int width(Node p, int depth) {
      if (p==null) {
    	  return 0;
      } else if (depth == 0) {
    	  return 1;
      } else {
    	  return width(p.left, depth - 1) + width(p.right, depth - 1);
      }
   }

   // nested node class
   private class Node {
      private Edge e;
      private Node left;
      private Node right;

      public Node(Edge data) {
          this(data, null, null);
       }
      
      public Node(Edge data, Node l, Node r) {
         this.left = l; 
         this.right = r;
         this.e = data;
      }

      public String toString() {
         return e.toString();
      }
      
      public Edge getData() {
    	  return e;
      }
      
      public void setData(Edge e) {
    	  this.e = e;
      }
   } 
}

