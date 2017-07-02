package de.upb.cs.is.jpl.api.metric.subset01;


import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.AAverageDecomposableIVectorDoubleMetric;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.EmptyMetricConfiguration;


/**
 * This class implements the subset 01 loss for multilabel classification. For a given instance the
 * loss is {@code 1}, if the predicted and the actual label set are not identical and {@code 0}, if
 * they are identical.
 * 
 * @author Alexander Hetzer
 *
 */
public class Subset01Loss extends AAverageDecomposableIVectorDoubleMetric<EmptyMetricConfiguration> {

   /**
    * Creates new {@link Subset01Loss} with the default metric configuration.
    */
   public Subset01Loss() {
      super(EMetric.SUBSET_01_LOSS.getMetricIdentifier());
   }


   @Override
   public Double getLossForSingleRating(IVector expectedRating, IVector predictedRating) throws LossException {
      assertEqualVectorLength(expectedRating, predictedRating);
      if (expectedRating.equals(predictedRating)) {
         return 0.0;
      }
      return 1.0;
   }


   @Override
   protected EmptyMetricConfiguration createDefaultMetricConfiguration() {
      return new EmptyMetricConfiguration();
   }

}
