package de.upb.cs.is.jpl.api.metric.kendallstau;


import java.util.List;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.ADecomposableMetric;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.RankCorrelationHelper;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * KendallsTau is evaluation metric class that is responsible for evaluation based on the metric
 * associated with {@link EMetric#KENDALLS_TAU} value.
 * 
 * @see de.upb.cs.is.jpl.api.metric.AMetric
 * @author Pritha Gupta
 * @author Andreas Kornelsen
 *
 */
public class KendallsTau extends ADecomposableMetric<KendallsTauConfiguration, Ranking, Double> {
   private static final String EXCEPTION_MESSAGE_PREDICTED_RANK_DOESNOT_CONTAIN_ALL_OBJECTS_IN_TRUE_RANKING = "The objects in expected ranking is not present in the predicted ranking.";
   private static final String EXCEPTION_MESSAGE_PREDICTED_RANK_IS_GREATER = "The number of objects in predicted ranking are greater then the objects in the partial ranking.";


   /**
    * Creates a new Kendall's Tau evaluation metric.
    */
   public KendallsTau() {
      super(EMetric.KENDALLS_TAU.getMetricIdentifier());
   }


   @Override
   protected KendallsTauConfiguration createDefaultMetricConfiguration() {
      return new KendallsTauConfiguration();
   }


   @Override
   public Double getLossForSingleRating(Ranking expectedRanking, Ranking predictionRanking) throws LossException {
      int[] objectsIncludingTruth = expectedRanking.getObjectList();
      int[] objectsPrediction = predictionRanking.getObjectList();

      checkInitialConditions(objectsIncludingTruth, objectsPrediction);
      int orignalLength = objectsPrediction.length;

      if (objectsIncludingTruth.length < objectsPrediction.length) {
         objectsPrediction = RankCorrelationHelper.removeObjectsFromPredictionNotOccuringInTheTruth(objectsIncludingTruth,
               objectsPrediction);
      }

      objectsIncludingTruth = RankCorrelationHelper.getIncrementOrderValues(objectsIncludingTruth);
      objectsPrediction = RankCorrelationHelper.getIncrementOrderValues(objectsPrediction);

      int transposition = 0;
      for (int i = 0; i < objectsPrediction.length; i++) {
         for (int j = i + 1; j < objectsPrediction.length; j++) {
            if ((objectsIncludingTruth[i] - objectsIncludingTruth[j]) * (objectsPrediction[i] - objectsPrediction[j]) < 0)
               transposition++;
         }
      }
      return 1 - ((4.0 * transposition) / ((orignalLength) * (orignalLength - 1)));
   }


   private void checkInitialConditions(int[] objectsIncludingTruth, int[] objectsPrediction) throws LossException {
      if (objectsIncludingTruth.length > objectsPrediction.length || objectsPrediction.length < 2) {
         throw new LossException(EXCEPTION_MESSAGE_PREDICTED_RANK_IS_GREATER);
      }
      boolean valid = true;
      for (int object : objectsIncludingTruth) {
         if (CollectionsUtils.indexOfObjectInIntegerArray(objectsPrediction, object) == -1) {
            valid = false;
         }
      }
      if (!valid) {
         throw new LossException(EXCEPTION_MESSAGE_PREDICTED_RANK_DOESNOT_CONTAIN_ALL_OBJECTS_IN_TRUE_RANKING);
      }
   }


   @Override
   public Double getAggregatedLossForRatings(List<Ranking> expectedRatings, List<Ranking> predictedRatings) throws LossException {
      double meanLoss = 0.0;
      for (int i = 0; i < expectedRatings.size(); i++) {
         meanLoss += getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
      }
      meanLoss = meanLoss / expectedRatings.size();
      return meanLoss;
   }


   @Override
   public Double getWeightedAggregatedLossForRatings(List<Double> weights, List<Ranking> expectedRatings, List<Ranking> predictedRatings)
         throws LossException {
      double weightedLoss = 0.0;
      double weightSum = 0.0;
      for (int i = 0; i < expectedRatings.size(); i++) {
         weightedLoss += weights.get(i) * getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
         weightSum += weights.get(i);
      }
      weightedLoss = weightedLoss / weightSum;
      return weightedLoss;
   }


}
