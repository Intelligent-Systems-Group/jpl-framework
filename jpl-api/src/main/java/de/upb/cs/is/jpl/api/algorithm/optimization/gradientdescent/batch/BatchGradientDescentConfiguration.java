package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.batch;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.AGradientDescentConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the configuration for the {@link BatchGradientDescent}. All parameters are
 * described in detail in the javadoc of the according member variables.
 * 
 * @author Alexander Hetzer
 *
 */
public class BatchGradientDescentConfiguration extends AGradientDescentConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "batch_gradient_descent";

   private static final String PARAMETER_NAME_ITERATIONS_DATASET_MULTIPLIER = "iterations_dataset_size_multiplier";
   private static final String PARAMETER_NAME_MINIMAL_WEIGHT_CHANGE = "minimal_weight_change";

   /**
    * This parameter is used as a stopping criterion. The dataset size multiplied with this
    * parameter is used as an upper bound on the amount of iterations of this algorithm.
    */
   @SerializedName(PARAMETER_NAME_ITERATIONS_DATASET_MULTIPLIER)
   private int iterationsDatasetSizeMultiplier = Integer.MAX_VALUE;
   /**
    * This parameter is used as a stopping criterion. If the current norm of the difference between
    * the weight vector in this iteration and the weight vector of the last iteration is less than
    * this value, the algorithm stops.
    */
   @SerializedName(PARAMETER_NAME_MINIMAL_WEIGHT_CHANGE)
   private double minimalWeightChange = Double.MAX_VALUE;


   /**
    * Creates a new {@link BatchGradientDescentConfiguration}.
    */
   public BatchGradientDescentConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      if (iterationsDatasetSizeMultiplier <= 0) {
         throw new ParameterValidationFailedException(String.format(ERROR_INVALID_VALUE_FOR_PARAMETER,
               PARAMETER_NAME_ITERATIONS_DATASET_MULTIPLIER, iterationsDatasetSizeMultiplier));
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      BatchGradientDescentConfiguration batchGradientDescentConfiguration = (BatchGradientDescentConfiguration) configuration;
      if (batchGradientDescentConfiguration.iterationsDatasetSizeMultiplier < Integer.MAX_VALUE) {
         this.iterationsDatasetSizeMultiplier = batchGradientDescentConfiguration.iterationsDatasetSizeMultiplier;
      }
      if (batchGradientDescentConfiguration.minimalWeightChange < Double.MAX_VALUE) {
         this.minimalWeightChange = batchGradientDescentConfiguration.minimalWeightChange;
      }
   }


   /**
    * Returns the dataset size multiplier used to determine the maximum number of iterations.
    * 
    * @return the dataset size multiplier used to determine the maximum number of iterations
    */
   public int getIterationsDatasetSizeMultiplier() {
      return iterationsDatasetSizeMultiplier;
   }


   /**
    * Sets the dataset size multiplier used to determine the maximum number of iterations to the
    * given value.
    * 
    * @param iterationsDatasetSizeMultiplier the new value for the dataset size multiplier used to
    *           determine the maximum number of iterations
    */
   public void setIterationsDatasetSizeMultiplier(int iterationsDatasetSizeMultiplier) {
      this.iterationsDatasetSizeMultiplier = iterationsDatasetSizeMultiplier;
   }


   /**
    * Returns the minimal weight change that needs to happen for the algorithm to run. If the norm
    * of the change of the weight vector is below this threshold, the algorithm will stop running.
    * 
    * @return the minimal weight change that needs to happen for the algorithm to run
    */
   public double getMinimalWeightChange() {
      return minimalWeightChange;
   }


   /**
    * Sets the minimal weight change that needs to happen for the algorithm to run. If the norm of
    * the change of the weight vector is below this threshold, the algorithm will stop running.
    * 
    * @param minimalWeightChange the new value for the minimal weight change that needs to happen
    *           for the algorithm to run
    */
   public void setMinimalWeightChange(double minimalWeightChange) {
      this.minimalWeightChange = minimalWeightChange;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + iterationsDatasetSizeMultiplier;
      long temp;
      temp = Double.doubleToLongBits(minimalWeightChange);
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
      BatchGradientDescentConfiguration other = (BatchGradientDescentConfiguration) obj;
      if (iterationsDatasetSizeMultiplier != other.iterationsDatasetSizeMultiplier)
         return false;
      if (Double.doubleToLongBits(minimalWeightChange) != Double.doubleToLongBits(other.minimalWeightChange))
         return false;
      return true;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_ITERATIONS_DATASET_MULTIPLIER + StringUtils.COLON + iterationsDatasetSizeMultiplier
            + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + PARAMETER_NAME_MINIMAL_WEIGHT_CHANGE + StringUtils.COLON
            + minimalWeightChange + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + super.toString();
   }


}
