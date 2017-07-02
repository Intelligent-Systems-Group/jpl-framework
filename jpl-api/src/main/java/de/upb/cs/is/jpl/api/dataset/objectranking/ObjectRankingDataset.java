package de.upb.cs.is.jpl.api.dataset.objectranking;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADataset;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This dataset is used to store a dataset for object ranking algorithms. Not implemented yet.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingDataset extends ADataset<double[], List<double[]>, Ranking> {
   private static final String THE_PROVIDED_ITEM_VECTORS_ARE_NOT_THE_SAME_AS_IN_THE_DATASET = "The provided item vectors are not the same as in the dataset.";

   private List<double[]> contextVectors;
   private List<double[]> itemVectors;
   private List<Ranking> rankings;


   /**
    * Creates an empty object ranking dataset.
    */
   public ObjectRankingDataset() {
      super();
   }


   /**
    * Create a new {@link ObjectRankingDataset} with the context vectors, item vectors and rankings.
    * 
    * @param contextVectors the context vectors
    * @param itemVectors the item vectors
    * @param rankings the list of rankings
    */
   public ObjectRankingDataset(List<double[]> contextVectors, List<double[]> itemVectors, List<Ranking> rankings) {
      this.contextVectors = CollectionsUtils.getDeepCopyOf(contextVectors);
      this.itemVectors = CollectionsUtils.getDeepCopyOf(itemVectors);
      this.rankings = CollectionsUtils.getDeepCopyOf(rankings);
   }


   @Override
   public IInstance<double[], List<double[]>, Ranking> getInstance(int position) {
      assertInstanceIsInBounds(position);
      return new ObjectRankingInstance(position, contextVectors.get(position), itemVectors, rankings.get(position));
   }


   @Override
   public int getNumberOfInstances() {
      return rankings.size();
   }


   @Override
   public void addInstance(IInstance<double[], List<double[]>, Ranking> instance) throws InvalidInstanceException {
      assertInstanceHasCorrectType(instance, ObjectRankingInstance.class);
      if (itemVectors.isEmpty()) {
         itemVectors.addAll(instance.getItemFeatureVectors());
      }
      if (instance.getContextId() == -1) {
         if (!instance.getItemFeatureVectors().equals(itemVectors)) {
            throw new InvalidInstanceException(THE_PROVIDED_ITEM_VECTORS_ARE_NOT_THE_SAME_AS_IN_THE_DATASET);
         }
         contextVectors.add(instance.getContextFeatureVector());
         instance.setContextId(contextVectors.size() - 1);
         rankings.add(instance.getRating());
      }

   }


   @Override
   protected void init() {
      this.contextVectors = new ArrayList<>();
      this.itemVectors = new ArrayList<>();
      this.rankings = new ArrayList<>();
   }


   @Override
   public IDataset<double[], List<double[]>, Ranking> getPartOfDataset(int from, int to) {
      assertCorrectDatasetPartSelection(from, to);
      List<double[]> copyItemVectors;
      List<double[]> fromTocontextVectors = new ArrayList<>();
      copyItemVectors = CollectionsUtils.getDeepCopyOf(itemVectors);
      List<Ranking> fromToRankings = new ArrayList<>();
      for (int i = from; i < to; i++) {
         fromToRankings.add(rankings.get(i));
         fromTocontextVectors.add(contextVectors.get(i));
      }
      return new ObjectRankingDataset(fromTocontextVectors, copyItemVectors, fromToRankings);

   }


   @Override
   public IInstance<double[], List<double[]>, Ranking> getShuffledInstance(int instanceNumber) {
      IInstance<double[], List<double[]>, Ranking> instance = super.getShuffledInstance(instanceNumber);
      instance.setContextId(-1);
      return instance;
   }


   /**
    * Returns the ranking of an instance in the dataset.
    *
    * @param position the position of the ranking in the dataset
    * @return the ranking of an instance in the dataset
    */
   public Ranking getRankingOfInstance(int position) {
      return rankings.get(position);
   }


   /**
    * Returns the complete list of context vectors.
    * 
    * @return the {@link List} of context vectors
    */
   public List<double[]> getContextVectors() {
      return contextVectors;
   }


   /**
    * Returns the number of items in the dataset.
    * 
    * @return the number of items
    */
   public int getNumberOfItems() {
      return itemVectors.size();
   }


   /**
    * Transforms an item ID into the corresponding properties vector.
    * 
    * @param id the id which identifies the item
    * @return an array which contains the values for the selected item
    */
   public double[] getItemVector(Integer id) {
      return itemVectors.get(id);
   }


   /**
    * Returns the number of item features for the dataset.
    * 
    * @return an number of item features for the dataset
    */
   public int getNumofItemFeatures() {
      return itemVectors.get(0).length;
   }


   /**
    * Returns the number of context features for the dataset.
    * 
    * @return an number of context features for the dataset
    */
   public int getNumofContextFeatures() {
      return contextVectors.get(0).length;
   }


   /**
    * Returns the complete list of {@link Ranking}.
    * 
    * @return the {@link List} of {@link Ranking}
    */
   public List<Ranking> getRankings() {
      return rankings;
   }


   /**
    * Returns the minimum length of the objects in the {@link Ranking}s in the dataset.
    * 
    * @return the minimum length of the objects in the ranking
    */
   public int getMinimumLengthOfRating() {
      int miniumRankedObjects = Integer.MAX_VALUE;
      for (Ranking ranking : rankings) {
         if (ranking.getObjectList().length < miniumRankedObjects) {
            miniumRankedObjects = ranking.getObjectList().length;
         }
      }
      return miniumRankedObjects;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((contextVectors == null) ? 0 : contextVectors.hashCode());
      result = prime * result + ((itemVectors == null) ? 0 : itemVectors.hashCode());
      result = prime * result + ((rankings == null) ? 0 : rankings.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      ObjectRankingDataset other = (ObjectRankingDataset) obj;
      if (contextVectors == null) {
         if (other.contextVectors != null)
            return false;
      } else if (!contextVectors.equals(other.contextVectors))
         return false;
      if (itemVectors == null) {
         if (other.itemVectors != null)
            return false;
      } else if (!itemVectors.equals(other.itemVectors))
         return false;
      if (rankings == null) {
         if (other.rankings != null)
            return false;
      } else if (!rankings.equals(other.rankings))
         return false;
      return true;
   }


}
