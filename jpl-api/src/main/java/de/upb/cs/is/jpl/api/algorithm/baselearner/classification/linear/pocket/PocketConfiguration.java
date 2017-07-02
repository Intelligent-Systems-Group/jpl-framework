package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.pocket;


import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.LinearClassificationConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration contains all parameters for a implementation of
 * {@link PocketLearningAlgorithm}.
 * 
 * @author Tanja Tornede
 *
 */
public class PocketConfiguration extends LinearClassificationConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "baselearner" + StringUtils.FORWARD_SLASH + "classification"
         + StringUtils.FORWARD_SLASH + "linear" + StringUtils.FORWARD_SLASH + "pocket";


   /**
    * Creates a default configuration for {@link PocketLearningAlgorithm}.
    */
   public PocketConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }
}
