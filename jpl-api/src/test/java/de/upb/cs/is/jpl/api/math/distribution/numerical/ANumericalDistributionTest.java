package de.upb.cs.is.jpl.api.math.distribution.numerical;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.exception.math.UndefinedStatisticException;
import de.upb.cs.is.jpl.api.math.distribution.ADistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.IDistribution;
import de.upb.cs.is.jpl.api.math.distribution.numerical.integer.IIntegerDistribution;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.IRealDistribution;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.api.util.datastructure.Triple;


/**
 * Abstract test class for {@link ANumericalDistribution} implementations.
 * 
 * @author Tanja Tornede
 *
 * @param <SPACE> Defines the type of elements forming the space which this distribution is defined
 *           over.
 */
public abstract class ANumericalDistributionTest<SPACE extends Number> extends ADistributionTest<SPACE> {

   protected static final String ERROR_GETTING_PROBABILITY_SHOULD_RETURN_IN_EXCEPTION_FOR = "Getting the probability should return in an exception for the following value: ";
   private static final String ERROR_VARIANCE_OF_DISTRIBUTION_EXPECTED_TO_BE_UNDEFINED = "The variance of this distribution was expected to be undefined, but is not.";
   private static final String ERROR_EXPECTED_VALUE_OF_DISTRIBUTION_EXPECTED_TO_BE_UNDEFINED = "The expected value of this distribution was expected to be undefined, but is not.";

   protected static final String REFLECTION_ANUMERICALDISTRIBUTION_ERROR_LOWER_BOUND_BIGGER_UPPER_BOUND = "ERROR_LOWER_BOUND_BIGGER_UPPER_BOUND";

   private static final String RESOURCE_DIRECTORY_LEVEL = "math" + StringUtils.FORWARD_SLASH + "distribution" + StringUtils.FORWARD_SLASH;


   /**
    * Creates a new unit test for numerical distributions with the additional path to the resources
    * given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ANumericalDistributionTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   /**
    * Returns the distribution created with default parameters.
    * 
    * @return a distribution with the default parameters
    */
   @Override
   protected abstract INumericalDistribution<SPACE> getDistribution();


   /**
    * Returns an array of parameters to create a distribution used for testing all statistics.
    * 
    * @return an array of parameters to test all statistics
    * @throws JsonParsingFailedException if something went wrong during the creation of a
    *            {@link JsonObject}
    */
   protected abstract JsonObject getTestingParameters() throws JsonParsingFailedException;


   /**
    * Returns the variance of the distribution created with {@link #getTestingParameters()}. If the
    * variance of a distribution is not defined, that means an exception is thrown, this method has
    * to return {@link Double#NaN}.
    * 
    * @return the variance of the testing distribution
    */
   protected abstract double getTestingVarianceResult();


   /**
    * Returns the expected value of the distribution created with {@link #getTestingParameters()}.
    * If the expected value of a distribution is not defined, that means an exception is thrown,
    * this method has to return {@link Double#NaN}.
    * 
    * @return the expected value of the testing distribution
    */
   protected abstract double getTestingExpectedValueResult();


   /**
    * Returns a list of values used to test {@link IDistribution#getProbabilityFor(SPACE)}.
    * <p>
    * Every entry of the list is a triple:
    * <ul>
    * <li>The first element is the value that is given to the method.
    * <li>The second element is the expected return value of the method.
    * <li>The third element represents the exception string if one is expected, in this case the
    * second element will not be considered. If it is not the case this element has to be
    * {@link StringUtils#EMPTY_STRING}.
    * </ul>
    * </p>
    * 
    * @return a list of tiples to test {@link IDistribution#getProbabilityFor(SPACE)}
    */
   protected abstract List<Triple<SPACE, Double, String>> getTestingProbabilityEventsAndResults();


   /**
    * Returns a list of values used to test
    * {@link INumericalDistribution#getProbabilityFor(double, double)}.
    * <p>
    * Every entry of the list is a triple:
    * <ul>
    * <li>The first element is a pair of values that is given to the method.
    * <li>The second element is the expected return value of the method.
    * <li>The third element represents the exception string if one is expected, in this case the
    * second element will not be considered. If it is not the case this element has to be
    * {@link StringUtils#EMPTY_STRING}.
    * </ul>
    * </p>
    * 
    * @return a list of tiples to test
    *         {@link INumericalDistribution#getProbabilityFor(double, double)}
    */
   protected abstract List<Triple<Pair<SPACE, SPACE>, Double, String>> getTestingProbabilityEventPairsAndResults();


   /**
    * Returns a list of values used to test
    * {@link INumericalDistribution#getCumulativeDistributionFunctionValueFor(double)}.
    * <p>
    * Every entry of the list is a pair:
    * <ul>
    * <li>The first element is the value that is given to the method.
    * <li>The second element is the expected return value of the method.
    * </ul>
    * </p>
    * 
    * @return a list of pairs to test
    *         {@link INumericalDistribution#getCumulativeDistributionFunctionValueFor(double)}
    */
   protected abstract List<Pair<SPACE, Double>> getTestingCumulativeEventsAndResults();


   /**
    * Returns the distribution created with testing parameters.
    * 
    * @return a distribution with the testing parameters
    * 
    * @throws JsonParsingFailedException if something went wrong while parsing the json file
    * @throws ParameterValidationFailedException if something went wrong while validating the
    *            parameters
    */
   protected abstract INumericalDistribution<SPACE> getTestingDistribution()
         throws JsonParsingFailedException,
            ParameterValidationFailedException;


   /**
    * Tests the variance of the distribution.
    * 
    * @throws JsonParsingFailedException if something went wrong while parsing the json file
    * @throws ParameterValidationFailedException if something went wrong while validating the
    *            parameters
    */
   @Test
   public void testVariance() throws JsonParsingFailedException, ParameterValidationFailedException {
      INumericalDistribution<SPACE> distribution = getTestingDistribution();
      try {
         double variance = distribution.getVariance();
         Assert.assertEquals(getTestingVarianceResult(), variance, TestUtils.DOUBLE_DELTA);
      } catch (UndefinedStatisticException ex) {
         if (!Double.isNaN(getTestingVarianceResult())) {
            Assert.fail(ERROR_VARIANCE_OF_DISTRIBUTION_EXPECTED_TO_BE_UNDEFINED);
         }
      }
   }


   /**
    * Tests the variance of the distribution.
    * 
    * @throws JsonParsingFailedException if something went wrong while parsing the json file
    * @throws ParameterValidationFailedException if something went wrong while validating the
    *            parameters
    */
   @Test
   public void testExpectedValue() throws JsonParsingFailedException, ParameterValidationFailedException {
      INumericalDistribution<SPACE> distribution = getTestingDistribution();
      try {
         double expectedValue = distribution.getExpectedValue();
         Assert.assertEquals(getTestingExpectedValueResult(), expectedValue, TestUtils.DOUBLE_DELTA);
      } catch (UndefinedStatisticException ex) {
         if (!Double.isNaN(getTestingExpectedValueResult())) {
            Assert.fail(ERROR_EXPECTED_VALUE_OF_DISTRIBUTION_EXPECTED_TO_BE_UNDEFINED);
         }
      }
   }


   /**
    * Tests the method {@link IDistribution#getProbabilityFor(Object)} of the distribution.
    * 
    * @throws JsonParsingFailedException if something went wrong while parsing the json file
    * @throws ParameterValidationFailedException if something went wrong while validating the
    *            parameters
    * @throws DistributionException if the given value is not valid for the distribution
    */
   @Test
   public void testGetProbabilityFor() throws JsonParsingFailedException, ParameterValidationFailedException, DistributionException {
      INumericalDistribution<SPACE> distribution = getTestingDistribution();
      List<Triple<SPACE, Double, String>> probabilityEventsAndResults = getTestingProbabilityEventsAndResults();
      for (int i = 0; i < probabilityEventsAndResults.size(); i++) {
         Triple<SPACE, Double, String> eventAndResultPair = probabilityEventsAndResults.get(i);
         if (eventAndResultPair.getThird().equals(StringUtils.EMPTY_STRING)) {
            Assert.assertEquals(eventAndResultPair.getSecond(), distribution.getProbabilityFor(eventAndResultPair.getFirst()),
                  TestUtils.DOUBLE_DELTA);
         } else {
            try {
               distribution.getProbabilityFor(eventAndResultPair.getFirst());
               Assert.fail(ERROR_GETTING_PROBABILITY_SHOULD_RETURN_IN_EXCEPTION_FOR + eventAndResultPair.getFirst());
            } catch (DistributionException e) {
               Assert.assertEquals(eventAndResultPair.getThird(), e.getMessage());
            }
         }
      }
   }


   /**
    * Tests the method {@link IIntegerDistribution#getProbabilityFor(Integer)} of the distribution.
    * 
    * @throws JsonParsingFailedException if something went wrong while parsing the json file
    * @throws ParameterValidationFailedException if something went wrong while validating the
    *            parameters
    * @throws DistributionException if the given value is not valid for the distribution
    */
   @Test
   public void testGetProbabilityForInterval()
         throws JsonParsingFailedException,
            ParameterValidationFailedException,
            DistributionException {
      INumericalDistribution<SPACE> distribution = getTestingDistribution();
      List<Triple<Pair<SPACE, SPACE>, Double, String>> probabilityEventPairsAndResults = getTestingProbabilityEventPairsAndResults();
      for (int i = 0; i < probabilityEventPairsAndResults.size(); i++) {
         Triple<Pair<SPACE, SPACE>, Double, String> eventPairAndResult = probabilityEventPairsAndResults.get(i);
         if (eventPairAndResult.getThird().equals(StringUtils.EMPTY_STRING)) {
            Assert.assertEquals(eventPairAndResult.getSecond().doubleValue(),
                  getProbabilityFor(distribution, eventPairAndResult.getFirst().getFirst(), eventPairAndResult.getFirst().getSecond()),
                  TestUtils.DOUBLE_DELTA);
         } else {
            try {
               getProbabilityFor(distribution, eventPairAndResult.getFirst().getFirst(), eventPairAndResult.getFirst().getSecond());
               Assert.fail(ERROR_GETTING_PROBABILITY_SHOULD_RETURN_IN_EXCEPTION_FOR + eventPairAndResult.getFirst().getFirst().doubleValue()
                     + StringUtils.SINGLE_WHITESPACE + eventPairAndResult.getFirst().getSecond().doubleValue());
            } catch (DistributionException e) {
               Assert.assertEquals(eventPairAndResult.getThird(), e.getMessage());
            }
         }
      }
   }


   /**
    * Tests the method {@link IIntegerDistribution#getCumulativeDistributionFunctionValueFor(int)}
    * of the distribution.
    * 
    * @throws JsonParsingFailedException if something went wrong while parsing the json file
    * @throws ParameterValidationFailedException if something went wrong while validating the
    *            parameters
    */
   @Test
   public void testGetCumulativeDistributionFunctionValue() throws JsonParsingFailedException, ParameterValidationFailedException {
      INumericalDistribution<SPACE> distribution = getTestingDistribution();
      List<Pair<SPACE, Double>> cumulativeEventsAndResults = getTestingCumulativeEventsAndResults();
      for (int i = 0; i < cumulativeEventsAndResults.size(); i++) {
         Pair<SPACE, Double> cumulativeEventAndResultPair = cumulativeEventsAndResults.get(i);
         Assert.assertEquals(cumulativeEventAndResultPair.getSecond().doubleValue(),
               getCumulativeDistributionFunctionValueFor(distribution, cumulativeEventAndResultPair.getFirst()), TestUtils.DOUBLE_DELTA);
      }
   }


   /**
    * Returns the method value {@link IIntegerDistribution#getProbabilityFor(int, int)} or
    * {@link IRealDistribution#getProbabilityFor(double, double)}, depending on the type of the
    * given {@code distribution}.
    * 
    * @param distribution the distribution to get the method value from
    * @param lowerBound the lower bound of the probability interval
    * @param upperBound the upper bound of the probability interval
    */
   protected abstract double getProbabilityFor(INumericalDistribution<SPACE> distribution, SPACE lowerBound, SPACE upperBound)
         throws DistributionException;


   /**
    * Returns the method value
    * {@link IIntegerDistribution#getCumulativeDistributionFunctionValueFor(int)} or
    * {@link IRealDistribution#getCumulativeDistributionFunctionValueFor(double)}, depending on the
    * type of the given {@code distribution}.
    * 
    * @param distribution the distribution to get the method value from
    * @param value the argument for which the cumulative distribution function should be computed
    */
   protected abstract double getCumulativeDistributionFunctionValueFor(INumericalDistribution<SPACE> distribution, SPACE value);

}
