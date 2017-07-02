package de.upb.cs.is.jpl.api.math.distribution.numerical.integer.bernoulli;


import java.util.Random;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.AIntegerDistribution;


/**
 * This class represents the implementation of the Bernoulli distribution.
 * 
 * @author Tanja Tornede
 *
 */
public class BernoulliDistribution extends AIntegerDistribution<BernoulliDistributionConfiguration> {

   private static final String ERROR_PARAMETER_NOT_PROBABILITY = "The given value has to be: 0 < p < 1";


   /**
    * Creates a new {@link BernoulliDistribution} with the default parameter.
    */
   public BernoulliDistribution() {
      super();
   }


   /**
    * Creates a new {@link BernoulliDistribution} with the given {@code p} as mean.
    * 
    * @param p the mean of the distribution, has to be: 0 &lt; {@code p} &lt; 1
    * @throws ParameterValidationFailedException if the parameter does not fit the conditions
    */
   public BernoulliDistribution(double p) throws ParameterValidationFailedException {
      this();
      setDistributionConfiguration(new BernoulliDistributionConfiguration(p));
   }


   @Override
   public BernoulliDistributionConfiguration createDefaultDistributionConfiguration() {
      BernoulliDistributionConfiguration defaultConfiguration = new BernoulliDistributionConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }


   @Override
   public double getVariance() {
      return configuration.getP() * (1 - configuration.getP());
   }


   @Override
   public double getExpectedValue() {
      return configuration.getP();
   }


   /**
    * {@inheritDoc}
    * <p>
    * This method returns {@code 1} if a randomly generated value is &gt; {@code (1-p)}, otherwise
    * if this value is &lt;= {@code (1-p)} it returns {@code 0}.
    */
   @Override
   public Integer generateSample() {
      Random randomGenerator = RandomGenerator.getRNG();
      double randomValue = randomGenerator.nextDouble();
      if (randomValue > (1 - configuration.getP())) {
         return 1;
      } else {
         return 0;
      }
   }


   @Override
   public double getProbabilityFor(Integer x) throws DistributionException {
      if (x < 0 || x > 1) {
         throw new DistributionException(ERROR_PARAMETER_NOT_PROBABILITY);
      }
      if (x == 1) {
         return configuration.getP();
      } else {
         return 1 - configuration.getP();
      }
   }


   @Override
   public double getCumulativeDistributionFunctionValueFor(int x) {
      if (x < 0) {
         return 0;
      } else if (x < 1) {
         return 1 - configuration.getP();
      } else {
         return configuration.getP();
      }
   }

}
