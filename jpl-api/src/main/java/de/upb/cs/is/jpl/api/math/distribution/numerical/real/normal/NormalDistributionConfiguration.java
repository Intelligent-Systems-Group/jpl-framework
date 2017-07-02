package de.upb.cs.is.jpl.api.math.distribution.numerical.real.normal;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.ADistributionConfiguration;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.ARealDistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * 
 * This class represents the {@link ADistributionConfiguration} for the {@link NormalDistribution}.
 * It should be used in order to define both the mean and the standard deviation parameter of the
 * associated {@link NormalDistribution}.
 * 
 * @author Alexander Hetzer
 *
 */
public class NormalDistributionConfiguration extends ARealDistributionConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILENAME = "normal";

   private static final String PARAMETER_NAME_MU = "mu";
   private static final String PARAMETER_NAME_SIGMA = "sigma";

   /**
    * The expected value of this distribution.
    */
   @SerializedName(PARAMETER_NAME_MU)
   private double mu = Double.MAX_VALUE;

   /**
    * The standard deviation of this distribution;
    */
   @SerializedName(PARAMETER_NAME_SIGMA)
   private double sigma = Double.MAX_VALUE;


   /**
    * Creates a new {@link NormalDistributionConfiguration} whose parameters are initialized with
    * default values.
    */
   public NormalDistributionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILENAME);
   }


   /**
    * Creates a new {@link NormalDistributionConfiguration} initialized with the given {@code mu} as
    * expected value and the given {@code sigma} as the standard deviation.
    * 
    * @param mu the expected value to initialize this configuration with
    * @param sigma the standard deviation to initialize this configuration with. Has to be positive
    *           (&gt;0).
    * @throws ParameterValidationFailedException if the standard deviation is non-positive (&lt;=0)
    */
   public NormalDistributionConfiguration(double mu, double sigma) throws ParameterValidationFailedException {
      this();
      this.mu = mu;
      this.sigma = sigma;
      validateParameters();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (sigma <= 0) {
         throw new ParameterValidationFailedException(ERROR_NON_POSITIVE_SCALING_PARAMETER);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      NormalDistributionConfiguration castedConfiguration = (NormalDistributionConfiguration) configuration;
      if (castedConfiguration.mu < Double.MAX_VALUE) {
         this.mu = castedConfiguration.mu;
      }
      if (castedConfiguration.sigma < Double.MAX_VALUE) {
         this.sigma = castedConfiguration.sigma;
      }
   }


   /**
    * Returns the value of the mean.
    * 
    * @return the value of the mean
    */
   public double getMu() {
      return mu;
   }


   /**
    * Sets the mean to the given value.
    * 
    * @param mu the new value of the mean
    */
   public void setMu(double mu) {
      this.mu = mu;
   }


   /**
    * Returns the standard deviation.
    * 
    * @return the standard deviation
    */
   public double getSigma() {
      return sigma;
   }


   /**
    * Sets the standard deviation to the given value.
    * 
    * @param sigma the new value of the standard deviation
    */
   public void setSigma(double sigma) {
      this.sigma = sigma;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(mu);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(sigma);
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
      NormalDistributionConfiguration other = (NormalDistributionConfiguration) obj;
      if (Double.doubleToLongBits(mu) != Double.doubleToLongBits(other.mu))
         return false;
      if (Double.doubleToLongBits(sigma) != Double.doubleToLongBits(other.sigma))
         return false;
      return true;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_MU + StringUtils.COLON + mu + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + PARAMETER_NAME_SIGMA
            + StringUtils.COLON + sigma;
   }


}
