package de.upb.cs.is.jpl.api.math.distribution.numerical.real.normal;


import org.apache.commons.math3.random.RandomGenerator;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.AApacheRealDistribution;


/**
 * This class represents the implementation of the normal distribution.
 * 
 * @author Alexander Hetzer
 *
 */
public class NormalDistribution
      extends AApacheRealDistribution<org.apache.commons.math3.distribution.NormalDistribution, NormalDistributionConfiguration> {


   /**
    * Creates a new {@link NormalDistribution} whose parameters are initialized with default values.
    */
   public NormalDistribution() {
      super();
   }


   /**
    * Creates a new {@link NormalDistribution} with the given {@code mu} as the expected value and
    * the given {@code sigma} as a standard deviation.
    * 
    * @param mu the expected value of this distribution
    * @param sigma the standard deviation of this distribution. Has to be positive (&gt;0).
    * @throws ParameterValidationFailedException if the standard deviation {@code sigma} is
    *            non-positive (&lt;=0)
    */
   public NormalDistribution(double mu, double sigma) throws ParameterValidationFailedException {
      this();
      setDistributionConfiguration(new NormalDistributionConfiguration(mu, sigma));
   }


   @Override
   protected org.apache.commons.math3.distribution.NormalDistribution createInternalDistribution(RandomGenerator randomGenerator) {
      double mu = getDistributionConfiguration().getMu();
      double sigma = getDistributionConfiguration().getSigma();
      return new org.apache.commons.math3.distribution.NormalDistribution(randomGenerator, mu, sigma);
   }


   @Override
   public NormalDistributionConfiguration createDefaultDistributionConfiguration() {
      NormalDistributionConfiguration defaultConfiguration = new NormalDistributionConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }

}
