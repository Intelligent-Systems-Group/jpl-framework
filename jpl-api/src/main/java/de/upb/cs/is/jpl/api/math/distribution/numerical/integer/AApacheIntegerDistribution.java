package de.upb.cs.is.jpl.api.math.distribution.numerical.integer;


import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.random.RandomGenerator;

import de.upb.cs.is.jpl.api.exception.math.UndefinedStatisticException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.ApacheRandomGeneratorManager;


/**
 * This abstract class represents an Apache Math 3 distribution inside the jPL framework. It
 * implements all functionality defined by {@link IIntegerDistribution} using the internal
 * distribution and the according API defined by {@link IntegerDistribution}. All distributions
 * implemented in the jPL framework using an Apache distribution internally should subclass this
 * class.
 * 
 * @author Tanja Tornede
 * 
 * @param <DISTRIBUTION> the type of the internal distribution to be used
 * @param <CONFIG> the generic type extending {@link AIntegerDistributionConfiguration}
 *
 */
public abstract class AApacheIntegerDistribution<DISTRIBUTION extends IntegerDistribution, CONFIG extends AIntegerDistributionConfiguration>
      extends AIntegerDistribution<CONFIG> {


   private DISTRIBUTION apacheDistribution;


   /**
    * This method creates the corresponding internal Apache distribution using the given
    * {@link RandomGenerator}, which should be used internally to answer incoming calls.
    * 
    * Note: This method has to be overwritten in a correct manner in order to allow this
    * distribution to work correctly.
    * 
    * @param randomGenerator the random generator to use in the internal Apache distribution
    * 
    * @return the corresponding internal Apache distribution
    */
   protected abstract DISTRIBUTION createInternalDistribution(RandomGenerator randomGenerator);


   @Override
   public double getVariance() throws UndefinedStatisticException {
      double variance = getInternalDistribution().getNumericalVariance();
      if (Double.isNaN(variance)) {
         throw new UndefinedStatisticException(String.format(ERROR_UNDEFINED_VARIANCE, getClass().getSimpleName()));
      }
      return variance;
   }


   @Override
   public double getExpectedValue() throws UndefinedStatisticException {
      double expectedValue = getInternalDistribution().getNumericalMean();
      if (Double.isNaN(expectedValue)) {
         throw new UndefinedStatisticException(String.format(ERROR_UNDEFINED_EXPECTED_VALUE, getClass().getSimpleName()));
      }
      return expectedValue;
   }


   @Override
   public Integer generateSample() {
      return getInternalDistribution().sample();
   }


   @Override
   public double getProbabilityFor(Integer x) {
      return getInternalDistribution().probability(x);
   }


   @Override
   public double getCumulativeDistributionFunctionValueFor(int x) {
      return getInternalDistribution().cumulativeProbability(x);
   }


   /**
    * Returns the internal distribution being wrapped.
    * 
    * @return the internal distribution being wrapped
    */
   protected DISTRIBUTION getInternalDistribution() {
      if (apacheDistribution == null) {
         apacheDistribution = createInternalDistribution(ApacheRandomGeneratorManager.getApacheRandomGenerator());
      }
      return apacheDistribution;
   }
}
