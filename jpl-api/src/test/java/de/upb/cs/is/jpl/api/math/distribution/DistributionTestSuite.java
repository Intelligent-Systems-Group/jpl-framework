package de.upb.cs.is.jpl.api.math.distribution;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.math.distribution.numerical.NumericalDistributionTestSuite;


/**
 * Test suite for all distribution classes.
 * 
 * @author Tanja Tornede
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ NumericalDistributionTestSuite.class })
public class DistributionTestSuite {

}
