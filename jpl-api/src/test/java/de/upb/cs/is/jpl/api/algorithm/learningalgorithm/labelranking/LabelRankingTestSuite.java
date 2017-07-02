package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.instancebasedlabelranking.InstanceBasedLabelRankingLearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.labelrankingbypairwisecomparison.LabelRankingByPairwiseComparisonLearningAlgorithmTest;


/**
 * This test suite contains all tests which are written for Label Ranking algorithms.
 * 
 * @author Andreas Kornelsen
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ LabelRankingByPairwiseComparisonLearningAlgorithmTest.class, InstanceBasedLabelRankingLearningAlgorithmTest.class })
public class LabelRankingTestSuite {

}
