package de.upb.cs.is.jpl.api.metric.hammingloss;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.AAverageDecomposableIVectorDoubleMetricTest;
import de.upb.cs.is.jpl.api.metric.AMetric;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class tests the {@link HammingLoss}.
 * 
 * @author Alexander Hetzer
 *
 */
public class HammingLossTest extends AAverageDecomposableIVectorDoubleMetricTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "hammingloss" + File.separator;


   /**
    * Creates a new {@link HammingLossTest}.
    */
   public HammingLossTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public List<Pair<Pair<IVector, IVector>, Double>> getCorrectListofPairsOfRatings() {
      List<Pair<Pair<IVector, IVector>, Double>> correctListOfPairsOfRatings = new ArrayList<>();
      double[] firstRating1 = { 1, 0, 1, 1, 0 };
      double[] secondRating1 = { 0, 1, 0, 0, 1 };
      correctListOfPairsOfRatings.add(TestUtils.getArgumentAndExpectedResultPair(firstRating1, secondRating1, 1.0));

      double[] firstRating2 = { 0, 1, 1, 1, 0 };
      double[] secondRating2 = { 0, 1, 1, 0, 1 };
      correctListOfPairsOfRatings.add(TestUtils.getArgumentAndExpectedResultPair(firstRating2, secondRating2, 2.0 / 5.0));

      double[] firstRating3 = { 0, 0, 0, 0, 0 };
      double[] secondRating3 = { 0, 0, 0, 0, 0 };
      correctListOfPairsOfRatings.add(TestUtils.getArgumentAndExpectedResultPair(firstRating3, secondRating3, 0.0));

      double[] firstRating4 = { 1, 1, 1, 1, 1 };
      double[] secondRating4 = { 1, 1, 1, 1, 1 };
      correctListOfPairsOfRatings.add(TestUtils.getArgumentAndExpectedResultPair(firstRating4, secondRating4, 0.0));

      double[] firstRating5 = { 0, 1, 1, 1, 1 };
      double[] secondRating5 = { 1, 1, 1, 1, 1 };
      correctListOfPairsOfRatings.add(TestUtils.getArgumentAndExpectedResultPair(firstRating5, secondRating5, 1.0 / 5.0));

      return correctListOfPairsOfRatings;
   }


   @Override
   public List<Pair<String, Pair<Pair<IVector, IVector>, Double>>> getWrongListOfPairsOfRatingsWithExceptionMessage() {
      List<Pair<String, Pair<Pair<IVector, IVector>, Double>>> wrongListOfPairsOfRatingsWithExceptionMessage = new ArrayList<>();

      double[] firstRating1 = { 1, 0, 1, 1, 0, 0 };
      double[] secondRating1 = { 0, 1, 0, 0, 1 };
      wrongListOfPairsOfRatingsWithExceptionMessage.add(TestUtils.getArgumentAndExpectedResultPairWithExpectedExceptionMessage(
            TestUtils.getArgumentAndExpectedResultPair(firstRating1, secondRating1, 0.0),
            TestUtils.getStringByReflectionSafely(AMetric.class, REFLECTION_ERROR_UNEQUAL_VECTOR_SIZES)));
      return wrongListOfPairsOfRatingsWithExceptionMessage;
   }


   @Override
   public IMetric<IVector, Double> getEvaluationMetric() {
      return new HammingLoss();
   }

}
