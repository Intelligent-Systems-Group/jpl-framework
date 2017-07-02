package de.upb.cs.is.jpl.api.metric.spearmancorrelation;


import java.util.List;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.ADecomposableMetric;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.RankCorrelationHelper;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * The class represents an implementation for the Spearman's rank correlation. It can handle partial
 * ranking by computing an 'optimistic' rank correlation between a partial expected ranking and a
 * compute predicted ranking.
 * 
 * @author Andreas Kornelsen
 *
 */
public class SpearmansCorrelation extends ADecomposableMetric<SpearmansConfiguration, Ranking, Double> {

   private static final String EXCEPTION_MESSAGE_LENGTH_RANKING = "The number of objects in the predicted ranking is less then the number of objects in true ranking.";
   private static final String EXCEPTION_MESSAGE_PREDICTED_RANK_DOESNOT_CONTAIN_ALL_OBJECTS_IN_TRUE_RANKING = "The objects in expected ranking is not present in the predicted ranking.";


   /**
    * Creates new {@link SpearmansCorrelation} with the default metric configuration.
    */
   public SpearmansCorrelation() {
      super(EMetric.SPEARMANS_RANK_CORRELATION.getMetricIdentifier());
   }


   @Override
   protected SpearmansConfiguration createDefaultMetricConfiguration() {
      return new SpearmansConfiguration();
   }


   @Override
   public Double getLossForSingleRating(Ranking expectedRanking, Ranking predictionRanking) throws LossException {
      int[] objectsIncludingTruth = expectedRanking.getObjectList();
      int[] objectsPrediction = predictionRanking.getObjectList();

      if (objectsIncludingTruth.length > objectsPrediction.length) {
         throw new LossException(EXCEPTION_MESSAGE_LENGTH_RANKING);
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
      if (objectsIncludingTruth.length < objectsPrediction.length) {
         objectsPrediction = RankCorrelationHelper.removeObjectsFromPredictionNotOccuringInTheTruth(objectsIncludingTruth,
               objectsPrediction);
      }

      objectsIncludingTruth = RankCorrelationHelper.getIncrementOrderValues(objectsIncludingTruth);
      objectsPrediction = RankCorrelationHelper.getIncrementOrderValues(objectsPrediction);

      double sumOfSquaredDistances = 0.0;
      for (int i = 0; i < objectsIncludingTruth.length; i++) {
         int indexInPrediction = objectsPrediction[i];
         int indexInExpectation = objectsIncludingTruth[i];
         sumOfSquaredDistances += Math.pow((double) indexInPrediction - indexInExpectation, 2);
      }

      int n = objectsIncludingTruth.length;
      return 1 - (6 * sumOfSquaredDistances / (n * (Math.pow(n, 2) - 1)));

   }


   @Override
   public Double getAggregatedLossForRatings(List<Ranking> expectedRatings, List<Ranking> predictedRatings) throws LossException {
      Double meanLoss = 0.0;
      for (int i = 0; i < expectedRatings.size(); i++) {
         meanLoss += getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
      }
      meanLoss = meanLoss / expectedRatings.size();
      return meanLoss;
   }


   @Override
   public Double getWeightedAggregatedLossForRatings(List<Double> weights, List<Ranking> expectedRatings, List<Ranking> predictedRatings)
         throws LossException {
      Double weightedLoss = 0.0;
      Double weightSum = 0.0;
      for (int i = 0; i < expectedRatings.size(); i++) {
         weightedLoss += weights.get(i) * getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
         weightSum += weights.get(i);
      }
      weightedLoss = weightedLoss / weightSum;
      return weightedLoss;
   }


}
