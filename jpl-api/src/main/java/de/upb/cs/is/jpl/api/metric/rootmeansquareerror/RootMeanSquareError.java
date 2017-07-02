package de.upb.cs.is.jpl.api.metric.rootmeansquareerror;


import java.util.List;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.AMetricConfiguration;
import de.upb.cs.is.jpl.api.metric.ANonDecomposableMetric;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.EmptyMetricConfiguration;
import de.upb.cs.is.jpl.api.metric.meansquarederror.MeanSquaredError;


/**
 * This class implements the root mean square error.
 * 
 * @author Pritha Gupta
 *
 */
public class RootMeanSquareError extends ANonDecomposableMetric<EmptyMetricConfiguration, Double, Double> {

   /**
    * Creates new {@link RootMeanSquareError} with the default metric configuration.
    */
   public RootMeanSquareError() {
      super(EMetric.ROOT_MEAN_SQUARE_ERROR.getMetricIdentifier());
   }


   @Override
   public Double getAggregatedLossForRatings(List<Double> expectedRatings, List<Double> predictedRatings) throws LossException {
      MeanSquaredError meanSquareError = new MeanSquaredError();
      double meanSquarLoss = meanSquareError.getAggregatedLossForRatings(expectedRatings, predictedRatings);
      return Math.sqrt(meanSquarLoss);
   }


   @Override
   protected AMetricConfiguration createDefaultMetricConfiguration() {
      return new EmptyMetricConfiguration();
   }


   @Override
   public Double getWeightedAggregatedLossForRatings(List<Double> weights, List<Double> expectedRatings, List<Double> predictedRatings)
         throws LossException {
      MeanSquaredError meanSquareError = new MeanSquaredError();
      double weightedLoss = 0.0;
      double weightSum = 0.0;
      for (int i = 0; i < expectedRatings.size(); i++) {
         weightedLoss += weights.get(i) * meanSquareError.getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
         weightSum += weights.get(i);
      }
      weightedLoss = weightedLoss / weightSum;
      return Math.sqrt(weightedLoss);
   }
}
