package de.upb.cs.is.jpl.api.algorithm.distributionfitting.ranking;


import de.upb.cs.is.jpl.api.algorithm.distributionfitting.ADistributionFittingAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the abstract configuration for a
 * {@link ARankingDistributionFittingAlgorithm}.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class ARankingDistributionFittingAlgorithmConfiguration extends ADistributionFittingAlgorithmConfiguration {

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "special" + StringUtils.FORWARD_SLASH;


   /**
    * Creates an {@link ARankingDistributionFittingAlgorithmConfiguration} for the distribution
    * fitting algorithms.
    *
    * @param defaultConfigurationFileName the name of the default configuration file
    */
   public ARankingDistributionFittingAlgorithmConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
   }


   /**
    * Creates an {@link ARankingDistributionFittingAlgorithmConfiguration}.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file, including the
    *           path inside the default configuration folder for distribution fitting algorithms
    * @param pathToDefaultConfigurationLocation the location of the default configuration file
    */
   public ARankingDistributionFittingAlgorithmConfiguration(String defaultConfigurationFileName,
         String pathToDefaultConfigurationLocation) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION + pathToDefaultConfigurationLocation);
   }


}
