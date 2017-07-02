package de.upb.cs.is.jpl.api.metric.meanabsoluteerror;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.metric.ADecomposableMetricTest;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class tests the {@link MeanAbsoluteError}.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class MeanAbsoluteErrorTest extends ADecomposableMetricTest<Double, Double> {
   private static final String RESOURCE_DIRECTORY_LEVEL = "meanabsoluteerror" + File.separator;
   private static final String UNEQUAL_LIST_LENGTH_REFLECTION_VARIABLE = "ERROR_UNEQUAL_LIST_LENGTH";
   private static final String ERROR_REFLECTION_FAILED = "Test can not run because reflection failed";

   private static final Logger logger = LoggerFactory.getLogger(MeanAbsoluteErrorTest.class);


   /**
    * Creates a new {@link MeanAbsoluteErrorTest}
    */
   public MeanAbsoluteErrorTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public List<Pair<Pair<Double, Double>, Double>> getCorrectListofPairsOfRatings() {
      List<Pair<Pair<Double, Double>, Double>> correctListOfPairs = new ArrayList<>();
      double[][] pairs = { { 1, 2 }, { 3, 5 }, { 6, 9 }, { 10, 14 } };
      double[] pairResults = { 1, 2, 3, 4 };

      for (int i = 0; i < pairs.length; i++) {
         correctListOfPairs.add(Pair.of(Pair.of(pairs[i][0], pairs[i][1]), pairResults[i]));
      }

      return correctListOfPairs;
   }


   @Override
   public List<Pair<String, Pair<Pair<Double, Double>, Double>>> getWrongListOfPairsOfRatingsWithExceptionMessage() {
      List<Pair<String, Pair<Pair<Double, Double>, Double>>> wrongListOfPairs = new ArrayList<>();
      wrongListOfPairs.add(Pair.of(StringUtils.EMPTY_STRING, Pair.of(Pair.of(5.0, 7.0), 1.0)));
      wrongListOfPairs.add(Pair.of(StringUtils.EMPTY_STRING, Pair.of(Pair.of(1.0, 7.0), 2.0)));
      return wrongListOfPairs;
   }


   @Override
   public IMetric<Double, Double> getEvaluationMetric() {
      return new MeanAbsoluteError();
   }


   @Override
   public List<Pair<Pair<List<Double>, List<Double>>, Double>> getCorrectListofPairsListOfRatings() {
      List<Pair<Pair<List<Double>, List<Double>>, Double>> correctListOfPairs = new ArrayList<>();
      List<Pair<Pair<Double, Double>, Double>> correctPairList = getCorrectListofPairsOfRatings();
      List<Double> expectationList = new ArrayList<>();
      List<Double> predictionList = new ArrayList<>();
      double result = 0.0;
      for (Pair<Pair<Double, Double>, Double> correctPair : correctPairList) {
         expectationList.add(correctPair.getFirst().getFirst());
         predictionList.add(correctPair.getFirst().getSecond());
         result += correctPair.getSecond();

      }
      result /= expectationList.size();
      correctListOfPairs.add(Pair.of(Pair.of(expectationList, predictionList), result));
      return correctListOfPairs;
   }


   @Override
   public List<Pair<String, Pair<Pair<List<Double>, List<Double>>, Double>>> getWrongListOfPairsOfListOfRatingsWithExceptionMessage() {
      List<Pair<String, Pair<Pair<List<Double>, List<Double>>, Double>>> wrongListOfPairs = new ArrayList<>();
      List<Double> expectationList = new ArrayList<>();
      List<Double> predictionList = new ArrayList<>();
      double result = 0.0;
      expectationList.add(1.0);
      expectationList.add(2.0);
      predictionList.add(1.0);
      try {
         String errorResult = TestUtils.getStringByReflection(MeanAbsoluteError.class, UNEQUAL_LIST_LENGTH_REFLECTION_VARIABLE);
         wrongListOfPairs.add(Pair.of(errorResult, Pair.of(Pair.of(expectationList, predictionList), result)));
         return wrongListOfPairs;
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         Assert.fail(ERROR_REFLECTION_FAILED);
         logger.error(ERROR_REFLECTION_FAILED);
      }

      return null;
   }


}
