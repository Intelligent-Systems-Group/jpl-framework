package de.upb.cs.is.jpl.api.evaluation.objectranking;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.rankaggregation.RankAggregationEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This static helper class introduces simple way to implement different evaluations for object
 * ranking learning problem.
 * 
 * @author Pritha Gupta
 */
public class ObjectRankingEvaluationHelper {


   private static final String LOSS_CANNOTBE_CALCULATED_FOR_EVALUATION_METRIC = "Loss cannot be calculated for the evaluation metric %s, due to error %s, for evaluation setting: %s.";
   private static final Logger logger = LoggerFactory.getLogger(RankAggregationEvaluationHelper.class);


   /**
    * Instantiates a new private {@link ObjectRankingEvaluationHelper} helper to hide the public
    * one.
    */
   private ObjectRankingEvaluationHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Evaluate single combination for all object ranking evaluations.
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
         ObjectRankingDataset objectRankingDataset = (ObjectRankingDataset) evaluationSetting.getDataset();
         evaluationResult.setDataset(objectRankingDataset);
         List<Ranking> predictedRankings = (List<Ranking>) evaluationSetting.getLearningModel().predict(objectRankingDataset);
         List<Ranking> expectedRankings = objectRankingDataset.getRankings();
         for (IMetric<?, ?> evaluationMetric : evaluationSetting.getMetrics()) {
            IMetric<Ranking, Double> castedEvaluationMetric = (IMetric<Ranking, Double>) evaluationMetric;
            double loss = castedEvaluationMetric.getAggregatedLossForRatings(expectedRankings, predictedRankings);
            evaluationResult.addLossWithMetric(loss, castedEvaluationMetric);
         }
         evaluationResult.setExtraEvaluationInformation(StringUtils.EMPTY_STRING);
      } catch (LossException exception) {
         logger.error(exception.getMessage(), exception);
         throw new LossException(String.format(LOSS_CANNOTBE_CALCULATED_FOR_EVALUATION_METRIC, evaluationSetting.getMetrics(),
               exception.getMessage(), evaluationSetting.toString()), exception.getCause());
      }
      return evaluationResult;
   }
}
