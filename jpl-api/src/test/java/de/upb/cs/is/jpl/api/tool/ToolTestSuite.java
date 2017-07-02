package de.upb.cs.is.jpl.api.tool;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.tool.gson.GsonTest;


/**
 * Test suite for all tool tests.
 * 
 * @author Alexander Hetzer
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ GsonTest.class })
public class ToolTestSuite {

}
