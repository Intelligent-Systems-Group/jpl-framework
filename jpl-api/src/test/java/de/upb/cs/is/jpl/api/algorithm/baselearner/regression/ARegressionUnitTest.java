package de.upb.cs.is.jpl.api.algorithm.baselearner.regression;


import java.io.File;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerTest;


/**
 * This abstract class is used for all tests of the regression algorithms.
 * 
 * @author Tanja Tornede
 * 
 * @param <CONTEXT> the type of the context feature vector used in the implementation of the
 *           algorithm
 * @param <ITEM> the type of the item feature vector used in the implementation of the algorithm
 * @param <RATING> the type of the rating used in the implementation of the algorithm
 */
public abstract class ARegressionUnitTest<CONTEXT, ITEM, RATING> extends ABaselearnerTest<CONTEXT, ITEM, RATING> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "regression" + File.separator;


   /**
    * Creates a new unit test for regression algorithms with the additional path to the resources
    * given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ARegressionUnitTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }
}
