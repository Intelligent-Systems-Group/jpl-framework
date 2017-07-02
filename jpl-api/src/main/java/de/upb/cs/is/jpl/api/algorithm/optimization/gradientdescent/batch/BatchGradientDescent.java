package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.batch;


import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.AGradientDescent;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.AGradientDescentConfiguration;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.EGradientDescent;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.IGradientStep;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * This class is a very simple implementation of the batch gradient descent, using all instances of
 * a dataset for a single gradient computation.
 * 
 * @author Alexander Hetzer
 *
 */
public class BatchGradientDescent extends AGradientDescent<BatchGradientDescentConfiguration> {

   private int numberOfIterations;


   /**
    * Creates a new {@link BatchGradientDescent} initialized from the default configuration values.
    */
   public BatchGradientDescent() {
      super();
   }


   /**
    * Creates a batch gradient descent instance with the given gradient step procedure.
    * 
    * @param gradientStep the gradient step procedure to use
    */
   public BatchGradientDescent(IGradientStep gradientStep) {
      super(gradientStep);
   }


   /**
    * Creates a batch gradient descent instance with the given gradient step procedure and the given
    * learning rate.
    * 
    * @param gradientStep the gradient step procedure to use
    * @param learningRate the learning rate to use
    */
   public BatchGradientDescent(IGradientStep gradientStep, double learningRate) {
      super(gradientStep, learningRate);
   }


   /**
    * Creates a batch gradient descent instance with the given gradient step procedure and the given
    * learning rate. Furthermore this instance is will be optimized to work on the given dataset.
    * 
    * @param dataset the dataset to optimize this gradient descent on
    * @param gradientStep the gradient step procedure to use
    * @param learningRate the learning rate to use
    */
   public BatchGradientDescent(List<Triple<IVector, Double, Double>> dataset, IGradientStep gradientStep, double learningRate) {
      super(dataset, gradientStep, learningRate);
      numberOfIterations = 0;
   }


   @Override
   protected void finishIteration() {
      numberOfIterations++;
   }


   @Override
   protected void computeGradient() {
      currentGradient.zeroAllDimensions();
      IVector instanceCopy = new DenseDoubleVector(numberOfDimensions);
      for (Triple<IVector, Double, Double> instance : dataset) {
         instanceCopy.zeroAllDimensions();
         instanceCopy.addVector(instance.getFirst());
         instanceCopy.multiplyByConstant(instance.getSecond());
         double denominator = 1 + Math.exp(instance.getSecond() * currentWeightVector.dotProduct(instance.getFirst()));
         instanceCopy.divideByConstant(denominator);
         currentGradient.addVector(instanceCopy);
      }
      currentGradient.divideByConstant(-dataset.size());
   }


   @Override
   protected boolean shouldRun() {
      boolean shouldRun = numberOfIterations < dataset.size() * this.configuration.getIterationsDatasetSizeMultiplier();
      shouldRun = shouldRun && this.configuration.getGradientStep().getWeightChange() > this.configuration.getMinimalWeightChange();
      return shouldRun;
   }


   @Override
   public void initialize() {
      this.currentGradient = new DenseDoubleVector(numberOfDimensions);
      this.currentWeightVector = new DenseDoubleVector(numberOfDimensions);
      currentWeightVector.fillRandomly();
      this.updateWeight = 1.0;
   }


   @Override
   protected AGradientDescentConfiguration createDefaultConfiguration() {
      return new BatchGradientDescentConfiguration();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + numberOfIterations;
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      BatchGradientDescent other = (BatchGradientDescent) obj;
      if (numberOfIterations != other.numberOfIterations)
         return false;
      return true;
   }


   @Override
   public String toString() {
      return EGradientDescent.BATCH_GRADIENT_DESCENT.getIdentifier();
   }


}
