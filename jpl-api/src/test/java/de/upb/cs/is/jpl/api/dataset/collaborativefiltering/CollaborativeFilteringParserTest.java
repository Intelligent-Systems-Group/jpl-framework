package de.upb.cs.is.jpl.api.dataset.collaborativefiltering;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import de.upb.cs.is.jpl.api.util.TestUtils;


/**
 * Tests for {@link CollaborativeFilteringParser}
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringParserTest extends ADatasetParserTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "collaborativefiltering" + File.separator;

   private static final String SIMPLE_DATASET_GPRF = "simpleDataset.gprf";
   private static final String MOVIELENS_DATASET_GPRF = "movielens.gprf";
   private static final String INVALID_DATASET_GPRF = "relativeDataset.gprf";

   private static final String NO_VALIDATION_DEFINED = "No validation defined.";


   /**
    * Creates the test.
    */
   public CollaborativeFilteringParserTest() {
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
      return new CollaborativeFilteringParser();
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
      result.add(new DatasetFile(new File(getTestRessourcePathFor(SIMPLE_DATASET_GPRF))));
      result.add(new DatasetFile(new File(getTestRessourcePathFor(MOVIELENS_DATASET_GPRF))));
      return result;
   }


   @Override
   protected void validateDataset(int testNumber, IDataset<?, ?, ?> parseResult) {
      assertTrue(parseResult instanceof CollaborativeFilteringDataset);
      CollaborativeFilteringDataset dataset = (CollaborativeFilteringDataset) parseResult;
      switch (testNumber) {
         case 0:
            validateSimpleDataset(dataset);
            break;
         case 1:
            validateMovieLensDataset(dataset);
            break;
         default:
            Assert.fail(NO_VALIDATION_DEFINED);
            break;
      }


   }


   /**
    * Validate the parsed Movielens dataset.
    * 
    * @param dataset the parse result which is validated
    */
   private void validateMovieLensDataset(CollaborativeFilteringDataset dataset) {
      assertTrue(dataset != null);
      assertEquals(100000, dataset.getNumberOfInstances());
      // Too much to validate, it's enough that it is successfully parsed

   }


   /**
    * Validate the parsed simple dataset.
    * 
    * @param dataset the parse result which is validated
    */
   private void validateSimpleDataset(CollaborativeFilteringDataset dataset) {
      double[][] expectedRatings = { { 5, 3, 0, 1 }, { 4, 0, 0, 1 }, { 1, 1, 0, 5 }, { 1, 0, 0, 4 }, { 0, 1, 5, 4 } };
      for (int i = 0; i < expectedRatings.length; i++) {
         for (int j = 0; j < expectedRatings[i].length; j++) {
            CollaborativeFilteringInstance instance = dataset.getInstance(i, j);
            assertEquals(j, instance.getItemId());
            assertEquals(i, instance.getContextId().intValue());
            assertEquals(expectedRatings[i][j], instance.getRating(), TestUtils.DOUBLE_DELTA);
         }
      }

   }

}
