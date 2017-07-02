package de.upb.cs.is.jpl.api.math.distribution.numerical.integer;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.bernoulli.BernoulliDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.binomial.BinomialDistributionTest;


/**
 * Test suite for all integer distribution classes.
 * 
 * @author Tanja Tornede
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BernoulliDistributionTest.class, BinomialDistributionTest.class })
public class IntegerDistributionTestSuite {

}
