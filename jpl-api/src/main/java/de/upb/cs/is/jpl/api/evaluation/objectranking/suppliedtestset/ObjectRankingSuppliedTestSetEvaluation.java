package de.upb.cs.is.jpl.api.evaluation.objectranking.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.objectranking.ObjectRankingEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This class implemented the supplied test set evaluation for object ranking.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingSuppliedTestSetEvaluation
      extends ASuppliedTestSetEvaluation<ObjectRankingSuppliedTestSetEvaluationConfiguration> {


   /**
    * Creates a new {@link ObjectRankingSuppliedTestSetEvaluation} object with
    * {@link ELearningProblem#OBJECT_RANKING} and instantiates its member variables.
    * 
    */
   public ObjectRankingSuppliedTestSetEvaluation() {
      super(ELearningProblem.OBJECT_RANKING);
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return ObjectRankingEvaluationHelper.evaluateSingleCombination(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new ObjectRankingSuppliedTestSetEvaluationConfiguration();
   }


   @Override
   protected void init() {
      // nothing to initialize here

   }

}
