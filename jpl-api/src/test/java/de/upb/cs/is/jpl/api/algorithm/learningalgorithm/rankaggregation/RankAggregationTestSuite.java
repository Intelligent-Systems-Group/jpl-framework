package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.bordacount.BordaCountLearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.kemenyyoung.KemenyYoungLearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.plackettluce.PlackettLuceLearningAlgorithmTest;


/**
 * Test suite for all rank aggregation algorithm tests.
 * 
 * @author Pritha Gupta
 * @author Andreas Kornelsen
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ BordaCountLearningAlgorithmTest.class, KemenyYoungLearningAlgorithmTest.class, PlackettLuceLearningAlgorithmTest.class })
public class RankAggregationTestSuite {

}
