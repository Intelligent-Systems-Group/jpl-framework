package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.stochastic;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.AGradientDescentConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the configuration of {@link StochasticGradientDescent}. It contains all
 * parameters for this gradient descent approach. Detailed descriptions of the parameters and what
 * they are used are given as comments on the member variables.
 * 
 * @author Alexander Hetzer
 *
 */
public class StochasticGradientDescentConfiguration extends AGradientDescentConfiguration {


   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "stochastic_gradient_descent";

   private static final String PARAMETER_NAME_VALIDATION_ERROR_CHECK_TIME_MULTIPLIER = "validation_error_check_time_multiplier";
   private static final String PARAMETER_NAME_VALIDATION_SET_SIZE_IN_PERCENTAGE = "validation_set_size_in_percentage";
   private static final String PARAMETER_NAME_ITERATIONS_DATASET_SIZE_MULTIPLIER = "iterations_dataset_size_multiplier";
   private static final String PARAMETER_NAME_MAXIMUM_NUMBER_OF_IMPROVEMENT_CHECKS_SINCE_LAST_IMPROVEMENT = "maximum_number_of_improvement_checks_since_last_improvement";

   /**
    * This parameter is used as a stopping criterion. It determines the amount of checks since the
    * last improvement without finding a new improvement for which the algorithm should stop. This
    * improvement check is based on a validation dataset, which is separated from the original data
    * before starting.
    */
   @SerializedName(PARAMETER_NAME_MAXIMUM_NUMBER_OF_IMPROVEMENT_CHECKS_SINCE_LAST_IMPROVEMENT)
   private int maximumNumberOfImprovementChecksSinceLastImprovement = Integer.MAX_VALUE;

   /**
    * This parameter is used as a stopping criterion. The dataset size multiplied with this
    * parameter is used as an upper bound on the amount of iterations of this algorithm.
    */
   @SerializedName(PARAMETER_NAME_ITERATIONS_DATASET_SIZE_MULTIPLIER)
   private int iterationsDatasetSizeMultiplier = Integer.MAX_VALUE;

   /**
    * This parameter determines the validation dataset size in percentage. The validation dataset is
    * used to determine the improvement of the result of the algorithm over time by computing the
    * cross entropy error on the validation set.
    */
   @SerializedName(PARAMETER_NAME_VALIDATION_SET_SIZE_IN_PERCENTAGE)
   private double validationSetSizeInPercentage = Double.MAX_VALUE;

   /**
    * This parameter can be used to control how often the improvement is checked based on the
    * validation dataset. The check is performed every
    * <code>iteration % validationDataset.size() * parameter</code> iterations. This means that the
    * higher the value for this parameter is, the less often the validation error is checked and the
    * faster the algorithm runs.
    */
   @SerializedName(PARAMETER_NAME_VALIDATION_ERROR_CHECK_TIME_MULTIPLIER)
   private int validationErrorCheckTimeMultiplier = Integer.MAX_VALUE;


   /**
    * Creates a new {@link StochasticGradientDescentConfiguration}.
    */
   public StochasticGradientDescentConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      if (maximumNumberOfImprovementChecksSinceLastImprovement <= 0) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_INVALID_VALUE_FOR_PARAMETER, PARAMETER_NAME_MAXIMUM_NUMBER_OF_IMPROVEMENT_CHECKS_SINCE_LAST_IMPROVEMENT,
                     StringUtils.EMPTY_STRING + maximumNumberOfImprovementChecksSinceLastImprovement));
      }
      if (iterationsDatasetSizeMultiplier <= 0) {
         throw new ParameterValidationFailedException(String.format(ERROR_INVALID_VALUE_FOR_PARAMETER,
               PARAMETER_NAME_ITERATIONS_DATASET_SIZE_MULTIPLIER, StringUtils.EMPTY_STRING + iterationsDatasetSizeMultiplier));
      }
      if (validationSetSizeInPercentage < 0 || validationSetSizeInPercentage > 1.0) {
         throw new ParameterValidationFailedException(String.format(ERROR_INVALID_VALUE_FOR_PARAMETER,
               PARAMETER_NAME_VALIDATION_SET_SIZE_IN_PERCENTAGE, StringUtils.EMPTY_STRING + validationSetSizeInPercentage));
      }
      if (validationErrorCheckTimeMultiplier <= 0) {
         throw new ParameterValidationFailedException(String.format(ERROR_INVALID_VALUE_FOR_PARAMETER,
               PARAMETER_NAME_VALIDATION_ERROR_CHECK_TIME_MULTIPLIER, StringUtils.EMPTY_STRING + validationErrorCheckTimeMultiplier));
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      StochasticGradientDescentConfiguration stochasticGradientDescentConfiguration = (StochasticGradientDescentConfiguration) configuration;
      if (stochasticGradientDescentConfiguration.iterationsDatasetSizeMultiplier < Integer.MAX_VALUE) {
         this.iterationsDatasetSizeMultiplier = stochasticGradientDescentConfiguration.iterationsDatasetSizeMultiplier;
      }
      if (stochasticGradientDescentConfiguration.maximumNumberOfImprovementChecksSinceLastImprovement < Integer.MAX_VALUE) {
         this.maximumNumberOfImprovementChecksSinceLastImprovement = stochasticGradientDescentConfiguration.maximumNumberOfImprovementChecksSinceLastImprovement;
      }
      if (stochasticGradientDescentConfiguration.validationErrorCheckTimeMultiplier < Integer.MAX_VALUE) {
         this.validationErrorCheckTimeMultiplier = stochasticGradientDescentConfiguration.validationErrorCheckTimeMultiplier;
      }
      if (stochasticGradientDescentConfiguration.validationSetSizeInPercentage < Double.MAX_VALUE) {
         this.validationSetSizeInPercentage = stochasticGradientDescentConfiguration.validationSetSizeInPercentage;
      }
   }


   /**
    * Returns the maximum number of improvement checks since the last improvement.
    * 
    * @return the maximum number of improvement checks since the last improvement
    */
   public int getMaximumNumberOfImprovementChecksSinceLastImprovement() {
      return maximumNumberOfImprovementChecksSinceLastImprovement;
   }


   /**
    * Sets the the maximum number of improvement checks since the last improvement to the given
    * value.
    * 
    * @param maximumNumberOfImprovementChecksSinceLastImprovement the new value for the maximum
    *           number of improvement checks since the last improvement
    */
   public void setMaximumNumberOfImprovementChecksSinceLastImprovement(int maximumNumberOfImprovementChecksSinceLastImprovement) {
      this.maximumNumberOfImprovementChecksSinceLastImprovement = maximumNumberOfImprovementChecksSinceLastImprovement;
   }


   /**
    * Returns the iteration dataset size multiplier.
    * 
    * @return the iteration dataset size multiplier
    */
   public int getIterationsDatasetSizeMultiplier() {
      return iterationsDatasetSizeMultiplier;
   }


   /**
    * Sets the iteration dataset size multiplier.
    * 
    * @param iterationsDatasetSizeMultiplier the new value for the iterations dataset size
    *           multiplier
    */
   public void setIterationsDatasetSizeMultiplier(int iterationsDatasetSizeMultiplier) {
      this.iterationsDatasetSizeMultiplier = iterationsDatasetSizeMultiplier;
   }


   /**
    * Returns the validation set size in percentage.
    * 
    * @return the validation set size in percentage
    */
   public double getValidationSetSizeInPercentage() {
      return validationSetSizeInPercentage;
   }


   /**
    * Sets the validation set size in percentage to the given value.
    * 
    * @param validationSetSizeInPercentage the new validation set size in percentage
    */
   public void setValidationSetSizeInPercentage(double validationSetSizeInPercentage) {
      this.validationSetSizeInPercentage = validationSetSizeInPercentage;
   }


   /**
    * Returns the validation error check time multiplier.
    * 
    * @return the validation error check time multiplier
    */
   public int getValidationErrorCheckTimeMultiplier() {
      return validationErrorCheckTimeMultiplier;
   }


   /**
    * Sets the validation error check time multiplier to the given value.
    * 
    * @param validationErrorCheckTimeMultiplier the new value for the validation error check time
    *           multiplier
    */
   public void setValidationErrorCheckTimeMultiplier(int validationErrorCheckTimeMultiplier) {
      this.validationErrorCheckTimeMultiplier = validationErrorCheckTimeMultiplier;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_MAXIMUM_NUMBER_OF_IMPROVEMENT_CHECKS_SINCE_LAST_IMPROVEMENT + StringUtils.COLON
            + maximumNumberOfImprovementChecksSinceLastImprovement + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND
            + PARAMETER_NAME_ITERATIONS_DATASET_SIZE_MULTIPLIER + StringUtils.COLON + iterationsDatasetSizeMultiplier
            + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + PARAMETER_NAME_VALIDATION_SET_SIZE_IN_PERCENTAGE + StringUtils.COLON
            + validationSetSizeInPercentage + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND
            + PARAMETER_NAME_VALIDATION_ERROR_CHECK_TIME_MULTIPLIER + StringUtils.COLON + validationErrorCheckTimeMultiplier
            + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + super.toString();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + iterationsDatasetSizeMultiplier;
      result = prime * result + maximumNumberOfImprovementChecksSinceLastImprovement;
      result = prime * result + validationErrorCheckTimeMultiplier;
      long temp;
      temp = Double.doubleToLongBits(validationSetSizeInPercentage);
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
      StochasticGradientDescentConfiguration other = (StochasticGradientDescentConfiguration) obj;
      if (iterationsDatasetSizeMultiplier != other.iterationsDatasetSizeMultiplier)
         return false;
      if (maximumNumberOfImprovementChecksSinceLastImprovement != other.maximumNumberOfImprovementChecksSinceLastImprovement)
         return false;
      if (validationErrorCheckTimeMultiplier != other.validationErrorCheckTimeMultiplier)
         return false;
      if (Double.doubleToLongBits(validationSetSizeInPercentage) != Double.doubleToLongBits(other.validationSetSizeInPercentage))
         return false;
      return true;
   }


}
