package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.percentagesplit;


import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for an implementation of
 * {@link OrdinalClassificationPercentageSplitEvaluation}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationPercentageSplitEvaluationConfiguration extends APercentageSplitEvaluationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "ordinalclassification" + StringUtils.FORWARD_SLASH
         + "ordinal_classification_percentage_split_evaluation";


   protected OrdinalClassificationPercentageSplitEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
