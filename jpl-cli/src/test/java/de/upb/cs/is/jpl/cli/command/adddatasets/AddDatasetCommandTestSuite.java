package de.upb.cs.is.jpl.cli.command.adddatasets;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.cli.command.adddataset.AddDatasetCommand;


/**
 * Test suite for all {@link AddDatasetCommand} tests.
 * 
 * @author Sebastian Osterbrink
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ AddDatasetCommandUnitTest.class, AddDatasetCommandIntegrationTest.class })
public class AddDatasetCommandTestSuite {


}
