package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.pairwiserankingalgorithm;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine.SupportVectorMachineClassification;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.AObjectRankingWithBaseLearner;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * This class implements the basic pair wise ranking algorithm for Object Ranking learning problem.
 * It is linked to the {@link Enum} value, {@link ELearningAlgorithm#PAIRWISE_RANKING}. This
 * algorithm taken {@link SupportVectorMachineClassification} as the base learner, which is called
 * RankSVM approach.
 * 
 * @author Pritha Gupta
 *
 */
public class PairwiseRankingLearningAlgorithm extends AObjectRankingWithBaseLearner<PairwiseRankingConfiguration> {


   /**
    * Creates a pairwise ranking learning algorithm with the enum identifier.
    */
   public PairwiseRankingLearningAlgorithm() {
      super(ELearningAlgorithm.PAIRWISE_RANKING.getIdentifier());
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new PairwiseRankingConfiguration();
   }


   @Override
   protected void setBaselearnerDataset(ObjectRankingDataset objectRankingDataset) {
      baselearnerDataset = PairwiseRankingTransformerUtils.createBaseLearnerDatasetFromObjectRankingDataset(objectRankingDataset,
            configuration.getMethodTypeToGenerateClassificationDataset(), configuration.getLambdaValue());
   }


   @Override
   protected ILearningModel<?> createObjectRankingLearningModel(int numberofItemFeatures, int numberOfContextFeatures) {
      return new PairwiseRankingLearningModel(baseLearningModel, numberofItemFeatures, numberOfContextFeatures,
            configuration.getMethodTypeToGenerateClassificationDataset());
   }


   @Override
   public PairwiseRankingLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (PairwiseRankingLearningModel) super.train(dataset);
   }

}
