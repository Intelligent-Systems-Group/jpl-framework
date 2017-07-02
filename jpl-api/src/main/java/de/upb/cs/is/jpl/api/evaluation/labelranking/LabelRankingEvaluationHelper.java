package de.upb.cs.is.jpl.api.evaluation.labelranking;


import java.util.List;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.dataset.labelranking.LabelRankingDataset;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.labelranking.crossvalidation.LabelRankingCrossValidationEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.insample.LabelRankingInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.percentagesplit.LabelRankingPercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.evaluation.labelranking.suppliedtestset.LabelRankingSuppliedTestsetEvaluation;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class contains the helper method for the evaluations
 * {@link LabelRankingSuppliedTestsetEvaluation}, {@link LabelRankingInSampleEvaluation},
 * {@link LabelRankingPercentageSplitEvaluation} and {@link LabelRankingCrossValidationEvaluation}
 * 
 * @author Andreas Kornelsen
 */
public class LabelRankingEvaluationHelper {


   /**
    * Hides the public constructor.
    */
   private LabelRankingEvaluationHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Evaluate single combination for all label ranking evaluations.
    *
    * @param evaluationSetting the evaluation setting
    * @return the evaluation result
    * @throws LossException if an error occurs during the computation of the loss
    * @throws PredictionFailedException if an error occurs during the prediction
    */
   public static EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting)
         throws LossException,
            PredictionFailedException {
      EvaluationResult evaluationResult = new EvaluationResult();

      LabelRankingDataset labelRankingDataset = (LabelRankingDataset) evaluationSetting.getDataset();

      @SuppressWarnings("unchecked")
      List<Ranking> predictedRankings = (List<Ranking>) evaluationSetting.getLearningModel().predict(evaluationSetting.getDataset());
      List<Ranking> expectedRankings = labelRankingDataset.getCopyOfRankings();

      for (IMetric<?, ?> evaluationMetric : evaluationSetting.getMetrics()) {
         @SuppressWarnings("unchecked")
         IMetric<Ranking, Double> castedEvaluationMetric = (IMetric<Ranking, Double>) evaluationMetric;
         double loss = castedEvaluationMetric.getAggregatedLossForRatings(expectedRankings, predictedRankings);

         evaluationResult.addLossWithMetric(loss, castedEvaluationMetric);
      }

      evaluationResult.setLearningAlgorithm(evaluationSetting.getLearningAlgorithm());
      evaluationResult.setDataset(evaluationSetting.getDataset());
      evaluationResult.setExtraEvaluationInformation(StringUtils.EMPTY_STRING);
      return evaluationResult;
   }

}
