package de.upb.cs.is.jpl.api.metric;


import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.common.AParameterizedUnitTest;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The test template for the {@link ANonDecomposableMetric} evaluation metric class. If you are
 * implementing any evaluation metrics, then you need to extend this class to test the basic
 * functionality of the evaluation metric.
 * 
 * @author Pritha Gupta
 * @param <INPUT> the return type of the prediction(s) and true values on which the loss needs to be
 *           checked
 * @param <OUTPUT> the metric return type
 */
public abstract class ANonDecomposableMetricTest<INPUT, OUTPUT> extends AParameterizedUnitTest {
   protected static final double ERROR_MARGIN = 0.001;
   private static final String RESOURCE_DIRECTORY_LEVEL = "metric" + File.separator;
   private static final String ERROR_CORRECT_PARAMETER_NOT_ACCEPTED = "The evaluation metric should accept the parameter value pair: %s.";
   private static final String ERROR_INCORRECT_PARAMETER_ACCEPTED = "The evaluation metric  should not accept the parameter value pair: %s.";
   protected static final String ERROR_WRONG_OUTPUT = "The output should be \"%s\" and is \"%s\".";
   protected static final String ERROR_CORRECT_RATING_GIVING_WRONG_LOSS = "The evaluation metric should accept the give value %s for rating value pair: %s.";
   protected static final String ASSERT_EVALUATION_LOSS_EQUAL = "The loss of the metric %s, should be the expected value %s, but was %s. ";

   protected static final String REFLECTION_ERROR_UNEQUAL_LIST_SIZES = "ERROR_UNEQUAL_LIST_SIZES";
   protected static final String REFLECTION_ERROR_UNEQUAL_VECTOR_SIZES = "ERROR_UNEQUAL_VECTOR_SIZES";


   /**
    * Creates a new unit test for {@link ANonDecomposableMetricTest} with the additional path to the
    * resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ANonDecomposableMetricTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL, additionalResourcePath);
   }


   /**
    * Returns an instance of the evaluation metric which should be checked and you also need to
    * provide the type of prediction(s) and loss type.
    * 
    * @return instance of the evaluation metric to check
    */
   public abstract IMetric<INPUT, OUTPUT> getEvaluationMetric();


   /**
    * Returns the correct list of pairs of predictions and true values in form of list of ratings
    * and the corresponding metric result for the provided ratings. It provides inputs for positive
    * testing of {@link AMetric#getAggregatedLossForRatings(List, List)}.
    * 
    * @return a list of true value of ratings and predicted value ratings paired with the metric
    *         loss
    */
   public abstract List<Pair<Pair<List<INPUT>, List<INPUT>>, OUTPUT>> getCorrectListofPairsListOfRatings();


   /**
    * Returns the incorrect list of pairs of predictions and true values in form of ratings and the
    * corresponding wrong metric result for the provided ratings, with exception message thrown for
    * the ratings provided. It provides inputs for negative testing of
    * {@link AMetric#getAggregatedLossForRatings(List, List)}.
    * 
    * @return a list of true value of ratings and predicted value ratings paired with corresponding
    *         wrong metric loss value and expected exception message
    */
   public abstract List<Pair<String, Pair<Pair<List<INPUT>, List<INPUT>>, OUTPUT>>> getWrongListOfPairsOfListOfRatingsWithExceptionMessage();


   /**
    * Test if the evaluation metric able to set correct parameter. This test fails if some parameter
    * is correct and is not parsed properly by {@link AMetricConfiguration}. {@inheritDoc}
    * 
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   @Override
   @Test
   public void testCorrectParameters() throws JsonParsingFailedException {
      List<JsonObject> testPairs = getCorrectParameters();

      for (int i = 0; i < testPairs.size(); i++) {
         IMetric<?, ?> evaluationMetric = getEvaluationMetric();
         JsonObject object = testPairs.get(i);
         try {
            evaluationMetric.setParameters(object);

         } catch (ParameterValidationFailedException e) {
            // Fail if exception in set parameters is thrown
            fail(String.format(ERROR_CORRECT_PARAMETER_NOT_ACCEPTED, object.toString()));
         }
      }
   }


   /**
    * Test if the evaluation if able to detect incorrect parameter. It should fail if no exception
    * is thrown for the given parameter values. It also asserts if the given exception message is
    * what which is thrown by the evaluation metric. {@inheritDoc}
    * 
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   @Override
   @Test
   public void testWrongParameters() throws JsonParsingFailedException {
      List<Pair<String, JsonObject>> testPairs = getWrongParameters();

      for (int i = 0; i < testPairs.size(); i++) {
         IMetric<?, ?> evaluationMetric = getEvaluationMetric();
         JsonObject object = testPairs.get(i).getSecond();
         try {
            evaluationMetric.setParameters(object);
            // Fail if no exception in set parameters is thrown
            fail(String.format(ERROR_INCORRECT_PARAMETER_ACCEPTED, testPairs.get(i).getSecond().toString()));
         } catch (ParameterValidationFailedException exception) {
            Assert.assertEquals(ERROR_WRONG_OUTPUT, exception.getMessage(), testPairs.get(i).getFirst());
         }
      }
   }


   /**
    * Tests if evaluation metric is able to calculate correct loss for a given pair of list of
    * rating. It provides test for positive testing of
    * {@link AMetric#getAggregatedLossForRatings(List, List)}.
    */
   @Test
   public void testCorrectListOfPairsOfListofRatings() {
      List<Pair<Pair<List<INPUT>, List<INPUT>>, OUTPUT>> testPairs = getCorrectListofPairsListOfRatings();
      IMetric<INPUT, OUTPUT> evaluationMetric = getEvaluationMetric();

      for (int i = 0; i < testPairs.size(); i++) {

         try {
            OUTPUT prediction = evaluationMetric.getAggregatedLossForRatings(testPairs.get(i).getFirst().getFirst(),
                  testPairs.get(i).getFirst().getSecond());
            OUTPUT actual = testPairs.get(i).getSecond();
            if (prediction instanceof Double) {
               boolean compareResultDoubleValues = TestUtils.areDoublesEqual((Double) actual, (Double) prediction, 0.0000001);
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
    * Tests if evaluation metric is able to calculate correct loss for a given pair of list of
    * rating, by negative testing, i.e. by checking if loss function is not the incorrect value for
    * loss provided by user. And if for a particular rating the loss cannot be calculated then, it
    * should throw the {@link LossException} with correct message. It provides test for negative
    * testing of {@link AMetric#getAggregatedLossForRatings(List, List)}.
    */
   @Test
   public void testWrongListOfPairsOfListofRatings() {
      List<Pair<String, Pair<Pair<List<INPUT>, List<INPUT>>, OUTPUT>>> testPairs = getWrongListOfPairsOfListOfRatingsWithExceptionMessage();

      for (int i = 0; i < testPairs.size(); i++) {
         IMetric<INPUT, OUTPUT> evaluationMetric = getEvaluationMetric();

         try {
            Pair<Pair<List<INPUT>, List<INPUT>>, OUTPUT> ratingWithWrongResult = testPairs.get(i).getSecond();
            OUTPUT prediction = evaluationMetric.getAggregatedLossForRatings(ratingWithWrongResult.getFirst().getFirst(),
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
            Assert.assertEquals(ERROR_WRONG_OUTPUT, exception.getMessage(), testPairs.get(i).getFirst());
         }
      }
   }

}
