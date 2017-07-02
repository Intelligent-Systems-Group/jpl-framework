package de.upb.cs.is.jpl.api.math.distribution.numerical.integer.binomial;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.AIntegerDistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for a implementation of {@link BinomialDistribution}.
 * 
 * @author Tanja Tornede
 *
 */
public class BinomialDistributionConfiguration extends AIntegerDistributionConfiguration {

   private static final String ERROR_PARAMETER_NOT_POSITIVE_INTEGER = "The given value for the number of trials has to be: 0 <= n";
   private static final String ERROR_PARAMETER_NOT_PROBABILITY = "The given value for the probability has to be: 0 <= p <= 1";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "binomial";

   private static final String PARAMETER_N = "n";
   @SerializedName(PARAMETER_N)
   private int n = Integer.MAX_VALUE;

   private static final String PARAMETER_P = "p";
   @SerializedName(PARAMETER_P)
   private double p = Double.MAX_VALUE;


   /**
    * Creates a default configuration for {@link BinomialDistributionConfiguration}.
    */
   public BinomialDistributionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   /**
    * Creates a {@link BinomialDistributionConfiguration} with the given parameters.
    * 
    * @param n the number of trials: {@code n} &gt;= 0
    * @param p the success probability in each trial: 0 &lt;= {@code p} &lt;= 1
    * @throws ParameterValidationFailedException if validating the parameters went wrong
    */
   public BinomialDistributionConfiguration(int n, double p) throws ParameterValidationFailedException {
      this();
      this.n = n;
      this.p = p;
      validateParameters();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (n < 0) {
         throw new ParameterValidationFailedException(ERROR_PARAMETER_NOT_POSITIVE_INTEGER);
      }
      if (p < 0 || p > 1) {
         throw new ParameterValidationFailedException(ERROR_PARAMETER_NOT_PROBABILITY);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      BinomialDistributionConfiguration castedConfiguration = (BinomialDistributionConfiguration) configuration;
      copyParameterN(castedConfiguration);
      copyParameterP(castedConfiguration);
   }


   private void copyParameterN(BinomialDistributionConfiguration configuration) {
      if (configuration.n < Integer.MAX_VALUE) {
         this.n = configuration.n;
      }
   }


   private void copyParameterP(BinomialDistributionConfiguration configuration) {
      if (configuration.p < Double.MAX_VALUE) {
         this.p = configuration.p;
      }
   }


   /**
    * Sets the parameter n used in the {@link BinomialDistribution}.
    * 
    * @param n the parameter p to be used in in the {@link BinomialDistribution}
    */
   public void setN(int n) {
      this.n = n;
   }


   /**
    * Returns the parameter n used in the {@link BinomialDistribution}.
    * 
    * @return the parameter n used in the {@link BinomialDistribution}
    */
   public int getN() {
      return n;
   }


   /**
    * Sets the parameter p used in the {@link BinomialDistribution}.
    * 
    * @param p the parameter p to be used in in the {@link BinomialDistribution}
    */
   public void setP(double p) {
      this.p = p;
   }


   /**
    * Returns the parameter p used in the {@link BinomialDistribution}.
    * 
    * @return the parameter p used in the {@link BinomialDistribution}
    */
   public double getP() {
      return p;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + n;
      long temp;
      temp = Double.doubleToLongBits(p);
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
      BinomialDistributionConfiguration other = (BinomialDistributionConfiguration) obj;
      if (n != other.n) {
         return false;
      }
      if (Double.doubleToLongBits(p) != Double.doubleToLongBits(other.p)) {
         return false;
      }
      return true;
   }


   @Override
   public String toString() {
      return PARAMETER_P + StringUtils.COLON + p + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + PARAMETER_N + StringUtils.COLON + n;
   }


}
