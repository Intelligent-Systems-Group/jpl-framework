package de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Before;

import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
import de.upb.cs.is.jpl.api.dataset.ADatasetParserTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.util.TestUtils;


/**
 * Tests for {@link DefaultAbsoluteDatasetParser}.
 * 
 * @author Sebastian Osterbrink
 * 
 */
public class DefaultAbsoluteDatasetParserTest extends ADatasetParserTest {

   private static final String ITEM_FEATURE_NAME = "Item Feature Name";
   private static final String CONTEXT_FEATURE_NAME = "Context Feature Name";
   private static final String IS_DATASET_OF_TYPE_DEFAULT_ABSOLUTE_DATASET = "Is dataset of type DefaultAbsoluteDataset";
   private static final String ALLOWED_VALUES = "Allowed Values:";
   private static final String ASSERT_THAT_THE_CORRECT_NUMBER_OF_ELEMENTS_IS_IN_THE_DATASET = "Assert that the correct number of elements is in the dataset.";
   private static final String SPARSE_DATASET_GPRF = "sparseDataset.gprf";
   private static final String DENSE_DATASET_GPRF = "dataset.gprf";
   private static final String FEATURE_DATASET_GPRF = "dataset_features.gprf";
   private static final String INVALID_DATASET_GPRF = "invalidDataset.gprf";

   private static final String VECTOR_CONTENT = "Vector Content";
   private static final String VECTOR_DIMENSION = "Vector Dimension";
   private static final String NO_VALIDATION_DEFINED = "No validation defined.";

   private static final String[][] EXPECTED_FEATURE_NAMES = { { "age", "income", "sex" },
         { "degree of action", "didactic degree", "degree of love" } };

   private static final String RESOURCE_DIRECTORY_LEVEL = "defaultdataset" + File.separator;


   /**
    * Sets the resource path for this test level
    */
   public DefaultAbsoluteDatasetParserTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Initialize the {@link LoggingConfiguration}.
    */
   @Before
   public void init() {
      LoggingConfiguration.setupLoggingConfiguration();

   }


   @Override
   protected IDatasetParser getDatasetParser() {
      return new DefaultAbsoluteDatasetParser();
   }


   @Override
   protected List<DatasetFile> getInvalidDatasets() {
      List<DatasetFile> result = new ArrayList<DatasetFile>();
      result.add(new DatasetFile(new File(getTestRessourcePathFor(INVALID_DATASET_GPRF))));
      return result;
   }


   @Override
   protected List<DatasetFile> getValidDatasets() {
      List<DatasetFile> result = new ArrayList<DatasetFile>();
      result.add(new DatasetFile(new File(getTestRessourcePathFor(DENSE_DATASET_GPRF))));
      result.add(new DatasetFile(new File(getTestRessourcePathFor(SPARSE_DATASET_GPRF))));
      result.add(new DatasetFile(new File(getTestRessourcePathFor(FEATURE_DATASET_GPRF))));
      return result;
   }


   @Override
   protected void validateDataset(int testNumber, IDataset<?, ?, ?> parseResult) {
      Assert.assertTrue(IS_DATASET_OF_TYPE_DEFAULT_ABSOLUTE_DATASET, parseResult instanceof DefaultAbsoluteDataset);
      DefaultAbsoluteDataset dataset = (DefaultAbsoluteDataset) parseResult;
      switch (testNumber) {
         case 0:
            validateDenseDataset(dataset);
            break;
         case 1:
            validateSparseDataset(dataset);
            break;
         case 2:
            validateAllowedFeature(dataset);
            break;
         default:
            Assert.fail(NO_VALIDATION_DEFINED);
            break;
      }


   }


   /**
    * Validate the parsing of the feature names and allowed values parameters.
    * 
    * @param dataset the parsed dataset
    */
   private void validateAllowedFeature(DefaultAbsoluteDataset dataset) {
      validateDenseDataset(dataset);

      // Test allowed ordinal values for features
      List<Double> expectedAllowedValues = new ArrayList<>();
      expectedAllowedValues.add(0.0);
      expectedAllowedValues.add(1.0);
      Assert.assertEquals(ALLOWED_VALUES, expectedAllowedValues, dataset.getContextFeatureAllowedValues(2));

      // Test feature names
      for (int i = 0; i < EXPECTED_FEATURE_NAMES[0].length; i++) {
         Assert.assertEquals(CONTEXT_FEATURE_NAME, EXPECTED_FEATURE_NAMES[0][i], dataset.getContextFeature(i));
         Assert.assertEquals(ITEM_FEATURE_NAME, EXPECTED_FEATURE_NAMES[1][i], dataset.getItemFeature(i));
      }

      // Test allowed ordinal values for ratingss
      expectedAllowedValues.add(2.0);
      expectedAllowedValues.add(3.0);
      expectedAllowedValues.add(4.0);
      expectedAllowedValues.add(5.0);
      Assert.assertEquals(ALLOWED_VALUES, expectedAllowedValues, dataset.getRatingAllowedValues());
   }


   /**
    * Validate the dense dataset.
    * 
    * @param dataset the parsed dataset, which should be validated.
    */
   private void validateDenseDataset(DefaultAbsoluteDataset dataset) {
      // Check item
      double[][] expectedItemVectors = { { 5.3, 2.1, 2.5 }, { 3.1, 3.5, 1.7 }, { 2.5, 4.5, 2.6 } };
      for (int i = 0; i < expectedItemVectors.length; i++) {
         Assert.assertArrayEquals(VECTOR_CONTENT, expectedItemVectors[i], dataset.getItemVector(i), TestUtils.DOUBLE_DELTA);
      }
      Assert.assertEquals(ASSERT_THAT_THE_CORRECT_NUMBER_OF_ELEMENTS_IS_IN_THE_DATASET, 3, dataset.getNumberOfItems());

      // Check context
      double[][] expectedContextVectors = { { 20.0, 8.5, 1.0 }, { 30.0, 48.5, 0.0 }, { 15.0, 0.52, 1.0 } };
      for (int i = 0; i < expectedContextVectors.length; i++) {
         Assert.assertArrayEquals(VECTOR_CONTENT, expectedContextVectors[i], dataset.getContextVector(i), TestUtils.DOUBLE_DELTA);
      }
      Assert.assertEquals(ASSERT_THAT_THE_CORRECT_NUMBER_OF_ELEMENTS_IS_IN_THE_DATASET, 3, dataset.getNumberOfContexts());

      // Check all ratings:
      double[][] expectedRatingVectors = { { 2.0, 0, 4.0 }, { 0.0, 3.0, 0.0 }, { 0.0, 1.0, 4.0 } };
      for (int i = 0; i < expectedRatingVectors.length; i++) {
         Assert.assertArrayEquals(VECTOR_CONTENT, expectedRatingVectors[i], dataset.getInstance(i).getRating().asArray(),
               TestUtils.DOUBLE_DELTA);
      }
      Assert.assertEquals(ASSERT_THAT_THE_CORRECT_NUMBER_OF_ELEMENTS_IS_IN_THE_DATASET, 3, dataset.getNumberOfInstances());

   }


   /**
    * Validate the sparse dataset.
    * 
    * @param dataset the parsed dataset, which should be validated.
    */
   private void validateSparseDataset(DefaultAbsoluteDataset dataset) {
      int expectedDimension = 4;
      double[] expectedSquaredNorms = { 30.0, 25.25, 22.25, 22.25 };

      for (int i = 0; i < expectedSquaredNorms.length; i++) {
         assertCorrectVector(dataset.getItemVector(i), expectedDimension, Math.sqrt(expectedSquaredNorms[i]));
         assertCorrectVector(dataset.getContextVector(i), expectedDimension, Math.sqrt(expectedSquaredNorms[i]));
         assertCorrectVector(dataset.getInstance(i).getRating().asArray(), expectedDimension, Math.sqrt(expectedSquaredNorms[i]));
      }

   }


   /**
    * Assert that the given vector is the expected vector by checking the dimension an the norm.
    * 
    * @param vector the tested vector
    * @param expectedDimension the expected dimension of the vector
    * @param expectedNorm the expected norm of the vector
    */
   private void assertCorrectVector(double[] vector, int expectedDimension, double expectedNorm) {
      RealVector testVector = new ArrayRealVector(vector);
      Assert.assertEquals(VECTOR_DIMENSION, expectedDimension, testVector.getDimension());
      Assert.assertEquals(VECTOR_CONTENT, expectedNorm, testVector.getNorm(), TestUtils.DOUBLE_DELTA);
   }


}
