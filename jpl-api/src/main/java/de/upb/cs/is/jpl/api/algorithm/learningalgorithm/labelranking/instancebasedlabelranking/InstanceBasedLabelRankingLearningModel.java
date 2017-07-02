package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.instancebasedlabelranking;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor.KNearestNeighborLearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.ALabelRankingLearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.plackettluce.PlackettLuceLearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingDataset;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingInstance;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * 
 * The instance-based label ranking algorithm computes the k nearest neighbors of a new query
 * instance in the training dataset. The rankings of this neighbors are aggregated with a rank
 * aggregation algorithm, for example with the {@link PlackettLuceLearningAlgorithm}. The output of
 * the rank aggregation is the prediction of the query instance.
 *
 * @author Andreas Kornelsen
 */
public class InstanceBasedLabelRankingLearningModel extends ALabelRankingLearningModel {

   private static final String ERROR_MESSAGE_BASE_LEARNER_TRAINING = "An error occurred during the training of the rank aggregation of the k nearest neighbor rankings.";
   private LabelRankingDataset labelRankingDataset;
   private KNearestNeighborLearningModel kNNModel;

   private ILearningAlgorithm rankAggregationAlgorithm;


   /**
    * Instantiates a new instance based label ranking model.
    *
    * @param labelRankingDataset the label ranking dataset
    * @param kNNModel the model for kNearestNeighbour
    * @param rankAggregationAlgorithm the rank aggregation algorithm for the aggregation of the
    *           neighbors
    * @param numberOfFeaturesDuringTheTrain the number of features during the prediction
    */
   public InstanceBasedLabelRankingLearningModel(LabelRankingDataset labelRankingDataset, KNearestNeighborLearningModel kNNModel,
         ILearningAlgorithm rankAggregationAlgorithm, int numberOfFeaturesDuringTheTrain) {
      super(numberOfFeaturesDuringTheTrain);
      this.labelRankingDataset = labelRankingDataset;
      this.kNNModel = kNNModel;
      this.rankAggregationAlgorithm = rankAggregationAlgorithm;
   }


   @Override
   public Ranking predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      assertInstanceHasCorrectType(instance, LabelRankingInstance.class);

      LabelRankingInstance labelRankingInstance = (LabelRankingInstance) instance;
      int[] neighborIndices = kNNModel.getNeighborIndeces(labelRankingInstance.getContextFeatureVector());
      List<Ranking> neighborRankings = getNeighborRankings(neighborIndices, labelRankingDataset);

      RankAggregationDataset neighborRankAggregationDataset = new RankAggregationDataset(labelRankingDataset.getCopyOfLabels(),
            neighborRankings);
      Ranking ranking = null;
      try {
         ranking = (Ranking) rankAggregationAlgorithm.train(neighborRankAggregationDataset).predict(neighborRankAggregationDataset).get(0);
      } catch (TrainModelsFailedException e) {
         throw new PredictionFailedException(ERROR_MESSAGE_BASE_LEARNER_TRAINING, e);
      }

      if (ranking == null) {
         throw new PredictionFailedException(ERROR_MESSAGE_BASE_LEARNER_TRAINING);
      }

      return ranking;
   }


   /**
    * Returns the ratings from the dataset for the given instance ids.
    *
    * @param neighborIndices the indices of the neighbors
    * @param dataset the training dataset
    * @return the rankings for the given ids in the dataset
    */
   private List<Ranking> getNeighborRankings(int[] neighborIndices, LabelRankingDataset dataset) {
      List<Ranking> rankings = new ArrayList<>(neighborIndices.length);
      for (int index : neighborIndices) {
         rankings.add(dataset.getRankingOfInstance(index));
      }
      return rankings;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((kNNModel == null) ? 0 : kNNModel.hashCode());
      result = prime * result + ((labelRankingDataset == null) ? 0 : labelRankingDataset.hashCode());
      result = prime * result + ((rankAggregationAlgorithm == null) ? 0 : rankAggregationAlgorithm.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof InstanceBasedLabelRankingLearningModel))
         return false;
      InstanceBasedLabelRankingLearningModel other = (InstanceBasedLabelRankingLearningModel) obj;
      if (kNNModel == null) {
         if (other.kNNModel != null)
            return false;
      } else if (!kNNModel.equals(other.kNNModel))
         return false;
      if (labelRankingDataset == null) {
         if (other.labelRankingDataset != null)
            return false;
      } else if (!labelRankingDataset.equals(other.labelRankingDataset))
         return false;
      if (rankAggregationAlgorithm == null) {
         if (other.rankAggregationAlgorithm != null)
            return false;
      } else if (!rankAggregationAlgorithm.equals(other.rankAggregationAlgorithm))
         return false;
      return true;
   }

}
