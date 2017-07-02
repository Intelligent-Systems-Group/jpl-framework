package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.bordacount;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDataset;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * This class implements the basic Borda-Count Algorithm for rank aggregation problem. It is linked
 * to the {@link Enum} value, {@link ELearningAlgorithm#BORDA_COUNT}.
 *
 * @author Andreas Kornelsen
 */
public class BordaCountLearningAlgorithm extends ALearningAlgorithm<BordaCountConfiguration> {

   private static final String ERROR_OPERATOR = "There is an unsupported ranking operator in the training dataset.";


   /**
    * Creates a new borda count learning algorithm with the enum identifier.
    */
   public BordaCountLearningAlgorithm() {
      super(ELearningAlgorithm.BORDA_COUNT.getIdentifier());
   }


   @Override
   public ADatasetParser getDatasetParser() {
      return new RankAggregationDatasetParser();
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      RankAggregationDataset rankAggregationDataset = (RankAggregationDataset) dataset;

      BordaCountConfiguration algorithmConfiguration = getAlgorithmConfiguration();

      List<Integer> labels = rankAggregationDataset.getLabels();
      Map<Integer, Double> scores = new HashMap<>();

      int numberOfInstances = rankAggregationDataset.getNumberOfInstances();
      for (int instanceIndex = 0; instanceIndex < numberOfInstances; instanceIndex++) {
         Ranking rating = rankAggregationDataset.getRankingOfInstance(instanceIndex);

         int[] comparativeOperators = rating.getCompareOperators();
         validateComparativeOperators(comparativeOperators);

         double countRanking = rankAggregationDataset.getCountForRankingOfInstance(instanceIndex);
         int[] ranking = rating.getObjectList();

         List<Integer> unsetLabels = new ArrayList<>();
         double scoreUnsetLabels = 0;

         int labelScore = rankAggregationDataset.getCountOfLabels();
         for (int label : ranking) {
            if (labels.contains(label)) {
               double var = countRanking * labelScore;
               if (scores.containsKey(label)) {
                  scores.put(label, scores.get(label) + var);
               } else {
                  scores.put(label, var);
               }
            } else {
               double var = countRanking * labelScore;
               scoreUnsetLabels += var;
               unsetLabels.add(label);
            }

            labelScore--;
         }

         if (algorithmConfiguration.isRatingWithMissedLabels() && !unsetLabels.isEmpty()) {
            double scoreForEachUnsetLabel = scoreUnsetLabels / unsetLabels.size();
            for (int label : unsetLabels) {
               if (scores.containsKey(label)) {
                  scores.put(label, scores.get(label) + scoreForEachUnsetLabel);
               } else {
                  scores.put(label, scoreForEachUnsetLabel);
               }
            }
         }

      }
      return new BordaCountLearningModel(scores, rankAggregationDataset.getLabels());
   }


   @Override
   public BordaCountLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (BordaCountLearningModel) super.train(dataset);
   }


   /**
    * Validates whether the operators are set correctly or not. The BordaCount Algorithm doesn't
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
      // There is no configuration required during the init phase.
   }


   @Override
   public AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new BordaCountConfiguration();
   }

}
