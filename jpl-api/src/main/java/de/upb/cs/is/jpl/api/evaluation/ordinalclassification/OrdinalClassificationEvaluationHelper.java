package de.upb.cs.is.jpl.api.evaluation.ordinalclassification;


import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDataset;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This static helper class introduces simple way to implement different evaluations for ordinal
 * classification.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationEvaluationHelper {

   /**
    * Hides the public constructor.
    */
   private OrdinalClassificationEvaluationHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Evaluate single combination for all ordinal classification evaluations.
    *
    * @param evaluationSetting the evaluation setting
    * 
    * @return the evaluation result
    * 
    * @throws LossException if an error occurs during the computation of the loss
    * @throws PredictionFailedException if an error occurs during the prediction
    */
   public static EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting)
         throws LossException,
            PredictionFailedException {
      OrdinalClassificationDataset ordinalClassificationDataset = (OrdinalClassificationDataset) evaluationSetting.getDataset();
      List<Double> expectedRatings = ordinalClassificationDataset.getRatings();

      @SuppressWarnings("unchecked")
      List<Double> predictedRatings = (List<Double>) evaluationSetting.getLearningModel().predict(ordinalClassificationDataset);

      EvaluationResult evaluationResult = new EvaluationResult();
      for (IMetric<?, ?> evaluationMetric : evaluationSetting.getMetrics()) {
         @SuppressWarnings("unchecked")
         IMetric<Double, Double> castedEvaluationMetric = (IMetric<Double, Double>) evaluationMetric;

         double loss = castedEvaluationMetric.getAggregatedLossForRatings(expectedRatings, predictedRatings);
         evaluationResult.addLossWithMetric(loss, castedEvaluationMetric);
      }
      evaluationResult.setLearningAlgorithm(evaluationSetting.getLearningAlgorithm());
      evaluationResult.setDataset(evaluationSetting.getDataset());
      evaluationResult.setExtraEvaluationInformation(StringUtils.EMPTY_STRING);
      return evaluationResult;
   }

}
