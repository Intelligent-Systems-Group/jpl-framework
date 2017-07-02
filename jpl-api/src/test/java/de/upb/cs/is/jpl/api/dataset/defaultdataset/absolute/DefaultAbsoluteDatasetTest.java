package de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetTest;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDataset;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * Tests for {@link DefaultAbsoluteDataset}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class DefaultAbsoluteDatasetTest extends ADatasetTest<double[], List<double[]>, IVector> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "defaultdataset" + File.separator;

   protected DefaultAbsoluteDataset dataset;


   /**
    * Sets the resource path for this test level
    */
   public DefaultAbsoluteDatasetTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected DefaultAbsoluteDataset getDataset() {
      if (dataset == null)
         dataset = new DefaultAbsoluteDataset();
      return dataset;
   }


   @Override
   protected List<IInstance<double[], List<double[]>, IVector>> getValidInstances() {
      List<IInstance<double[], List<double[]>, IVector>> validInstances = new ArrayList<IInstance<double[], List<double[]>, IVector>>();
      for (int i = 0; i < 10; i++) {
         IVector vector = new DenseDoubleVector(3);
         vector.fillRandomly();
         validInstances.add(new DefaultInstance<IVector>(i, vector, dataset));
      }
      return validInstances;
   }


}
