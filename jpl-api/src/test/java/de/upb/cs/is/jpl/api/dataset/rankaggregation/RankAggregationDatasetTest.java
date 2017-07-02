package de.upb.cs.is.jpl.api.dataset.rankaggregation;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetTest;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * Tests for the dataset {@link RankAggregationDataset}.
 */
public class RankAggregationDatasetTest extends ADatasetTest<Integer, NullType, Ranking> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "rankaggregation" + File.separator;

   private int[] COUNT_RANKINGS = new int[] { 2, 2, 1, 1, 1, 1, 1 };
   private int[][] RANKINGS = new int[][] { { 11, 14, 12, 13, 9, 10, 8, 5, 7, 6, 4, 3, 2, 1 },
         { 11, 14, 12, 13, 9, 10, 7, 8, 5, 6, 4, 3, 2, 1 }, { 11, 14, 12, 13, 9, 10, 5, 7, 8, 6, 4, 3, 2, 1 },
         { 11, 14, 12, 13, 9, 10, 7, 8, 6, 5, 3, 4, 2, 1 }, { 11, 12, 14, 9, 13, 10, 7, 8, 5, 6, 4, 3, 2, 1 },
         { 11, 14, 12, 9, 13, 10, 7, 8, 5, 6, 4, 3, 2, 1 }, { 11, 14, 12, 13, 9, 10, 7, 8, 5, 4, 6, 3, 2, 1 } };


   /**
    * Sets the resource path for this test level.
    * 
    */
   public RankAggregationDatasetTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IDataset<Integer, NullType, Ranking> getDataset() {
      return new RankAggregationDataset();
   }


   @Override
   protected List<IInstance<Integer, NullType, Ranking>> getValidInstances() {
      List<IInstance<Integer, NullType, Ranking>> validInstances = new ArrayList<IInstance<Integer, NullType, Ranking>>();

      for (int i = 0; i < RANKINGS.length; i++) {
         RankAggregationInstance rankAggregationInstance = new RankAggregationInstance();
         rankAggregationInstance.setContextFeatureVector(COUNT_RANKINGS[i]);
         rankAggregationInstance.setRating(new Ranking(RANKINGS[i], Ranking.createCompareOperatorArrayForLabels(RANKINGS[i])));
         rankAggregationInstance.setContextId(i);
         validInstances.add(rankAggregationInstance);
      }
      return validInstances;
   }

}
