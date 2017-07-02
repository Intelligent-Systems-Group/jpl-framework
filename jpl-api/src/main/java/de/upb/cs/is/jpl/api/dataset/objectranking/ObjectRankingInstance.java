package de.upb.cs.is.jpl.api.dataset.objectranking;


import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.AInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This instance is used to store an instance for object ranking dataset.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingInstance extends AInstance<double[], List<double[]>, Ranking> {

   private double[] contextFeatures;
   protected List<double[]> itemFeatures;


   /**
    * Creates a new {@link ObjectRankingInstance} object with context features, item list with its
    * features and ranking.
    * 
    * @param ranking the Ranking
    * @param contextFeature the context feature vector for the instance
    * @param itemFeature the dataset which provides the vector information
    */
   public ObjectRankingInstance(double[] contextFeature, List<double[]> itemFeature, Ranking ranking) {
      this(-1, contextFeature, itemFeature, ranking);

   }


   /**
    * Creates a new {@link ObjectRankingInstance} object with context features, item list with its
    * features and ranking.
    * 
    * @param contextId the assigned context id
    * @param ranking the Ranking
    * @param contextFeature the context feature vector for the instance
    * @param itemFeature the dataset which provides the vector information
    */
   public ObjectRankingInstance(Integer contextId, double[] contextFeature, List<double[]> itemFeature, Ranking ranking) {
      this.setContextId(contextId);
      this.setRating(ranking);
      this.contextFeatures = contextFeature;
      this.itemFeatures = CollectionsUtils.getDeepCopyOf(itemFeature);
   }


   @Override
   public double[] getContextFeatureVector() {
      return contextFeatures;
   }


   @Override
   public void setContextFeatureVector(double[] contextFeatureVector) {
      this.contextFeatures = contextFeatureVector;

   }


   @Override
   public List<double[]> getItemFeatureVectors() {
      return itemFeatures;
   }


   @Override
   public void setItemFeatureVector(List<double[]> itemFeatureVectors) {
      this.itemFeatures = CollectionsUtils.getDeepCopyOf(itemFeatureVectors);
   }


   /**
    * Get the number of item features for one item.
    * 
    * @return an number of item features for the dataset
    */
   public int getNumofItemFeatures() {
      return itemFeatures.get(0).length;
   }


   /**
    * Returns the number of items in the dataset.
    * 
    * @return the number of items
    */
   public int getNumberOfItems() {
      return itemFeatures.size();
   }


   /**
    * Returns the number of context features in the instance.
    * 
    * @return the number of items
    */
   public int getNumberOfContextsFeatures() {
      return contextFeatures.length;
   }


   /**
    * Transforms an item ID into the corresponding properties vector.
    * 
    * @param id the id which identifies the item
    * @return an array which contains the values for the selected item
    */
   public double[] getFeaturesForItem(Integer id) {
      return itemFeatures.get(id);
   }


   @Override
   public boolean equals(Object secondInstance) {
      if (secondInstance instanceof ObjectRankingInstance) {
         ObjectRankingInstance objectRankingInstance = (ObjectRankingInstance) secondInstance;
         boolean itemFeaturesEqual = true;
         for (int i = 0; i < itemFeatures.size(); i++) {
            if (!Arrays.equals(itemFeatures.get(i), objectRankingInstance.getItemFeatureVectors().get(i)))
               itemFeaturesEqual = false;
         }
         return Arrays.equals(contextFeatures, objectRankingInstance.getContextFeatureVector()) && itemFeaturesEqual
               && rating.equals(objectRankingInstance.getRating());
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = 1;
      hashCode = 31 * hashCode + (contextId == null ? 0 : contextId.hashCode());
      hashCode = 31 * hashCode + (rating == null ? 0 : rating.hashCode());
      return hashCode;
   }

}
