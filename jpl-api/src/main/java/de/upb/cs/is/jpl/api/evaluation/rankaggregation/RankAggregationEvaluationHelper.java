package de.upb.cs.is.jpl.api.evaluation.rankaggregation;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.rankaggregation.RankAggregationDataset;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.rankaggregation.insample.RankAggregationInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.rankaggregation.suppliedtestset.RankAggregationSuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class contains the helper method for the evaluations
 * {@link RankAggregationInSampleEvaluation} and{@link RankAggregationSuppliedTestSetEvaluation}.
 * 
 * @author Pritha Gupta
 */
public class RankAggregationEvaluationHelper {

   private static final String LOSS_CANNOTBE_CALCULATED_FOR_EVALUATION_METRIC = "Loss cannot be calculated for the evaluation metric %s, due to error %s, for evaluation setting: %s.";
   private static final Logger logger = LoggerFactory.getLogger(RankAggregationEvaluationHelper.class);


   /**
    * Instantiates a new private {@link RankAggregationEvaluationHelper} helper to hide the public
    * one.
    */
   private RankAggregationEvaluationHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Evaluate single combination for all rank aggregation evaluations.
    *
    * @param evaluationSetting the evaluation setting
    * @return the evaluation result
    * @throws LossException if an error occurs during the computation of the loss
    * @throws PredictionFailedException if an error occurs during the prediction
    */
   @SuppressWarnings("unchecked")
   public static EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting)
         throws LossException,
            PredictionFailedException {
      EvaluationResult evaluationResult = new EvaluationResult();
      try {
         evaluationResult.setLearningAlgorithm(evaluationSetting.getLearningAlgorithm());
         evaluationResult.setDataset(evaluationSetting.getDataset());
         RankAggregationDataset rankAggregationDataset = (RankAggregationDataset) evaluationSetting.getDataset();
         Ranking predicted = (Ranking) evaluationSetting.getLearningModel().predict(rankAggregationDataset).get(0);
         List<Ranking> predictedRankings = new ArrayList<>();
         List<Ranking> expectedRankings = new ArrayList<>();
         List<Double> weights = new ArrayList<>();

         for (int i = 0; i < rankAggregationDataset.getNumberOfInstances(); i++) {
            predictedRankings.add(predicted);
            expectedRankings.add(rankAggregationDataset.getInstance(i).getRating());
            weights.add((double) rankAggregationDataset.getCountForRankingOfInstance(i));
         }

         for (IMetric<?, ?> evaluationMetric : evaluationSetting.getMetrics()) {
            IMetric<Ranking, Double> castedEvaluationMetric = (IMetric<Ranking, Double>) evaluationMetric;
            double loss = castedEvaluationMetric.getWeightedAggregatedLossForRatings(weights, expectedRankings, predictedRankings);
            evaluationResult.addLossWithMetric(loss, castedEvaluationMetric);
         }
         evaluationResult.setExtraEvaluationInformation(predicted.toString());
      } catch (LossException exception) {
         logger.error(exception.getMessage(), exception);
         throw new LossException(String.format(LOSS_CANNOTBE_CALCULATED_FOR_EVALUATION_METRIC, evaluationSetting.getMetrics(),
               exception.getMessage(), evaluationSetting.toString()), exception.getCause());
      }
      return evaluationResult;
   }

}
