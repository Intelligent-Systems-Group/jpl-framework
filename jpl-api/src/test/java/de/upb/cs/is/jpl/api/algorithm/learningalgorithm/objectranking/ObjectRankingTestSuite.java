package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.expectedrankregression.ExpectedRankRegressionTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.pairwiserankingalgorithm.PairwiseRankingLearningAlgorithmTest;


/**
 * This test suite contains all tests which are written for Object Ranking algorithms.
 * 
 * @author Pritha Gupta
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ExpectedRankRegressionTest.class, PairwiseRankingLearningAlgorithmTest.class })
public class ObjectRankingTestSuite {

}
