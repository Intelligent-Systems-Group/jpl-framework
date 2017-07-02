package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.combinedrankingandregression.CombinedRankingAndRegressionLearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank.PerceptronRankLearningAlgorithmTest;


/**
 * Test suite for all instance ranking algorithm tests.
 * 
 * @author Tanja Tornede
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ PerceptronRankLearningAlgorithmTest.class, CombinedRankingAndRegressionLearningAlgorithmTest.class })
public class InstanceRankingTestSuite {

}
