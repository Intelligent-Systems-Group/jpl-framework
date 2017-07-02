package de.upb.cs.is.jpl.api.math.distribution.numerical.integer.binomial;


import org.apache.commons.math3.random.RandomGenerator;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.AApacheIntegerDistribution;


/**
 * This class represents the implementation of the Binomial distribution.
 * 
 * @author Tanja Tornede
 *
 */
public class BinomialDistribution
      extends AApacheIntegerDistribution<org.apache.commons.math3.distribution.BinomialDistribution, BinomialDistributionConfiguration> {

   /**
    * Creates a new {@link BinomialDistribution} with default parameters.
    */
   public BinomialDistribution() {
      super();
   }


   /**
    * Creates a new {@link BinomialDistribution} with the given parameters.
    * 
    * @param n the number of trials: {@code n} &gt;= 0
    * @param p the success probability in each trial: 0 &lt;= {@code p} &lt;= 1
    * @throws ParameterValidationFailedException if one of the parameters does not fit the
    *            conditions
    */
   public BinomialDistribution(int n, double p) throws ParameterValidationFailedException {
      this();
      setDistributionConfiguration(new BinomialDistributionConfiguration(n, p));
   }


   @Override
   protected org.apache.commons.math3.distribution.BinomialDistribution createInternalDistribution(RandomGenerator randomGenerator) {
      return new org.apache.commons.math3.distribution.BinomialDistribution(randomGenerator, configuration.getN(), configuration.getP());
   }


   @Override
   public BinomialDistributionConfiguration createDefaultDistributionConfiguration() {
      BinomialDistributionConfiguration defaultConfiguration = new BinomialDistributionConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }
}
