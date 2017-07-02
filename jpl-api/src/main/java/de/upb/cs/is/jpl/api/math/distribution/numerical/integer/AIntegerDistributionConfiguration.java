package de.upb.cs.is.jpl.api.math.distribution.numerical.integer;


import de.upb.cs.is.jpl.api.math.distribution.numerical.ANumericalDistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the abstract configuration for a {@link AIntegerDistribution}.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class AIntegerDistributionConfiguration extends ANumericalDistributionConfiguration {

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "integer" + StringUtils.FORWARD_SLASH;


   /**
    * Creates an {@link AIntegerDistributionConfiguration}.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file, including the
    *           path inside the default configuration folder for distributions
    */
   public AIntegerDistributionConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
   }
}
