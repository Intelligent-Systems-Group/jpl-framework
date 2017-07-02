package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationResult;
import de.upb.cs.is.jpl.api.evaluation.EvaluationSetting;
import de.upb.cs.is.jpl.api.evaluation.ordinalclassification.OrdinalClassificationEvaluationHelper;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * This class is the implementation of {@link ASuppliedTestSetEvaluation} for the ordinal
 * classification learning problem, and is associated with
 * {@link EEvaluation#SUPPLIED_TEST_SET_ORDINAL_CLASSIFICATION}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationSuppliedTestSetEvaluation
      extends ASuppliedTestSetEvaluation<OrdinalClassificationSuppliedTestSetEvaluationConfiguration> {

   /**
    * Creates a new {@link OrdinalClassificationSuppliedTestSetEvaluation}.
    */
   public OrdinalClassificationSuppliedTestSetEvaluation() {
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
      return new OrdinalClassificationSuppliedTestSetEvaluationConfiguration();
   }

}
