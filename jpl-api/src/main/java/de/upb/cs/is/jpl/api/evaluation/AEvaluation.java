package de.upb.cs.is.jpl.api.evaluation;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.WrongConfigurationTypeException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationNotCarriedOutSuccesfully;
import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.exception.evaluation.TrainTestDatasetPairsNotCreated;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.metric.AMetricConfiguration;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * The abstract class for evaluating the current evaluation on Algorithm(s) and storing the
 * evaluationResult of the current running evaluation.
 * 
 * @author Pritha Gupta
 * @param <CONFIG> the {@link AEvaluationConfiguration} associated with this evaluation
 */
public abstract class AEvaluation<CONFIG extends AEvaluationConfiguration> implements IEvaluation {

   private static final String EVALUATION_NOT_CARRIED_OUT_MESSAGE = "Evaluation for list of evaluation settings with set number %d was not carried out due to reason: %s";
   private static final Logger logger = LoggerFactory.getLogger(AEvaluation.class);
   protected static final String EVALUATION_SETTING_CANNOT_BE_FOUND_WARNING_MESSAGE = "The evaluation Setting is set to null for set number: %s";
   protected static final String PARAMETER_NOT_VALID_FOR_METRIC = "User provided parameters values which are not valid for metric %s and the error is: %s";
   protected static final String ADDING_LEARNING_ALGORITHM_MESSAGE = "Adding learning algorithm: %s";
   protected static final String EVALUATION_RESULT_NULL = "The evaluation cannot be done for on metric %s for learning algorithm %s on dataset %s";
   protected static final String EMPTY_JSON_OBJECT = "{}";
   protected static final String EVALUATION_NOT_SUCCESSFUL_RESULT = "The current evaluation was not carried out successfully for learning problem %s, there are no evaluation results obtained. Please refer to above warnings and exceptions for the cause";
   protected static final String ERROR_WRONG_CONFIGURATION_TYPE = "Initialized evaluation configuration with wrong configuration type.";
   protected static final String DATASET_PARSING_WARNING_MESSAGE = "Cannot parse the dataset file %s for learning algorithm %s due to error %s";
   protected static final String DATASET_PARSER_NOT_SET_WARNING_MESSAGE = "Cannot get the dataset parser for learning algorithm %s";
   protected static final String MODEL_CANNOT_BE_TRAINED_WARNING_MESSAGE = "Cannot train the model on the dataset file %s for learning algorithm %s due to error %s";
   protected static final String ADDING_DATASET_MESSAGE = "Adding dataset: %s";
   protected static final String EVALAUTION_RESULT_MESSAGE = "The final interpreted results for evaluation are as follows:";
   protected static final String NULL_OUTPUT_FOR_EVALUATION_METRIC = "Output not set for metric %s in evaluation result %s";

   private static final String EVALUATION_RESULT_FOR_SET_NOT_EVALUATED = "Evaluation Result for the set number %s is not evaluated properly.";
   private static final String INVALID_EVALUATION_RESULTS_CREATED_FOR_SET_NO_UNIQUE_ALGORTIHM_DATASET = "Invalid Evaluation Results list created for one set of evaluation settings as they do not contain unique pair of dataset and learning algorithm";
   private static final String INVALID_EVALUATION_RESULTS_CREATED_FOR_SET_NO_UNIQUE_EVALUATION_METRICS = "Invalid Evaluation Results list created for one set of evaluation settings as do not contain unique list of metrics";
   private static final String EVALUATION_OUTPUT_CANNOT_BE_WRITTEN_IN_FILE = "Evaluation output cannot be written for current evaluation due to error %s";

   private static final String UTF_8 = "UTF-8";
   protected List<EvaluationResult> evaluationResults;
   protected CONFIG configuration;
   protected ELearningProblem eLearningProblem;
   protected AMetricConfiguration evaluationMetricConfiguration;
   protected EvaluationsOutputGenerator evaluationOuputGenerator;
   protected IDatasetParser datasetParser;


   /**
    * Creates an evaluation with the default configuration and the learning problem with which the
    * evaluation class is associated with.
    * 
    * @param eLearningProblem the learning problem associated with the evaluation class to set
    */
   public AEvaluation(ELearningProblem eLearningProblem) {
      init();
      configuration = null;
      evaluationResults = new ArrayList<>();
      this.eLearningProblem = eLearningProblem;
      getEvaluationConfiguration();
   }


   /**
    * Initializes the member variables.
    */
   protected abstract void init();


   @Override
   public void evaluate() throws EvaluationNotCarriedOutSuccesfully {
      List<Pair<Integer, List<EvaluationSetting>>> setNumberWithEvaluationSettings = configuration
            .getListOfEvaluationSettingsWithSetNumber();

      for (Pair<Integer, List<EvaluationSetting>> evaluationSettingsForOneSet : setNumberWithEvaluationSettings) {
         EvaluationResult evaluationResult = null;
         try {
            evaluationResult = runEvaluationForOneSetOfEvaluationSettings(evaluationSettingsForOneSet);
         } catch (EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm exception) {
            logger.warn(String.format(EVALUATION_NOT_CARRIED_OUT_MESSAGE, evaluationSettingsForOneSet.getFirst(), exception.getMessage()),
                  exception);
         }
         String warningMessage = String.format(EVALUATION_RESULT_FOR_SET_NOT_EVALUATED,
               String.valueOf(evaluationSettingsForOneSet.getFirst()));
         addEvaluationResult(evaluationResult, warningMessage);
      }

      if (!checkValidtiyOfEvalautionResult()) {
         String errorMessage = String.format(EVALUATION_NOT_SUCCESSFUL_RESULT, eLearningProblem);
         throw new EvaluationNotCarriedOutSuccesfully(errorMessage);
      }
      setDatasetsAndEvaluationResultsInEvaluationOutputGenerator();

   }


   /**
    * Creates a list of {@link IDataset}s on which the evaluation has been run and sets the
    * {@link EvaluationResult}s and list of dataset in the {@link EvaluationsOutputGenerator}.
    */
   private void setDatasetsAndEvaluationResultsInEvaluationOutputGenerator() {
      Map<DatasetFile, IDataset<?, ?, ?>> datasetFilesWithDataset = new HashMap<>();
      List<DatasetFile> datasetFiles = new ArrayList<>();
      for (EvaluationResult evaluationResult : evaluationResults) {
         if (!datasetFiles.contains(evaluationResult.getDataset().getDatasetFile())) {
            datasetFilesWithDataset.put(evaluationResult.getDataset().getDatasetFile(), evaluationResult.getDataset());
            datasetFiles.add(evaluationResult.getDataset().getDatasetFile());
         }
      }
      for (DatasetFile file : datasetFiles) {
         for (int i = 0; i < evaluationResults.size(); i++) {
            DatasetFile outputFile = evaluationResults.get(i).getDataset().getDatasetFile();
            if (outputFile.equals(file)) {
               evaluationResults.get(i).setDataset(datasetFilesWithDataset.get(file));
            }
         }
      }
      List<IDataset<?, ?, ?>> datasets = new ArrayList<>(datasetFilesWithDataset.values());
      evaluationOuputGenerator.setDatasets(datasets);
      evaluationOuputGenerator.setEvaluationResults(evaluationResults);
   }


   @Override
   public EvaluationResult runEvaluationForOneSetOfEvaluationSettings(Pair<Integer, List<EvaluationSetting>> evaluationSettingsForOneSet)
         throws EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm {
      List<EvaluationResult> evaluationResultsForOneSet = new ArrayList<>();
      for (EvaluationSetting evaluationSetting : evaluationSettingsForOneSet.getSecond()) {
         if (evaluationSetting != null) {
            try {
               EvaluationResult evaluationResult = evaluateSingleCombination(evaluationSetting);
               if (evaluationResult != null) {
                  evaluationResultsForOneSet.add(evaluationResult);
               } else {
                  String warningMessage = String.format(EVALUATION_RESULT_NULL, evaluationSetting.getMetrics().getClass().getSimpleName(),
                        evaluationSetting.getLearningAlgorithm(), evaluationSetting.getDataset());
                  logger.warn(warningMessage);
               }
            } catch (LossException | PredictionFailedException exception) {
               logger.warn(exception.getMessage(), exception);
            }
         } else {
            logger.warn(String.format(EVALUATION_SETTING_CANNOT_BE_FOUND_WARNING_MESSAGE, evaluationSettingsForOneSet.getFirst()));
         }
      }
      if (evaluationResultsForOneSet.size() == 1) {
         return evaluationResultsForOneSet.get(0);
      }
      return createCombinedEvaluationResultForOneSet(evaluationResultsForOneSet);
   }


   /**
    * This method will be run for single evaluation on pair of dataset and learning algorithm from
    * the {@link EvaluationSetting} of {@link ILearningAlgorithm} (which is trained on some dataset
    * and has a learning model {@link ILearningModel}) and {@link IDataset} for the corresponding
    * {@link EMetric} and stores the corresponding result in {@link EvaluationResult}.
    * 
    * @param evaluationSetting the evaluation setting containing all objects required for evaluation
    *           which needs to be done
    * 
    * @return the {@link EvaluationResult} for the given metric, by learning algorithm on given
    *         dataset
    * @throws LossException if the the loss could not be calculated for provided setting
    * @throws PredictionFailedException if the prediction is not working for
    */
   public abstract EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting)
         throws LossException,
            PredictionFailedException;


   /**
    * Combines the list of {@link EvaluationResult}s to form on result, evaluated on a list of
    * {@link EvaluationSetting}s generated from
    * {@link IEvaluation#runEvaluationForOneSetOfEvaluationSettings(Pair)}. This method creates a
    * single {@link EvaluationResult}, for the list of evaluation result created for one set of
    * {@link EvaluationSetting}.
    * 
    * @param evaluationResultsForOneSet the list of evaluation results needs to be combined
    * @return the combined evaluation result containing the unique learning algorithm, for all the
    *         datasets having same dataset file, identified as one dataset
    * @throws EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm if the set of evaluation
    *            result obtained were not evaluated for unique pair of algorithm and dataset
    */
   public static EvaluationResult createCombinedEvaluationResultForOneSet(List<EvaluationResult> evaluationResultsForOneSet)
         throws EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm {
      EvaluationResult combinedEvaluationResultForOneSet = new EvaluationResult();
      if (!evaluationResultsForOneSet.isEmpty()) {
         Triple<ILearningAlgorithm, IDataset<?, ?, ?>, List<IMetric<?, ?>>> triple = getLearningAlgorithmForOneSetOfEvaluations(
               evaluationResultsForOneSet);
         combinedEvaluationResultForOneSet.setDataset(triple.getSecond());
         combinedEvaluationResultForOneSet.setLearningAlgorithm(triple.getFirst());
         combinedEvaluationResultForOneSet.setEvaluationMetrics(triple.getThird());
      }
      for (IMetric<?, ?> evaluationMetric : combinedEvaluationResultForOneSet.getEvaluationMetrics()) {
         Double combinedLoss = 0.0;
         for (EvaluationResult evaluationResult : evaluationResultsForOneSet) {
            Object loss = evaluationResult.getLossForMetric(evaluationMetric);
            if (loss == null)
               logger.warn(String.format(NULL_OUTPUT_FOR_EVALUATION_METRIC, evaluationMetric.toString(), evaluationResult.toString()));
            else if (loss.getClass() == Double.class || loss.getClass() == Float.class || loss.getClass() == Integer.class) {
               combinedLoss = combinedLoss + (Double) loss;
            }
            // For any other type of return type for output for the metric one has to write it here
            // on how to convert it
         }
         combinedLoss = combinedLoss / evaluationResultsForOneSet.size();
         combinedEvaluationResultForOneSet.addLossWithMetric(combinedLoss, evaluationMetric);
      }
      combinedEvaluationResultForOneSet.setExtraEvaluationInformation(evaluationResultsForOneSet.get(0).getExtraEvaluationInformation());
      return combinedEvaluationResultForOneSet;
   }


   /**
    * Returns the unique {@link Pair} of {@link ILearningAlgorithm} and {@link IDataset}in the set
    * of {@link EvaluationResult}.
    * 
    * @param list the list of evaluation results for which you want loss
    * @return the unique set of learning algorithm, dataset and list of metrics
    * @throws EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm if the set of evaluation
    *            result is not created for unique pair of algorithm and dataset
    */
   private static Triple<ILearningAlgorithm, IDataset<?, ?, ?>, List<IMetric<?, ?>>> getLearningAlgorithmForOneSetOfEvaluations(
         List<EvaluationResult> list) throws EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm {
      ILearningAlgorithm learningAlgorithm = list.get(0).getLearningAlgorithm();
      IDataset<?, ?, ?> dataset = list.get(0).getDataset();
      List<IMetric<?, ?>> evaluationMetrics = list.get(0).getEvaluationMetrics();

      for (EvaluationResult evaluationResult : list) {
         if (!learningAlgorithm.equals(evaluationResult.getLearningAlgorithm())
               || !dataset.getDatasetFile().equals(dataset.getDatasetFile())) {
            throw new EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm(
                  INVALID_EVALUATION_RESULTS_CREATED_FOR_SET_NO_UNIQUE_ALGORTIHM_DATASET);
         }
         for (IMetric<?, ?> evaluationMetric : evaluationMetrics) {
            if (!evaluationResult.getEvaluationMetrics().contains(evaluationMetric))
               throw new EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm(
                     INVALID_EVALUATION_RESULTS_CREATED_FOR_SET_NO_UNIQUE_EVALUATION_METRICS);
         }
      }
      return Triple.of(learningAlgorithm, dataset, evaluationMetrics);
   }


   /**
    * Returns the list of all the {@link EvaluationResult} obtained after running this evaluation.
    * 
    * @return the list of evaluation result
    */
   public List<EvaluationResult> getEvaluationResult() {
      return evaluationResults;
   }


   /**
    * This method get the {@link IMetric} with user provide configuration in the system
    * configuration json file.
    * 
    * @param eMetric the enum of metric
    * @return the {@link IMetric} with the parameters provided in the configuration
    * @throws ParameterValidationFailedException if the parameters provided by user are not valid
    */
   protected IMetric<?, ?> getEvaluationMetricWithUserProvidedConfiguration(EMetric eMetric) throws ParameterValidationFailedException {
      IMetric<?, ?> evalautionMetric = eMetric.createEvaluationMetric();
      evaluationMetricConfiguration = evalautionMetric.getMetricConfiguration();
      String evaluationMetricIdentifier = eMetric.getMetricIdentifier();
      JsonObject userProvidedMetricPrameters = getMetricParameterJsonObject(evaluationMetricIdentifier);
      if (userProvidedMetricPrameters != null && !userProvidedMetricPrameters.toString().equals(EMPTY_JSON_OBJECT)) {
         try {
            evaluationMetricConfiguration.overrideConfiguration(userProvidedMetricPrameters);
            evalautionMetric.setMetricConfiguration(evaluationMetricConfiguration);
         } catch (ParameterValidationFailedException paramterValidationFailed) {
            String errorMessage = String.format(PARAMETER_NOT_VALID_FOR_METRIC, evaluationMetricIdentifier,
                  paramterValidationFailed.getMessage());
            throw new ParameterValidationFailedException(errorMessage, paramterValidationFailed);
         }
      }
      return evalautionMetric;
   }


   /**
    * This method adds the provided {@link EvaluationResult} to the current list of the evaluation
    * results for the current evaluation.
    * 
    * @param evaluationResult the {@link EvaluationResult} to be added to list
    * @param warningMessage the warning message to be logged if the result is null
    */
   protected void addEvaluationResult(EvaluationResult evaluationResult, String warningMessage) {
      if (evaluationResult != null) {
         evaluationResults.add(evaluationResult);
      } else {
         logger.warn(warningMessage);
      }
   }


   @Override
   public JsonObject getMetricParameterJsonObject(String metricIdentifier) {
      AEvaluationConfiguration evaluationConfiguration = this.getEvaluationConfiguration();
      JsonObject metricParameters = new JsonObject();
      List<MetricDefinition> metricDefinitions = evaluationConfiguration.getMetricDefinitions();
      for (int i = 0; i < metricDefinitions.size(); i++) {
         String currentMetricIdentifier = metricDefinitions.get(i).getName();
         if (metricIdentifier.equalsIgnoreCase(currentMetricIdentifier)) {
            metricParameters = metricDefinitions.get(i).getParameters();
         }
      }
      return metricParameters;
   }


   /**
    * This function checks if any result is stored for the current evaluation {@link List}. Its
    * returns a {@code true} value, if the list is not empty else it returns @code false}, which
    * means that the evaluation could not get any result.
    * 
    * @return {@code false} if the evaluation results list if empty, else {@code true}
    */
   public boolean checkValidtiyOfEvalautionResult() {
      boolean valid = false;
      for (EvaluationResult result : evaluationResults) {
         if (!result.checkValidityOfTheResult()) {
            valid = true;
         }
      }
      return valid;
   }


   @Override
   public void setupEvaluation(List<DatasetFile> datasetFiles, List<ILearningAlgorithm> learningAlgorithms)
         throws ParameterValidationFailedException {
      if (configuration.getMetrics().isEmpty()) {
         for (String evaluationMetricIdentifer : configuration.getMetricIdentifiers()) {
            EMetric eEvaluationMetric = EMetric.getEEvaluationMetricByProblemAndIdentifier(eLearningProblem, evaluationMetricIdentifer);
            IMetric<?, ?> evaluationMetric = getEvaluationMetricWithUserProvidedConfiguration(eEvaluationMetric);
            configuration.addMetric(evaluationMetric);
         }
      }
      setupEvaluation(datasetFiles, learningAlgorithms, configuration.getMetrics());
   }


   @Override
   public void setupEvaluation(List<DatasetFile> datasetFiles, List<ILearningAlgorithm> learningAlgorithms, List<IMetric<?, ?>> metrics) {
      configuration.setMetrics(metrics);
      evaluationOuputGenerator = new EvaluationsOutputGenerator(learningAlgorithms, metrics);
      int setNumber = 0;
      for (ILearningAlgorithm learningAlgorithm : learningAlgorithms) {
         logger.debug(String.format(ADDING_LEARNING_ALGORITHM_MESSAGE, learningAlgorithm));
         for (DatasetFile datasetFile : datasetFiles) {
            setNumber = setupSingleEvaluationDatasetAndAlgorithm(setNumber, datasetFile, learningAlgorithm, metrics);
            setNumber++;
         }
      }
   }


   @Override
   public int setupSingleEvaluationDatasetAndAlgorithm(int setNumber, DatasetFile datasetFile, ILearningAlgorithm learningAlgorithm,
         List<IMetric<?, ?>> metrics) {
      try {
         datasetParser = learningAlgorithm.getDatasetParser();
         if (datasetParser != null) {
            IDataset<?, ?, ?> dataset = datasetParser.parse(datasetFile);
            logger.debug(String.format(ADDING_DATASET_MESSAGE, datasetFile.getFile()));
            List<EvaluationSetting> evaluationSettings = new ArrayList<>();
            List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> testTrainDatasetPairs = getTestTrainPairs(dataset);
            for (int i = 0; i < testTrainDatasetPairs.size(); i++) {
               ILearningModel<?> learningModel = learningAlgorithm.train(testTrainDatasetPairs.get(i).getSecond());
               EvaluationSetting evaluationSetting = new EvaluationSetting(testTrainDatasetPairs.get(i).getFirst(), learningAlgorithm,
                     learningModel, metrics);
               evaluationSettings.add(evaluationSetting);
            }
            getEvaluationConfiguration().addEvaluationSettingsWithSetNumber(setNumber, evaluationSettings);
         } else {
            logger.warn(String.format(DATASET_PARSER_NOT_SET_WARNING_MESSAGE, learningAlgorithm));
         }
      } catch (ParsingFailedException parsingFailedException) {
         String errorMessage = String.format(DATASET_PARSING_WARNING_MESSAGE, datasetFile.getFile(), learningAlgorithm,
               parsingFailedException.getMessage());
         logger.error(errorMessage, parsingFailedException);
      } catch (TrainModelsFailedException trainModelsFailedException) {
         logger.error(String.format(MODEL_CANNOT_BE_TRAINED_WARNING_MESSAGE, datasetFile.getFile(), learningAlgorithm,
               trainModelsFailedException.getMessage()), trainModelsFailedException);
      } catch (TrainTestDatasetPairsNotCreated exception) {
         logger.error(exception.getMessage(), exception);
      }
      return setNumber;
   }


   @Override
   public String interpretEvaluationResult() {
      StringBuilder evaluationOutputBuilder = new StringBuilder();
      evaluationOutputBuilder.append(EVALAUTION_RESULT_MESSAGE);
      evaluationOutputBuilder.append(StringUtils.LINE_BREAK);
      evaluationOutputBuilder.append(StringUtils.LINE_BREAK);
      evaluationOutputBuilder.append(configuration.toString());
      evaluationOutputBuilder.append(evaluationOuputGenerator.generateEvaluationOutputForEvaluationResults(65, 30));
      String evaluationOutput = evaluationOutputBuilder.toString();
      File file = new File(configuration.getOutputFilePath());
      try (InputStream inputStream = IOUtils.toInputStream(evaluationOutput, UTF_8)) {
         if (!file.exists()) {
            if (file.getParentFile() != null && !file.getParentFile().isDirectory()) {
               file.getParentFile().mkdirs();
            }
            file.createNewFile();
         }
         OutputStream outputStream = new FileOutputStream(file);
         IOUtils.copy(inputStream, outputStream);
         outputStream.close();
      } catch (IOException | NullPointerException exception) {
         logger.error(String.format(EVALUATION_OUTPUT_CANNOT_BE_WRITTEN_IN_FILE, exception.getMessage()), exception);
      }
      return evaluationOutput;

   }


   @SuppressWarnings("unchecked")
   @Override
   public AEvaluationConfiguration getEvaluationConfiguration() {
      if (configuration == null) {
         configuration = (CONFIG) getDefaultEvalutionConfiguration();
      }
      return configuration;
   }


   @Override
   public AEvaluationConfiguration getDefaultEvalutionConfiguration() {
      AEvaluationConfiguration evaluationConfiguration = createDefaultEvaluationConfiguration();
      evaluationConfiguration.setELearningProblem(eLearningProblem);
      evaluationConfiguration.initializeDefaultConfiguration();
      return evaluationConfiguration;
   }


   @Override
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException {
      getEvaluationConfiguration().overrideConfiguration(jsonObject);
   }


   @Override
   @SuppressWarnings("unchecked")
   public void setEvaluationConfiguration(AEvaluationConfiguration evaluationConfiguration) {
      if (!createDefaultEvaluationConfiguration().getClass().isInstance(evaluationConfiguration)) {
         throw new WrongConfigurationTypeException(ERROR_WRONG_CONFIGURATION_TYPE);
      }
      this.configuration = (CONFIG) evaluationConfiguration;
   }


   /**
    * Creates the default evaluation configuration of this evaluation and returns it.
    * 
    * @return the default evaluation configuration
    */
   protected abstract AEvaluationConfiguration createDefaultEvaluationConfiguration();


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
      result = prime * result + ((eLearningProblem == null) ? 0 : eLearningProblem.hashCode());
      result = prime * result + ((evaluationResults == null) ? 0 : evaluationResults.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (secondObject instanceof AEvaluation<?>) {
         AEvaluation<?> castedObject = (AEvaluation<?>) secondObject;
         if (configuration.equals(castedObject.configuration) && eLearningProblem != castedObject.eLearningProblem
               && evaluationResults.equals(castedObject.evaluationResults)) {
            return true;
         }
      }
      return false;
   }
}
