package de.upb.cs.is.jpl.api.math.distribution.numerical.real;


import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.ANumericalDistribution;


/**
 * This abstract class is the base class for all types of distributions on the real numbers.
 * 
 * @author Alexander Hetzer
 * @author Tanja Tornede
 * 
 * @param <CONFIG> the generic type extending {@link ARealDistributionConfiguration}
 */
public abstract class ARealDistribution<CONFIG extends ARealDistributionConfiguration> extends ANumericalDistribution<Double, CONFIG>
      implements IRealDistribution {

   protected static final String ERROR_NON_POSITIVE_SCALING_PARAMETER = "The scaling parameter has to be positive.";


   /**
    * Creates a new {@link ARealDistribution}.
    */
   public ARealDistribution() {
      super();
   }


   @Override
   public double getProbabilityFor(double lowerBound, double upperBound) throws DistributionException {
      if (lowerBound > upperBound) {
         throw new DistributionException(String.format(ERROR_LOWER_BOUND_BIGGER_UPPER_BOUND, lowerBound, upperBound));
      }
      return getCumulativeDistributionFunctionValueFor(upperBound) - getCumulativeDistributionFunctionValueFor(lowerBound);
   }
}
