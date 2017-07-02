package de.upb.cs.is.jpl.api.math.distribution.numerical.real.cauchy;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.ADistributionConfiguration;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.ARealDistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the {@link ADistributionConfiguration} for the {@link CauchyDistribution}.
 * It should be used in order to define both the location and the scaling parameter of the
 * associated {@link CauchyDistribution}.
 * 
 * @author Alexander Hetzer
 *
 */
public class CauchyDistributionConfiguration extends ARealDistributionConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILENAME = "cauchy";

   private static final String PARAMETER_NAME_MU = "mu";
   private static final String PARAMETER_NAME_LAMBDA = "lambda";

   /**
    * The location parameter of this distribution.
    */
   @SerializedName(PARAMETER_NAME_MU)
   private double mu = Double.MAX_VALUE;

   /**
    * The scaling parameter of this distribution.
    */
   @SerializedName(PARAMETER_NAME_LAMBDA)
   private double lambda = Double.MAX_VALUE;


   /**
    * Creates a {@link CauchyDistributionConfiguration} initialized with the default values for both
    * the location and the scaling parameter.
    */
   public CauchyDistributionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILENAME);
   }


   /**
    * Creates a new {@link CauchyDistributionConfiguration} with the given {@code mu} as a location
    * parameter and the given {@code lambda} as a scaling parameter.
    * 
    * @param mu the location parameter of this distribution
    * @param lambda the scaling parameter of this distribution. Has to be positive (&gt;0).
    * @throws ParameterValidationFailedException if the scaling parameter {@code lambda} is
    *            non-positive (&lt;=0)
    */
   public CauchyDistributionConfiguration(double mu, double lambda) throws ParameterValidationFailedException {
      this();
      this.mu = mu;
      this.lambda = lambda;
      validateParameters();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (lambda <= 0) {
         throw new ParameterValidationFailedException(ERROR_NON_POSITIVE_SCALING_PARAMETER);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      CauchyDistributionConfiguration castedConfiguration = (CauchyDistributionConfiguration) configuration;
      if (castedConfiguration.lambda < Double.MAX_VALUE) {
         this.lambda = castedConfiguration.lambda;
      }
      if (castedConfiguration.mu < Double.MAX_VALUE) {
         this.mu = castedConfiguration.mu;
      }

   }


   /**
    * Returns the value of the location parameter {@code mu}.
    * 
    * @return the value of the location parameter {@code mu}
    */
   public double getMu() {
      return mu;
   }


   /**
    * Set the location parameter {@code mu} to the given value.
    * 
    * @param mu the new value of the location paramete {@code mu}
    */
   public void setMu(double mu) {
      this.mu = mu;
   }


   /**
    * Returns the value of the scaling parameter {@code lambda}.
    * 
    * @return the value of the scaling parameter {@code lambda}
    */
   public double getLambda() {
      return lambda;
   }


   /**
    * Set the scaling parameter {@code lambda} to the given value.
    * 
    * @param lambda the new value of the scaling parameter {@code lambda}
    */
   public void setLambda(double lambda) {
      this.lambda = lambda;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(lambda);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(mu);
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
      CauchyDistributionConfiguration other = (CauchyDistributionConfiguration) obj;
      if (Double.doubleToLongBits(lambda) != Double.doubleToLongBits(other.lambda))
         return false;
      if (Double.doubleToLongBits(mu) != Double.doubleToLongBits(other.mu))
         return false;
      return true;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_MU + StringUtils.COLON + mu + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + PARAMETER_NAME_LAMBDA
            + StringUtils.COLON + lambda;
   }


}
