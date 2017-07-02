package de.upb.cs.is.jpl.api.math.distribution.special.ranking;


import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.math.distribution.special.ASpecialDistributionTest;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Abstract test for ranking distributions.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class ARankingDistributionTest extends ASpecialDistributionTest<Ranking> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "ranking" + StringUtils.FORWARD_SLASH;


   /**
    * Creates a new unit test for algorithms with the additional path to the resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ARankingDistributionTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }
}
