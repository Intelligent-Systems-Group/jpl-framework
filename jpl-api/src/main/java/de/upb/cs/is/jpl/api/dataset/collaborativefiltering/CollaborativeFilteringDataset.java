package de.upb.cs.is.jpl.api.dataset.collaborativefiltering;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.dataset.ADataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleMatrix;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * A Dataset especially optimized for Collaborative Filtering matrix operations.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringDataset extends ADataset<IVector, IVector, Double> {

   @SuppressWarnings("unused")
   private static final Logger logger = LoggerFactory.getLogger(CollaborativeFilteringDataset.class);

   private static final String SAME_ITEM_ID_BUT_A_DIFFERENT_FEATURE_VETCOR = "Cannot add an instance with the same item ID but a different feature vetcor";

   private static final String SAME_CONTEXT_ID_BUT_A_DIFFERENT_FEATURE_VETCOR = "Cannot add an instance with the same context ID but a different feature vetcor";

   private static final String INVALID_INSTANCE = "Invalid Instance. Cannot add this instance to the dataset.";

   protected SparseDoubleMatrix matrix;

   protected double threshold = 0.01;

   protected Map<Integer, Pair<Integer, Integer>> instancePositions;

   protected List<String> contextFeatures;
   protected List<IVector> contextVectors;

   protected List<String> itemFeatures;
   protected List<IVector> itemVectors;


   /**
    * Creates a new {@link CollaborativeFilteringDataset}.
    * 
    * @param numRows the number of rows in the the ratings matrix
    * @param numCols the number of columns in the columns matrix
    */
   public CollaborativeFilteringDataset(int numRows, int numCols) {
      this();
      matrix = new SparseDoubleMatrix(numRows, numCols);
   }


   /**
    * 
    */
   public CollaborativeFilteringDataset() {
      contextVectors = new ArrayList<>();
      contextFeatures = new ArrayList<>();
      itemFeatures = new ArrayList<>();
      itemVectors = new ArrayList<>();
      matrix = null;
      instancePositions = new HashMap<>();
   }


   @Override
   public CollaborativeFilteringInstance getInstance(int position) {
      if (position < 0 || position >= instancePositions.size()) {
         throw new IndexOutOfBoundsException(String.format("Cannot access instance %s", position));
      }
      Pair<Integer, Integer> coordinates = instancePositions.get(position);
      int x = coordinates.getFirst();
      int y = coordinates.getSecond();
      return new CollaborativeFilteringInstance(x, y, matrix.getValue(x, y), this);
   }


   @Override
   public void addInstance(IInstance<IVector, IVector, Double> instance) throws InvalidInstanceException {
      if (!(instance instanceof CollaborativeFilteringInstance)) {
         throw new InvalidInstanceException(INVALID_INSTANCE);
      }

      CollaborativeFilteringInstance cfInstance = (CollaborativeFilteringInstance) instance;
      if (!cfInstance.assignedDataset.equals(this)) {
         checkContextFeatureVector(cfInstance.getContextId(), cfInstance.getContextFeatureVector());
         checkItemFeatureVector(cfInstance.getItemId(), cfInstance.getItemFeatureVectors());
      }
      setRating(cfInstance.getContextId(), cfInstance.getItemId(), cfInstance.getRating());
   }


   private void checkItemFeatureVector(int itemId, IVector itemFeatureVectors) throws InvalidInstanceException {
      if (itemFeatureVectors != null) {
         if (!itemFeatureVectors.equals(itemVectors.get(itemId)) && itemVectors.get(itemId).euclideanNorm() > threshold) {
            throw new InvalidInstanceException(SAME_ITEM_ID_BUT_A_DIFFERENT_FEATURE_VETCOR);
         } else {
            setItemVector(itemId, itemFeatureVectors);
         }
      }
   }


   /**
    * Check if the the context feature vector is compatible with the current dataset. It is
    * incompatible if it's different than the existing value. If the existing value is zero, then
    * it's replaced by the new vector.
    * 
    * @param contextId the context id
    * @param contextFeatureVector the feature vector
    * @throws InvalidInstanceException
    */
   private void checkContextFeatureVector(Integer contextId, IVector contextFeatureVector) throws InvalidInstanceException {
      if (contextFeatureVector != null) {
         if (!contextFeatureVector.equals(contextVectors.get(contextId)) && contextVectors.get(contextId).euclideanNorm() > threshold) {
            throw new InvalidInstanceException(SAME_CONTEXT_ID_BUT_A_DIFFERENT_FEATURE_VETCOR);
         } else {
            setContextVector(contextId, contextFeatureVector);
         }
      }
   }


   /**
    * Sets a single entry in the matrix.
    * 
    * @param contextId the context which this rating describes
    * @param itemId the item which this rating describes
    * @param rating the rating which is set
    */
   public void setRating(int contextId, int itemId, double rating) {
      instancePositions.put(instancePositions.size(), Pair.of(contextId, itemId));
      matrix.setValue(contextId, itemId, rating);
   }


   /**
    * Returns a single entry in the matrix.
    * 
    * @param contextId the context which this rating describes
    * @param itemId the item which this rating describes
    * @return rating the rating for this context-item combination
    */
   public CollaborativeFilteringInstance getInstance(int contextId, int itemId) {
      return new CollaborativeFilteringInstance(contextId, itemId, matrix.getValue(contextId, itemId), this);
   }


   /**
    * Returns a single entry in the matrix.
    * 
    * @param contextId the context which this rating describes
    * @param itemId the item which this rating describes
    * @return rating the rating for this context-item combination
    */
   public double getRating(int contextId, int itemId) {
      return matrix.getValue(contextId, itemId);
   }


   @Override
   public CollaborativeFilteringDataset getPartOfDataset(int from, int to) {

      CollaborativeFilteringDataset result = new CollaborativeFilteringDataset(matrix.getNumberOfRows(), matrix.getNumberOfColumns());
      result.datasetFile = this.datasetFile;
      result.contextVectors = CollectionsUtils.getDeepCopyOf(contextVectors);
      result.contextFeatures = CollectionsUtils.getDeepCopyOf(contextFeatures);
      result.itemVectors = CollectionsUtils.getDeepCopyOf(itemVectors);
      result.itemFeatures = CollectionsUtils.getDeepCopyOf(itemFeatures);

      for (int i = from; i < to; i++) {
         int x = instancePositions.get(i).getFirst();
         int y = instancePositions.get(i).getSecond();
         result.setRating(x, y, matrix.getValue(x, y));
      }
      return result;
   }


   @Override
   public int getNumberOfInstances() {

      return instancePositions.size();
   }


   @Override
   protected void init() {
      // nothing needed here
   }


   /**
    * Returns a row vector from the matrix of ratings
    * 
    * @param i the number of the row
    * @return the ratings for this row / user
    */
   public IVector getRowVector(int i) {
      return matrix.getRowVector(i);
   }


   /**
    * Returns a column vector from the matrix of ratings
    * 
    * @param i the number of the column
    * @return the ratings for this column / item
    */
   public IVector getColumnVector(int i) {
      return matrix.getColumnVector(i);
   }


   /**
    * Getter for context vectors.
    * 
    * @param contextId the context id to which the vector is assigned
    * @return the feature values for the context vector
    */
   public IVector getContextVector(int contextId) {
      return contextVectors.get(contextId);
   }


   /**
    * Getter for item vectors.
    * 
    * @param itemId the item id to which the vector is assigned
    * @return the feature values for the item vector
    */
   public IVector getItemVector(int itemId) {
      return itemVectors.get(itemId);
   }


   /**
    * Adds an context vector to the set of existing context vectors.
    * 
    * @param id : The id under which this vector is saved
    * @param contextVector : The vector of context properties
    */
   public void setContextVector(Integer id, IVector contextVector) {
      if (id < contextVectors.size()) {
         contextVectors.set(id, contextVector);
      } else {
         while (contextVectors.size() < id) {
            contextVectors.add(new SparseDoubleVector(contextVector.length()));
         }
         contextVectors.add(contextVector);
      }
   }


   /**
    * Adds an item vector to the set of existing context vectors.
    * 
    * @param id : The id under which this vector is saved
    * @param itemVector : The vector of context properties
    */
   public void setItemVector(Integer id, IVector itemVector) {
      if (id < itemVectors.size()) {
         itemVectors.set(id, itemVector);
      } else {
         while (itemVectors.size() < id) {
            itemVectors.add(new SparseDoubleVector(itemVector.length()));
         }
         itemVectors.add(itemVector);
      }
   }


   /**
    * Returns the number of existing item vectors.
    * 
    * @return the number of existing items
    */
   public int getNumberOfItems() {
      return matrix != null ? matrix.getNumberOfColumns() : 0;
   }


   /**
    * Returns the number of existing context vectors
    * 
    * @return the number of existing contexts
    */
   public int getNumberOfContexts() {
      return matrix != null ? matrix.getNumberOfRows() : 0;
   }


   /**
    * Returns the name of the context feature with this id.
    * 
    * @param id the number of this context feature
    * @return the contextFeatures.
    */
   public String getContextFeature(Integer id) {
      return contextFeatures.get(id);
   }


   /**
    * Sets the name of the context feature with this id.
    * 
    * @param id the id number of the context feature
    * @param contextFeature the name of the contextFeature
    */
   public void setContextFeature(Integer id, String contextFeature) {
      if (contextFeatures.size() > id) {
         contextFeatures.set(id, contextFeature);
      } else {
         while (contextFeatures.size() < id) {
            contextFeatures.add(StringUtils.EMPTY_STRING);
         }
         contextFeatures.add(contextFeature);
      }
   }


   /**
    * Returns the name of the item feature with this id.
    * 
    * @param id the id of the item feature
    * @return the name of the item feature
    */
   public String getItemFeature(Integer id) {
      return itemFeatures.get(id);
   }


   /**
    * Sets the name of the item feature with this id.
    * 
    * @param id the id number of the item feature
    * @param itemFeature the name of the item feature
    */
   public void setItemFeature(Integer id, String itemFeature) {
      if (itemFeatures.size() > id) {
         itemFeatures.set(id, itemFeature);
      } else {
         while (itemFeatures.size() < id) {
            itemFeatures.add(StringUtils.EMPTY_STRING);
         }
         itemFeatures.add(itemFeature);
      }
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((contextFeatures == null) ? 0 : contextFeatures.hashCode());
      result = prime * result + ((contextVectors == null) ? 0 : contextVectors.hashCode());
      result = prime * result + ((instancePositions == null) ? 0 : instancePositions.hashCode());
      result = prime * result + ((itemFeatures == null) ? 0 : itemFeatures.hashCode());
      result = prime * result + ((itemVectors == null) ? 0 : itemVectors.hashCode());
      result = prime * result + ((matrix == null) ? 0 : matrix.hashCode());
      long temp;
      temp = Double.doubleToLongBits(threshold);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof CollaborativeFilteringDataset))
         return false;
      CollaborativeFilteringDataset other = (CollaborativeFilteringDataset) obj;
      if (contextFeatures == null) {
         if (other.contextFeatures != null)
            return false;
      } else if (!contextFeatures.equals(other.contextFeatures))
         return false;
      if (contextVectors == null) {
         if (other.contextVectors != null)
            return false;
      } else if (!contextVectors.equals(other.contextVectors))
         return false;
      if (instancePositions == null) {
         if (other.instancePositions != null)
            return false;
      } else if (!instancePositions.equals(other.instancePositions))
         return false;
      if (itemFeatures == null) {
         if (other.itemFeatures != null)
            return false;
      } else if (!itemFeatures.equals(other.itemFeatures))
         return false;
      if (itemVectors == null) {
         if (other.itemVectors != null)
            return false;
      } else if (!itemVectors.equals(other.itemVectors))
         return false;
      if (matrix == null) {
         if (other.matrix != null)
            return false;
      } else if (!matrix.equals(other.matrix))
         return false;
      if (Double.doubleToLongBits(threshold) != Double.doubleToLongBits(other.threshold))
         return false;
      return true;
   }


}
