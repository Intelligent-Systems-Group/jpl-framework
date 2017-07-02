package de.upb.cs.is.jpl.api.evaluation.labelranking.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.labelranking.LabelRankingEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * 
 * {@link LabelRankingSuppliedTestsetEvaluation} is an evaluation class that is responsible for
 * evaluation associated with {@link EEvaluation#PERCENTAGE_SPLIT_LABEL_RANKING} value.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingSuppliedTestsetEvaluation extends ASuppliedTestSetEvaluation<LabelRankingSuppliedTestsetEvaluationConfiguration> {


   /**
    * Creates a new {@link LabelRankingSuppliedTestsetEvaluation} object with appropriate enum of
    * type {@link ELearningProblem} and instantiates its member variables.
    * 
    */
   public LabelRankingSuppliedTestsetEvaluation() {
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
      return new LabelRankingSuppliedTestsetEvaluationConfiguration();
   }
}
