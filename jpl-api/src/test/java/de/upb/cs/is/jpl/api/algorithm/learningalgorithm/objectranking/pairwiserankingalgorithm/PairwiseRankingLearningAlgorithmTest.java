package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.pairwiserankingalgorithm;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.BaseLearnerJsonObjectCreator;
import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.AObjectRankingTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.spearmancorrelation.SpearmansCorrelation;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Unit test for {@link PairwiseRankingLearningAlgorithm}.
 * 
 * @author Pritha Gupta
 *
 */
public class PairwiseRankingLearningAlgorithmTest extends AObjectRankingTest {


   private static final Logger logger = LoggerFactory.getLogger(PairwiseRankingLearningAlgorithmTest.class);

   private int[][] RANKINGS = new int[][] { { 7, 3, 5, 2, 1, 0, 4, 8, 6, 9 }, { 7, 3, 5, 2, 1, 0, 4, 8, 6, 9 },
         { 7, 3, 5, 2, 1, 0, 4, 8, 6, 9 } };


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      PairwiseRankingLearningAlgorithm algorithm = new PairwiseRankingLearningAlgorithm();
      JsonObject object = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
            new BaseLearnerJsonObjectCreator(EBaseLearner.LOGISTIC_CLASSIFICATION.getBaseLearnerIdentifier(), new JsonObject()));
      JsonObject methodType = JsonUtils.createJsonObjectFromKeyAndValue("method_type", "svor");
      try {
         algorithm.setParameters(object);
         algorithm.setParameters(methodType);
      } catch (ParameterValidationFailedException e) {
         e.printStackTrace();
      }
      return algorithm;
   }


   @Override
   public List<Pair<IDataset<double[], List<double[]>, Ranking>, List<Ranking>>> getPredictionsForDatasetList() {

      List<Ranking> predictedRankings = new ArrayList<>();
      ObjectRankingDataset dataset = (ObjectRankingDataset) this.createDatasetOutOfFile(new ObjectRankingDatasetParser(),
            getTestRessourcePathFor(OBJECT_RANKING_DATASET_A));
      ObjectRankingDataset partDataset = (ObjectRankingDataset) dataset.getPartOfDataset(15, 18);
      for (int i = 0; i < RANKINGS.length; i++) {
         predictedRankings.add(new Ranking(RANKINGS[i], Ranking.createCompareOperatorArrayForLabels(RANKINGS[i])));
      }
      return Arrays.asList(Pair.of(partDataset, predictedRankings));
   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<JsonObject> returnList = new ArrayList<>();
      JsonObject object = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
            new BaseLearnerJsonObjectCreator(EBaseLearner.LOGISTIC_CLASSIFICATION.getBaseLearnerIdentifier(), new JsonObject()));
      returnList.add(object);
      object = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
            new BaseLearnerJsonObjectCreator(EBaseLearner.SVM_CLASSIFICATION.getBaseLearnerIdentifier(), new JsonObject()));

      object = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
            new BaseLearnerJsonObjectCreator(EBaseLearner.PERCEPTRON.getBaseLearnerIdentifier(), new JsonObject()));

      returnList.add(object);

      return returnList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      List<Pair<String, JsonObject>> returnList = new ArrayList<>();

      try {
         JsonObject object = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
               new BaseLearnerJsonObjectCreator(EBaseLearner.LINEAR_REGRESSION.getBaseLearnerIdentifier(), new JsonObject()));
         String exceptionString = TestUtils.getStringByReflection(AAlgorithmConfigurationWithBaseLearner.class,
               PARAMETER_BASELEARNER_NOT_CLASSIFIER_REFLECTION_VARIABLE);
         returnList.add(Pair.of(String.format(exceptionString, EBaseLearner.LINEAR_REGRESSION.getBaseLearnerIdentifier()), object));

         object = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
               new BaseLearnerJsonObjectCreator(EBaseLearner.LOGISTIC_REGRESSION.getBaseLearnerIdentifier(), new JsonObject()));
         returnList.add(Pair.of(String.format(exceptionString, EBaseLearner.LOGISTIC_REGRESSION.getBaseLearnerIdentifier()), object));


      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         Assert.fail(ERROR_REFLECTION_FAILED);
         logger.error(ERROR_REFLECTION_FAILED);
      }

      return returnList;
   }


   @Override
   public List<IDataset<double[], List<double[]>, Ranking>> getCorrectDatasetList() {
      List<IDataset<double[], List<double[]>, Ranking>> correctDatasets = new ArrayList<>();
      ObjectRankingDataset objectRankingDataset = (ObjectRankingDataset) this.createDatasetOutOfFile(new ObjectRankingDatasetParser(),
            getTestRessourcePathFor(OBJECT_RANKING_DATASET_A));
      correctDatasets.add(objectRankingDataset);
      return correctDatasets;

   }


   /**
    * The test method to check the validDataset and training of {@link PairwiseRankingLearningModel}
    * on it.
    * 
    * @throws ParsingFailedException if the Parser encountered a problem while parsing the file
    * @throws TrainModelsFailedException if the training failed
    * @throws PredictionFailedException if the linear regression prediction fails
    * @throws LossException if loss cannot be calculated
    */
   @Test
   @Ignore
   public void pairwiseRankingAlgorithmTestCorrectInput()
         throws ParsingFailedException,
            TrainModelsFailedException,
            PredictionFailedException,
            LossException {
      File file = new File(getTestRessourcePathFor(OBJECT_RANKING_DATASET_B));
      PairwiseRankingLearningAlgorithm algortihm = new PairwiseRankingLearningAlgorithm();
      DatasetFile datasetFile = new DatasetFile(file);
      ObjectRankingDatasetParser objectRankingParser = (ObjectRankingDatasetParser) algortihm.getDatasetParser();
      IDataset<?, ?, ?> objectRankingDataset = objectRankingParser.parse(datasetFile);
      IInstance<?, ?, ?> instance = objectRankingDataset.getInstance(505);
      IDataset<?, ?, ?> testSet = objectRankingDataset.getPartOfDataset(515, 518);
      objectRankingDataset = objectRankingDataset.getPartOfDataset(0, 10);
      ILearningModel<?> train = algortihm.train(objectRankingDataset);
      PairwiseRankingLearningModel castedLearningModel = (PairwiseRankingLearningModel) train;
      Ranking predict = null;
      List<Ranking> predictedRankingsOnTestSet = new ArrayList<>();
      if (castedLearningModel != null) {
         predict = castedLearningModel.predict(instance);
         predictedRankingsOnTestSet = castedLearningModel.predict(testSet);
      }
      int[] objectList = new int[] { 0, 8, 21, 16, 7, 12, 30, 24, 59, 14 };
      Ranking expectedResult = new Ranking(objectList, Ranking.createCompareOperatorArrayForLabels(objectList));
      assertNotNull(predict);
      assertTrue(expectedResult.getObjectList().length == predict.getObjectList().length);
      assertTrue(expectedResult.getCompareOperators().length == predict.getCompareOperators().length);
      assertTrue(expectedResult.equals(predict));
      List<Ranking> expected = ((ObjectRankingDataset) testSet).getRankings();
      SpearmansCorrelation correlation = new SpearmansCorrelation();
      double rho = correlation.getAggregatedLossForRatings(expected, predictedRankingsOnTestSet);
      Assert.assertEquals(0.5191919191919192, rho, 0.0001);

   }
}
