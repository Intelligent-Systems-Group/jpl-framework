package de.upb.cs.is.jpl.api.math.distribution.numerical.real.chisquared;


import org.apache.commons.math3.random.RandomGenerator;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.AApacheRealDistribution;


/**
 * This class represents the implementation of the chi-squared distribution.
 * 
 * @author Alexander Hetzer
 *
 */
public class ChiSquaredDistribution
      extends AApacheRealDistribution<org.apache.commons.math3.distribution.ChiSquaredDistribution, ChiSquaredDistributionConfiguration> {

   /**
    * Creates a new {@link ChiSquaredDistribution} whose parameters are initialized with default
    * values.
    */
   public ChiSquaredDistribution() {
      super();
   }


   /**
    * Creates a {@link ChiSquaredDistribution} with the given degrees of freedom.
    * 
    * @param degreesOfFreedom the degrees of freedom of this distribution. Has to be positive
    *           (&gt;0).
    * @throws ParameterValidationFailedException if the given amount of degrees of freedom is not
    *            positive (&lt;=0)
    */
   public ChiSquaredDistribution(int degreesOfFreedom) throws ParameterValidationFailedException {
      this();
      setDistributionConfiguration(new ChiSquaredDistributionConfiguration(degreesOfFreedom));
   }


   @Override
   protected org.apache.commons.math3.distribution.ChiSquaredDistribution createInternalDistribution(RandomGenerator randomGenerator) {
      int degreesOfFreedom = getDistributionConfiguration().getDegreesOfFreedom();
      return new org.apache.commons.math3.distribution.ChiSquaredDistribution(randomGenerator, degreesOfFreedom);
   }


   @Override
   public ChiSquaredDistributionConfiguration createDefaultDistributionConfiguration() {
      ChiSquaredDistributionConfiguration defaultConfiguration = new ChiSquaredDistributionConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }

}
