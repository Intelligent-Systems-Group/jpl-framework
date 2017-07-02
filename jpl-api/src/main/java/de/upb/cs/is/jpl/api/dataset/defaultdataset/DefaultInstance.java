package de.upb.cs.is.jpl.api.dataset.defaultdataset;


import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.dataset.AInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;


/**
 * Default Instance for rating information
 * 
 * @author Sebastian Osterbrink
 * @param <T> the type of the rating used in this instance. Expected values are {@link Double} for
 *           absolute ratings and {@link Ranking} for relative ratings.
 */
public class DefaultInstance<T> extends AInstance<double[], List<double[]>, T> {

   @SuppressWarnings("unused")
   private static final Logger logger = LoggerFactory.getLogger(DefaultInstance.class);

   protected double[] contextFeatures;
   protected List<double[]> itemFeatures;

   protected DefaultDataset<T> assignedDataset;


   /**
    * Creates a new Instance object.
    * 
    * @param contextId the assigned context id
    * @param rating the Rating information
    * @param assignedDataset the dataset which provides the vector information
    */
   public DefaultInstance(Integer contextId, T rating, DefaultDataset<T> assignedDataset) {
      this.setContextId(contextId);
      this.setRating(rating);
      this.assignedDataset = assignedDataset;
   }


   /**
    * Creates a new Instance object.
    * 
    * @param contextFeature the feature vector for this context
    * @param itemFeatures the list of the feature vectors for all items
    * @param rating the Rating information
    */
   public DefaultInstance(double[] contextFeature, List<double[]> itemFeatures, T rating) {
      this.setContextId(-1);
      this.setRating(rating);
      this.contextFeatures = contextFeature;
      this.itemFeatures = itemFeatures;

   }


   @Override
   public double[] getContextFeatureVector() {
      return contextFeatures != null ? contextFeatures : assignedDataset.getContextVector(contextId);
   }


   @Override
   public void setContextFeatureVector(double[] contextFeatureVector) {
      this.contextId = -1;
      this.contextFeatures = contextFeatureVector;
   }


   @Override
   public List<double[]> getItemFeatureVectors() {
      return itemFeatures != null ? itemFeatures : assignedDataset.getItemVectors();
   }


   @Override
   public void setItemFeatureVector(List<double[]> itemFeatureVectors) {
      this.itemFeatures = itemFeatureVectors;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + Arrays.hashCode(contextFeatures);
      result = prime * result + ((itemFeatures == null) ? 0 : itemFeatures.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof DefaultInstance))
         return false;
      DefaultInstance<?> other = (DefaultInstance<?>) obj;
      if (!Arrays.equals(getContextFeatureVector(), other.getContextFeatureVector()))
         return false;
      if (getItemFeatureVectors() == null) {
         if (other.getItemFeatureVectors() != null)
            return false;
      } else if (!getItemFeatureVectors().equals(other.getItemFeatureVectors()))
         return false;
      return true;
   }


}
