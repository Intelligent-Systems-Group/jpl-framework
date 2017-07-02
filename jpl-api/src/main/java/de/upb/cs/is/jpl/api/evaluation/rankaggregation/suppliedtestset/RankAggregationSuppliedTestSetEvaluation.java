package de.upb.cs.is.jpl.api.evaluation.rankaggregation.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.rankaggregation.RankAggregationEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This is evaluation class that is responsible for evaluation associated with
 * {@link EEvaluation#SUPPLIED_TEST_SET_RANK_AGGREGATION} value.
 * 
 * @see de.upb.cs.is.jpl.api.evaluation.AEvaluation
 * @author Pritha Gupta
 *
 */
public class RankAggregationSuppliedTestSetEvaluation extends ASuppliedTestSetEvaluation<RankAggregationSuppliedTestEvaluationConfiguration> {


   /**
    * Creates a new {@link RankAggregationSuppliedTestSetEvaluation} object Rank Aggregation Enum
    * {@link ELearningProblem#RANK_AGGREGATION} and instantiates its member variables.
    * 
    */
   public RankAggregationSuppliedTestSetEvaluation() {
      super(ELearningProblem.RANK_AGGREGATION);
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return RankAggregationEvaluationHelper.evaluateSingleCombination(evaluationSetting);
   }


   @Override
   protected RankAggregationSuppliedTestEvaluationConfiguration createDefaultEvaluationConfiguration() {
      RankAggregationSuppliedTestEvaluationConfiguration evaluationConfiguration = new RankAggregationSuppliedTestEvaluationConfiguration();
      evaluationConfiguration.setELearningProblem(eLearningProblem);
      return evaluationConfiguration;
   }


   @Override
   protected void init() {
      // nothing to initialize here

   }


}
