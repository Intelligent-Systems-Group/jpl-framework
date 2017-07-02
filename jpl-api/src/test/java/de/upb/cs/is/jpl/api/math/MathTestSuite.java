package de.upb.cs.is.jpl.api.math;


import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
import de.upb.cs.is.jpl.api.math.linearalgrbra.LinearAlgebraTestSuite;


/**
 * Test suite for all math related classes.
 * 
 * @author Alexander Hetzer
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ LinearAlgebraTestSuite.class })
public class MathTestSuite {

   /**
    * Initializes the logging framework with the default logging configuration before running this
    * suite.
    */
   @BeforeClass
   public static void setUp() {
      LoggingConfiguration.setupLoggingConfiguration();
   }

}
