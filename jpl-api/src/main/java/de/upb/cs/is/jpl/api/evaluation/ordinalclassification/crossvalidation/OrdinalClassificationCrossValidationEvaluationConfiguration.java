package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.crossvalidation;


import de.upb.cs.is.jpl.api.evaluation.ACrossValidationEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for an implementation of
 * {@link OrdinalClassificationCrossValidationEvaluation}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationCrossValidationEvaluationConfiguration extends ACrossValidationEvaluationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "ordinalclassification" + StringUtils.FORWARD_SLASH
         + "ordinal_classification_cross_validation_evaluation";


   /**
    * Creates a new {@link OrdinalClassificationCrossValidationEvaluationConfiguration}.
    */
   public OrdinalClassificationCrossValidationEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
