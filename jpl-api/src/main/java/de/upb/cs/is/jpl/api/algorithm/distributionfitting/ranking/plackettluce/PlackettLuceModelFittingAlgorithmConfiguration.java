package de.upb.cs.is.jpl.api.algorithm.distributionfitting.ranking.plackettluce;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.distributionfitting.ranking.ARankingDistributionFittingAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce.PlackettLuceModel;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class is a configuration for the {@link PlackettLuceModelFittingAlgorithm} of the
 * {@link PlackettLuceModel}. Currently this configuration contains two parameters of the algorithm.
 * Detailed descriptions of the parameters and what they are used are given as comments on the
 * member variables.
 * 
 * @author Tanja Tornede
 *
 */
public class PlackettLuceModelFittingAlgorithmConfiguration extends ARankingDistributionFittingAlgorithmConfiguration {

   private static final String ERROR_POSITIVE_PARAMETER = "The '%s' parameter has to be positive";
   private static final String ERROR_NON_NEGATIVE_PARAMETER = "The '%s' parameter has to be non negative.";

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "plackett_luce";

   private static final String PARAMETER_NAME_ITERATIONS_SAMPLE_SET_SIZE_MULTIPLIER = "iterations_sample_set_size_multiplier";
   private static final String PARAMETER_NAME_MINIMUM_REQUIRED_CHANGE_ON_UPDATE = "minimum_required_change_on_update";

   /**
    * This parameter is used as a stopping criterion. The sample set size multiplied with this
    * parameter is used as an upper bound on the amount of iterations of this algorithm.
    */
   @SerializedName(PARAMETER_NAME_ITERATIONS_SAMPLE_SET_SIZE_MULTIPLIER)
   private int iterationsSampleSetSizeMultiplier = Integer.MAX_VALUE;

   /**
    * This parameter is used as a stopping criterion. Each iteration the sum of the changes of the
    * components of the old and the new parameter vector is computed. If this sum falls below the
    * value defined by this criterion, the algorithm will stop.
    */
   @SerializedName(PARAMETER_NAME_MINIMUM_REQUIRED_CHANGE_ON_UPDATE)
   private double minimumRequiredChangeOnUpdate = Double.MAX_VALUE;


   /**
    * Creates a {@link PlackettLuceModelFittingAlgorithmConfiguration} with the default
    * configuration.
    */
   public PlackettLuceModelFittingAlgorithmConfiguration() {
      super(PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
   }


   /**
    * Creates a new {@link PlackettLuceModelFittingAlgorithmConfiguration} with the given minimum
    * required change on update parameter and the given iterations sample set multiplier.
    * 
    * Both of these parameters are used as stopping criteria of the associated
    * {@link PlackettLuceModelFittingAlgorithm}. The sample set size multiplied with the iterations
    * sample set size multiplier parameters is used as an upper bound on the amount of iterations of
    * this algorithm.
    * 
    * The minimum required change on update parameter is used in the following way: Each iteration
    * the sum of the changes of the components of the old and the new parameter vector is computed.
    * If this sum falls below the value defined by this criterion, the algorithm will stop.
    * 
    * @param minimumRequiredChangeOnUpdate the minimum required change on update parameter. Has to
    *           be larger or equal than {@code 0}.
    * @param iterationsSampleSetMultiplier the iterations sample set multiplier parameter. Has to be
    *           larger than {@code 0}.
    * @throws ParameterValidationFailedException if validating the parameters went wrong
    */
   public PlackettLuceModelFittingAlgorithmConfiguration(double minimumRequiredChangeOnUpdate, int iterationsSampleSetMultiplier)
         throws ParameterValidationFailedException {
      this();
      this.minimumRequiredChangeOnUpdate = minimumRequiredChangeOnUpdate;
      this.iterationsSampleSetSizeMultiplier = iterationsSampleSetMultiplier;
      validateParameters();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (minimumRequiredChangeOnUpdate < 0) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_NON_NEGATIVE_PARAMETER, PARAMETER_NAME_MINIMUM_REQUIRED_CHANGE_ON_UPDATE));
      }
      if (iterationsSampleSetSizeMultiplier <= 0) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_POSITIVE_PARAMETER, PARAMETER_NAME_ITERATIONS_SAMPLE_SET_SIZE_MULTIPLIER));
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      PlackettLuceModelFittingAlgorithmConfiguration castedConfiguration = (PlackettLuceModelFittingAlgorithmConfiguration) configuration;
      if (castedConfiguration.iterationsSampleSetSizeMultiplier < Integer.MAX_VALUE) {
         this.iterationsSampleSetSizeMultiplier = castedConfiguration.iterationsSampleSetSizeMultiplier;
      }
      if (castedConfiguration.minimumRequiredChangeOnUpdate < Double.MAX_VALUE) {
         this.minimumRequiredChangeOnUpdate = castedConfiguration.minimumRequiredChangeOnUpdate;
      }
   }


   /**
    * Returns the iterations sample set size multiplier parameter. This parameter is used as a
    * stopping criterion. The sample set size multiplied with this parameter is used as an upper
    * bound on the amount of iterations of this algorithm.
    * 
    * @return the iterations sample set size multiplier
    */
   public int getIterationsSampleSetSizeMultiplier() {
      return iterationsSampleSetSizeMultiplier;
   }


   /**
    * Sets the iterations sample set size multiplier parameter to the given value.
    * 
    * @param iterationsSampleSetSizeMultiplier the new value to use for the iterations sample set
    *           size multiplier parameter
    */
   public void setIterationsSampleSetSizeMultiplier(int iterationsSampleSetSizeMultiplier) {
      this.iterationsSampleSetSizeMultiplier = iterationsSampleSetSizeMultiplier;
   }


   /**
    * Returns the minimum required change on update. This parameter is used as a stopping criterion.
    * Each iteration the sum of the changes of the components of the old and the new parameter
    * vector is computed. If this sum falls below the value defined by this criterion, the algorithm
    * will stop.
    * 
    * @return the minimum required change on update
    */
   public double getMinimumRequiredChangeOnUpdate() {
      return minimumRequiredChangeOnUpdate;
   }


   /**
    * Sets the minimum required change on update parameter to the given value.
    * 
    * @param minimumRequiredChangeOnUpdate the new value for the minimum required change on update
    *           parameter
    */
   public void setMinimumRequiredChangeOnUpdate(double minimumRequiredChangeOnUpdate) {
      this.minimumRequiredChangeOnUpdate = minimumRequiredChangeOnUpdate;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + iterationsSampleSetSizeMultiplier;
      long temp;
      temp = Double.doubleToLongBits(minimumRequiredChangeOnUpdate);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (!super.equals(obj)) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      PlackettLuceModelFittingAlgorithmConfiguration other = (PlackettLuceModelFittingAlgorithmConfiguration) obj;
      if (iterationsSampleSetSizeMultiplier != other.iterationsSampleSetSizeMultiplier) {
         return false;
      }
      if (Double.doubleToLongBits(minimumRequiredChangeOnUpdate) != Double.doubleToLongBits(other.minimumRequiredChangeOnUpdate)) {
         return false;
      }
      return true;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_ITERATIONS_SAMPLE_SET_SIZE_MULTIPLIER + StringUtils.COLON + iterationsSampleSetSizeMultiplier
            + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + PARAMETER_NAME_MINIMUM_REQUIRED_CHANGE_ON_UPDATE
            + minimumRequiredChangeOnUpdate;
   }


}
