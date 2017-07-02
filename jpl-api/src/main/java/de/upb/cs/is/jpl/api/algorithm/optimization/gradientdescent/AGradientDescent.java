package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.IGradientStep;
import de.upb.cs.is.jpl.api.exception.JplRuntimeException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.WrongConfigurationTypeException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.math.util.FunctionUtils;
import de.upb.cs.is.jpl.api.metric.crossentropy.CrossEntropyError;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * This abstract class implements the general functionality of any gradient descent procedure. Its
 * main component is the {@link #optimize()} method, iteratively optimizing the gradient computed
 * based on the gradient step. Any gradient descent should be able to be configured via a
 * {@link AGradientDescentConfiguration}.
 * 
 * @author Alexander Hetzer
 * 
 * @param <CONFIG> the type of the configuration used by this gradient descent approach
 *
 */
public abstract class AGradientDescent<CONFIG extends AGradientDescentConfiguration> implements IGradientDescent {

   protected CONFIG configuration;

   private static final Logger logger = LoggerFactory.getLogger(AGradientDescent.class);

   private static final String ERROR_WRONG_CONFIGURATION_TYPE = "Initialized algorithm configuration with wrong configuration type.";

   private static final String ERROR_GIVEN_DATASET_NULL = "The given dataset is not allowed to be null.";
   private static final String ERROR_GIVEN_DATASET_EMPTY = "The given dataset is not allowed to be empty.";
   private static final String ERROR_COMPUTATION_VALIDATION_ERROR = "Error during computation of validation error: %s";

   protected List<Triple<IVector, Double, Double>> dataset;
   protected List<Triple<IVector, Double, Double>> validationDataset;
   protected int numberOfDimensions;

   protected IVector currentGradient;
   protected IVector currentWeightVector;

   protected double updateWeight;

   protected Random randomVariable;


   /**
    * Constructs a gradient descent instance with the given dataset, gradient step procedure and the
    * learning rate.
    * 
    * @param dataset the dataset this gradient descent procedure should work with
    * @param gradientStep the gradient step technique this gradient descent procedure should use
    * @param learningRate the learning rate this gradient descent procedure should use
    */
   public AGradientDescent(List<Triple<IVector, Double, Double>> dataset, IGradientStep gradientStep, double learningRate) {
      this(gradientStep, learningRate);
      setDataset(dataset);
   }


   /**
    * Constructs a gradient descent instance with the given gradient step procedure and the learning
    * rate. Note: If this constructor is used, make sure to call {@link #setDataset(List)} before
    * using this instance.
    * 
    * @param gradientStep the gradient step technique this gradient descent procedure should use
    * @param learningRate the learning rate this gradient descent procedure should use
    */
   public AGradientDescent(IGradientStep gradientStep, double learningRate) {
      this();
      this.configuration.setGradientStep(gradientStep);
      this.configuration.setLearningRate(learningRate);
   }


   /**
    * Creates a new {@link AGradientDescent} initialized with the default configuration values.
    */
   public AGradientDescent() {
      getConfiguration();
      this.randomVariable = RandomGenerator.getRNG();
      this.validationDataset = new ArrayList<>();
      this.updateWeight = 1.0;
   }


   /**
    * Constructs a gradient descent instance with the given gradient step procedure. Note: If this
    * constructor is used, make sure to call {@link #setDataset(List)} and
    * {@link #setLearningRate(double)} before using this instance.
    * 
    * @param gradientStep the gradient step technique this gradient descent procedure should use
    */
   public AGradientDescent(IGradientStep gradientStep) {
      this(gradientStep, 0);
   }


   @Override
   public void setDataset(List<Triple<IVector, Double, Double>> dataset) {
      if (dataset == null) {
         throw new IllegalArgumentException(ERROR_GIVEN_DATASET_NULL);
      }
      if (dataset.isEmpty()) {
         throw new IllegalArgumentException(ERROR_GIVEN_DATASET_EMPTY);
      }
      this.dataset = dataset;
      this.numberOfDimensions = dataset.get(0).getFirst().length();
   }


   @Override
   public void optimize() {
      this.configuration.getGradientStep().init(numberOfDimensions);
      while (shouldRun()) {
         computeGradient();
         this.configuration.getGradientStep().update(currentWeightVector, currentGradient, this.configuration.getLearningRate(),
               updateWeight);
         finishIteration();
      }

   }


   /**
    * This method is called at the end of each iteration. It should be filled with any special
    * update, which needs to be done each iteration.
    */
   protected abstract void finishIteration();


   /**
    * Computes the gradient based on the current weight vector and the dataset.
    */
   protected abstract void computeGradient();


   /**
    * Determines whether the algorithm should do another gradient step or whether it should stop.
    * 
    * @return {@code true}, if the algorithm should to another gradient step, {@code false}, if it
    *         should stop
    */
   protected abstract boolean shouldRun();


   /**
    * Creates the default configuration of this gradient descent and returns it.
    * 
    * @return the default algorithm configuration of this gradient descent
    */
   protected abstract AGradientDescentConfiguration createDefaultConfiguration();


   /**
    * Removes some instances from the training dataset and adds these to the validation dataset. The
    * amount of instances removed depends on the given percentage.
    * 
    * @param sizeInPercentage the relative amount of instances to add to the validation dataset
    */
   protected void fillValidationDataset(double sizeInPercentage) {
      validationDataset = new ArrayList<>();
      int targetSize = (int) (dataset.size() * sizeInPercentage);
      for (int i = 0; i < targetSize; i++) {
         validationDataset.add(dataset.remove(randomVariable.nextInt(dataset.size())));
      }
   }


   @Override
   public IVector getFinalWeightVector() {
      return currentWeightVector;
   }


   @Override
   public boolean isDatasetNull() {
      return dataset == null;
   }


   /**
    * Computes the error on the validation set based on the cross entropy error.
    * 
    * @return the error on the validation set based on the cross entropy error
    */
   protected double computeErrorOnValidationSet() {
      if (validationDataset.isEmpty()) {
         throw new JplRuntimeException("Cannot compute validation error on an empty validation set.");
      }
      double result = 0;
      CrossEntropyError lossFunction = new CrossEntropyError();
      for (Triple<IVector, Double, Double> instance : validationDataset) {
         try {
            result += lossFunction.getLossForSingleRating(instance.getSecond(),
                  FunctionUtils.logisticFunction(currentWeightVector.dotProduct(instance.getFirst())));
         } catch (LossException lossException) {
            logger.error(String.format(ERROR_COMPUTATION_VALIDATION_ERROR, lossException.getMessage()), lossException);
         }
      }
      return result / validationDataset.size();
   }


   @Override
   public double getLearningRate() {
      return configuration.getLearningRate();
   }


   @Override
   public void setLearningRate(double learningRate) {
      this.configuration.setLearningRate(learningRate);
   }


   @Override
   public AGradientDescentConfiguration getDefaultConfiguration() {
      AGradientDescentConfiguration defaultConfiguration = createDefaultConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }


   @Override
   @SuppressWarnings("unchecked")
   public void setConfiguration(AGradientDescentConfiguration configuration) {
      if (configuration.getClass().isInstance(createDefaultConfiguration().getClass())) {
         throw new WrongConfigurationTypeException(ERROR_WRONG_CONFIGURATION_TYPE);
      }
      this.configuration = (CONFIG) configuration;
   }


   @Override
   @SuppressWarnings("unchecked")
   public AGradientDescentConfiguration getConfiguration() {
      if (configuration == null) {
         configuration = (CONFIG) getDefaultConfiguration();
      }
      return configuration;
   }


   @Override
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException {
      getConfiguration().overrideConfiguration(jsonObject);
   }


   @Override
   public String toString() {
      return getClass().getSimpleName();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
      result = prime * result + ((currentGradient == null) ? 0 : currentGradient.hashCode());
      result = prime * result + ((currentWeightVector == null) ? 0 : currentWeightVector.hashCode());
      result = prime * result + ((dataset == null) ? 0 : dataset.hashCode());
      result = prime * result + numberOfDimensions;
      result = prime * result + ((randomVariable == null) ? 0 : randomVariable.hashCode());
      long temp;
      temp = Double.doubleToLongBits(updateWeight);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + ((validationDataset == null) ? 0 : validationDataset.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      AGradientDescent<?> other = (AGradientDescent<?>) obj;
      if (configuration == null) {
         if (other.configuration != null)
            return false;
      } else if (!configuration.equals(other.configuration))
         return false;
      if (currentGradient == null) {
         if (other.currentGradient != null)
            return false;
      } else if (!currentGradient.equals(other.currentGradient))
         return false;
      if (currentWeightVector == null) {
         if (other.currentWeightVector != null)
            return false;
      } else if (!currentWeightVector.equals(other.currentWeightVector))
         return false;
      if (dataset == null) {
         if (other.dataset != null)
            return false;
      } else if (!dataset.equals(other.dataset))
         return false;
      if (numberOfDimensions != other.numberOfDimensions)
         return false;
      if (randomVariable == null) {
         if (other.randomVariable != null)
            return false;
      } else if (!randomVariable.equals(other.randomVariable))
         return false;
      if (Double.doubleToLongBits(updateWeight) != Double.doubleToLongBits(other.updateWeight))
         return false;
      if (validationDataset == null) {
         if (other.validationDataset != null)
            return false;
      } else if (!validationDataset.equals(other.validationDataset))
         return false;
      return true;
   }


}
