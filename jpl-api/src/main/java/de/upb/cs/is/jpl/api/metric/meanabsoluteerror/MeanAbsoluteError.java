package de.upb.cs.is.jpl.api.metric.meanabsoluteerror;


import java.util.List;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.ADecomposableMetric;
import de.upb.cs.is.jpl.api.metric.AMetricConfiguration;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.EmptyMetricConfiguration;


/**
 * MeanAbsoluteError is evaluation metric class that is responsible for evaluation based on the
 * metric associated with {@link EMetric#MEAN_ABSOLUTE_ERROR} value.
 * 
 * @see de.upb.cs.is.jpl.api.metric.AMetric
 * @author Sebastian Gottschalk
 *
 */
public class MeanAbsoluteError extends ADecomposableMetric<EmptyMetricConfiguration, Double, Double> {
   private static final String ERROR_UNEQUAL_LIST_LENGTH = "The lists of the the expectations and the predictions must have the same length";


   /**
    * Creates new {@link MeanAbsoluteError} with the default metric configuration.
    */
   public MeanAbsoluteError() {
      super(EMetric.MEAN_ABSOLUTE_ERROR.getMetricIdentifier());
   }


   @Override
   protected AMetricConfiguration createDefaultMetricConfiguration() {
      return new EmptyMetricConfiguration();
   }


   @Override
   public Double getAggregatedLossForRatings(List<Double> expectedRatings, List<Double> predictedRatings) throws LossException {
      if (expectedRatings == null || predictedRatings == null || expectedRatings.size() != predictedRatings.size()) {
         throw new LossException(ERROR_UNEQUAL_LIST_LENGTH);
      }

      double resultValue = 0.0;
      for (int i = 0; i < expectedRatings.size(); i++) {
         resultValue += getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
      }
      resultValue /= expectedRatings.size();
      return resultValue;
   }


   @Override
   public Double getLossForSingleRating(Double expectedRating, Double predictedRating) throws LossException {
      return Math.abs(expectedRating - predictedRating);
   }


   @Override
   public Double getWeightedAggregatedLossForRatings(List<Double> weights, List<Double> expectedRatings, List<Double> predictedRatings)
         throws LossException {
      throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_ERROR_MESSAGE);
   }

}
