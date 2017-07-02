package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.kemenyyoung;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.ARankAggregationTest;
import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
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
 * This class contains the unit tests for the Kemeny-Young Algorithm.
 * 
 * @author Pritha Gupta
 *
 */
public class KemenyYoungLearningAlgorithmTest extends ARankAggregationTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "kemenyyoung" + File.separator;

   private static final String RANK_AGGREGATION_DATASET_WITH_EQUALS = "ED-00006-00000004-soc_with_equals_comperator.gprf";
   private static final String RANK_AGGREGATION_NETFLIX_DATASET = "netflix1-soc.gprf";
   private static final String INSTANCE_RANKING_DATASET = "instancerankingtest.gprf";


   /**
    * Creates a new unit test for the {@link KemenyYoungLearningAlgorithm}.
    */
   public KemenyYoungLearningAlgorithmTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * This method is run before all the test run. It setup logging for tests.
    * 
    */
   @Before
   public void setupTests() {
      LoggingConfiguration.setupLoggingConfiguration();
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new KemenyYoungLearningAlgorithm();
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> wrongDataset = new ArrayList<IDataset<?, ?, ?>>();
      wrongDataset.add(
            this.createDatasetOutOfFile(new RankAggregationDatasetParser(), getTestRessourcePathFor(RANK_AGGREGATION_DATASET_WITH_EQUALS)));
      wrongDataset.add(this.createDatasetOutOfFile(new InstanceRankingDatasetParser(), getTestRessourcePathFor(INSTANCE_RANKING_DATASET)));
      return wrongDataset;
   }


   @Override
   public List<IDataset<Integer, NullType, Ranking>> getCorrectDatasetList() {
      return Arrays.asList((RankAggregationDataset) this.createDatasetOutOfFile(new RankAggregationDatasetParser(),
            getTestRessourcePathFor(RANK_AGGREGATION_NETFLIX_DATASET)));
   }


   @Override
   public List<Pair<IDataset<Integer, NullType, Ranking>, List<Ranking>>> getPredictionsForDatasetList() {
      int[] objectList = new int[] { 1, 0, 2 };
      List<Ranking> expected = Arrays.asList(new Ranking(objectList, Ranking.createCompareOperatorArrayForLabels(objectList)));
      RankAggregationDataset dataset = (RankAggregationDataset) this.createDatasetOutOfFile(new RankAggregationDatasetParser(),
            getTestRessourcePathFor(RANK_AGGREGATION_NETFLIX_DATASET));
      return Arrays.asList(Pair.of(dataset, expected));
   }


   /**
    * The test method to check the validDataset and training of {@link KemenyYoungLearningModel} on
    * it.
    * 
    * @throws ParsingFailedException if the Parser encountered a problem while parsing the file.
    * @throws TrainModelsFailedException if the training failed.
    * @throws PredictionFailedException if prediction fails
    */
   @Test
   public void kemenyAlgorithmTestCorrectInput() throws ParsingFailedException, TrainModelsFailedException, PredictionFailedException {
      File file = new File(getTestRessourcePathFor(RANK_AGGREGATION_NETFLIX_DATASET));
      KemenyYoungLearningAlgorithm kemenyYoungAlgorithm = new KemenyYoungLearningAlgorithm();
      DatasetFile kemenyDatasetFile = new DatasetFile(file);
      RankAggregationDatasetParser kemenyYoungDatasetParser = (RankAggregationDatasetParser) kemenyYoungAlgorithm.getDatasetParser();
      IDataset<?, ?, ?> kemenyYoungDataset = kemenyYoungDatasetParser.parse(kemenyDatasetFile);
      ILearningModel<?> train = kemenyYoungAlgorithm.train(kemenyYoungDataset);
      KemenyYoungLearningModel kemneyYoungLearningModel = (KemenyYoungLearningModel) train;

      Ranking predict = null;
      if (kemneyYoungLearningModel != null) {
         predict = kemneyYoungLearningModel.predict(kemenyYoungDataset.getInstance(0));
      }
      int[] objectList = new int[] { 1, 0, 2 };
      Ranking expectedResult = new Ranking(objectList, Ranking.createCompareOperatorArrayForLabels(objectList));
      assertNotNull(predict);
      assertTrue(predict.equals(expectedResult));
   }


   /**
    * The test method to check the invalidDataset and training of {@link KemenyYoungLearningModel}
    * on it. It show get TrainModelsFailedException because we parse the dataset with the other
    * comparators which are not compatible for Kemeny-Young Algorithm.
    * 
    * @throws ParsingFailedException if the Parser encountered a problem while parsing the file
    * @throws TrainModelsFailedException if the training failed
    */
   @Test(expected = TrainModelsFailedException.class)
   public void kemenyAlgorithmTestIncorrectInput() throws ParsingFailedException, TrainModelsFailedException {
      File file = new File(getTestRessourcePathFor(RANK_AGGREGATION_DATASET_WITH_EQUALS));
      KemenyYoungLearningAlgorithm kemenyYoungAlgorithm = new KemenyYoungLearningAlgorithm();
      DatasetFile kemenyDatasetFile = new DatasetFile(file);
      RankAggregationDatasetParser kemenyYoungDatasetParser = (RankAggregationDatasetParser) kemenyYoungAlgorithm.getDatasetParser();
      IDataset<?, ?, ?> kemenyYoungDataset = kemenyYoungDatasetParser.parse(kemenyDatasetFile);
      kemenyYoungDataset = kemenyYoungDatasetParser.parse(kemenyDatasetFile);
      kemenyYoungAlgorithm.train(kemenyYoungDataset);
   }


   /**
    * The test method to check the invalidDataset and training of {@link KemenyYoungLearningModel}
    * on it. It show get ParsingFailedException because we parse the instance ranking dataset which
    * are not compatible for Rank Aggregation problem.
    * 
    * @throws ParsingFailedException if the Parser encountered a problem while parsing the file
    * @throws TrainModelsFailedException if the training failed
    */
   @Test(expected = ParsingFailedException.class)
   public void kemenyAlgorithmTestIncorrectInputWrongDataset() throws ParsingFailedException, TrainModelsFailedException {
      File file = new File(getTestRessourcePathFor(INSTANCE_RANKING_DATASET));
      KemenyYoungLearningAlgorithm kemenyYoungAlgorithm = new KemenyYoungLearningAlgorithm();
      DatasetFile kemenyDatasetFile = new DatasetFile(file);
      RankAggregationDatasetParser kemenyYoungDatasetParser = (RankAggregationDatasetParser) kemenyYoungAlgorithm.getDatasetParser();
      IDataset<?, ?, ?> kemenyYoungDataset = kemenyYoungDatasetParser.parse(kemenyDatasetFile);
      kemenyYoungDataset = kemenyYoungDatasetParser.parse(kemenyDatasetFile);
      kemenyYoungAlgorithm.train(kemenyYoungDataset);
   }
}
