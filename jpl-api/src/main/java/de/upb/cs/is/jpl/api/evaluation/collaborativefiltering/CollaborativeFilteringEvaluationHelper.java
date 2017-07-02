package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringInstance;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.IMetric;


/**
 * Helper class which is called from the different evaluation classes.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class CollaborativeFilteringEvaluationHelper {

   private CollaborativeFilteringEvaluationHelper() {
      // Nothing to do
   }


   /**
    * Evaluate for one single evaluation setting.
    * 
    * @param evaluationSetting the evaluation setting for which the evaluation is done
    * @return the created evalueation result
    * @throws PredictionFailedException if the evaluation encountered a problem while predicting an
    *            instance
    * @throws LossException if the evaluation encountered a problem while calculating the losses
    */
   @SuppressWarnings("unchecked")
   public static EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting)
         throws PredictionFailedException,
            LossException {
      EvaluationResult evaluationResult = new EvaluationResult();

      List<IMetric<?, ?>> evaluationMetrics = evaluationSetting.getMetrics();
      IDataset<?, ?, Double> testingDataset = (IDataset<?, ?, Double>) evaluationSetting.getDataset();
      ILearningModel<Double> model = (ILearningModel<Double>) evaluationSetting.getLearningModel();
      List<Double> predictions = new ArrayList<>();
      List<Double> expectedValues = new ArrayList<>();

      evaluationResult.setLearningAlgorithm(evaluationSetting.getLearningAlgorithm());
      evaluationResult.setDataset(evaluationSetting.getDataset());
      evaluationResult.setEvaluationMetrics(evaluationMetrics);

      for (IMetric<?, ?> rawMetric : evaluationMetrics) {
         IMetric<Double, Double> metric = (IMetric<Double, Double>) rawMetric;
         for (int i = 0; i < testingDataset.getNumberOfInstances(); i++) {
            CollaborativeFilteringInstance instance = (CollaborativeFilteringInstance) testingDataset.getInstance(i);
            expectedValues.add(instance.getRating());
            double prediction = model.predict(instance);
            predictions.add(prediction);
         }
         double loss = metric.getAggregatedLossForRatings(predictions, expectedValues);
         evaluationResult.addLossWithMetric(loss, metric);
      }
      return evaluationResult;
   }

}
