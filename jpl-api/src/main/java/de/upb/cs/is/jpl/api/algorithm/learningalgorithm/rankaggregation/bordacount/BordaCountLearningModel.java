package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.bordacount;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.ARankAggregationLearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDataset;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * The learning model class is created by Borda Count algorithm after the training on the
 * {@link DefaultRelativeDataset}. The model predicts the ranking according to the label scores.
 * Thereby, the model can only predict the ranking of the dataset and not on new instances or new
 * datasets.
 * 
 * @author Andreas Kornelsen
 */
public class BordaCountLearningModel extends ARankAggregationLearningModel {

   private Map<Integer, Double> scores;
   private Ranking ranking;


   /**
    * Creates new {@link BordaCountLearningModel} with the scores for each the labels of ranking and
    * list of the labels to be ranked.
    * 
    * @param scores the scores for the labels
    * @param listOfLabels the list of labels
    */
   public BordaCountLearningModel(Map<Integer, Double> scores, List<Integer> listOfLabels) {
      super(listOfLabels);
      this.scores = CollectionsUtils.getdeepCopyOfMap(scores);
   }


   @Override
   public List<Ranking> predict(final IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      assertDatasetHasCorrectType(dataset, RankAggregationDataset.class);
      assetDatasetCompatibility(dataset);
      List<Ranking> result = new ArrayList<>();
      result.add(predict());
      return result;
   }


   @Override
   public Ranking predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      assertInstanceHasCorrectType(instance, RankAggregationInstance.class);
      assertInstanceCompatibility(instance);
      return predict();
   }


   /**
    * The rank aggregation of labels. The ranking is constructed according to the score of the
    * labels.
    *
    * @return the aggregated ranking based on the score of the labels
    */
   public Ranking predict() {
      if (ranking == null) {
         int[] rankAggregation = CollectionsUtils.getSortedKeyValuesInDecreasingValueOrder(scores);
         int[] compareOperators = Ranking.createCompareOperatorArrayForLabels(rankAggregation);
         ranking = new Ranking(rankAggregation, compareOperators);
      }
      return ranking;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((listOfLabels == null) ? 0 : listOfLabels.hashCode());
      result = prime * result + ((scores == null) ? 0 : scores.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof BordaCountLearningModel))
         return false;
      BordaCountLearningModel other = (BordaCountLearningModel) obj;
      if (listOfLabels == null) {
         if (other.listOfLabels != null)
            return false;
      } else if (!listOfLabels.equals(other.listOfLabels))
         return false;
      if (scores == null) {
         if (other.scores != null)
            return false;
      } else if (!scores.equals(other.scores))
         return false;
      return true;
   }


}
