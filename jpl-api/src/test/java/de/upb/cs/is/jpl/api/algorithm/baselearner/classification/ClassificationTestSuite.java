package de.upb.cs.is.jpl.api.algorithm.baselearner.classification;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.LinearClassificationTestSuite;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine.SupportVectorMachineClassificationTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.regression.logistic.LogisticRegressionTest;


/**
 * Test suite for all base learner tests.
 * 
 * @author Tanja Tornede
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ LinearClassificationTestSuite.class, LogisticRegressionTest.class, SupportVectorMachineClassificationTest.class })
public class ClassificationTestSuite {

}
