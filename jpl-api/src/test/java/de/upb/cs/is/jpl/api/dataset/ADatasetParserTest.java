package de.upb.cs.is.jpl.api.dataset;


import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Test;

import de.upb.cs.is.jpl.api.common.AUnitTest;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;


/**
 * Tests for dataset parsers which extend {@link IDatasetParser} .
 * 
 * @author Sebastian Osterbrink
 *
 */
public abstract class ADatasetParserTest extends AUnitTest {


   private static final String RESOURCE_DIRECTORY_LEVEL = "dataset" + File.separator;
   private static final String PARSING_FAILED_EXCEPTION_EXPECTED_BUT_NOT_FOUND = ParsingFailedException.class.getSimpleName()
         + " expected, but not found";


   /**
    * Sets the resource path for this test level
    * 
    * @param additionalResourcePath the folder level for datasets
    */
   public ADatasetParserTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL, additionalResourcePath);
   }


   /**
    * Parses an invalid Dataset and validates the results.
    */
   @Test
   public void testParseInvalidDataset() {
      List<DatasetFile> datasets = getInvalidDatasets();
      for (int i = 0; i < datasets.size(); i++) {
         try {
            DatasetFile datasetFile = datasets.get(i);
            getDatasetParser().parse(datasetFile);
            fail(PARSING_FAILED_EXCEPTION_EXPECTED_BUT_NOT_FOUND);
         } catch (ParsingFailedException e) {
            // This exception is expected
         }
      }
   }


   /**
    * Parses an valid dataset and validates the results.
    * 
    * @throws ParsingFailedException if an unexpected error occurs
    */
   @Test
   public void testParseValidDataset() throws ParsingFailedException {
      List<DatasetFile> datasets = getValidDatasets();
      for (int i = 0; i < datasets.size(); i++) {
         DatasetFile datasetFile = datasets.get(i);
         IDatasetParser parser = getDatasetParser();
         parser.parse(datasetFile);
         validateDataset(i, parser.getDataset());
      }
   }


   /**
    * Provides a {@link List} of invalid {@link DatasetFile}.
    * 
    * @return a {@link List} of {@link DatasetFile}s which contain invalid datasets for the tested
    *         {@link IDatasetParser}
    */
   protected abstract List<DatasetFile> getInvalidDatasets();


   /**
    * Provides a {@link List} of valid {@link DatasetFile}.
    * 
    * @return a {@link List} of {@link DatasetFile}s which contain valid datasets for the tested
    *         {@link IDatasetParser}
    */
   protected abstract List<DatasetFile> getValidDatasets();


   /**
    * Validate that the the given dataset is the expected dataset. This methods validates that the
    * content of the file described by the {@link DatasetFile} was parsed correctly and all expected
    * information is included in the {@link IDataset}.
    * 
    * @param datasetNumber the position of the datasetFile in the {@link List} provided by
    *           {@link ADatasetParserTest#getValidDatasets()}
    * @param parseResult the result of the parse operation on the valid dataset
    */
   protected abstract void validateDataset(int datasetNumber, IDataset<?, ?, ?> parseResult);


   /**
    * Returns the dataset parser which is to be tested by this class.
    * 
    * @return the dataset parser object for this test class
    */
   protected abstract IDatasetParser getDatasetParser();


}
