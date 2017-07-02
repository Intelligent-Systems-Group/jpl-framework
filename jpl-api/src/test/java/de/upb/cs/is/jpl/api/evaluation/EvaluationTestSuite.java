package de.upb.cs.is.jpl.api.evaluation;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.CollaborativeFilteringEvaluationTestSuite;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.InstanceRankingEvaluationTestSuite;
import de.upb.cs.is.jpl.api.evaluation.labelranking.LabelRankingEvaluationTestSuite;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.MultilabelClassificationEvaluationTestSuite;
import de.upb.cs.is.jpl.api.evaluation.objectranking.ObjectRankingEvaluationTestSuite;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.OrdinalClassificationEvaluationTestSuite;
import de.upb.cs.is.jpl.api.evaluation.rankagrregation.RankAggregationEvaluationTestSuite;


/**
 * Test suite for all evaluation tests.
 * 
 * @author Pritha Gupta
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ InstanceRankingEvaluationTestSuite.class, RankAggregationEvaluationTestSuite.class,
      LabelRankingEvaluationTestSuite.class, OrdinalClassificationEvaluationTestSuite.class,
      MultilabelClassificationEvaluationTestSuite.class, ObjectRankingEvaluationTestSuite.class, CollaborativeFilteringEvaluationTestSuite.class })
public class EvaluationTestSuite {

}
