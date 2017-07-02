package de.upb.cs.is.jpl.api.evaluation.labelranking.crossvalidation;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluation;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.labelranking.LabelRankingEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * {@link LabelRankingCrossValidationEvaluation} is an evaluation class that is responsible for
 * evaluation associated with {@link EEvaluation#CROSS_VALIDATION_LABEL_RANKING} value.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingCrossValidationEvaluation extends ACrossValidationEvaluation<LabelRankingCrossValidationEvaluationConfiguration> {

   /**
    * Creates a new {@link LabelRankingCrossValidationEvaluation} object with appropriate enum of
    * type {@link ELearningProblem} and instantiates its member variables.
    * 
    */
   public LabelRankingCrossValidationEvaluation() {
      super(ELearningProblem.LABEL_RANKING);
   }


   @Override
   protected void init() {
      // nothing to initialize here
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return LabelRankingEvaluationHelper.evaluateSingleCombination(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new LabelRankingCrossValidationEvaluationConfiguration();
   }
}
