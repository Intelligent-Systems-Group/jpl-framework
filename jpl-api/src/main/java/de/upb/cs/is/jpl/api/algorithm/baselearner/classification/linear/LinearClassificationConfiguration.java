package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for an implementation of {@link ALinearClassification}
 * .
 * 
 * @author Tanja Tornede
 *
 */
public abstract class LinearClassificationConfiguration extends AAlgorithmConfiguration {

   private static final String ERROR_LEARNING_RATE_NOT_POSITIVE = "The learning rate is not a positive value!";
   private static final String ERROR_NUMBER_ITERATIONS_NOT_GREATER_ONE = "The number of iterations is to small, it has to be at least 1!";

   private static final String NUMBER_OF_ITERATIONS = "number_of_iterations";
   private static final String LEARNING_RATE = "learning_rate";


   @SerializedName(LEARNING_RATE)
   protected double learningRate = Double.MAX_VALUE;

   @SerializedName(NUMBER_OF_ITERATIONS)
   protected int numberOfIterations = Integer.MAX_VALUE;


   /**
    * Creates a default configuration for {@link ALinearClassification} implementations using the
    * given default configuration file name.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file
    */
   public LinearClassificationConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      assertCorrectLearningRate();
      assertCorrectNumberOfIterations();
   }


   /**
    * Checks weather the learning rate is valid.
    * 
    * @throws ParameterValidationFailedException if the learning rate is not valid
    */
   protected void assertCorrectLearningRate() throws ParameterValidationFailedException {
      if (learningRate <= 0) {
         throw new ParameterValidationFailedException(ERROR_LEARNING_RATE_NOT_POSITIVE);
      }
   }


   /**
    * Checks weather the number of iterations are valid.
    * 
    * @throws ParameterValidationFailedException if the number of iterations are not valid
    */
   protected void assertCorrectNumberOfIterations() throws ParameterValidationFailedException {
      if (numberOfIterations < 1) {
         throw new ParameterValidationFailedException(ERROR_NUMBER_ITERATIONS_NOT_GREATER_ONE);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      LinearClassificationConfiguration castedConfiguration = (LinearClassificationConfiguration) configuration;
      copyLearningRate(castedConfiguration);
      copyNumberOfIterations(castedConfiguration);
   }


   /**
    * Copies the learning rate of the given {@link LinearClassificationConfiguration}.
    * 
    * @param castedConfiguration the configuration to copy from
    */
   private void copyLearningRate(LinearClassificationConfiguration castedConfiguration) {
      if (castedConfiguration.learningRate < Double.MAX_VALUE) {
         this.learningRate = castedConfiguration.learningRate;
      }
   }


   /**
    * Copies the number of iterations of the given {@link LinearClassificationConfiguration}.
    * 
    * @param castedConfiguration the configuration to copy from
    */
   private void copyNumberOfIterations(LinearClassificationConfiguration castedConfiguration) {
      if (castedConfiguration.numberOfIterations < Integer.MAX_VALUE) {
         this.numberOfIterations = castedConfiguration.numberOfIterations;
      }
   }


   /**
    * Returns the learning rate of the according algorithm.
    * 
    * @return the learning rate of the according algorithm
    */
   public double getLearningRate() {
      return learningRate;
   }


   /**
    * Sets the learning rate of the according algorithm
    * 
    * @param learningRate the learning rate of the according algorithm to set
    */
   public void setLearningRate(double learningRate) {
      this.learningRate = learningRate;
   }


   /**
    * Returns the number of iterations the linear classification algorithm will perform.
    * 
    * @return the number of iterations the algorithm will perform
    */
   public double getNumberOfIterations() {
      return numberOfIterations;
   }


   /**
    * Sets the number of iterations the linear classification algorithm will perform.
    * 
    * @param numberOfIterations the number of iterations the algorithm will perform
    */
   public void setNumberOfIterations(int numberOfIterations) {
      this.numberOfIterations = numberOfIterations;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      LinearClassificationConfiguration other = (LinearClassificationConfiguration) obj;
      if (Double.doubleToLongBits(learningRate) != Double.doubleToLongBits(other.learningRate))
         return false;
      if (numberOfIterations != other.numberOfIterations)
         return false;
      return true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(learningRate);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + numberOfIterations;
      return result;
   }


   @Override
   public String toString() {
      return LEARNING_RATE + StringUtils.COLON + learningRate + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + NUMBER_OF_ITERATIONS
            + StringUtils.COLON + numberOfIterations;
   }

}
