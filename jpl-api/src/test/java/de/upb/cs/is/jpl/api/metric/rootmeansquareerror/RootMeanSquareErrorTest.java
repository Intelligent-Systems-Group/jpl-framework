package de.upb.cs.is.jpl.api.metric.rootmeansquareerror;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.metric.ANonDecomposableMetricTest;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.metric.meanabsoluteerror.MeanAbsoluteError;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class tests the {@link MeanAbsoluteError}.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class RootMeanSquareErrorTest extends ANonDecomposableMetricTest<Double, Double> {
   private static final String RESOURCE_DIRECTORY_LEVEL = "rootmeansquarederror" + File.separator;
   private static final String UNEQUAL_LIST_LENGTH_REFLECTION_VARIABLE = "ERROR_UNEQUAL_LIST_LENGTH";
   private static final String ERROR_REFLECTION_FAILED = "Test can not run because reflection failed";

   private static final Logger logger = LoggerFactory.getLogger(RootMeanSquareErrorTest.class);


   /**
    * Creates a new {@link RootMeanSquareErrorTest}
    */
   public RootMeanSquareErrorTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IMetric<Double, Double> getEvaluationMetric() {
      return new RootMeanSquareError();
   }


   @Override
   public List<Pair<Pair<List<Double>, List<Double>>, Double>> getCorrectListofPairsListOfRatings() {
      List<Pair<Pair<List<Double>, List<Double>>, Double>> correctListOfPairs = new ArrayList<>();
      List<Double> expectationList = new ArrayList<>();
      List<Double> predictionList = new ArrayList<>();
      double[][] pairs = { { 1, 2 }, { 3, 5 }, { 6, 9 }, { 10, 14 } };
      double result = 2.7386127;
      for (double[] pair : pairs) {
         expectationList.add(pair[0]);
         predictionList.add(pair[1]);

      }
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
