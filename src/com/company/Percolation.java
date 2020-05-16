package com.company;

public class Percolation
{
   private int[] parent;  // parent[i] = parent of i
   private int[] size;    // size[i] = number of sites in tree rooted at i
   private int numOpenSites = 0;
   private int[] state; //-1 = closed, 0 = open, 5 = full
   private int count;     // number of components
   private int numTrees;
   private boolean percolates = false; //is the top connected to the bottom?
   private int rowLength; // width of grid
   private int totalLength; //size of array plus one top and one bottom, ex. 10*10 grid = size 102 array

   /**
    * Initializes an empty union-find data structure with
    * {@code n} elements {@code 0} through {@code n-1}.
    * Initially, each elements is in its own set.
    *
    * @param n the number of elements
    * @throws IllegalArgumentException if {@code n < 0}
    */
   public Percolation(int n)
   {

      count = n * n; //size of grid
      numTrees = count;
      rowLength = n; //size of one row or column
      totalLength = n * n + 2; // number of nodes, add one for top and one for bottom
      parent = new int[totalLength]; //stores the parent of an index at the index, if parent[5] stores 10, 10 is the parent of parent[5]
      size = new int[totalLength];// # of children a node has, parent[0] node will always be the parent even of other larger trees
      state = new int[totalLength]; //stores whether node is closed, open, full (-1, 0, 5)

      for (int i = 0; i < totalLength; i++)
      {
         parent[i] = i; // each node starts as parent of itself
         size[i] = 1; // each node starts as its own tree, size one
         state[i] = -1;// each node starts out closed
      }
      state[0] = 0;//Top node outside of n*n grid attached to surface always open
      state[totalLength - 1] = 0; // bottom node outside of n*n grid attached to surface always open
   }                                                                            //            0
   //                                                                                     -1 -1 -1
   //                                                                                     -1 -1 -1
   //                                                                                     -1 -1 -1
   //                                                                                         0

   /**
    * Returns the number of sets.
    *
    * @return the number of sets (between {@code 1} and {@code n})
    */
   public int count()
   {
      return numTrees;
   }

   /**
    * @param n node to be opened
    */
   public void open(int n)
   {
      //opens a node unless already opened and does not already percolate
      //then checks neighbors and connects then if open or fills itself if neighbors are full
      if (n > 0 && n < totalLength - 2 && !percolates)//
      {
         if (state[n] == -1)
         {
            numOpenSites++;
            state[n] = n;

            if (n < rowLength + 1) union(0, n);
            if (n > totalLength - rowLength - 2) union(n, totalLength - 1);

            connectRelative(n);

            if (find(totalLength - 1) == 0) percolates = true;

            //-------------------------------------
            //below just prints grid each time a node opens to track accuracy and see how it works/debugging
            // sometimes the yellow highlighted last node opened highlights too many nodes but dont care to fix it now
            if (rowLength < 11 && !percolates)
            {
               int count = 0;
               int row = rowLength;
               System.out.println("New Node Opened: " + n);
               for (int x = 1; x < state.length - 1; x++)
               {
                  if (count == row)
                  {
                     System.out.println("");
                     row += rowLength;
                  }
                  count++;
                  int y = find(x);
                  if (x == n)
                  {
                     if (y == 0) System.out.print("\u001B[33m " + 5 + "  \u001B[0m");
                     else
                     {
                        y = state[x];
                        if (y != -1) System.out.print("\u001B[33m " + 0 + "  ");
                        else System.out.print("\u001B[33m" + y + "  \u001B[0m");
                     }
                  } else
                  {
                     if (y == 0) System.out.print("\u001B[34m " + 5 + "  \u001B[0m");
                     else
                     {
                        y = state[x];
                        if (y != -1) System.out.print(" " + 0 + "  ");
                        else System.out.print("\u001B[31m" + y + "  \u001B[0m");
                     }
                  }
               }
               System.out.println("\n");
               //------------------------------------------------
            }
         }
      }
   }

   /**
    * Merges the set containing element {@code p} with the
    * the set containing element {@code q}.
    *
    * @param p one element
    * @param q the other element
    * @throws IllegalArgumentException unless
    *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
    */
   public void union(int p, int q)
   {
      int rootP = find(p);
      int rootQ = find(q);
      if (rootP == rootQ) return;

      // always make 0 the parent of all other nodes and subtrees
      if (rootP == 0)
      {
         parent[rootQ] = rootP;
         size[rootP] += size[rootQ];
      } else if (rootQ == 0)
      {
         parent[rootP] = rootQ;
         size[rootQ] += size[rootP];
      }
      // otherwise make smaller tree root point to larger one
      else if (size[rootP] < size[rootQ])
      {
         parent[rootP] = rootQ;
         size[rootQ] += size[rootP];
      } else
      {
         parent[rootQ] = rootP;
         size[rootP] += size[rootQ];
      }
      numTrees--;
   }

   /**
    * check all relatives of node n to see if they can be connected
    * @param n node in array
    */
   public void connectRelative(int n)
   {
      if (n > rowLength && state[n - rowLength] != -1)//parent
         union(n, n - rowLength);
      if (n < (totalLength - rowLength - 1) && state[n + rowLength] != -1) // child
         union(n, n + rowLength);
      if (n % rowLength != 1 && state[n - 1] != -1) // left sibling
         union(n, n - 1);
      if (n % rowLength > 0 && state[n + 1] != -1) // right sibling
         union(n, n + 1);
   }

   /**
    * Returns the canonical element/rootnode/parent of the set/tree containing element {@code p}.
    *
    * @param p an element
    * @return the canonical element/parent of the set containing {@code p}
    * @throws IllegalArgumentException unless {@code 0 <= p < n}
    */
   public int find(int p)
   {
      validate(p);
      int root = p;
      while (root != parent[root]) root = parent[root];
      while (p != root)
      {
         int newp = parent[p];
         parent[p] = root;
         p = newp;
      }
      return root;
   }

   // validate that p is a valid index
   private void validate(int p)
   {
      int n = parent.length;
      if (p < 0 || p >= n)
      {
         throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
      }
   }

   /**
    * Returns true if the two elements are in the same set.
    *
    * @param p one element
    * @param q the other element
    * @return {@code true} if {@code p} and {@code q} are in the same set;
    * {@code false} otherwise
    * @throws IllegalArgumentException unless
    *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
    * @deprecated Replace with two calls to {@link #find(int)}.
    */
   @Deprecated
   public boolean connected(int p, int q)
   {
      return find(p) == find(q);
   }

   /**
    * @return whether the top connects to bottom(percolates)
    */
   public boolean isPercolates()
   {
      return percolates;
   }

   /**
    * @return number sites opened
    */
   public int getNumOpenSites()
   {
      return numOpenSites;
   }

   /**
    *  main/driver code
    */
   public static void main(String[] args)
   {
      /**
       * change size of array/grid here using width, width 10 or less will print grid,
       * able to compute array size nnn,nnn,nnn depending on computer
       */
      int width = 2;
      int size = width * width;
      //Intantiate array/tree grid structure
      Percolation uf = new Percolation(width);

      int count = 0;
      int row = width;

      //print original array for reference, array index,row,column
      System.out.println("Original array ");
      if (width < 11)
      {
         for (int x = 1; x < uf.state.length - 1; x++)
         {
            if (count == row)
            {
               System.out.println("");
               row += width;
            }
            count++;
            int y = uf.find(x);
            if (y < 10) System.out.print(" " + y + "," + ((y) / (10)) + "/" + (y % 10) + "  ");
            else if (y % width == 0) System.out.print(" " + y + "'" + (((y) / (10))) + "/" + (y % 10) + "  ");
            else System.out.print(y + "," + ((y) / (10)) + "/" + (y % 10) + "  ");
         }
      }
      System.out.println("");

      boolean perc = false;
      int num = 0;
      long startTime;
      long elapsedTime = 0;

      //************************************
      //driver code opens a random site until percolation(top connects to bottom)
      int p = 0;
      int lastOpened = 0;
      startTime = System.nanoTime();
      while (!perc)
      {
         p = (int) (Math.random() * size);

         //         if(perc == false)
         if (uf.state[p] == -1 && perc == false)
         {
            uf.open(p);
            lastOpened = p;
            num++;
            perc = uf.isPercolates();
         }
      }
      //**************************************

      //display relevant data
      elapsedTime = (System.nanoTime()) - startTime;
      double elapsedTimeInSecond = (double) elapsedTime / 1000000000;
      System.out.println("Time: " + elapsedTimeInSecond + " seconds");
      System.out.println("Opened sites: " + "'" + uf.getNumOpenSites() + "' " + num + "/" + ((uf.state.length) - 2) + " = " + (double) num / (uf.state.length - 2) + "%");
      System.out.println(uf.count() + " Trees\n");
      System.out.println("last node opened " + lastOpened + " :" + (lastOpened / width + 1) + "," + lastOpened % width);
      System.out.println("Percolates?: " + uf.percolates);

      // ----------------------------
      //print resultant percolating grid
      count = 0;
      row = width;
      if (width < 11)
      {
         for (int x = 1; x < uf.state.length - 1; x++)
         {
            if (count == row)
            {
               System.out.println("");
               row += width;
            }
            count++;
            int y = uf.find(x);
            if (x == lastOpened)
            {
               if (y == 0) System.out.print("\u001B[33m " + 5 + "  \u001B[0m");
               else
               {
                  y = uf.state[x];
                  if (y != -1) System.out.print("\u001B[33m " + 0 + "  ");
                  else System.out.print("\u001B[33m" + y + "  \u001B[0m");
               }
            } else
            {
               if (y == 0) System.out.print("\u001B[34m " + 5 + "  \u001B[0m");
               else
               {
                  y = uf.state[x];
                  if (y != -1) System.out.print(" " + 0 + "  ");
                  else System.out.print("\u001B[31m" + y + "  \u001B[0m");
               }
            }
         }
      }
   }
}
