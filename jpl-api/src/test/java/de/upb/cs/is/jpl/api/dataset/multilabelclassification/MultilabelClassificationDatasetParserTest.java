package de.upb.cs.is.jpl.api.dataset.multilabelclassification;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import de.upb.cs.is.jpl.api.dataset.ADatasetParserTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;
import de.upb.cs.is.jpl.api.util.TestUtils;


/**
 * This class is responsible for testing the {@link MultilabelClassificationDatasetParser}. This is
 * done via the MULAN birds dataset, by checking general statistics of the parsed dataset and
 * samples of the feature and target vectors.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationDatasetParserTest extends ADatasetParserTest {

   private static final String ERROR_VECTOR_MISMATCH = "Vector mismatch: Expected %s, but found %s.";
   private static final String ERROR_INVALID_AMOUNT_OF_CONTEXT_FEATURES = "Invalid amount of context features. Expected %d, but found %d.";
   private static final String ERROR_INVALID_AMOUNT_OF_LABELS = "Invalid amount of labels. Expected %d, but found %d.";
   private static final String ERROR_INVALID_AMOUNT_OF_TARGET_VECTORS = "Invalid amount of target vectors. Expected %d, but found %d.";
   private static final String ERROR_INVALID_AMOUNT_OF_INSTANCES = "Invalid amount of instances. Expected %d, but found %d.";
   private static final String ERROR_INVALID_AMOUNT_OF_FEATURE_VECTORS = "Invalid amount of feature vectors. Expected %d feature vectors, but found %d feature vectors.";
   private static final String ERROR_INVALID_DATASET_TYPE = "Invalid dataset type: %s";
   private static final String ERROR_INVALID_DATASET_NUMBER = "Invalid dataset number: Unexpected test.";

   private static final String RESOURCE_DIRECTORY_LEVEL = "multilabelclassification" + File.separator;
   private static final String BIRDS_DATASET_NAME = "birds-arff.gprf";
   private static final String INSTANCE_RANKING_DATASET_NAME = "instancerankingtest.gprf";

   private static final int BIRDS_DATASET_EXPECTED_NUMBER_OF_INSTANCES = 323;
   private static final int BIRDS_DATASET_EXPECTED_NUMBER_OF_LAST_INSTANCE = 322;
   private static final int BIRDS_DATASET_EXPECTED_NUMBER_OF_LABELS = 19;
   private static final int BIRDS_DATASET_EXPECTED_NUMBER_OF_FEATURES = 260;

   private MultilabelClassificationDataset dataset;


   /**
    * Creates a {@link MultilabelClassificationDatasetParserTest}.
    */
   public MultilabelClassificationDatasetParserTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected List<DatasetFile> getInvalidDatasets() {
      List<DatasetFile> invalidDatasets = new ArrayList<>(1);
      invalidDatasets.add(new DatasetFile(new File(getTestRessourcePathFor(INSTANCE_RANKING_DATASET_NAME))));
      return invalidDatasets;
   }


   @Override
   protected List<DatasetFile> getValidDatasets() {
      List<DatasetFile> validDatasets = new ArrayList<>(1);
      validDatasets.add(new DatasetFile(new File(getTestRessourcePathFor(BIRDS_DATASET_NAME))));
      return validDatasets;
   }


   @Override
   protected void validateDataset(int datasetNumber, IDataset<?, ?, ?> dataset) {
      assertThatDatasetNumberIsCorrect(datasetNumber);
      assertThatDatasetTypeIsCorrect(dataset);

      this.dataset = (MultilabelClassificationDataset) dataset;

      assertThatFeatureVectorSamplesAreCorrect();
      assertThatTargetVectorSamplesAreCorrect();
      assertThatDatasetStatisticsAreCorrect();

   }


   /**
    * Checks whether the dataset number is correct.
    * 
    * @param datasetNumber the dataset number to check
    */
   private void assertThatDatasetNumberIsCorrect(int datasetNumber) {
      if (datasetNumber != 0) {
         Assert.fail(ERROR_INVALID_DATASET_NUMBER);
      }
   }


   /**
    * Checks whether the given dataset is of the correct type.
    * 
    * @param dataset the dataset to check
    */
   private void assertThatDatasetTypeIsCorrect(IDataset<?, ?, ?> dataset) {
      if (!MultilabelClassificationDataset.class.isInstance(dataset)) {
         Assert.fail(String.format(ERROR_INVALID_DATASET_TYPE, dataset.getClass().getSimpleName()));
      }
   }


   /**
    * Checks whether three samples of feature vectors are parsed correctly.
    */
   private void assertThatFeatureVectorSamplesAreCorrect() {
      double[] firstInstanceFeatureVector = { 0.132445, 0.143931, 0.227729, 0.298556, 0.385907, 0.378363, 0.354708, 0.384165, 0.360092,
            0.347465, 0.341827, 0.349941, 0.349431, 0.425508, 0.470748, 0.545955, 0.51548, 0.439492, 0.287173, 0.134652, 0.082536, 0.001778,
            0.009794, 0.006053, 0.00889, 0.011114, 0.010665, 0.010292, 0.008779, 0.008474, 0.008938, 0.006323, 0.006312, 0.00545, 0.005856,
            0.004785, 0.005768, 0.005577, 0.004093, 0.002956, 0.001453, 0.000378, 0.000228, 0.000003, 1.149963, 0.633886, 0.174504,
            0.140118, -0.001635, 0.009522, 0.223278, 0.054186, 0.271983, -0.093769, 0.281505, 0.05063, 0.15325, 0.089142, 0.038072,
            -0.146862, -0.043963, -0.112776, 0.148789, -0.038485, 1.411375, 4.343825, 4.280796, 3.129599, 2.721941, 2.929928, 2.844482,
            2.461795, 3.259899, 2.802381, 3.358946, 3.030141, 2.791503, 3.036005, 2.897101, 2.924398, 2.769962, 2.447959, 2.744954,
            2.970031, 3.261215, 2.90107, 13.275365, 30.683872, 0.109666, 0.136106, 0.230236, 0.293377, 0.388477, 0.379941, 0.347394,
            0.38237, 0.357848, 0.347323, 0.336042, 0.349153, 0.346295, 0.426454, 0.473917, 0.550035, 0.516055, 0.441869, 0.28826, 0.134921,
            0.081633, 0.001066, 0.001066, 0.006681, 0.012333, 0.007551, 0.110052, 0.147133, 0.076194, 0.116403, 0.102881, 0.070216,
            0.161051, 0.137583, 0.143189, 0.228379, 0.257903, 0.342719, 0.307655, 0.267676, 0.173148, 0.069437, 0.048159, 0.536979,
            0.391755, 0.548946, 0.611088, 0.719835, 0.658375, 0.653405, 0.656092, 0.705196, 0.57884, 0.590465, 0.615968, 0.573633, 0.701415,
            0.682925, 0.723159, 0.697713, 0.583431, 0.406544, 0.18773, 0.214058, 0.017532, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1 };
      assertThatFeatureVectorIsCorrect(dataset, firstInstanceFeatureVector, 0);

      double[] instanceFromMiddleFeatureVector = { 0.010102, 0.006309, 0.011397, 0.017973, 0.028205, 0.025117, 0.02353, 0.028197, 0.025515,
            0.026228, 0.030462, 0.035307, 0.043585, 0.068731, 0.121314, 0.191844, 0.229685, 0.122463, 0.073741, 0.053896, 0.033672,
            0.001066, 0.00029, 0.000069, 0.00021, 0.000308, 0.000407, 0.000327, 0.000332, 0.000343, 0.000258, 0.000223, 0.000263, 0.000419,
            0.000788, 0.000608, 0.003035, 0.010193, 0.018852, 0.00205, 0.000194, 0.000107, 0.000061, 0, 2.846154, 2.111309, 1.982035,
            1.210022, 0.864985, 0.969817, 1.042438, 0.89409, 1.144879, 0.665911, 1.115212, 2.632422, 5.802305, 0.925319, 2.142335, 1.965225,
            1.706673, 3.620542, 0.348293, 0.144886, 0.298332, 1, 12.704006, 7.651429, 7.038194, 3.88515, 3.612844, 3.846014, 3.910218,
            3.613784, 5.362208, 2.957366, 6.813429, 18.353415, 62.971821, 4.637315, 10.146846, 8.075125, 5.564707, 21.858134, 3.514219,
            2.854025, 2.971799, 1, 0.001358, 0.001681, 0.00527, 0.012491, 0.0243, 0.022039, 0.019695, 0.025391, 0.02327, 0.024064, 0.027984,
            0.032642, 0.040478, 0.066081, 0.107319, 0.158219, 0.174768, 0.115198, 0.073143, 0.053776, 0.033423, 0.001066, 0.001066,
            0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.003045, 0.003615, 0.02005,
            0.038267, 0.048002, 0.076465, 0.046994, 0.033217, 0.026093, 0.014949, 0.120206, 0.048206, 0.078702, 0.085106, 0.113163,
            0.103107, 0.103412, 0.099386, 0.107025, 0.073884, 0.139858, 0.215639, 0.378765, 0.17123, 0.493606, 0.704923, 0.879305, 0.445574,
            0.126279, 0.087008, 0.058155, 0.001066, 0, 0, 0.1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0.4, 0, 0, 0, 0, 0, 0, 0, 0.1, 0.1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.1, 0.1, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 46.1, 57.505652, 31, 20.92845,
            1490.9, 2352.434675, 1, 16 };
      assertThatFeatureVectorIsCorrect(dataset, instanceFromMiddleFeatureVector, BIRDS_DATASET_EXPECTED_NUMBER_OF_INSTANCES - 11);

      double[] lastInstanceFeatureVector = { 0.008697, 0.012031, 0.021212, 0.028663, 0.044081, 0.041791, 0.044002, 0.046693, 0.04022,
            0.041678, 0.045603, 0.047064, 0.05343, 0.081807, 0.105669, 0.146873, 0.159271, 0.131206, 0.088501, 0.064915, 0.041199, 0.001072,
            0.000256, 0.000224, 0.00052, 0.000614, 0.000777, 0.0008, 0.000687, 0.000542, 0.000434, 0.000414, 0.000361, 0.000355, 0.000395,
            0.000497, 0.000619, 0.000977, 0.000927, 0.000555, 0.000224, 0.000151, 0.00009, 0, 3.735429, 2.04813, 1.595559, 1.416315,
            0.860539, 0.856318, 0.66048, 0.365012, 0.513399, 0.563498, 0.31913, 0.234287, 0.532343, 0.337227, 0.033842, 0.265304, 0.300955,
            -0.011124, 0.184635, 0.284178, 0.296492, 11.898257, 21.017123, 7.787985, 5.937025, 5.871079, 3.949949, 3.383678, 3.006909,
            2.943034, 2.931245, 3.083017, 2.85099, 2.501573, 3.679805, 3.258939, 2.949693, 3.058207, 3.213689, 3.217774, 2.964758, 3.33072,
            3.581263, 158.185974, 0.001497, 0.006243, 0.013788, 0.022663, 0.040007, 0.036616, 0.039392, 0.044926, 0.039356, 0.039585,
            0.044631, 0.045937, 0.052023, 0.081043, 0.105159, 0.145114, 0.156651, 0.131122, 0.087561, 0.064635, 0.041226, 0.001066,
            0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.001066, 0.005296, 0.003717,
            0.008882, 0.027077, 0.032299, 0.075351, 0.08507, 0.056783, 0.043531, 0.032411, 0.014173, 0.141554, 0.091147, 0.13597, 0.172279,
            0.153097, 0.135521, 0.130105, 0.135805, 0.105846, 0.113747, 0.109456, 0.101221, 0.153537, 0.160877, 0.178788, 0.234544,
            0.263846, 0.207746, 0.133628, 0.117341, 0.08492, 0.001916, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17 };
      assertThatFeatureVectorIsCorrect(dataset, lastInstanceFeatureVector, BIRDS_DATASET_EXPECTED_NUMBER_OF_LAST_INSTANCE);
   }


   /**
    * Checks whether three samples of target vectors are parsed correctly.
    */
   private void assertThatTargetVectorSamplesAreCorrect() {
      double[] firstInstanceTargetVector = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
      assertThatTargetVectorIsCorrect(dataset, firstInstanceTargetVector, 0);

      double[] instanceFromMiddleTargetVector = { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
      assertThatTargetVectorIsCorrect(dataset, instanceFromMiddleTargetVector, BIRDS_DATASET_EXPECTED_NUMBER_OF_INSTANCES - 11);

      double[] lastInstanceTargetVector = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
      assertThatTargetVectorIsCorrect(dataset, lastInstanceTargetVector, BIRDS_DATASET_EXPECTED_NUMBER_OF_LAST_INSTANCE);
   }


   /**
    * Checks whether general statistics, such as the number of instances, are correct for the parsed
    * dataset.
    */
   private void assertThatDatasetStatisticsAreCorrect() {
      Assert.assertEquals(String.format(ERROR_INVALID_AMOUNT_OF_FEATURE_VECTORS, BIRDS_DATASET_EXPECTED_NUMBER_OF_INSTANCES,
            dataset.getFeatureVectors().size()), BIRDS_DATASET_EXPECTED_NUMBER_OF_INSTANCES, dataset.getFeatureVectors().size());
      Assert.assertEquals(
            String.format(ERROR_INVALID_AMOUNT_OF_INSTANCES, BIRDS_DATASET_EXPECTED_NUMBER_OF_INSTANCES, dataset.getNumberOfInstances()),
            BIRDS_DATASET_EXPECTED_NUMBER_OF_INSTANCES, dataset.getNumberOfInstances());
      Assert.assertEquals(String.format(ERROR_INVALID_AMOUNT_OF_TARGET_VECTORS, BIRDS_DATASET_EXPECTED_NUMBER_OF_INSTANCES,
            dataset.getCorrectResults().size()), BIRDS_DATASET_EXPECTED_NUMBER_OF_INSTANCES, dataset.getCorrectResults().size());
      Assert.assertEquals(
            String.format(ERROR_INVALID_AMOUNT_OF_LABELS, BIRDS_DATASET_EXPECTED_NUMBER_OF_LABELS, dataset.getNumberOfLabels()),
            BIRDS_DATASET_EXPECTED_NUMBER_OF_LABELS, dataset.getNumberOfLabels());
      Assert.assertEquals(String.format(ERROR_INVALID_AMOUNT_OF_CONTEXT_FEATURES, BIRDS_DATASET_EXPECTED_NUMBER_OF_FEATURES,
            dataset.getNumberOfFeatures()), BIRDS_DATASET_EXPECTED_NUMBER_OF_FEATURES, dataset.getNumberOfFeatures());
   }


   /**
    * Asserts that the feature vector in the given dataset at the given index is the given expected
    * one.
    * 
    * @param dataset the dataset the feature vector to check is from
    * @param expectedFeatureVector the expected feature vector
    * @param indexOfFeatureVectorToCheck the index of the feature vector to check
    */
   private void assertThatFeatureVectorIsCorrect(MultilabelClassificationDataset dataset, double[] expectedFeatureVector,
         int indexOfFeatureVectorToCheck) {
      double[] actualFeatureVector = dataset.getFeatureVectors().get(indexOfFeatureVectorToCheck);
      Assert.assertArrayEquals(
            String.format(ERROR_VECTOR_MISMATCH, Arrays.toString(expectedFeatureVector), Arrays.toString(actualFeatureVector)),
            expectedFeatureVector, actualFeatureVector, TestUtils.DOUBLE_DELTA);
   }


   /**
    * Asserts that the target vector in the given dataset at the given index is the given expected
    * one.
    * 
    * @param dataset the dataset the feature vector to check is from
    * @param expectedTargetVector the expected target vector
    * @param indexOfTargetVectorToCheck the index of the target vector to check
    */
   private void assertThatTargetVectorIsCorrect(MultilabelClassificationDataset dataset, double[] expectedTargetVector,
         int indexOfTargetVectorToCheck) {
      SparseDoubleVector targetVector = new SparseDoubleVector(expectedTargetVector);
      SparseDoubleVector actualTargetVector = dataset.getCorrectResults().get(indexOfTargetVectorToCheck);
      Assert.assertEquals(String.format(ERROR_VECTOR_MISMATCH, targetVector.toString(), actualTargetVector.toString()), targetVector,
            actualTargetVector);
   }


   @Override
   protected IDatasetParser getDatasetParser() {
      return new MultilabelClassificationDatasetParser();
   }

}
