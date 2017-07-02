package de.upb.cs.is.jpl.api.evaluation.multilabelclassification.percentagesplit;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.APercentageSplitEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the {@link AEvaluationConfiguration} used for the
 * {@link MultilabelClassificationPercentageSplitEvaluation}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationPercentageSplitEvaluationConfiguration extends APercentageSplitEvaluationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "multilabelclassification" + StringUtils.FORWARD_SLASH
         + "multilabel_classification_percentage_split_evaluation";


   /**
    * Creates a new {@link MultilabelClassificationPercentageSplitEvaluationConfiguration}.
    */
   public MultilabelClassificationPercentageSplitEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
