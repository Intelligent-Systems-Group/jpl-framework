package de.upb.cs.is.jpl.api.evaluation.instanceranking.insample;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.InstanceRankingEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * InstanceRankingInSampleEvaluation is evaluation class that is responsible for evaluation
 * associated with {@link EEvaluation#USE_TRAINING_DATASET_INSTANCE_RANKING} value.
 * 
 * @see de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluation
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingInSampleEvaluation extends AInSampleEvaluation<InstanceRankingInSampleEvaluationConfiguration> {

   /**
    * Creates a new {@link InstanceRankingInSampleEvaluation}.
    * 
    */
   public InstanceRankingInSampleEvaluation() {
      super(ELearningProblem.INSTANCE_RANKING);
   }


   @Override
   protected void init() {
      // nothing to initialize here
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return InstanceRankingEvaluationHelper.evaluateSingleCombinationHelper(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new InstanceRankingInSampleEvaluationConfiguration();
   }
}
