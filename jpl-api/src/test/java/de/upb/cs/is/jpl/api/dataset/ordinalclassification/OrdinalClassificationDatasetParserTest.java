package de.upb.cs.is.jpl.api.dataset.ordinalclassification;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetParserTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.util.IOUtils;


/**
 * Tests for the {@link OrdinalClassificationDatasetParser}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationDatasetParserTest extends ADatasetParserTest {

   private static final String FAILED_GIVEN_DATASET_NUMBER_IS_UNKNOWN = "The given dataset number is unknown.";
   private static final String FAILED_NUMBER_OF_FEATURE_VECTORS_IS_INCORRECT = "The number of feature vectors is incorrect.";
   private static final String FAILED_NUMBER_OF_RATINGS_IS_INCORRECT = "The number of ratings is incorrect.";
   private static final String FAILED_NUMBER_OF_FEATURES_IS_INCORRECT = "The number of features is incorrect.";
   private static final String FAILED_DATASET_IS_NOT_INSTANCE_OF_ORDINAL_CLASSIFICATION_DATASET = "The dataset is not instance of "
         + OrdinalClassificationDataset.class.getSimpleName() + ".";
   private static final String FAILED_PREDICTION_CLASSES_ARE_NOT_CORRECTLY_SET_IN_DATASET = "The prediction classes are not correctly set in the dataset.";
   private static final String FAILED_PREDICTION_CLASSES_ARE_NOT_CORRECTLY_SET_IN_DATASET_PARAMS = "The prediction classes are not correctly set in the dataset. Expected <%s> but was <%s>";
   private static final String FAILED_SIZE_OF_PREDICTION_CLASSES_OF_DATASET_IS_NOT_CORRECTLY_SET = "The size of the prediction classes of the dataset is not correctly set.";

   private static final String RESOURCE_DIRECTORY_LEVEL = "ordinalclassification" + File.separator;

   private static final String INVALID_DATASET_FILE = "invalid_dataset.gprf";
   private static final String AUTOMOBILE_FOLDER_NAME = "automobile" + File.separator;
   private static final String AUTOMOBILE_WITHOUT_PREDICTION_CLASSES_FOLDER_NAME = "automobile_without_prediction_classes" + File.separator;


   /**
    * Creates a test for the {@link OrdinalClassificationDatasetParser}.
    */
   public OrdinalClassificationDatasetParserTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IDatasetParser getDatasetParser() {
      return new OrdinalClassificationDatasetParser();
   }


   @Override
   protected List<DatasetFile> getInvalidDatasets() {
      List<DatasetFile> datasetFileList = new ArrayList<>();
      datasetFileList.add(new DatasetFile(new File(getTestRessourcePathFor(INVALID_DATASET_FILE))));
      return datasetFileList;
   }


   @Override
   protected List<DatasetFile> getValidDatasets() {
      List<DatasetFile> datasetFileList = new ArrayList<>();
      datasetFileList.addAll(IOUtils.getListOfDatasetFilesInFolder(getTestRessourcePathFor(AUTOMOBILE_FOLDER_NAME)));
      datasetFileList
            .addAll(IOUtils.getListOfDatasetFilesInFolder(getTestRessourcePathFor(AUTOMOBILE_WITHOUT_PREDICTION_CLASSES_FOLDER_NAME)));
      return datasetFileList;
   }


   @Override
   protected void validateDataset(int datasetNumber, IDataset<?, ?, ?> dataset) {
      if (datasetNumber < 10) {
         validateAutomobileDatasets(dataset);
      } else if (datasetNumber < 20) {
         validateAutomobileDatasetsWithoutPredictionClasses(dataset);
      } else {
         fail(FAILED_GIVEN_DATASET_NUMBER_IS_UNKNOWN);
      }
   }


   /**
    * Checks weather the given dataset is an ordinal one, if the numbers of features and instances
    * are correct and if the list of prediction classes correctly set.
    * 
    * @param dataset the dataset to check
    */
   private void validateAutomobileDatasets(IDataset<?, ?, ?> dataset) {
      validateAutomobileDatasetFileSizes(dataset);
      OrdinalClassificationDataset ordinalDataset = (OrdinalClassificationDataset) dataset;

      double[] expectedPredictionClasses = { 0, 1, 2, 3, 4, 5 };
      List<Double> expectedPreditionClassList = new ArrayList<>();
      for (int i = 0; i < expectedPredictionClasses.length; i++) {
         expectedPreditionClassList.add(expectedPredictionClasses[i]);
      }
      assertEquals(FAILED_SIZE_OF_PREDICTION_CLASSES_OF_DATASET_IS_NOT_CORRECTLY_SET, expectedPreditionClassList.size(),
            ordinalDataset.getValidRatings().size());
      assertEquals(FAILED_PREDICTION_CLASSES_ARE_NOT_CORRECTLY_SET_IN_DATASET, expectedPreditionClassList,
            ordinalDataset.getValidRatings());
   }


   /**
    * Checks if the given dataset is an ordinal one, if the numbers of features and instances are
    * correct and if the list of prediction classes is empty.
    * 
    * @param dataset the dataset to check
    */
   private void validateAutomobileDatasetsWithoutPredictionClasses(IDataset<?, ?, ?> dataset) {
      validateAutomobileDatasetFileSizes(dataset);
      OrdinalClassificationDataset ordinalDataset = (OrdinalClassificationDataset) dataset;

      double[] expectedPredictionClasses = { -2, -1, 0, 1, 2, 3 };
      List<Double> expectedPreditionClassList = new ArrayList<>();
      for (int i = 0; i < expectedPredictionClasses.length; i++) {
         expectedPreditionClassList.add(expectedPredictionClasses[i]);
      }
      assertEquals(FAILED_SIZE_OF_PREDICTION_CLASSES_OF_DATASET_IS_NOT_CORRECTLY_SET, expectedPreditionClassList.size(),
            ordinalDataset.getValidRatings().size());
      assertTrue(String.format(FAILED_PREDICTION_CLASSES_ARE_NOT_CORRECTLY_SET_IN_DATASET_PARAMS,
            Arrays.toString(expectedPredictionClasses), ordinalDataset.getValidRatings()),
            expectedPreditionClassList.containsAll(ordinalDataset.getValidRatings()));
   }


   /**
    * Checks weather the given dataset is an ordinal one, if it has the correct number of features,
    * the correct number of instances and therefore the correct number of feature vectors and rating
    * information.
    * 
    * @param dataset the dataset to check
    */
   private void validateAutomobileDatasetFileSizes(IDataset<?, ?, ?> dataset) {
      assertTrue(FAILED_DATASET_IS_NOT_INSTANCE_OF_ORDINAL_CLASSIFICATION_DATASET, dataset instanceof OrdinalClassificationDataset);
      OrdinalClassificationDataset ordinalDataset = (OrdinalClassificationDataset) dataset;

      assertEquals(FAILED_NUMBER_OF_FEATURES_IS_INCORRECT, 25, ordinalDataset.getNumberOfFeatures());

      int expectedNumberOfInstances = 52;
      assertEquals(FAILED_NUMBER_OF_FEATURE_VECTORS_IS_INCORRECT, expectedNumberOfInstances, ordinalDataset.getFeatureVectors().size());
      assertEquals(FAILED_NUMBER_OF_RATINGS_IS_INCORRECT, expectedNumberOfInstances, ordinalDataset.getRatings().size());
   }

}
