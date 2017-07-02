package de.upb.cs.is.jpl.api.evaluation.instanceranking.suppliedtestset;


import java.util.ArrayList;

import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.instanceranking.InstanceRankingEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * InstanceRankingTestSetEvaluation is evaluation class that is responsible for evaluation
 * associated with {@link EEvaluation#SUPPLIED_TEST_SET_INSTANCE_RANKING} value.
 * 
 * @see de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluation
 * @author Sebastian Gottschalk
 *
 */
public class InstanceRankingSuppliedTestsetEvaluation
      extends ASuppliedTestSetEvaluation<InstanceRankingSuppliedTestsetEvaluationConfiguration> {

   /**
    * Creates a new {@link InstanceRankingSuppliedTestsetEvaluation}.
    * 
    */
   public InstanceRankingSuppliedTestsetEvaluation() {
      super(ELearningProblem.INSTANCE_RANKING);
   }


   @Override
   protected void init() {
      this.evaluationResults = new ArrayList<>();

   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return InstanceRankingEvaluationHelper.evaluateSingleCombinationHelper(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new InstanceRankingSuppliedTestsetEvaluationConfiguration();

   }
}
