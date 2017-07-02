package de.upb.cs.is.jpl.api.math.distribution.numerical.real;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.math.distribution.numerical.real.beta.BetaDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.cauchy.CauchyDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.chisquared.ChiSquaredDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.gumbel.GumbelDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.normal.NormalDistributionTest;


/**
 * Test suite for all real distribution classes.
 * 
 * @author Tanja Tornede
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BetaDistributionTest.class, CauchyDistributionTest.class, ChiSquaredDistributionTest.class,
      GumbelDistributionTest.class, NormalDistributionTest.class })
public class RealDistributionTestSuite {

}
