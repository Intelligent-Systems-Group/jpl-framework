package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.stochastic;


import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.AGradientDescent;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.AGradientDescentConfiguration;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.EGradientDescent;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.IGradientStep;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.adam.AdamGradientStep;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * This class represents a stochastic gradient descent implementation, which can work with weighted
 * examples. It can be configured via the {@link StochasticGradientDescentConfiguration}. The
 * gradient is computed based on a single instance. After each epoch (computing the gradient based
 * on each instance in the dataset), the dataset is shuffled in order to enable random picks of the
 * instances, although they are actually picked sequentially.
 * 
 * A configurable percentage of the dataset is set aside before training as a validation set in
 * order to allow the usage of early stopping. By default this is 5% of the dataset. Each X
 * iterations, the validation error based on the cross entropy error is computed and the currently
 * best known weight vector is updated with the current one, if the validation error improved. X is
 * defined as the size of validation dataset times the validation error check time parameter.
 * 
 * The actual weight vector update is based on the gradient step approach which is given the
 * computed gradient and the learning rate. Currently the {@link AdamGradientStep} approach works
 * best.
 * 
 * The optimization is stopped if Y checks of the validation error were performed without
 * improvement, where Y is defined by the maximum number of improvement checks since last
 * improvement parameter. Furthermore the optimzation is stopped if the number of iterations exceeds
 * the upper bound, which is defined by the dataset size times the iterations dataset size
 * multiplier parameter.
 * 
 * @author Alexander Hetzer
 *
 */
public class StochasticGradientDescent extends AGradientDescent<StochasticGradientDescentConfiguration> {

   private static final Logger logger = LoggerFactory.getLogger(StochasticGradientDescent.class);

   private static final String DEBUG_STOPPED_AFTER_ITERATIONS = "Stopped SGD after {} iterations";

   private int numberOfIterations;

   private int currentExampleIndex;


   private double bestValidationError;
   private IVector bestWeightVector;

   private int numberOfImprovementChecksSinceLastImprovement;


   /**
    * Creates a stochastic gradient descent instance with the given gradient step procedure. Note:
    * When using this constructor, make sure to call {@link #setLearningRate(double)} before using
    * this instance.
    * 
    * @param gradientStep the gradient step procedure to use
    */
   public StochasticGradientDescent(IGradientStep gradientStep) {
      super(gradientStep);
   }


   /**
    * Creates a stochastic gradient descent instance with the given gradient step procedure and the
    * given learning rate.
    * 
    * @param gradientStep the gradient step procedure to use
    * @param learningRate the learning rate to use
    */
   public StochasticGradientDescent(IGradientStep gradientStep, double learningRate) {
      super(gradientStep, learningRate);
   }


   /**
    * Creates a new {@link StochasticGradientDescent} initialized from the default configuration.
    * 
    */
   public StochasticGradientDescent() {
      super();
   }


   /**
    * Creates a stochastic gradient descent instance with the given gradient step procedure and the
    * given learning rate. Furthermore this instance is will be optimized to work on the given
    * dataset.
    * 
    * @param dataset the dataset to optimize this gradient descent on
    * @param gradientStep the gradient step procedure to use
    * @param learningRate the learning rate to use
    */
   public StochasticGradientDescent(List<Triple<IVector, Double, Double>> dataset, IGradientStep gradientStep, double learningRate) {
      super(dataset, gradientStep, learningRate);
   }


   @Override
   public void initialize() {
      this.currentGradient = new DenseDoubleVector(numberOfDimensions);
      this.currentWeightVector = new DenseDoubleVector(numberOfDimensions);
      this.currentWeightVector.fillRandomly();

      this.bestWeightVector = this.currentWeightVector.duplicate();
      this.bestValidationError = Double.MAX_VALUE;
      this.currentExampleIndex = 0;
      this.numberOfImprovementChecksSinceLastImprovement = 0;
      this.numberOfIterations = 0;

      this.updateWeight = 1;

      fillValidationDataset(configuration.getValidationSetSizeInPercentage());


   }


   @Override
   protected void finishIteration() {
      numberOfIterations++;
      if (numberOfIterations % validationDataset.size() * configuration.getValidationErrorCheckTimeMultiplier() == 0) {
         double validationError = computeErrorOnValidationSet();
         updateBestWeightVectorAndValidationError(validationError);
      }
   }


   /**
    * Updates the best weight vector and the validation error.
    * 
    * @param currentValidationError the current validation error to check against
    */
   private void updateBestWeightVectorAndValidationError(double currentValidationError) {
      if (currentValidationError < bestValidationError) {
         bestWeightVector = currentWeightVector.duplicate();
         bestValidationError = currentValidationError;
         numberOfImprovementChecksSinceLastImprovement = 0;
      } else {
         numberOfImprovementChecksSinceLastImprovement++;
      }
   }


   @Override
   protected void computeGradient() {
      if (numberOfIterations % dataset.size() == 0) {
         currentExampleIndex = 0;
         Collections.shuffle(dataset, randomVariable);
      }

      Triple<IVector, Double, Double> randomInstance = dataset.get(currentExampleIndex);
      this.updateWeight = randomInstance.getThird();

      currentGradient = randomInstance.getFirst().duplicate();
      currentGradient.multiplyByConstant(-randomInstance.getSecond());
      currentGradient
            .divideByConstant(1 + Math.exp(randomInstance.getSecond() * currentWeightVector.dotProduct(randomInstance.getFirst())));

      currentExampleIndex++;
   }


   @Override
   protected boolean shouldRun() {
      if (numberOfIterations > dataset.size() * configuration.getIterationsDatasetSizeMultiplier()) {
         logger.debug(DEBUG_STOPPED_AFTER_ITERATIONS, numberOfIterations);
         return false;
      } else if (numberOfImprovementChecksSinceLastImprovement > configuration.getMaximumNumberOfImprovementChecksSinceLastImprovement()) {
         logger.debug(DEBUG_STOPPED_AFTER_ITERATIONS, numberOfIterations);
         return false;
      }
      return true;
   }


   @Override
   public IVector getFinalWeightVector() {
      return bestWeightVector;
   }


   @Override
   protected AGradientDescentConfiguration createDefaultConfiguration() {
      return new StochasticGradientDescentConfiguration();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(bestValidationError);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + ((bestWeightVector == null) ? 0 : bestWeightVector.hashCode());
      result = prime * result + currentExampleIndex;
      result = prime * result + numberOfImprovementChecksSinceLastImprovement;
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
      StochasticGradientDescent other = (StochasticGradientDescent) obj;
      if (Double.doubleToLongBits(bestValidationError) != Double.doubleToLongBits(other.bestValidationError))
         return false;
      if (bestWeightVector == null) {
         if (other.bestWeightVector != null)
            return false;
      } else if (!bestWeightVector.equals(other.bestWeightVector))
         return false;
      if (currentExampleIndex != other.currentExampleIndex)
         return false;
      if (numberOfImprovementChecksSinceLastImprovement != other.numberOfImprovementChecksSinceLastImprovement)
         return false;
      if (numberOfIterations != other.numberOfIterations)
         return false;
      return true;
   }


   @Override
   public String toString() {
      return EGradientDescent.STOCHASTIC_GRADIENT_DESCENT.getIdentifier();
   }


}
