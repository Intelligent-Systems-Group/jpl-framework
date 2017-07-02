package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.CollaborativeFilteringEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * Evaluates the results of a collaborative filtering algorithm on a fixed testing dataset.
 * 
 * @author Sebastian Osterbrink
 */
public class CollaborativeFilteringSuppliedTestSetEvaluation
      extends ASuppliedTestSetEvaluation<CollaborativeFilteringSuppliedTestSetEvaluationConfiguration> {

   /**
    * Creates the Evaluation.
    */
   public CollaborativeFilteringSuppliedTestSetEvaluation() {
      super(ELearningProblem.COLLABORATIVE_FILTERING);
   }


   @Override
   protected void init() {
      // nothing to do
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return CollaborativeFilteringEvaluationHelper.evaluateSingleCombination(evaluationSetting);

   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new CollaborativeFilteringSuppliedTestSetEvaluationConfiguration();
   }

}
