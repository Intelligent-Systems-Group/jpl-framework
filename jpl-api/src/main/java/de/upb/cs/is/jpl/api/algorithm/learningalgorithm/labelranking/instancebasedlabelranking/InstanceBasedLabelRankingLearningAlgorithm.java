package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.instancebasedlabelranking;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor.KNearestNeighborClassification;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor.KNearestNeighborConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor.KNearestNeighborLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.plackettluce.PlackettLuceLearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingDataset;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * The instance-based label ranking algorithm computes the k nearest neighbors of a new query
 * instance in the training dataset. The rankings of this neighbors are aggregated with a rank
 * aggregation algorithm, for example with the {@link PlackettLuceLearningAlgorithm}. The output of
 * the rank aggregation is the prediction of the query instance.
 * 
 * @author Andreas Kornelsen
 */
public class InstanceBasedLabelRankingLearningAlgorithm extends ALearningAlgorithm<InstanceBasedLabelRankingConfiguration> {

   /**
    * Creates a new instance based label ranking learning algorithm with the enum identifier.
    */
   public InstanceBasedLabelRankingLearningAlgorithm() {
      super(ELearningAlgorithm.IB_LR.getIdentifier());
   }


   @Override
   public IDatasetParser getDatasetParser() {
      return new LabelRankingDatasetParser();
   }


   @Override
   public void init() {
      // The init method is not required for this algorithm.
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new InstanceBasedLabelRankingConfiguration();
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      LabelRankingDataset labelRankingDataset = (LabelRankingDataset) dataset;

      BaselearnerDataset baselearnerDatasetWithFeaturesAndIdsAsPredictor = labelRankingDataset
            .getBaselearnerDatasetWithFeaturesAndIdsAsPredictor();


      InstanceBasedLabelRankingConfiguration algorithmConfiguration = getAlgorithmConfiguration();
      KNearestNeighborConfiguration kNearestNeighborConfiguration = (KNearestNeighborConfiguration) algorithmConfiguration
            .getBaseLearnerAlgorithm().getAlgorithmConfiguration();
      KNearestNeighborClassification kNearestNeighborClassification = new KNearestNeighborClassification(
            kNearestNeighborConfiguration.getNumberOfNeighbors());
      KNearestNeighborLearningModel kNNModel = kNearestNeighborClassification.train(baselearnerDatasetWithFeaturesAndIdsAsPredictor);

      ILearningAlgorithm rankAggregationAlgorithm = ((InstanceBasedLabelRankingConfiguration) getDefaultAlgorithmConfiguration())
            .getRankAggregationAlgorithm();

      return new InstanceBasedLabelRankingLearningModel(labelRankingDataset, kNNModel, rankAggregationAlgorithm,
            labelRankingDataset.getNumberOfFeatures());
   }


   @Override
   public InstanceBasedLabelRankingLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (InstanceBasedLabelRankingLearningModel) super.train(dataset);
   }


   @Override
   public boolean equals(Object object) {
      if (this == object)
         return true;
      if (!super.equals(object))
         return false;
      if (object instanceof InstanceBasedLabelRankingLearningAlgorithm) {
         InstanceBasedLabelRankingLearningAlgorithm comparedObject = (InstanceBasedLabelRankingLearningAlgorithm) object;
         return this.configuration.equals(comparedObject.getAlgorithmConfiguration());
      }
      return false;
   }


   @Override
   public int hashCode() {
      return super.hashCode() + 31 * configuration.hashCode();
   }
}
