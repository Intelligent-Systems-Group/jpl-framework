package de.upb.cs.is.jpl.api.evaluation.multilabelclassification.crossvalidation;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluation;
import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.multilabelclassification.MultilabelClassificationEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This class is the implementation of {@link ACrossValidationEvaluation} for the multilabel
 * classification learning problem.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationCrossValidationEvaluation extends ACrossValidationEvaluation<ACrossValidationEvaluationConfiguration> {

   /**
    * Creates a new {@link MultilabelClassificationCrossValidationEvaluation}.
    */
   public MultilabelClassificationCrossValidationEvaluation() {
      super(ELearningProblem.MULTILABEL_CLASSIFICATION);
   }


   @Override
   protected void init() {
      // nothing to do
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return MultilabelClassificationEvaluationHelper.evaluateSingleCombination(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new MultilabelClassificationCrossValidationEvaluationConfiguration();
   }

}
