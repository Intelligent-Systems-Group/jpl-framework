package de.upb.cs.is.jpl.api.metric.spearmancorrelation;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.metric.ADecomposableMetricTest;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.metric.RankingUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the
 * {@link SpearmansCorrelation}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class SpearmansCorrelationTest extends ADecomposableMetricTest<Ranking, Double> {


   private static final String RESOURCE_DIRECTORY_LEVEL = "spearmancorrelation" + File.separator;
   private static final String REFLECTION_EXCEPTION_MESSAGE_PREDICTED_RANK_DOESNOT_CONTAIN_ALL_OBJECTS_IN_TRUE_RANKING = "EXCEPTION_MESSAGE_PREDICTED_RANK_DOESNOT_CONTAIN_ALL_OBJECTS_IN_TRUE_RANKING";

   private static final String REFLECTION_EXCEPTION_MESSAGE_PREDICTED_RANK_IS_GREATER = "EXCEPTION_MESSAGE_LENGTH_RANKING";


   /**
    * Creates new unit test for {@link SpearmansCorrelationTest} with the resource path.
    */
   public SpearmansCorrelationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IMetric<Ranking, Double> getEvaluationMetric() {
      return new SpearmansCorrelation();
   }


   @Override
   public List<Pair<Pair<Ranking, Ranking>, Double>> getCorrectListofPairsOfRatings() {
      Pair<Ranking, Ranking> secondPairOfPartialRankings = TestUtils.getRankingPair(new int[] { 4, 2 }, new int[] { 1, 2, 3, 4 });
      Pair<Pair<Ranking, Ranking>, Double> secondPairOfRankingWithPartialResult = Pair.of(secondPairOfPartialRankings, -1.0);

      Pair<Ranking, Ranking> completeRankingForFirstPartialRanking = TestUtils.getRankingPair(new int[] { 2, 1 }, new int[] { 1, 2 });
      Pair<Pair<Ranking, Ranking>, Double> completeRankingForFirstPartialRankingWithResult = Pair.of(completeRankingForFirstPartialRanking,
            -1.0);

      Pair<Ranking, Ranking> thirdPairOfPartialRankings = TestUtils.getRankingPair(new int[] { 4, 2, 3 }, new int[] { 1, 2, 3, 4 });
      Pair<Pair<Ranking, Ranking>, Double> thirdPairOfRankingWithPartialResult = Pair.of(thirdPairOfPartialRankings, -0.5);

      Pair<Ranking, Ranking> completeRankingForThirdPartialRanking = TestUtils.getRankingPair(new int[] { 3, 1, 2 }, new int[] { 1, 2, 3 });
      Pair<Pair<Ranking, Ranking>, Double> completeRankingForThirdPartialRankingWithResult = Pair.of(completeRankingForThirdPartialRanking,
            -0.5);


      Pair<Ranking, Ranking> firstPairOfPartialRankings = TestUtils.getRankingPair(new int[] { 2, 4 }, new int[] { 1, 2, 3, 4 });
      Pair<Pair<Ranking, Ranking>, Double> firstPairOfPartialRankingWithResult = Pair.of(firstPairOfPartialRankings, 1.0);


      Pair<Ranking, Ranking> firstPairOfRankings = TestUtils.getRankingPair(new int[] { 1, 2, 4, 6, 3, 5, 9, 7, 10, 8 },
            new int[] { 4, 5, 3, 1, 9, 7, 6, 8, 2, 10 });
      Pair<Pair<Ranking, Ranking>, Double> firstPairOfRankingWithResult = Pair.of(firstPairOfRankings, 0.01818181818182);

      Pair<Ranking, Ranking> secondPairOfRankings = TestUtils.getRankingPair(new int[] { 3, 1, 5, 6, 4, 2 },
            new int[] { 4, 2, 5, 6, 3, 1 });
      Pair<Pair<Ranking, Ranking>, Double> secondPairOfRankingWithResult = Pair.of(secondPairOfRankings, 0.8857142857142857);

      Pair<Ranking, Ranking> thirdPairOfRankings = TestUtils.getRankingPair(new int[] { 5, 4, 3, 2, 1, 10, 9, 8, 7, 6 },
            new int[] { 1, 2, 3, 4, 5, 10, 9, 8, 7, 6 });
      Pair<Pair<Ranking, Ranking>, Double> thirdPairOfRankingWithResult = Pair.of(thirdPairOfRankings, 0.75757575757575768);

      Pair<Ranking, Ranking> fifthPairOfRankings = TestUtils.getRankingPair(new int[] { 8, 2, 53, 13, 3, 12, 39, 67, 89, 93 },
            new int[] { 13, 53, 2, 8, 12, 3, 93, 39, 89, 67 });
      Pair<Pair<Ranking, Ranking>, Double> fifthPairOfRankingWithResult = Pair.of(fifthPairOfRankings, 0.32121212121212128);


      Pair<Ranking, Ranking> fourthPairOfRankings = TestUtils.getRankingPair(new int[] { 3, 1, 7, 5, 2, 4, 6, 8, 9, 10 },
            new int[] { 5, 7, 1, 3, 4, 2, 10, 6, 9, 8 });
      Pair<Pair<Ranking, Ranking>, Double> fourthPairOfRankingWithResult = Pair.of(fourthPairOfRankings, 0.32121212121212128);


      Pair<Ranking, Ranking> sixthPairOfRankings = TestUtils.getRankingPair(new int[] { 9, 3, 10, 4, 6, 5, 8, 1, 2, 7 },
            new int[] { 4, 2, 10, 7, 5, 9, 8, 1, 3, 6 });
      Pair<Pair<Ranking, Ranking>, Double> sixthPairOfRankingWithResult = Pair.of(sixthPairOfRankings, 0.67272727272);


      return Arrays.asList(sixthPairOfRankingWithResult, completeRankingForFirstPartialRankingWithResult,
            completeRankingForThirdPartialRankingWithResult, thirdPairOfRankingWithPartialResult, secondPairOfRankingWithPartialResult,
            firstPairOfPartialRankingWithResult, fifthPairOfRankingWithResult, fourthPairOfRankingWithResult, firstPairOfRankingWithResult,
            secondPairOfRankingWithResult, thirdPairOfRankingWithResult);
   }


   @Override
   public List<Pair<String, Pair<Pair<Ranking, Ranking>, Double>>> getWrongListOfPairsOfRatingsWithExceptionMessage() {

      List<Pair<String, Pair<Pair<Ranking, Ranking>, Double>>> wrongListOfRatingsWithExceptionMessage = new ArrayList<>();

      int[] firstRankingObjects = new int[] { 1, 2, 4, 6, 3, 5, 9, 7, 10, 8 };
      int[] firstRankingCompareOberators = Ranking.createCompareOperatorArrayForLabels(firstRankingObjects);
      Ranking firstRanking = new Ranking(firstRankingObjects, firstRankingCompareOberators);

      int[] secondRankingObjects = new int[] { 4, 5, 3, 1, 9, 7, 6, 8, 2 };
      int[] secondRankingCompareOberators = Ranking.createCompareOperatorArrayForLabels(secondRankingObjects);
      Ranking secondRanking = new Ranking(secondRankingObjects, secondRankingCompareOberators);
      String exceptionMessageUnequalLength = StringUtils.EMPTY_STRING;
      String exceptionMessageUnequalObjectNotExistingInPredictedRanking = StringUtils.EMPTY_STRING;

      try {
         exceptionMessageUnequalLength = TestUtils.getStringByReflection(SpearmansCorrelation.class,
               REFLECTION_EXCEPTION_MESSAGE_PREDICTED_RANK_IS_GREATER);
         exceptionMessageUnequalObjectNotExistingInPredictedRanking = TestUtils.getStringByReflection(SpearmansCorrelation.class,
               REFLECTION_EXCEPTION_MESSAGE_PREDICTED_RANK_DOESNOT_CONTAIN_ALL_OBJECTS_IN_TRUE_RANKING);
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         // The abstract method should be adapted to remove this work around.
      }

      wrongListOfRatingsWithExceptionMessage
            .add(Pair.of(exceptionMessageUnequalLength, Pair.of(Pair.of(firstRanking, secondRanking), 0.01818181818182)));


      int[] secondRankingObjectsForSecondPair = new int[] { 4, 5, 3, 1, 9, 7, 6, 8, 2, 10 };
      int[] secondRankingCompareOberatorsForSecondPair = Ranking.createCompareOperatorArrayForLabels(secondRankingObjects);
      Ranking secondRankingForSecondPair = new Ranking(secondRankingObjectsForSecondPair, secondRankingCompareOberatorsForSecondPair);

      wrongListOfRatingsWithExceptionMessage
            .add(Pair.of(StringUtils.EMPTY_STRING, Pair.of(Pair.of(firstRanking, secondRankingForSecondPair), 0.4)));
      Pair<Ranking, Ranking> thirdPairOfRankings = TestUtils.getRankingPair(new int[] { 1, 2, 4, 6 }, new int[] { 4, 5, 3, 1, 9, 7, 6, 8 });
      Pair<Pair<Ranking, Ranking>, Double> thirdPairOfRankingsWithResult = Pair.of(thirdPairOfRankings, 0.5);
      wrongListOfRatingsWithExceptionMessage
            .add(Pair.of(exceptionMessageUnequalObjectNotExistingInPredictedRanking, thirdPairOfRankingsWithResult));

      return wrongListOfRatingsWithExceptionMessage;
   }


   @Override
   public List<Pair<Pair<List<Ranking>, List<Ranking>>, Double>> getCorrectListofPairsListOfRatings() {

      // Compared with python scipy.stats.spearmanr
      List<Pair<Pair<List<Ranking>, List<Ranking>>, Double>> listOfRankings = new ArrayList<>();
      List<Ranking> expectedRanking = RankingUtils.getRankingsFormObjects(RankingUtils.expectedRankingObjects);
      List<Ranking> actualRanking = RankingUtils.getRankingsFormObjects(RankingUtils.actualRankingObjects);
      listOfRankings.add(Pair.of(Pair.of(expectedRanking, actualRanking), 0.34962406015));
      return listOfRankings;
   }


   @Override
   public List<Pair<String, Pair<Pair<List<Ranking>, List<Ranking>>, Double>>> getWrongListOfPairsOfListOfRatingsWithExceptionMessage() {
      // Compared with python scipy.stats.spearmanr
      List<Ranking> expectedRanking = RankingUtils.getRankingsFormObjects(RankingUtils.expectedRankingObjects);
      List<Ranking> actualRanking = RankingUtils.getRankingsFormObjects(RankingUtils.actualRankingObjects);
      List<Pair<String, Pair<Pair<List<Ranking>, List<Ranking>>, Double>>> testPairs = new ArrayList<>();
      testPairs.add(Pair.of(StringUtils.EMPTY_STRING, Pair.of(Pair.of(expectedRanking, actualRanking), 0.5)));
      return testPairs;
   }

}
