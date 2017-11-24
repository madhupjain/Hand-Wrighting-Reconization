
public class SampleData implements Comparable,Cloneable {

/**
  * The downsampled data as a grid of booleans.
  */
   protected boolean grid[][];

/**
  * The letter.
  */
   protected char letter;

   public SampleData(char letter,int width,int height)
   {
     grid = new boolean[width][height];
     this.letter = letter;
   }

/**
  * Set one pixel of sample data.
  */
   public void setData(int x,int y,boolean v)
   {
     grid[x][y]=v;
   }

/**
  * Get a pixel from the sample.
  */
   public boolean getData(int x,int y)
   {
     return grid[x][y];
   }

/**
  * Clear the downsampled image
  */
   public void clear()
   {
     for ( int x=0;x<grid.length;x++ )
       for ( int y=0;y<grid[0].length;y++ )
         grid[x][y]=false;
   }

/**
  * Get the height of the down sampled image.
  */
   public int getHeight()
   {
     return grid[0].length;
   }

/**
  * Get the width of the downsampled image.
  */
   public int getWidth()
   {
     return grid.length;
   }

/**
  * Get the letter that this sample represents.
  */
   public char getLetter()
   {
     return letter;
   }

/**
  * Set the letter that this sample represents.
  */
   public void setLetter(char letter)
   {
     this.letter = letter;
   }
/**
  * Compare this sample to another, used for sorting.
  */

   public int compareTo(Object o)
   {
     SampleData obj = (SampleData)o;
     if ( this.getLetter()>obj.getLetter() )
       return 1;
     else
       return -1;
   }

/**
  * Convert this sample to a string.
  */
   public String toString()
   {
     return ""+letter;
   }


/**
  * Create a copy of this sample
  */
   public Object clone()

   {

     SampleData obj = new SampleData(letter,getWidth(),getHeight());
     for ( int y=0;y<getHeight();y++ )
       for ( int x=0;x<getWidth();x++ )
         obj.setData(x,y,getData(x,y));
     return obj;
   }

}