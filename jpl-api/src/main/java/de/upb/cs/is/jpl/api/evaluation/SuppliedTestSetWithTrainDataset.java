package de.upb.cs.is.jpl.api.evaluation;


import java.io.File;

import com.google.gson.annotations.SerializedName;


/**
 * Required to parse the supplied test set from the evaluation configuration for the
 * {@link EvaluationsKeyValuePairs#SUPPLIED_TEST_SET_FOR_DATASET} evaluations.
 * 
 * @author Pritha Gupta
 *
 */
public class SuppliedTestSetWithTrainDataset {


   private static final String TESTSET_FILE_PATH = "testset_file_path";
   private static final String DATASET_FILE_NAME = "dataset_file_name";
   private static final String TEST_TRAIN_DATASET_MESSAGE = DATASET_FILE_NAME + "= %s," + TESTSET_FILE_PATH + "= %s ";

   @SerializedName(DATASET_FILE_NAME)
   private String datasetFileName;

   @SerializedName(TESTSET_FILE_PATH)
   private String testsetFilePath;


   /**
    * Creates the a new {@link SuppliedTestSetWithTrainDataset} with test dataset file path and
    * training dataset filename.
    * 
    * @param datasetFileName the training dataset file name
    * @param testsetFilePath the test dataset file path
    */
   public SuppliedTestSetWithTrainDataset(String datasetFileName, String testsetFilePath) {
      super();
      this.datasetFileName = datasetFileName;
      this.testsetFilePath = testsetFilePath;
   }


   /**
    * Returns the training dataset file name identifier.
    * 
    * @return the identifier name
    */
   public String getDatasetFileName() {
      return datasetFileName;
   }


   /**
    * Returns the path for the test dataset.
    * 
    * @return the path for the test dataset
    */
   public String getTestsetFilePath() {
      return testsetFilePath;
   }


   /**
    * Sets the training dataset file name identifier.
    * 
    * @param testsetFilePath the testsetFilePath to set
    */
   public void setTestsetFilePath(String testsetFilePath) {
      this.testsetFilePath = testsetFilePath;
   }


   /**
    * Sets the training dataset file name identifier.
    * 
    * @param datasetFileName the datasetFileName to set
    */
   public void setDatasetFileName(String datasetFileName) {
      this.datasetFileName = datasetFileName;
   }


   @Override
   public String toString() {
      File file = new File(testsetFilePath);
      return String.format(TEST_TRAIN_DATASET_MESSAGE, datasetFileName, file.getName());
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((datasetFileName == null) ? 0 : datasetFileName.hashCode());
      result = prime * result + ((testsetFilePath == null) ? 0 : testsetFilePath.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      SuppliedTestSetWithTrainDataset other = (SuppliedTestSetWithTrainDataset) obj;
      if (datasetFileName == null) {
         if (other.datasetFileName != null)
            return false;
      } else if (!datasetFileName.equals(other.datasetFileName))
         return false;
      if (testsetFilePath == null) {
         if (other.testsetFilePath != null)
            return false;
      } else if (!testsetFilePath.equals(other.testsetFilePath))
         return false;
      return true;
   }
}
