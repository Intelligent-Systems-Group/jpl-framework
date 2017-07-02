package de.upb.cs.is.jpl.api.evaluation.labelranking;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.evaluation.labelranking.crossvalidation.LabelRankingCrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.labelranking.insample.LabelRankingInSampleEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.labelranking.percentagesplit.LabelRankingPercentageSplitEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.labelranking.suppliedtestset.LabelRankingSuppliedTestsetEvaluationTest;


/**
 * Test suite for all label ranking evaluation tests.
 * 
 * @author Andreas Kornelsen
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ LabelRankingInSampleEvaluationTest.class, LabelRankingCrossValidationEvaluationTest.class,
      LabelRankingPercentageSplitEvaluationTest.class, LabelRankingSuppliedTestsetEvaluationTest.class })
public class LabelRankingEvaluationTestSuite {

}
