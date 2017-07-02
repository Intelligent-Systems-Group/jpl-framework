package de.upb.cs.is.jpl.api.metric;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * This abstract class can be extended by decomposable metrics mapping from vector to double. It
 * implements both of the loss aggregation methods by taking the average. Furthermore it offers some
 * convenience methods such as checking if two vectors have the same length.
 * 
 * @author Alexander Hetzer
 *
 * @param <CONFIG> the type of the configuration of this metric
 */
public abstract class AAverageDecomposableIVectorDoubleMetric<CONFIG extends AMetricConfiguration>
      extends ADecomposableMetric<CONFIG, IVector, Double> {

   private static final Logger logger = LoggerFactory.getLogger(AAverageDecomposableIVectorDoubleMetric.class);

   private static final String ERROR_CANNOT_AVERAGE = "Cannot average over 0 values. Either the input list is empty or all evaluations on single values failed.";


   /**
    * Creates new {@link AAverageDecomposableIVectorDoubleMetric} with the default metric
    * configuration.
    * 
    * @param metricIdentifer the metric identifier
    */
   public AAverageDecomposableIVectorDoubleMetric(String metricIdentifer) {
      super(metricIdentifer);
   }


   @Override
   public Double getAggregatedLossForRatings(List<IVector> expectedRatings, List<IVector> predictedRatings) throws LossException {
      if (expectedRatings.size() != predictedRatings.size()) {
         throw new LossException(ERROR_UNEQUAL_LIST_SIZES);
      }
      double aggregatedLoss = 0;
      int successfulComputations = 0;
      for (int i = 0; i < expectedRatings.size(); i++) {
         try {
            aggregatedLoss += getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
            successfulComputations++;
         } catch (LossException exception) {
            logger.error(exception.getMessage(), exception);
         }
      }
      if (successfulComputations == 0) {
         throw new LossException(ERROR_CANNOT_AVERAGE);
      }
      return aggregatedLoss / successfulComputations;
   }


   @Override
   public Double getWeightedAggregatedLossForRatings(List<Double> weights, List<IVector> expectedRatings, List<IVector> predictedRatings)
         throws LossException {
      if (expectedRatings.size() != predictedRatings.size() || expectedRatings.size() != weights.size()) {
         throw new LossException(ERROR_UNEQUAL_LIST_SIZES);
      }
      double aggregatedLoss = 0;
      for (int i = 0; i < expectedRatings.size(); i++) {
         aggregatedLoss += weights.get(i) * getLossForSingleRating(expectedRatings.get(i), predictedRatings.get(i));
      }
      return aggregatedLoss / expectedRatings.size();
   }


   /**
    * Checks if the given two vectors have the same length. If this is not the case a
    * {@link LossException} is thrown.
    * 
    * @param firstVector the first vector to check
    * @param secondVector the second vector to check
    * 
    * @throws LossException if the given vectors have an unequal length
    */
   protected void assertEqualVectorLength(IVector firstVector, IVector secondVector) throws LossException {
      if (firstVector.length() != secondVector.length()) {
         throw new LossException(ERROR_UNEQUAL_VECTOR_SIZES);
      }
   }

}
