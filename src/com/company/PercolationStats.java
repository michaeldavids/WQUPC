package com.company;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{
   private double[] thresh;
//   private double mean = 0;
   private double stdv = 0;

   // perform independent trials on an n-by-n grid
   public PercolationStats(int n, int trials)
   {
      if (n <= 0 || trials <= 0)
         throw new IllegalArgumentException ("Illegal argument");
      int tr = 0;
      thresh = new double[trials];
      for (int i = 0; i < trials; i++)
      {
         Percolation p = new Percolation(n);

         int a;
         int b;
         boolean perc = false;
         while (!perc)
         {
            a = (StdRandom.uniform(n)) + 1;
            b = (StdRandom.uniform(n)) + 1;
//            p.open(a, b);

//            perc = p.percolates();
         }
//         thresh[tr] = ((((double) (p.numberOfOpenSites())) / n) / n);
         tr++;
      }
   }

   // sample mean of percolation threshold
   public double mean()
   {
//      mean = StdStats.mean(thresh);
      return StdStats.mean(thresh);
   }

   // sample standard deviation of percolation threshold
   public double stddev()
   {
//      stdv = StdStats.stddev(thresh);
      return StdStats.stddev(thresh);
   }

   // low endpoint of 95% confidence interval
   public double confidenceLo()
   {
      double conLo = (mean() - ((1.96 * stddev()) / (Math.sqrt(thresh.length)))); // mean
      return conLo;
   }

   // high endpoint of 95% confidence interval
   public double confidenceHi()
   {
      double conHi = (mean() + ((1.96 * stddev()) / (Math.sqrt(thresh.length)))); // mean
      return conHi;
   }

   // test client (see below)
   public static void main(String[] args)
   {
//      int[] arg = new int[2];
//      for (int i = 0; i < args.length; i++)
//      {
//         String a = args[i];
//         arg[i] = Integer.parseInt(a);
//      }
//      if (arg[0] >= 0 && arg[1] >= 0)
//      {
//         PercolationStats ps = new PercolationStats(arg[0], arg[1]);
               PercolationStats ps = new PercolationStats(200, 100);

         System.out.println("mean                    = " + ps.mean());
         System.out.println("stddev                  = " + ps.stddev());
         System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
//      }
   }
}