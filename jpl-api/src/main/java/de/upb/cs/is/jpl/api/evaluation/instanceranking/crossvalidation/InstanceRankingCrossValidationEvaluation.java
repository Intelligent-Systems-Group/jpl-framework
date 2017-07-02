package de.upb.cs.is.jpl.api.evaluation.instanceranking.crossvalidation;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluation;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.InstanceRankingEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * InstanceRankingCrossValidationEvaluation is evaluation class that is responsible for evaluation
 * associated with {@link EEvaluation#CROSS_VALIDATION_INSTANCE_RANKING} value.
 * 
 * @see de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluation
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingCrossValidationEvaluation extends ACrossValidationEvaluation<InstanceRankingCrossValidationConfiguration> {

   /**
    * Creates a new {@link InstanceRankingCrossValidationEvaluation}.
    * 
    */
   public InstanceRankingCrossValidationEvaluation() {
      super(ELearningProblem.INSTANCE_RANKING);
   }


   @Override
   protected void init() {
      // Empty method
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return InstanceRankingEvaluationHelper.evaluateSingleCombinationHelper(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new InstanceRankingCrossValidationConfiguration();
   }

}
