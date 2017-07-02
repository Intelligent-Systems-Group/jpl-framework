package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithmTest;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDatasetParser;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDatasetParser;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDatasetParser;


/**
 * This abstract class is used for all tests for algorithms for rank aggregation problem.
 * 
 * @author Pritha Gupta
 *
 */
public abstract class AObjectRankingTest extends ALearningAlgorithmTest<double[], List<double[]>, Ranking> {
   private static final String RESOURCE_DIRECTORY_LEVEL = "objectranking" + File.separator;

   private static final String RANK_AGGREGATION_WITH_EQUALS_DATASET = "ED-00006-00000004-soc_with_equals_comperator.gprf";
   private static final String INSTANCE_RANKING_DATASET = "instancerankingtest.gprf";
   protected static final String PARAMETER_BASELEARNER_NOT_REGRESSION_REFLECTION_VARIABLE = "ERROR_BASE_LEARNER_IS_NOT_A_REGRESSION";
   protected static final String PARAMETER_BASELEARNER_NOT_CLASSIFIER_REFLECTION_VARIABLE = "ERROR_BASE_LEARNER_IS_NOT_A_CLASSIFIER";

   protected static final String ERROR_REFLECTION_FAILED = "Test can not run because reflection failed";
   protected static final String OBJECT_RANKING_DATASET_B = "sushi-dataset-zip-b.gprf";
   protected static final String OBJECT_RANKING_DATASET_A = "sushi-dataset-zip-a.gprf";


   /**
    * Creates a new unit test for rank aggregation algorithms with the additional path to the
    * resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public AObjectRankingTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   /**
    * Creates a new unit test for rank aggregation algorithms without any additional path to the
    * resources.
    */
   public AObjectRankingTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> wrongDataset = new ArrayList<IDataset<?, ?, ?>>();
      wrongDataset.add(
            this.createDatasetOutOfFile(new RankAggregationDatasetParser(), getTestRessourcePathFor(RANK_AGGREGATION_WITH_EQUALS_DATASET)));
      wrongDataset.add(this.createDatasetOutOfFile(new InstanceRankingDatasetParser(), getTestRessourcePathFor(INSTANCE_RANKING_DATASET)));

      return wrongDataset;
   }


   @Override
   public List<IDataset<double[], List<double[]>, Ranking>> getCorrectDatasetList() {
      List<IDataset<double[], List<double[]>, Ranking>> correctDatasets = new ArrayList<>();
      ObjectRankingDataset objectRankingDataset = (ObjectRankingDataset) this.createDatasetOutOfFile(new ObjectRankingDatasetParser(),
            getTestRessourcePathFor(OBJECT_RANKING_DATASET_B));
      correctDatasets.add(objectRankingDataset);
      return correctDatasets;
   }

}
