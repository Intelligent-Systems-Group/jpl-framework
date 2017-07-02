package de.upb.cs.is.jpl.api.math.distribution.special;


import de.upb.cs.is.jpl.api.math.distribution.ADistributionTest;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Abstract distribution test for {@link ISpecialDistribution}.
 * 
 * @author Tanja Tornede
 *
 * @param <SPACE> the space used in the special distirubtion to test
 */
public abstract class ASpecialDistributionTest<SPACE> extends ADistributionTest<SPACE> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "special" + StringUtils.FORWARD_SLASH;


   /**
    * Creates a new unit test for algorithms with the additional path to the resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ASpecialDistributionTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }

}
