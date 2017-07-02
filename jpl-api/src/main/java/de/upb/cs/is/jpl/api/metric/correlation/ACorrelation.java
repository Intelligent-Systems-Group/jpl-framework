package de.upb.cs.is.jpl.api.metric.correlation;


import java.util.List;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.ADecomposableMetric;
import de.upb.cs.is.jpl.api.metric.EmptyMetricConfiguration;


/**
 * Correlations describe the similarity between two vectors.
 * 
 * @author Sebastian Osterbrink
 * @param <RATING> the type of object for which the correlation is computed
 *
 */
public abstract class ACorrelation<RATING> extends ADecomposableMetric<EmptyMetricConfiguration, RATING, Double> {


   private static final String AGGREGATION_NOT_SUPPORTED = "Correlations only support pairwise operations.";


   /**
    * Creates new {@link ACorrelation} with the default metric configuration.
    * 
    * @param metricIdentifer the metric identifier
    */
   public ACorrelation(String metricIdentifer) {
      super(metricIdentifer);
   }


   @Override
   public Double getAggregatedLossForRatings(List<RATING> expectedRatings, List<RATING> predictedRatings) throws LossException {
      throw new UnsupportedOperationException(AGGREGATION_NOT_SUPPORTED);
   }


   @Override
   public Double getWeightedAggregatedLossForRatings(List<Double> weights, List<RATING> expectedRatings, List<RATING> predictedRatings)
         throws LossException {
      throw new UnsupportedOperationException(AGGREGATION_NOT_SUPPORTED);
   }


   @Override
   protected EmptyMetricConfiguration createDefaultMetricConfiguration() {
      return new EmptyMetricConfiguration();
   }
}
