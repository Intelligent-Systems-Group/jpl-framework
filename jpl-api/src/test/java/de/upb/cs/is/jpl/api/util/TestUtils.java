package de.upb.cs.is.jpl.api.util;


import java.io.File;
import java.lang.reflect.Field;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class provides some useful methods which can be used to write tests.
 * 
 * @author Tanja Tornede
 *
 */
public class TestUtils {

   /** Delta value used to approximate the distance of two double values in the test cases. */
   public static final double DOUBLE_DELTA = 0.0001;


   /** The root path to the default configurations in the main resources. */
   public static final String DEFAULT_CONFIGURATIONS_ROOT_PATH = "src" + File.separator + "main" + File.separator + "resources"
         + File.separator + "default_configuration" + File.separator;

   /** The root path to the test resources. */
   public static final String TEST_RESOURCES_ROOT_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator;


   /**
    * Returns the string form the given declared field of the given class to reflect.
    * 
    * @param classToReflect the class containing a member variable with the given name
    * @param declaredFieldToReflect the declared field of the given class to reflect
    * @return the string reflected from the given class
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   public static String getStringByReflection(Class<?> classToReflect, String declaredFieldToReflect)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Field f = classToReflect.getDeclaredField(declaredFieldToReflect);
      f.setAccessible(true);
      return ((String) f.get(null));
   }


   /**
    * Returns the string form the given declared field of the given class to reflect. If the
    * reflection fails due to an exception an empty string is returned.
    * 
    * @param classToReflect the class containing a member variable with the given name
    * @param declaredFieldToReflect the declared field of the given class to reflect
    * @return the string reflected from the given class or an empty string if the reflection fails
    */
   public static String getStringByReflectionSafely(Class<?> classToReflect, String declaredFieldToReflect) {
      try {
         return getStringByReflection(classToReflect, declaredFieldToReflect);
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         return "";
      }
   }


   /**
    * Checks whether the difference between the two given double values is below or equal to the
    * given delta.
    * 
    * @param firstValue the first double value
    * @param secondValue the second double value
    * @param delta the maximum distance the two values are allowed to have
    * @return {@code true}, if the difference between the first and the second value is smaller or
    *         equal to delta, otherwise {@code false}
    */
   public static boolean areDoublesEqual(double firstValue, double secondValue, double delta) {
      return Math.abs(firstValue - secondValue) <= delta;
   }


   /**
    * Returns a pair of rankings for the given ranking objects.
    *
    * @param firstRankingObjects the first ranking objects
    * @param secondRankingObjects the second ranking objects
    * @return the ranking pair
    */
   public static Pair<Ranking, Ranking> getRankingPair(int[] firstRankingObjects, int[] secondRankingObjects) {
      int[] firstRankingCompareOberators = Ranking.createCompareOperatorArrayForLabels(firstRankingObjects);
      Ranking firstRanking = new Ranking(firstRankingObjects, firstRankingCompareOberators);

      int[] secondRankingCompareOberators = Ranking.createCompareOperatorArrayForLabels(secondRankingObjects);
      Ranking secondRanking = new Ranking(secondRankingObjects, secondRankingCompareOberators);

      return Pair.of(firstRanking, secondRanking);
   }


   /**
    * Returns a pair of the pair of the arguments and the expected result and the expected exception
    * message.
    * 
    * @param argumentsAndExpectedResult the pair of the arguments and the expected result
    * @param expectedExceptionMessage the expected exception message
    * @return the pair of the arguments and the expected result and the expected exception message
    */
   public static Pair<String, Pair<Pair<IVector, IVector>, Double>> getArgumentAndExpectedResultPairWithExpectedExceptionMessage(
         Pair<Pair<IVector, IVector>, Double> argumentsAndExpectedResult, String expectedExceptionMessage) {
      return Pair.of(expectedExceptionMessage, argumentsAndExpectedResult);
   }


   /**
    * Returns a pair of the pair of the arguments and the expected result.
    * 
    * @param firstArgument the first argument to put into the inner pair
    * @param secondArgument the second argument to put into the inner pair
    * @param expectedResult the expected result to put into the outer pair
    * @return a pair of the pair of the arguments and the expected result
    */
   public static Pair<Pair<IVector, IVector>, Double> getArgumentAndExpectedResultPair(double[] firstArgument, double[] secondArgument,
         Double expectedResult) {
      return Pair.of(Pair.of(new DenseDoubleVector(firstArgument), new DenseDoubleVector(secondArgument)), expectedResult);
   }

}
