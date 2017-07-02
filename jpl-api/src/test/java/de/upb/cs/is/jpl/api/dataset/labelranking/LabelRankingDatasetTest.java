package de.upb.cs.is.jpl.api.dataset.labelranking;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetTest;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This class is responsible for testing the {@link LabelRankingDataset}.
 * 
 * @author Andreas Kornelsen
 */
public class LabelRankingDatasetTest extends ADatasetTest<double[], NullType, Ranking> {


   private static final String RESOURCE_DIRECTORY_LEVEL = "labelranking" + File.separator;


   private static final double[][] FEATURES = new double[][] {
         { 0.911, 0.0271, 0.555, 2.07, -0.16, -0.929, -0.673, -1.17, -1.17, -1.14, 0.233, 1.19, 0.174, 1.06, 0.311, -0.127, -0.221, -1.16,
               -0.685, -0.599, -1.06, -0.375, 0.577 },
         { -0.423, -0.514, -0.434, 0.631, -0.167, -0.315, -0.514, -0.514, -0.479, -0.514, -0.0894, -0.322, 0.129, 0.741, 0.0635, -0.155,
               -0.514, -0.514, -0.0562, 0.631, -0.514, 0.0512, -0.514 },
         { -0.246, 0.0369, -0.298, -0.306, -0.165, -0.286, -0.167, -0.141, -0.195, -0.216, -0.15, -0.187, -0.229, -0.0976, -0.306, -0.2,
               -0.282, 0.0132, -0.279, -0.306, -0.306, -0.0376, -0.297 },
         { -1.93, -1.9, 0.41, 0.628, 0.503, 0.425, 0.488, 0.41, 0.581, 0.55, 0.597, 0.581, 0.254, -1.88, 0.254, 0.254, 0.519, -1.88, -1.87,
               0.674, 0.659, 0.425, 0.581 } };
   private static final int[][] RANKINGS = new int[][] { { 3, 1, 2, 4 }, { 4, 1, 3, 2 }, { 1, 2, 3, 4 }, { 1, 4, 2, 3 } };


   /**
    * Instantiates a new label ranking dataset test and sets the resource path for this test level.
    */
   public LabelRankingDatasetTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IDataset<double[], NullType, Ranking> getDataset() {
      return new LabelRankingDataset();
   }


   @Override
   protected List<IInstance<double[], NullType, Ranking>> getValidInstances() {
      List<IInstance<double[], NullType, Ranking>> validInstances = new ArrayList<IInstance<double[], NullType, Ranking>>();

      for (int i = 0; i < RANKINGS.length; i++) {
         LabelRankingInstance labelRankingInstance = new LabelRankingInstance();
         labelRankingInstance.setContextFeatureVector(FEATURES[i]);
         labelRankingInstance.setRating(new Ranking(RANKINGS[i], Ranking.createCompareOperatorArrayForLabels(RANKINGS[i])));
         labelRankingInstance.setContextId(i);
         validInstances.add(labelRankingInstance);
      }
      return validInstances;
   }


}
