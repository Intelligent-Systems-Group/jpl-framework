package de.upb.cs.is.jpl.api.dataset.collaborativefiltering;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.upb.cs.is.jpl.api.dataset.ADatasetTest;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;


/**
 * Tests for {@link CollaborativeFilteringDataset}
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringDatasetTest extends ADatasetTest<IVector, IVector, Double> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "collaborativefiltering" + File.separator;


   /**
    * Sets the resource path for this test level
    */
   public CollaborativeFilteringDatasetTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   protected CollaborativeFilteringDataset dataset;


   @Override
   protected CollaborativeFilteringDataset getDataset() {
      dataset = new CollaborativeFilteringDataset(10, 10);
      for (int i = 0; i < 10; i++) {
         dataset.setItemVector(i, new SparseDoubleVector(1));
         dataset.setContextVector(i, new SparseDoubleVector(1));
      }
      return dataset;
   }


   @Override
   protected List<IInstance<IVector, IVector, Double>> getValidInstances() {
      List<IInstance<IVector, IVector, Double>> validInstances = new ArrayList<>();
      RandomGenerator.initializeRNG(1234);
      Random random = RandomGenerator.getRNG();
      for (int i = 0; i < 10; i++) {
         int itemId = random.nextInt(dataset.getNumberOfItems());
         int contextId = random.nextInt(dataset.getNumberOfContexts());
         System.out.println(itemId + " " + contextId);
         Double rating = random.nextInt(5) + 1.0;
         validInstances.add(new CollaborativeFilteringInstance(contextId, itemId, rating, dataset));
      }
      return validInstances;
   }

}
