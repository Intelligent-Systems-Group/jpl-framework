package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.insample;


import de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for an implementation of
 * {@link OrdinalClassificationInSampleEvaluation}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationInSampleEvaluationConfiguration extends AInSampleEvaluationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "ordinalclassification" + StringUtils.FORWARD_SLASH
         + "ordinal_classification_in_sample_evaluation";


   protected OrdinalClassificationInSampleEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
