package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.adam;


import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.AGradientStep;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.AGradientStepConfiguration;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.EGradientStep;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.stochastic.StochasticGradientDescent;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * This class is an implementation of the Adam method (proposed by Diederik P. Kingma and Jimmy Lei
 * Ba), which can be used as a gradient step approach in {@link StochasticGradientDescent}.
 * 
 * The Adam method computes adaptive learning rates for each parameter. It keeps adaptive estimates
 * of the first and the second moment of the past gradients and uses these to update the weight
 * vector. As these estimates are initialized as {@code 0} vectors, they are biased towards
 * {@code 0} and therefore bias corrected versions of the estimates are used for the actual update.
 * All parameters are set to the values suggested in the paper by default.
 * 
 * @author Alexander Hetzer
 * 
 *
 */
public class AdamGradientStep extends AGradientStep<AdamGradientStepConfiguration> {

   private double exponentialDecayRateForFirstMoment;
   private double exponentialDecayRateForSecondMoment;

   private DenseDoubleVector firstMomentVector;
   private DenseDoubleVector secondMomentVector;

   private int timestep;
   private double epsilon;

   private double lastWeightChange;


   /**
    * Creates a new {@link AdamGradientStep}.
    */
   public AdamGradientStep() {
      super();
   }


   @Override
   public void update(IVector weightVector, IVector gradient, double learningRate, double updateWeight) {
      timestep++;
      IVector oldWeightVector = weightVector.duplicate();
      updateBiasedFirstMomentEstimate(gradient, updateWeight);
      updateBiasedSecondRawMomentEstimate(gradient, updateWeight);
      // one can add a decay for the learning rate here by dividing by Math.sqrt(timestep)
      updateWeightVector(weightVector, learningRate);
      computeWeightChange(oldWeightVector, weightVector);
   }


   /**
    * Updates the given weight vector with the given learning rate.
    * 
    * @param weightVector the weight vector to update
    * @param learningRate the learning rate to use for the update
    */
   private void updateWeightVector(IVector weightVector, double learningRate) {
      IVector nominator = computeBiasCorrectedFirstMomentEstimate();
      nominator.multiplyByConstant(learningRate);

      IVector denominator = computeBiasCorrectedSecondRawMomentEstimate();
      denominator.squareRoot();
      denominator.addConstant(epsilon);

      nominator.divideByVectorPairwise(denominator);

      weightVector.subtractVector(nominator);
   }


   /**
    * Updates the biased second raw moment estimate based on the given gradient.
    * 
    * @param gradient the gradient to base the update on
    * @param updateWeight the weight to weight the gradient with
    */
   private void updateBiasedSecondRawMomentEstimate(IVector gradient, double updateWeight) {
      secondMomentVector.multiplyByConstant(exponentialDecayRateForSecondMoment);
      IVector gradientCopy = gradient.multiplyByVectorPairwiseToCopy(gradient);
      gradientCopy.multiplyByConstant(1 - exponentialDecayRateForSecondMoment);
      gradientCopy.multiplyByConstant(Math.pow(updateWeight, 2));
      secondMomentVector.addVector(gradientCopy);
   }


   /**
    * Updates the biased first moment estimate based on the given gradient.
    * 
    * @param gradient the gradient to base the update on
    * @param updateWeight the weight to weight the gradient with
    */
   private void updateBiasedFirstMomentEstimate(IVector gradient, double updateWeight) {
      firstMomentVector.multiplyByConstant(exponentialDecayRateForFirstMoment);
      IVector gradientCopy = gradient.duplicate();
      gradientCopy.multiplyByConstant(1 - exponentialDecayRateForFirstMoment);
      gradientCopy.multiplyByConstant(updateWeight);
      firstMomentVector.addVector(gradientCopy);
   }


   /**
    * Computes the bias corrected first moment estimate.
    * 
    * @return the bias corrected first moment estimate
    */
   private IVector computeBiasCorrectedFirstMomentEstimate() {
      double denominator = 1 - Math.pow(exponentialDecayRateForFirstMoment, timestep);
      return firstMomentVector.divideByConstantToCopy(denominator);
   }


   /**
    * Computes the bias corrected second raw moment estimate.
    * 
    * @return the bias corrected second raw moment estimate
    */
   private IVector computeBiasCorrectedSecondRawMomentEstimate() {
      double denominator = 1 - Math.pow(exponentialDecayRateForSecondMoment, timestep);
      return secondMomentVector.divideByConstantToCopy(denominator);
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
      firstMomentVector = new DenseDoubleVector(numberOfDimensions);
      secondMomentVector = new DenseDoubleVector(numberOfDimensions);
      timestep = 0;

      exponentialDecayRateForFirstMoment = this.configuration.getExponentialDecayRateForFirstMoment();
      exponentialDecayRateForSecondMoment = this.configuration.getExponentialDecayRateForSecondMoment();

      epsilon = this.configuration.getEpsilon();

      lastWeightChange = 1;
   }


   @Override
   public double getWeightChange() {
      return lastWeightChange;
   }


   @Override
   protected AGradientStepConfiguration createDefaultConfiguration() {
      return new AdamGradientStepConfiguration();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(epsilon);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(exponentialDecayRateForFirstMoment);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(exponentialDecayRateForSecondMoment);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + ((firstMomentVector == null) ? 0 : firstMomentVector.hashCode());
      temp = Double.doubleToLongBits(lastWeightChange);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + ((secondMomentVector == null) ? 0 : secondMomentVector.hashCode());
      result = prime * result + timestep;
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
      AdamGradientStep other = (AdamGradientStep) obj;
      if (Double.doubleToLongBits(epsilon) != Double.doubleToLongBits(other.epsilon))
         return false;
      if (Double.doubleToLongBits(exponentialDecayRateForFirstMoment) != Double.doubleToLongBits(other.exponentialDecayRateForFirstMoment))
         return false;
      if (Double.doubleToLongBits(exponentialDecayRateForSecondMoment) != Double
            .doubleToLongBits(other.exponentialDecayRateForSecondMoment))
         return false;
      if (firstMomentVector == null) {
         if (other.firstMomentVector != null)
            return false;
      } else if (!firstMomentVector.equals(other.firstMomentVector))
         return false;
      if (Double.doubleToLongBits(lastWeightChange) != Double.doubleToLongBits(other.lastWeightChange))
         return false;
      if (secondMomentVector == null) {
         if (other.secondMomentVector != null)
            return false;
      } else if (!secondMomentVector.equals(other.secondMomentVector))
         return false;
      if (timestep != other.timestep)
         return false;
      return true;
   }


   @Override
   public String toString() {
      return EGradientStep.ADAM_GRADIENT_STEP.getIdentifier();
   }


}
