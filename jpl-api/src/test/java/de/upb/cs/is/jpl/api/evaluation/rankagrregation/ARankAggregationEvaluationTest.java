package de.upb.cs.is.jpl.api.evaluation.rankagrregation;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.bordacount.BordaCountLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.kemenyyoung.KemenyYoungLearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationTest;
import de.upb.cs.is.jpl.api.evaluation.EvaluationMetricJsonObjectCreator;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This abstract class is used for all tests for evaluations for rank aggregation problem.
 * 
 * @author Pritha Gupta
 * 
 */
public abstract class ARankAggregationEvaluationTest extends AEvaluationTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "rankaggregation" + File.separator;
   protected static final String ERROR_REFLECTION_FAILED = "The string value cannot be acquired via reflection due to error: %s";
   protected static final String RANK_AGGREGATION_TRAIN_DATASET = "ED-00006-00000004-soc.gprf"; // CompleteOrderList
   protected static final String RANK_AGGREGATION_TEST_DATASET = "ED-00006-00000004-pwg.gprf"; // CompleteOrderList

   private final static Logger logger = LoggerFactory.getLogger(ARankAggregationEvaluationTest.class);


   /**
    * Creates a new unit test for rank aggregation evaluations with the additional path to the
    * resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ARankAggregationEvaluationTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   /**
    * Creates a new unit test for rank aggregation evaluations without any additional path to the
    * resources.
    */
   public ARankAggregationEvaluationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      List<ILearningAlgorithm> learningAlgorithms = new ArrayList<>();
      learningAlgorithms.add(new KemenyYoungLearningAlgorithm());
      learningAlgorithms.add(new BordaCountLearningAlgorithm());
      return learningAlgorithms;
   }


   @Override
   public void setEvaluationMetrics() {
      evaluationMetrics = new ArrayList<>();
      EMetric eEvaluationMetric = EMetric.KENDALLS_TAU;
      evaluationMetrics.add(eEvaluationMetric.createEvaluationMetric());
      eEvaluationMetric = EMetric.SPEARMANS_RANK_CORRELATION;
      evaluationMetrics.add(eEvaluationMetric.createEvaluationMetric());
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      List<Pair<String, JsonObject>> returnList = new ArrayList<Pair<String, JsonObject>>();
      String errorMessageMetric = StringUtils.EMPTY_STRING;

      try {
         errorMessageMetric = TestUtils.getStringByReflection(AEvaluationConfiguration.class, "VALIDATION_EVALUATIONMETRICS_ERROR_MESSAGE");
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         logger.error(String.format(ERROR_REFLECTION_FAILED, e.getMessage()));
      }

      List<EvaluationMetricJsonObjectCreator> evaluationMetricElementList = new ArrayList<>();
      evaluationMetricElementList.add(new EvaluationMetricJsonObjectCreator("kendalls_tau", new JsonObject()));
      evaluationMetricElementList.add(new EvaluationMetricJsonObjectCreator("spearman_correlation2", new JsonObject()));
      JsonObject evaluationMetric = EvaluationMetricJsonObjectCreator.getEvalautionMetricJsonArray(evaluationMetricElementList);

      returnList.add(Pair.of(String.format(errorMessageMetric, "spearman_correlation2"), evaluationMetric));
      return returnList;
   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<JsonObject> returnList = new ArrayList<JsonObject>();
      List<EvaluationMetricJsonObjectCreator> evaluationMetricElementList = new ArrayList<>();
      evaluationMetricElementList.add(new EvaluationMetricJsonObjectCreator("kendalls_tau", new JsonObject()));
      evaluationMetricElementList.add(new EvaluationMetricJsonObjectCreator("spearman_correlation", new JsonObject()));
      JsonObject evaluationMetric = EvaluationMetricJsonObjectCreator.getEvalautionMetricJsonArray(evaluationMetricElementList);
      returnList.add(evaluationMetric);
      return returnList;
   }


   /**
    * {@inheritDoc} Added evaluation output for rank aggregation.
    */
   @Override
   protected List<Pair<EvaluationSetting, EvaluationResult>> createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
         double[][] lossArraysForAlgorithms, String datasetName, String testsetFileName) {
      List<Pair<EvaluationSetting, EvaluationResult>> settingsAndResults = super.createListOfEvaluationSettingAndResultPairsForLossArrays(
            lossArraysForAlgorithms, datasetName, testsetFileName, StringUtils.EMPTY_STRING);
      for (Pair<EvaluationSetting, EvaluationResult> setingAndResult : settingsAndResults) {
         Ranking predicted;
         try {
            predicted = (Ranking) setingAndResult.getFirst().getLearningModel().predict(setingAndResult.getFirst().getDataset()).get(0);
            setingAndResult.getSecond().setExtraEvaluationInformation(predicted.toString());
         } catch (PredictionFailedException e) {
            e.printStackTrace();
         }
      }
      return settingsAndResults;
   }


   /**
    * {@inheritDoc} Added correct evaluation output for rank aggregation.
    */
   @Override
   protected List<Pair<EvaluationSetting, EvaluationResult>> createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
         double[][] lossArraysForAlgorithms, String datasetName) {
      List<Pair<EvaluationSetting, EvaluationResult>> settingsAndResults = super.createListOfEvaluationSettingAndResultPairsForLossArrays(
            lossArraysForAlgorithms, datasetName, StringUtils.EMPTY_STRING);
      for (Pair<EvaluationSetting, EvaluationResult> setingAndResult : settingsAndResults) {
         Ranking predicted;
         try {
            predicted = (Ranking) setingAndResult.getFirst().getLearningModel().predict(setingAndResult.getFirst().getDataset()).get(0);
            setingAndResult.getSecond().setExtraEvaluationInformation(predicted.toString());
         } catch (PredictionFailedException e) {
            e.printStackTrace();
         }
      }
      return settingsAndResults;
   }
}
