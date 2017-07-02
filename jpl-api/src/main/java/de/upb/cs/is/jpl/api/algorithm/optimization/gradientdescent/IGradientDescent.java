package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent;


import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * This interface defines the methods, which need to be supported by all gradient descent
 * implementations in this tool. In particular the {@link #optimize()} needs to be present doing the
 * actual optimization of the parameter vector and the {@link #getFinalWeightVector()} should return
 * the final result of the optimization process. Furthermore additional methods for initializing and
 * adapting the gradient descent approach have to be implemented.
 * 
 * @author Alexander Hetzer
 *
 */
public interface IGradientDescent {

   /**
    * This method optimizes a gradient based on a dataset, a gradient step procedure and a learning
    * rate. Should only be called after {@link #initialize()}.
    */
   public void optimize();


   /**
    * This method returns the final weight vector obtained via the optimization process. It should
    * only be called after the {@link IGradientDescent#optimize()} method.
    * 
    * @return the final weight vector obtained via the optimization process
    */
   public IVector getFinalWeightVector();


   /**
    * This method initializes all important parts of this gradient descent. It should be called
    * before using {@link #optimize()}.
    */
   public void initialize();


   /**
    * Returns true if the local dataset is {@code null}.
    * 
    * @return {@code true} if the local dataset is {@code null}, otherwise {@code false}
    */
   public boolean isDatasetNull();


   /**
    * Sets the given dataset as the local dataset. Note that the given dataset is not allowed to be
    * empty or {@code null}.
    * 
    * @param dataset the dataset to be used as local dataset
    */
   public void setDataset(List<Triple<IVector, Double, Double>> dataset);


   /**
    * Returns the current learning rate of this gradient descent approach.
    * 
    * @return the learning rate of this gradient descent approach
    */
   public double getLearningRate();


   /**
    * Sets the learning rate of this gradient descent approach to the given value.
    * 
    * @param learningRate the new learning rate to set
    */
   public void setLearningRate(double learningRate);


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
    * Returns the default configuration of this gradient descent, initialized with values from the
    * according default configuration file.
    * 
    * @return the default configuration of this gradient descent
    */
   public AGradientDescentConfiguration getDefaultConfiguration();


   /**
    * Sets the configuration of this gradient descent to the given configuration.
    * 
    * @param configuration the configuration to set
    */
   public void setConfiguration(AGradientDescentConfiguration configuration);


   /**
    * Returns the current configuration of this gradient descent.
    * 
    * @return the current configuration of this gradient descent
    */
   public AGradientDescentConfiguration getConfiguration();
}
