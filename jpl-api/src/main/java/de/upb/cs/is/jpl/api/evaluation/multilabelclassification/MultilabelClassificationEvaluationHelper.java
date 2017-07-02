package de.upb.cs.is.jpl.api.evaluation.multilabelclassification;


import java.util.List;

import de.upb.cs.is.jpl.api.dataset.multilabelclassification.MultilabelClassificationDataset;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This static helper class introduces simple way to implement different evaluations for multilabel
 * classification.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationEvaluationHelper {

   /**
    * Hides the public constructor.
    */
   private MultilabelClassificationEvaluationHelper() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Evaluate single combination for all multilabel classification evaluations.
    *
    * @param evaluationSetting the evaluation setting to evaluate on
    * 
    * @return the result of the evaluation
    * 
    * @throws LossException if an error occurs during the computation of the loss
    * @throws PredictionFailedException if an error occurs during the prediction
    */
   public static EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting)
         throws LossException,
            PredictionFailedException {
      EvaluationResult evaluationResult = new EvaluationResult();

      MultilabelClassificationDataset multilabelClassificationDataset = (MultilabelClassificationDataset) evaluationSetting.getDataset();


      List<SparseDoubleVector> expectedRatings = multilabelClassificationDataset.getCorrectResults();
      @SuppressWarnings("unchecked")
      List<SparseDoubleVector> predictedRatings = (List<SparseDoubleVector>) evaluationSetting.getLearningModel()
            .predict(multilabelClassificationDataset);

      for (IMetric<?, ?> evaluationMetric : evaluationSetting.getMetrics()) {
         @SuppressWarnings("unchecked")
         IMetric<SparseDoubleVector, Double> castedEvaluationMetric = (IMetric<SparseDoubleVector, Double>) evaluationMetric;
         double loss = castedEvaluationMetric.getAggregatedLossForRatings(expectedRatings, predictedRatings);
         evaluationResult.addLossWithMetric(loss, castedEvaluationMetric);
      }


      evaluationResult.setLearningAlgorithm(evaluationSetting.getLearningAlgorithm());
      evaluationResult.setDataset(evaluationSetting.getDataset());
      evaluationResult.setExtraEvaluationInformation(StringUtils.EMPTY_STRING);
      return evaluationResult;
   }

}
