package de.upb.cs.is.jpl.api.math.distribution.numerical;


import de.upb.cs.is.jpl.api.math.distribution.ADistribution;


/**
 * This abstract class is the base class for all types of numerical distributions.
 * 
 * @author Tanja Tornede
 *
 * @param <SPACE> defines the type of elements forming the space which this distribution is defined
 *           over.
 * @param <CONFIG> the generic type extending {@link ANumericalDistributionConfiguration}
 */
public abstract class ANumericalDistribution<SPACE extends Number, CONFIG extends ANumericalDistributionConfiguration>
      extends ADistribution<SPACE, CONFIG> implements INumericalDistribution<SPACE> {

   protected static final String ERROR_LOWER_BOUND_BIGGER_UPPER_BOUND = "Lower bound %s cannot be bigger than upper bound %s.";

   protected static final String ERROR_UNDEFINED_VARIANCE = "The variance of the  '%s' is undefined.";
   protected static final String ERROR_UNDEFINED_EXPECTED_VALUE = "The expected value of the '%s' is undefined.";


   /**
    * Creates a new {@link ANumericalDistribution}.
    */
   public ANumericalDistribution() {
      super();
   }

}
