package de.upb.cs.is.jpl.api.algorithm.distributionfitting;


import de.upb.cs.is.jpl.api.algorithm.IAlgorithm;
import de.upb.cs.is.jpl.api.exception.algorithm.FittingDistributionFailedException;
import de.upb.cs.is.jpl.api.math.distribution.IDistribution;
import de.upb.cs.is.jpl.api.math.distribution.IDistributionSampleSet;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.IIntegerDistribution;


/**
 * Interface for a distribution fitting algorithm specifying the required behavior. A distribution
 * fitting algorithm should be able to fit a given sample set.
 * 
 * @author Tanja Tornede
 * 
 * @param <SPACE> the space (domain) of the {@link IDistributionSampleSet} of this distribution,
 *           e.g. Integer for {@link IIntegerDistribution}s
 *
 */
public interface IDistributionFittingAlgorithm<SPACE> extends IAlgorithm {


   /**
    * Performs the actual fitting process of this distribution fitting algorithm and returns the
    * constructed distribution.
    * 
    * @param sampleSet the sample set to apply the fitting algorithm on
    * @return the distribution fit to the samples contained in the given {@code sampleSet}
    * @throws FittingDistributionFailedException if the fitting process cannot be continued due to
    *            an error
    */
   public abstract IDistribution<SPACE> fit(final IDistributionSampleSet<SPACE> sampleSet) throws FittingDistributionFailedException;

}
