package de.upb.cs.is.jpl.api.dataset.multilabelclassification;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetTest;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This class is responsible for testing the {@link MultilabelClassificationDataset}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationDatasetTest extends ADatasetTest<double[], NullType, SparseDoubleVector> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "multilabelclassification" + File.separator;

   private double[][] correctFeatureVectors = { { 1, 5, 4, 3 }, { 3, 3, 66, 8 }, { 1.5, 8, 7.5, -1.2 } };
   private double[][] correctTargetVectors = { { 0, 0, 1 }, { 1, 0, 0 }, { 1, 1, 1 } };


   /**
    * Creates a {@link MultilabelClassificationDatasetTest}.
    */
   public MultilabelClassificationDatasetTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IDataset<double[], NullType, SparseDoubleVector> getDataset() {
      return new MultilabelClassificationDataset();
   }


   @Override
   protected List<IInstance<double[], NullType, SparseDoubleVector>> getValidInstances() {
      List<IInstance<double[], NullType, SparseDoubleVector>> validInstances = new ArrayList<>();
      for (int i = 0; i < correctFeatureVectors.length; i++) {
         MultilabelClassificationInstance instance = new MultilabelClassificationInstance(correctFeatureVectors[i],
               correctTargetVectors[i]);
         validInstances.add(instance);
      }
      return validInstances;
   }

}
