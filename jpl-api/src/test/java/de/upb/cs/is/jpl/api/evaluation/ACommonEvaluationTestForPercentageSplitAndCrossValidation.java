package de.upb.cs.is.jpl.api.evaluation;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Class containing functions common to the cross validation and percentage split evaluation.
 * 
 * @author Pritha Gupta
 *
 */
public abstract class ACommonEvaluationTestForPercentageSplitAndCrossValidation extends AEvaluationTest {
   private static final String ERROR_CORRECT_EVALUATION_RESULT_NOT_ACHIEVED_FOR_CORRECT_SET_OF_SETTING = "The evaluation should evaluate the result:\n%s for set of setting:\n%s.";
   private static final String ASSERT_CORRECT_EVALUATION_RESULT_ACHIEVED_FOR_INCORRECT_SET_OF_SETTING = "The evaluation should not evaluate the result:\n%s for set of setting:\n%s";
   private static final String ASSERT_CORRECT_LOSS_ACHIEVED_FOR_INCORRECT_SETTING = "The evaluation should not evaluate the loss:%f for setting:\n%s";
   private static final String ERROR_EXCEPTION_NOT_OCCURR_ERROR_MESSAGE = "There should be no exception for the set of evaluation setting %s";
   private static final String DEBUG_MESSAGE_EVALUATION_RESULT = "File: %s, expectedLoss and evaluatedLoss for %s: %f, %f";

   protected static final String COULD_NOT_SPLIT_THE_DATASET_INTO_TRAINING_AND_TESTING_DATSET = "Could not split the dataset into training and testing datset";

   private static final Logger logger = LoggerFactory.getLogger(ACommonEvaluationTestForPercentageSplitAndCrossValidation.class);


   /**
    * Creates a new unit test for cross validation and percentage split evaluation with the
    * additional path to the resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ACommonEvaluationTestForPercentageSplitAndCrossValidation(String additionalResourcePath) {
      super(additionalResourcePath);
   }


   /**
    * Returns list of {@link Pair} of one set of {@link EvaluationSetting}s ({@link Pair} of list of
    * evaluation settings with set number) paired with the {@link EvaluationResult} which will
    * evaluated correctly on the set by the {@link IEvaluation}. The result is provided with the
    * list of settings with set number as a pair.
    * 
    * @return a list of evaluation settings set paired with correct evaluation result
    */
   public abstract List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithCorrectEvaluationResult();


   /**
    * Returns list of {@link Pair} of one set of {@link EvaluationSetting}s ({@link Pair} of list of
    * evaluation settings with set number) paired with the {@link EvaluationResult} which will
    * evaluated in-correctly on the set by the {@link IEvaluation}. The result is provided with the
    * list of settings with set number as a pair.
    * 
    * 
    * @return a list of evaluation settings set paired with wrong evaluation result
    */
   public abstract List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListEvaluationSettingsSetWithWrongEvaluationResult();


   /**
    * Test if the evaluation be able to detect correct evaluation result for the given set of
    * evaluation settings. It asserts equality of losses and the extra evaluation output in the
    * common evaluated result after running evaluation for one set, which combines all results to
    * one {@link EvaluationResult} after taking mean of all losses and provided results.
    */
   @Override
   @Test
   public void testCorrectEvaluationSettings() {
      evaluation = (AEvaluation<?>) getEvaluation();
      List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> testPairs = getListEvaluationSettingsSetWithCorrectEvaluationResult();

      for (int i = 0; i < testPairs.size(); i++) {
         Pair<Integer, List<EvaluationSetting>> evaluationSetting = testPairs.get(i).getFirst();
         try {

            EvaluationResult evaluatedEvaluationResult = evaluation.runEvaluationForOneSetOfEvaluationSettings(evaluationSetting);
            EvaluationResult expectedEvaluationResult = testPairs.get(i).getSecond();
            String expectedResult = expectedEvaluationResult.getExtraEvaluationInformation();
            String evaluatedResult = evaluatedEvaluationResult.getExtraEvaluationInformation();
            Assert.assertEquals(String.format(ASSERT_EVALUATION_RESULT_EQUAL, expectedResult, evaluatedResult), expectedResult,
                  evaluatedResult);
            for (IMetric<?, ?> evaluationMetric : expectedEvaluationResult.getEvaluationMetrics()) {
               Object expectedLoss = expectedEvaluationResult.getLossForMetric(evaluationMetric);
               Object evaluatedLoss = evaluatedEvaluationResult.getLossForMetric(evaluationMetric);

               String[] split = expectedEvaluationResult.getDataset().getDatasetFile().toString().split("/");
               String fileName = split[split.length - 1];
               logger.debug(
                     String.format(DEBUG_MESSAGE_EVALUATION_RESULT, fileName, evaluationMetric.toString(), expectedLoss, evaluatedLoss));
               if (expectedLoss instanceof Double) {
                  Assert.assertEquals(
                        String.format(ASSERT_EVALUATION_LOSS_EQUAL, evaluationMetric.toString(),
                              expectedEvaluationResult.getLearningAlgorithm().toString()),
                        (Double) expectedLoss, (Double) evaluatedLoss, ERROR_MARGIN);
               } else {
                  Assert.assertEquals(String.format(ASSERT_EVALUATION_LOSS_EQUAL, evaluationMetric.toString(),
                        expectedEvaluationResult.getLearningAlgorithm().toString()), expectedLoss, evaluatedLoss);
               }
            }

         } catch (EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm exception) {
            // Fail if exception in set parameters is thrown
            fail(String.format(ERROR_CORRECT_EVALUATION_RESULT_NOT_ACHIEVED_FOR_CORRECT_SET_OF_SETTING,
                  testPairs.get(i).getSecond().toString(), evaluationSetting.toString()));
         }
      }
   }


   /**
    * Test if the evaluation be able to detect incorrect evaluation result for the given set of
    * evaluation settings. Mainly used for percentage split and the cross validation evaluations. It
    * asserts inequality of losses and the extra evaluation output in the common evaluated result
    * after running evaluation for one set, which combines all results to one
    * {@link EvaluationResult} after taking mean of all losses and provided results.
    */
   @Override
   @Test
   public void testWrongEvaluationSettings() {
      List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> testPairs = getListEvaluationSettingsSetWithWrongEvaluationResult();
      AEvaluation<?> evaluation = (AEvaluation<?>) getEvaluation();
      for (int i = 0; i < testPairs.size(); i++) {
         Pair<Integer, List<EvaluationSetting>> evaluationSetting = testPairs.get(i).getFirst();
         try {

            EvaluationResult evaluatedEvaluationResult = evaluation.runEvaluationForOneSetOfEvaluationSettings(evaluationSetting);
            EvaluationResult expectedEvaluationResult = testPairs.get(i).getSecond();

            String expectedResult = expectedEvaluationResult.getExtraEvaluationInformation();
            String evaluatedResult = evaluatedEvaluationResult.getExtraEvaluationInformation();
            Assert.assertNotEquals(String.format(ASSERT_CORRECT_EVALUATION_RESULT_ACHIEVED_FOR_INCORRECT_SET_OF_SETTING, expectedResult,
                  evaluationSetting.toString()), evaluatedResult, expectedResult);

            for (IMetric<?, ?> evaluationMetric : expectedEvaluationResult.getEvaluationMetrics()) {
               Object expectedLoss = expectedEvaluationResult.getLossForMetric(evaluationMetric);
               Object evaluatedLoss = evaluatedEvaluationResult.getLossForMetric(evaluationMetric);
               if (evaluatedLoss.getClass() == Double.class) {
                  expectedLoss = (double) expectedLoss;
                  evaluatedLoss = (double) evaluatedLoss;
                  boolean compare = TestUtils.areDoublesEqual((double) expectedLoss, (double) evaluatedLoss, ERROR_MARGIN);
                  Assert.assertFalse(String.format(ASSERT_CORRECT_LOSS_ACHIEVED_FOR_INCORRECT_SETTING, expectedLoss, evaluatedLoss),
                        compare);
               } else {
                  Assert.assertNotEquals(String.format(ASSERT_CORRECT_LOSS_ACHIEVED_FOR_INCORRECT_SETTING, expectedLoss, evaluatedLoss),
                        expectedLoss, evaluatedLoss);
               }
            }

         } catch (EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm exception) {
            Assert.fail(String.format(ERROR_EXCEPTION_NOT_OCCURR_ERROR_MESSAGE, evaluationSetting.toString()));
         }
      }
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings() {
      // no longer needed, because testCorrectEvaluationSettings uses
      // getWrongListOfEvaluationSettingsForOneSet
      return null;
   }


   @Override
   public List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings() {
      // no longer needed, because testWrongEvaluationSettings uses
      // getWrongListOfEvaluationSettingsForOneSet
      return null;
   }


   /**
    * Creates a list of {@link Pair} of one set of {@link EvaluationSetting}s ({@link Pair} of list
    * of evaluation settings with set number) paired with {@link EvaluationResult} for the given
    * array of loss arrays. The array should have one array of losses for each algorithm to test and
    * each of these arrays should have one entry per evaluation metric that is being tested
    * 
    * @param lossArraysForAlgorithms the 2 dimensional double array containing the losses for each
    *           algorithm
    * @param datasetName the name of the dataset to test on
    * @param evaluationOutput the output to add to the evaluation result
    * @return a list of pairs of evaluation settings and results for the given array of loss arrays
    */
   protected abstract List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListOfEvaluationSettingsSetWithEvaluationResultForMetricResults(
         double[][] metricResults, String datasetName, String evaluationOutput);


   /**
    * Creates a list of {@link Pair} of one set of {@link EvaluationSetting}s ({@link Pair} of list
    * of evaluation settings with set number) paired with correct {@link EvaluationResult} for the
    * given array of loss arrays. The array should have one array of losses for each algorithm to
    * test and each of these arrays should have one entry per evaluation metric that is being
    * tested.
    * 
    * @param lossArraysForAlgorithms the 2 dimensional double array containing the losses for each
    *           algorithm
    * @param datasetName the name of the training dataset
    * @return a list of evaluation settings set paired with correct evaluation result
    */
   protected List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getCorrectListOfEvaluationSettingsForOneSetForMetricResults(
         double[][] metricResults, String datasetName) {
      return getListOfEvaluationSettingsSetWithEvaluationResultForMetricResults(metricResults, datasetName, StringUtils.EMPTY_STRING);
   }


   /**
    * Creates a list of {@link Pair} of one set of {@link EvaluationSetting}s ({@link Pair} of list
    * of evaluation settings with set number) paired with wrong {@link EvaluationResult} for the
    * given array of loss arrays. The array should have one array of losses for each algorithm to
    * test and each of these arrays should have one entry per evaluation metric that is being
    * tested. This method just puts single space for the extra evaluation output.
    * 
    * @param lossArraysForAlgorithms the 2 dimensional double array containing the losses for each
    *           algorithm
    * @param datasetName the name of the training dataset
    * @return a list of evaluation settings set paired with wrong evaluation result
    */
   protected List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getWrongListOfEvaluationSettingsForOneSetForMetricResults(
         double[][] metricResults, String datasetName) {
      return getListOfEvaluationSettingsSetWithEvaluationResultForMetricResults(metricResults, datasetName, StringUtils.SINGLE_WHITESPACE);
   }
}
