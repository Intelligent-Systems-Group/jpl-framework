package de.upb.cs.is.jpl.api.evaluation.ordinalclassification.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for an implementation of
 * {@link ASuppliedTestSetEvaluationConfiguration}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationSuppliedTestSetEvaluationConfiguration extends ASuppliedTestSetEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "ordinalclassification" + StringUtils.FORWARD_SLASH
         + "ordinal_classification_supplied_test_set_evaluation";


   protected OrdinalClassificationSuppliedTestSetEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
