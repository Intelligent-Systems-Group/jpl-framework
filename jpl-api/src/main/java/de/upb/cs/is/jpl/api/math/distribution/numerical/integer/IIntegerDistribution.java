package de.upb.cs.is.jpl.api.math.distribution.numerical.integer;


import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.INumericalDistribution;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.bernoulli.BernoulliDistribution;


/**
 * This interface defines the common functionality which all kinds of integer distributions, i.e.
 * the {@link BernoulliDistribution}, need to support.
 * 
 * @author Tanja Tornede
 *
 */
public interface IIntegerDistribution extends INumericalDistribution<Integer> {

   /**
    * For a random variable {@code X} distributed according to this distribution,
    * {@code P(lowerBound <= X <= upperBound} is returned.
    * 
    * @param lowerBound the lower bound of the probability interval
    * @param upperBound the upper bound of the probability interval
    * @return {@code P(lowerBound <= X <= upperBound}
    * @throws DistributionException if the probability cannot be computed
    */
   public double getProbabilityFor(int lowerBound, int upperBound) throws DistributionException;


   /**
    * Returns the value of the cumulative distribution function for the given argument, i.e. F(x).
    * 
    * @param x the argument for which the cumulative distribution function should be computed
    * @return the value of the cumulative distribution function for the given argument
    */
   public double getCumulativeDistributionFunctionValueFor(int x);
}
