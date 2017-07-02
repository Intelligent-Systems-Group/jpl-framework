package de.upb.cs.is.jpl.api.evaluation.multilabelclassification.insample;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.MultilabelClassificationEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This class is the implementation of {@link AInSampleEvaluation} for the multilabel classification
 * learning problem.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationInSampleEvaluation
      extends AInSampleEvaluation<MultilabelClassificationInSampleEvaluationConfiguration> {

   /**
    * Creates a {@link MultilabelClassificationInSampleEvaluation}.
    */
   public MultilabelClassificationInSampleEvaluation() {
      super(ELearningProblem.MULTILABEL_CLASSIFICATION);
   }


   @Override
   protected void init() {
      // not needed
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return MultilabelClassificationEvaluationHelper.evaluateSingleCombination(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new MultilabelClassificationInSampleEvaluationConfiguration();
   }

}
