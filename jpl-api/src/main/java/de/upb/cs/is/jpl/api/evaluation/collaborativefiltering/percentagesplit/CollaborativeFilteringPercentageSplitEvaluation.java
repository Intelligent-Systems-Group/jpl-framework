package de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.percentagesplit;


import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.collaborativefiltering.CollaborativeFilteringEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * Evaluates the results of a collaborative filtering algorithm by performing a percentage split of
 * the dataset.
 * 
 * @author Sebastian Osterbrink
 */
public class CollaborativeFilteringPercentageSplitEvaluation
      extends APercentageSplitEvaluation<CollaborativeFilteringPercentageSplitEvaluationConfiguration> {
   @SuppressWarnings("unused")
   private static final Logger logger = LoggerFactory.getLogger(CollaborativeFilteringPercentageSplitEvaluation.class);


   /**
    * Creates the Evaluation.
    */
   public CollaborativeFilteringPercentageSplitEvaluation() {
      super(ELearningProblem.COLLABORATIVE_FILTERING);
   }


   @Override
   protected void init() {
      evaluationResults = new ArrayList<>();
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return CollaborativeFilteringEvaluationHelper.evaluateSingleCombination(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new CollaborativeFilteringPercentageSplitEvaluationConfiguration();
   }

}
