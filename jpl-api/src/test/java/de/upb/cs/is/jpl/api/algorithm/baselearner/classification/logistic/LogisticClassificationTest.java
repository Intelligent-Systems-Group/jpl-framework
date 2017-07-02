package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.logistic;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.BaseLearnerJsonObjectCreator;
import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.AClassificationTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetParser;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Test class for {@link LogisticClassification}.
 * 
 * @author Tanja Tornede
 *
 */
public class LogisticClassificationTest extends AClassificationTest<double[], NullType, Double> {

   private static final String ERROR_REFLECTION_FAILED = "The reflection failed as of some reson.";

   private static final String REFLECTION_LOGISTIC_CLASSIFICATION_CONFIGURATION_ERROR_THRESHOLD_INVALID = "ERROR_THRESHOLD_INVALID";
   private static final String REFLECTION_LOGISTIC_CLASSIFICATION_CONFIGURATION_ERROR_WRONG_IDENTIFIER_FOR_BASE_LEARNER = "ERROR_WRONG_IDENTIFIER_FOR_BASE_LEARNER";

   private static final String RESOURCE_DIRECTORY_LEVEL = "logistic" + File.separator;
   protected static final String WRONG_DATASET_NAME = "wrongdataset.gprf";

   private static final String CONFIGURATION_PARAMETER_LEARNING_RATE = "learning_rate";
   private static final String CONFIGURATION_PARAMETER_LEARNING_RATE_VALUE = "0.2";

   private static final String CONFIGURATION_PARAMETER_THRESHOLD = "threshold";
   private static final double CONFIGURATION_PARAMETER_THRESHOLD_VALUE = 0.5;

   private BaselearnerDataset diabetes;
   private BaselearnerDataset iososphere;


   /**
    * Creates a test for {@link LogisticClassification}.
    */
   public LogisticClassificationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public void setupTest() {
      super.setupTest();
      RandomGenerator.initializeRNG(1234);

      diabetes = createDiabetesBaselearnerDataset();
      diabetes.shuffle();

      iososphere = createIonosphereBaselearnerDataset();
      iososphere.shuffle();
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new LogisticClassification(CONFIGURATION_PARAMETER_THRESHOLD_VALUE);
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> datasetList = new ArrayList<>();
      datasetList.add(this.createDatasetOutOfFile(new DefaultAbsoluteDatasetParser(), getTestRessourcePathFor(WRONG_DATASET_NAME)));
      return datasetList;
   }


   @Override
   public List<IDataset<double[], NullType, Double>> getCorrectDatasetList() {
      List<IDataset<double[], NullType, Double>> datasetList = new ArrayList<>();
      datasetList.add(diabetes.getPartOfDataset(0, 40));
      datasetList.add(iososphere.getPartOfDataset(0, 40));
      return datasetList;
   }


   @Override
   public List<Pair<IDataset<double[], NullType, Double>, List<Double>>> getPredictionsForDatasetList() {
      BaselearnerDataset[] baselearnerDatasets = { (BaselearnerDataset) diabetes.getPartOfDataset(45, 49),
            (BaselearnerDataset) iososphere.getPartOfDataset(45, 49) };

      List<Pair<IDataset<double[], NullType, Double>, List<Double>>> correctDatasetList = new ArrayList<>();
      for (BaselearnerDataset dataset : baselearnerDatasets) {
         List<Double> correctPredictions = Arrays.asList(ArrayUtils.toObject(dataset.getCorrectResults()));
         Arrays.fill(dataset.getCorrectResults(), 0);
         correctDatasetList.add(Pair.of(dataset, correctPredictions));
      }
      return correctDatasetList;
   }


   /**
    * Creates a baselearner dataset out of the ionosphere dataset, containing the first 100
    * instances.
    * 
    * @return a baselearner datast out of the ionosphere dataset
    */
   private BaselearnerDataset createIonosphereBaselearnerDataset() {
      double[][] featureVector = {
            { 1.0, 0.0, 0.74468, 0.10638, 0.88706, 0.00982, 0.88542, 0.01471, 0.87234, -0.01418, 0.7305, 0.10638, 0.87657, 0.02912, 0.87235,
                  0.03382, 0.95745, 0.07801, 0.95035, 0.04255, 0.85597, 0.04743, 0.84931, 0.05178, 0.87234, 0.11348, 0.83429, 0.06014,
                  0.74468, -0.03546, 0.8171, 0.068, 0.80774, 0.07173 },
            { 1.0, 0.0, 0.92657, 0.04174, 0.89266, 0.15766, 0.86098, 0.19791, 0.83675, 0.36526, 0.80619, 0.40198, 0.76221, 0.40552, 0.66586,
                  0.4836, 0.60101, 0.51752, 0.53392, 0.5218, 0.48435, 0.54212, 0.42546, 0.55684, 0.3334, 0.55274, 0.26978, 0.54214, 0.22307,
                  0.53448, 0.14312, 0.49124, 0.11573, 0.46571 },
            { 1.0, 0.0, 0.7381, 0.83333, -0.7619, -0.2381, 0.33333, -0.14286, 0.45238, -0.14286, -0.67285, 0.12808, 0.33333, 0.0, 0.28571,
                  -0.07143, -0.38214, 0.51163, 0.2381, 0.02381, 0.45238, 0.04762, 0.16667, -0.2619, -0.57255, -0.10234, 0.24889, -0.51079,
                  1.0, 0.0, -0.66667, -0.04762, 0.2619, 0.02381 },
            { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                  0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
            { 1.0, 0.0, 1.0, 0.09802, 1.0, 0.25101, 0.9839, 0.33044, 0.80365, 0.5302, 0.74977, 0.60297, 0.56937, 0.71942, 0.55311, 0.74079,
                  0.29452, 0.82193, 0.21137, 0.79777, 0.09709, 0.82162, -0.01734, 0.7987, -0.15144, 0.75596, -0.22839, 0.69187, -0.31713,
                  0.60948, -0.40291, 0.54522, -0.42815, 0.44534 },
            { 1.0, 0.0, 0.39179, -0.06343, 0.97464, 0.04328, 1.0, 1.0, 0.35821, 0.15299, 0.54478, 0.1306, 0.61567, -0.8209, 0.57836, 0.6791,
                  0.66791, -0.10448, 0.46642, -0.11567, 0.65574, 0.14792, 0.83209, 0.45522, 0.47015, 0.16418, 0.49309, 0.1463, 0.32463,
                  -0.02612, 0.39118, 0.13521, 0.34411, 0.12755 },
            { 0.0, 0.0, 0.0, 0.0, -1.0, -1.0, 1.0, 1.0, -1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 1.0, 1.0, -1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0,
                  1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0 },
            { 1.0, 0.0, 1.0, 0.06655, 1.0, -0.18388, 1.0, -0.2732, 1.0, -0.43107, 1.0, -0.41349, 0.96232, -0.51874, 0.90711, -0.59017,
                  0.8923, -0.66474, 0.69876, -0.70997, 0.70645, -0.7632, 0.63081, -0.80544, 0.55867, -0.89128, 0.47211, -0.865, 0.40303,
                  -0.83675, 0.30996, -0.89093, 0.22995, -0.89158 },
            { 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, -1.0, -1.0, -1.0, -1.0, -1.0, 1.0, -1.0, 1.0, 0.0,
                  0.0, 0.0, 0.0, 1.0, -1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0 },
            { 1.0, 0.0, 1.0, -0.02259, 1.0, -0.04494, 1.0, -0.06682, 1.0, -0.08799, 1.0, 0.56173, 1.0, -0.12738, 1.0, -0.14522, 1.0,
                  0.32407, 1.0, -0.17639, 0.99484, -0.18949, 0.95601, -0.20081, 1.0, -0.92284, 0.8728, -0.21793, 0.8292, -0.2237, 0.78479,
                  -0.22765, 0.73992, -0.22981 },
            { 1.0, 0.0, 0.97467, 0.13082, 0.9412, 0.20036, 0.88783, 0.32248, 0.89009, 0.32711, 0.8555, 0.45217, 0.72298, 0.52284, 0.69946,
                  0.5882, 0.58548, 0.66893, 0.48869, 0.70398, 0.44245, 0.68159, 0.35289, 0.75622, 0.26832, 0.7621, 0.16813, 0.78541,
                  0.07497, 0.80439, -0.02962, 0.77702, -0.10289, 0.74242 },
            { 1.0, 0.0, 0.84713, -0.03397, 0.86412, -0.08493, 0.81953, 0.0, 0.73673, -0.07643, 0.71975, -0.13588, 0.74947, -0.11677,
                  0.77495, -0.18684, 0.78132, -0.21231, 0.61996, -0.10191, 0.79193, -0.15711, 0.89384, -0.03397, 0.84926, -0.26115, 0.74115,
                  -0.23312, 0.66242, -0.22293, 0.72611, -0.37792, 0.65817, -0.24841 },
            { 1.0, 0.0, 0.45455, 0.09091, 0.63636, 0.09091, 0.27273, 0.18182, 0.63636, 0.0, 0.36364, -0.09091, 0.45455, -0.09091, 0.48612,
                  -0.01343, 0.63636, -0.18182, 0.45455, 0.0, 0.36364, -0.09091, 0.27273, 0.18182, 0.36364, -0.09091, 0.34442, -0.01768,
                  0.27273, 0.0, 0.36364, 0.0, 0.28985, -0.01832 },
            { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                  0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
            { 1.0, 0.0, 0.41932, 0.12482, 0.35, 0.125, 0.23182, 0.27955, -0.03636, 0.44318, 0.04517, 0.36194, -0.19091, 0.33636, -0.1335,
                  0.27322, 0.02727, 0.40455, -0.34773, 0.12727, -0.20028, 0.05078, -0.18636, 0.36364, -0.14003, -0.04802, -0.09971,
                  -0.07114, -1.0, -1.0, -0.02916, -0.07464, -0.00526, -0.06314 },
            { 1.0, 0.0, 0.74084, 0.04974, 0.79074, 0.02543, 0.78575, 0.03793, 0.6623, 0.09948, 0.67801, 0.31152, 0.75934, 0.07348, 0.74695,
                  0.08442, 0.70681, -0.07853, 0.63613, 0.0, 0.70021, 0.11355, 0.68183, 0.12185, 0.67016, 0.15445, 0.64158, 0.13608, 0.65707,
                  0.17539, 0.59759, 0.14697, 0.57455, 0.15114 },
            { 1.0, 0.0, 0.57647, -0.01569, 0.40392, 0.0, 0.38431, 0.12941, 0.4, -0.05882, 0.56471, 0.14118, 0.46667, 0.08235, 0.52549,
                  -0.0549, 0.58039, 0.01569, 0.50196, 0.0, 0.45882, 0.06667, 0.58039, 0.08235, 0.49804, 0.00392, 0.48601, 0.10039, 0.46275,
                  0.08235, 0.45098, 0.23529, 0.43137, 0.17255 },
            { 1.0, 0.0, 0.91767, 0.18198, 0.8609, 0.35543, 0.72873, 0.45747, 0.60425, 0.69865, 0.50376, 0.74922, 0.361, 0.81795, 0.15664,
                  0.83558, 0.00396, 0.8521, -0.1639, 0.77853, -0.35996, 0.76193, -0.43087, 0.65385, -0.5314, 0.53886, -0.60328, 0.40972,
                  -0.64511, 0.27338, -0.6571, 0.13667, -0.64056, 0.05394 },
            { 1.0, 0.0, 0.93147, 0.29282, 0.79917, 0.55756, 0.59952, 0.71596, 0.26203, 0.92651, 0.04636, 0.96748, -0.23237, 0.9513,
                  -0.55926, 0.81018, -0.73329, 0.62385, -0.90995, 0.362, -0.92254, 0.0604, -0.93618, -0.19838, -0.83192, -0.46906, -0.65165,
                  -0.69556, -0.41223, -0.85725, -0.1359, -0.93953, 0.10007, -0.94823 },
            { 1.0, 0.0, 0.68729, 1.0, 0.91973, -0.76087, 0.81773, 0.04348, 0.76087, 0.10702, 0.86789, 0.73746, 0.70067, 0.18227, 0.7592,
                  0.13712, 0.93478, -0.25084, 0.70736, 0.18729, 0.64883, 0.24582, 0.60201, 0.77425, 1.0, -0.53846, 0.89262, 0.22216, 0.7107,
                  0.53846, 1.0, -0.06522, 0.56522, 0.23913 },
            { 1.0, 0.0, 0.98822, 0.02187, 0.93102, 0.341, 0.83904, 0.35222, 0.74706, 0.48906, 0.73584, 0.51879, 0.55076, 0.60179, 0.4313,
                  0.66237, 0.318, 0.70443, 0.28379, 0.68873, 0.07515, 0.73696, 0.06338, 0.71284, -0.16489, 0.69714, -0.16556, 0.6051,
                  -0.16209, 0.55805, -0.34717, 0.44195, -0.33483, 0.37465 },
            { 1.0, 0.0, 0.89563, 0.37917, 0.67311, 0.69438, 0.35916, 0.88696, -0.04193, 0.93345, -0.38875, 0.84414, -0.67274, 0.62078,
                  -0.8268, 0.30356, -0.8615, -0.05365, -0.73564, -0.34275, -0.51778, -0.62443, -0.23428, -0.73855, 0.06911, -0.73856,
                  0.33531, -0.62296, 0.52414, -0.42086, 0.61217, -0.17343, 0.60073, 0.0866 },
            { 1.0, 0.0, 1.0, 0.5, 1.0, 0.25, 0.25, 1.0, 0.16851, 0.9118, -0.13336, 0.80454, -0.34107, 0.60793, -0.4382, 0.37856, -0.43663,
                  0.16709, -0.36676, 0.00678, -0.26477, -0.09025, -0.16178, -0.12964, -0.07782, -0.12744, -0.02089, -0.10242, 0.01033,
                  -0.07036, 0.02224, -0.04142, 0.02249, -0.02017 },
            { 1.0, 0.0, -0.5418, 0.14861, -0.33746, 0.73375, 0.52012, -0.13932, 0.31889, -0.06811, 0.20743, -0.1517, 0.47368, 0.08978,
                  0.56347, -0.1548, 0.16409, 0.45201, 0.33746, 0.03406, 0.50464, 0.07121, -0.63777, -0.6161, 1.0, 0.65635, 0.41348,
                  -0.40116, -0.1517, 0.11146, 0.02399, 0.5582, 0.52632, -0.08978 },
            { 1.0, 0.0, 0.34694, 0.20408, 0.46939, 0.2449, 0.40816, 0.20408, 0.46939, 0.44898, 0.30612, 0.59184, 0.12245, 0.55102, 0.0,
                  0.5102, -0.06122, 0.55102, -0.20408, 0.55102, -0.28571, 0.44898, -0.28571, 0.32653, -0.61224, 0.22449, -0.46579, 0.14895,
                  -0.59184, 0.18367, -0.34694, 0.0, -0.26531, -0.2449 },
            { 1.0, 0.0, 1.0, 0.24168, 1.0, 0.4859, 1.0, 0.72973, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.77128, 1.0, 1.0, 1.0, 1.0, 0.74468,
                  1.0, 0.89647, 1.0, 0.64628, 1.0, 0.38255, 1.0, 0.10819, 1.0, -0.1737, 1.0, -0.81383, 1.0 },
            { 1.0, 0.0, 0.32834, 0.0252, 0.15236, 0.21278, 0.14919, 0.74003, -0.25706, 0.92324, -0.10312, 0.1938, -0.61352, 0.25786,
                  -0.94053, -0.05409, -0.13117, -0.14329, -0.30315, -0.44615, -0.11409, -0.85597, 0.02668, -0.22786, 0.27942, -0.06295,
                  0.33737, -0.11876, 0.27657, -0.11409, 0.15078, 0.13296, 0.12197, 0.20468 },
            { 1.0, 0.0, 0.91176, -0.08824, 0.97059, 0.17647, 0.82353, 0.08824, 0.91176, -0.02941, 0.97059, -0.17647, 0.97059, 0.14706,
                  0.94118, 0.02941, 1.0, 0.0, 1.0, 0.0, 0.76471, 0.11765, 0.88235, 0.02941, 0.85294, 0.02941, 0.92663, 0.026, 0.94118,
                  -0.11765, 0.97059, 0.05882, 0.91176, 0.05882 },
            { 1.0, 0.0, 0.74449, -0.0239, 0.70772, 0.03309, 0.72243, 0.16912, 0.79228, 0.07721, 0.81434, 0.43934, 0.63787, 0.00551, 0.70772,
                  0.21691, 1.0, 0.06066, 0.61029, 0.05147, 0.67463, 0.04228, 0.52022, -0.25, 0.72978, -0.15809, 0.61727, 0.07124, 0.30882,
                  0.0864, 0.55916, 0.07458, 0.60294, 0.21691 },
            { 1.0, 0.0, 1.0, 0.0738, 1.0, 0.0342, 1.0, -0.05563, 1.0, 0.08764, 1.0, 0.19651, 1.0, 0.20328, 1.0, 0.12785, 1.0, 0.10561, 1.0,
                  0.27087, 1.0, 0.44758, 1.0, 0.4175, 1.0, 0.20033, 1.0, 0.36743, 0.95603, 0.48641, 1.0, 0.32492, 1.0, 0.46712 },
            { 1.0, 0.0, 0.47337, 0.19527, 0.06213, -0.18343, 0.62316, 0.01006, 0.45562, -0.04438, 0.56509, 0.01775, 0.44675, 0.27515,
                  0.71598, -0.03846, 0.55621, 0.12426, 0.4142, 0.11538, 0.52767, 0.02842, 0.51183, -0.10651, 0.47929, -0.02367, 0.46514,
                  0.03259, 0.5355, 0.25148, 0.31953, -0.14497, 0.34615, -0.00296 },
            { 1.0, 0.0, 0.96355, -0.07198, 1.0, -0.14333, 1.0, -0.21313, 1.0, -0.36174, 0.9257, -0.43569, 0.9451, -0.40668, 0.90392,
                  -0.46381, 0.98305, -0.35257, 0.84537, -0.6602, 0.75346, -0.60589, 0.69637, -0.64225, 0.85106, -0.6544, 0.57577, -0.69712,
                  0.25435, -0.63919, 0.45114, -0.72779, 0.38895, -0.7342 },
            { 1.0, 0.0, 0.63816, 1.0, 0.20833, -1.0, 1.0, 1.0, 0.87719, 0.30921, -0.66886, 1.0, -0.05921, 0.58772, 0.01754, 0.05044,
                  -0.51535, -1.0, 0.14254, -0.03289, 0.32675, -0.4386, -1.0, 1.0, 0.80921, -1.0, 1.0, -0.0614, 1.0, 1.0, 0.20614, -1.0, 1.0,
                  1.0 },
            { 1.0, 0.0, 0.50466, -0.169, 0.71442, 0.01513, 0.71063, 0.02258, 0.68065, 0.01282, 0.34615, 0.05594, 0.6905, 0.04393, 0.68101,
                  0.05058, 0.67023, 0.05692, 0.63403, -0.04662, 0.64503, 0.06856, 0.63077, 0.07381, 0.84033, 0.18065, 0.59935, 0.08304,
                  0.38228, 0.0676, 0.56466, 0.09046, 0.54632, 0.09346 },
            { 0.0, 0.0, 1.0, 1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, 0.0, 0.0, 1.0, -1.0,
                  1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0 },
            { 1.0, 0.0, 0.88208, -0.14639, 0.93408, -0.11057, 0.921, -0.1645, 0.88307, -0.17036, 0.88462, -0.31809, 0.85269, -0.31463,
                  0.82116, -0.35924, 0.80681, -0.33632, 0.75243, -0.47022, 0.70555, -0.47153, 0.6615, -0.50085, 0.61297, -0.48086, 0.56804,
                  -0.54629, 0.50179, -0.59854, 0.47075, -0.57377, 0.42189, -0.58086 },
            { 1.0, 0.0, 0.94052, -0.01531, 0.9417, 0.01001, 0.94994, -0.01472, 0.95878, -0.0106, 0.94641, -0.0371, 0.97173, -0.01767,
                  0.97055, -0.03887, 0.95465, -0.04064, 0.9523, -0.04711, 0.94229, -0.02179, 0.92815, -0.04417, 0.92049, -0.04476, 0.92695,
                  -0.05827, 0.90342, -0.07479, 0.91991, -0.07244, 0.92049, -0.0742 },
            { 1.0, 0.0, 0.19466, 0.05725, 0.04198, 0.25191, -0.10557, 0.48866, -0.18321, -0.18321, -0.41985, 0.06107, -0.4542, 0.0916,
                  -0.16412, -0.30534, -0.10305, -0.39695, 0.18702, -0.17557, 0.34012, -0.11953, 0.28626, -0.16031, 0.21645, 0.24692,
                  0.03913, 0.31092, -0.03817, 0.26336, -0.16794, 0.16794, -0.30153, -0.33588 },
            { 1.0, 0.0, 1.0, 0.14286, 1.0, 0.71429, 1.0, 0.71429, 1.0, -0.14286, 0.85714, -0.14286, 1.0, 0.02534, 1.0, 0.0, 0.42857,
                  -0.14286, 1.0, 0.03617, 1.0, -0.28571, 1.0, 0.0, 0.28571, -0.28571, 1.0, 0.04891, 1.0, 0.05182, 1.0, 0.57143, 1.0, 0.0 },
            { 1.0, 0.0, 0.28409, -0.31818, 0.0, 0.0, 0.68182, -1.0, 0.30682, 0.95833, 0.64394, 0.06439, 0.34848, -0.84848, 0.0, 0.0,
                  0.59091, -0.35985, 0.45076, -0.80682, 0.0, 0.0, 0.0, 0.0, 0.24242, 0.17803, 1.0, -0.23864, 0.06061, -0.48485, 0.16288,
                  -0.70076, 0.0, 0.0 },
            { 1.0, 0.0, 0.97513, 0.0071, 0.98579, 0.01954, 1.0, 0.01954, 0.9929, 0.01599, 0.95737, 0.02309, 0.97158, 0.03552, 1.0, 0.0373,
                  0.97869, 0.02131, 0.98579, 0.05684, 0.97158, 0.04796, 0.94494, 0.05506, 0.98401, 0.03552, 0.9754, 0.06477, 0.94849,
                  0.08171, 0.99112, 0.06217, 0.98934, 0.09947 },
            { 0.0, 0.0, 1.0, -1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0,
                  1.0, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0 },
            { 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.47744, -0.89098, -0.51504, 0.45489, -0.95489, 0.28571, 0.64662, 1.0, 0.0,
                  0.0, 0.0, 0.0, 0.6203, 0.20301, -1.0, -1.0, 1.0, -1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0 },
            { 1.0, 0.0, 0.69444, 0.38889, 0.0, 0.0, -0.32937, 0.69841, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.20635, -0.24206, 0.21032, 0.19444,
                  0.46429, 0.78175, 0.0, 0.0, 0.0, 0.0, 0.73413, 0.27381, 0.7619, 0.63492, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
            { 1.0, 0.0, 0.32789, 0.11042, 0.1597, 0.29308, 0.1402, 0.74485, -0.25131, 0.91993, -0.16503, 0.26664, -0.63714, 0.24865,
                  -0.9765, -0.00337, -0.23227, -0.19909, -0.30522, -0.48886, -0.14426, -0.89991, 0.09345, -0.28916, 0.28307, -0.1856,
                  0.39599, -0.11498, 0.31005, 0.05614, 0.21443, 0.2054, 0.13376, 0.26422 },
            { 1.0, 0.0, 0.71521, -0.00647, 0.66667, -0.04207, 0.63107, -0.05178, 0.77994, 0.08091, 0.67314, 0.09709, 0.64725, 0.15858,
                  0.60194, -0.01942, 0.54369, -0.04531, 0.46926, -0.10032, 0.64725, 0.14887, 0.39159, 0.21683, 0.52427, -0.05502, 0.45105,
                  4.0E-4, 0.31392, -0.06796, 0.49191, -0.1068, 0.30421, -0.05178 },
            { 1.0, 0.0, 0.85736, 7.5E-4, 0.81927, -0.05676, 0.77521, -0.04182, 0.84317, 0.09037, 0.86258, 0.11949, 0.88051, -0.06124,
                  0.78342, 0.0351, 0.83719, -0.06796, 0.8357, -0.1419, 0.88125, 0.01195, 0.90515, 0.0224, 0.79686, -0.01942, 0.82383,
                  -0.03678, 0.88125, -0.06423, 0.73936, -0.01942, 0.79089, -0.09186 },
            { 1.0, 0.0, -1.0, -1.0, 1.0, 1.0, 1.0, -0.14375, 0.0, 0.0, -1.0, 1.0, 1.0, 1.0, 0.17917, -1.0, -1.0, -1.0, 0.0875, -1.0, 1.0,
                  -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, -1.0, 1.0, 1.0, 0.0, 0.0 },
            { 1.0, 0.0, 0.29073, 0.10025, 0.23308, 0.17293, 0.03759, 0.34336, 0.1203, 0.26316, 0.06266, 0.21303, -0.04725, 0.12767,
                  -0.06333, 0.07907, -0.06328, 0.04097, -0.05431, 0.01408, -0.04166, -0.0028, -0.02876, -0.01176, -0.01755, -0.01505,
                  -0.00886, -0.01475, -0.0028, -0.0125, 9.6E-4, -0.00948, 0.0029, -0.00647 },
            { 1.0, 0.0, -1.0, 1.0, -1.0, 0.15244, 0.28354, 1.0, -1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -0.23476, 0.28301, -1.0, 1.0, 1.0,
                  -0.31402, -1.0, -1.0, -1.0, 1.0, -1.0, -1.0, -0.03578, 1.0, -1.0, -1.0, -0.32317, 0.14939, 1.0 } };

      double[] ratings = { 1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0,
            1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, -1.0, -1.0, -1.0, 1.0,
            1.0, 1.0, -1.0, 1.0, -1.0 };

      return createBaselearnerDataset(featureVector, ratings);
   }


   /**
    * Creates a baselearner dataset out of the diabetes dataset, containing the first 100 instances.
    * 
    * @return a baselearner datast out of the diabetes dataset
    */
   private BaselearnerDataset createDiabetesBaselearnerDataset() {
      double[][] featureVectors = { { 4.0, 129.0, 60.0, 12.0, 231.0, 27.5, 0.527, 31.0 }, { 5.0, 110.0, 68.0, 0.0, 0.0, 26.0, 0.292, 30.0 },
            { 11.0, 111.0, 84.0, 40.0, 0.0, 46.8, 0.925, 45.0 }, { 8.0, 118.0, 72.0, 19.0, 0.0, 23.1, 1.476, 46.0 },
            { 2.0, 99.0, 52.0, 15.0, 94.0, 24.6, 0.637, 21.0 }, { 0.0, 161.0, 50.0, 0.0, 0.0, 21.9, 0.254, 65.0 },
            { 6.0, 134.0, 70.0, 23.0, 130.0, 35.4, 0.542, 29.0 }, { 0.0, 102.0, 86.0, 17.0, 105.0, 29.3, 0.695, 27.0 },
            { 13.0, 158.0, 114.0, 0.0, 0.0, 42.3, 0.257, 44.0 }, { 0.0, 93.0, 60.0, 0.0, 0.0, 35.3, 0.263, 25.0 },
            { 2.0, 100.0, 68.0, 25.0, 71.0, 38.5, 0.324, 26.0 }, { 2.0, 155.0, 74.0, 17.0, 96.0, 26.6, 0.433, 27.0 },
            { 10.0, 161.0, 68.0, 23.0, 132.0, 25.5, 0.326, 47.0 }, { 1.0, 106.0, 70.0, 28.0, 135.0, 34.2, 0.142, 22.0 },
            { 6.0, 109.0, 60.0, 27.0, 0.0, 25.0, 0.206, 27.0 }, { 7.0, 124.0, 70.0, 33.0, 215.0, 25.5, 0.161, 37.0 },
            { 13.0, 152.0, 90.0, 33.0, 29.0, 26.8, 0.731, 43.0 }, { 4.0, 148.0, 60.0, 27.0, 318.0, 30.9, 0.15, 29.0 },
            { 0.0, 121.0, 66.0, 30.0, 165.0, 34.3, 0.203, 33.0 }, { 1.0, 189.0, 60.0, 23.0, 846.0, 30.1, 0.398, 59.0 },
            { 11.0, 136.0, 84.0, 35.0, 130.0, 28.3, 0.26, 42.0 }, { 0.0, 180.0, 66.0, 39.0, 0.0, 42.0, 1.893, 25.0 },
            { 4.0, 128.0, 70.0, 0.0, 0.0, 34.3, 0.303, 24.0 }, { 2.0, 105.0, 58.0, 40.0, 94.0, 34.9, 0.225, 25.0 },
            { 4.0, 95.0, 60.0, 32.0, 0.0, 35.4, 0.284, 28.0 }, { 5.0, 73.0, 60.0, 0.0, 0.0, 26.8, 0.268, 27.0 },
            { 9.0, 57.0, 80.0, 37.0, 0.0, 32.8, 0.096, 41.0 }, { 2.0, 71.0, 70.0, 27.0, 0.0, 28.0, 0.586, 22.0 },
            { 2.0, 142.0, 82.0, 18.0, 64.0, 24.7, 0.761, 21.0 }, { 0.0, 117.0, 80.0, 31.0, 53.0, 45.2, 0.089, 24.0 },
            { 3.0, 173.0, 82.0, 48.0, 465.0, 38.4, 2.137, 25.0 }, { 3.0, 111.0, 62.0, 0.0, 0.0, 22.6, 0.142, 21.0 },
            { 1.0, 131.0, 64.0, 14.0, 415.0, 23.7, 0.389, 21.0 }, { 1.0, 128.0, 98.0, 41.0, 58.0, 32.0, 1.321, 33.0 },
            { 3.0, 83.0, 58.0, 31.0, 18.0, 34.3, 0.336, 25.0 }, { 2.0, 119.0, 0.0, 0.0, 0.0, 19.6, 0.832, 72.0 },
            { 0.0, 91.0, 68.0, 32.0, 210.0, 39.9, 0.381, 25.0 }, { 11.0, 143.0, 94.0, 33.0, 146.0, 36.6, 0.254, 51.0 },
            { 2.0, 109.0, 92.0, 0.0, 0.0, 42.7, 0.845, 54.0 }, { 7.0, 81.0, 78.0, 40.0, 48.0, 46.7, 0.261, 42.0 },
            { 7.0, 102.0, 74.0, 40.0, 105.0, 37.2, 0.204, 45.0 }, { 2.0, 127.0, 58.0, 24.0, 275.0, 27.7, 1.6, 25.0 },
            { 1.0, 173.0, 74.0, 0.0, 0.0, 36.8, 0.088, 38.0 }, { 10.0, 129.0, 62.0, 36.0, 0.0, 41.2, 0.441, 38.0 },
            { 8.0, 85.0, 55.0, 20.0, 0.0, 24.4, 0.136, 42.0 }, { 6.0, 137.0, 61.0, 0.0, 0.0, 24.2, 0.151, 55.0 },
            { 0.0, 100.0, 70.0, 26.0, 50.0, 30.8, 0.597, 21.0 }, { 11.0, 155.0, 76.0, 28.0, 150.0, 33.3, 1.353, 51.0 },
            { 6.0, 107.0, 88.0, 0.0, 0.0, 36.8, 0.727, 31.0 }, { 2.0, 108.0, 62.0, 10.0, 278.0, 25.3, 0.881, 22.0 } };

      double[] ratings = { -1.0, -1.0, 1.0, -1.0, -1.0, -1.0, 1.0, -1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0,
            1.0, 1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, 1.0, -1.0, -1.0, 1.0, -1.0, -1.0, -1.0, 1.0, -1.0, -1.0, -1.0, -1.0,
            1.0, 1.0, -1.0, -1.0, -1.0, 1.0, -1.0, -1.0 };

      return createBaselearnerDataset(featureVectors, ratings);
   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      String[] parameterNames = { CONFIGURATION_PARAMETER_THRESHOLD };
      double[] parameterValues = { 0.2 };

      List<JsonObject> parameterList = new ArrayList<>();
      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i])));
      }

      JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(new BaseLearnerJsonObjectCreator(
            EBaseLearner.LOGISTIC_REGRESSION.getBaseLearnerIdentifier(),
            JsonUtils.createJsonObjectFromKeyAndValue(CONFIGURATION_PARAMETER_LEARNING_RATE, CONFIGURATION_PARAMETER_LEARNING_RATE_VALUE)));
      parameterList.add(baselearnerDefinition);

      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      List<Pair<String, JsonObject>> parameterList = new ArrayList<>();

      String[] parameterNames = { CONFIGURATION_PARAMETER_THRESHOLD, CONFIGURATION_PARAMETER_THRESHOLD };
      double[] parameterValues = { 0, 1 };
      String[] parameterExceptionMessages = {
            TestUtils.getStringByReflectionSafely(LogisticClassificationConfiguration.class,
                  REFLECTION_LOGISTIC_CLASSIFICATION_CONFIGURATION_ERROR_THRESHOLD_INVALID),
            TestUtils.getStringByReflectionSafely(LogisticClassificationConfiguration.class,
                  REFLECTION_LOGISTIC_CLASSIFICATION_CONFIGURATION_ERROR_THRESHOLD_INVALID) };

      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(Pair.of(parameterExceptionMessages[i],
               JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i]))));
      }

      try {
         String exceptionString = TestUtils.getStringByReflection(LogisticClassificationConfiguration.class,
               REFLECTION_LOGISTIC_CLASSIFICATION_CONFIGURATION_ERROR_WRONG_IDENTIFIER_FOR_BASE_LEARNER);

         EBaseLearner[] invalidBaselearners = { EBaseLearner.PERCEPTRON, EBaseLearner.POCKET, EBaseLearner.MINI_BATCH_PEGASOS,
               EBaseLearner.SVM_CLASSIFICATION, EBaseLearner.KNEAREST_NEIGHBOUR, EBaseLearner.LINEAR_REGRESSION };

         for (EBaseLearner invalidBaselearner : invalidBaselearners) {
            JsonObject baselearnerDefinition = BaseLearnerJsonObjectCreator.getBaseLearnerJsonObject(
                  new BaseLearnerJsonObjectCreator(invalidBaselearner.getBaseLearnerIdentifier(), new JsonObject()));
            parameterList.add(Pair.of(String.format(exceptionString, invalidBaselearner.name()), baselearnerDefinition));
         }

      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         Assert.fail(ERROR_REFLECTION_FAILED);
      }

      return parameterList;
   }

}