package de.upb.cs.is.jpl.api.metric.examplebasedfmeasure;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.ANonDecomposableIVectorDoubleMetric;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class implements the example based F-measure (as presented in the paper "A Review on
 * Multi-Label Learning Algorithms" by Min-Ling Zhang and Zhi-Hua Zhou) with a beta parameter for
 * multilabel classification. The measure is computed using the aggregated (averaged) results of the
 * example based {@link ExampleBasedPrecision} and {@link ExampleBasedRecall} implementations.
 * 
 * @author Alexander Hetzer
 *
 */
public class ExampleBasedFMeasure extends ANonDecomposableIVectorDoubleMetric<FMeasureConfiguration> {

   private static final String ERROR_CANNOT_COMPUTE_F_MEASURE_DUE_TO_ZERO_DENOMINATOR = "Cannot compute f-measure due to zero denominator.";

   private double beta = 1;

   private static final ExampleBasedPrecision PRECISION = new ExampleBasedPrecision();
   private static final ExampleBasedRecall RECALL = new ExampleBasedRecall();


   /**
    * Creates a new {@link ExampleBasedFMeasure}.
    */
   public ExampleBasedFMeasure() {
      super(EMetric.EXAMPLE_BASED_F_MEASURE.getMetricIdentifier());
      beta = getMetricConfiguration().getBeta();
   }


   @Override
   public Double getAggregatedLossForRatings(List<IVector> expectedRatings, List<IVector> predictedRatings) throws LossException {
      assertRatingListsHaveSameSize(expectedRatings, predictedRatings);
      Pair<List<IVector>, List<IVector>> clearedRatings = getPairOfValidExamples(expectedRatings, predictedRatings);
      List<IVector> clearedExpectedRatings = clearedRatings.getFirst();
      List<IVector> clearedPredictedRatings = clearedRatings.getSecond();

      double numerator = (1 + Math.pow(beta, 2)) * PRECISION.getAggregatedLossForRatings(clearedExpectedRatings, clearedPredictedRatings)
            * RECALL.getAggregatedLossForRatings(clearedExpectedRatings, clearedPredictedRatings);
      double denominator = Math.pow(beta, 2) * PRECISION.getAggregatedLossForRatings(clearedExpectedRatings, clearedPredictedRatings)
            + RECALL.getAggregatedLossForRatings(clearedExpectedRatings, clearedPredictedRatings);
      if (denominator <= 0) {
         throw new LossException(ERROR_CANNOT_COMPUTE_F_MEASURE_DUE_TO_ZERO_DENOMINATOR);
      }
      return numerator / denominator;
   }


   /**
    * Returns a pair of lists of valid expected and predicted ratings, which can be used as
    * arguments for the {@link ExampleBasedFMeasure}. It clears the given lists from those examples,
    * which cannot be used as arguments for the f-measure. It specifically excludes 0 vectors, as
    * they cannot be handled by definition.
    * 
    * @param expectedRatings the list of expected vectors
    * @param predictedRatings the list of predicted vectors
    * @return a pair of lists of valid expected and predicted ratings
    * @throws LossException if any of the corresponding vectors inside the lists are of unequal
    *            length
    */
   private Pair<List<IVector>, List<IVector>> getPairOfValidExamples(List<IVector> expectedRatings, List<IVector> predictedRatings)
         throws LossException {
      List<IVector> clearedExpectedRatings = new ArrayList<>();
      List<IVector> clearedPredictedRatings = new ArrayList<>();
      for (int i = 0; i < expectedRatings.size(); i++) {
         boolean expectedRatingIsZeroVector = true;
         boolean predictedRatingIsZeroVector = true;
         IVector expectedRating = expectedRatings.get(i);
         IVector predictedRating = predictedRatings.get(i);
         assertEqualVectorLength(expectedRating, predictedRating);
         for (int j = 0; j < expectedRating.length(); j++) {
            if (Double.compare(expectedRating.getValue(j), 1.0) == 0) {
               expectedRatingIsZeroVector = false;
            }
            if (Double.compare(predictedRating.getValue(j), 1.0) == 0) {
               predictedRatingIsZeroVector = false;
            }
         }
         if (!expectedRatingIsZeroVector && !predictedRatingIsZeroVector) {
            clearedExpectedRatings.add(expectedRating);
            clearedPredictedRatings.add(predictedRating);
         }
      }
      return Pair.of(clearedExpectedRatings, clearedPredictedRatings);
   }


   @Override
   protected FMeasureConfiguration createDefaultMetricConfiguration() {
      return new FMeasureConfiguration();
   }


}
