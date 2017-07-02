package de.upb.cs.is.jpl.api.math.distribution.numerical.real.beta;


import org.apache.commons.math3.random.RandomGenerator;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.AApacheRealDistribution;


/**
 * This class represents the implementation of the Beta distribution.
 * 
 * @author Tanja Tornede
 *
 */
public class BetaDistribution
      extends AApacheRealDistribution<org.apache.commons.math3.distribution.BetaDistribution, BetaDistributionConfiguration> {

   /**
    * Creates a new {@link BetaDistribution} with the given {@code alpha} and {@code beta} as shape
    * parameter.
    */
   public BetaDistribution() {
      super();
   }


   /**
    * Creates a new {@link BetaDistribution} with the given {@code alpha} and {@code beta} as shape
    * parameter.
    * 
    * @param alpha the first shape parameter, has to be &gt; 0
    * @param beta the second shape parameter, has to be &gt; 0
    * @throws ParameterValidationFailedException if one of the parameters does not fit its
    *            constraints
    */
   public BetaDistribution(double alpha, double beta) throws ParameterValidationFailedException {
      this();
      setDistributionConfiguration(new BetaDistributionConfiguration(alpha, beta));
   }


   @Override
   protected org.apache.commons.math3.distribution.BetaDistribution createInternalDistribution(RandomGenerator randomGenerator) {
      return new org.apache.commons.math3.distribution.BetaDistribution(randomGenerator, configuration.getAlpha(), configuration.getBeta());
   }


   @Override
   public BetaDistributionConfiguration createDefaultDistributionConfiguration() {
      BetaDistributionConfiguration defaultConfiguration = new BetaDistributionConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }

}
