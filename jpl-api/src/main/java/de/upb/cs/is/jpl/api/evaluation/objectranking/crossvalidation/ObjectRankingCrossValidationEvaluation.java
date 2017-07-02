package de.upb.cs.is.jpl.api.evaluation.objectranking.crossvalidation;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluation;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.objectranking.ObjectRankingEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This class implements the cross validation evaluation for object ranking.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingCrossValidationEvaluation
      extends ACrossValidationEvaluation<ObjectRankingCrossValidationEvaluationConfiguration> {


   /**
    * Creates a new {@link ObjectRankingCrossValidationEvaluation} object with
    * {@link ELearningProblem#OBJECT_RANKING} and instantiates its member variables.
    * 
    */
   public ObjectRankingCrossValidationEvaluation() {
      super(ELearningProblem.OBJECT_RANKING);
   }


   @Override
   protected void init() {
      // nothing to initialize here
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return ObjectRankingEvaluationHelper.evaluateSingleCombination(evaluationSetting);

   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new ObjectRankingCrossValidationEvaluationConfiguration();
   }

}
