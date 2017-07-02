package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.bordacount;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.ARankAggregationTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDatasetParser;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDataset;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains the unit tests for the borda count algorithm.
 * 
 * @author Andreas Kornelsen
 *
 */
public class BordaCountLearningAlgorithmTest extends ARankAggregationTest {

   private static final String RANK_AGGREGATION_DATASET = "ED-00006-00000004-soc.gprf";
   private static final String RANK_AGGREGATION_WITH_EQUALS_DATASET = "ED-00006-00000004-soc_with_equals_comperator.gprf";
   private static final String INSTANCE_DATASET = "instancerankingtest.gprf";
   private static final String ASSERT_TRUE_PREDICTION = "The predicted ranking should be the same like expected, expectedRanking: %s.";


   private static final String RESOURCE_DIRECTORY_LEVEL = "bordacount" + File.separator;


   /**
    * Creates a new unit test for the {@link BordaCountLearningAlgorithm}.
    */
   public BordaCountLearningAlgorithmTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new BordaCountLearningAlgorithm();
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> wrongDataset = new ArrayList<IDataset<?, ?, ?>>();
      wrongDataset.add(
            this.createDatasetOutOfFile(new RankAggregationDatasetParser(), getTestRessourcePathFor(RANK_AGGREGATION_WITH_EQUALS_DATASET)));
      wrongDataset.add(this.createDatasetOutOfFile(new InstanceRankingDatasetParser(), getTestRessourcePathFor(INSTANCE_DATASET)));

      return wrongDataset;
   }


   @Override
   public List<IDataset<Integer, NullType, Ranking>> getCorrectDatasetList() {
      return Arrays.asList((RankAggregationDataset) this.createDatasetOutOfFile(new RankAggregationDatasetParser(),
            getTestRessourcePathFor(RANK_AGGREGATION_DATASET)));
   }


   @Override
   public List<Pair<IDataset<Integer, NullType, Ranking>, List<Ranking>>> getPredictionsForDatasetList() {

      int[] objectList = new int[] { 10, 13, 11, 12, 8, 9, 6, 7, 4, 5, 3, 2, 1, 0 };
      Ranking ranking = new Ranking(objectList, Ranking.createCompareOperatorArrayForLabels(objectList));
      List<Ranking> rankings = Arrays.asList(ranking);
      Pair<IDataset<Integer, NullType, Ranking>, List<Ranking>> result = Pair.of((RankAggregationDataset) this
            .createDatasetOutOfFile(new RankAggregationDatasetParser(), getTestRessourcePathFor(RANK_AGGREGATION_DATASET)), rankings);

      return Arrays.asList(result);
   }


   /**
    * Tests Borda Count algorithm with a correct formated dataset and test the predict result.
    * 
    * @throws ParsingFailedException if the Parser encountered a problem while parsing the file
    * @throws TrainModelsFailedException if the training failed
    */
   @Test
   public void bordaCountAlgorithmTestCorrectInput() throws ParsingFailedException, TrainModelsFailedException {
      File file = new File(getTestRessourcePathFor(RANK_AGGREGATION_DATASET));

      BordaCountLearningAlgorithm bordaCountAlgorithm = new BordaCountLearningAlgorithm();

      RankAggregationDatasetParser datasetParser = (RankAggregationDatasetParser) bordaCountAlgorithm.getDatasetParser();

      DatasetFile bordaCountDatasetFile = new DatasetFile(file);

      IDataset<?, ?, ?> bordaCountDataset = datasetParser.parse(bordaCountDatasetFile);

      ILearningModel<?> train = bordaCountAlgorithm.train(bordaCountDataset);
      BordaCountLearningModel model = (BordaCountLearningModel) train;

      Ranking predict = model.predict();
      int[] objectList = new int[] { 10, 13, 11, 12, 8, 9, 6, 7, 4, 5, 3, 2, 1, 0 };
      Ranking ranking = new Ranking(objectList, Ranking.createCompareOperatorArrayForLabels(objectList));
      assertEquals(String.format(ASSERT_TRUE_PREDICTION, ranking.toString()), ranking, predict);

   }


   /**
    * Tests Borda Count algorithm with a format which doesn't work for Borda Count.
    * 
    * @throws ParsingFailedException if the Parser encountered a problem while parsing the file
    * @throws TrainModelsFailedException if the training failed
    */
   @Test(expected = TrainModelsFailedException.class)
   public void bordaCountAlgorithmTestIncorrectInput() throws ParsingFailedException, TrainModelsFailedException {
      File file = new File(getTestRessourcePathFor(RANK_AGGREGATION_WITH_EQUALS_DATASET));

      BordaCountLearningAlgorithm bordaCountAlgorithm = new BordaCountLearningAlgorithm();

      RankAggregationDatasetParser datasetParser = (RankAggregationDatasetParser) bordaCountAlgorithm.getDatasetParser();

      DatasetFile bordaCountDatasetFile = new DatasetFile(file);

      IDataset<?, ?, ?> bordaCountDataset = datasetParser.parse(bordaCountDatasetFile);

      bordaCountAlgorithm.train(bordaCountDataset);
   }


   /**
    * Use wrong dataset for Borda Count to get a TrainFailedException. The instance ranking dataset
    * is used for the test.
    * 
    * @throws ParsingFailedException if the parsing of the dataset fails
    * @throws TrainModelsFailedException if the training of the models fails
    */
   @Test(expected = ParsingFailedException.class)
   public void bordaCountAlgorithmTestIncorrectInputWrongDataset() throws ParsingFailedException, TrainModelsFailedException {
      File file = new File(getTestRessourcePathFor(INSTANCE_DATASET));

      BordaCountLearningAlgorithm bordaCountAlgorithm = new BordaCountLearningAlgorithm();

      RankAggregationDatasetParser datasetParser = (RankAggregationDatasetParser) bordaCountAlgorithm.getDatasetParser();

      DatasetFile bordaCountDatasetFile = new DatasetFile(file);

      IDataset<?, ?, ?> bordaCountDataset = datasetParser.parse(bordaCountDatasetFile);

      bordaCountAlgorithm.train(bordaCountDataset);
   }

}
