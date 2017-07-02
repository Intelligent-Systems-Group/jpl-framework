package de.upb.cs.is.jpl.api.metric.examplebasedfmeasure;


import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.AAverageDecomposableIVectorDoubleMetric;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.EmptyMetricConfiguration;


/**
 * This class implements the example based recall measure for multilabel classification (as
 * presented in the paper "A Review on Multi-Label Learning Algorithms" by Min-Ling Zhang and
 * Zhi-Hua Zhou), that is, for a single instance the ratio of the size of the intersection between
 * the actual and the predicted labelset and the size of the actual label set is computed. The
 * aggregation for a whole dataset is done by averaging over the instances in the dataset. If the
 * measure cannot be computed for a specific instance, as both labels sets are empty, a
 * {@link LossException} is thrown. If this happens during the aggregation, the according instance
 * is simply ignored.
 * 
 * @author Alexander Hetzer
 *
 */
public class ExampleBasedRecall extends AAverageDecomposableIVectorDoubleMetric<EmptyMetricConfiguration> {

   private static final String ERROR_CANNOT_COMPUTE_RECALL_FOR_TWO_0_VECTORS = "Cannot compute recall for two 0 vectors.";


   /**
    * Creates a new {@link ExampleBasedPrecision}.
    */
   public ExampleBasedRecall() {
      super(EMetric.EXAMPLE_BASED_RECALL.getMetricIdentifier());
   }


   @Override
   public Double getLossForSingleRating(IVector expectedRating, IVector predictedRating) throws LossException {
      assertEqualVectorLength(expectedRating, predictedRating);
      double numerator = 0;
      double denominator = 0;
      for (int i = 0; i < expectedRating.length(); i++) {
         if (Double.compare(expectedRating.getValue(i), 1.0) == 0) {
            denominator++;
            if (Double.compare(predictedRating.getValue(i), 1.0) == 0) {
               numerator++;
            }
         }
      }
      if (denominator <= 0) {
         throw new LossException(ERROR_CANNOT_COMPUTE_RECALL_FOR_TWO_0_VECTORS);
      }
      return numerator / denominator;
   }


   @Override
   protected EmptyMetricConfiguration createDefaultMetricConfiguration() {
      return new EmptyMetricConfiguration();
   }


}
