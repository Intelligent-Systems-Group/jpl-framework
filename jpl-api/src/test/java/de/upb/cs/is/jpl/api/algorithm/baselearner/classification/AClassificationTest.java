package de.upb.cs.is.jpl.api.algorithm.baselearner.classification;


import java.io.File;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerTest;


/**
 * This abstract class is used for all tests of the classification algorithms.
 * 
 * @author Tanja Tornede
 * 
 * @param <CONTEXT> the type of the context feature vector used in the implementation of the
 *           algorithm
 * @param <ITEM> the type of the item feature vector used in the implementation of the algorithm
 * @param <RATING> the type of the rating used in the implementation of the algorithm
 */
public abstract class AClassificationTest<CONTEXT, ITEM, RATING> extends ABaselearnerTest<CONTEXT, ITEM, RATING> {

   private static final String ADDITIONAL_DIRECTORY = "classification" + File.separator;


   /**
    * Creates a new unit test for classification algorithms with the additional path to the
    * resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public AClassificationTest(String additionalResourcePath) {
      super(ADDITIONAL_DIRECTORY + additionalResourcePath);
   }
}
