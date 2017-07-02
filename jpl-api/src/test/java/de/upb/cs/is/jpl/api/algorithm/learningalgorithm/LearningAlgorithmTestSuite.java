package de.upb.cs.is.jpl.api.algorithm.learningalgorithm;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.CollaborativeFilteringTestSuite;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.InstanceRankingTestSuite;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.LabelRankingTestSuite;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.MultilabelClassificationLearningAlgorithmTestSuite;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.ObjectRankingTestSuite;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.OrdinalClassificationTestSuite;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.RankAggregationTestSuite;


/**
 * Test suite for all learning algorithm tests.
 * 
 * @author Alexander Hetzer
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ InstanceRankingTestSuite.class, OrdinalClassificationTestSuite.class, RankAggregationTestSuite.class,
      ObjectRankingTestSuite.class, CollaborativeFilteringTestSuite.class, LabelRankingTestSuite.class,
      MultilabelClassificationLearningAlgorithmTestSuite.class })
public class LearningAlgorithmTestSuite {

}
