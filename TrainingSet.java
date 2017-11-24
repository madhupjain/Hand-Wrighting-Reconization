import java.io.*;
import java.util.*;

public class TrainingSet {

   protected int inputCount;
   protected int outputCount;
   protected double input[][];
   protected double output[][];
   protected double classify[];
   protected int trainingSetCount;

   TrainingSet ( int inputCount , int outputCount )
   {
     this.inputCount = inputCount;
     this.outputCount = outputCount;
     trainingSetCount = 0;
   }

   /**
    * Get the input neuron count
    */
   public int getInputCount()
   {
     return inputCount;
   }

   /**
    * Get the output neuron count
    */
   public int getOutputCount()
   {
     return outputCount;
   }

   /**
    * Set the number of entries in the training set. 
    */
   public void setTrainingSetCount(int trainingSetCount)
   {
     this.trainingSetCount = trainingSetCount;
     input = new double[trainingSetCount][inputCount];
     output = new double[trainingSetCount][outputCount];
     classify = new double[trainingSetCount];
   }

   /**
    * Get the training set data.
    */
   public int getTrainingSetCount()
   {
     return trainingSetCount;
   }

   /**
    * Set one of the training set's inputs.
    */
   void setInput(int set,int index,double value)
   throws RuntimeException
   {
     if ( (set<0) || (set>=trainingSetCount) )
       throw(new RuntimeException("Training set out of range:" + set ));
     if ( (index<0) || (index>=inputCount) )
       throw(new RuntimeException("Training input index out of range:" 
+ index ));
     input[set][index] = value;
   }


   /**
    * Set one of the training set's outputs.
    */
   void setOutput(int set,int index,double value)
   throws RuntimeException
   {
     if ( (set<0) || (set>=trainingSetCount) )
       throw(new RuntimeException("Training set out of range:" + set ));
     if ( (index<0) || (set>=outputCount) )
       throw(new RuntimeException("Training input index out of range:" 
+ index ));
     output[set][index] = value;
   }


   /**
    * Set one of the training set's classifications.
    */
   void setClassify(int set,double value)
   throws RuntimeException
   {
     if ( (set<0) || (set>=trainingSetCount) )
       throw(new RuntimeException("Training set out of range:" + set ));
     classify[set] = value;
   }


   /**
    * Get a specified input value.
    */
   double getInput(int set,int index)
   throws RuntimeException
   {
     if ( (set<0) || (set>=trainingSetCount) )
       throw(new RuntimeException("Training set out of range:" + set ));
     if ( (index<0) || (index>=inputCount) )
       throw(new RuntimeException("Training input index out of range:" 
+ index ));
     return input[set][index];
   }

   /**
    * Get one of the output values.
    */
   double getOutput(int set,int index)
   throws RuntimeException
   {
     if ( (set<0) || (set>=trainingSetCount) )
       throw(new RuntimeException("Training set out of range:" + set ));
     if ( (index<0) || (set>=outputCount) )
       throw(new RuntimeException("Training input index out of range:" 
+ index ));
     return output[set][index];
   }

   /**
    * Get the classification.
    */
   double getClassify(int set)
   throws RuntimeException
   {
     if ( (set<0) || (set>=trainingSetCount) )
       throw(new RuntimeException("Training set out of range:" + set ));
     return classify[set];
   }

   /**
    * Calculate the classifications.
    */
   void CalculateClass(int c)
   {
     for ( int i=0;i<=trainingSetCount;i++ ) {
       classify[i] = c + 0.1;
     }
   }
   /**
    * Get an output set.
    */

   double []getOutputSet(int set)
   throws RuntimeException
   {
     if ( (set<0) || (set>=trainingSetCount) )
       throw(new RuntimeException("Training set out of range:" + set ));
     return output[set];
   }

   /**
    * Get an input set.
    */

   double []getInputSet(int set)
   throws RuntimeException
   {
     if ( (set<0) || (set>=trainingSetCount) )
       throw(new RuntimeException("Training set out of range:" + set ));
     return input[set];
   }


}