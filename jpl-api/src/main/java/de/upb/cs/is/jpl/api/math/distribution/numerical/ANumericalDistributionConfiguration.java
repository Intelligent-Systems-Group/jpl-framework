package de.upb.cs.is.jpl.api.math.distribution.numerical;


import de.upb.cs.is.jpl.api.math.distribution.ADistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the abstract configuration for a numerical distribution.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class ANumericalDistributionConfiguration extends ADistributionConfiguration {

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "numerical" + StringUtils.FORWARD_SLASH;


   /**
    * Creates an {@link ANumericalDistributionConfiguration}.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file, including the
    *           path inside the default configuration folder for distributions
    * @param pathToDefaultConfigurationLocation the location of the default configuration file
    */
   public ANumericalDistributionConfiguration(String defaultConfigurationFileName, String pathToDefaultConfigurationLocation) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION + pathToDefaultConfigurationLocation);
   }

}
