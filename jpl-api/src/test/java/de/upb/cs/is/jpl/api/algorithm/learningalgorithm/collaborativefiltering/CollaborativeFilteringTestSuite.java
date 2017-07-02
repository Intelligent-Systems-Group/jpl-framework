package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * This Testsuite contains all tests for Collaborative Filtering algorithms.
 * 
 * @author Sebastian Osterbrink
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MatrixFactorizationLearningAlgorithmTest.class, UserBasedFilteringLearningAlgorithmTest.class })
public class CollaborativeFilteringTestSuite {

}
