package de.upb.cs.is.jpl.api.metric.kendallstau;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.metric.ADecomposableMetricTest;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.metric.RankingUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class contains unit test for validating the implementation of the {@link KendallsTau}.
 * 
 * @author Pritha Gupta
 *
 */
public class KendallsTauTest extends ADecomposableMetricTest<Ranking, Double> {
   private static final String RESOURCE_DIRECTORY_LEVEL = "kendalls" + File.separator;

   private static final String REFLECTION_EXCEPTION_MESSAGE_PREDICTED_RANK_IS_GREATER = "EXCEPTION_MESSAGE_PREDICTED_RANK_IS_GREATER";
   private static final String REFLECTION_EXCEPTION_MESSAGE_PREDICTED_RANK_DOESNOT_CONTAIN_ALL_OBJECTS_IN_TRUE_RANKING = "EXCEPTION_MESSAGE_PREDICTED_RANK_DOESNOT_CONTAIN_ALL_OBJECTS_IN_TRUE_RANKING";


   /**
    * Creates new unit test for {@link KendallsTauTest} with the resource path.
    */
   public KendallsTauTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IMetric<Ranking, Double> getEvaluationMetric() {
      return new KendallsTau();
   }


   @Override
   public List<Pair<Pair<Ranking, Ranking>, Double>> getCorrectListofPairsOfRatings() {

      List<Pair<Pair<Ranking, Ranking>, Double>> corretListOfPairsOfRankings = new ArrayList<>();


      Pair<Ranking, Ranking> firstPairOfRankings = TestUtils.getRankingPair(new int[] { 9, 3, 10, 4, 6, 5, 8, 1, 2, 7 },
            new int[] { 4, 2, 10, 7, 5, 9, 8, 1, 3, 6 });
      corretListOfPairsOfRankings.add(Pair.of(firstPairOfRankings, 0.51111111111));


      Pair<Ranking, Ranking> secondPairOfRankings = TestUtils.getRankingPair(new int[] { 2, 4 }, new int[] { 1, 2, 3, 4 });
      corretListOfPairsOfRankings.add(Pair.of(secondPairOfRankings, 1.0));

      Pair<Ranking, Ranking> thirdPairOfRankings = TestUtils.getRankingPair(new int[] { 4, 2 }, new int[] { 1, 2, 3, 4 });
      corretListOfPairsOfRankings.add(Pair.of(thirdPairOfRankings, 0.6666666666666667));

      Pair<Ranking, Ranking> fourthPairOfRankings = TestUtils.getRankingPair(new int[] { 1, 2, 4, 6, 3, 5, 9, 7, 10, 8 },
            new int[] { 4, 5, 3, 1, 9, 7, 6, 8, 2, 10 });
      corretListOfPairsOfRankings.add(Pair.of(fourthPairOfRankings, 0.022222222222222143));

      Pair<Ranking, Ranking> fifthPairOfRankings = TestUtils.getRankingPair(new int[] { 1, 2, 3, 4 }, new int[] { 1, 2, 3, 4 });
      corretListOfPairsOfRankings.add(Pair.of(fifthPairOfRankings, 1.0));


      // Orderings 2,5,1,6,4,7,3,8,9,10 and 3,6,4,5,1,8,2,10,9,7 for the objects
      // 2,3,8,12,13,39,53,67,89,93
      Pair<Ranking, Ranking> sixthPairOfRankings = TestUtils.getRankingPair(new int[] { 8, 2, 53, 13, 3, 12, 39, 67, 89, 93 },
            new int[] { 13, 53, 2, 8, 12, 3, 93, 39, 89, 67 });
      corretListOfPairsOfRankings.add(Pair.of(sixthPairOfRankings, 0.15555555555555559));

      Pair<Ranking, Ranking> seventhPairOfRankings = TestUtils.getRankingPair(new int[] { 3, 1, 7, 5, 2, 4, 6, 8, 9, 10 },
            new int[] { 5, 7, 1, 3, 4, 2, 10, 6, 9, 8 });
      corretListOfPairsOfRankings.add(Pair.of(seventhPairOfRankings, 0.15555555555555556));

      return corretListOfPairsOfRankings;
   }


   @Override
   public List<Pair<String, Pair<Pair<Ranking, Ranking>, Double>>> getWrongListOfPairsOfRatingsWithExceptionMessage() {
      List<Pair<String, Pair<Pair<Ranking, Ranking>, Double>>> wrongListofPairOfRatingWithExceptionMessage = new ArrayList<>();

      Pair<Ranking, Ranking> firstPairOfRankings = TestUtils.getRankingPair(new int[] { 1, 2, 4, 6, 3, 5, 9, 7, 10, 8 },
            new int[] { 4, 5, 3, 1, 9, 7, 6, 8, 2, 10 });
      Pair<Pair<Ranking, Ranking>, Double> firstPairOfRankingsWithResult = Pair.of(firstPairOfRankings, 0.5);
      String exceptionMessageUnequalLength = StringUtils.EMPTY_STRING;
      String exceptionMessageUnequalObjectNotExistingInPredictedRanking = StringUtils.EMPTY_STRING;

      wrongListofPairOfRatingWithExceptionMessage.add(Pair.of(exceptionMessageUnequalLength, firstPairOfRankingsWithResult));

      Pair<Ranking, Ranking> secondPairOfRankings = TestUtils.getRankingPair(new int[] { 1, 2, 4, 6, 3, 5, 9, 7, 10, 8 },
            new int[] { 4, 5, 3, 1, 9, 7, 6, 8 });

      try {
         exceptionMessageUnequalLength = TestUtils.getStringByReflection(KendallsTau.class,
               REFLECTION_EXCEPTION_MESSAGE_PREDICTED_RANK_IS_GREATER);
         exceptionMessageUnequalObjectNotExistingInPredictedRanking = TestUtils.getStringByReflection(KendallsTau.class,
               REFLECTION_EXCEPTION_MESSAGE_PREDICTED_RANK_DOESNOT_CONTAIN_ALL_OBJECTS_IN_TRUE_RANKING);
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         // The abstract method should be adapted to remove this work around.
      }
      Pair<Pair<Ranking, Ranking>, Double> secondPairOfRankingsWithResult = Pair.of(secondPairOfRankings, 0.5);
      wrongListofPairOfRatingWithExceptionMessage.add(Pair.of(exceptionMessageUnequalLength, secondPairOfRankingsWithResult));
      Pair<Ranking, Ranking> thirdPairOfRankings = TestUtils.getRankingPair(new int[] { 1, 2, 4, 6 }, new int[] { 4, 5, 3, 1, 9, 7, 6, 8 });
      Pair<Pair<Ranking, Ranking>, Double> thirdPairOfRankingsWithResult = Pair.of(thirdPairOfRankings, 0.5);
      wrongListofPairOfRatingWithExceptionMessage
            .add(Pair.of(exceptionMessageUnequalObjectNotExistingInPredictedRanking, thirdPairOfRankingsWithResult));

      return wrongListofPairOfRatingWithExceptionMessage;
   }


   @Override
   public List<Pair<Pair<List<Ranking>, List<Ranking>>, Double>> getCorrectListofPairsListOfRatings() {
      // Compared with python scipy.stats.spearmanr
      List<Pair<Pair<List<Ranking>, List<Ranking>>, Double>> listOfRankings = new ArrayList<>();
      List<Ranking> expectedRanking = RankingUtils.getRankingsFormObjects(RankingUtils.expectedRankingObjects);
      List<Ranking> actualRanking = RankingUtils.getRankingsFormObjects(RankingUtils.actualRankingObjects);
      listOfRankings.add(Pair.of(Pair.of(expectedRanking, actualRanking), 0.26566416040100255));
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
