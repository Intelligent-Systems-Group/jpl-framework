package de.upb.cs.is.jpl.api.algorithm.baselearner;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.ClassificationTestSuite;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor.KNearestNeighborClassificationTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.regression.linear.LinearRegressionUnitTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.regression.logistic.LogisticRegressionStochasticGradientDescentTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.regression.logistic.LogisticRegressionTest;


/**
 * Test suite for all base learner tests.
 * 
 * @author Tanja Tornede
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ ClassificationTestSuite.class, LogisticRegressionTest.class, LogisticRegressionStochasticGradientDescentTest.class,
      LinearRegressionUnitTest.class, KNearestNeighborClassificationTest.class })


public class BaselearnerTestSuite {

}
