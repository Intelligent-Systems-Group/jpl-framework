package de.upb.cs.is.jpl.api.dataset.objectranking;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetTest;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;


/**
 * Test for the {@link ObjectRankingDataset}.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingDatasetTest extends ADatasetTest<double[], List<double[]>, Ranking> {
   private static final String RESOURCE_DIRECTORY_LEVEL = "objectranking" + File.separator;
   private int[][] RANKINGS = new int[][] { { 1, 2, 3, 4, 5 }, { 5, 4, 3, 2, 1 }, { 1, 3, 5, 2, 4 }, { 1, 3, 5, 4, 2 }, { 1, 5, 3, 4, 2 } };
   private double[][] ITEM_FEATURES = new double[][] { { 1.0, 0.0, 6.0, 2.72897800776197, 2.13842173350582, 1.83841991341991, 0.84 },
         { 1.0, 0.0, 3.0, 0.926384364820847, 1.99022801302932, 1.99245867768595, 0.88 },
         { 1.0, 0.0, 1.0, 1.76955903271693, 2.34850640113798, 1.87472451790634, 0.88 },
         { 1.0, 0.0, 5.0, 2.68840082361016, 2.04323953328758, 1.51515151515152, 0.92 },
         { 1.0, 0.0, 8.0, 0.81304347826087, 1.64347826086957, 3.28728191000918, 0.88 } };
   private double[][] CONTEXT_FEATURES = new double[][] { { 10.0 }, { 10.0 }, { 10.0 }, { 10.0 }, { 10.0 } };


   /**
    * Creates a new {@link ObjectRankingDatasetTest} and sets the resource path for this test level.
    */
   public ObjectRankingDatasetTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IDataset<double[], List<double[]>, Ranking> getDataset() {
      return new ObjectRankingDataset();
   }


   @Override
   protected List<IInstance<double[], List<double[]>, Ranking>> getValidInstances() {
      List<IInstance<double[], List<double[]>, Ranking>> validInstances = new ArrayList<>();
      for (int instancePosition = 0; instancePosition < RANKINGS.length; instancePosition++) {
         int[] compareOperators = Ranking.createCompareOperatorArrayForLabels(RANKINGS[instancePosition]);
         Ranking ranking = new Ranking(RANKINGS[instancePosition], compareOperators);
         ObjectRankingInstance objectRankingInstance = new ObjectRankingInstance(CONTEXT_FEATURES[instancePosition],
               Arrays.asList(ITEM_FEATURES), ranking);
         validInstances.add(objectRankingInstance);
      }
      return validInstances;
   }

}
