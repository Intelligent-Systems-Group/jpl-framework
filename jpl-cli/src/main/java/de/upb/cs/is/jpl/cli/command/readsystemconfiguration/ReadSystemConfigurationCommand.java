package de.upb.cs.is.jpl.cli.command.readsystemconfiguration;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.evaluation.EvaluationsKeyValuePairs;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfigurationKeyValue;


/**
 * The main purpose for ReadSystemConfigurationCommand is to execute the
 * {@link JsonSystemConfiguration} and set the parameters of the {@link SystemConfiguration}.
 *
 * @author Andreas Kornelsen
 */
public class ReadSystemConfigurationCommand extends ACommand {

   private static final Logger logger = LoggerFactory.getLogger(ReadSystemConfigurationCommand.class);

   private static final String ERROR_REASON = "There is a problem with the converting the SystemConfiguration.json file into JsonSystemConfiguration."
         + StringUtils.LINE_BREAK;
   private static final String ERROR_REASON_INFO = StringUtils.LINE_BREAK + " For the stack trace take a look into the logging file.";
   private static final String ERROR_JSON_EXCEPTION_MESSAGE = "The provided SystemConfiguration file seems to be not valid, filePath: %s";
   private static final String ERROR_FILE_PATH_NOT_CORRECT_EXCEPTION_MESSAGE = "The filePath for the SystemConfigration.json file is not correct. The file doesn't exists, filePath: %s";
   private static final String ERROR_CONVERSION_EXCEPTION_MESSAGE = "The conversion of the SystemConfigration.json file was not successful.";
   private static final String ERROR_FILE_PATH_IS_DIRECTORY_EXCEPTION_MESSAGE = "The filePath for the SystemConfiguration.json file is a directory, filePath: %s";
   private static final String ERROR_EMPTY_FILEPATH_EXCEPTION_MESSAGE = "The system configuration absolute path is empty.";

   private static final String CONVERSION_WARN_MESSAGE = "The conversion of the SystemConfigration.json file seems to be not successful.";

   private static final String TUPLE = "%s: %s";

   private static final String USED_FOR_JSON = "Used JSON for ";

   private static final String MESSAGE_CONSOLE_MUTLIPLE_ALGORITHMS_ARE_SET_CORRECTLY = "%d algorithms are set correctly.";
   private static final String MESSAGE_CONSOLE_SEED_SET_CORRECTLY = "The%s seed is set to %d.";
   private static final String MESSAGE_CONSOLE_ONE_ALGORITHM_IS_SET_CORRECTLY = "%d algorithm is set correctly.";

   private static final String MESSAGE_CONSOLE_MUTLIPLE_EVALUATIONS_METRICS_ARE_SET_CORRECTLY = "Is set correctly with %d evaluation metrics.";
   private static final String MESSAGE_CONSOLE_ONE_EVALUATION_METRIC_IS_SET_CORRECTLY = "Is set correctly with %d evaluation metric.";

   private static final Object DEAULT_STRING = " default";

   private String systemConfigurationAbsolutePath = StringUtils.EMPTY_STRING;
   private String failureReason = StringUtils.EMPTY_STRING;

   private Gson gsonPrettyPrinting = new GsonBuilder().setPrettyPrinting().create();


   /**
    * Creates an add read system configuration command based on the path to a
    * SystemConfiguration.json file.
    * 
    * @param systemConfigurationAbsolutPath the absolute file path to the SystemConfiguration.json
    *           file
    */
   public ReadSystemConfigurationCommand(String systemConfigurationAbsolutPath) {
      super(ECommand.READ_SYSTEM_CONFIGURATION.getCommandIdentifier());
      this.systemConfigurationAbsolutePath = systemConfigurationAbsolutPath;
   }


   @Override
   public boolean canBeExecuted() {
      boolean canBeExecuted = false;
      try {
         getJsonSystemConfiguration();
         canBeExecuted = true;
      } catch (ParameterValidationFailedException pvfe) {
         failureReason = pvfe.getMessage();
         logger.error(CONVERSION_WARN_MESSAGE, pvfe);
      } catch (JsonParsingFailedException jpfe) {
         failureReason = jpfe.getMessage();
         logger.error(String.format(ERROR_JSON_EXCEPTION_MESSAGE, systemConfigurationAbsolutePath), jpfe);
      }

      return canBeExecuted;
   }


   /**
    * Returns the {@link JsonSystemConfiguration} from the SystemConfiguration.json file by a
    * conversion with Gson.
    *
    * @return a converted JsonSystemConfiguration class from a SystemConfiguraiton.json file
    * @throws ParameterValidationFailedException if the systemConfigurationAbsolutPath is not set
    *            correctly
    * @throws JsonParsingFailedException if the JSON can't be parsed correctly
    */
   private JsonSystemConfiguration getJsonSystemConfiguration() throws ParameterValidationFailedException, JsonParsingFailedException {

      JsonSystemConfiguration jsonSystemConfiguration = new JsonSystemConfiguration();
      File systemConfigurationFile = checkSystemConfigurationAbsolutePath();

      JsonObject systemConfigurationJsonObject = JsonUtils.createJsonObjectFromFile(systemConfigurationFile);
      jsonSystemConfiguration.initializeConfiguration(systemConfigurationJsonObject);

      return jsonSystemConfiguration;
   }


   /**
    * Checks the systemConfigurationAbsolutePath whether it is set correctly or not.
    *
    * @return the File class for the systemConfigurationAbsolutPath
    * @throws ParameterValidationFailedException if the path to a directory is not set correctly
    */
   private File checkSystemConfigurationAbsolutePath() throws ParameterValidationFailedException {
      if (systemConfigurationAbsolutePath == null || systemConfigurationAbsolutePath.isEmpty()) {
         throw new ParameterValidationFailedException(ERROR_EMPTY_FILEPATH_EXCEPTION_MESSAGE);
      }

      File systemConfigurationFile = new File(systemConfigurationAbsolutePath);
      if (!systemConfigurationFile.exists()) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_FILE_PATH_NOT_CORRECT_EXCEPTION_MESSAGE, systemConfigurationAbsolutePath));
      }
      if (systemConfigurationFile.isDirectory()) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_FILE_PATH_IS_DIRECTORY_EXCEPTION_MESSAGE, systemConfigurationAbsolutePath));
      }

      return systemConfigurationFile;
   }


   @Override
   public CommandResult executeCommand() {
      JsonSystemConfiguration jsonSystemConfiguration = null;

      try {
         jsonSystemConfiguration = getJsonSystemConfiguration();
         String successfullySetParametersInSystemConfiguration = setSystemConfigurationParameters(jsonSystemConfiguration);
         CommandResult successfulCommandResult = CommandResult.createSuccessCommandResult(true);
         successfulCommandResult.setAdditionalInformation(successfullySetParametersInSystemConfiguration);
         return successfulCommandResult;
      } catch (ParameterValidationFailedException e) {
         logger.error(ERROR_CONVERSION_EXCEPTION_MESSAGE, e);
         return CommandResult.createFailureCommandResult(e);
      } catch (JsonParsingFailedException jpfe) {
         logger.error(String.format(ERROR_JSON_EXCEPTION_MESSAGE, systemConfigurationAbsolutePath), jpfe);
         return CommandResult.createFailureCommandResult(jpfe);
      }
   }


   /**
    * Sets the fields of {@link SystemConfiguration} from {@link JsonSystemConfiguration}.
    *
    * @param jsonSystemConfiguration the new system configuration parameters which should be set in
    *           the {@link SystemConfiguration}
    * @return successfullySetParameters the list of parameters which are set correctly
    */
   private String setSystemConfigurationParameters(JsonSystemConfiguration jsonSystemConfiguration) {
      SystemConfiguration systemConfiguration = SystemConfiguration.getSystemConfiguration();
      systemConfiguration.getDatasetFiles().addAll(getDatasetFilesFromJsonInputFiles(jsonSystemConfiguration.getInputFiles()));
      systemConfiguration.setJsonAlgorithmConfiguration(jsonSystemConfiguration.getAlgorithms());
      systemConfiguration.setJsonEvaluationConfiguration(jsonSystemConfiguration.getEvaluations());

      systemConfiguration.setOutputFilePath(jsonSystemConfiguration.getOutputFile());
      systemConfiguration
            .setLearningProblem(ELearningProblem.getELearningProblemByIdentifier(jsonSystemConfiguration.getLearningProblem()));

      long seed = jsonSystemConfiguration.getSeed();
      systemConfiguration.setSeed(seed);

      return getFormattedParameterOutputStringForConsoleAndLogAdditionalInformormation(systemConfiguration, seed);
   }


   /**
    * Builds the successful parameter output string for the console output of the
    * ReadSystemConfigurationCommand.
    *
    * @param systemConfiguration the SystemConfiguration after the parsing of the
    *           SystmeConfiguration file
    * @param seed the seed for the random variable in the system configuration
    * @return successfully set parameters for the console output
    */
   private String getFormattedParameterOutputStringForConsoleAndLogAdditionalInformormation(SystemConfiguration systemConfiguration,
         long seed) {
      StringBuilder successfullySetParameters = new StringBuilder();
      StringBuilder inputFileConsoleOutput = new StringBuilder();

      List<DatasetFile> datasetFiles = systemConfiguration.getDatasetFiles();
      String separator = StringUtils.TAB_SPACE;
      for (DatasetFile datasetFile : datasetFiles) {
         String datasetAbsoluteFile = datasetFile.getFile().getAbsolutePath();
         inputFileConsoleOutput.append(separator);
         inputFileConsoleOutput.append(datasetAbsoluteFile);
         inputFileConsoleOutput.append(StringUtils.LINE_BREAK);

         separator = StringUtils.TAB_SPACE + StringUtils.TAB_SPACE + StringUtils.TAB_SPACE;
      }


      successfullySetParameters.append(String.format(TUPLE, SystemConfigurationKeyValue.INPUT_FILES, inputFileConsoleOutput.toString()));

      successfullySetParameters.append(
            String.format(TUPLE, SystemConfigurationKeyValue.OUTPUT_FILE, StringUtils.TAB_SPACE + systemConfiguration.getOutputFilePath()));
      successfullySetParameters.append(StringUtils.LINE_BREAK);

      successfullySetParameters.append(String.format(TUPLE, SystemConfigurationKeyValue.LEARNING_PROBLEM,
            systemConfiguration.getLearningProblem().getLearningProblemIdentifier()));
      successfullySetParameters.append(StringUtils.LINE_BREAK);


      int learningAlgorithmsSize = systemConfiguration.getLearningAlgorithmsConfigurationJsonArray().size();
      int evaluationMetricsSize = systemConfiguration.getJsonEvaluationConfiguration()
            .getAsJsonArray(EvaluationsKeyValuePairs.EVALUATION_METRIC_ARRAY_IDENTIFIER).size();
      String messageAlgorithmsSet = MESSAGE_CONSOLE_MUTLIPLE_ALGORITHMS_ARE_SET_CORRECTLY;
      String messageEvaluationsSet = MESSAGE_CONSOLE_MUTLIPLE_EVALUATIONS_METRICS_ARE_SET_CORRECTLY;
      if (learningAlgorithmsSize == 1) {
         messageAlgorithmsSet = MESSAGE_CONSOLE_ONE_ALGORITHM_IS_SET_CORRECTLY;
      }
      if (evaluationMetricsSize == 1) {
         messageEvaluationsSet = MESSAGE_CONSOLE_ONE_EVALUATION_METRIC_IS_SET_CORRECTLY;
      }

      successfullySetParameters.append(String.format(TUPLE, SystemConfigurationKeyValue.ALGORITHMS,
            StringUtils.TAB_SPACE + String.format(messageAlgorithmsSet, learningAlgorithmsSize)));
      successfullySetParameters.append(StringUtils.LINE_BREAK);

      successfullySetParameters.append(String.format(TUPLE, SystemConfigurationKeyValue.EVALUATION,
            StringUtils.TAB_SPACE + String.format(messageEvaluationsSet, evaluationMetricsSize)));

      successfullySetParameters.append(StringUtils.LINE_BREAK);
      if (seed == RandomGenerator.DEFAULT_SEED) {
         successfullySetParameters.append(String.format(TUPLE, SystemConfigurationKeyValue.SEED,
               StringUtils.TAB_SPACE + StringUtils.TAB_SPACE + String.format(MESSAGE_CONSOLE_SEED_SET_CORRECTLY, DEAULT_STRING, seed)));
      } else {
         successfullySetParameters.append(String.format(TUPLE, SystemConfigurationKeyValue.SEED, StringUtils.TAB_SPACE
               + StringUtils.TAB_SPACE + String.format(MESSAGE_CONSOLE_SEED_SET_CORRECTLY, StringUtils.EMPTY_STRING, seed)));
      }


      String formattedParameterOutputStringForConsole = successfullySetParameters.toString();

      String prettyPrintedAndTabSpacedEvaluationConfiguration = getTabSpacedString(
            gsonPrettyPrinting.toJson(systemConfiguration.getJsonEvaluationConfiguration()));

      String prettyPrintedAndTabSpacedAlgorithmConfiguration = getTabSpacedString(
            gsonPrettyPrinting.toJson(systemConfiguration.getLearningAlgorithmsConfigurationJsonArray()));

      successfullySetParameters.append(StringUtils.LINE_BREAK);
      successfullySetParameters.append(String.format(TUPLE, USED_FOR_JSON + SystemConfigurationKeyValue.ALGORITHMS,
            StringUtils.LINE_BREAK + prettyPrintedAndTabSpacedAlgorithmConfiguration));
      successfullySetParameters.append(StringUtils.LINE_BREAK);


      successfullySetParameters.append(String.format(TUPLE, USED_FOR_JSON + SystemConfigurationKeyValue.EVALUATION,
            StringUtils.LINE_BREAK + prettyPrintedAndTabSpacedEvaluationConfiguration));


      logger.debug(successfullySetParameters.toString());

      return formattedParameterOutputStringForConsole;
   }


   /**
    * Includes for the given {@code prettyJsonString} tabs for each line.
    * 
    * @param prettyJsonStirng the string which should be formatted with tabs before each line
    * @return the tab spaced pretty JSON string
    */
   private String getTabSpacedString(String prettyJsonString) {
      String[] split = prettyJsonString.split(StringUtils.LINE_BREAK);
      StringBuilder tabSpaceString = new StringBuilder();
      for (String line : split) {
         tabSpaceString.append(StringUtils.TAB_SPACE + StringUtils.TAB_SPACE + StringUtils.TAB_SPACE);
         tabSpaceString.append(line);
         tabSpaceString.append(StringUtils.LINE_BREAK);
      }
      return tabSpaceString.toString();
   }


   /**
    * Returns a list of {@link DatasetFile} from a list of {@link InputFile}.
    *
    * @param inputFiles the input files
    * @return the dataset files from json input files
    */
   private List<DatasetFile> getDatasetFilesFromJsonInputFiles(List<JsonDatasetFile> inputFiles) {
      ArrayList<DatasetFile> datasetFiles = new ArrayList<>();

      for (JsonDatasetFile inputFile : inputFiles) {
         String description = inputFile.getDescription();
         String absoluteFilePath = inputFile.getFilePath();
         List<Integer> selectedContextFeatures = inputFile.getSelectedContextFeatures();
         List<Integer> selectedItemFeature = inputFile.getSelectedItemFeature();

         DatasetFile datasetFile = new DatasetFile(new File(absoluteFilePath));
         datasetFile.setComment(description);
         datasetFile.setContextFeatures(selectedContextFeatures);
         datasetFile.setItemFeatures(selectedItemFeature);

         datasetFiles.add(datasetFile);
      }

      return datasetFiles;
   }


   @Override
   public String getFailureReason() {
      if (failureReason.isEmpty()) {
         return StringUtils.EMPTY_STRING;
      }
      return ERROR_REASON + failureReason + ERROR_REASON_INFO;
   }


   @Override
   public void undo() {
      SystemConfiguration.getSystemConfiguration().resetSystemConfiguration();
   }
}