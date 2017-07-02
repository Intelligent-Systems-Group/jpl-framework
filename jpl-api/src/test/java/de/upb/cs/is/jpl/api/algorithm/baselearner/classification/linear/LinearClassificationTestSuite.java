package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.algorithm.MiniBatchPegasosLearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.algorithm.PerceptronLearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.algorithm.PocketLearningAlgorithmTest;


/**
 * Test suite for all linear classification tests.
 * 
 * @author Tanja Tornede
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ PerceptronLearningAlgorithmTest.class, PocketLearningAlgorithmTest.class, MiniBatchPegasosLearningAlgorithmTest.class })
public class LinearClassificationTestSuite {

}
