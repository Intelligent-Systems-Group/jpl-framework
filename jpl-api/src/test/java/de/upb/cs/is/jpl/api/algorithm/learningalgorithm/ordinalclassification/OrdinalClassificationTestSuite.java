package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.reductionframework.OrdinalClassificationReductionFrameworkTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.simple.SimpleOrdinalClassificationTest;


/**
 * Test suite for all ordinal classification algorithm tests.
 * 
 * @author Tanja Tornede
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ SimpleOrdinalClassificationTest.class, OrdinalClassificationReductionFrameworkTest.class })
public class OrdinalClassificationTestSuite {

}
