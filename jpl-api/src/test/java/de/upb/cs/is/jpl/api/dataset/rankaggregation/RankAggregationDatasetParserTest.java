package de.upb.cs.is.jpl.api.dataset.rankaggregation;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import de.upb.cs.is.jpl.api.dataset.ADatasetParserTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;


/**
 * Test for the GPRF dataset parser {@link RankAggregationDatasetParser}.
 * 
 * @author Andreas Kornelsen
 */
public class RankAggregationDatasetParserTest extends ADatasetParserTest {

   private static final String ASSERT_EXPECTED_NUMBER_OF_INSTANCES = "The number of instances should be %d, but is %d";
   private static final String ASSERT_EQUALS_RANKINGS = "The rankings should be equal; expectedRanking: %s, ranking: %s";
   private static final String ASSERT_EQUALS_COUNT_RANKING = "The count for the ranking should be the same; expectedCount: %d, count: %d";

   private int[] COUNT_RANKINGS = new int[] { 2, 2, 1, 1, 1, 1, 1 };
   private int[][] RANKINGS = new int[][] { { 11, 14, 12, 13, 9, 10, 8, 5, 7, 6, 4, 3, 2, 1 },
         { 11, 14, 12, 13, 9, 10, 7, 8, 5, 6, 4, 3, 2, 1 }, { 11, 14, 12, 13, 9, 10, 5, 7, 8, 6, 4, 3, 2, 1 },
         { 11, 14, 12, 13, 9, 10, 7, 8, 6, 5, 3, 4, 2, 1 }, { 11, 12, 14, 9, 13, 10, 7, 8, 5, 6, 4, 3, 2, 1 },
         { 11, 14, 12, 9, 13, 10, 7, 8, 5, 6, 4, 3, 2, 1 }, { 11, 14, 12, 13, 9, 10, 7, 8, 5, 4, 6, 3, 2, 1 } };

   private static final String RESOURCE_DIRECTORY_LEVEL = "rankaggregation" + File.separator;
   private static final String DATASET_INSTANCE_MOVIE_LENS = "instancemovielens.gprf";
   private static final String DATASET_RANK_AGGREGATION_DATASET = "ED-00006-00000004-soc.gprf";


   /**
    * Sets the resource path for this test level.
    */
   public RankAggregationDatasetParserTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IDatasetParser getDatasetParser() {
      return new RankAggregationDatasetParser();
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
      validDatasets.add(new DatasetFile(new File(getTestRessourcePathFor(DATASET_RANK_AGGREGATION_DATASET))));
      return validDatasets;
   }


   @Override
   protected void validateDataset(int datasetNumber, IDataset<?, ?, ?> parseResult) {

      RankAggregationDataset rankAggregationDataset = (RankAggregationDataset) parseResult;
      if (datasetNumber == 0) {
         int numberOfInstances = rankAggregationDataset.getNumberOfInstances();
         int expectedNumberOfRankings = RANKINGS.length;
         int expectedNumberOfCountRankings = COUNT_RANKINGS.length;

         Assert.assertEquals(String.format(ASSERT_EXPECTED_NUMBER_OF_INSTANCES, expectedNumberOfRankings, numberOfInstances),
               expectedNumberOfRankings, numberOfInstances);
         Assert.assertEquals(String.format(ASSERT_EXPECTED_NUMBER_OF_INSTANCES, expectedNumberOfCountRankings, numberOfInstances),
               expectedNumberOfCountRankings, numberOfInstances);

         for (int instancePosition = 0; instancePosition < rankAggregationDataset.getNumberOfInstances(); instancePosition++) {
            Ranking rankingOfInstance = rankAggregationDataset.getRankingOfInstance(instancePosition);

            int[] compareOperators = Ranking.createCompareOperatorArrayForLabels(RANKINGS[instancePosition]);

            Ranking expectedRankingOfInstance = new Ranking(RANKINGS[instancePosition], compareOperators);
            Assert.assertEquals(String.format(ASSERT_EQUALS_RANKINGS, expectedRankingOfInstance.toString(), rankingOfInstance.toString()),
                  expectedRankingOfInstance, rankingOfInstance);

            int countForRankingOfInstance = rankAggregationDataset.getCountForRankingOfInstance(instancePosition);
            int expectedCountForRankingOfInstance = COUNT_RANKINGS[instancePosition];

            Assert.assertEquals(String.format(ASSERT_EQUALS_COUNT_RANKING, expectedCountForRankingOfInstance, countForRankingOfInstance),
                  expectedCountForRankingOfInstance, countForRankingOfInstance);
         }
      }
   }


}
