package de.upb.cs.is.jpl.api.algorithm.learningalgorithm;


import java.io.File;

import de.upb.cs.is.jpl.api.algorithm.ATrainableAlgorithmTest;


/**
 * This abstract class is used for all tests of ordinal classification.
 * 
 * @author Tanja Tornede
 * 
 * @param <CONTEXT> the type of the context feature vector used in the implementation of the
 *           algorithm
 * @param <ITEM> the type of the item feature vector used in the implementation of the algorithm
 * @param <RATING> the type of the rating used in the implementation of the algorithm
 */
public abstract class ALearningAlgorithmTest<CONTEXT, ITEM, RATING> extends ATrainableAlgorithmTest<CONTEXT, ITEM, RATING> {

   private static final String ADDITIONAL_DIRECTORY = "learningalgorithm" + File.separator;


   /**
    * Creates a new unit test for learning algorithms with the additional path to the resources
    * given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ALearningAlgorithmTest(String additionalResourcePath) {
      super(ADDITIONAL_DIRECTORY + additionalResourcePath);
   }
}
