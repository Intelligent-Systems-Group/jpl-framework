package de.upb.cs.is.jpl.api.math.distribution.numerical.integer.bernoulli;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.AIntegerDistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for a implementation of {@link BernoulliDistribution}.
 * 
 * @author Tanja Tornede
 *
 */
public class BernoulliDistributionConfiguration extends AIntegerDistributionConfiguration {

   private static final String ERROR_PARAMETER_NOT_PROBABILITY = "The given value has to be: 0 < p < 1";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "bernoulli";

   private static final String PARAMETER_P = "p";

   @SerializedName(PARAMETER_P)
   private double p = Double.MAX_VALUE;


   /**
    * Creates a default configuration for {@link BernoulliDistributionConfiguration}.
    */
   public BernoulliDistributionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   /**
    * Creates a {@link BernoulliDistributionConfiguration} with the given parameter.
    * 
    * @param p the mean of the distribution, has to be: 0 &lt; {@code p} &lt; 1
    * @throws ParameterValidationFailedException if validating the parameters went wrong
    */
   public BernoulliDistributionConfiguration(double p) throws ParameterValidationFailedException {
      this();
      this.p = p;
      validateParameters();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (p <= 0 || p >= 1) {
         throw new ParameterValidationFailedException(ERROR_PARAMETER_NOT_PROBABILITY);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      BernoulliDistributionConfiguration castedConfiguration = (BernoulliDistributionConfiguration) configuration;
      copyParameterP(castedConfiguration);
   }


   private void copyParameterP(BernoulliDistributionConfiguration configuration) {
      if (configuration.p < Double.MAX_VALUE) {
         this.p = configuration.p;
      }
   }


   /**
    * Sets the parameter p used in the {@link BernoulliDistribution}.
    * 
    * @param p the parameter p to be used in in the {@link BernoulliDistribution}
    */
   public void setP(double p) {
      this.p = p;
   }


   /**
    * Returns the parameter p used in the {@link BernoulliDistribution}.
    * 
    * @return the parameter p used in the {@link BernoulliDistribution}
    */
   public double getP() {
      return p;
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
      BernoulliDistributionConfiguration other = (BernoulliDistributionConfiguration) obj;
      if (Double.doubleToLongBits(p) != Double.doubleToLongBits(other.p)) {
         return false;
      }
      return true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(p);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      return result;
   }


   @Override
   public String toString() {
      return PARAMETER_P + StringUtils.COLON + p;
   }

}
