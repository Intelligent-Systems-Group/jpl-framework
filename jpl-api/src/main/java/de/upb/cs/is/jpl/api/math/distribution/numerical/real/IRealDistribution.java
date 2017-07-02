package de.upb.cs.is.jpl.api.math.distribution.numerical.real;


import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.INumericalDistribution;


/**
 * This interface defines the functionality, which should be supported by all distributions on the
 * real numbers inside the jPL framework.
 * 
 * @author Alexander Hetzer
 *
 */
public interface IRealDistribution extends INumericalDistribution<Double> {

   /**
    * For a random variable {@code X} distributed according to this distribution,
    * {@code P(lowerBound <= X <= upperBound} is returned.
    * 
    * @param lowerBound the lower bound of the probability interval
    * @param upperBound the upper bound of the probability interval
    * @return {@code P(lowerBound <= X <= upperBound}
    * @throws DistributionException if the probability cannot be computed
    */
   public double getProbabilityFor(double lowerBound, double upperBound) throws DistributionException;


   /**
    * Returns the value of the cumulative distribution function for the given argument, i.e. F(x).
    * 
    * @param x the argument for which the cumulative distribution function should be computed
    * @return the value of the cumulative distribution function for the given argument
    */
   public double getCumulativeDistributionFunctionValueFor(double x);

}
