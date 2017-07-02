package de.upb.cs.is.jpl.api.math.distribution.numerical.integer;


import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.ANumericalDistribution;


/**
 * This abstract class is the base class for all types of integer distributions.
 * 
 * @author Tanja Tornede
 * 
 * @param <CONFIG> the generic type extending {@link AIntegerDistributionConfiguration}
 *
 */
public abstract class AIntegerDistribution<CONFIG extends AIntegerDistributionConfiguration> extends ANumericalDistribution<Integer, CONFIG>
      implements IIntegerDistribution {

   /**
    * Creates a new {@link AIntegerDistribution}.
    */
   public AIntegerDistribution() {
      super();
   }


   @Override
   public double getProbabilityFor(int lowerBound, int upperBound) throws DistributionException {
      if (lowerBound > upperBound) {
         throw new DistributionException(String.format(ERROR_LOWER_BOUND_BIGGER_UPPER_BOUND, lowerBound, upperBound));
      }
      return getCumulativeDistributionFunctionValueFor(upperBound) - getCumulativeDistributionFunctionValueFor(lowerBound);
   }
}
