package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation;


import java.io.File;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithmTest;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This abstract class is used for all tests for algorithms for rank aggregation problem.
 * 
 * @author Pritha Gupta
 *
 */
public abstract class ARankAggregationTest extends ALearningAlgorithmTest<Integer, NullType, Ranking> {
   private static final String RESOURCE_DIRECTORY_LEVEL = "rankaggregation" + File.separator;


   /**
    * Creates a new unit test for rank aggregation algorithms with the additional path to the
    * resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ARankAggregationTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   /**
    * Creates a new unit test for rank aggregation algorithms without any additional path to the
    * resources.
    */
   public ARankAggregationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }
}
