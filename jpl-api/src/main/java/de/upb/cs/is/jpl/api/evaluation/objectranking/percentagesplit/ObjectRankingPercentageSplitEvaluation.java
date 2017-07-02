package de.upb.cs.is.jpl.api.evaluation.objectranking.percentagesplit;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.objectranking.ObjectRankingEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This class implements the percentage split evaluation for object ranking.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingPercentageSplitEvaluation
      extends APercentageSplitEvaluation<ObjectRankingPercentageSplitEvaluationConfiguration> {


   /**
    * Creates a new {@link ObjectRankingPercentageSplitEvaluation} object with
    * {@link ELearningProblem#OBJECT_RANKING} and instantiates its member variables.
    * 
    */
   public ObjectRankingPercentageSplitEvaluation() {
      super(ELearningProblem.OBJECT_RANKING);
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return ObjectRankingEvaluationHelper.evaluateSingleCombination(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new ObjectRankingPercentageSplitEvaluationConfiguration();
   }


   @Override
   protected void init() {
      // nothing to initialize here

   }

}
