package de.upb.cs.is.jpl.api.evaluation.multilabelclassification;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.crossvalidation.MultilabelClassificationCrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.insample.MultilabelClassificationInSampleEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.percentagesplit.MultilabelClassificationPercentageSplitEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.suppliedtest.MultilabelClassificationSuppliedTestsetEvaluationTest;


/**
 * This test suite contains all evaluation test for multilabel classification.
 * 
 * @author Alexander Hetzer
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MultilabelClassificationCrossValidationEvaluationTest.class,
      MultilabelClassificationPercentageSplitEvaluationTest.class, MultilabelClassificationInSampleEvaluationTest.class,
      MultilabelClassificationSuppliedTestsetEvaluationTest.class })
public class MultilabelClassificationEvaluationTestSuite {

}
