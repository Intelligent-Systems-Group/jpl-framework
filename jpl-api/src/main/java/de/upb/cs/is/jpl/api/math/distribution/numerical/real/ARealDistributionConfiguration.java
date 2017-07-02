package de.upb.cs.is.jpl.api.math.distribution.numerical.real;


import de.upb.cs.is.jpl.api.math.distribution.numerical.ANumericalDistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the abstract configuration for a {@link ARealDistribution}.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class ARealDistributionConfiguration extends ANumericalDistributionConfiguration {

   protected static final String ERROR_NON_POSITIVE_SCALING_PARAMETER = "The scaling parameter has to be positive.";

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "real" + StringUtils.FORWARD_SLASH;


   /**
    * Creates an {@link ARealDistributionConfiguration}.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file, including the
    *           path inside the default configuration folder for distributions
    */
   public ARealDistributionConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
   }
}
