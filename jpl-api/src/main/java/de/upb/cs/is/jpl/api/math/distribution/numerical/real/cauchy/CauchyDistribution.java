package de.upb.cs.is.jpl.api.math.distribution.numerical.real.cauchy;


import org.apache.commons.math3.random.RandomGenerator;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.AApacheRealDistribution;


/**
 * This class represents the implementation of the Cauchy distribution.
 * 
 * @author Alexander Hetzer
 *
 */
public class CauchyDistribution
      extends AApacheRealDistribution<org.apache.commons.math3.distribution.CauchyDistribution, CauchyDistributionConfiguration> {

   /**
    * Creates a new {@link CauchyDistribution} whose parameters are initialized with default values.
    */
   public CauchyDistribution() {
      super();
   }


   /**
    * Creates a new {@link CauchyDistribution} with the given {@code mu} as a location parameter and
    * the given {@code lambda} as a scaling parameter.
    * 
    * @param mu the location parameter of this distribution
    * @param lambda the scaling parameter of this distribution. Has to be positive (&gt;0).
    * @throws ParameterValidationFailedException if the scaling parameter {@code lambda} is
    *            non-positive (&lt;=0)
    */
   public CauchyDistribution(double mu, double lambda) throws ParameterValidationFailedException {
      this();
      setDistributionConfiguration(new CauchyDistributionConfiguration(mu, lambda));
   }


   @Override
   protected org.apache.commons.math3.distribution.CauchyDistribution createInternalDistribution(RandomGenerator randomGenerator) {
      double mu = getDistributionConfiguration().getMu();
      double lambda = getDistributionConfiguration().getLambda();
      return new org.apache.commons.math3.distribution.CauchyDistribution(randomGenerator, mu, lambda);
   }


   @Override
   public CauchyDistributionConfiguration createDefaultDistributionConfiguration() {
      CauchyDistributionConfiguration defaultCauchyDistributionConfiguration = new CauchyDistributionConfiguration();
      defaultCauchyDistributionConfiguration.initializeDefaultConfiguration();
      return defaultCauchyDistributionConfiguration;
   }

}
