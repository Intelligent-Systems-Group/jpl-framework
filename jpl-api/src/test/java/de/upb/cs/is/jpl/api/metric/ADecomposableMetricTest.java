package de.upb.cs.is.jpl.api.metric;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The test template for the {@link ADecomposableMetric} evaluation metric class. If you are
 * implementing any decomposable evaluation metrics, then you need to extend this class to test the
 * basic functionality of the evaluation metric.
 * 
 * @author Pritha Gupta
 * @param <INPUT> the return type of the prediction(s) and true values on which the loss needs to be
 *           checked
 * @param <OUTPUT> the metric return type
 */
public abstract class ADecomposableMetricTest<INPUT, OUTPUT> extends ANonDecomposableMetricTest<INPUT, OUTPUT> {


   /**
    * Creates a new unit test for {@link ADecomposableMetricTest} with the additional path to the
    * resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ADecomposableMetricTest(String additionalResourcePath) {
      super(additionalResourcePath);
   }


   /**
    * Returns the correct list of pairs of predictions and true values in form of ratings and the
    * corresponding metric result for the provided ratings. It provides inputs for positive testing
    * of {@link AMetric#getLossForSingleRating(Object, Object)}.
    * 
    * @return a list of true value and predicted value paired with the metric loss
    */
   public abstract List<Pair<Pair<INPUT, INPUT>, OUTPUT>> getCorrectListofPairsOfRatings();


   /**
    * Returns the incorrect list of pairs of predictions and true values in form of ratings and the
    * corresponding wrong metric result for the provided ratings, with exception message thrown for
    * the ratings provided. It provides inputs for negative testing of
    * {@link AMetric#getLossForSingleRating(Object, Object)}.
    * 
    * @return a list of pairs of predictions and true values with corresponding wrong metric loss
    *         value and expected exception message
    */
   public abstract List<Pair<String, Pair<Pair<INPUT, INPUT>, OUTPUT>>> getWrongListOfPairsOfRatingsWithExceptionMessage();


   /**
    * Tests if evaluation metric is able to calculate correct loss for a given pair of rating. It
    * provides test for positive testing of {@link AMetric#getLossForSingleRating(Object, Object)}.
    */
   @Test
   public void testCorrectListOfPairsOfRatings() {
      List<Pair<Pair<INPUT, INPUT>, OUTPUT>> testPairs = getCorrectListofPairsOfRatings();

      for (int i = 0; i < testPairs.size(); i++) {
         IMetric<INPUT, OUTPUT> evaluationMetric = getEvaluationMetric();

         try {
            INPUT firstRating = testPairs.get(i).getFirst().getFirst();
            INPUT secondRating = testPairs.get(i).getFirst().getSecond();
            OUTPUT prediction = evaluationMetric.getLossForSingleRating(firstRating, secondRating);
            OUTPUT actual = testPairs.get(i).getSecond();
            if (prediction instanceof Double) {
               boolean compareResultDoubleValues = TestUtils.areDoublesEqual((Double) actual, (Double) prediction, ERROR_MARGIN);
               Assert.assertTrue(String.format(ASSERT_EVALUATION_LOSS_EQUAL, evaluationMetric.toString(), String.valueOf(actual),
                     String.valueOf(prediction)), compareResultDoubleValues);
            } else {
               Assert.assertEquals(actual, prediction);
            }
         } catch (LossException exception) {
            // Fail if exception in set parameters is thrown
            fail(String.format(ERROR_CORRECT_RATING_GIVING_WRONG_LOSS, testPairs.get(i).getSecond().toString(),
                  testPairs.get(i).getFirst().toString()));
         }
      }
   }


   /**
    * Tests if evaluation metric is able to calculate correct loss for a given pair of rating, by
    * negative testing, i.e. by checking if loss function is not returning the incorrect value for
    * loss provided by user. And if for a particular rating the loss cannot be calculated then, it
    * should throw the {@link LossException} with correct message and that message is the first part
    * of the objects in the list from
    * {@link ADecomposableMetricTest #getWrongListOfPairsOfRatingsWithExceptionMessage()}. It
    * provides test for negative testing of {@link AMetric#getLossForSingleRating(Object, Object)}.
    */
   @Test
   public void testWrongListOfPairsOfRatings() {
      List<Pair<String, Pair<Pair<INPUT, INPUT>, OUTPUT>>> testPairs = getWrongListOfPairsOfRatingsWithExceptionMessage();

      for (int i = 0; i < testPairs.size(); i++) {
         IMetric<INPUT, OUTPUT> evaluationMetric = getEvaluationMetric();

         try {
            Pair<Pair<INPUT, INPUT>, OUTPUT> ratingWithWrongResult = testPairs.get(i).getSecond();
            OUTPUT prediction = evaluationMetric.getLossForSingleRating(ratingWithWrongResult.getFirst().getFirst(),
                  ratingWithWrongResult.getFirst().getSecond());
            OUTPUT actual = ratingWithWrongResult.getSecond();
            if (prediction instanceof Double) {
               boolean compareResultDoubleValues = TestUtils.areDoublesEqual((Double) actual, (Double) prediction, ERROR_MARGIN);
               Assert.assertFalse(compareResultDoubleValues);
            }
            Assert.assertNotEquals(prediction, actual);
            Assert.assertTrue(testPairs.get(i).getFirst().isEmpty());
         } catch (LossException exception) {
            // Fail if exception in set parameters is thrown
            Assert.assertEquals(ERROR_WRONG_OUTPUT, testPairs.get(i).getFirst(), exception.getMessage());
         }
      }
   }


}
