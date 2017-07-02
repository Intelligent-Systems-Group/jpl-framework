package de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.adam;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.AGradientStepConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the configuration for the {@link AdamGradientStep}. It stores all
 * parameters of the adam gradient step approach, which are described in detail in the javadoc on
 * the member variables.
 * 
 * @author Alexander Hetzer
 *
 */
public class AdamGradientStepConfiguration extends AGradientStepConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "adam_gradient_step";

   private static final String PARAMETER_NAME_BETA1 = "beta1";
   private static final String PARAMETER_NAME_BETA2 = "beta2";
   private static final String PARAMETER_NAME_EPSILON = "epsilon";

   /**
    * This parameter is the exponential decay rate for the first moment used for the weight vector
    * update in the gradient step procedure.
    */
   @SerializedName(PARAMETER_NAME_BETA1)
   protected double exponentialDecayRateForFirstMoment = Double.MAX_VALUE;
   /**
    * This parameter is the exponential decay rate for the second moment used for the weight vector
    * update in the gradient step procedure.
    */
   @SerializedName(PARAMETER_NAME_BETA2)
   protected double exponentialDecayRateForSecondMoment = Double.MAX_VALUE;

   /**
    * This parameter is a very small number ensuring that no division by zero is happening during
    * the weight vector update.
    */
   @SerializedName(PARAMETER_NAME_EPSILON)
   protected double epsilon = Double.MAX_VALUE;


   /**
    * Creates a new {@link AdamGradientStepConfiguration}.
    */
   public AdamGradientStepConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (exponentialDecayRateForFirstMoment <= 0 || exponentialDecayRateForFirstMoment >= 1.0) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_INVALID_VALUE_FOR_PARAMETER, PARAMETER_NAME_BETA1, String.valueOf(exponentialDecayRateForFirstMoment)));
      }
      if (exponentialDecayRateForSecondMoment <= 0 || exponentialDecayRateForSecondMoment >= 1.0) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_INVALID_VALUE_FOR_PARAMETER, PARAMETER_NAME_BETA2, String.valueOf(exponentialDecayRateForSecondMoment)));
      }
      if (epsilon <= 0 || epsilon >= 1) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_INVALID_VALUE_FOR_PARAMETER, PARAMETER_NAME_EPSILON, String.valueOf(epsilon)));
      }

   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      AdamGradientStepConfiguration adamGradientStepConfiguration = (AdamGradientStepConfiguration) configuration;
      if (adamGradientStepConfiguration.exponentialDecayRateForFirstMoment < Double.MAX_VALUE) {
         this.exponentialDecayRateForFirstMoment = adamGradientStepConfiguration.exponentialDecayRateForFirstMoment;
      }
      if (adamGradientStepConfiguration.exponentialDecayRateForSecondMoment < Double.MAX_VALUE) {
         this.exponentialDecayRateForSecondMoment = adamGradientStepConfiguration.exponentialDecayRateForSecondMoment;
      }
      if (adamGradientStepConfiguration.epsilon < Double.MAX_VALUE) {
         this.epsilon = adamGradientStepConfiguration.epsilon;
      }

   }


   /**
    * Returns the exponential decay rate for the first moment.
    * 
    * @return the exponential decay rate for the first moment
    */
   public double getExponentialDecayRateForFirstMoment() {
      return exponentialDecayRateForFirstMoment;
   }


   /**
    * Sets the exponential decay rate for the first moment to the given value.
    * 
    * @param exponentialDecayRateForFirstMoment the new value for the exponential decay rate
    */
   public void setExponentialDecayRateForFirstMoment(double exponentialDecayRateForFirstMoment) {
      this.exponentialDecayRateForFirstMoment = exponentialDecayRateForFirstMoment;
   }


   /**
    * Returns the exponential decay rate for the second moment.
    * 
    * @return the exponential decay rate for the second moment
    */
   public double getExponentialDecayRateForSecondMoment() {
      return exponentialDecayRateForSecondMoment;
   }


   /**
    * Sets the exponential decay rate for the second moment to the given value.
    * 
    * @param exponentialDecayRateForSecondMoment the new value for the exponential decay rate
    */
   public void setExponentialDecayRateForSecondMoment(double exponentialDecayRateForSecondMoment) {
      this.exponentialDecayRateForSecondMoment = exponentialDecayRateForSecondMoment;
   }


   /**
    * Returns the epsilon.
    * 
    * @return the espilon
    */
   public double getEpsilon() {
      return epsilon;
   }


   /**
    * Sets the epsilon to the given value.
    * 
    * @param epsilon the new epsilon
    */
   public void setEpsilon(double epsilon) {
      this.epsilon = epsilon;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_BETA1 + StringUtils.COLON + exponentialDecayRateForFirstMoment + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND
            + PARAMETER_NAME_BETA2 + StringUtils.COLON + exponentialDecayRateForSecondMoment
            + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + PARAMETER_NAME_EPSILON + StringUtils.COLON + epsilon;
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
      AdamGradientStepConfiguration other = (AdamGradientStepConfiguration) obj;
      if (Double.doubleToLongBits(epsilon) != Double.doubleToLongBits(other.epsilon))
         return false;
      if (Double.doubleToLongBits(exponentialDecayRateForFirstMoment) != Double.doubleToLongBits(other.exponentialDecayRateForFirstMoment))
         return false;
      if (Double.doubleToLongBits(exponentialDecayRateForSecondMoment) != Double
            .doubleToLongBits(other.exponentialDecayRateForSecondMoment))
         return false;
      return true;
   }


}
