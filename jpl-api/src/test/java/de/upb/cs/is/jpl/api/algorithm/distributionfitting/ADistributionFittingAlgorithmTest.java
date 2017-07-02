package de.upb.cs.is.jpl.api.algorithm.distributionfitting;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.IAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.distributionfitting.IDistributionFittingAlgorithm;


/**
 * Abstract Test for {@link IDistributionFittingAlgorithm}.
 * 
 * @author Tanja Tornede
 *
 * @param <SPACE> the space (domain) of the {@link IDistributionFittingAlgorithm} to test
 */
public abstract class ADistributionFittingAlgorithmTest<SPACE> extends AAlgorithmTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "distributionfitting";


   /**
    * Creates a new unit test for algorithms with the additional path to the resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ADistributionFittingAlgorithmTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   /**
    * Returns an instance of the {@link IDistributionFittingAlgorithm} which should be checked.
    * 
    * @return instance of the distribution fitting algorithm to check
    */
   public abstract IDistributionFittingAlgorithm<SPACE> getDistributionFittingAlgorithm();


   @Override
   public IAlgorithm getAlgorithm() {
      return getDistributionFittingAlgorithm();
   }

}
