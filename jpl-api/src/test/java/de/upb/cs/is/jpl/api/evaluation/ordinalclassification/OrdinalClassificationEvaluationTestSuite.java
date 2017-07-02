package de.upb.cs.is.jpl.api.evaluation.ordinalclassification;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.crossvalidation.OrdinalClassificationCrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.insample.OrdinalClassificationInSampleEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.percentagesplit.OrdinalClassificationPercentageSplitEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.suppliedtestset.OrdinalClassificationSuppliedTestSetEvaluationTest;


/**
 * Test suite for all ordinal classification evaluation tests.
 * 
 * @author Tanja Tornede
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ OrdinalClassificationCrossValidationEvaluationTest.class, OrdinalClassificationInSampleEvaluationTest.class,
      OrdinalClassificationPercentageSplitEvaluationTest.class, OrdinalClassificationSuppliedTestSetEvaluationTest.class })
public class OrdinalClassificationEvaluationTestSuite {

}
