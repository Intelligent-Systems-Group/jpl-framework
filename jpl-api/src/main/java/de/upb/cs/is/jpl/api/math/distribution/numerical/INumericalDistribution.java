package de.upb.cs.is.jpl.api.math.distribution.numerical;


import de.upb.cs.is.jpl.api.exception.math.UndefinedStatisticException;
import de.upb.cs.is.jpl.api.math.distribution.IDistribution;


/**
 * This abstract class is the base class for all types of numerical distributions.
 * 
 * @author Tanja Tornede
 *
 * @param <SPACE> Defines the type of elements forming the space which this distribution is defined
 *           over.
 */
public interface INumericalDistribution<SPACE extends Number> extends IDistribution<SPACE> {

   /**
    * Returns the variance of this distribution.
    * 
    * @return the variance of this distribution
    * @throws UndefinedStatisticException if the variance is not defined
    */
   public double getVariance() throws UndefinedStatisticException;


   /**
    * Returns the expected value of this distribution.
    * 
    * @return the expected value of this distribution
    * @throws UndefinedStatisticException if the expected value is not defined
    */
   public double getExpectedValue() throws UndefinedStatisticException;

}
