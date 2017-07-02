package de.upb.cs.is.jpl.api.evaluation.objectranking;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.evaluation.objectranking.crossvalidation.ObjectRankingCrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.objectranking.insample.ObjectRankingInSampleEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.objectranking.percentagesplit.ObjectRankingPercentageSplitEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.objectranking.suppliedtestset.ObjectRankingSuppliedTestsetEvaluationTest;


/**
 * Test suite for all object ranking evaluation tests.
 * 
 * @author Pritha Gupta
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ ObjectRankingPercentageSplitEvaluationTest.class, ObjectRankingCrossValidationEvaluationTest.class,
      ObjectRankingSuppliedTestsetEvaluationTest.class, ObjectRankingInSampleEvaluationTest.class })
public class ObjectRankingEvaluationTestSuite {

}
