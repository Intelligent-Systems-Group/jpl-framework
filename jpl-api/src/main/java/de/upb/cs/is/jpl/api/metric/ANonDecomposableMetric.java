package de.upb.cs.is.jpl.api.metric;


import java.util.List;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;


/**
 * The abstract implementation for instance wise non-decomposable metric. The instance wise
 * non-decomposable metric can only have compute a value on a list of actual and expected inputs, as
 * it is not instance wise decomposable.
 * 
 * @author Pritha Gupta
 * @param <CONFIG> the generic type extending AEvaluationMetricConfiguration, configuration
 *           associated with the Evaluation Metric class
 * @param <INPUT> the input type of the metric
 * @param <OUTPUT> the result type of the metric, i.e. Double
 * 
 */
public abstract class ANonDecomposableMetric<CONFIG extends AMetricConfiguration, INPUT, OUTPUT> extends AMetric<CONFIG, INPUT, OUTPUT> {


   /**
    * Creates new {@link ANonDecomposableMetric} with the default metric configuration.
    * 
    * @param metricIdentifer the metric identifier
    */
   public ANonDecomposableMetric(String metricIdentifer) {
      super(metricIdentifer);
   }


   @Override
   public OUTPUT getLossForSingleRating(INPUT expectedRating, INPUT predictedRating) throws LossException {
      throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_ERROR_MESSAGE);
   }


   @Override
   public OUTPUT getWeightedAggregatedLossForRatings(List<Double> weights, List<INPUT> expectedRatings, List<INPUT> predictedRatings)
         throws LossException {
      throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_ERROR_MESSAGE);
   }


   @Override
   public List<OUTPUT> getLossForRatings(List<INPUT> expectedRatings, List<INPUT> predictedRatings) throws LossException {
      throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_ERROR_MESSAGE);
   }
}
