package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.perceptron;


import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.LinearClassificationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for a implementation of
 * {@link PerceptronLearningAlgorithm}.
 * 
 * @author Tanja Tornede
 *
 */
public class PerceptronConfiguration extends LinearClassificationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "baselearner" + StringUtils.FORWARD_SLASH + "classification"
         + StringUtils.FORWARD_SLASH + "linear" + StringUtils.FORWARD_SLASH + "perceptron";


   /**
    * Creates a default configuration for {@link PerceptronLearningAlgorithm}.
    */
   public PerceptronConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }
}
