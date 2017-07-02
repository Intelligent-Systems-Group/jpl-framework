package de.upb.cs.is.jpl.api.dataset;


import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.upb.cs.is.jpl.api.common.AUnitTest;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;


/**
 * Test template for {@link IDataset} classes. If you create new dataset then for testing that the
 * implementation of the dataset, you need to extend this test.
 * 
 * @author Sebastian Osterbrink
 * 
 * @param <CONTEXT> the type of the context vector
 * @param <ITEM> the type of the item vector inside each instance
 * @param <RATING> the rating type of this instance
 * 
 */
public abstract class ADatasetTest<CONTEXT, ITEM, RATING> extends AUnitTest {


   private static final String TEST_CORRECT_INSTANCES_ARE_RETURNED = "Test that the correct instances are returned.";
   private static final String COULD_RETRIEVE_INVALID_INDEX_INSTANCE = "Could retrieve an instance with an invalid index. Instance %s was returned for index %s.";
   private static final String CORRECT_NUMBER_OF_INSTANCES_WAS_ADDED = "Correct number of instances was added.";
   private static final String DATA_RETRIEVAL_WORKING_CORRECTLY = "Check data retrieval.";
   private static final String EXACTLY_ONE_INSTANCE_WAS_ADDED = "Exactly one instance was added.";

   private static final String RESOURCE_DIRECTORY_LEVEL = "dataset" + File.separator;


   /**
    * Sets the resource path for this test level
    * 
    * @param additionalResourcePath the folder level for datasets
    */
   public ADatasetTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL, additionalResourcePath);
   }


   /**
    * Tests information storage and retrieval.
    * 
    * @throws InvalidInstanceException if an instance could not be added to the dataset.
    */
   @Test
   public void testInstanceRetrieval() throws InvalidInstanceException {
      IDataset<CONTEXT, ITEM, RATING> dataset = getDataset();
      List<IInstance<CONTEXT, ITEM, RATING>> testInstances = getValidInstances();
      for (IInstance<CONTEXT, ITEM, RATING> validInstance : testInstances) {
         int instanceNumber = dataset.getNumberOfInstances();
         dataset.addInstance(validInstance);
         Assert.assertEquals(EXACTLY_ONE_INSTANCE_WAS_ADDED, instanceNumber + 1, dataset.getNumberOfInstances());
         Assert.assertEquals(DATA_RETRIEVAL_WORKING_CORRECTLY, validInstance, dataset.getInstance(instanceNumber));
      }
   }


   /**
    * Tests that invalid indexes will cause an {@link IndexOutOfBoundsException}.
    * 
    * @throws InvalidInstanceException if an instance could not be added to the dataset
    */
   @Test
   public void testGetIvalidInstanceNumber() throws InvalidInstanceException {
      IDataset<CONTEXT, ITEM, RATING> dataset = fillDatasetWithCorrectInstances();
      int[] invalidIndexes = { -1, dataset.getNumberOfInstances(), Integer.MAX_VALUE };
      for (int i = 0; i < invalidIndexes.length; i++) {
         try {
            IInstance<CONTEXT, ITEM, RATING> result = dataset.getInstance(invalidIndexes[i]);
            Assert.fail(String.format(COULD_RETRIEVE_INVALID_INDEX_INSTANCE, result, invalidIndexes[i]));
         } catch (IndexOutOfBoundsException e) {
         }
      }
   }


   /**
    * Test whether the correct instances are returned by {@link IDataset#getPartOfDataset}.
    * 
    * @throws InvalidInstanceException if an instance could not be added to the dataset
    */
   @Test
   public void testGetPartOfDataset() throws InvalidInstanceException {
      IDataset<CONTEXT, ITEM, RATING> dataset = fillDatasetWithCorrectInstances();
      for (int i = 0; i < dataset.getNumberOfInstances() - 1; i++) {
         IDataset<CONTEXT, ITEM, RATING> partialDataset = dataset.getPartOfDataset(i, i + 2);
         Assert.assertEquals(TEST_CORRECT_INSTANCES_ARE_RETURNED, dataset.getInstance(i), partialDataset.getInstance(0));
         Assert.assertEquals(TEST_CORRECT_INSTANCES_ARE_RETURNED, dataset.getInstance(i + 1), partialDataset.getInstance(1));
      }
   }


   /**
    * Fills the dataset with all available valid instances.
    * 
    * @return the filled dataset
    * @throws InvalidInstanceException if an instance could not be added to the dataset
    */
   protected IDataset<CONTEXT, ITEM, RATING> fillDatasetWithCorrectInstances() throws InvalidInstanceException {
      IDataset<CONTEXT, ITEM, RATING> dataset = getDataset();
      List<IInstance<CONTEXT, ITEM, RATING>> testInstances = getValidInstances();
      for (IInstance<CONTEXT, ITEM, RATING> validInstance : testInstances) {
         dataset.addInstance(validInstance);
      }
      Assert.assertEquals(CORRECT_NUMBER_OF_INSTANCES_WAS_ADDED, testInstances.size(), dataset.getNumberOfInstances());
      return dataset;
   }


   /**
    * Provides a dataset object, on which the tests are performed.
    * 
    * @return a new {@link IDataset} object
    */
   protected abstract IDataset<CONTEXT, ITEM, RATING> getDataset();


   /**
    * Provides a {@link List} of {@link IInstance} objects which are added to the dataset and
    * subsequently retrieved from it.
    * 
    * @return the {@link List} of instances
    */
   protected abstract List<IInstance<CONTEXT, ITEM, RATING>> getValidInstances();


}
