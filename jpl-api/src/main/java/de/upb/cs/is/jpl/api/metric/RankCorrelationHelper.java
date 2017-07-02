package de.upb.cs.is.jpl.api.metric;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * The Class contains helper methods for the rank correlation metrics Kendall's Tau and Spearman's
 * Rank Correlation.
 * 
 * @author Andreas Kornelsen
 */
public class RankCorrelationHelper {

   /**
    * This private constructor which hides the public one.
    */
   private RankCorrelationHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Removes the objects from the prediction not occurring in the truth.
    *
    * @param objectsIncludingTruth the objects including truth
    * @param objectsPrediction the objects prediction
    * @return the objects of the prediction occurring in the truth
    */
   public static int[] removeObjectsFromPredictionNotOccuringInTheTruth(int[] objectsIncludingTruth, int[] objectsPrediction) {
      int[] predictionWithoutUnorruredObjectsInTruth = new int[objectsIncludingTruth.length];

      int index = 0;
      for (int objectPrediction : objectsPrediction) {
         for (int objectTruth : objectsIncludingTruth) {
            if (objectPrediction == objectTruth) {
               predictionWithoutUnorruredObjectsInTruth[index++] = objectPrediction;
            }
         }
      }

      return predictionWithoutUnorruredObjectsInTruth;
   }


   /**
    * Returns the increment order of the given object list. For example if [4, 5, 1] is given it
    * will be transformed to [2, 3, 1]
    *
    * @param objectList the object list with not increment labels
    * @return the increment order values
    */
   public static int[] getIncrementOrderValues(int[] objectList) {
      int[] incrementOrderValues = new int[objectList.length];
      List<Integer> objectPrediction = IntStream.of(objectList).boxed().collect(Collectors.toList());

      List<Integer> sortedObjectPrediction = CollectionsUtils.getDeepCopyOf(objectPrediction);
      Collections.sort(sortedObjectPrediction);
      int index = 0;
      for (int object : objectPrediction) {
         incrementOrderValues[index++] = sortedObjectPrediction.indexOf(object) + 1;
      }

      return incrementOrderValues;
   }
}
