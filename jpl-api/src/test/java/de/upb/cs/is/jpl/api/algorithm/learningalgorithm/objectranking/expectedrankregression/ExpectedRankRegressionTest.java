package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.expectedrankregression;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
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
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.spearmancorrelation.SpearmansCorrelation;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The test class for {@link ExpectedRankRegression}.
 * 
 * @author Pritha Gupta
 *
 */
public class ExpectedRankRegressionTest extends AObjectRankingTest {

   private static final Logger logger = LoggerFactory.getLogger(ExpectedRankRegressionTest.class);
   private int[][] RANKINGS = new int[][] { { 2, 15, 6, 1, 13, 25, 46, 55, 74, 56 }, { 8, 0, 9, 3, 4, 5, 43, 24, 29, 40 },
         { 19, 9, 13, 4, 47, 31, 30, 55, 50, 99 } };

   private static final String LEARNING_RATE_IDENTIFIER = "learning_rate";
   private static final String LEARNING_RATE = "0.2";


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new ExpectedRankRegression();
   }


   @Override
   public List<Pair<IDataset<double[], List<double[]>, Ranking>, List<Ranking>>> getPredictionsForDatasetList() {
      List<Ranking> predictedRankings = new ArrayList<>();
      ObjectRankingDataset dataset = (ObjectRankingDataset) this.createDatasetOutOfFile(new ObjectRankingDatasetParser(),
            getTestRessourcePathFor(OBJECT_RANKING_DATASET_B));
      ObjectRankingDataset partDataset = (ObjectRankingDataset) dataset.getPartOfDataset(2, 5);
      for (int i = 0; i < RANKINGS.length; i++) {
         predictedRankings.add(new Ranking(RANKINGS[i], Ranking.createCompareOperatorArrayForLabels(RANKINGS[i])));
      }
      return Arrays.asList(Pair.of(partDataset, predictedRankings));
   }


   /**
    * The test method to check the validDataset and training of
    * {@link ExpectedRankRegressionLearningModel} on it.
    * 
    * @throws ParsingFailedException if the Parser encountered a problem while parsing the file
    * @throws TrainModelsFailedException if the training failed
    * @throws PredictionFailedException if the linear regression prediction fails
    * @throws LossException if loss cannot be calculated
    */
   @Test
   public void expectedRankRegressionTestCorrectInput()
         throws ParsingFailedException,
            TrainModelsFailedException,
            PredictionFailedException,
            LossException {
      File file = new File(getTestRessourcePathFor(OBJECT_RANKING_DATASET_B));
      ExpectedRankRegression expectedRankRegressionAlgorithm = new ExpectedRankRegression();
      DatasetFile datasetFile = new DatasetFile(file);
      ObjectRankingDatasetParser expectedRankRegressionDatasetParser = (ObjectRankingDatasetParser) expectedRankRegressionAlgorithm
            .getDatasetParser();
      IDataset<?, ?, ?> expectedRankRegressionDataset = expectedRankRegressionDatasetParser.parse(datasetFile);
      IInstance<?, ?, ?> instance = expectedRankRegressionDataset.getInstance(505);
      IDataset<?, ?, ?> testSet = expectedRankRegressionDataset.getPartOfDataset(500, expectedRankRegressionDataset.getNumberOfInstances());
      expectedRankRegressionDataset = expectedRankRegressionDataset.getPartOfDataset(0, 500);
      ILearningModel<?> train = expectedRankRegressionAlgorithm.train(expectedRankRegressionDataset);
      ExpectedRankRegressionLearningModel expectedRankRegressionLearningModel = (ExpectedRankRegressionLearningModel) train;

      Ranking predict = null;
      List<Ranking> predictedRankingsOnTestSet = new ArrayList<>();
      if (expectedRankRegressionLearningModel != null) {
         predict = expectedRankRegressionLearningModel.predict(instance);
         predictedRankingsOnTestSet = expectedRankRegressionLearningModel.predict(testSet);
      }
      int[] objectList = new int[] { 8, 0, 21, 12, 7, 14, 16, 24, 30, 59 };
      Ranking expectedResult = new Ranking(objectList, Ranking.createCompareOperatorArrayForLabels(objectList));
      assertNotNull(predict);
      assertTrue(expectedResult.getObjectList().length == predict.getObjectList().length);
      assertTrue(expectedResult.getCompareOperators().length == predict.getCompareOperators().length);
      assertTrue(expectedResult.equals(predict));
      List<Ranking> expected = ((ObjectRankingDataset) testSet).getRankings();
      SpearmansCorrelation correlation = new SpearmansCorrelation();
      double rho = correlation.getAggregatedLossForRatings(expected, predictedRankingsOnTestSet);
      Assert.assertEquals(0.17740336700336695, rho, 0.0001);

   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<JsonObject> returnList = new ArrayList<>();
      JsonObject object = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
            new BaseLearnerJsonObjectCreator(EBaseLearner.LINEAR_REGRESSION.getBaseLearnerIdentifier(), new JsonObject()));
      returnList.add(object);
      object = BaseLearnerJsonObjectCreator
            .getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(EBaseLearner.LOGISTIC_REGRESSION.getBaseLearnerIdentifier(),
                  JsonUtils.createJsonObjectFromKeyAndValue(LEARNING_RATE_IDENTIFIER, LEARNING_RATE)));


      returnList.add(object);

      return returnList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      List<Pair<String, JsonObject>> returnList = new ArrayList<>();

      try {
         JsonObject object = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
               new BaseLearnerJsonObjectCreator(EBaseLearner.SVM_CLASSIFICATION.getBaseLearnerIdentifier(), new JsonObject()));
         String exceptionString = TestUtils.getStringByReflection(AAlgorithmConfigurationWithBaseLearner.class,
               PARAMETER_BASELEARNER_NOT_REGRESSION_REFLECTION_VARIABLE);
         returnList.add(Pair.of(String.format(exceptionString, EBaseLearner.SVM_CLASSIFICATION.getBaseLearnerIdentifier()), object));

         object = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
               new BaseLearnerJsonObjectCreator(EBaseLearner.PERCEPTRON.getBaseLearnerIdentifier(), new JsonObject()));
         returnList.add(Pair.of(String.format(exceptionString, EBaseLearner.PERCEPTRON.getBaseLearnerIdentifier()), object));


      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         Assert.fail(ERROR_REFLECTION_FAILED);
         logger.error(ERROR_REFLECTION_FAILED);
      }

      return returnList;
   }

}
