package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.plackettluce;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration is used to store all necessary parameters given by the user for the
 * {@link PlackettLuceLearningAlgorithm}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class PlackettLuceConfiguration extends AAlgorithmConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "rankaggregation"
         + StringUtils.FORWARD_SLASH + "plackett_luce";

   private static final String VALIDATION_ERROR_MESSAGE_MAX_ITERATIONS = "The iteration number should be greater than zero";

   private static final String VALIDATION_ERROR_MESSAGE_TOLERANCE = "Provide a value between zero and one.";

   private static final String JOSN_KEY_VALUE_MAX_ITERATIONS = "max_iterations";

   private static final String JSON_KEY_VALUE_NORM_TOLERANCE = "norm_tolerance";

   private static final String JSON_KEY_VALUE_LOG_LIKELIHOOD_TOLERANCE = "log_likelihood_tolerance";

   @SerializedName(JOSN_KEY_VALUE_MAX_ITERATIONS)
   private int maxIterations = Integer.MAX_VALUE;

   @SerializedName(JSON_KEY_VALUE_NORM_TOLERANCE)
   private double normTolerance = Double.MAX_VALUE;

   @SerializedName(JSON_KEY_VALUE_LOG_LIKELIHOOD_TOLERANCE)
   private double logLikelihoodTolerance = Double.MAX_VALUE;


   /**
    * Creates a new configuration for the {@link PlackettLuceLearningAlgorithm}
    */
   public PlackettLuceConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (maxIterations <= 0) {
         throw new ParameterValidationFailedException(VALIDATION_ERROR_MESSAGE_MAX_ITERATIONS);
      }

      if (normTolerance < 0 || normTolerance > 1) {
         throw new ParameterValidationFailedException(VALIDATION_ERROR_MESSAGE_TOLERANCE);
      }

      if (logLikelihoodTolerance < 0 || logLikelihoodTolerance > 1) {
         throw new ParameterValidationFailedException(VALIDATION_ERROR_MESSAGE_TOLERANCE);
      }

   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      PlackettLuceConfiguration plackettLuceConfiguration = (PlackettLuceConfiguration) configuration;
      if (plackettLuceConfiguration.maxIterations < Integer.MAX_VALUE) {
         this.maxIterations = plackettLuceConfiguration.maxIterations;
      }
      if (plackettLuceConfiguration.normTolerance < Double.MAX_VALUE) {
         this.normTolerance = plackettLuceConfiguration.normTolerance;
      }
      if (plackettLuceConfiguration.logLikelihoodTolerance < Double.MAX_VALUE) {
         this.logLikelihoodTolerance = plackettLuceConfiguration.logLikelihoodTolerance;
      }
   }


   /**
    * Returns the maximum of iterations for the Minorization Maximazation algorithm.
    *
    * @return the maxIterations
    */
   public int getMaxIterations() {
      return maxIterations;
   }


   /**
    * Sets the maximum of iterations for the Minorization Maximazation algorithm.
    *
    * @param maxIterations the max of iterations
    */
   public void setMaxIterations(int maxIterations) {
      this.maxIterations = maxIterations;
   }


   /**
    * Returns the norm tolerance for the Minorization Maximazation algorithm.
    *
    * @return the norm tolerance
    */
   public double getNormTolerance() {
      return normTolerance;
   }


   /**
    * Sets the norm tolerance for the Minorization Maximazation algorithm.
    *
    * @param normTolerance the norm tolerance to set
    */
   public void setNormTolerance(double normTolerance) {
      this.normTolerance = normTolerance;
   }


   /**
    * Returns the log likelihood tolerance for the Minorization Maximazation algorithm.
    *
    * @return the log likelihood tolerance
    */
   public double getLogLikelihoodTolerance() {
      return logLikelihoodTolerance;
   }


   /**
    * Sets the log likelihood tolerance for the Minorization Maximazation algorithm.
    *
    * @param logLikelihoodTolerance the log likelihood tolerance to set
    */
   public void setLogLikelihoodTolerance(double logLikelihoodTolerance) {
      this.logLikelihoodTolerance = logLikelihoodTolerance;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(logLikelihoodTolerance);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + maxIterations;
      temp = Double.doubleToLongBits(normTolerance);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof PlackettLuceConfiguration))
         return false;
      PlackettLuceConfiguration other = (PlackettLuceConfiguration) obj;
      if (Double.doubleToLongBits(logLikelihoodTolerance) != Double.doubleToLongBits(other.logLikelihoodTolerance))
         return false;
      if (maxIterations != other.maxIterations)
         return false;
      if (Double.doubleToLongBits(normTolerance) != Double.doubleToLongBits(other.normTolerance))
         return false;
      return true;
   }


   @Override
   public String toString() {
      StringBuilder toStringBuilder = new StringBuilder();
      toStringBuilder.append(JOSN_KEY_VALUE_MAX_ITERATIONS);
      toStringBuilder.append(StringUtils.COLON);
      toStringBuilder.append(maxIterations);
      toStringBuilder.append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND);
      toStringBuilder.append(JSON_KEY_VALUE_NORM_TOLERANCE);
      toStringBuilder.append(StringUtils.COLON);
      toStringBuilder.append(normTolerance);
      toStringBuilder.append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND);
      toStringBuilder.append(JSON_KEY_VALUE_LOG_LIKELIHOOD_TOLERANCE);
      toStringBuilder.append(StringUtils.COLON);
      toStringBuilder.append(logLikelihoodTolerance);
      return toStringBuilder.toString();
   }


}