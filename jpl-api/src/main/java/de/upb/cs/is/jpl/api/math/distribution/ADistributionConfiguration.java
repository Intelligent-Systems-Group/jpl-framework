package de.upb.cs.is.jpl.api.math.distribution;


import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.configuration.json.AJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the abstract configuration for a distribution.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class ADistributionConfiguration extends AJsonConfiguration {

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "math" + StringUtils.FORWARD_SLASH + "distribution"
         + StringUtils.FORWARD_SLASH;


   /**
    * Creates an {@link ADistributionConfiguration}.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file, including the
    *           path inside the default configuration folder for distributions
    * @param pathToDefaultConfigurationLocation the location of the default configuration file
    */
   public ADistributionConfiguration(String defaultConfigurationFileName, String pathToDefaultConfigurationLocation) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION + pathToDefaultConfigurationLocation);
   }


   @Override
   protected JsonObject getDefaultConfigurationFileAsJsonObject() throws JsonParsingFailedException {
      return super.getDefaultConfigurationFileAsJsonObject().getAsJsonObject(DEFAULT_PARAMETER_VALUES);
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof ADistributionConfiguration) {
         return true;
      }
      return false;
   }


   @Override
   public String toString() {
      return getClass().getSimpleName();
   }

}
