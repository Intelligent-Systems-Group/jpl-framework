package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking;


import java.io.File;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithmTest;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This abstract class is used for all tests for algorithms for label ranking problem.
 * 
 * @author Andreas Kornelsen
 */
public abstract class ALabelRankingTest extends ALearningAlgorithmTest<double[], NullType, Ranking> {
   private static final String RESOURCE_DIRECTORY_LEVEL = "labelranking" + File.separator;


   /**
    * Creates a new unit test for label ranking algorithms with the additional path to the resources
    * given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ALabelRankingTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   /**
    * Creates a new unit test for label ranking algorithms without any additional path to the
    * resources.
    */
   public ALabelRankingTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }
}
