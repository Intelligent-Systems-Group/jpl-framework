package de.upb.cs.is.jpl.api.evaluation.multilabelclassification.crossvalidation;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the {@link AEvaluationConfiguration} used for the
 * {@link MultilabelClassificationCrossValidationEvaluation}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationCrossValidationEvaluationConfiguration extends ACrossValidationEvaluationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "multilabelclassification" + StringUtils.FORWARD_SLASH
         + "multilabel_classification_cross_validation_evaluation";


   /**
    * Creates a new {@link MultilabelClassificationCrossValidationEvaluationConfiguration}.
    */
   public MultilabelClassificationCrossValidationEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
