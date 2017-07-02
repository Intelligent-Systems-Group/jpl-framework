package de.upb.cs.is.jpl.api.math.distribution.special;


import de.upb.cs.is.jpl.api.math.distribution.IDistribution;


/**
 * This interface defines the common functionality which all kinds of special distributions, i.e.
 * distribution on a different space than the real numbers, need to support.
 * 
 * @author Alexander Hetzer
 * 
 * @param <SPACE> Defines the type of elements forming the space which this distribution is defined
 *           over.
 *
 */
public interface ISpecialDistribution<SPACE> extends IDistribution<SPACE> {

}
