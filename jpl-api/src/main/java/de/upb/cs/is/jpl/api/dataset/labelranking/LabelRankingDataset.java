package de.upb.cs.is.jpl.api.dataset.labelranking;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.dataset.ADataset;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * 
 * This class represents a simple and efficient dataset for the label ranking problem. The dataset
 * is implemented in such a way that it does not work with {@link LabelRankingInstance}s directly in
 * order to save resources.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingDataset extends ADataset<double[], NullType, Ranking> {


   private List<Integer> labels;
   private List<double[]> features;
   private List<Ranking> rankings;
   private List<Integer> ids;


   /**
    * Creates an empty label ranking dataset.
    */
   public LabelRankingDataset() {
      this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }


   /**
    * Creates a new label ranking dataset with complete dataset information.
    *
    * @param labels the labels of the rankings
    * @param features the feature values of all instances
    * @param rankings the rankings for all instances
    */
   public LabelRankingDataset(List<Integer> labels, List<double[]> features, List<Ranking> rankings) {
      this(labels, features, rankings, new ArrayList<>());
   }


   /**
    * Creates a new label ranking dataset with complete dataset information and the internal
    * instance ids.
    *
    * @param labels the labels of the rankings
    * @param features the feature values of all instances
    * @param rankings the rankings for all instances
    * @param instanceIds the ids of the instances
    */
   private LabelRankingDataset(List<Integer> labels, List<double[]> features, List<Ranking> rankings, List<Integer> instanceIds) {
      super();
      this.labels = labels;
      this.features = features;
      this.rankings = rankings;

      if (instanceIds.isEmpty()) {
         List<Integer> localInstanceIds = new ArrayList<>();
         for (int id = 0; id < rankings.size(); id++) {
            localInstanceIds.add(id);
         }
         this.ids = localInstanceIds;
      } else {
         this.ids = instanceIds;
      }
   }


   @Override
   protected void init() {
      // The init method is not required for this dataset.
   }


   @Override
   public int getNumberOfInstances() {
      return features.size();
   }


   @Override
   public IInstance<double[], NullType, Ranking> getInstance(int instanceId) {
      assertInstanceIsInBounds(instanceId);

      LabelRankingInstance labelRankingInstance = new LabelRankingInstance();
      labelRankingInstance.setContextFeatureVector(features.get(instanceId));
      labelRankingInstance.setContextId(ids.get(instanceId));
      labelRankingInstance.setRating(rankings.get(instanceId));
      labelRankingInstance.setTotalNumberOfLabels(labels.size());

      return labelRankingInstance;
   }


   @Override
   public IDataset<double[], NullType, Ranking> getPartOfDataset(int from, int to) {
      assertCorrectDatasetPartSelection(from, to);

      List<Integer> copyLabels = CollectionsUtils.getDeepCopyOf(labels);

      List<double[]> fromToFeatures = new ArrayList<>();
      List<Ranking> fromToRankings = new ArrayList<>();
      List<Integer> fromToIds = new ArrayList<>();

      for (int i = from; i < to; i++) {
         fromToFeatures.add(features.get(i));
         fromToRankings.add(rankings.get(i));
         fromToIds.add(ids.get(i));
      }

      return new LabelRankingDataset(copyLabels, fromToFeatures, fromToRankings, fromToIds);
   }


   @Override
   public void addInstance(IInstance<double[], NullType, Ranking> instance) throws InvalidInstanceException {
      assertInstanceHasCorrectType(instance, LabelRankingInstance.class);

      LabelRankingInstance castedInstance = (LabelRankingInstance) instance;

      features.add(castedInstance.getContextFeatureVector());
      rankings.add(castedInstance.getRating());
      addLabels(castedInstance.getRating());
      ids.add(ids.size());
   }


   /**
    * Adds the labels from the given ranking if they are not already included in the label list.
    *
    * @param ranking the ranking
    */
   private void addLabels(Ranking ranking) {
      for (int label : ranking.getObjectList()) {
         if (!labels.contains(label)) {
            labels.add(label);
         }
      }
   }


   /**
    * Returns the baselearner dataset with the label ranking dataset features and ids as predictor.
    *
    * @return the baselearner dataset with features and index as predictor
    */
   public BaselearnerDataset getBaselearnerDatasetWithFeaturesAndIdsAsPredictor() {
      BaselearnerDataset baselearnerDataset = new BaselearnerDataset(getNumberOfInstances(), getNumberOfFeatures());
      for (int index = 0; index < features.size(); index++) {
         baselearnerDataset.addFeatureVectorWithResult(getFeatureValuesOfAnInstance(index), index);
      }
      return baselearnerDataset;
   }


   /**
    * Returns the ranking of an instance in the dataset.
    *
    * @param instanceId the instance id
    * @return the ranking of an instance in the dataset
    */
   public Ranking getRankingOfInstance(int instanceId) {
      return rankings.get(instanceId);
   }


   /**
    * Returns the features values of an instance in the dataset.
    *
    * @param instanceId the instance id
    * @return the features values of an instance in the dataset
    */
   public double[] getFeatureValuesOfAnInstance(int instanceId) {
      return features.get(instanceId);
   }


   /**
    * Returns the count of all individual labels.
    *
    * @return the count of individual labels
    */
   public int getNumberOfLabels() {
      return labels.size();
   }


   /**
    * Returns the number of features.
    *
    * @return the number of features
    */
   public int getNumberOfFeatures() {
      if (features.isEmpty() || features.get(0) == null || features.get(0).length == 0) {
         return 0;
      }
      return features.get(0).length;
   }


   /**
    * Returns a copy of rankings.
    * 
    * @return an copy of the rankings
    */
   public List<Ranking> getCopyOfRankings() {
      return CollectionsUtils.getDeepCopyOf(rankings);
   }


   /**
    * Returns a copy of labels.
    * 
    * @return an copy of the labels
    */
   public List<Integer> getCopyOfLabels() {
      return CollectionsUtils.getDeepCopyOf(labels);
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((features == null) ? 0 : features.hashCode());
      result = prime * result + ((ids == null) ? 0 : ids.hashCode());
      result = prime * result + ((labels == null) ? 0 : labels.hashCode());
      result = prime * result + ((rankings == null) ? 0 : rankings.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof LabelRankingDataset))
         return false;
      LabelRankingDataset other = (LabelRankingDataset) obj;
      if (features == null) {
         if (other.features != null)
            return false;
      } else if (!features.equals(other.features))
         return false;
      if (ids == null) {
         if (other.ids != null)
            return false;
      } else if (!ids.equals(other.ids))
         return false;
      if (labels == null) {
         if (other.labels != null)
            return false;
      } else if (!labels.equals(other.labels))
         return false;
      if (rankings == null) {
         if (other.rankings != null)
            return false;
      } else if (!rankings.equals(other.rankings))
         return false;
      return true;
   }

}
