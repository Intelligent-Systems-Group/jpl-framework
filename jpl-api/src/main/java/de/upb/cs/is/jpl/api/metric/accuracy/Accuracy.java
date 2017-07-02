package de.upb.cs.is.jpl.api.metric.accuracy;


import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.AAverageDecomposableIVectorDoubleMetric;
import de.upb.cs.is.jpl.api.metric.AMetricConfiguration;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.EmptyMetricConfiguration;


/**
 * This class implements the example based accuracy measure for multilabel classification, that is,
 * for a single instance the ratio of the size of the intersection between the actual and the
 * predicted labelset and the size of the union of the two sets is computed. The aggregation for a
 * whole dataset is done by averaging over the instances in the dataset. If the accuracy cannot be
 * computed for a specific instance, as both labels sets are empty, a {@link LossException} is
 * thrown. If this happens during the aggregation, the according instance is simply ignored.
 * 
 * @author Alexander Hetzer
 *
 */
public class Accuracy extends AAverageDecomposableIVectorDoubleMetric<EmptyMetricConfiguration> {


   private static final String ERROR_CANNOT_COMPUTE_ACCURACY_FOR_TWO_ZERO_VECTORS = "Cannot compute accuracy for two zero vectors.";


   /**
    * Creates a new {@link Accuracy}.
    */
   public Accuracy() {
      super(EMetric.ACCURACY.getMetricIdentifier());
   }


   @Override
   public Double getLossForSingleRating(IVector expectedRating, IVector predictedRating) throws LossException {
      assertEqualVectorLength(expectedRating, predictedRating);
      double nominator = 0;
      double denominator = 0;
      for (int i = 0; i < expectedRating.length(); i++) {
         if (Double.compare(expectedRating.getValue(i), 1.0) == 0 && Double.compare(predictedRating.getValue(i), 1.0) == 0) {
            nominator += 1;
         }
         if (Double.compare(expectedRating.getValue(i), 1.0) == 0 || Double.compare(predictedRating.getValue(i), 1.0) == 0) {
            denominator++;
         }
      }
      // if this is the case, both vectors are 0 vectors
      if (Double.compare(denominator, 0.0) == 0) {
         throw new LossException(ERROR_CANNOT_COMPUTE_ACCURACY_FOR_TWO_ZERO_VECTORS);
      }
      return nominator / denominator;
   }


   @Override
   protected AMetricConfiguration createDefaultMetricConfiguration() {
      return new EmptyMetricConfiguration();
   }

}
