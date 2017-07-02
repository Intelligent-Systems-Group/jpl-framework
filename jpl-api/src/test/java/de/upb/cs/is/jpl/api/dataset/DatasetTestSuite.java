package de.upb.cs.is.jpl.api.dataset;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.LabelRankingTestSuite;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringDatasetTestSuite;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultDatasetTestSuite;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDatasetTestSuite;
import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDatasetTestSuite;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDatasetTestSuite;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDatasetTestSuite;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDatasetTestSuite;


/**
 * Test suite for all dataset tests.
 * 
 * @author Sebastian Osterbrink
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ CollaborativeFilteringDatasetTestSuite.class, DefaultDatasetTestSuite.class, InstanceRankingDatasetTestSuite.class,
      LabelRankingTestSuite.class, ObjectRankingDatasetTestSuite.class, OrdinalClassificationDatasetTestSuite.class,
      RankAggregationDatasetTestSuite.class, MultilabelClassificationDatasetTestSuite.class })
public class DatasetTestSuite {

}
