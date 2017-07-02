package de.upb.cs.is.jpl.api.math.distribution.numerical.real.gumbel;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.ADistributionConfiguration;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.ARealDistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the {@link ADistributionConfiguration} for the {@link GumbelDistribution}.
 * It should be used in order to define both the location and the scaling parameter of the
 * associated {@link GumbelDistribution}.
 * 
 * @author Alexander Hetzer
 *
 */
public class GumbelDistributionConfiguration extends ARealDistributionConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILENAME = "gumbel";

   private static final String PARAMETER_NAME_MU = "mu";
   private static final String PARAMETER_NAME_BETA = "beta";

   /**
    * The location parameter of this distribution.
    */
   @SerializedName(PARAMETER_NAME_MU)
   private double mu = Double.MAX_VALUE;

   /**
    * The scaling parameter of this distribution.
    */
   @SerializedName(PARAMETER_NAME_BETA)
   private double beta = Double.MAX_VALUE;


   /**
    * Creates a new {@link GumbelDistributionConfiguration} whose parameters are initialized with
    * default values.
    */
   public GumbelDistributionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILENAME);
   }


   /**
    * Creates a new {@link GumbelDistributionConfiguration} with the given {@code mu} as a location
    * parameter and the given {@code beta} as a scaling parameter.
    * 
    * @param mu the location parameter of this distribution
    * @param beta the scaling parameter of this distribution. Has to be positive (&gt;0).
    * @throws ParameterValidationFailedException if the scaling parameter {@code beta} is
    *            non-positive (&lt;=0)
    */
   public GumbelDistributionConfiguration(double mu, double beta) throws ParameterValidationFailedException {
      this();
      this.mu = mu;
      this.beta = beta;
      validateParameters();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (beta <= 0) {
         throw new ParameterValidationFailedException(ERROR_NON_POSITIVE_SCALING_PARAMETER);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      GumbelDistributionConfiguration castedConfiguration = (GumbelDistributionConfiguration) configuration;
      if (castedConfiguration.beta < Double.MAX_VALUE) {
         this.beta = castedConfiguration.beta;
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
    * Returns the value of the scaling parameter {@code beta}.
    * 
    * @return the value of the scaling parameter {@code beta}
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Set the scaling parameter {@code beta} to the given value.
    * 
    * @param beta the new value of the scaling parameter {@code beta}
    */
   public void setBeta(double beta) {
      this.beta = beta;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(beta);
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
      GumbelDistributionConfiguration other = (GumbelDistributionConfiguration) obj;
      if (Double.doubleToLongBits(beta) != Double.doubleToLongBits(other.beta))
         return false;
      if (Double.doubleToLongBits(mu) != Double.doubleToLongBits(other.mu))
         return false;
      return true;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_MU + StringUtils.COLON + mu + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + PARAMETER_NAME_BETA
            + StringUtils.COLON + beta;
   }


}
