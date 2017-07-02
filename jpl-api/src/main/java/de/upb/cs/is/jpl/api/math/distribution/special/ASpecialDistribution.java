package de.upb.cs.is.jpl.api.math.distribution.special;


import de.upb.cs.is.jpl.api.math.distribution.ADistribution;
import de.upb.cs.is.jpl.api.math.distribution.IDistribution;


/**
 * This abstract class is the base class for all types of special distributions.
 * 
 * @author Alexander Hetzer
 *
 * @param <SPACE> Defines the type of elements forming the space which this distribution is defined
 *           over.
 * @param <CONFIG> Defines the type of configuration linked to this kind of {@link IDistribution}.
 */
public abstract class ASpecialDistribution<SPACE, CONFIG extends ASpecialDistributionConfiguration> extends ADistribution<SPACE, CONFIG>
      implements ISpecialDistribution<SPACE> {

}
