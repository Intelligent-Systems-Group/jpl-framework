package de.upb.cs.is.jpl.api.algorithm;


import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.configuration.json.AJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;


/**
 * This is the base class for all algorithm configuration classes. The path to the default
 * configuration file location is set in the constructor of this class.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class AAlgorithmConfiguration extends AJsonConfiguration {

   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "algorithm";


   /**
    * Creates an {@link AAlgorithmConfiguration}.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file
    */
   public AAlgorithmConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
   }


   /**
    * Creates an {@link AAlgorithmConfiguration}.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file, including the
    *           path inside the default configuration folder for algorithms
    * @param pathToDefaultConfigurationLocation the location of the default configuration file
    */
   public AAlgorithmConfiguration(String defaultConfigurationFileName, String pathToDefaultConfigurationLocation) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION + pathToDefaultConfigurationLocation);
   }


   @Override
   protected JsonObject getDefaultConfigurationFileAsJsonObject() throws JsonParsingFailedException {
      return super.getDefaultConfigurationFileAsJsonObject().getAsJsonObject(DEFAULT_PARAMETER_VALUES);
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof AAlgorithmConfiguration) {
         return true;
      }
      return false;
   }


   @Override
   public String toString() {
      return getClass().getSimpleName();
   }

}
