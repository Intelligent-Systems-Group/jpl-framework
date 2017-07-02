package de.upb.cs.is.jpl.api.evaluation.multilabelclassification.suppliedtestset;


import de.upb.cs.is.jpl.api.evaluation.ASuppliedTestSetEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the {@link ASuppliedTestSetEvaluationConfiguration} used for the
 * {@link MultilabelClassificationSuppliedTestsetEvaluation}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationSuppliedTestsetEvaluationConfiguration extends ASuppliedTestSetEvaluationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "multilabelclassification" + StringUtils.FORWARD_SLASH
         + "multilabel_classification_supplied_testset_evaluation";


   /**
    * Creates a new {@link MultilabelClassificationSuppliedTestsetEvaluationConfiguration}.
    */
   public MultilabelClassificationSuppliedTestsetEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
