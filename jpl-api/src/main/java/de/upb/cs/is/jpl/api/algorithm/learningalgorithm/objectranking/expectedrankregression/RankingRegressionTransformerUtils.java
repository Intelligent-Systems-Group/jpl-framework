package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.expectedrankregression;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingInstance;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains implementations to convert an instance or the object ranking dataset to the
 * base learner dataset for regression. So that we can use different {@link ABaselearnerAlgorithm}
 * which are implemented to solve the regression problem on it and use it to get the scoring
 * function.
 * 
 * @author Pritha Gupta
 *
 */
public class RankingRegressionTransformerUtils {


   /**
    * Hides the public constructor.
    */
   private RankingRegressionTransformerUtils() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * It transform one instance of the Object Ranking dataset to {@link BaselearnerDataset} for
    * running regression.
    * 
    * @param objectRankingInstance the object ranking instance to be used for transformation
    * @return the base learner dataset to be used for learning the linear regression function
    */
   public static BaselearnerDataset tranformOneInstanceToBaseLearnerRegressionDataset(ObjectRankingInstance objectRankingInstance) {
      Ranking ranking = objectRankingInstance.getRating();
      int numOfObjectsInPartialOrder = ranking.getObjectList().length;
      int numberOfFeatures = objectRankingInstance.getNumofItemFeatures();
      int numberOfContextFeatures = objectRankingInstance.getNumberOfContextsFeatures();
      BaselearnerDataset transformedDataset = new BaselearnerDataset(numOfObjectsInPartialOrder,
            numberOfFeatures + numberOfContextFeatures);
      double rank = 1;
      for (int obj : ranking.getObjectList()) {
         double expectedRank = rank / (numOfObjectsInPartialOrder + 1);
         double[] features = ArrayUtils.addAll(objectRankingInstance.getContextFeatureVector(),
               objectRankingInstance.getFeaturesForItem(obj));

         BaselearnerInstance instance = new BaselearnerInstance(features, expectedRank);
         transformedDataset.addInstance(instance);
         rank++;
      }
      return transformedDataset;
   }


   /**
    * The function to transform the part of the provided object ranking dataset to base learner
    * dataset so that linear regression model can be learned on it. This function transform each
    * rank of each object to expected rank of the object and add pair of the expected rank and
    * feature value of the item and the context under which it is ranked in the dataset.
    * 
    * @param objectRankingDataset the dataset to be transformed
    * @return the transformed base learner dataset
    */
   public static BaselearnerDataset transformDatasetToBaseLearnerRegressionDataset(ObjectRankingDataset objectRankingDataset) {
      Map<Integer, ArrayList<Pair<Double, double[]>>> objectAndExpectedRanks = new HashMap<>();
      int numberOfInstances = 0;

      for (int i = 0; i < objectRankingDataset.getNumberOfInstances(); i++) {
         Ranking ranking = objectRankingDataset.getInstance(i).getRating();
         int numOfObjectsInPartialOrder = ranking.getObjectList().length;
         double[] contextVector = objectRankingDataset.getInstance(i).getContextFeatureVector();
         double rank = 1;
         for (int obj : ranking.getObjectList()) {
            double expectedRank = rank / (numOfObjectsInPartialOrder + 1);
            ArrayList<Pair<Double, double[]>> expectedRanks = new ArrayList<>();
            if (!objectAndExpectedRanks.containsKey(obj)) {
               expectedRanks.add(Pair.of(expectedRank, contextVector));
               objectAndExpectedRanks.put(obj, expectedRanks);
            } else {
               expectedRanks = (ArrayList<Pair<Double, double[]>>) CollectionsUtils.getDeepCopyOf(objectAndExpectedRanks.get(obj));
               expectedRanks.add(Pair.of(expectedRank, contextVector));
               objectAndExpectedRanks.replace(obj, expectedRanks);
            }
            rank++;
            numberOfInstances++;
         }
      }

      BaselearnerDataset transformedDataset = new BaselearnerDataset(numberOfInstances,
            objectRankingDataset.getNumofItemFeatures() + objectRankingDataset.getNumofContextFeatures());
      Iterator<Entry<Integer, ArrayList<Pair<Double, double[]>>>> iterator = objectAndExpectedRanks.entrySet().iterator();
      while (iterator.hasNext()) {
         Entry<Integer, ArrayList<Pair<Double, double[]>>> entry = iterator.next();
         int object = entry.getKey();
         for (Pair<Double, double[]> rankAndContextVector : entry.getValue()) {
            double[] features = ArrayUtils.addAll(rankAndContextVector.getSecond(), objectRankingDataset.getItemVector(object));
            BaselearnerInstance instance = new BaselearnerInstance(features, rankAndContextVector.getFirst());
            transformedDataset.addInstance(instance);
         }
      }
      return transformedDataset;

   }


}
