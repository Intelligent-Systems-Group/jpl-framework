package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.insample;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.OrdinalClassificationEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This class is the implementation of {@link AInSampleEvaluation} for the ordinal classification
 * learning problem, and is associated with
 * {@link EEvaluation#USE_TRAINING_DATASET_ORDINAL_CLASSIFICATION}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationInSampleEvaluation extends AInSampleEvaluation<OrdinalClassificationInSampleEvaluationConfiguration> {

   /**
    * Creates a new {@link OrdinalClassificationInSampleEvaluation}.
    */
   public OrdinalClassificationInSampleEvaluation() {
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
      return new OrdinalClassificationInSampleEvaluationConfiguration();
   }

}
