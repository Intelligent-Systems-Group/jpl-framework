package de.upb.cs.is.jpl.api.dataset.instanceranking;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetTest;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * Unit tests for {@link InstanceRankingDataset}.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingDatasetTest extends ADatasetTest<double[], NullType, Integer> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "instanceranking" + File.separator;


   /**
    * Sets the resource path for this test level.
    */
   public InstanceRankingDatasetTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   protected IDataset<double[], NullType, Integer> getDataset() {
      return new InstanceRankingDataset();
   }


   @Override
   protected List<IInstance<double[], NullType, Integer>> getValidInstances() {
      List<IInstance<double[], NullType, Integer>> validInstances = new ArrayList<>();

      for (int i = 1; i <= InstanceRankingDatasetParserTest.TESTDATA_INSTANCEARRAY.length; i++) {

         validInstances.add(new InstanceRankingInstance(i, InstanceRankingDatasetParserTest.TESTDATA_INSTANCEARRAY[i - 1],
               InstanceRankingDatasetParserTest.TESTDATA_RATINGARRAY[i - 1]));

      }
      return validInstances;
   }

}
