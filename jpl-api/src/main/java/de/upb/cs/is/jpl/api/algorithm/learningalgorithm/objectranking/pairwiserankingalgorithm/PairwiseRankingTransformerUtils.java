package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.pairwiserankingalgorithm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingInstance;
import de.upb.cs.is.jpl.api.exception.dataset.WrongDatasetInputException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.PermuatorCombinator;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class contains implementations to convert an instance or the object ranking dataset to the
 * base learner dataset for classification. So that we can use different
 * {@link ABaselearnerAlgorithm} which are implemented to solve the classifier problem on it and use
 * it to get the scoring function.
 * 
 * @author Pritha Gupta
 *
 */
public class PairwiseRankingTransformerUtils {

   private static final String CANNOT_COMBINE_THE_GIVEN_BASE_LEARNER_ERROR_MESSAGE = "Cannot combine the given base learner dataset as they have different number of features";


   /**
    * Hides the public constructor.
    */
   private PairwiseRankingTransformerUtils() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * The method to transform the provided {@link ObjectRankingInstance} to classifier
    * {@link BaselearnerDataset}. This function takes one ranking and generate all possible
    * combinations of pair wise preferences. Suppose if x1 is ranked before x2 then it creates an
    * instance with weights (features(x1-x2)) and class as 1. or an instance with weights
    * (features(x2-x1)) and class -1. The output dataset contains balanced classes, i.e. there are
    * the same number of -1 as +1
    * 
    * @param objectRankingInstance the dataset to be transformed
    * @return the transformed base learner dataset
    */
   public static BaselearnerDataset transformInstanceToPairswisePreferencesClassifcationDataset(
         ObjectRankingInstance objectRankingInstance) {
      int[] objects = objectRankingInstance.getRating().getObjectList();
      List<Integer> listOfIndexes = CollectionsUtils.createListOfIndexesForProvidedNumber(objects.length);
      List<Integer[]> combination = PermuatorCombinator.getCombinationsnCr(listOfIndexes, 2);
      int numberOfItemFeatures = objectRankingInstance.getNumofItemFeatures();
      int numberOfContextFeatures = objectRankingInstance.getNumberOfContextsFeatures();
      BaselearnerDataset transformedDataset = new BaselearnerDataset(combination.size(), numberOfItemFeatures + numberOfContextFeatures);
      int k = 1;
      double[] contextFeatures = objectRankingInstance.getContextFeatureVector();
      for (Integer[] obj : combination) {

         double[] itemFeaturesForObjectOne = objectRankingInstance.getFeaturesForItem(objects[obj[0]]);
         double[] itemFeaturesForObjectTwo = objectRankingInstance.getFeaturesForItem(objects[obj[1]]);
         DenseDoubleVector featureVector = new DenseDoubleVector(itemFeaturesForObjectOne);
         featureVector.subtractVector(itemFeaturesForObjectTwo);
         double classValue = Math.signum(obj[1] - (double) obj[0]);
         if (Double.compare(classValue, Math.pow(-1, k)) != 0) {
            featureVector.multiplyByConstant(-1);
            classValue = classValue * -1;
         }
         BaselearnerInstance instance = new BaselearnerInstance(ArrayUtils.addAll(contextFeatures, featureVector.asArray()), classValue);
         transformedDataset.addInstance(instance);
         k++;
      }
      return transformedDataset;
   }


   /**
    * The method to transform the provided {@link ObjectRankingInstance} to classifier
    * {@link BaselearnerDataset}.A threshold point say t. which is determined by this value t =
    * thresholdPercentage*numberofrankedobjects in an ranking. Then all objects ranked before t are
    * classified as +1 while others as -1.
    * 
    * @param objectRankingInstance the dataset to be transformed
    * @param minimumThresholdRank the minimum threshold rank for which the dataset has to be created
    * @param lambda the lamda value for order-svm for regularizers
    * @return the transformed base learner dataset
    */
   public static BaselearnerDataset transformInstanceToOrderedPairwiseClassificationDataset(ObjectRankingInstance objectRankingInstance,
         int minimumThresholdRank, double lambda) {
      int[] objects = objectRankingInstance.getRating().getObjectList();
      int numberOfItemFeatures = objectRankingInstance.getNumofItemFeatures();
      int numberOfContextFeatures = objectRankingInstance.getNumberOfContextsFeatures();
      int totalNumOfObjectFeatures = numberOfItemFeatures + numberOfContextFeatures;
      int numbderOfFeaturesForBaseLearnerDataset = minimumThresholdRank * (totalNumOfObjectFeatures + 1) - 1;
      BaselearnerDataset transformedDataset = new BaselearnerDataset(objects.length * (minimumThresholdRank - 1),
            numbderOfFeaturesForBaseLearnerDataset);
      double[] contextFeatures = objectRankingInstance.getContextFeatureVector();
      double constant = Math.sqrt((lambda * totalNumOfObjectFeatures) / 2);
      for (int thresholdRank = 1; thresholdRank < minimumThresholdRank; thresholdRank++) {
         int rank = 1;
         for (int obj : objects) {
            double classValue = Math.signum((float) thresholdRank - (float) rank);
            if (thresholdRank == rank)
               classValue = 1.0;
            double[] itemFeaturesForObjectOne = objectRankingInstance.getFeaturesForItem(obj);
            double[] feature = ArrayUtils.addAll(contextFeatures, itemFeaturesForObjectOne);
            double[] bias = new double[minimumThresholdRank - 1];
            Arrays.fill(bias, 0.0);
            bias[thresholdRank - 1] = 1;
            double[] vtFeatures = new double[totalNumOfObjectFeatures * (minimumThresholdRank - 1)];
            for (int i = totalNumOfObjectFeatures * (thresholdRank - 1); i < totalNumOfObjectFeatures * thresholdRank; i++) {
               vtFeatures[i] = constant * feature[i - totalNumOfObjectFeatures * (thresholdRank - 1)];
            }
            BaselearnerInstance instance = new BaselearnerInstance(ArrayUtils.addAll(ArrayUtils.addAll(feature, bias), vtFeatures),
                  classValue);
            transformedDataset.addInstance(instance);
            rank++;
         }
      }
      return transformedDataset;
   }


   /**
    * This method transforms each instance {@link ObjectRankingInstance} to classifier
    * {@link BaselearnerDataset} using function
    * {@link #transformInstanceToPairswisePreferencesClassifcationDataset} and creates a list of
    * base learner dataset.
    * 
    * @param objectRankingDataset the object ranking dataset
    * @param methodType the order-svm or svor method to generate preferences
    * @param lambda the lambda value for regularization of v_ts for order-svm pairwise feature
    *           generations
    * @return the list of {@link BaselearnerDataset}
    */
   public static List<BaselearnerDataset> createListOfBaseLearnerDatasetsFromObjectRankingDataset(ObjectRankingDataset objectRankingDataset,
         String methodType, double lambda) {
      List<BaselearnerDataset> baselearnerDatasets = new ArrayList<>();
      for (int i = 0; i < objectRankingDataset.getNumberOfInstances(); i++) {
         ObjectRankingInstance instance = (ObjectRankingInstance) objectRankingDataset.getInstance(i);
         if (methodType.equals(PairwiseRankingConfiguration.SVOR)) {
            baselearnerDatasets.add(transformInstanceToPairswisePreferencesClassifcationDataset(instance));
         } else if (methodType.equals(PairwiseRankingConfiguration.ORDER_SVM)) {
            baselearnerDatasets.add(transformInstanceToOrderedPairwiseClassificationDataset(instance,
                  objectRankingDataset.getMinimumLengthOfRating(), lambda));
         }
      }
      return baselearnerDatasets;
   }


   /**
    * This method transforms each instance {@link ObjectRankingInstance} to classifier
    * {@link BaselearnerDataset} using function
    * {@link #transformInstanceToPairswisePreferencesClassifcationDataset} and creates a list of
    * base learner dataset, which are then combined to form one base learner dataset using
    * {@link #combineListOfBaselearnerDatasets}
    * 
    * @param objectRankingDataset the object ranking dataset
    * @param methodType the order-svm or svor method to generate preferences
    * @param lambda the lambda value for regularization of v_ts for order-svm pairwise feature
    *           generations
    * @return the list of {@link BaselearnerDataset}
    */
   public static BaselearnerDataset createBaseLearnerDatasetFromObjectRankingDataset(ObjectRankingDataset objectRankingDataset,
         String methodType, double lambda) {
      List<BaselearnerDataset> baselearnerDatasets = createListOfBaseLearnerDatasetsFromObjectRankingDataset(objectRankingDataset,
            methodType, lambda);
      return combineListOfBaselearnerDatasets(baselearnerDatasets);
   }


   /**
    * Combines the given base leaner datasets into one dataset.
    * 
    * @param baselearnerDatasets the list of base learner datasets
    * @return the combines base learner dataset
    */
   public static BaselearnerDataset combineListOfBaselearnerDatasets(List<BaselearnerDataset> baselearnerDatasets) {
      int numberOfInstances = 0;
      int numberOfFeatures = 0;
      for (BaselearnerDataset dataset : baselearnerDatasets) {
         numberOfFeatures = baselearnerDatasets.get(0).getNumberOfFeatures();
         numberOfInstances += dataset.getNumberOfInstances();
         if (numberOfFeatures != dataset.getNumberOfFeatures()) {
            throw new WrongDatasetInputException(CANNOT_COMBINE_THE_GIVEN_BASE_LEARNER_ERROR_MESSAGE);
         }
      }
      BaselearnerDataset transformedDataset = new BaselearnerDataset(numberOfInstances, numberOfFeatures);
      for (BaselearnerDataset dataset : baselearnerDatasets) {
         for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
            BaselearnerInstance instance = dataset.getInstance(i);
            transformedDataset.addInstance(instance);
         }
      }
      return transformedDataset;

   }

}
