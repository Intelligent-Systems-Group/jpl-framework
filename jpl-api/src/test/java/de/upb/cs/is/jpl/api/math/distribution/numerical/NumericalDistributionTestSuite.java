package de.upb.cs.is.jpl.api.math.distribution.numerical;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.IntegerDistributionTestSuite;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.RealDistributionTestSuite;


/**
 * Test suite for all numerical distribution classes.
 * 
 * @author Tanja Tornede
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ IntegerDistributionTestSuite.class, RealDistributionTestSuite.class })
public class NumericalDistributionTestSuite {

}
