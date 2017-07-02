package de.upb.cs.is.jpl.api.metric;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This abstract test should simplify tests for decomposable metrics mapping from double to double.
 * They implement the tests on lists of the inputs by using the methods for testing on single
 * inputs.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class AAverageDecomposableDoubleDoubleMetricTest extends ADecomposableMetricTest<Double, Double> {

   /**
    * Creates a new {@link AAverageDecomposableDoubleDoubleMetricTest} with the given additional
    * resource path.
    * 
    * @param additionalResourcePath the additional resource path to add at this level of abstraction
    */
   public AAverageDecomposableDoubleDoubleMetricTest(String additionalResourcePath) {
      super(additionalResourcePath);
   }


   @Override
   public List<Pair<Pair<List<Double>, List<Double>>, Double>> getCorrectListofPairsListOfRatings() {
      List<Pair<Pair<Double, Double>, Double>> correctListOfPairsOfRatings = getCorrectListofPairsOfRatings();
      List<Pair<Pair<List<Double>, List<Double>>, Double>> correctListOfPairsListOfRatings = new ArrayList<>();
      List<Double> firstArguments = new ArrayList<>();
      List<Double> secondArguments = new ArrayList<>();
      double result = 0;
      for (Pair<Pair<Double, Double>, Double> pairs : correctListOfPairsOfRatings) {
         firstArguments.add(pairs.getFirst().getFirst());
         secondArguments.add(pairs.getFirst().getSecond());
         result += pairs.getSecond();
      }
      correctListOfPairsListOfRatings.add(Pair.of(Pair.of(firstArguments, secondArguments), result / firstArguments.size()));

      return correctListOfPairsListOfRatings;
   }


   @Override
   public List<Pair<String, Pair<Pair<List<Double>, List<Double>>, Double>>> getWrongListOfPairsOfListOfRatingsWithExceptionMessage() {
      List<Pair<Pair<List<Double>, List<Double>>, Double>> wrongListOfPairsListOfRatings = getCorrectListofPairsListOfRatings();
      wrongListOfPairsListOfRatings.get(0).getFirst().getFirst().add(2.0);
      List<Pair<String, Pair<Pair<List<Double>, List<Double>>, Double>>> wrongListOfPairsOfListOfRatingsWithExceptionMessage = new ArrayList<>();
      wrongListOfPairsOfListOfRatingsWithExceptionMessage
            .add(Pair.of(TestUtils.getStringByReflectionSafely(AMetric.class, REFLECTION_ERROR_UNEQUAL_LIST_SIZES),
                  wrongListOfPairsListOfRatings.get(0)));

      return wrongListOfPairsOfListOfRatingsWithExceptionMessage;
   }

}
