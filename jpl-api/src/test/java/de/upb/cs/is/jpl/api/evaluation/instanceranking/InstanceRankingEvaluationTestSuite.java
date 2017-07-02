package de.upb.cs.is.jpl.api.evaluation.instanceranking;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.evaluation.instanceranking.crossvalidation.InstanceRankingCrossValidationEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.insample.InstanceRankingInSampleEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.percentagesplit.InstanceRankingPercentageSplitEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.suppliedtestset.InstanceRankingSuppliedTestSetEvaluationTest;


/**
 * Test suite for all instance ranking evaluation tests.
 * 
 * @author Sebastian Gottschalk
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ InstanceRankingCrossValidationEvaluationTest.class, InstanceRankingPercentageSplitEvaluationTest.class,
      InstanceRankingInSampleEvaluationTest.class, InstanceRankingSuppliedTestSetEvaluationTest.class })
public class InstanceRankingEvaluationTestSuite {

}
