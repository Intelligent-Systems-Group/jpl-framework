package de.upb.cs.is.jpl.api;


import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.AlgorithmTestSuite;
import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
import de.upb.cs.is.jpl.api.dataset.DatasetTestSuite;
import de.upb.cs.is.jpl.api.evaluation.EvaluationTestSuite;
import de.upb.cs.is.jpl.api.math.MathTestSuite;
import de.upb.cs.is.jpl.api.metric.MetricTestSuite;
import de.upb.cs.is.jpl.api.tool.ToolTestSuite;
import de.upb.cs.is.jpl.api.util.UtilTestSuite;


/**
 * Test suite to run all existing test suites. Everyone should add their specific test suites here.
 * This test suite should include one test suite for each top-level (de.upb.cs.is.jpl.xyz) package.
 * 
 * @author Alexander Hetzer
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ AlgorithmTestSuite.class, DatasetTestSuite.class, EvaluationTestSuite.class, MetricTestSuite.class,
      MathTestSuite.class, ToolTestSuite.class, UtilTestSuite.class })
public class RunAllAPITestsSuite {

   /**
    * Initializes the logging framework with the default logging configuration before running this
    * suite.
    */
   @BeforeClass
   public static void setUp() {
      LoggingConfiguration.setupLoggingConfiguration();
   }

}
