package de.upb.cs.is.jpl.api.metric.hammingloss;


import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.AAverageDecomposableIVectorDoubleMetric;
import de.upb.cs.is.jpl.api.metric.AMetricConfiguration;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.EmptyMetricConfiguration;


/**
 * This instance wise decomposable metric represents the hamming loss.
 * 
 * @author Alexander Hetzer
 *
 */
public class HammingLoss extends AAverageDecomposableIVectorDoubleMetric<EmptyMetricConfiguration> {

   /**
    * Creates a new {@link HammingLoss} evaluation metric.
    */
   public HammingLoss() {
      super(EMetric.HAMMINGLOSS.getMetricIdentifier());
   }


   @Override
   public Double getLossForSingleRating(IVector expectedRating, IVector predictedRating) throws LossException {
      if (expectedRating.length() != predictedRating.length()) {
         throw new LossException(ERROR_UNEQUAL_VECTOR_SIZES);
      }
      double[] expectedRatingAsArray = expectedRating.asArray();
      double[] predictedRatingAsArray = predictedRating.asArray();

      double sum = 0;
      for (int i = 0; i < expectedRatingAsArray.length; i++) {
         if (Math.abs(expectedRatingAsArray[i] - predictedRatingAsArray[i]) > 0) {
            sum += 1;
         }
      }

      return sum / expectedRating.length();
   }


   @Override
   protected AMetricConfiguration createDefaultMetricConfiguration() {
      return new EmptyMetricConfiguration();
   }

}
