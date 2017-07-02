package de.upb.cs.is.jpl.api.dataset.objectranking;


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
 * Test for the GPRF dataset parser {@link ObjectRankingDatasetParser}.
 * 
 * @author Pritha Gupta
 */
public class ObjectRankingDatasetParserTest extends ADatasetParserTest {
   private static final String RESOURCE_DIRECTORY_LEVEL = "objectranking" + File.separator;
   private static final String DATASET_OBJECT_RANKING = "generatedDataset.gprf";
   private static final String DATASET_INSTANCE_MOVIE_LENS = "instancemovielens.gprf";
   private static final String ASSERT_EXPECTED_NUMBER_OF_INSTANCES = "The number of instances should be %d, but is %d";
   private static final String ASSERT_EQUALS_RANKINGS = "The rankings should be equal; expectedRanking: %s, ranking: %s";
   private static final String ASSERT_EQUALS_ITEM_FEATURES = "The item features should be equal; expected feature: %s, feature: %s";
   private static final String ASSERT_EQUALS_CONTEXT_FEATURES = "The context features should be equal; expected feature: %s, feature: %s";

   private int[][] RANKINGS = new int[][] { { 59, 5, 4, 45, 88, 61, 68, 2, 13, 75 }, { 23, 32, 61, 22, 9, 25, 7, 13, 18, 77 },
         { 3, 16, 14, 2, 7, 26, 47, 75, 57, 56 }, { 9, 1, 4, 10, 25, 44, 5, 6, 30, 41 }, { 10, 48, 51, 31, 5, 20, 99, 56, 32, 14 },
         { 38, 7, 1, 44, 4, 57, 47, 35, 12, 18 }, { 5, 12, 26, 11, 10, 3, 4, 6, 79, 91 }, { 4, 27, 9, 10, 6, 70, 28, 7, 15, 22 },
         { 20, 39, 27, 1, 5, 23, 28, 30, 16, 17 }, { 15, 38, 4, 2, 44, 10, 37, 21, 19, 7 }, { 9, 3, 31, 89, 12, 6, 65, 7, 8, 1 },
         { 14, 16, 20, 54, 33, 43, 10, 24, 18, 8 } };
   private double[][] ITEM_FEATURES = new double[][] { { 1.0, 0.0, 6.0, 2.72897800776197, 2.13842173350582, 1.83841991341991, 0.84 },
         { 1.0, 0.0, 3.0, 0.926384364820847, 1.99022801302932, 1.99245867768595, 0.88 },
         { 1.0, 0.0, 1.0, 1.76955903271693, 2.34850640113798, 1.87472451790634, 0.88 },
         { 1.0, 0.0, 5.0, 2.68840082361016, 2.04323953328758, 1.51515151515152, 0.92 },
         { 1.0, 0.0, 8.0, 0.81304347826087, 1.64347826086957, 3.28728191000918, 0.88 } };
   private double[][] CONTEXT_FEATURES = new double[][] { { 10.0 }, { 10.0 }, { 10.0 }, { 10.0 }, { 10.0 } };


   /**
    * Creates a new {@link ObjectRankingDatasetParserTest} and sets the resource path for this test
    * level.
    */
   public ObjectRankingDatasetParserTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IDatasetParser getDatasetParser() {
      return new ObjectRankingDatasetParser();
   }


   @Override
   protected List<DatasetFile> getInvalidDatasets() {
      List<DatasetFile> invalidDatasets = new ArrayList<DatasetFile>();
      invalidDatasets.add(new DatasetFile(new File(getTestRessourcePathFor(DATASET_INSTANCE_MOVIE_LENS))));
      return invalidDatasets;
   }


   @Override
   protected List<DatasetFile> getValidDatasets() {
      List<DatasetFile> validDatasets = new ArrayList<DatasetFile>();
      validDatasets.add(new DatasetFile(new File(getTestRessourcePathFor(DATASET_OBJECT_RANKING))));
      return validDatasets;
   }


   @Override
   protected void validateDataset(int datasetNumber, IDataset<?, ?, ?> parseResult) {
      ObjectRankingDataset objectRankingDataset = (ObjectRankingDataset) parseResult;
      if (datasetNumber == 0) {
         int numberOfInstances = objectRankingDataset.getNumberOfInstances();
         int expectedNumberOfRankings = RANKINGS.length;
         Assert.assertEquals(String.format(ASSERT_EXPECTED_NUMBER_OF_INSTANCES, expectedNumberOfRankings, numberOfInstances),
               expectedNumberOfRankings, numberOfInstances);
         for (int instancePosition = 0; instancePosition < numberOfInstances; instancePosition++) {
            Ranking rankingOfInstance = objectRankingDataset.getRankingOfInstance(instancePosition);
            int[] compareOperators = Ranking.createCompareOperatorArrayForLabels(RANKINGS[instancePosition]);
            Ranking expectedRankingOfInstance = new Ranking(RANKINGS[instancePosition], compareOperators);
            Assert.assertEquals(String.format(ASSERT_EQUALS_RANKINGS, expectedRankingOfInstance.toString(), rankingOfInstance.toString()),
                  expectedRankingOfInstance, rankingOfInstance);
         }
         for (int i = 0; i < ITEM_FEATURES.length; i++) {
            double[] feature = objectRankingDataset.getItemVector(i);
            Assert.assertEquals(String.format(ASSERT_EQUALS_ITEM_FEATURES, Arrays.toString(ITEM_FEATURES[i]), Arrays.toString(feature)),
                  Arrays.toString(feature), Arrays.toString(ITEM_FEATURES[i]));
         }
         for (int i = 0; i < CONTEXT_FEATURES.length; i++) {
            double[] feature = objectRankingDataset.getContextVectors().get(i);
            Assert.assertEquals(
                  String.format(ASSERT_EQUALS_CONTEXT_FEATURES, Arrays.toString(CONTEXT_FEATURES[i]), Arrays.toString(feature)),
                  Arrays.toString(feature), Arrays.toString(CONTEXT_FEATURES[i]));
         }
      }
   }


}
