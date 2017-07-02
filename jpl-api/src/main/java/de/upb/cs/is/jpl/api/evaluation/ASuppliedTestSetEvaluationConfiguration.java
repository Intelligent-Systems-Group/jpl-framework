package de.upb.cs.is.jpl.api.evaluation;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This is an abstract class containing configuration for the supplied test set evaluation that is
 * running on current command. It can be extended for different types of evaluations. It can be
 * extended for different types of evaluations. You should always call up to your superclass when
 * implementing these methods:
 * <ul>
 * <li>validateParameters()</li>
 * <li>copyValues(IJsonConfiguration configuration)</li>
 * </ul>
 * 
 * @author Pritha Gupta
 * @see AEvaluationConfiguration
 * 
 */
public abstract class ASuppliedTestSetEvaluationConfiguration extends AEvaluationConfiguration {
   private static final String TEST_DATASET_ALREADY_EXISTS_FOR_ANOTHER_TRAINING_DATASET_MESSAGE = "Test dataset already exists for another training dataset, so you cannot provide this dataset for another training dataset for which it can tested";
   private static final String TESTING_DATASET_STRING = "Testing Dataset";
   private static final String TRAINING_DATASET_STRING = "Training Dataset";
   private static final String DATASET_FILE_NOT_SET_ERROR_MESSAGE = "Dataset file identifier not set for supplied test sets.";
   private static final String TEST_SET_NOT_SET_ERROR_MESSAGE = "Test Dataset is not set for the training dataset.";
   private static final String DATASET_FILE_NOT_FOUND_ERROR_MESSAGE = "Dataset file identifier does not have corresponding dataset file in the system configuration.";
   private static final String SUPLLIED_TESTSET_MESSAGE = "Evaluation ran on on training and test set pairs: %s \nSupplied-testset evaluation for %s.";

   private transient List<DatasetFile> trainingDatasetFilesFromSystemConfiguration;

   @SerializedName(EvaluationsKeyValuePairs.SUPPLIED_TEST_SET_FOR_DATASET)
   protected List<SuppliedTestSetWithTrainDataset> suppliedTestWithTrainDatasets;


   /**
    * Creates an abstract test set evaluation configuration and initialize it with default
    * configuration provided in the file.
    * 
    * @param defaultConfigurationFileName the default configuration file name
    */
   public ASuppliedTestSetEvaluationConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName);
      suppliedTestWithTrainDatasets = new ArrayList<>();
      trainingDatasetFilesFromSystemConfiguration = new ArrayList<>();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      List<String> testDatasetFilePaths = new ArrayList<>();
      for (SuppliedTestSetWithTrainDataset suppliedTestWithTrainDataset : suppliedTestWithTrainDatasets) {
         if (testDatasetFilePaths.contains(suppliedTestWithTrainDataset.getTestsetFilePath())) {
            throw new ParameterValidationFailedException(TEST_DATASET_ALREADY_EXISTS_FOR_ANOTHER_TRAINING_DATASET_MESSAGE);
         }
         validateTestDatasetAndTrainDatasetPair(suppliedTestWithTrainDataset.getDatasetFileName(),
               suppliedTestWithTrainDataset.getTestsetFilePath());
         testDatasetFilePaths.add(suppliedTestWithTrainDataset.getTestsetFilePath());

      }
   }


   /**
    * Checks the validity of one object of {@link SuppliedTestSetWithTrainDataset}.
    * 
    * @param datasetFileName the training dataset file name identifier
    * @param testSetFilePath the path to the test dataset
    * @throws ParameterValidationFailedException if the the value set for the test set dataset
    *            object is not valid
    */
   public void validateTestDatasetAndTrainDatasetPair(String datasetFileName, String testSetFilePath)
         throws ParameterValidationFailedException {

      if (datasetFileName == null || datasetFileName.isEmpty()) {
         throw new ParameterValidationFailedException(DATASET_FILE_NOT_SET_ERROR_MESSAGE);
      }

      File testFile = new File(testSetFilePath);
      if (testSetFilePath == null || testSetFilePath.isEmpty() || !testFile.exists()) {
         throw new ParameterValidationFailedException(TEST_SET_NOT_SET_ERROR_MESSAGE);
      }
      validateExistenceOfTrainingDatasetFileInConfiguration(datasetFileName);
   }


   /**
    * Validates weather the training dataset file name provided is present in the configuration.
    * 
    * @param datasetFileName the file name to be validated
    * @throws ParameterValidationFailedException if the dataset is not present in configuration.
    */
   public void validateExistenceOfTrainingDatasetFileInConfiguration(String datasetFileName) throws ParameterValidationFailedException {
      DatasetFile trainingDatasetFile = null;
      for (DatasetFile datasetFile : trainingDatasetFilesFromSystemConfiguration) {
         if (datasetFileName.equals(datasetFile.getFile().getName())) {
            trainingDatasetFile = datasetFile;
         }
      }
      if (trainingDatasetFile == null) {
         throw new ParameterValidationFailedException(DATASET_FILE_NOT_FOUND_ERROR_MESSAGE);
      }

   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      ASuppliedTestSetEvaluationConfiguration castedConfiguration = (ASuppliedTestSetEvaluationConfiguration) configuration;
      if (castedConfiguration.suppliedTestWithTrainDatasets != null && !castedConfiguration.suppliedTestWithTrainDatasets.isEmpty()) {
         this.suppliedTestWithTrainDatasets = CollectionsUtils.getDeepCopyOf(castedConfiguration.suppliedTestWithTrainDatasets);
      }
   }


   /**
    * Returns the test dataset file path for the provided training dataset.
    * 
    * @param datasetFile the training dataset
    * @return the supplied test file path provided for the training dataset in the configuration
    */
   public List<String> getTestSetPathsForTrainingDatasetFile(DatasetFile datasetFile) {
      List<String> testSetFilePaths = new ArrayList<>();
      for (SuppliedTestSetWithTrainDataset suppliedTestWithTrainDataset : suppliedTestWithTrainDatasets) {
         if (suppliedTestWithTrainDataset.getDatasetFileName().equals(datasetFile.getFile().getName())) {
            testSetFilePaths.add(suppliedTestWithTrainDataset.getTestsetFilePath());
         }
      }
      return testSetFilePaths;
   }


   /**
    * Sets the list of training {@link DatasetFile} stored in the system configuration, copied to
    * this class, or the list of dataset files formed by user for training.
    * 
    * @param trainingDatasetFilesFromSystemConfiguration the list of training {@link DatasetFile}
    *           stored in the system configuration
    */
   public void setTrainingDatasetFilesFromSystemConfiguration(List<DatasetFile> trainingDatasetFilesFromSystemConfiguration) {
      this.trainingDatasetFilesFromSystemConfiguration = CollectionsUtils.getDeepCopyOf(trainingDatasetFilesFromSystemConfiguration);
   }


   /**
    * Returns the list of testing dataset file paths and training dataset file names.
    * 
    * @return the suppliedTestWithTrainDatasets the list of testing dataset file paths and training
    *         dataset file names
    */
   public List<SuppliedTestSetWithTrainDataset> getSuppliedTestWithTrainDatasets() {
      return suppliedTestWithTrainDatasets;
   }


   /**
    * Sets the list of testing dataset file paths and training dataset file names.
    * 
    * @param suppliedTestWithTrainDatasets the suppliedTestWithTrainDatasets to set
    */
   public void setSuppliedTestWithTrainDatasets(List<SuppliedTestSetWithTrainDataset> suppliedTestWithTrainDatasets) {
      this.suppliedTestWithTrainDatasets = suppliedTestWithTrainDatasets;
   }


   /**
    * The list of training dataset files.
    * 
    * @return the trainingDatasetFilesFromSystemConfiguration
    */
   public List<DatasetFile> getTrainingDatasetFilesFromSystemConfiguration() {
      return trainingDatasetFilesFromSystemConfiguration;
   }


   /**
    * Adds the testing and the training dataset files in the configuration.
    * 
    * @param testDatasetFile the testing dataset file
    * @param trainingDatasetFile the training dataset file
    * @throws ParameterValidationFailedException if the added pair is not valid
    */
   public void addTestingAndTrainingDatasetPair(DatasetFile testDatasetFile, DatasetFile trainingDatasetFile)
         throws ParameterValidationFailedException {
      if (testDatasetFile != null && trainingDatasetFile != null) {
         SuppliedTestSetWithTrainDataset object = new SuppliedTestSetWithTrainDataset(trainingDatasetFile.getFile().getName(),
               testDatasetFile.getFile().getAbsolutePath());
         if (!suppliedTestWithTrainDatasets.contains(object)) {
            suppliedTestWithTrainDatasets.add(object);
         }
         if (!trainingDatasetFilesFromSystemConfiguration.contains(trainingDatasetFile)) {
            trainingDatasetFilesFromSystemConfiguration.add(trainingDatasetFile);
         }
         validateParameters();
      }
   }


   @Override
   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(StringUtils.LINE_BREAK);
      stringBuilder.append(StringUtils.repeat(StringUtils.DASH, 150));
      stringBuilder.append(StringUtils.LINE_BREAK);
      stringBuilder.append(EvaluationsOutputGenerator.getCenteredString(TRAINING_DATASET_STRING, 75));
      stringBuilder.append(EvaluationsOutputGenerator.getCenteredString(TESTING_DATASET_STRING, 75));
      stringBuilder.append(StringUtils.LINE_BREAK);
      stringBuilder.append(StringUtils.repeat(StringUtils.DASH, 150));
      stringBuilder.append(StringUtils.LINE_BREAK);
      for (SuppliedTestSetWithTrainDataset suppliedTestWithTrainDataset : suppliedTestWithTrainDatasets) {
         stringBuilder.append(EvaluationsOutputGenerator.getCenteredString(suppliedTestWithTrainDataset.getDatasetFileName(), 75));
         File file = new File(suppliedTestWithTrainDataset.getTestsetFilePath());
         stringBuilder.append(EvaluationsOutputGenerator.getCenteredString(file.getName(), 75));
         stringBuilder.append(StringUtils.LINE_BREAK);
      }
      stringBuilder.append(StringUtils.repeat(StringUtils.DASH, 150));
      stringBuilder.append(StringUtils.LINE_BREAK);
      return String.format(SUPLLIED_TESTSET_MESSAGE, stringBuilder.toString(), this.eLearningProblem.getLearningProblemIdentifier());
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((suppliedTestWithTrainDatasets == null) ? 0 : suppliedTestWithTrainDatasets.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof ASuppliedTestSetEvaluationConfiguration) {
         ASuppliedTestSetEvaluationConfiguration castedObject = ASuppliedTestSetEvaluationConfiguration.class.cast(secondObject);
         if (suppliedTestWithTrainDatasets == null) {
            if (castedObject.suppliedTestWithTrainDatasets != null)
               return false;
         } else if (suppliedTestWithTrainDatasets.equals(castedObject.suppliedTestWithTrainDatasets))
            return true;
      }
      return false;
   }
}
