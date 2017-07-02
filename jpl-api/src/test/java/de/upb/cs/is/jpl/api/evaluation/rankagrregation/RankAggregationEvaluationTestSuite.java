package de.upb.cs.is.jpl.api.evaluation.rankagrregation;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.evaluation.rankagrregation.insampleevaluation.RankAggregationInSampleEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.rankagrregation.supliedtestsetevaluation.RankAggregationTestSetEvaluationTest;


/**
 * Test suite for all rank aggregation evaluation tests.
 * 
 * @author Pritha Gupta
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ RankAggregationTestSetEvaluationTest.class, RankAggregationInSampleEvaluationTest.class })
public class RankAggregationEvaluationTestSuite {

}
