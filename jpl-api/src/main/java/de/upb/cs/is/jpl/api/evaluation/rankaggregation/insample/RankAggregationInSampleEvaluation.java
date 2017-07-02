package de.upb.cs.is.jpl.api.evaluation.rankaggregation.insample;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.rankaggregation.RankAggregationEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This is evaluation class that is responsible for evaluation associated with
 * {@link EEvaluation#USE_TRAINING_DATASET_RANK_AGGREGATION} value.
 * 
 * @author Pritha Gupta
 *
 */
public class RankAggregationInSampleEvaluation extends AInSampleEvaluation<RankAggregationInSampleEvaluationConfiguration> {

   /**
    * Creates a new {@link RankAggregationInSampleEvaluation} object Rank Aggregation Enum
    * {@link ELearningProblem#RANK_AGGREGATION} and instantiates its member variables.
    * 
    */
   public RankAggregationInSampleEvaluation() {
      super(ELearningProblem.RANK_AGGREGATION);
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return RankAggregationEvaluationHelper.evaluateSingleCombination(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      RankAggregationInSampleEvaluationConfiguration evaluationConfiguration = new RankAggregationInSampleEvaluationConfiguration();
      evaluationConfiguration.setELearningProblem(eLearningProblem);
      return evaluationConfiguration;
   }


   @Override
   protected void init() {
      // nothing to initialize here
   }


}
