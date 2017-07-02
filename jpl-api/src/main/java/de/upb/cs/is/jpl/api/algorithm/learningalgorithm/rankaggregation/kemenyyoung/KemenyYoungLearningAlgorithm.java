package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.kemenyyoung;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDataset;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.util.TwoDMatrixUtils;


/**
 * 
 * This class implements the basic Kemeny-Young algorithm for rank aggregation learning problem
 * which used exhaustive search technique. It is linked to the {@link Enum} value,
 * {@link ELearningAlgorithm#KEMENEY_YOUNG}.
 * 
 * @author Pritha Gupta
 *
 */

public class KemenyYoungLearningAlgorithm extends ALearningAlgorithm<KemenyYoungConfiguration> {
   private static final Logger logger = LoggerFactory.getLogger(KemenyYoungLearningAlgorithm.class);
   private static final String ERROR_OPERATOR = "There is an unsupported ranking operator in the training dataset.";
   private static final String MODEL_TRAINED_SUCCESSFULLY = "The Kemeny-Young model is trained successfully on the dataset %s ";


   /**
    * Creates a new Kemeny-Young learning algorithm with the enum identifier.
    */
   public KemenyYoungLearningAlgorithm() {
      super(ELearningAlgorithm.KEMENEY_YOUNG.getIdentifier());
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      RankAggregationDataset rankAggregationDataset = (RankAggregationDataset) dataset;
      int numberOfInstances = rankAggregationDataset.getNumberOfInstances();
      int countLabel = rankAggregationDataset.getCountOfLabels();
      TwoDMatrixUtils kemenyYoungPairwiseScoreMatrix = new TwoDMatrixUtils(countLabel, countLabel);
      for (int instanceIndex = 0; instanceIndex < numberOfInstances; instanceIndex++) {
         IInstance<?, ?, Ranking> instance = rankAggregationDataset.getInstance(instanceIndex);
         Ranking rating = instance.getRating();
         int[] comparativeOperators = rating.getCompareOperators();
         validateComparativeOperators(comparativeOperators);
         double countRanking = rankAggregationDataset.getCountForRankingOfInstance(instanceIndex);
         int[] ranking = rating.getObjectList();
         for (int i = 0; i < ranking.length; i++) {
            for (int j = i + 1; j < ranking.length; j++) {
               kemenyYoungPairwiseScoreMatrix.addInExistingValue(ranking[i], ranking[j], (int) countRanking);
            }
         }
      }
      logger.debug(String.format(MODEL_TRAINED_SUCCESSFULLY, dataset.toString()));
      return new KemenyYoungLearningModel(kemenyYoungPairwiseScoreMatrix, rankAggregationDataset.getLabels());
   }


   @Override
   public KemenyYoungLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (KemenyYoungLearningModel) super.train(dataset);
   }


   /**
    * Validates whether the operators are set correctly or not. The Kemeny-Young Algorithm doesn't
    * work for all operators. It works for ordered relation.
    *
    * @param comparativeOperators the comparative operators
    * @throws TrainModelsFailedException if the order is not total
    */
   private void validateComparativeOperators(int[] comparativeOperators) throws TrainModelsFailedException {
      for (Integer comparator : comparativeOperators) {
         if (Ranking.COMPARABLE_ENCODING != comparator) {
            throw new TrainModelsFailedException(ERROR_OPERATOR);
         }
      }
   }


   @Override
   public void init() {
      // function not applicable here
   }


   @Override
   public ADatasetParser getDatasetParser() {
      return new RankAggregationDatasetParser();
   }


   @Override
   public AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new KemenyYoungConfiguration();
   }
}
