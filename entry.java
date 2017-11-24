import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Entry extends JPanel {

   /**
    * The image that the user is drawing into.
    */
   protected Image entryImage;

   /**
    * A graphics handle to the image that the
    * user is drawing into.
    */
   protected Graphics entryGraphics;

   /**
    * The last x that the user was drawing at.
    */
   protected int lastX = -1;

   /**
    * The last y that the user was drawing at.
    */
   protected int lastY = -1;

   /**
    * The down sample component used with this
    * component.
    */
   protected Sample sample;

   /**
    * Specifies the left boundary of the cropping
    * rectangle.
    */
   protected int downSampleLeft;

   /**
    * Specifies the right boundary of the cropping
    * rectangle.
    */
   protected int downSampleRight;

   /**
    * Specifies the top boundary of the cropping
    * rectangle.
    */
   protected int downSampleTop;

   /**
    * Specifies the bottom boundary of the cropping
    * rectangle.
    */
   protected int downSampleBottom;

   /**
    * The downsample ratio for x.
    */
   protected double ratioX;

   /**
    * The downsample ratio for y
    */
   protected double ratioY;

   /**
    * The pixel map of what the user has drawn.
    * Used to downsample it.
    */
   protected int pixelMap[];

   Entry()
   {
     enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK|
                  AWTEvent.MOUSE_EVENT_MASK|
                  AWTEvent.COMPONENT_EVENT_MASK);
   }

   protected void initImage()
   {
     entryImage = createImage(getWidth(),getHeight());
     entryGraphics = entryImage.getGraphics();
     entryGraphics.setColor(Color.white);
     entryGraphics.fillRect(0,0,getWidth(),getHeight());
   }

   public void paint(Graphics g)
   {
     if ( entryImage==null )
       initImage();
     g.drawImage(entryImage,0,0,this);
     System.out.println("Height of Iamge " + entryImage.getHeight(this));
     System.out.println("Width of Iamge " + entryImage.getWidth(this));
     
     g.setColor(Color.black);
     g.drawRect(0,0,getWidth(),getHeight());
     g.setColor(Color.red);
     System.out.println("Left " + downSampleLeft);
     System.out.println("Top " + downSampleTop);
     System.out.println("Right " + downSampleRight);
     System.out.println("Bottom " + downSampleBottom);
     
     g.drawRect(downSampleLeft,
                downSampleTop,
                downSampleRight-downSampleLeft,
                downSampleBottom-downSampleTop);

   }

   /**
    * Process messages.
    */
   protected void processMouseEvent(MouseEvent e)
   {
     if ( e.getID()!=MouseEvent.MOUSE_PRESSED )
       return;
     lastX = e.getX();
     lastY = e.getY();
   }

   protected void processMouseMotionEvent(MouseEvent e)
   {
     if ( e.getID()!=MouseEvent.MOUSE_DRAGGED )
       return;

     entryGraphics.setColor(Color.black);
     entryGraphics.drawLine(lastX,lastY,e.getX(),e.getY());
     getGraphics().drawImage(entryImage,0,0,this);
     lastX = e.getX();
     lastY = e.getY();
   }

   public void setSample(Sample s)
   {
     sample = s;
   }

   public Sample getSample()
   {
     return sample;
   }

   protected boolean hLineClear(int y)
   {
     int w = entryImage.getWidth(this);
     for ( int i=0;i<w;i++ ) {
       if ( pixelMap[(y*w)+i] !=-1 )
         return false;
     }
     return true;
   }

   protected boolean vLineClear(int x)
   {
     int w = entryImage.getWidth(this);
     int h = entryImage.getHeight(this);
     for ( int i=0;i<h;i++ ) {
       if ( pixelMap[(i*w)+x] !=-1 )
         return false;
     }
     return true;
   }

   protected void findBounds(int w,int h)
   {
     // top line
     for ( int y=0;y<h;y++ ) {
       if ( !hLineClear(y) ) {
         downSampleTop=y;
         
         break;
       }

     }
     // bottom line
     for ( int y=h-1;y>=0;y-- ) {
       if ( !hLineClear(y) ) {
         downSampleBottom=y;
         
         break;
       }
     }
     // left line
     for ( int x=0;x<w;x++ ) {
       if ( !vLineClear(x) ) {
         downSampleLeft = x;
         

         break;
       }
     }

     // right line
     for ( int x=w-1;x>=0;x-- ) {
       if ( !vLineClear(x) ) {
         downSampleRight = x;
         

         break;
       }
     }
   }

   /**
    * Called to downsample a quadrant of the image.
    */
   protected boolean downSampleQuadrant(int x,int y)
   {
     int w = entryImage.getWidth(this);
     int startX = (int)(downSampleLeft+(x*ratioX));
     int startY = (int)(downSampleTop+(y*ratioY));
     int endX = (int)(startX + ratioX);
     int endY = (int)(startY + ratioY);

     for ( int yy=startY;yy<=endY;yy++ ) {
       for ( int xx=startX;xx<=endX;xx++ ) {
         int loc = xx+(yy*w);

         if ( pixelMap[ loc  ]!= -1 )
           return true;
       }
     }

     return false;
   }


   public void downSample()
   {
     int w = entryImage.getWidth(this);
     int h = entryImage.getHeight(this);

     PixelGrabber grabber = new PixelGrabber(
                                            entryImage,
                                            0,
                                            0,
                                            w,
                                            h,
                                            true);
     try {
       grabber.grabPixels();
       pixelMap = (int[])grabber.getPixels();
       findBounds(w,h);

       // now downsample
       SampleData data = sample.getData();

       ratioX = (double)(downSampleRight-
                         downSampleLeft)/(double)data.getWidth();
       ratioY = (double)(downSampleBottom-
                         downSampleTop)/(double)data.getHeight();

       for ( int y=0;y<data.getHeight();y++ ) {
         for ( int x=0;x<data.getWidth();x++ ) {
           if ( downSampleQuadrant(x,y) )
             data.setData(x,y,true);
           else
             data.setData(x,y,false);
         }
       }

       sample.repaint();
       repaint();
     } catch ( InterruptedException e ) {
     }
   }

   /**
    * Called to clear the image.
    */
   public void clear()
   {
     this.entryGraphics.setColor(Color.white);
     this.entryGraphics.fillRect(0,0,getWidth(),getHeight());
     this.downSampleBottom =
     this.downSampleTop =
     this.downSampleLeft =
     this.downSampleRight = 0;
     repaint();
   }
}
