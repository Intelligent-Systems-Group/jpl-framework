package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep;


import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * The gradient step interface defines the methods, which are required by any gradient step update
 * procedure. Most importantly it defines an update method, which will perform the actual weight
 * vector update based on some specific strategy.
 * 
 * @author Alexander Hetzer
 *
 */
public interface IGradientStep {

   /**
    * Updates the weight vector, based on the given gradient and learning rate. The final result
    * will be stored in the given weight vector. Each implementation of this interface should fill
    * this method according to its update strategy.
    * 
    * @param weightVector the vector which needs to be optimized
    * @param gradient the gradient to update the weight vector
    * @param learningRate the learning rate, used to update the weight vector
    * @param updateWeight the weight to use for the update of the current weight vector
    */
   public void update(IVector weightVector, IVector gradient, double learningRate, double updateWeight);


   /**
    * Initializes this gradient step for an update procedure of a vector with
    * {@code numberOfDimensions} dimensions.
    * 
    * @param numberOfDimensions the number of dimensions of the weight vector to be updated
    */
   public void init(int numberOfDimensions);


   /**
    * Returns the norm of difference vector between the weight vector before the update and after
    * the update.
    * 
    * @return the norm of the difference vector between the weight vector before and after the
    *         update
    */
   public double getWeightChange();


   /**
    * Sets the parameters of this gradient descent based on the given Json object and performs a
    * validation of the values.
    * 
    * @param jsonObject the json object containing the algorithm parameters
    * 
    * @throws ParameterValidationFailedException if the parameter validation failed
    */
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException;


   /**
    * Returns the default configuration of this gradient step, initialized with values from the
    * according default configuration file.
    * 
    * @return the default configuration of this gradient descent
    */
   public AGradientStepConfiguration getDefaultConfiguration();


   /**
    * Sets the configuration of this gradient step to the given configuration.
    * 
    * @param configuration the configuration to set
    */
   public void setConfiguration(AGradientStepConfiguration configuration);


   /**
    * Returns the current configuration of this gradient step.
    * 
    * @return the current configuration of this gradient step
    */
   public AGradientStepConfiguration getConfiguration();

}
