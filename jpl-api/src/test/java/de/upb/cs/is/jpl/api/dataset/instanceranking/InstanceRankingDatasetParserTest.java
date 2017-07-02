package de.upb.cs.is.jpl.api.dataset.instanceranking;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;

import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
import de.upb.cs.is.jpl.api.dataset.ADatasetParserTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.util.TestUtils;


/**
 * Unit tests for {@link InstanceRankingDatasetParser}.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingDatasetParserTest extends ADatasetParserTest {

   private static final String ERROR_WRONG_FEATURES = "The features of content %s should be %s but is %s.";
   private static final String ERROR_WRONG_RATING = "The rating of content %s should be %s but is %s.";

   private static final String RESOURCE_DIRECTORY_LEVEL = "instanceranking" + File.separator;


   /**
    * Sets the resource path for this test level
    */
   public InstanceRankingDatasetParserTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }

   /**
    * Array of context vectors which are equal to the one in the instancerankingtest.gprf.
    */
   public static final double[][] TESTDATA_INSTANCEARRAY = { { 8.3, 4.3, 6.7, 7.1, 5.3 }, { 3.2, 8.3, 5.6, 6.4, 3.4 },
         { 4.4, 8.6, 6.3, 5.2, 2.5 }, { 4.8, 7.4, 5.8, 5.5, 2.8 }, { 7.6, 6.2, 7.4, 8.2, 7.3 }, { 3.4, 7.4, 6.4, 5.5, 7.9 },
         { 3.3, 7.2, 6.2, 5.3, 7.7 }, { 7.8, 6.8, 7.2, 7.4, 8.3 }, { 3.5, 3.6, 4.2, 5.7, 5.1 }, { 2.8, 3.3, 4.6, 4.2, 3.7 } };
   /**
    * Ratings for the instances defined at
    * {@link InstanceRankingDatasetParserTest#TESTDATA_INSTANCEARRAY}.
    */
   public static final int[] TESTDATA_RATINGARRAY = { 5, 4, 3, 4, 5, 2, 2, 4, 1, 1 };


   /**
    * Initialize the {@link LoggingConfiguration}.
    */
   @Before
   public void init() {
      LoggingConfiguration.setupLoggingConfiguration();

   }


   @Override
   protected IDatasetParser getDatasetParser() {
      return new InstanceRankingDatasetParser();
   }


   @Override
   protected List<DatasetFile> getInvalidDatasets() {

      List<DatasetFile> result = new ArrayList<DatasetFile>();
      result.add(new DatasetFile(new File(getTestRessourcePathFor("incorrectfilecontent.gprf"))));
      result.add(new DatasetFile(new File(getTestRessourcePathFor("renamedpdf.gprf"))));

      return result;
   }


   @Override
   protected List<DatasetFile> getValidDatasets() {

      List<DatasetFile> result = new ArrayList<DatasetFile>();
      result.add(new DatasetFile(new File(getTestRessourcePathFor("instancerankingtest.gprf"))));
      result.add(new DatasetFile(new File(getTestRessourcePathFor("instancemovielens.gprf"))));

      return result;
   }


   @Override
   protected void validateDataset(int datasetNumber, IDataset<?, ?, ?> parseResult) {
      // Only one dataset is tested here therefore no need to use datasetNumber

      InstanceRankingDataset instanceDataset = (InstanceRankingDataset) parseResult;
      if (datasetNumber == 1) {
         for (int i = 100; i < TESTDATA_INSTANCEARRAY.length; i++) {
            // Test standard functions
            InstanceRankingInstance instance = (InstanceRankingInstance) instanceDataset.getInstance(i);

            Assert.assertArrayEquals(
                  String.format(ERROR_WRONG_FEATURES, "" + i, Arrays.toString(TESTDATA_INSTANCEARRAY[i]),
                        Arrays.toString(instance.getContextFeatureVector())),
                  TESTDATA_INSTANCEARRAY[i], instance.getContextFeatureVector(), TestUtils.DOUBLE_DELTA);
            Assert.assertEquals(String.format(ERROR_WRONG_RATING, "" + i, TESTDATA_RATINGARRAY[i], instance.getRating()),
                  TESTDATA_RATINGARRAY[i], instance.getRating(), TestUtils.DOUBLE_DELTA);


            // Test custom functions
            Assert.assertArrayEquals(
                  String.format(ERROR_WRONG_FEATURES, "" + i, Arrays.toString(TESTDATA_INSTANCEARRAY[i]),
                        Arrays.toString(instanceDataset.getContextFeatureList().get(i))),
                  TESTDATA_INSTANCEARRAY[i], instanceDataset.getContextFeatureList().get(i), TestUtils.DOUBLE_DELTA);
            Assert.assertEquals(String.format(ERROR_WRONG_RATING, "" + i, TESTDATA_RATINGARRAY[i], instanceDataset.getRatingList().get(i)),
                  TESTDATA_RATINGARRAY[i], instanceDataset.getRatingList().get(i), TestUtils.DOUBLE_DELTA);

         }
      }

   }


}
