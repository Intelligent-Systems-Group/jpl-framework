package de.upb.cs.is.jpl.api.dataset.defaultdataset.relative;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;

import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
import de.upb.cs.is.jpl.api.dataset.ADatasetParserTest;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDatasetParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;


/**
 * Tests for {@link DefaultRelativeDatasetParser}.
 * 
 * @author Sebastian Osterbrink
 * 
 */
public class DefaultRelativeDatasetParserTest extends ADatasetParserTest {

   private static final String CHECK_CORRECT_CLASS_OF_THE_PARSED_DATASET = "Check correct class of the parsed dataset.";
   private static final String RELATIVE_DATASET_GPRF = "relativeDataset.gprf";
   private static final String INVALID_DATASET_GPRF = "invalidDataset.gprf";
   private static final String VERIFY_CONTEXT_ID = "Verify Context ID:";
   private static final String COMPARE_OBJECTS = "Compare Objects in the Ranking:";
   private static final String COMPARE_OPERATORS = "Compare Operators in the Ranking:";
   private static final String ASSERT_THAT_ALL_INSTANCES_WERE_READ = "Assert that all instances were read:";

   private static final String RESOURCE_DIRECTORY_LEVEL = "defaultdataset" + File.separator;


   /**
    * Sets the resource path for this test level
    */
   public DefaultRelativeDatasetParserTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Initialize the {@link LoggingConfiguration}.
    */
   @Before
   public void init() {
      LoggingConfiguration.setupLoggingConfiguration();

   }


   @Override
   protected IDatasetParser getDatasetParser() {
      return new DefaultRelativeDatasetParser();
   }


   @Override
   protected List<DatasetFile> getInvalidDatasets() {
      List<DatasetFile> result = new ArrayList<DatasetFile>();
      result.add(new DatasetFile(new File(getTestRessourcePathFor(INVALID_DATASET_GPRF))));
      return result;
   }


   @Override
   protected List<DatasetFile> getValidDatasets() {
      List<DatasetFile> result = new ArrayList<DatasetFile>();
      result.add(new DatasetFile(new File(getTestRessourcePathFor(RELATIVE_DATASET_GPRF))));
      return result;
   }


   @Override
   protected void validateDataset(int datasetNumber, IDataset<?, ?, ?> parseResult) {
      Ranking ranking = null;
      DefaultInstance<Ranking> instance;

      Assert.assertTrue(CHECK_CORRECT_CLASS_OF_THE_PARSED_DATASET, parseResult instanceof DefaultRelativeDataset);
      DefaultRelativeDataset defaultDataset = (DefaultRelativeDataset) parseResult;

      Assert.assertEquals(ASSERT_THAT_ALL_INSTANCES_WERE_READ, 4, defaultDataset.getNumberOfInstances());

      int[][] expectedOperators = { { Ranking.COMPARABLE_ENCODING, Ranking.COMPARABLE_ENCODING },
            { Ranking.COMPARABLE_ENCODING * 10 + Ranking.OPENING_BRACKETS_ENCODING, Ranking.COMPARABLE_ENCODING,
                  Ranking.CLOSING_BRACKETS_ENCODING },
            { Ranking.COMPARABLE_ENCODING * 10 + Ranking.OPENING_BRACKETS_ENCODING, Ranking.EQUALS_ENCODING,
                  Ranking.CLOSING_BRACKETS_ENCODING },
            { Ranking.OPENING_BRACKETS_ENCODING, Ranking.NOT_COMPARABLE_ENCODING,
                  Ranking.CLOSING_BRACKETS_ENCODING * 10 + Ranking.COMPARABLE_ENCODING } };
      int[] expectedObjects = { 0, 1, 2 };
      Assert.assertEquals(expectedOperators.length, defaultDataset.getNumberOfInstances());
      for (int i = 0; i < defaultDataset.getNumberOfInstances(); i++) {
         instance = defaultDataset.getInstance(i);
         Assert.assertEquals(VERIFY_CONTEXT_ID, i, instance.getContextId().intValue());
         ranking = instance.getRating();
         Assert.assertArrayEquals(COMPARE_OBJECTS, expectedObjects, ranking.getObjectList());
         Assert.assertArrayEquals(COMPARE_OPERATORS, expectedOperators[i], ranking.getCompareOperators());
      }


   }


}
