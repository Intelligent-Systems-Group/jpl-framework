package de.upb.cs.is.jpl.api.dataset;


import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.algorithm.UnsupportedInstanceTypeException;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * An interface for a dataset which is intended to be used in combination with the interface
 * {@link IInstance} .
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
public interface IDataset<CONTEXT, ITEM, RATING> {

   /**
    * Adds a range [to, from[ of instances of the source dataset to the target dataset
    * 
    * @param sourceDataset the source of the instances
    * @param targetDataset the dataset where the instances are added
    * @param from the first index to add
    * @param to the first index which is not added
    * @throws InvalidInstanceException if the instance cannot be added to the target dataset
    */
   @SuppressWarnings("unchecked")
   public static void addShuffledInstancesToDataset(IDataset<?, ?, ?> sourceDataset, IDataset<?, ?, ?> targetDataset, int from, int to)
         throws InvalidInstanceException {

      for (int instanceIndex = from; instanceIndex < to; instanceIndex++) {
         IInstance<?, ?, ?> instance = sourceDataset.getShuffledInstance(instanceIndex);
         targetDataset.addInstance(instance.getClass().cast(instance));
      }

   }


   /**
    * Returns the i-th instance of the dataset.
    * 
    * @param position the position of the instance in the dataset to obtain
    * @return the i-th instance of the dataset
    */
   public IInstance<CONTEXT, ITEM, RATING> getInstance(final int position);


   /**
    * Returns the i-th instance of the shuffled dataset.
    * 
    * @param position the position of the instance in the dataset to obtain
    * @return the i-th instance of the dataset
    */
   public IInstance<CONTEXT, ITEM, RATING> getShuffledInstance(final int position);


   /**
    * Shuffles the order of the instances in this dataset.
    */
   public void shuffle();


   /**
    * Returns a subset of instances of this dataset which contains all instances which have an index
    * in the given range.
    * 
    * @param from the first index that will be in the returned dataset.s
    * @param to the position of the first instance that will <b>not</b> be in the returning dataset
    * @return a subset of instances of this dataset
    */
   public IDataset<CONTEXT, ITEM, RATING> getPartOfDataset(final int from, final int to);


   /**
    * Returns the number of instances of this dataset.
    * 
    * @return the number of instances
    */
   public int getNumberOfInstances();


   /**
    * Adds an instance to this dataset.
    *
    * @param instance the instance to add
    * @throws InvalidInstanceException if the instance is not valid for this dataset
    * @throws UnsupportedInstanceTypeException if the type of the given instance is unsupported
    */
   public void addInstance(IInstance<CONTEXT, ITEM, RATING> instance) throws InvalidInstanceException;


   /**
    * Stores the given dataset file, which is related to this dataset.
    * 
    * @param file the file from which this dataset was created
    */
   public void setDatasetFile(DatasetFile file);


   /**
    * Returns the dataset file of this dataset.
    * 
    * @return the dataset file from which this dataset was created
    */
   public DatasetFile getDatasetFile();


   /**
    * Returns the unique long id for the unique identifier for the dataset.
    * 
    * @return the long unique id generated for the dataset
    */
   public long getId();


   /**
    * Returns the unique long string identifying the dataset.
    * 
    * @return the unique string identifying the dataset
    */
   public String getUniqueStringIdentifyingDataset();
}
