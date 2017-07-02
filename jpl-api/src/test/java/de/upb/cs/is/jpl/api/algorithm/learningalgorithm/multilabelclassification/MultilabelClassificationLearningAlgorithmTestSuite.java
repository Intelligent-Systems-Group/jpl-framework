package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.binaryrelevancelearning.BinaryRelevanceLearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.classifierchains.ClassifierChainsLearningAlgorithmTest;


/**
 * This test suite tests all multilabel classification learning algorithms.
 * 
 * @author Alexander Hetzer
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BinaryRelevanceLearningAlgorithmTest.class, ClassifierChainsLearningAlgorithmTest.class })
public class MultilabelClassificationLearningAlgorithmTestSuite {

}
