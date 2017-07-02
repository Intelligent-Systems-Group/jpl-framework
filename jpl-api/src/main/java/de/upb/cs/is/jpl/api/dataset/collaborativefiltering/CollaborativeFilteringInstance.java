package de.upb.cs.is.jpl.api.dataset.collaborativefiltering;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.dataset.AInstance;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringInstance extends AInstance<IVector, IVector, Double> {


   @SuppressWarnings("unused")
   private static final Logger logger = LoggerFactory.getLogger(CollaborativeFilteringInstance.class);

   protected IVector contextFeatureVector;
   protected IVector itemFeatureVector;

   protected int itemId;

   protected CollaborativeFilteringDataset assignedDataset;


   /**
    * Creates a new Instance object.
    * 
    * @param contextId the assigned context id.
    * @param itemId the assigned item id.
    * @param rating the Rating information.
    * @param assignedDataset the dataset which provides the vector information.
    */
   public CollaborativeFilteringInstance(int contextId, int itemId, Double rating, CollaborativeFilteringDataset assignedDataset) {
      this.setContextId(contextId);
      this.itemId = itemId;
      this.setRating(rating);
      this.assignedDataset = assignedDataset;
   }


   /**
    * Creates a new Instance object.
    * 
    * @param contextId the assigned context id.
    * @param itemId the assigned item id.
    * @param contextFeatureVector the feature vector for this context.
    * @param itemFeatureVector the feature vectors for this item.
    * @param rating the Rating information.
    */
   public CollaborativeFilteringInstance(int contextId, int itemId, IVector contextFeatureVector, IVector itemFeatureVector,
         Double rating) {
      this.setContextId(contextId);
      this.itemId = itemId;
      this.setRating(rating);
      this.contextFeatureVector = contextFeatureVector;
      this.itemFeatureVector = itemFeatureVector;

   }


   @Override
   public IVector getContextFeatureVector() {
      if (contextFeatureVector != null) {
         return assignedDataset.getContextVector(contextId);
      } else {
         return contextFeatureVector;
      }
   }


   @Override
   public void setContextFeatureVector(IVector contextFeatureVector) {
      this.contextFeatureVector = contextFeatureVector;
   }


   @Override
   public IVector getItemFeatureVectors() {
      return itemFeatureVector != null ? itemFeatureVector : assignedDataset.getItemVector(itemId);
   }


   @Override
   public void setItemFeatureVector(IVector itemFeatureVector) {
      this.itemFeatureVector = itemFeatureVector;
   }


   /**
    * Getter for the assigned item id
    * 
    * @return the assigned item id
    */
   public int getItemId() {
      return itemId;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((assignedDataset == null) ? 0 : assignedDataset.hashCode());
      result = prime * result + ((contextFeatureVector == null) ? 0 : contextFeatureVector.hashCode());
      result = prime * result + ((itemFeatureVector == null) ? 0 : itemFeatureVector.hashCode());
      result = prime * result + itemId;
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof CollaborativeFilteringInstance))
         return false;
      CollaborativeFilteringInstance other = (CollaborativeFilteringInstance) obj;


      if (getContextFeatureVector() == null) {
         if (other.getContextFeatureVector() != null)
            return false;
      } else if (!getContextFeatureVector().equals(other.getContextFeatureVector()))
         return false;
      if (getItemFeatureVectors() == null) {
         if (other.getItemFeatureVectors() != null)
            return false;
      } else if (!getItemFeatureVectors().equals(other.getItemFeatureVectors()))
         return false;
      if (itemId != other.itemId)
         return false;
      return true;
   }

}
