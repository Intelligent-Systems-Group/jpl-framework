package de.upb.cs.is.jpl.api.metric;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.metric.kendallstau.KendallsTau;
import de.upb.cs.is.jpl.api.metric.kendallstau.KendallsTauTest;
import de.upb.cs.is.jpl.api.metric.spearmancorrelation.SpearmansCorrelation;
import de.upb.cs.is.jpl.api.metric.spearmancorrelation.SpearmansCorrelationTest;


/**
 * This class contains the ranking utils providing the list of expected and predicted rankings for
 * the {@link KendallsTauTest} and {@link SpearmansCorrelationTest}.
 * 
 * @author Pritha Gupta
 *
 */
public class RankingUtils {
   /**
    * the list of expected rankings for the {@link SpearmansCorrelation} and {@link KendallsTau}
    * tests.
    */
   public static List<int[]> expectedRankingObjects = Arrays.asList(new int[] { 4, 1, 2, 5, 3, 6, 7 }, new int[] { 2, 1, 3, 4, 5, 6, 7 },
         new int[] { 6, 1, 5, 2, 3, 4, 7 }, new int[] { 7, 2, 4, 6, 1, 3, 5 }, new int[] { 3, 2, 1, 6, 4, 7, 5 },
         new int[] { 5, 7, 6, 2, 1, 4, 3 }, new int[] { 7, 5, 4, 3, 2, 1, 6 }, new int[] { 4, 3, 1, 2, 5, 7, 6 },
         new int[] { 6, 7, 1, 3, 5, 2, 4 }, new int[] { 5, 7, 1, 6, 3, 2, 4 }, new int[] { 5, 3, 6, 7, 2, 4, 1 },
         new int[] { 5, 4, 6, 7, 1, 3, 2 }, new int[] { 7, 5, 3, 4, 2, 1, 6 }, new int[] { 2, 3, 1, 7, 4, 6, 5 },
         new int[] { 7, 6, 5, 3, 4, 2, 1 }, new int[] { 3, 4, 1, 6, 5, 7, 2 }, new int[] { 5, 6, 7, 1, 3, 2, 4 },
         new int[] { 3, 4, 1, 2, 6, 5, 7 }, new int[] { 5, 1, 2, 4, 3, 6, 7 }, new int[] { 6, 4, 7, 2, 5, 3, 1 },
         new int[] { 5, 3, 2, 1, 4, 6, 7 }, new int[] { 2, 1, 3, 5, 6, 4, 7 }, new int[] { 4, 6, 2, 5, 7, 1, 3 },
         new int[] { 7, 6, 4, 2, 3, 5, 1 }, new int[] { 5, 4, 3, 1, 7, 6, 2 }, new int[] { 1, 3, 7, 2, 5, 6, 4 },
         new int[] { 2, 1, 7, 4, 6, 5, 3 }, new int[] { 2, 4, 7, 1, 5, 3, 6 }, new int[] { 2, 4, 5, 6, 3, 1, 7 },
         new int[] { 6, 3, 5, 4, 1, 7, 2 }, new int[] { 3, 4, 5, 7, 2, 1, 6 }, new int[] { 4, 5, 3, 7, 1, 2, 6 },
         new int[] { 6, 7, 5, 2, 1, 4, 3 }, new int[] { 5, 3, 1, 2, 6, 7, 4 }, new int[] { 6, 7, 5, 3, 2, 1, 4 },
         new int[] { 2, 4, 6, 7, 1, 3, 5 }, new int[] { 4, 6, 2, 3, 7, 5, 1 }, new int[] { 5, 7, 2, 1, 6, 4, 3 },
         new int[] { 4, 3, 1, 5, 7, 6, 2 }, new int[] { 1, 3, 7, 4, 2, 5, 6 }, new int[] { 3, 6, 4, 5, 1, 7, 2 },
         new int[] { 4, 5, 2, 3, 7, 6, 1 }, new int[] { 5, 4, 6, 7, 1, 3, 2 }, new int[] { 2, 4, 3, 1, 6, 7, 5 },
         new int[] { 7, 6, 4, 3, 5, 2, 1 }, new int[] { 7, 6, 3, 1, 2, 4, 5 }, new int[] { 5, 3, 1, 2, 7, 6, 4 },
         new int[] { 5, 4, 7, 1, 3, 2, 6 }, new int[] { 4, 5, 6, 7, 3, 1, 2 }, new int[] { 2, 1, 5, 3, 4, 7, 6 },
         new int[] { 6, 7, 4, 5, 2, 3, 1 }, new int[] { 2, 3, 1, 7, 5, 4, 6 }, new int[] { 5, 4, 7, 6, 1, 2, 3 },
         new int[] { 6, 7, 3, 4, 5, 2, 1 }, new int[] { 5, 4, 7, 3, 1, 2, 6 }, new int[] { 3, 5, 4, 7, 6, 2, 1 },
         new int[] { 5, 6, 3, 2, 7, 1, 4 }, new int[] { 1, 2, 4, 5, 7, 6, 3 }, new int[] { 4, 5, 6, 7, 3, 2, 1 },
         new int[] { 5, 6, 3, 2, 4, 7, 1 }, new int[] { 3, 5, 2, 6, 4, 7, 1 }, new int[] { 1, 4, 2, 7, 3, 5, 6 },
         new int[] { 6, 1, 2, 5, 7, 3, 4 }, new int[] { 5, 6, 7, 4, 1, 3, 2 }, new int[] { 3, 2, 7, 4, 1, 6, 5 },
         new int[] { 1, 6, 2, 7, 4, 5, 3 }, new int[] { 3, 7, 1, 6, 4, 5, 2 }, new int[] { 2, 5, 1, 4, 6, 7, 3 },
         new int[] { 4, 7, 5, 1, 6, 2, 3 }, new int[] { 5, 6, 3, 2, 7, 4, 1 }, new int[] { 4, 5, 1, 7, 3, 2, 6 },
         new int[] { 4, 6, 5, 2, 7, 3, 1 }, new int[] { 4, 6, 3, 5, 2, 7, 1 }, new int[] { 1, 2, 5, 7, 4, 6, 3 },
         new int[] { 5, 7, 2, 4, 6, 3, 1 }, new int[] { 3, 7, 2, 4, 6, 5, 1 });
   /**
    * the list of actual rankings for the {@link SpearmansCorrelation} and {@link KendallsTau}
    * tests.
    */
   public static List<int[]> actualRankingObjects = Arrays.asList(new int[] { 3, 1, 6, 2, 5, 4, 7 }, new int[] { 4, 2, 5, 7, 3, 1, 6 },
         new int[] { 5, 1, 6, 4, 3, 2, 7 }, new int[] { 2, 4, 5, 7, 6, 3, 1 }, new int[] { 1, 2, 3, 6, 5, 4, 7 },
         new int[] { 4, 6, 3, 1, 7, 5, 2 }, new int[] { 4, 5, 7, 2, 6, 1, 3 }, new int[] { 1, 4, 3, 2, 6, 7, 5 },
         new int[] { 5, 6, 3, 4, 7, 2, 1 }, new int[] { 7, 4, 6, 3, 5, 2, 1 }, new int[] { 1, 5, 4, 7, 6, 2, 3 },
         new int[] { 1, 3, 2, 4, 6, 5, 7 }, new int[] { 6, 4, 5, 7, 3, 1, 2 }, new int[] { 2, 1, 3, 6, 4, 5, 7 },
         new int[] { 7, 5, 6, 1, 4, 2, 3 }, new int[] { 2, 1, 3, 6, 4, 5, 7 }, new int[] { 5, 4, 6, 7, 3, 1, 2 },
         new int[] { 1, 3, 2, 4, 5, 7, 6 }, new int[] { 4, 3, 2, 1, 5, 6, 7 }, new int[] { 2, 1, 5, 6, 3, 4, 7 },
         new int[] { 6, 4, 5, 2, 3, 1, 7 }, new int[] { 3, 1, 2, 4, 5, 6, 7 }, new int[] { 6, 7, 5, 2, 4, 3, 1 },
         new int[] { 6, 3, 7, 4, 2, 1, 5 }, new int[] { 6, 7, 5, 3, 4, 2, 1 }, new int[] { 4, 1, 3, 2, 5, 6, 7 },
         new int[] { 1, 2, 3, 6, 4, 5, 7 }, new int[] { 5, 7, 3, 2, 6, 4, 1 }, new int[] { 1, 2, 5, 7, 4, 3, 6 },
         new int[] { 7, 5, 4, 6, 3, 2, 1 }, new int[] { 4, 1, 5, 6, 2, 3, 7 }, new int[] { 5, 1, 4, 7, 2, 3, 6 },
         new int[] { 6, 7, 4, 2, 5, 3, 1 }, new int[] { 3, 5, 2, 1, 7, 6, 4 }, new int[] { 6, 7, 4, 2, 5, 3, 1 },
         new int[] { 5, 6, 3, 7, 4, 1, 2 }, new int[] { 7, 6, 4, 5, 3, 2, 1 }, new int[] { 2, 4, 3, 7, 6, 5, 1 },
         new int[] { 6, 7, 4, 3, 5, 2, 1 }, new int[] { 2, 1, 4, 7, 3, 5, 6 }, new int[] { 6, 7, 3, 1, 5, 4, 2 },
         new int[] { 6, 7, 4, 2, 5, 3, 1 }, new int[] { 3, 6, 2, 7, 5, 4, 1 }, new int[] { 4, 5, 2, 7, 6, 3, 1 },
         new int[] { 3, 6, 4, 7, 5, 2, 1 }, new int[] { 4, 6, 5, 7, 3, 2, 1 }, new int[] { 2, 6, 3, 5, 7, 4, 1 },
         new int[] { 3, 7, 4, 2, 6, 5, 1 }, new int[] { 4, 6, 5, 7, 3, 2, 1 }, new int[] { 4, 7, 3, 1, 6, 5, 2 },
         new int[] { 3, 7, 4, 6, 5, 2, 1 }, new int[] { 5, 7, 1, 6, 4, 3, 2 }, new int[] { 6, 7, 5, 4, 3, 2, 1 },
         new int[] { 2, 4, 3, 7, 6, 5, 1 }, new int[] { 2, 7, 4, 5, 6, 3, 1 }, new int[] { 4, 6, 2, 7, 5, 3, 1 },
         new int[] { 6, 7, 4, 3, 5, 2, 1 }, new int[] { 1, 2, 3, 5, 4, 7, 6 }, new int[] { 6, 4, 5, 7, 3, 1, 2 },
         new int[] { 3, 6, 5, 7, 4, 2, 1 }, new int[] { 5, 6, 4, 7, 3, 2, 1 }, new int[] { 1, 4, 3, 7, 5, 6, 2 },
         new int[] { 6, 7, 5, 4, 3, 2, 1 }, new int[] { 5, 4, 6, 7, 1, 2, 3 }, new int[] { 6, 7, 5, 4, 3, 2, 1 },
         new int[] { 1, 3, 2, 7, 4, 6, 5 }, new int[] { 5, 6, 4, 7, 3, 2, 1 }, new int[] { 3, 6, 2, 7, 4, 5, 1 },
         new int[] { 4, 6, 5, 7, 3, 2, 1 }, new int[] { 6, 7, 4, 3, 5, 2, 1 }, new int[] { 5, 6, 2, 7, 3, 4, 1 },
         new int[] { 3, 7, 2, 4, 5, 6, 1 }, new int[] { 5, 6, 4, 7, 2, 3, 1 }, new int[] { 1, 4, 3, 7, 5, 6, 2 },
         new int[] { 3, 6, 2, 7, 5, 4, 1 }, new int[] { 4, 6, 2, 7, 3, 5, 1 });


   /**
    * Returns rankings by transforming the given object into full ranking lists.
    *
    * @param rankingObjects the actual ranking objects
    * @return the rankings form objects
    */
   public static List<Ranking> getRankingsFormObjects(List<int[]> rankingObjects) {
      List<Ranking> rankings = new ArrayList<>();

      for (int[] rankingObject : rankingObjects) {
         rankings.add(new Ranking(rankingObject, Ranking.createCompareOperatorArrayForLabels(rankingObject)));
      }
      return rankings;
   }
}
