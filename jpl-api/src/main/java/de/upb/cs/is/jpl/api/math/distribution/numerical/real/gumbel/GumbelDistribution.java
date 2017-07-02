package de.upb.cs.is.jpl.api.math.distribution.numerical.real.gumbel;


import org.apache.commons.math3.random.RandomGenerator;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.AApacheRealDistribution;


/**
 * This class represents the implementation of the gumbel distribution.
 * 
 * @author Alexander Hetzer
 *
 */
public class GumbelDistribution
      extends AApacheRealDistribution<org.apache.commons.math3.distribution.GumbelDistribution, GumbelDistributionConfiguration> {

   /**
    * Creates a new {@link GumbelDistribution} whose parameters are initialized with default values.
    */
   public GumbelDistribution() {
      super();
   }


   /**
    * Creates a new {@link GumbelDistribution} with the given {@code mu} as a location parameter and
    * the given {@code beta} as a scaling parameter.
    * 
    * @param mu the location parameter of this distribution
    * @param beta the scaling parameter of this distribution. Has to be positive (&gt;0).
    * @throws ParameterValidationFailedException if the scaling parameter {@code beta} is
    *            non-positive (&lt;=0)
    */
   public GumbelDistribution(double mu, double beta) throws ParameterValidationFailedException {
      this();
      setDistributionConfiguration(new GumbelDistributionConfiguration(mu, beta));
   }


   @Override
   protected org.apache.commons.math3.distribution.GumbelDistribution createInternalDistribution(RandomGenerator randomGenerator) {
      double mu = getDistributionConfiguration().getMu();
      double beta = getDistributionConfiguration().getBeta();
      return new org.apache.commons.math3.distribution.GumbelDistribution(randomGenerator, mu, beta);
   }


   @Override
   public GumbelDistributionConfiguration createDefaultDistributionConfiguration() {
      GumbelDistributionConfiguration defaultConfiguration = new GumbelDistributionConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }


}
