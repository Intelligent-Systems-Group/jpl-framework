package de.upb.cs.is.jpl.api.dataset.defaultdataset.relative;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetTest;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;


/**
 * Tests for {@link DefaultRelativeDataset}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class DefaultRelativeDatasetTest extends ADatasetTest<double[], List<double[]>, Ranking> {


   private static final String RESOURCE_DIRECTORY_LEVEL = "defaultdataset" + File.separator;

   protected DefaultRelativeDataset dataset;


   /**
    * Sets the resource path for this test level
    */
   public DefaultRelativeDatasetTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected DefaultRelativeDataset getDataset() {
      dataset = new DefaultRelativeDataset();
      return dataset;
   }


   @Override
   protected List<IInstance<double[], List<double[]>, Ranking>> getValidInstances() {
      List<IInstance<double[], List<double[]>, Ranking>> validInstances = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
         int[] objectList = { i, i + 1, i + 2 };
         int[] compareOperators = { Ranking.COMPARABLE_ENCODING, Ranking.COMPARABLE_ENCODING };
         validInstances.add(new DefaultInstance<Ranking>(0, new Ranking(objectList, compareOperators), dataset));
      }
      return validInstances;
   }


}
