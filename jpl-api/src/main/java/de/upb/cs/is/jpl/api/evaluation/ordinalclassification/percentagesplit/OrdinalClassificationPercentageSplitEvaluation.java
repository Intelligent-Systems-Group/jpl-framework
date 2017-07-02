package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.percentagesplit;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.OrdinalClassificationEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This class is the implementation of {@link APercentageSplitEvaluation} for the ordinal
 * classification learning problem, and is associated with
 * {@link EEvaluation#PERCENTAGE_SPLIT_ORDINAL_CLASSIFICATION}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationPercentageSplitEvaluation
      extends APercentageSplitEvaluation<OrdinalClassificationPercentageSplitEvaluationConfiguration> {

   /**
    * Creates a new {@link OrdinalClassificationPercentageSplitEvaluation}.
    */
   public OrdinalClassificationPercentageSplitEvaluation() {
      super(ELearningProblem.ORDINAL_CLASSIFICATION);
   }


   @Override
   protected void init() {
      // nothing to do here
   }


   @Override
   public EvaluationResult evaluateSingleCombination(EvaluationSetting evaluationSetting) throws LossException, PredictionFailedException {
      return OrdinalClassificationEvaluationHelper.evaluateSingleCombination(evaluationSetting);
   }


   @Override
   protected AEvaluationConfiguration createDefaultEvaluationConfiguration() {
      return new OrdinalClassificationPercentageSplitEvaluationConfiguration();
   }

}
