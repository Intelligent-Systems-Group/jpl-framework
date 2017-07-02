package de.upb.cs.is.jpl.api.dataset.labelranking;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import de.upb.cs.is.jpl.api.dataset.ADatasetParserTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;


/**
 * Test for the GPRF dataset parser {@link LabelRankingDatasetParser}.
 * 
 * @author Andreas Kornelsen
 */
public class LabelRankingDatasetParserTest extends ADatasetParserTest {


   private static final String ASSERT_EXPECTED_NUMBER_OF_INSTANCES = "The number of instances should be %d, but is %d";
   private static final String ASSERT_EQUALS_RANKINGS = "The rankings should be equal; expectedRanking: %s, ranking: %s";
   private static final String ASSERT_EQUALS_COUNT_RANKING = "The count for the ranking should be the same; expectedCount: %s, count: %s";

   private static final double[][] FEATURES = new double[][] {
         { 0.911, 0.0271, 0.555, 2.07, -0.16, -0.929, -0.673, -1.17, -1.17, -1.14, 0.233, 1.19, 0.174, 1.06, 0.311, -0.127, -0.221, -1.16,
               -0.685, -0.599, -1.06, -0.375, 0.577 },
         { -0.423, -0.514, -0.434, 0.631, -0.167, -0.315, -0.514, -0.514, -0.479, -0.514, -0.0894, -0.322, 0.129, 0.741, 0.0635, -0.155,
               -0.514, -0.514, -0.0562, 0.631, -0.514, 0.0512, -0.514 },
         { -0.246, 0.0369, -0.298, -0.306, -0.165, -0.286, -0.167, -0.141, -0.195, -0.216, -0.15, -0.187, -0.229, -0.0976, -0.306, -0.2,
               -0.282, 0.0132, -0.279, -0.306, -0.306, -0.0376, -0.297 },
         { -1.93, -1.9, 0.41, 0.628, 0.503, 0.425, 0.488, 0.41, 0.581, 0.55, 0.597, 0.581, 0.254, -1.88, 0.254, 0.254, 0.519, -1.88, -1.87,
               0.674, 0.659, 0.425, 0.581 } };
   private static final int[][] RANKINGS = new int[][] { { 3, 1, 2, 4 }, { 4, 1, 3, 2 }, { 1, 2, 3, 4 }, { 1, 4, 2, 3 } };


   private static final String RESOURCE_DIRECTORY_LEVEL = "labelranking" + File.separator;
   private static final String DATASET_LABEL_RANKING = "cold-txt_predict.gprf";
   private static final String DATASET_INSTANCE_RANKING = "instancerankingtest.gprf";


   /**
    * Instantiates a new label ranking dataset parser test.
    */
   public LabelRankingDatasetParserTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected List<DatasetFile> getInvalidDatasets() {
      List<DatasetFile> invalidDatasets = new ArrayList<DatasetFile>();
      invalidDatasets.add(new DatasetFile(new File(getTestRessourcePathFor(DATASET_INSTANCE_RANKING))));
      return invalidDatasets;
   }


   @Override
   protected List<DatasetFile> getValidDatasets() {
      List<DatasetFile> validDatasets = new ArrayList<DatasetFile>();
      validDatasets.add(new DatasetFile(new File(getTestRessourcePathFor(DATASET_LABEL_RANKING))));
      return validDatasets;
   }


   @Override
   protected void validateDataset(int datasetNumber, IDataset<?, ?, ?> parseResult) {

      LabelRankingDataset labelRankingDataset = (LabelRankingDataset) parseResult;
      if (datasetNumber == 0) {
         int numberOfInstances = labelRankingDataset.getNumberOfInstances();
         int expectedNumberOfRankings = RANKINGS.length;
         int expectedNumberOfFeatures = FEATURES.length;

         Assert.assertEquals(String.format(ASSERT_EXPECTED_NUMBER_OF_INSTANCES, expectedNumberOfRankings, numberOfInstances),
               expectedNumberOfRankings, numberOfInstances);
         Assert.assertEquals(String.format(ASSERT_EXPECTED_NUMBER_OF_INSTANCES, expectedNumberOfFeatures, numberOfInstances),
               expectedNumberOfFeatures, numberOfInstances);

         for (int instancePosition = 0; instancePosition < labelRankingDataset.getNumberOfInstances(); instancePosition++) {
            Ranking rankingOfInstance = labelRankingDataset.getRankingOfInstance(instancePosition);

            int[] compareOperators = getCompareOperator(RANKINGS[instancePosition]);

            Ranking expectedRankingOfInstance = new Ranking(RANKINGS[instancePosition], compareOperators);
            Assert.assertEquals(String.format(ASSERT_EQUALS_RANKINGS, expectedRankingOfInstance.toString(), rankingOfInstance.toString()),
                  expectedRankingOfInstance, rankingOfInstance);

            double[] featuresForInstance = labelRankingDataset.getFeatureValuesOfAnInstance(instancePosition);
            double[] expectedFeaturesForInstnace = FEATURES[instancePosition];

            Assert.assertArrayEquals(String.format(ASSERT_EQUALS_COUNT_RANKING, Arrays.toString(expectedFeaturesForInstnace),
                  Arrays.toString(featuresForInstance)), expectedFeaturesForInstnace, featuresForInstance, 0.00000001);
         }
      }
   }


   @Override
   protected IDatasetParser getDatasetParser() {
      return new LabelRankingDatasetParser();
   }


   /**
    * Returns the compare operator for given labels of a ranking.
    *
    * @param labels the labels of the an ranking
    * @return the compare operator for the ranking
    */
   private int[] getCompareOperator(int[] labels) {
      int[] compareOperators = new int[labels.length - 1];
      for (int index = 0; index < compareOperators.length; index++) {
         compareOperators[index] = Ranking.COMPARABLE_ENCODING;
      }
      return compareOperators;
   }

}
