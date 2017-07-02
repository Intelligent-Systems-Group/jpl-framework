package de.upb.cs.is.jpl.api.evaluation;


import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.common.AParameterizedUnitTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The test template for the evaluations class. If there is any new evaluation implementation, then
 * this class needs to extend this class to test the basic functionality of the evaluation for
 * supplied test set and in sample evaluation.
 * 
 * @author Pritha Gupta
 *
 */
public abstract class AEvaluationTest extends AParameterizedUnitTest {

   private static final Logger logger = LoggerFactory.getLogger(AEvaluationTest.class);

   private static final String ERROR_CORRECT_PARAMETER_NOT_ACCEPTED = "The evaluation should accept the parameter key and value pair: %s.";
   private static final String ERROR_INCORRECT_PARAMETER_ACCEPTED = "The evaluation should not accept the parameter key and value pair: %s.";
   private static final String ERROR_WRONG_OUTPUT = "The output should be \"%s\" and is \"%s\".";

   private static final String ERROR_CORRECT_EVALUATION_RESULT_NOT_ACHIEVED_FOR_CORRECT_SETTING = "The evaluation should evaluate the result:\n%s for setting:\n%s.";
   private static final String ASSERT_CORRECT_EVALUATION_RESULT_ACHIEVED_FOR_INCORRECT_SETTING = "The evaluation should not evaluate the result:\n%s for setting:\n%s";
   private static final String ASSERT_CORRECT_LOSS_ACHIEVED_FOR_INCORRECT_SETTING = "The evaluation should not evaluate the loss:%f for setting:\n%s";

   private static final String ERROR_EXCEPTION_NOT_OCCURR_ERROR_MESSAGE = "There should be no exception for the evaluation metric %s";

   private static final String RESOURCE_DIRECTORY_LEVEL = "evaluation" + File.separator;
   protected static final String ASSERT_EVALUATION_LOSS_EQUAL = "The loss of the evaluation %s of algorithm %s should be the expected value. ";
   protected static final String ASSERT_EVALUATION_RESULT_EQUAL = "The result of the evaluation should be the same, expected result: %s, evaluated result: %s";
   protected static final Double ERROR_MARGIN = 0.01;
   private static final String DEBUG_MESSAGE_EVALUATION_RESULT = "File: %s, expectedLoss and evaluatedLoss for %s: %f, %f";

   protected List<IMetric<?, ?>> evaluationMetrics;
   protected AEvaluation<?> evaluation;


   /**
    * Creates a new unit test for evaluations with the additional path to the resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public AEvaluationTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL, additionalResourcePath);
      evaluationMetrics = new ArrayList<>();
      evaluation = (AEvaluation<?>) getEvaluation();
      setEvaluationMetrics();
   }


   /**
    * Initialize the {@link RandomGenerator} to create deterministic behavior.
    */
   @Before
   public void init() {
      RandomGenerator.initializeRNG(1234);
   }


   /**
    * Returns an instance of the evaluation which should be checked.
    * 
    * @return instance of the evaluation to check
    */
   public abstract IEvaluation getEvaluation();


   /**
    * Returns the list of learning algorithms to be checked.
    * 
    * @return instance of the evaluation to check
    */
   public abstract List<ILearningAlgorithm> getLearningAlgorithms();


   /**
    * Sets the list of evaluation metric on which the evaluation runs, for which the results need to
    * be checked.
    * 
    */
   public abstract void setEvaluationMetrics();


   /**
    * Returns a list of {@link EvaluationSetting} for which the evaluation result will be correctly
    * evaluated by the {@link IEvaluation}. The result is provided with the setting as a pair.
    * 
    * @return a list of EvaluationSetting which will provide correct evaluation result
    */
   public abstract List<Pair<EvaluationSetting, EvaluationResult>> getCorrectListOfEvaluationSettings();


   /**
    * Returns a list of {@link EvaluationSetting} for which the evaluation result will be
    * incorrectly evaluated by the {@link IEvaluation} with the exception string which will be
    * thrown and the incorrect evaluation result which can be obtained.
    * 
    * @return a list of JsonObject paired with exception messages thrown
    */
   public abstract List<Pair<EvaluationSetting, EvaluationResult>> getWrongListOfEvaluationSettings();


   /**
    * Creates a pair of {@link EvaluationResult} and {@link EvaluationSetting} for provided learning
    * algorithm and dataset file path. The result does not contain evaluation extra output and the
    * loss for the evaluation metric.
    * 
    * @param learningAlgorithm the learning algorithm required which is to be trained
    * @param filePath the dataset file path on which the algorithm perform the training and testing
    * @return the pair of {@link EvaluationResult} and {@link EvaluationSetting}
    */
   public Pair<EvaluationSetting, EvaluationResult> getPairOfEvaluationSettingAndResultForAlgorithmAndDataset(
         ILearningAlgorithm learningAlgorithm, String filePath) {
      IDataset<?, ?, ?> dataset = this.createDatasetOutOfFile(learningAlgorithm.getDatasetParser(), filePath);
      ILearningModel<?> learningModel = getLearningModelForAlgorithmOnDataset(learningAlgorithm, dataset);
      EvaluationSetting evaluationSetting = new EvaluationSetting(dataset, learningAlgorithm, learningModel, evaluationMetrics);
      EvaluationResult evaluationResult = new EvaluationResult();
      evaluationResult.setDataset(dataset);
      evaluationResult.setLearningAlgorithm(learningAlgorithm);
      return Pair.of(evaluationSetting, evaluationResult);

   }


   /**
    * Creates a pair of {@link EvaluationResult} and {@link EvaluationSetting} for provided learning
    * algorithm, dataset file path and test dataset file path. The result does not contain
    * evaluation extra output and the loss for the evaluation metric.
    * 
    * @param learningAlgorithm the learning algorithm required which is to be trained
    * @param filePath the dataset file path on which the algorithm perform the training
    * @param testFilePath the test dataset file path
    * @return the pair of {@link EvaluationResult} and {@link EvaluationSetting}
    */
   public Pair<EvaluationSetting, EvaluationResult> getPairOfEvaluationSettingAndResultForAlgorithmAndDataset(
         ILearningAlgorithm learningAlgorithm, String filePath, String testFilePath) {
      IDataset<?, ?, ?> testDataset = this.createDatasetOutOfFile(learningAlgorithm.getDatasetParser(), testFilePath);
      Pair<EvaluationSetting, EvaluationResult> evaluationSettingAndResult = getPairOfEvaluationSettingAndResultForAlgorithmAndDataset(
            learningAlgorithm, filePath);
      evaluationSettingAndResult.getFirst().setDataset(testDataset);
      return evaluationSettingAndResult;
   }


   /**
    * A helper methods which returns a {@link IDataset} by parsing a GPRF file with an
    * {@link IDatasetParser}.
    * 
    * @param datasetparser a dataset parser which is used to parse the file
    * @param pathToFile a path to a GPRF file
    * @return a dataset created by the parser or {@code null} if an error occurs
    */
   public IDataset<?, ?, ?> createDatasetOutOfFile(IDatasetParser datasetparser, String pathToFile) {
      DatasetFile datasetfile = new DatasetFile(new File(pathToFile));
      try {
         return datasetparser.parse(datasetfile);
      } catch (ParsingFailedException e) {
         fail(e.getMessage());
      }
      return null;
   }


   /**
    * A helper methods which returns a {@link ILearningModel} by training on the provided
    * {@link IDataset} by the given {@link ILearningAlgorithm}.
    * 
    * @param learningAlgorithm the learning algorithm required which is to be trained
    * @param dataset the dataset on which the algorithm perform the training
    * @return a learning model trained on the given dataset
    */
   public ILearningModel<?> getLearningModelForAlgorithmOnDataset(ILearningAlgorithm learningAlgorithm, IDataset<?, ?, ?> dataset) {
      try {
         return learningAlgorithm.train(dataset);
      } catch (TrainModelsFailedException e) {
         fail(e.getMessage());
      }
      return null;
   }


   /**
    * Test if the evaluation if able to set correct parameter. This test fails if some parameter is
    * correct and is not parsed properly by {@link AEvaluationConfiguration}. {@inheritDoc}
    * 
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   @Override
   @Test
   public void testCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> testPairs = getCorrectParameters();

      for (int i = 0; i < testPairs.size(); i++) {
         IEvaluation evaluation = getEvaluation();
         JsonObject object = testPairs.get(i);
         try {
            evaluation.setParameters(object);

         } catch (ParameterValidationFailedException e) {
            // Fail if exception in set parameters is thrown
            fail(String.format(ERROR_CORRECT_PARAMETER_NOT_ACCEPTED, object.toString()));
         }
      }
   }


   /**
    * Test if the evaluation be able to detect incorrect parameter. It should fail if no exception
    * is thrown for the given parameter values. It also asserts if the given exception message is
    * what which is thrown by the evaluation. {@inheritDoc}
    * 
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   @Override
   @Test
   public void testWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> testPairs = getWrongParameters();
      for (int i = 0; i < testPairs.size(); i++) {
         IEvaluation evaluation = getEvaluation();
         JsonObject object = testPairs.get(i).getSecond();
         try {
            evaluation.setParameters(object);
            // Fail if no exception in set parameters is thrown
            fail(String.format(ERROR_INCORRECT_PARAMETER_ACCEPTED, testPairs.get(i).getSecond().toString()));
         } catch (ParameterValidationFailedException exception) {
            Assert.assertEquals(ERROR_WRONG_OUTPUT, exception.getMessage(), testPairs.get(i).getFirst());
         }
      }
   }


   /**
    * Test if the evaluation be able to detect correct evaluation result for the given evaluation
    * setting. It asserts equality of losses and the extra evaluation output in the the evaluated
    * results and provided results.
    */
   @Test
   public void testCorrectEvaluationSettings() {
      evaluation = (AEvaluation<?>) getEvaluation();
      List<Pair<EvaluationSetting, EvaluationResult>> testPairs = getCorrectListOfEvaluationSettings();

      for (int i = 0; i < testPairs.size(); i++) {
         EvaluationSetting evaluationSetting = testPairs.get(i).getFirst();
         try {
            EvaluationResult evaluatedEvaluationResult = evaluation.evaluateSingleCombination(evaluationSetting);
            EvaluationResult expectedEvaluationResult = testPairs.get(i).getSecond();
            String expectedResult = expectedEvaluationResult.getExtraEvaluationInformation();
            String evaluatedResult = evaluatedEvaluationResult.getExtraEvaluationInformation();
            Assert.assertEquals(String.format(ASSERT_EVALUATION_RESULT_EQUAL, expectedResult, evaluatedResult), expectedResult,
                  evaluatedResult);
            for (int k = 0; k < expectedEvaluationResult.getEvaluationMetrics().size(); k++) {
               IMetric<?, ?> metric = expectedEvaluationResult.getEvaluationMetrics().get(k);
               Object expectedLoss = expectedEvaluationResult.getLossForMetric(metric);
               Object evaluatedLoss = evaluatedEvaluationResult.getLossForMetric(metric);
               if (expectedLoss instanceof Double) {
                  // logger.debug(String.format(MESSAGE_EVALUATED_LOSS, evaluatedLoss));
                  String[] split = evaluationSetting.getDataset().getDatasetFile().toString().split("/");
                  String fileName = split[split.length - 1];
                  logger.debug(String.format(DEBUG_MESSAGE_EVALUATION_RESULT, fileName, metric.toString(), expectedLoss, evaluatedLoss));
                  String assertMessage = String.format(ASSERT_EVALUATION_LOSS_EQUAL, metric.toString(),
                        expectedEvaluationResult.getLearningAlgorithm().toString());

                  Assert.assertEquals(assertMessage, (Double) expectedLoss, (Double) evaluatedLoss, ERROR_MARGIN);
               } else {
                  Assert.assertEquals(String.format(ASSERT_EVALUATION_LOSS_EQUAL, metric.toString(),
                        expectedEvaluationResult.getLearningAlgorithm().toString()), expectedLoss, evaluatedLoss);
               }
            }

         } catch (LossException | PredictionFailedException exception) {
            // Fail if exception in set parameters is thrown
            fail(String.format(ERROR_CORRECT_EVALUATION_RESULT_NOT_ACHIEVED_FOR_CORRECT_SETTING, testPairs.get(i).getSecond().toString(),
                  evaluationSetting.toString()));
         }
      }
   }


   /**
    * Test if the evaluation be able to detect incorrect evaluation result for the given evaluation
    * setting. It asserts inequality of losses and the extra evaluation output in the the evaluated
    * results and provided results.
    */
   @Test
   public void testWrongEvaluationSettings() {
      List<Pair<EvaluationSetting, EvaluationResult>> testPairs = getWrongListOfEvaluationSettings();
      AEvaluation<?> evaluation = (AEvaluation<?>) getEvaluation();
      for (int i = 0; i < testPairs.size(); i++) {
         try {
            EvaluationSetting evaluationSetting = testPairs.get(i).getFirst();
            EvaluationResult evaluatedEvaluationResult = evaluation.evaluateSingleCombination(evaluationSetting);
            EvaluationResult expectedEvaluationResult = testPairs.get(i).getSecond();

            String expectedResult = expectedEvaluationResult.getExtraEvaluationInformation();
            String evaluatedResult = evaluatedEvaluationResult.getExtraEvaluationInformation();
            Assert.assertNotEquals(String.format(ASSERT_CORRECT_EVALUATION_RESULT_ACHIEVED_FOR_INCORRECT_SETTING, expectedResult,
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

         } catch (LossException | PredictionFailedException exception) {
            Assert.fail(String.format(ERROR_EXCEPTION_NOT_OCCURR_ERROR_MESSAGE, testPairs.get(i).getFirst().getMetrics()));
         }
      }
   }


   /**
    * Creates a list of pairs of evaluation settings and results for the given array of loss arrays.
    * The array should have one array of losses for each algorithm to test and each of these arrays
    * should have one entry per evaluation metric that is being tested.
    * 
    * @param lossArraysForAlgorithms the 2 dimensional double array containing the losses for each
    *           algorithm
    * @param datasetName the name of the training dataset
    * @param evaluationOutput the output to add to the evaluation result
    * @return a list of pairs of evaluation settings and results for the given array of loss arrays
    */
   protected List<Pair<EvaluationSetting, EvaluationResult>> createListOfEvaluationSettingAndResultPairsForLossArrays(
         double[][] lossArraysForAlgorithms, String datasetName, String evaluationOutput) {
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = new ArrayList<>();
      for (int i = 0; i < getLearningAlgorithms().size(); i++) {
         ILearningAlgorithm learningAlgorithm = getLearningAlgorithms().get(i);
         Pair<EvaluationSetting, EvaluationResult> evaluationSettingAndEvaluationResultPair = getPairOfEvaluationSettingAndResultForAlgorithmAndDataset(
               learningAlgorithm, getTestRessourcePathFor(datasetName));
         evaluationSettingAndEvaluationResultPair.getSecond().setExtraEvaluationInformation(evaluationOutput);

         double[] loss = lossArraysForAlgorithms[i];
         for (int j = 0; j < evaluationMetrics.size(); j++) {
            evaluationSettingAndEvaluationResultPair.getSecond().addLossWithMetric(loss[j], evaluationMetrics.get(j));
         }
         correctEvalutionSettingAndEvaluationResultList.add(evaluationSettingAndEvaluationResultPair);
      }
      return correctEvalutionSettingAndEvaluationResultList;
   }


   /**
    * Creates a list of pairs of evaluation settings and results for the given array of loss arrays.
    * The array should have one array of losses for each algorithm to test and each of these arrays
    * should have one entry per evaluation metric that is being tested. This method also takes test
    * dataset file name on which the algorithms needs to be tested.
    * 
    * @param lossArraysForAlgorithms the 2 dimensional double array containing the losses for each
    *           algorithm
    * @param datasetName the name of the dataset to test on
    * @param testsetFileName the name of the test dataset
    * @param evaluationOutput the output to add to the evaluation result
    * @return a list of pairs of evaluation settings and results for the given array of loss arrays
    */
   protected List<Pair<EvaluationSetting, EvaluationResult>> createListOfEvaluationSettingAndResultPairsForLossArrays(
         double[][] lossArraysForAlgorithms, String datasetName, String testsetFileName, String evaluationOutput) {
      List<Pair<EvaluationSetting, EvaluationResult>> correctEvalutionSettingAndEvaluationResultList = new ArrayList<>();
      for (int i = 0; i < getLearningAlgorithms().size(); i++) {
         ILearningAlgorithm learningAlgorithm = getLearningAlgorithms().get(i);
         Pair<EvaluationSetting, EvaluationResult> evaluationSettingAndEvaluationResultPair = getPairOfEvaluationSettingAndResultForAlgorithmAndDataset(
               learningAlgorithm, getTestRessourcePathFor(datasetName), getTestRessourcePathFor(testsetFileName));
         evaluationSettingAndEvaluationResultPair.getSecond().setExtraEvaluationInformation(evaluationOutput);

         double[] loss = lossArraysForAlgorithms[i];
         for (int j = 0; j < evaluationMetrics.size(); j++) {
            evaluationSettingAndEvaluationResultPair.getSecond().addLossWithMetric(loss[j], evaluationMetrics.get(j));
         }
         correctEvalutionSettingAndEvaluationResultList.add(evaluationSettingAndEvaluationResultPair);
      }
      return correctEvalutionSettingAndEvaluationResultList;
   }


   /**
    * Creates a correct list of pairs of evaluation settings and results for the given array of loss
    * arrays. The array should have one array of losses for each algorithm to test and each of these
    * arrays should have one entry per evaluation metric that is being tested.
    * 
    * @param lossArraysForAlgorithms the 2 dimensional double array containing the losses for each
    *           algorithm
    * @param datasetName the name of the training dataset
    * @return a correct list of pairs of evaluation settings and results for the given array of loss
    *         arrays
    */
   protected List<Pair<EvaluationSetting, EvaluationResult>> createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
         double[][] lossArraysForAlgorithms, String datasetName) {
      return createListOfEvaluationSettingAndResultPairsForLossArrays(lossArraysForAlgorithms, datasetName, StringUtils.EMPTY_STRING);
   }


   /**
    * Creates an incorrect list of pairs of evaluation settings and results for the given array of
    * loss arrays. The array should have one array of losses for each algorithm to test and each of
    * these arrays should have one entry per evaluation metric that is being tested. This method
    * just puts single space for the extra evaluation output.
    * 
    * @param lossArraysForAlgorithms the 2 dimensional double array containing the losses for each
    *           algorithm
    * @param datasetName the name of the training dataset
    * @return an incorrect list of pairs of evaluation settings and results for the given array of
    *         loss arrays
    */
   protected List<Pair<EvaluationSetting, EvaluationResult>> createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
         double[][] lossArraysForAlgorithms, String datasetName) {
      return createListOfEvaluationSettingAndResultPairsForLossArrays(lossArraysForAlgorithms, datasetName, StringUtils.SINGLE_WHITESPACE);
   }


   /**
    * Creates a correct list of pairs of evaluation settings and results for the given array of loss
    * arrays. The array should have one array of losses for each algorithm to test and each of these
    * arrays should have one entry per evaluation metric that is being tested. This method also
    * takes test dataset file name on which the algorithms needs to be tested.
    * 
    * @param lossArraysForAlgorithms the 2 dimensional double array containing the losses for each
    *           algorithm
    * @param datasetName the name of the training dataset
    * @param testsetFileName the name of the test dataset
    * @return a correct list of pairs of evaluation settings and results for the given array of loss
    *         arrays
    */
   protected List<Pair<EvaluationSetting, EvaluationResult>> createListOfEvaluationSettingAndCorrectEvaluationResultPairsForLossArrays(
         double[][] lossArraysForAlgorithms, String datasetName, String testsetFileName) {
      return createListOfEvaluationSettingAndResultPairsForLossArrays(lossArraysForAlgorithms, datasetName, testsetFileName,
            StringUtils.EMPTY_STRING);
   }


   /**
    * Creates an incorrect list of pairs of evaluation settings and results for the given array of
    * loss arrays. The array should have one array of losses for each algorithm to test and each of
    * these arrays should have one entry per evaluation metric that is being tested. This method
    * just puts single space for the extra evaluation output. This method also takes test dataset
    * file name on which the algorithms needs to be tested.
    * 
    * @param lossArraysForAlgorithms the 2 dimensional double array containing the losses for each
    *           algorithm
    * @param datasetName the name of the training dataset
    * @param testsetFileName the name of the test dataset
    * @return an incorrect list of pairs of evaluation settings and results for the given array of
    *         loss arrays
    */
   protected List<Pair<EvaluationSetting, EvaluationResult>> createListOfEvaluationSettingAndWrongEvaluationResultPairsForLossArrays(
         double[][] lossArraysForAlgorithms, String datasetName, String testsetFileName) {
      return createListOfEvaluationSettingAndResultPairsForLossArrays(lossArraysForAlgorithms, datasetName, testsetFileName,
            StringUtils.SINGLE_WHITESPACE);
   }
}
