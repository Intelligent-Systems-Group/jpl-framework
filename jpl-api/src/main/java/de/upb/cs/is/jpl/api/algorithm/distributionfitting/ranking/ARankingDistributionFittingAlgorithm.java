package de.upb.cs.is.jpl.api.algorithm.distributionfitting.ranking;


import de.upb.cs.is.jpl.api.algorithm.distributionfitting.ADistributionFittingAlgorithm;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;


/**
 * This abstract class represents a distribution fitting algorithm.
 * 
 * @author Tanja Tornede
 * 
 * @param <CONFIG> the generic type extending
 *           {@link ARankingDistributionFittingAlgorithmConfiguration}, configuration associated
 *           with the {@link ARankingDistributionFittingAlgorithm} class.
 */
public abstract class ARankingDistributionFittingAlgorithm<CONFIG extends ARankingDistributionFittingAlgorithmConfiguration>
      extends ADistributionFittingAlgorithm<Ranking, CONFIG> {

   /**
    * Creates a new {@link ARankingDistributionFittingAlgorithm}.
    */
   public ARankingDistributionFittingAlgorithm() {
      super();
   }


}
