package de.upb.cs.is.jpl.api.evaluation.objectranking.insample;


import de.upb.cs.is.jpl.api.evaluation.AInSampleEvaluationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class implements the in-sample evaluation for object ranking.
 * 
 * @author Pritha Gupta
 *
 */
public class ObjectRankingInSampleEvaluationConfiguration extends AInSampleEvaluationConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "objectranking" + StringUtils.FORWARD_SLASH
         + "object_ranking_in_sample_evaluation";


   /**
    * Creates a new object {@link ObjectRankingInSampleEvaluationConfiguration} configuration and
    * initialize it with default configuration provided in the file.
    */
   protected ObjectRankingInSampleEvaluationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }

}
