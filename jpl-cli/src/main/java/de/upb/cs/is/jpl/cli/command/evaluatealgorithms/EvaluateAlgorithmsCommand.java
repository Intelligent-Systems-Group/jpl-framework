package de.upb.cs.is.jpl.cli.command.evaluatealgorithms;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationsKeyValuePairs;
import de.upb.cs.is.jpl.api.evaluation.IEvaluation;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationFailedException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.exception.command.CommandCannotBeExecutedException;
import de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand.CannotCreateEvaluationForLearningProblemException;
import de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand.CouldNotSetupEvaluationException;
import de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand.DatasetsOrLearningAlgorithmsNotSetForEvaluationException;
import de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand.EvaluationDoesnotExistForLearningProblemException;
import de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand.UnknownEvaluationIdentifierException;


/**
 * This command is responsible for the execution of the evaluation of multiple or single
 * algorithm(s). It executes the command according for multiple or single algorithm and set the
 * {@code CommandResult}, with failure result and updates the boolean in function
 * {@link EvaluateAlgorithmsCommand#canBeExecuted()}.
 * 
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluateAlgorithmsCommand extends ACommand {

   private static final String EVALUATION_IDENTIFIER_NOT_SET_IN_COMMAND_LINE_MESSAGE = "Evaluation identifier not set in the command line, so it the value provided in system configuration is taken.";
   private static final Logger logger = LoggerFactory.getLogger(EvaluateAlgorithmsCommand.class);
   private static final String SUCCESSFULL_EXECUTION_COMMAND_RESULT = "The evaluation %s for learning problem %s was executed successfully.";
   private static final String EVALUATION_METRIC_IDENTIFIER_NOTSET_WARNING_MESSAGE = "the list of evaluation metric identifiers is not set in system configuration and in command line, the default values will apply.";
   private static final String EVALUATION_JSONSTRING_NOTSET_WARNING_MESSAGE = "the evaluation Json string is empty, the default setting will apply for evaluation.";

   private static final String LEARNING_PROBLEM_NOTSET_ERROR_MESSAGE = "the learning problem is not set.";
   private static final String EVALUATION_IDENTIFIER_NOTSET_ERROR_MESSAGE = "the evaluation identifier is empty for learning problem %s.";
   private static final String COMMAND_CANNOT_BE_EXECUTED_ERROR_MESSAGE = "The evaluation command cannot be executed due to error: %s";
   private static final String PARAMETER_ERROR_MESSAGE = "Parameters provided for Evaluation are invalid, following error occurred: %s";

   private static final String EVALUATION_IDENTIFIER_UNKNOWN_ERROR_MESSAGE = "The given identifier for the evaluation \"%s\" is unknown!";
   private static final String EVALUATION_FOR_LEARNINGPROBLEM_NOT_EXIST_ERROR_MESSAGE = "The given type of the evaluation \"%s\" does not exist for the learning problem \"%s\".";
   private static final String DATASETSARENOTSET_ERROR_MESSAGE = "For evaluation \"%s\" datasets are not set in systemConfiguration.";
   private static final String LEARNING_ALGORITHMS_ERROR_MESSAGE = "For evaluation \"%s\" learning algorithms are not set in systemConfiguration.";
   private static final String COULDNOT_SETUP_EVALUATION_ERROR_MESSAGE = "For evaluation \"%s\" learning model, dataset and algorithm Map could not be not set in evaluationConfiguration. Please see above warnings for the reason.";
   private static final String CANNOT_CREATE_EVALUATION_FORLEARNINGPROBLEM_ERROR_MESSAGE = "The given type evaluation \"%s\" cannot be created for the learning problem \"%s\".";

   private boolean canBeExecuted;
   private Exception caughtException;
   private String failureReason;
   private CommandResult commandResult;
   private ELearningProblem eLearningProblem;
   private String evaluationIdentifier;
   private List<String> metricIdentifiers;
   private IEvaluation evaluation;
   private SystemConfiguration systemConfiguration;
   private AEvaluationConfiguration aEvaluationConfiguration;


   /**
    * Creates the {@code EvaluateAlgorithmsCommand} and initializes it command member variables,
    * with the values provided by the command handler.
    * 
    * @param evaluationIdentifierHandler the evaluation identifier which corresponds to kind of
    *           evaluation should be executed
    * @param metricIdentifierHandler the list of metrics identifiers on which the evaluation should
    *           be based on
    */
   public EvaluateAlgorithmsCommand(String evaluationIdentifierHandler, List<String> metricIdentifierHandler) {
      super(ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier());
      evaluationIdentifier = evaluationIdentifierHandler;
      metricIdentifiers = CollectionsUtils.getDeepCopyOf(metricIdentifierHandler);
      init();

   }


   @Override
   public boolean canBeExecuted() {
      try {
         checkInitialParametersForCommandExecution();
      } catch (CommandCannotBeExecutedException commandCannotBeExecuted) {
         failureReason = String.format(COMMAND_CANNOT_BE_EXECUTED_ERROR_MESSAGE, commandCannotBeExecuted.getMessage());
         logger.error(failureReason, commandCannotBeExecuted);
      }
      return canBeExecuted;
   }


   @Override
   public CommandResult executeCommand() {
      try {
         initiliazeEvaluationAndConfiguration();
         setRequiredFieldsForRunningEvaluation();
         evaluation.setEvaluationConfiguration(aEvaluationConfiguration);
         evaluation.evaluate();
         String commandResultString = String.format(SUCCESSFULL_EXECUTION_COMMAND_RESULT, evaluationIdentifier,
               eLearningProblem.getLearningProblemIdentifier());
         commandResult = CommandResult.createSuccessCommandResult(commandResultString);
      } catch (ParameterValidationFailedException parameterValidationFailedException) {
         caughtException = new ParameterValidationFailedException(
               String.format(PARAMETER_ERROR_MESSAGE, parameterValidationFailedException.getMessage()), parameterValidationFailedException);
      } catch (EvaluationFailedException evaluationException) {
         caughtException = evaluationException;
      } finally {
         if (caughtException != null) {
            commandResult = CommandResult.createFailureCommandResult(caughtException);
            logger.error(caughtException.getMessage());
         }
      }
      return commandResult;
   }


   @Override
   public String getFailureReason() {
      return failureReason;
   }


   @Override
   public void undo() {
      throw new UnsupportedOperationException();
   }


   /**
    * Initializes the member variables.
    */
   private void init() {
      systemConfiguration = SystemConfiguration.getSystemConfiguration();
      eLearningProblem = systemConfiguration.getLearningProblem();
      evaluation = null;
      aEvaluationConfiguration = null;
      canBeExecuted = true;
      commandResult = null;
      caughtException = null;
      failureReason = StringUtils.EMPTY_STRING;
   }


   /**
    * Create the {@link EEvaluation} from the systemConfiguration. Set the evaluation identifier if
    * the command line parser augments are not provided. Checks from the system Configuration
    * evaluationConfigurationJsonObject.
    * 
    * @throws ParameterValidationFailedException if the validation of the values failed for
    *            evaluationConfiguration
    * @throws CannotCreateEvaluationForLearningProblemException if we cannot create the evaluation
    *            for given learning problem
    * @throws JsonParsingFailedException if the evaluation configuration string is not in correct
    *            Json format
    * @throws UnknownEvaluationIdentifierException if the evaluation identifier is not correct
    * @throws EvaluationDoesnotExistForLearningProblemException if the {@link Enum} for evaluation
    *            doesn't exist for given learning problem
    *
    */
   private void initiliazeEvaluationAndConfiguration()
         throws ParameterValidationFailedException,
            UnknownEvaluationIdentifierException,
            EvaluationDoesnotExistForLearningProblemException,
            CannotCreateEvaluationForLearningProblemException {
      JsonObject evaluationConfigurationJsonObject = systemConfiguration.getJsonEvaluationConfiguration();

      if (evaluationIdentifier.isEmpty()) {
         JsonElement evaluationName = evaluationConfigurationJsonObject.get(EvaluationsKeyValuePairs.EVALUATION_NAME);
         if (evaluationName != null) {
            evaluationIdentifier = evaluationName.getAsString();
         }
         logger.info(EVALUATION_IDENTIFIER_NOT_SET_IN_COMMAND_LINE_MESSAGE);
      }
      EEvaluation eEvaluation = EEvaluation.getEEvaluationByProblemAndIdentifier(eLearningProblem, evaluationIdentifier);
      if (eEvaluation == null) {
         if (EvaluationsKeyValuePairs.getEvaluationIdentifiers().contains(evaluationIdentifier)) {
            String errorMessage = String.format(EVALUATION_FOR_LEARNINGPROBLEM_NOT_EXIST_ERROR_MESSAGE, evaluationIdentifier,
                  eLearningProblem.getLearningProblemIdentifier());
            throw new EvaluationDoesnotExistForLearningProblemException(errorMessage);
         }
         String errorMessage = String.format(EVALUATION_IDENTIFIER_UNKNOWN_ERROR_MESSAGE, evaluationIdentifier);
         throw new UnknownEvaluationIdentifierException(errorMessage);
      }
      initliazeEvaluationConfiguration(eEvaluation, evaluationConfigurationJsonObject);
   }


   /**
    * Initializes the evaluation configuration of type {@link AEvaluationConfiguration} for the
    * current running evaluation.
    * 
    * @param eEvaluation the {@link EEvaluation}
    * @param evaluationConfigurationJsonObject the evaluation configuration string as json object
    * @throws ParameterValidationFailedException if the validation of the values failed for
    *            evaluationConfiguration
    * @throws CannotCreateEvaluationForLearningProblemException if we cannot create the evaluation
    *            for given learning problem
    */
   private void initliazeEvaluationConfiguration(EEvaluation eEvaluation, JsonObject evaluationConfigurationJsonObject)
         throws ParameterValidationFailedException,
            CannotCreateEvaluationForLearningProblemException {
      evaluation = eEvaluation.createEvaluation();
      if (evaluation == null) {
         String errorMessage = String.format(CANNOT_CREATE_EVALUATION_FORLEARNINGPROBLEM_ERROR_MESSAGE, evaluationIdentifier,
               eLearningProblem.getLearningProblemIdentifier());
         throw new CannotCreateEvaluationForLearningProblemException(errorMessage);
      }
      aEvaluationConfiguration = evaluation.getEvaluationConfiguration();
      if (aEvaluationConfiguration instanceof ASuppliedTestSetEvaluationConfiguration) {
         ((ASuppliedTestSetEvaluationConfiguration) aEvaluationConfiguration)
               .setTrainingDatasetFilesFromSystemConfiguration(systemConfiguration.getDatasetFiles());
      }
      aEvaluationConfiguration.setOutputFilePath(systemConfiguration.getOutputFilePath());
      if (!metricIdentifiers.isEmpty()) {
         List<EvaluationMetricJsonElement> evaluationMetricElementList = new ArrayList<>();
         for (String evaluationMetricIdentifier : metricIdentifiers) {
            evaluationMetricElementList.add(new EvaluationMetricJsonElement(evaluationMetricIdentifier, new JsonObject()));
         }
         JsonObject evaluationMetricJsonObject = getEvalautionMetricJsonArray(evaluationMetricElementList);
         aEvaluationConfiguration.overrideConfiguration(evaluationMetricJsonObject);
      }
      aEvaluationConfiguration.overrideConfiguration(evaluationConfigurationJsonObject);

   }


   /**
    * Initialize the fields required for the currentEvaluation i.e. the algorithms corresponding
    * dataset map and the list of String of evaluation metric identifies from the command line
    * overriding the {@link AEvaluationConfiguration} over the system configuration metrics.
    * 
    * @throws DatasetsOrLearningAlgorithmsNotSetForEvaluationException if datasets or learning
    *            algorithms are not set for evaluation
    * @throws CouldNotSetupEvaluationException if the evaluation model could not be setup.
    * @throws ParameterValidationFailedException if the validation of the values failed for
    *            evaluation metrics in evaluation configuration
    *
    */
   private void setRequiredFieldsForRunningEvaluation()
         throws DatasetsOrLearningAlgorithmsNotSetForEvaluationException,
            CouldNotSetupEvaluationException,
            ParameterValidationFailedException {
      List<DatasetFile> datasetFiles = systemConfiguration.getDatasetFiles();
      List<ILearningAlgorithm> learningAlgorithms = systemConfiguration.getLearningAlgorithms();

      // To check if the datasetFiles or learningAlgorithms list or both are empty
      StringBuilder errorsStringBuilder = new StringBuilder();
      errorsStringBuilder.append(StringUtils.EMPTY_STRING);
      if (datasetFiles.isEmpty()) {
         errorsStringBuilder.append(StringUtils.LINE_BREAK);
         errorsStringBuilder.append(String.format(DATASETSARENOTSET_ERROR_MESSAGE, evaluationIdentifier));
      }
      if (learningAlgorithms.isEmpty()) {
         errorsStringBuilder.append(StringUtils.LINE_BREAK);
         errorsStringBuilder.append(String.format(LEARNING_ALGORITHMS_ERROR_MESSAGE, evaluationIdentifier));
      }
      if (!errorsStringBuilder.toString().isEmpty()) {
         String errorMessage = errorsStringBuilder.toString();
         throw new DatasetsOrLearningAlgorithmsNotSetForEvaluationException(errorMessage);
      }
      evaluation.setupEvaluation(datasetFiles, learningAlgorithms);

      if (aEvaluationConfiguration.getListOfEvaluationSettingsWithSetNumber().isEmpty()) {
         String errorMessage = String.format(COULDNOT_SETUP_EVALUATION_ERROR_MESSAGE, evaluationIdentifier);
         throw new CouldNotSetupEvaluationException(errorMessage);
      }
   }


   /**
    * This function is used to check if all the initial parameters are set for the execution of the
    * {@codeEvaluateAlgorithmsCommand}.
    * 
    * @throws CommandCannotBeExecutedException if the initial parameters are not set for the
    *            execution of the command
    */
   private void checkInitialParametersForCommandExecution() throws CommandCannotBeExecutedException {
      if (eLearningProblem == null) {
         canBeExecuted = false;
         throw new CommandCannotBeExecutedException(LEARNING_PROBLEM_NOTSET_ERROR_MESSAGE);
      }
      JsonObject evaluationConfigurationJsonObject = systemConfiguration.getJsonEvaluationConfiguration();
      JsonElement evaluationName = null;
      JsonElement evaluationMetrics = null;
      if (evaluationConfigurationJsonObject.isJsonNull()) {
         logger.warn(EVALUATION_JSONSTRING_NOTSET_WARNING_MESSAGE);

      } else {
         evaluationName = evaluationConfigurationJsonObject.get(EvaluationsKeyValuePairs.EVALUATION_NAME);
         evaluationMetrics = evaluationConfigurationJsonObject.get(EvaluationsKeyValuePairs.EVALUATION_METRIC_ARRAY_IDENTIFIER);
      }
      if (evaluationIdentifier.isEmpty() && evaluationName == null) {
         canBeExecuted = false;
         String errorMessage = String.format(EVALUATION_IDENTIFIER_NOTSET_ERROR_MESSAGE, eLearningProblem.getLearningProblemIdentifier());
         throw new CommandCannotBeExecutedException(errorMessage);
      }
      if (metricIdentifiers.isEmpty() && evaluationMetrics == null) {
         logger.warn(EVALUATION_METRIC_IDENTIFIER_NOTSET_WARNING_MESSAGE);
      }

   }


   /**
    * The utility method to create json object for evaluation metric in the json configuration.
    * 
    * @param evaluationMetricElementArray the list of evaluation metric elements
    * @return the json object of the evaluation metric
    */
   public static JsonObject getEvalautionMetricJsonArray(List<EvaluationMetricJsonElement> evaluationMetricElementArray) {
      Gson gson = new GsonBuilder().create();
      JsonElement evaluationMetrics = gson.toJsonTree(evaluationMetricElementArray);
      JsonObject jsonObject = new JsonObject();
      jsonObject.add(EvaluationsKeyValuePairs.EVALUATION_METRIC_ARRAY_IDENTIFIER, evaluationMetrics);
      return jsonObject;
   }


   /**
    * Get the evaluation for current running evaluation instance.
    * 
    * @return the evaluation
    */
   public IEvaluation getEvaluation() {
      return evaluation;
   }
}
