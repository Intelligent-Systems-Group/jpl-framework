package de.upb.cs.is.jpl.api.metric;


import java.util.List;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * This abstract class can be used for all non decomposable metrics mapping from a vector to double.
 * It offers convenience methods such as checking that two lists have the same size.
 * 
 * @author Alexander Hetzer
 *
 * @param <CONFIG> the configuration type of this metric
 */
public abstract class ANonDecomposableIVectorDoubleMetric<CONFIG extends AMetricConfiguration>
      extends ANonDecomposableMetric<CONFIG, IVector, Double> {


   /**
    * Creates new {@link ANonDecomposableIVectorDoubleMetric} with the default metric configuration.
    * 
    * @param metricIdentifer the metric identifier
    */
   public ANonDecomposableIVectorDoubleMetric(String metricIdentifer) {
      super(metricIdentifer);
   }


   /**
    * Asserts that the two lists have the same size. If this is not the case a {@link LossException}
    * is thrown.
    * 
    * @param expectedRatings the first list to check
    * @param predictedRatings the second list to check
    * 
    * @throws LossException if the two lists do not have the same size
    */
   protected void assertRatingListsHaveSameSize(List<IVector> expectedRatings, List<IVector> predictedRatings) throws LossException {
      if (expectedRatings.size() != predictedRatings.size()) {
         throw new LossException(ERROR_UNEQUAL_LIST_SIZES);
      }
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
