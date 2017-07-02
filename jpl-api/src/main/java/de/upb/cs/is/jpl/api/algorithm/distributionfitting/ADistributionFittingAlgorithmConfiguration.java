package de.upb.cs.is.jpl.api.algorithm.distributionfitting;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the abstract configuration for a {@link ADistributionFittingAlgorithm}.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class ADistributionFittingAlgorithmConfiguration extends AAlgorithmConfiguration {

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = StringUtils.FORWARD_SLASH + "distributionfitting"
         + StringUtils.FORWARD_SLASH;


   /**
    * Creates an {@link ADistributionFittingAlgorithmConfiguration} for the distribution fitting
    * algorithms.
    *
    * @param defaultConfigurationFileName the name of the default configuration file
    */
   public ADistributionFittingAlgorithmConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
   }


   /**
    * Creates an {@link ADistributionFittingAlgorithmConfiguration}.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file, including the
    *           path inside the default configuration folder for distribution fitting algorithms
    * @param pathToDefaultConfigurationLocation the location of the default configuration file
    */
   public ADistributionFittingAlgorithmConfiguration(String defaultConfigurationFileName, String pathToDefaultConfigurationLocation) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION + pathToDefaultConfigurationLocation);
   }

}
