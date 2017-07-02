package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.plackettluce;


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
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains the unit tests for the Plackett-Luce algorithm.
 * 
 * @author Andreas Kornelsen
 *
 */
public class PlackettLuceLearningAlgorithmTest extends ARankAggregationTest {

   private static final String RANK_AGGREGATION_DATASET = "ED-00006-00000004-soc.gprf";
   private static final String RANK_AGGREGATION_WITH_EQUALS_DATASET = "ED-00006-00000004-soc_with_equals_comperator.gprf";
   private static final String INSTANCE_DATASET = "instancerankingtest.gprf";
   private static final String ASSERT_TRUE_PREDICTION = "The predicted ranking should be the same like expected, expectedRanking: %s.";


   private static final String RESOURCE_DIRECTORY_LEVEL = "plackettluce" + File.separator;


   /**
    * Creates a new unit test for the {@link PlackettLuceLearningAlgorithm}.
    */
   public PlackettLuceLearningAlgorithmTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new PlackettLuceLearningAlgorithm();
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
      int[] objectList = new int[] { 10, 11, 13, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 12 };
      Ranking ranking = new Ranking(objectList, Ranking.createCompareOperatorArrayForLabels(objectList));
      List<Ranking> rankings = Arrays.asList(ranking);
      Pair<IDataset<Integer, NullType, Ranking>, List<Ranking>> result = Pair.of((RankAggregationDataset) this
            .createDatasetOutOfFile(new RankAggregationDatasetParser(), getTestRessourcePathFor(RANK_AGGREGATION_DATASET)), rankings);

      return Arrays.asList(result);
   }


   /**
    * Tests {@link PlackettLuceLearningAlgorithm} with a correct formated dataset and test the
    * predict result.
    * 
    * @throws ParsingFailedException if the Parser encountered a problem while parsing the file
    * @throws TrainModelsFailedException if the training failed
    * @throws PredictionFailedException if an error occurs during the training of the algorithm
    */
   @Test
   public void plackettLuceAlgorithmTestCorrectInput()
         throws ParsingFailedException,
            TrainModelsFailedException,
            PredictionFailedException {
      File file = new File(getTestRessourcePathFor(RANK_AGGREGATION_DATASET));

      PlackettLuceLearningAlgorithm algorithm = new PlackettLuceLearningAlgorithm();

      RankAggregationDatasetParser datasetParser = (RankAggregationDatasetParser) algorithm.getDatasetParser();

      DatasetFile datasetFile = new DatasetFile(file);

      IDataset<?, ?, ?> dataset = datasetParser.parse(datasetFile);

      ILearningModel<?> train = algorithm.train(dataset);
      PlackettLuceLearningModel model = (PlackettLuceLearningModel) train;

      Ranking predict = model.predict(dataset).get(0);
      int[] objectList = new int[] { 10, 11, 13, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 12 };
      Ranking ranking = new Ranking(objectList, Ranking.createCompareOperatorArrayForLabels(objectList));
      assertEquals(String.format(ASSERT_TRUE_PREDICTION, ranking.toString()), ranking, predict);

   }


   /**
    * Tests {@link PlackettLuceLearningAlgorithm} with a format which doesn't work for the
    * {@link PlackettLuceLearningAlgorithm}.
    * 
    * @throws ParsingFailedException if the Parser encountered a problem while parsing the file
    * @throws TrainModelsFailedException if the training failed
    */
   @Test(expected = TrainModelsFailedException.class)
   public void plackettLuceAlgorithmTestIncorrectInput() throws ParsingFailedException, TrainModelsFailedException {
      File file = new File(getTestRessourcePathFor(RANK_AGGREGATION_WITH_EQUALS_DATASET));

      PlackettLuceLearningAlgorithm algorithm = new PlackettLuceLearningAlgorithm();

      RankAggregationDatasetParser datasetParser = (RankAggregationDatasetParser) algorithm.getDatasetParser();

      DatasetFile datasetFile = new DatasetFile(file);

      IDataset<?, ?, ?> dataset = datasetParser.parse(datasetFile);

      algorithm.train(dataset);
   }


   /**
    * Use wrong dataset for {@link PlackettLuceLearningAlgorithm} to get a TrainFailedException. The
    * instance ranking dataset is used for the test.
    * 
    * @throws ParsingFailedException if the parsing of the dataset fails
    * @throws TrainModelsFailedException if the training of the models fails
    */
   @Test(expected = ParsingFailedException.class)
   public void algorithmTestIncorrectInputWrongDataset() throws ParsingFailedException, TrainModelsFailedException {
      File file = new File(getTestRessourcePathFor(INSTANCE_DATASET));

      PlackettLuceLearningAlgorithm algorithm = new PlackettLuceLearningAlgorithm();

      RankAggregationDatasetParser datasetParser = (RankAggregationDatasetParser) algorithm.getDatasetParser();

      DatasetFile datasetFile = new DatasetFile(file);

      IDataset<?, ?, ?> dataset = datasetParser.parse(datasetFile);

      algorithm.train(dataset);
   }

}
