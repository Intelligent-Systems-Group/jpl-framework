package de.upb.cs.is.jpl.api.algorithm.distributionfitting;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithm;
import de.upb.cs.is.jpl.api.math.distribution.IDistributionSampleSet;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.IIntegerDistribution;


/**
 * This abstract class represents a distribution fitting algorithm.
 * 
 * @author Tanja Tornede
 * 
 * @param <SPACE> the space (domain) of the {@link IDistributionSampleSet} of this distribution,
 *           e.g. Integer for {@link IIntegerDistribution}s
 * @param <CONFIG> the generic type extending {@link ADistributionFittingAlgorithmConfiguration},
 *           configuration associated with the {@link ADistributionFittingAlgorithm} class.
 */
public abstract class ADistributionFittingAlgorithm<SPACE, CONFIG extends ADistributionFittingAlgorithmConfiguration>
      extends AAlgorithm<CONFIG> implements IDistributionFittingAlgorithm<SPACE> {

   /**
    * Creates a new {@link ADistributionFittingAlgorithm}.
    */
   public ADistributionFittingAlgorithm() {
      super();
   }


}
