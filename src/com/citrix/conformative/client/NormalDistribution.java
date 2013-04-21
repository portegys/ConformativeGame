// Normal Distribution.

package com.citrix.conformative.client;

import java.util.Random;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.i18n.client.NumberFormat;

public class NormalDistribution
{
   public static final int      CANVAS_WIDTH        = 300;
   public static final int      CANVAS_HEIGHT       = 300;
   public static final int      CANVAS_BORDER       = 30;
   public static final CssColor GRAPH_POINT_COLOR   = CssColor.make("rgba(150, 50, 50, 180)");
   public static final int      GRAPH_POINT_WIDTH   = 4;
   public static final int      NUM_PLOT_INTERVALS  = 20;
   public static final int      NUMBERING_FREQUENCY = 5;
   public static final int      NUM_SIGMAS          = 3;
   public static final double   DEFAULT_MU          = 10.0;
   public static final double   DEFAULT_SIGMA       = 2.0;
   private Canvas               canvas;
   private double               mu;
   private double               sigma;
   private Random               random;

   // Constructors.
   public NormalDistribution(Canvas canvas, double mu, double sigma)
   {
      this.canvas = canvas;
      this.mu     = mu;
      this.sigma  = sigma;
      random      = new Random();
   }


   public NormalDistribution(Canvas canvas)
   {
      this.canvas = canvas;
      mu          = DEFAULT_MU;
      sigma       = DEFAULT_SIGMA;
      random      = new Random();
   }


   public double getMu()
   {
      return(mu);
   }


   public void setMu(double mu)
   {
      this.mu = mu;
   }


   public double getSigma()
   {
      return(sigma);
   }


   public void setSigma(double sigma)
   {
      this.sigma = sigma;
   }


   // Get next value from distribution.
   public double nextValue()
   {
      double result = (random.nextGaussian() * sigma) + mu;

      if (result < 0.0)
      {
         result = 0.0;
      }
      return(result);
   }


   // Get probability value for x.
   public double phi(double x)
   {
      double d = x - mu;

      return(Math.exp(-(d * d) / (2.0 * sigma * sigma)) / (sigma * Math.sqrt(2.0 * Math.PI)));
   }


   // Draw.
   public void draw()
   {
      int x0, y0, x1, y1, x2, y2;

      canvas.setWidth(CANVAS_WIDTH + "px");
      canvas.setCoordinateSpaceWidth(CANVAS_WIDTH);
      canvas.setHeight(CANVAS_HEIGHT + "px");
      canvas.setCoordinateSpaceHeight(CANVAS_HEIGHT);
      Context2d context = canvas.getContext2d();

      double xLow = mu - ((double)NUM_SIGMAS * sigma);
      if (xLow < 0.0)
      {
         xLow = 0.0;
      }
      double xHigh     = mu + ((double)NUM_SIGMAS * sigma);
      double xInterval = (xHigh - xLow) / (double)NUM_PLOT_INTERVALS;
      double yHigh     = phi(mu);
      double yInterval = yHigh / (double)NUM_PLOT_INTERVALS;

      double xScale = ((double)CANVAS_WIDTH - 2 * CANVAS_BORDER) / NUM_PLOT_INTERVALS;
      double yScale = ((double)CANVAS_HEIGHT - 2 * CANVAS_BORDER) / NUM_PLOT_INTERVALS;

      double[] graphXcoords = new double[NUM_PLOT_INTERVALS + 1];
      int[] graphXpoints    = new int[NUM_PLOT_INTERVALS + 1];
      int[] graphYpoints    = new int[NUM_PLOT_INTERVALS + 1];
      for (int i = 0; i <= NUM_PLOT_INTERVALS; i++)
      {
         double x = xLow + ((double)i * xInterval);
         graphXcoords[i] = x;
         double y = phi(x);
         x1 = (int)((double)i * xScale + CANVAS_BORDER);
         y1 = (int)(((yHigh - y) / yInterval) * yScale + CANVAS_BORDER);
         graphXpoints[i] = x1;
         graphYpoints[i] = y1;
      }

      // Draw x and y axes.
      context.setStrokeStyle(CssColor.make("rgba(0, 0, 0, 255)"));
      context.beginPath();
      context.moveTo(CANVAS_BORDER, CANVAS_HEIGHT - CANVAS_BORDER);
      context.lineTo(CANVAS_BORDER, CANVAS_BORDER);
      context.closePath();
      context.stroke();
      context.beginPath();
      context.moveTo(CANVAS_BORDER, CANVAS_HEIGHT - CANVAS_BORDER);
      context.lineTo(CANVAS_WIDTH - CANVAS_BORDER, CANVAS_HEIGHT - CANVAS_BORDER);
      context.closePath();
      context.stroke();

      // Draw intervals for y axis.
      NumberFormat decimalFormat = NumberFormat.getFormat(".##");
      int          borderGap34   = (3 * CANVAS_BORDER) / 4;
      x0 = CANVAS_BORDER;
      x1 = GRAPH_POINT_WIDTH + CANVAS_BORDER;
      int off1 = CANVAS_HEIGHT - ((CANVAS_HEIGHT - CANVAS_BORDER * 2) / NUM_PLOT_INTERVALS + CANVAS_BORDER);
      int off2 = CANVAS_HEIGHT - ((2 * (CANVAS_HEIGHT - CANVAS_BORDER * 2)) / NUM_PLOT_INTERVALS + CANVAS_BORDER);
      int off  = (off2 - off1) / 4;
      for (int i = 0; i <= NUM_PLOT_INTERVALS; i++)
      {
         y0 = CANVAS_HEIGHT - ((i * (CANVAS_HEIGHT - CANVAS_BORDER * 2)) / NUM_PLOT_INTERVALS + CANVAS_BORDER);
         y1 = y0;
         context.beginPath();
         context.moveTo(x0, y0);
         context.lineTo(x1, y1);
         context.closePath();
         context.stroke();
         if ((i % NUMBERING_FREQUENCY) == 0)
         {
            context.fillText(decimalFormat.format((double)i * yInterval), x0 - borderGap34, y0 - off);
         }
      }

      // Draw intervals x axis
      int borderGap2 = CANVAS_BORDER / 2;
      y0   = CANVAS_HEIGHT - CANVAS_BORDER;
      y1   = y0 - GRAPH_POINT_WIDTH;
      off1 = (CANVAS_WIDTH - CANVAS_BORDER * 2) / NUM_PLOT_INTERVALS + CANVAS_BORDER;
      off2 = 2 * (CANVAS_WIDTH - CANVAS_BORDER * 2) / NUM_PLOT_INTERVALS + CANVAS_BORDER;
      off  = (off2 - off1) / 2;
      for (int i = 0; i <= NUM_PLOT_INTERVALS; i++)
      {
         x0 = i * (CANVAS_WIDTH - CANVAS_BORDER * 2) / NUM_PLOT_INTERVALS + CANVAS_BORDER;
         x1 = x0;
         context.beginPath();
         context.moveTo(x0, y0);
         context.lineTo(x1, y1);
         context.closePath();
         context.stroke();
         if ((i % NUMBERING_FREQUENCY) == 0)
         {
            context.fillText(decimalFormat.format(graphXcoords[i]), x0 - off, y0 + borderGap2);
         }
      }

      // Draw lines.
      for (int i = 0; i < NUM_PLOT_INTERVALS; i++)
      {
         context.beginPath();
         x1 = graphXpoints[i];
         y1 = graphYpoints[i];
         context.moveTo(x1, y1);
         x2 = graphXpoints[i + 1];
         y2 = graphYpoints[i + 1];
         context.lineTo(x2, y2);
         context.closePath();
         context.stroke();
      }

      // Draw points.
      context.setFillStyle(GRAPH_POINT_COLOR);
      for (int i = 0; i <= NUM_PLOT_INTERVALS; i++)
      {
         int x = graphXpoints[i];
         int y = graphYpoints[i];
         context.beginPath();
         context.arc(x, y, Math.abs((double)GRAPH_POINT_WIDTH / 2.0), 0.0, Math.PI * 2.0);
         context.closePath();
         context.fill();
      }
   }
}
