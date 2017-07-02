package de.upb.cs.is.jpl.cli;


import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
import de.upb.cs.is.jpl.cli.command.CommandTestSuite;
import de.upb.cs.is.jpl.cli.core.CoreTestSuite;
import de.upb.cs.is.jpl.cli.tools.ToolsTestSuite;


/**
 * Test suite to run all existing test suites. Everyone should add their specific test suites here.
 * This test suite should include one test suite for each top-level (de.upb.cs.is.jpl.xyz) package.
 * 
 * @author Alexander Hetzer
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ CommandTestSuite.class, CoreTestSuite.class, ToolsTestSuite.class })
public class RunAllClTestsSuite {

   /**
    * Initializes the logging framework with the default logging configuration before running this
    * suite.
    */
   @BeforeClass
   public static void setUp() {
      LoggingConfiguration.setupLoggingConfiguration();
   }

}
