package de.upb.cs.is.jpl.api.dataset.rankaggregation;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADataset;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * This class represents a simple and efficient dataset for the rank aggregation problem. The
 * dataset is implemented in such a way that it does not work with {@link RankAggregationInstance}s
 * directly in order to save resources.
 * 
 * @author Andreas Kornelsen
 *
 */
public class RankAggregationDataset extends ADataset<Integer, NullType, Ranking> {

   private List<Integer> labels;
   private List<Integer> countRankings;
   private List<Ranking> rankings;
   private List<Integer> ids;


   /**
    * Creates an empty rank aggregation dataset.
    */
   public RankAggregationDataset() {
      this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }


   /**
    * Creates a new rank aggregation dataset with complete dataset information, whereby each ranking
    * occurs once.
    *
    * @param labels the labels of the rankings
    * @param rankings the rankings
    */
   public RankAggregationDataset(List<Integer> labels, List<Ranking> rankings) {
      this(labels, new ArrayList<Integer>(Collections.nCopies(rankings.size(), 1)), rankings);
   }


   /**
    * Creates a new rank aggregation dataset with complete dataset information.
    *
    * @param labels the labels of the rankings
    * @param countRankings the count for each ranking
    * @param rankings the rankings
    */
   public RankAggregationDataset(List<Integer> labels, List<Integer> countRankings, List<Ranking> rankings) {
      this(labels, countRankings, rankings, new ArrayList<>());
   }


   /**
    * Creates a new rank aggregation dataset with complete dataset information and the internal
    * instance ids.
    *
    * @param labels the labels of the rankings
    * @param countRankings the count for each ranking
    * @param rankings the rankings
    * @param instanceIds the ids of the instances
    */
   private RankAggregationDataset(List<Integer> labels, List<Integer> countRankings, List<Ranking> rankings, List<Integer> instanceIds) {
      super();
      this.labels = labels;
      this.countRankings = countRankings;
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
      return countRankings.size();
   }


   @Override
   public IInstance<Integer, NullType, Ranking> getInstance(int position) {
      assertInstanceIsInBounds(position);

      RankAggregationInstance rankAggregationInstance = new RankAggregationInstance();
      rankAggregationInstance.setContextFeatureVector(countRankings.get(position));
      rankAggregationInstance.setContextId(ids.get(position));
      rankAggregationInstance.setRating(rankings.get(position));

      return rankAggregationInstance;
   }


   @Override
   public IDataset<Integer, NullType, Ranking> getPartOfDataset(int from, int to) {
      assertCorrectDatasetPartSelection(from, to);

      List<Integer> copyLabels = CollectionsUtils.getDeepCopyOf(labels);

      List<Integer> fromToCountRankings = new ArrayList<>();
      List<Ranking> fromToRankings = new ArrayList<>();
      List<Integer> fromToIds = new ArrayList<>();

      for (int i = from; i < to; i++) {
         fromToCountRankings.add(countRankings.get(i));
         fromToRankings.add(rankings.get(i));
         fromToIds.add(ids.get(i));
      }

      return new RankAggregationDataset(copyLabels, fromToCountRankings, fromToRankings, fromToIds);
   }


   @Override
   public void addInstance(IInstance<Integer, NullType, Ranking> instance) throws InvalidInstanceException {
      assertInstanceHasCorrectType(instance, RankAggregationInstance.class);

      RankAggregationInstance castedInstance = (RankAggregationInstance) instance;

      countRankings.add(castedInstance.getContextFeatureVector());
      rankings.add(castedInstance.getRating());
      addLabels(castedInstance.getRating());
      ids.add(castedInstance.getContextId());
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
    * Returns the ranking of an instance in the dataset.
    *
    * @param position the position of the ranking in the dataset
    * @return the ranking of an instance in the dataset
    */
   public Ranking getRankingOfInstance(int position) {
      return rankings.get(position);
   }


   /**
    * Returns the count for the ranking of an instance in the dataset.
    * 
    * @param position the position of the count for the ranking of an instance in the dataset
    * @return the count for the ranking of an instance in the dataset
    */
   public int getCountForRankingOfInstance(int position) {
      return countRankings.get(position);
   }


   /**
    * Returns the count of all individual labels.
    *
    * @return the count of individual labels
    */
   public int getCountOfLabels() {
      return labels.size();
   }


   /**
    * Returns the list of all individual labels.
    * 
    * @return the list of labels to be ranked
    */
   public List<Integer> getLabels() {
      return labels;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((countRankings == null) ? 0 : countRankings.hashCode());
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
      if (!(obj instanceof RankAggregationDataset))
         return false;
      RankAggregationDataset other = (RankAggregationDataset) obj;
      if (countRankings == null) {
         if (other.countRankings != null)
            return false;
      } else if (!countRankings.equals(other.countRankings))
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
