
package de.upb.cs.is.jpl.api.dataset;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.algorithm.UnsupportedInstanceTypeException;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.TypeCheckUtils;


/**
 * An abstract class for implementing a dataset.
 * 
 * @author Sebastian Osterbrink
 * @author Tanja Tornede
 * 
 * @param <CONTEXT> the type of the context feature vector used in the implementation of this class
 *           to store the context feature vector
 * @param <ITEM> the type of the item feature vector used in the implementation of this class to
 *           store the item feature vector
 * @param <RATING> the type of the rating used in the implementation of this class to store the
 *           rating. Suggested values are {@link IVector} for absolute ratings and {@link Ranking}
 *           for relative ratings.
 * 
 */
public abstract class ADataset<CONTEXT, ITEM, RATING> implements IDataset<CONTEXT, ITEM, RATING> {

   private static final String DATSET_FILE_NOT_SET = "DatsetFileNotSet";
   private static final String NO_DATASET_FILE = "No dataset file";
   private static final String CREATED_FROM = "%s created from %s";

   private static final String ERROR_GIVEN_INSTANCE_NUMBER_OUT_OF_BOUNDS = "The given instance number is out of the bounds of the dataset: %s";
   private static final String ERROR_CANNOT_SELECT_NEGATIVE_AMOUNT_OF_INSTANCES = "Cannot select a negative amount of instances. The value of 'from' is bigger than 'to'.";
   private static final String ERROR_GIVEN_BOUNDS_OUT_OF_BOUNDS = "The given bounds are out of the bounds of the dataset.";

   protected DatasetFile datasetFile = new DatasetFile(new File(DATSET_FILE_NOT_SET));

   private static final AtomicLong NEXT_ID = new AtomicLong(0);
   private final long id = NEXT_ID.getAndIncrement();


   protected Map<Integer, Integer> shuffledPositions;


   /**
    * Creates a new dataset object.
    */
   public ADataset() {
      init();
      shuffledPositions = new HashMap<>();
   }


   /**
    * Initializes the variables.
    */
   protected abstract void init();


   @Override
   public IInstance<CONTEXT, ITEM, RATING> getShuffledInstance(int instanceNumber) {
      if (shuffledPositions.containsKey(instanceNumber)) {
         return getInstance(shuffledPositions.get(instanceNumber));
      } else {
         return getInstance(instanceNumber);
      }
   }


   @Override
   public void shuffle() {
      List<Integer> positionNumbers = new ArrayList<>();
      for (int i = 0; i < getNumberOfInstances(); i++) {
         positionNumbers.add(i);
      }
      Random random = RandomGenerator.getRNG();
      int n = positionNumbers.size() - 1;
      for (int i = 0; i < n; i++) {
         Integer temp = positionNumbers.get(i);
         int swapPosition = i + random.nextInt(n - i);
         positionNumbers.set(i, positionNumbers.get(swapPosition));
         positionNumbers.set(swapPosition, temp);
      }

      shuffledPositions.clear();
      for (int i = 0; i < getNumberOfInstances(); i++) {
         shuffledPositions.put(i, positionNumbers.get(i));
      }
   }


   @Override
   public String toString() {
      return String.format(CREATED_FROM, getClass().getSimpleName(),
            datasetFile != null ? datasetFile.getFile().getName() : NO_DATASET_FILE);
   }


   @Override
   public DatasetFile getDatasetFile() {
      return datasetFile;
   }


   @Override
   public void setDatasetFile(DatasetFile datasetFile) {
      this.datasetFile = datasetFile;
   }


   @Override
   public final long getId() {
      return id;
   }


   @Override
   public final String getUniqueStringIdentifyingDataset() {
      return datasetFile.getFile().getName() + String.valueOf(id);
   }


   /**
    * Checks if the given interval of instances is within this dataset or not.
    * 
    * @param from the inclusive lower bound of the interval
    * @param to the exclusive upper bound of the interval
    * @throws IndexOutOfBoundsException if the interval is not within the dataset
    */
   protected void assertCorrectDatasetPartSelection(int from, int to) {
      if (from > to) {
         throw new IndexOutOfBoundsException(ERROR_CANNOT_SELECT_NEGATIVE_AMOUNT_OF_INSTANCES);
      }
      if (from < 0 || to > getNumberOfInstances()) {
         throw new IndexOutOfBoundsException(ERROR_GIVEN_BOUNDS_OUT_OF_BOUNDS);
      }
   }


   /**
    * Checks if the given instance number is part of the dataset or not.
    * 
    * @param instanceNumber the instance number to be checked
    * @throws IndexOutOfBoundsException if the given instance number is out of bounds of this
    *            dataset
    */
   protected void assertInstanceIsInBounds(int instanceNumber) {
      if (instanceNumber < 0 || instanceNumber >= getNumberOfInstances()) {
         throw new IndexOutOfBoundsException(String.format(ERROR_GIVEN_INSTANCE_NUMBER_OUT_OF_BOUNDS, instanceNumber));
      }
   }


   /**
    * Checks if the given instance is of the given type.
    * 
    * @param instance the instance to check the type of
    * @param expectedType the expected type of the instance
    * 
    * @throws UnsupportedInstanceTypeException if the given instance is not of the given expected
    *            type
    */
   protected void assertInstanceHasCorrectType(IInstance<CONTEXT, ITEM, RATING> instance, Class<?> expectedType) {
      TypeCheckUtils.assertInstanceHasCorrectType(instance, expectedType);
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((datasetFile == null) ? 0 : datasetFile.hashCode());
      result = prime * result + (int) (id ^ (id >>> 32));
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof ADataset))
         return false;
      ADataset<?, ?, ?> other = (ADataset<?, ?, ?>) obj;
      if (datasetFile == null) {
         if (other.datasetFile != null)
            return false;
      } else if (!datasetFile.equals(other.datasetFile))
         return false;
      if (id != other.id)
         return false;
      return true;
   }


}
