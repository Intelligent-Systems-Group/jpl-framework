package de.upb.cs.is.jpl.api.math.distribution.special;


import de.upb.cs.is.jpl.api.math.distribution.ADistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the abstract configuration for a {@link ASpecialDistribution}.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class ASpecialDistributionConfiguration extends ADistributionConfiguration {

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "special" + StringUtils.FORWARD_SLASH;


   /**
    * Creates an {@link ASpecialDistributionConfiguration}.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file, including the
    *           path inside the default configuration folder for distributions
    */
   public ASpecialDistributionConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
   }

}
