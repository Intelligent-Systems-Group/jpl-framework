package de.upb.cs.is.jpl.api.dataset.defaultdataset;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetParserTest;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetTest;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDatasetParserTest;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDatasetTest;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.RankingUnitTest;


/**
 * Test suite for all dataset tests.
 * 
 * @author Sebastian Osterbrink
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ DefaultRelativeDatasetParserTest.class, DefaultAbsoluteDatasetParserTest.class, DefaultAbsoluteDatasetTest.class,
      DefaultRelativeDatasetTest.class, RankingUnitTest.class })
public class DefaultDatasetTestSuite {

}
