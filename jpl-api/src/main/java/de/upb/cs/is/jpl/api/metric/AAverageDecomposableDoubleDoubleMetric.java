package de.upb.cs.is.jpl.api.metric;


import java.util.List;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;


/**
 * This abstract class can be extended by decomposable metrics mapping from double to double. It
 * implements both of the loss aggregation methods by taking the average.
 * 
 * @author Alexander Hetzer
 *
 * @param <CONFIG> the type of the configuration of this metric
 */
public abstract class AAverageDecomposableDoubleDoubleMetric<CONFIG extends AMetricConfiguration>
      extends ADecomposableMetric<CONFIG, Double, Double> {

   /**
    * Creates new {@link AAverageDecomposableDoubleDoubleMetric} with the default metric
    * configuration.
    * 
    * @param metricIdentifer the metric identifier
    */
   public AAverageDecomposableDoubleDoubleMetric(String metricIdentifer) {
      super(metricIdentifer);
   }


   @Override
   public Double getAggregatedLossForRatings(List<Double> expectedRatings, List<Double> predictedRatings) throws LossException {
      assertListsAreNotNullAndHaveSameSize(expectedRatings, predictedRatings);
      double result = 0;
      for (int i = 0; i < expectedRatings.size(); i++) {
         result += getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
      }
      return result / expectedRatings.size();
   }


   @Override
   public Double getWeightedAggregatedLossForRatings(List<Double> weights, List<Double> expectedRatings, List<Double> predictedRatings)
         throws LossException {
      assertListsAreNotNullAndHaveSameSize(expectedRatings, predictedRatings, weights);
      double result = 0;
      for (int i = 0; i < expectedRatings.size(); i++) {
         result += weights.get(i) * getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
      }
      return result / expectedRatings.size();
   }


}
