package de.upb.cs.is.jpl.cli.command.readsystemconfiguration;


import java.io.File;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.AJsonConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.evaluation.EvaluationsKeyValuePairs;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfigurationKeyValue;


/**
 * This class is required for parsing of the SystemConfiguration.json file. The
 * SystemConfiguration.json file is converted with the Gson library into the
 * {@code JsonSystemConfiguration} class. Thereby it makes use of the AJsonConfiguraiton which
 * defines a parsing procedure.
 *
 * @author Andreas Kornelsen
 */
public class JsonSystemConfiguration extends AJsonConfiguration {

   private static final String VALIDATION_FIELD_EXCEPTION_MESSAGE = "A value for the '%s' field is not specified in the SystemConfiguration.json file.";
   private static final String VALIDATION_FIELD_WRONG_IDENTIFIER_MESSAGE = "The identifier for the 'learning_problem' is wrong, identifier: %s";
   private static final String FILE_PATH_NOT_SET_EXCEPTION_MESSAGE = "An absolut file path for the key 'file_path' must be set in the SystemConfiguration.json file.";
   private static final String FILE_NOT_EXSIST_EXCEPTION_MESSAGE = "File does not exist, filePath: %s";
   private static final String NOT_A_FILE_EXCEPTION_MESSAGE = "The provided absolut file path is not a file, file path: %s";
   private static final String ITEM_FEATURE_NEGATIVE_VALUE_EXCEPTION_MESSAGE = "Item feature has a negative value for file with file path: %s";
   private static final String CONTEXT_FEATURE_NEGATIVE_VALUE_EXCEPTION_MESSAGE = "Context feature has a negative value for file with file path: %s";
   private static final String VALIDATION_FIELD_EXCEPTION_MESSAGE_NO_EVALUATION_METRIC_SET = "There is no evaluation metric set, evaluationJsonString: %s";

   @SerializedName(SystemConfigurationKeyValue.INPUT_FILES)
   private List<JsonDatasetFile> inputFiles = null;

   @SerializedName(SystemConfigurationKeyValue.OUTPUT_FILE)
   private String outputFile = null;

   @SerializedName(SystemConfigurationKeyValue.LEARNING_PROBLEM)
   private String learningProblem = null;

   @SerializedName(SystemConfigurationKeyValue.ALGORITHMS)
   private JsonArray algorithms = null;

   @SerializedName(SystemConfigurationKeyValue.EVALUATION)
   private JsonObject evaluation = null;

   @SerializedName(SystemConfigurationKeyValue.SEED)
   private long seed = RandomGenerator.DEFAULT_SEED;


   /**
    * Instantiates a new system configuration file Gson converter.
    *
    */
   public JsonSystemConfiguration() {
      super(StringUtils.EMPTY_STRING, StringUtils.EMPTY_STRING);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      validateInputFiles();
      validateOutputFile();
      validateLearningProblem();
      validateAlgorithms();
      validateEvaluation();
   }


   /**
    * Validate the converted evaluation configuration.
    *
    * @throws ParameterValidationFailedException if evaluation is null
    */
   private void validateEvaluation() throws ParameterValidationFailedException {
      if (evaluation == null) {
         throw new ParameterValidationFailedException(
               String.format(VALIDATION_FIELD_EXCEPTION_MESSAGE, SystemConfigurationKeyValue.EVALUATION));
      }

      if (evaluation.get(EvaluationsKeyValuePairs.EVALUATION_METRIC_ARRAY_IDENTIFIER) == null
            || evaluation.getAsJsonArray(EvaluationsKeyValuePairs.EVALUATION_METRIC_ARRAY_IDENTIFIER) == null) {
         throw new ParameterValidationFailedException(
               String.format(VALIDATION_FIELD_EXCEPTION_MESSAGE_NO_EVALUATION_METRIC_SET, evaluation.toString()));
      }

   }


   /**
    * Validate the converted algorithms configuration.
    *
    * @throws ParameterValidationFailedException if algorithms is null
    */
   private void validateAlgorithms() throws ParameterValidationFailedException {
      if (algorithms == null) {
         throw new ParameterValidationFailedException(
               String.format(VALIDATION_FIELD_EXCEPTION_MESSAGE, SystemConfigurationKeyValue.ALGORITHMS));
      }
   }


   /**
    * Validate the converted learning problem.
    *
    * @throws ParameterValidationFailedException if the learning problem is null or empty or there
    *            is no enum for the learning problem identifier
    */
   private void validateLearningProblem() throws ParameterValidationFailedException {
      if (learningProblem == null || learningProblem.isEmpty()) {
         throw new ParameterValidationFailedException(
               String.format(VALIDATION_FIELD_EXCEPTION_MESSAGE, SystemConfigurationKeyValue.LEARNING_PROBLEM));
      }

      if (ELearningProblem.getELearningProblemByIdentifier(learningProblem) == null) {
         throw new ParameterValidationFailedException(String.format(VALIDATION_FIELD_WRONG_IDENTIFIER_MESSAGE, learningProblem));
      }
   }


   /**
    * Validate the converted output file.
    *
    * @throws ParameterValidationFailedException if the output file is null or empty
    */
   private void validateOutputFile() throws ParameterValidationFailedException {
      if (outputFile == null || outputFile.isEmpty()) {
         throw new ParameterValidationFailedException(
               String.format(VALIDATION_FIELD_EXCEPTION_MESSAGE, SystemConfigurationKeyValue.OUTPUT_FILE));
      }
   }


   /**
    * Validate the converted input files.
    *
    * @throws ParameterValidationFailedException if the input files are not set properly, for
    *            example a file can't be found for the provided file path
    */
   private void validateInputFiles() throws ParameterValidationFailedException {
      if (inputFiles != null) {
         for (JsonDatasetFile jsonInputFile : inputFiles) {
            validateInputFile(jsonInputFile);
         }
      } else {
         throw new ParameterValidationFailedException(
               String.format(VALIDATION_FIELD_EXCEPTION_MESSAGE, SystemConfigurationKeyValue.INPUT_FILES));
      }
   }


   /**
    * Validate the converted dataset file.
    *
    * @param jsonDatasetFile the json input file
    * @throws ParameterValidationFailedException if a parameter is not set correctly
    */
   private void validateInputFile(JsonDatasetFile jsonDatasetFile) throws ParameterValidationFailedException {
      String description = jsonDatasetFile.getDescription();
      String filePath = jsonDatasetFile.getFilePath();
      List<Integer> selectedContextFeatures = jsonDatasetFile.getSelectedContextFeatures();
      List<Integer> selectedItemFeature = jsonDatasetFile.getSelectedItemFeature();

      if (description == null) {
         jsonDatasetFile.setDescription(StringUtils.EMPTY_STRING);
      }

      if (filePath == null || filePath.isEmpty()) {
         throw new ParameterValidationFailedException(FILE_PATH_NOT_SET_EXCEPTION_MESSAGE);
      }

      jsonDatasetFile.setFilePath(filePath);

      File inputFile = new File(filePath);
      validateInputFile(inputFile, filePath);

      validateFeatures(selectedItemFeature, filePath, ITEM_FEATURE_NEGATIVE_VALUE_EXCEPTION_MESSAGE);
      validateFeatures(selectedContextFeatures, filePath, CONTEXT_FEATURE_NEGATIVE_VALUE_EXCEPTION_MESSAGE);
   }


   /**
    * Validate the selected feature numbers of the {@link IDataset}.
    *
    * @param selectedFeatures the selected features for the dataset file
    * @param filePath the file path to the dataset file
    * @param exceptionMessage the exception message for the thrown exception
    * @throws ParameterValidationFailedException if the feature is below 0
    */
   private void validateFeatures(List<Integer> selectedFeatures, String filePath, String exceptionMessage)
         throws ParameterValidationFailedException {
      if (selectedFeatures != null) {
         for (int itemFeatureIndex : selectedFeatures) {
            if (itemFeatureIndex < 0) {
               throw new ParameterValidationFailedException(String.format(exceptionMessage, filePath));
            }
         }
      }
   }


   /**
    * Validate the file.
    *
    * @param inputFile the input file which should be validated
    * @param filePath the file path to the input file
    * @throws ParameterValidationFailedException if the input file doesn't exist or the file is an
    *            directory
    */
   private void validateInputFile(File inputFile, String filePath) throws ParameterValidationFailedException {
      if (!inputFile.exists()) {
         throw new ParameterValidationFailedException(String.format(FILE_NOT_EXSIST_EXCEPTION_MESSAGE, filePath));
      }
      if (inputFile.isDirectory()) {
         throw new ParameterValidationFailedException(String.format(NOT_A_FILE_EXCEPTION_MESSAGE, filePath));
      }
   }


   @Override
   public void resetToDefault() {
      // There is no default configuration for the system configuration, therefore this method is
      // overwritten.
   }


   @Override
   public void initializeDefaultConfiguration() {
      // There is no default configuration for the system configuration, therefore this method is
      // overwritten.
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      JsonSystemConfiguration castedConfiguration = (JsonSystemConfiguration) configuration;

      if (castedConfiguration.getInputFiles() != null) {
         this.inputFiles = castedConfiguration.getInputFiles();
      }
      if (castedConfiguration.getLearningProblem() != null) {
         this.learningProblem = castedConfiguration.getLearningProblem();
      }
      if (castedConfiguration.getOutputFile() != null) {
         this.outputFile = castedConfiguration.getOutputFile();
      }
      if (castedConfiguration.getAlgorithms() != null) {
         this.algorithms = castedConfiguration.getAlgorithms();
      }
      if (castedConfiguration.getEvaluations() != null) {
         this.evaluation = castedConfiguration.getEvaluations();
      }

      this.seed = castedConfiguration.getSeed();
   }


   /**
    * Returns the algorithms configuration.
    *
    * @return the algorithms configuration
    */
   public JsonArray getAlgorithms() {
      return algorithms;
   }


   /**
    * Sets the algorithms configuration.
    *
    * @param algorithms the new algorithms configuration
    */
   public void setAlgorithms(JsonArray algorithms) {
      this.algorithms = algorithms;
   }


   /**
    * Returns the evaluations configuration.
    *
    * @return the evaluations configuration
    */
   public JsonObject getEvaluations() {
      return evaluation;
   }


   /**
    * Sets the evaluations configuration.
    *
    * @param evaluations the new evaluations configuration
    */
   public void setEvaluations(JsonObject evaluations) {
      this.evaluation = evaluations;
   }


   /**
    * Returns the input files.
    *
    * @return the input files
    */
   public List<JsonDatasetFile> getInputFiles() {
      return inputFiles;
   }


   /**
    * Sets the input files.
    *
    * @param inputFiles the new input files
    */
   public void setInputFiles(List<JsonDatasetFile> inputFiles) {
      this.inputFiles = inputFiles;
   }


   /**
    * Returns the output file.
    *
    * @return the output file
    */
   public String getOutputFile() {
      return outputFile;
   }


   /**
    * Sets the output file.
    *
    * @param outputFile the new output file
    */
   public void setOutputFile(String outputFile) {
      this.outputFile = outputFile;
   }


   /**
    * Returns the learning problem.
    *
    * @return the learning problem
    */
   public String getLearningProblem() {
      return learningProblem;
   }


   /**
    * Sets the learning problem.
    *
    * @param learningProblem the new learning problem
    */
   public void setLearningProblem(String learningProblem) {
      this.learningProblem = learningProblem;
   }


   /**
    * Returns the seed.
    *
    * @return the seed
    */
   public long getSeed() {
      return seed;
   }


   /**
    * Sets the seed.
    *
    * @param seed the seed to set
    */
   public void setSeed(long seed) {
      this.seed = seed;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((algorithms == null) ? 0 : algorithms.hashCode());
      result = prime * result + ((evaluation == null) ? 0 : evaluation.hashCode());
      result = prime * result + ((inputFiles == null) ? 0 : inputFiles.hashCode());
      result = prime * result + ((learningProblem == null) ? 0 : learningProblem.hashCode());
      result = prime * result + ((outputFile == null) ? 0 : outputFile.hashCode());
      result = prime * result + (int) (seed ^ (seed >>> 32));
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof JsonSystemConfiguration))
         return false;
      JsonSystemConfiguration other = (JsonSystemConfiguration) obj;
      if (algorithms == null) {
         if (other.algorithms != null)
            return false;
      } else if (!algorithms.equals(other.algorithms))
         return false;
      if (evaluation == null) {
         if (other.evaluation != null)
            return false;
      } else if (!evaluation.equals(other.evaluation))
         return false;
      if (inputFiles == null) {
         if (other.inputFiles != null)
            return false;
      } else if (!inputFiles.equals(other.inputFiles))
         return false;
      if (learningProblem == null) {
         if (other.learningProblem != null)
            return false;
      } else if (!learningProblem.equals(other.learningProblem))
         return false;
      if (outputFile == null) {
         if (other.outputFile != null)
            return false;
      } else if (!outputFile.equals(other.outputFile))
         return false;
      if (seed != other.seed)
         return false;
      return true;
   }

}
