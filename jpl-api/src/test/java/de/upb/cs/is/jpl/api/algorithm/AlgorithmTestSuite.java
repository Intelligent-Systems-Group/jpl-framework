package de.upb.cs.is.jpl.api.algorithm;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.baselearner.BaselearnerTestSuite;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.LearningAlgorithmTestSuite;


/**
 * Test suite for all algorithm tests.
 * 
 * @author Alexander Hetzer
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ BaselearnerTestSuite.class, LearningAlgorithmTestSuite.class })
public class AlgorithmTestSuite {

}
