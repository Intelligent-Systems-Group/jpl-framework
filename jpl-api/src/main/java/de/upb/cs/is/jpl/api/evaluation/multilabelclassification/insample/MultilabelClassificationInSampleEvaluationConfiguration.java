package de.upb.cs.is.jpl.api.evaluation.multilabelclassification.insample;


import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the {@link AEvaluationConfiguration} used for the
 * {@link MultilabelClassificationInSampleEvaluation}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MultilabelClassificationInSampleEvaluationConfiguration extends AInSampleEvaluationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "multilabelclassification" + StringUtils.FORWARD_SLASH
         + "multilabel_classification_in_sample_evaluation";


   /**
    * Creates a {@link MultilabelClassificationInSampleEvaluationConfiguration}.
    */
   public MultilabelClassificationInSampleEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
