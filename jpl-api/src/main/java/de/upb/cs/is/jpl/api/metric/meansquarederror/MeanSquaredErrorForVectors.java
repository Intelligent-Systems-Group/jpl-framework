package de.upb.cs.is.jpl.api.metric.meansquarederror;


import java.util.List;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.ADecomposableMetric;
import de.upb.cs.is.jpl.api.metric.AMetricConfiguration;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.EmptyMetricConfiguration;


/**
 * MeanAbsoluteError is evaluation metric class that is responsible for evaluation based on the
 * metric associated with {@link EMetric#MEAN_ABSOLUTE_ERROR} value.
 * 
 * @see de.upb.cs.is.jpl.api.metric.AMetric
 * @author Sebastian Osterbrink
 *
 */
public class MeanSquaredErrorForVectors extends ADecomposableMetric<EmptyMetricConfiguration, IVector, Double> {

   /**
    * Creates new {@link MeanSquaredErrorForVectors} with the default metric configuration.
    */
   public MeanSquaredErrorForVectors() {
      super(EMetric.MEAN_SQUARED_ERROR_FOR_VECTORS.getMetricIdentifier());
   }


   @Override
   protected AMetricConfiguration createDefaultMetricConfiguration() {
      return new EmptyMetricConfiguration();
   }


   @Override
   public Double getAggregatedLossForRatings(List<IVector> expectedRatings, List<IVector> predictedRatings) throws LossException {
      double resultValue = 0.0;

      for (int i = 0; i < expectedRatings.size(); i++) {
         resultValue += getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
      }
      resultValue /= expectedRatings.size();

      return resultValue;

   }


   @Override
   public Double getLossForSingleRating(IVector expectedRating, IVector predictedRating) throws LossException {
      Double resultValue = 0.0;
      for (int i = 0; i < expectedRating.length(); i++) {
         resultValue += Math.pow(expectedRating.getValue(i) - predictedRating.getValue(i), 2);
      }
      return resultValue;
   }


   @Override
   public Double getWeightedAggregatedLossForRatings(List<Double> weights, List<IVector> expectedRatings, List<IVector> predictedRatings)
         throws LossException {
      throw new UnsupportedOperationException(OPERATION_NOT_SUPPORTED_ERROR_MESSAGE);
   }


}
