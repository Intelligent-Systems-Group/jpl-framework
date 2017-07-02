package de.upb.cs.is.jpl.api.math.distribution.numerical.real.beta;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.bernoulli.BernoulliDistribution;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.ARealDistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for a implementation of {@link BetaDistribution}.
 * 
 * @author Tanja Tornede
 *
 */
public class BetaDistributionConfiguration extends ARealDistributionConfiguration {

   private static final String ERROR_ALPHA_NOT_POSITIVE = "The given alpha value has to be positive!";
   private static final String ERROR_BETA_NOT_POSITIVE = "The given beta value has to be positive!";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "beta";

   private static final String PARAMETER_ALPHA_STRING = "alpha";
   @SerializedName(PARAMETER_ALPHA_STRING)
   private double alpha = Double.MAX_VALUE;

   private static final String PARAMETER_BETA_STRING = "beta";
   @SerializedName(PARAMETER_BETA_STRING)
   private double beta = Double.MAX_VALUE;


   /**
    * Creates a default configuration for {@link BetaDistributionConfiguration}.
    */
   public BetaDistributionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   /**
    * Creates a {@link BetaDistributionConfiguration} with the given parameters.
    * 
    * @param alpha the first shape parameter, has to be &gt; 0
    * @param beta the second shape parameter, has to be &gt; 0
    * @throws ParameterValidationFailedException if the validation of the parameters went wrong
    */
   public BetaDistributionConfiguration(double alpha, double beta) throws ParameterValidationFailedException {
      this();
      this.alpha = alpha;
      this.beta = beta;
      validateParameters();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (alpha <= 0) {
         throw new ParameterValidationFailedException(ERROR_ALPHA_NOT_POSITIVE);
      }
      if (beta <= 0) {
         throw new ParameterValidationFailedException(ERROR_BETA_NOT_POSITIVE);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      BetaDistributionConfiguration castedConfiguration = (BetaDistributionConfiguration) configuration;
      copyParameterAlpha(castedConfiguration);
      copyParameterBeta(castedConfiguration);
   }


   private void copyParameterAlpha(BetaDistributionConfiguration configuration) {
      if (configuration.alpha < Double.MAX_VALUE) {
         this.alpha = configuration.alpha;
      }
   }


   private void copyParameterBeta(BetaDistributionConfiguration configuration) {
      if (configuration.beta < Double.MAX_VALUE) {
         this.beta = configuration.beta;
      }
   }


   /**
    * Sets the parameter {@code alpha} used in the {@link BernoulliDistribution}.
    * 
    * @param alpha the parameter {@code alpha} to be used in in the {@link BernoulliDistribution}
    */
   public void setAlpha(double alpha) {
      this.alpha = alpha;
   }


   /**
    * Returns the parameter {@code alpha} used in the {@link BernoulliDistribution}.
    * 
    * @return the parameter {@code alpha} used in the {@link BernoulliDistribution}
    */
   public double getAlpha() {
      return alpha;
   }


   /**
    * Sets the parameter {@code beta} used in the {@link BernoulliDistribution}.
    * 
    * @param beta the parameter {@code beta} to be used in in the {@link BernoulliDistribution}
    */
   public void setBeta(double beta) {
      this.beta = beta;
   }


   /**
    * Returns the parameter {@code beta} used in the {@link BernoulliDistribution}.
    * 
    * @return the parameter {@code beta} used in the {@link BernoulliDistribution}
    */
   public double getBeta() {
      return beta;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(alpha);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(beta);
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
      BetaDistributionConfiguration other = (BetaDistributionConfiguration) obj;
      if (Double.doubleToLongBits(alpha) != Double.doubleToLongBits(other.alpha)) {
         return false;
      }
      if (Double.doubleToLongBits(beta) != Double.doubleToLongBits(other.beta)) {
         return false;
      }
      return true;
   }


   @Override
   public String toString() {
      return PARAMETER_ALPHA_STRING + StringUtils.COLON + alpha + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + PARAMETER_BETA_STRING
            + StringUtils.COLON + beta;
   }
}
