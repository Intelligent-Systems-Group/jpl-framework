package de.upb.cs.is.jpl.api.evaluation;


/**
 * The abstract class for evaluating the Algorithm(s) by using cross-validation technique and
 * storing the list of {@link EvaluationResult} of the current running evaluation. You should always
 * call up to your superclass when implementing these methods:
 * <ul>
 * <li>validateParameters()</li>
 * <li>copyValues(IJsonConfiguration configuration)</li>
 * </ul>
 * 
 * @author Pritha Gupta
 * @see AEvaluationConfiguration
 * 
 */
public class AInSampleEvaluationConfiguration extends AEvaluationConfiguration {
   private static final String IN_SAMPLE_MESSAGE = "In-Sample evaluation for %s.";


   /**
    * Creates an abstract in-sample evaluation configuration and initialize it with default
    * configuration provided in the file.
    * 
    * @param defaultConfigurationFileName the default configuration file name
    */
   protected AInSampleEvaluationConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName);
   }


   @Override
   public String toString() {
      return String.format(IN_SAMPLE_MESSAGE, this.eLearningProblem.getLearningProblemIdentifier());
   }

}
