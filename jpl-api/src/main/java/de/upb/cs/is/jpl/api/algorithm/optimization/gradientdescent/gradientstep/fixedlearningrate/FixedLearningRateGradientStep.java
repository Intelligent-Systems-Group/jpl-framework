package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.fixedlearningrate;


import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.AGradientStep;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.AGradientStepConfiguration;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.EGradientStep;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * The fixed learning rate gradient step is one of the easiest update steps for gradient descent.
 * The update consists of multiplying the gradient with the learning rate and subtracting it from
 * the weight vector to be updated.
 * 
 * @author Alexander Hetzer
 *
 */
public class FixedLearningRateGradientStep extends AGradientStep<FixedLearningRateGradientStepConfiguration> {

   private double lastWeightChange;


   /**
    * Creates a new fixed learning rate gradient step procedure.
    */
   public FixedLearningRateGradientStep() {
      lastWeightChange = 1;
   }


   @Override
   public void update(IVector weightVector, IVector gradient, double learningRate, double updateWeight) {
      IVector oldWeightVector = weightVector.duplicate();
      gradient.multiplyByConstant(learningRate * updateWeight);
      weightVector.subtractVector(gradient);
      computeWeightChange(oldWeightVector, weightVector);
   }


   /**
    * Computes the norm of the difference between the old and the updated weight vector.
    * 
    * @param oldWeightVector the old weight vector
    * @param newWeightVector the updated weight vector
    */
   private void computeWeightChange(IVector oldWeightVector, IVector newWeightVector) {
      oldWeightVector.subtractVector(newWeightVector);
      lastWeightChange = oldWeightVector.euclideanNorm();
   }


   @Override
   public void init(int numberOfDimensions) {
      // nothing to do, as this update procedure is very simple
   }


   @Override
   public double getWeightChange() {
      return lastWeightChange;
   }


   @Override
   protected AGradientStepConfiguration createDefaultConfiguration() {
      return new FixedLearningRateGradientStepConfiguration();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(lastWeightChange);
      result = prime * result + (int) (temp ^ (temp >>> 32));
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
      FixedLearningRateGradientStep other = (FixedLearningRateGradientStep) obj;
      if (Double.doubleToLongBits(lastWeightChange) != Double.doubleToLongBits(other.lastWeightChange))
         return false;
      return true;
   }


   @Override
   public String toString() {
      return EGradientStep.FIXED_LEARNING_RATE_GRADIENT_STEP.getIdentifier();
   }
}
