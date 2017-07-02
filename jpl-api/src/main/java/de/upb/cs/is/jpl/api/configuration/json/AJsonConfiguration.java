package de.upb.cs.is.jpl.api.configuration.json;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import de.upb.cs.is.jpl.api.exception.configuration.json.DefaultConfigurationException;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Abstract class representing a configuration based on a json file. It provides default
 * implementations of all methods defined in {@link IJsonConfiguration}. Every configuration seeking
 * to use the automatic json file to java object transformation, should subclass this class.
 * 
 * @author Alexander Hetzer
 *
 */
public abstract class AJsonConfiguration implements IJsonConfiguration {

   private static final String ERROR_COULD_NOT_RESET_CONFIGURATION = "Could not reset configuration '%s' to default values. The default configuration file could not be found.";
   private static final String ERROR_PARAMETER_VALIDATION_FAILED = "Parameter validation of the default configuration '%s' failed. Please check the configuration file.";
   private static final String ERROR_KEY_IS_NULL_OR_EMPTY = "The given key '%s' is null or empty.";
   private static final String ERROR_COULD_NOT_PARSE_GIVEN_JSON_OBJECT = "Could not parse the given json object '%s' into class '%s' due to type inconsistencies.";

   private static final Logger logger = LoggerFactory.getLogger(AJsonConfiguration.class);

   protected static final String DEFAULT_PARAMETER_VALUES = "default_parameter_values";
   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = StringUtils.FORWARD_SLASH + "default_configuration"
         + StringUtils.FORWARD_SLASH;

   protected String defaultConfigurationFileName;
   protected String pathToDefaultConfigurationLocation;


   /**
    * Creates an abstract json configuration with the given default configuration file name and its
    * location.
    * 
    * @param defaultConfigurationFileName the name of the default configuration file
    * @param pathToDefaultConfigurationLocation the location of the default configuration file
    */
   public AJsonConfiguration(String defaultConfigurationFileName, String pathToDefaultConfigurationLocation) {
      this.defaultConfigurationFileName = defaultConfigurationFileName;
      this.pathToDefaultConfigurationLocation = PATH_TO_DEFAULT_CONFIGURATION_LOCATION + pathToDefaultConfigurationLocation;
   }


   @Override
   public void initializeConfiguration(JsonObject userSuppliedConfigurationFile) throws ParameterValidationFailedException {
      initializeDefaultConfiguration();
      initializeFromJsonObject(userSuppliedConfigurationFile);
   }


   @Override
   public void initializeDefaultConfiguration() {
      resetToDefault();
   }


   @Override
   public void overrideConfiguration(JsonObject userSuppliedConfigurationFile) throws ParameterValidationFailedException {
      initializeFromJsonObject(userSuppliedConfigurationFile);
   }


   @Override
   public void resetToDefault() {
      try {
         JsonObject defaultConfiguration = getDefaultConfigurationFileAsJsonObject();
         initializeFromJsonObject(defaultConfiguration);

         // as the following exceptions should only occur in production, we will only throw
         // runtime exceptions here and still log the error
      } catch (JsonParsingFailedException jsonParsingFailedException) {
         String errorMessage = String.format(ERROR_COULD_NOT_RESET_CONFIGURATION, defaultConfigurationFileName);
         logger.error(errorMessage);
         throw new DefaultConfigurationException(errorMessage, jsonParsingFailedException);
      } catch (ParameterValidationFailedException parameterValidationFailedException) {
         String errorMessage = String.format(ERROR_PARAMETER_VALIDATION_FAILED, defaultConfigurationFileName);
         logger.error(errorMessage);
         throw new DefaultConfigurationException(errorMessage, parameterValidationFailedException);
      }
   }


   /**
    * Returns the Json object located under the given key in the default configuration file.
    *
    * @param key this parameter specifies the key for the JsonObject in the JSON file
    * 
    * @return the json object stored under the given key in the default configuration file as json
    *         object
    * 
    * @throws JsonParsingFailedException if the default configuration file cannot be found under the
    *            location, which is decomposed of {@link #pathToDefaultConfigurationLocation}
    *            {@code +} {@link #defaultConfigurationFileName} {@code + .json} or if anything else
    *            went wrong during the parsing process
    * @throws NullPointerException if the given key is {@code null} or empty
    */
   protected JsonObject getJsonObjectFromDefaultConfiguration(String key) throws JsonParsingFailedException {
      if (key == null || key.isEmpty()) {
         throw new NullPointerException(String.format(ERROR_KEY_IS_NULL_OR_EMPTY, key));
      }
      JsonObject defaultConfigurationJsonObject = getDefaultConfigurationFileAsJsonObject();
      return defaultConfigurationJsonObject.getAsJsonObject(key);
   }


   /**
    * Returns the default configuration file as json object.
    * 
    * @return the default configuration file as json object
    * 
    * @throws JsonParsingFailedException if any problems occurred during reading the default
    *            configuration file
    */
   protected JsonObject getDefaultConfigurationFileAsJsonObject() throws JsonParsingFailedException {
      String completePathToDefaultConfiguration = pathToDefaultConfigurationLocation;
      completePathToDefaultConfiguration += StringUtils.FORWARD_SLASH;
      completePathToDefaultConfiguration += defaultConfigurationFileName + JsonUtils.JSON_FILE_NAME_EXTENSION;
      return JsonUtils.createJsonObjectFromFilePath(completePathToDefaultConfiguration);
   }


   /**
    * Validates the parameters of this configuration.
    * 
    * @throws ParameterValidationFailedException if the validation process fails
    */
   protected abstract void validateParameters() throws ParameterValidationFailedException;


   /**
    * Copies all existing values from the given configuration.
    * 
    * @param configuration the configuration to copy values from
    */
   protected abstract void copyValues(IJsonConfiguration configuration);


   /**
    * Initialises this configuration from the given json object and validates the parameters.
    * 
    * @param jsonObject the json object to initialize the configuration from
    * 
    * @throws ParameterValidationFailedException if the parameter validation failed
    */
   protected void initializeFromJsonObject(JsonObject jsonObject) throws ParameterValidationFailedException {
      Gson gson = new Gson();
      try {
         IJsonConfiguration parsedConfiguration = gson.fromJson(jsonObject.toString(), getClass());
         copyValues(parsedConfiguration);
         validateParameters();
      } catch (JsonSyntaxException jsonSyntaxException) {
         String errorMessage = String.format(ERROR_COULD_NOT_PARSE_GIVEN_JSON_OBJECT, jsonObject.toString(), getClass().getSimpleName());
         logger.error(errorMessage, jsonSyntaxException);
         throw new ParameterValidationFailedException(errorMessage);
      }
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((defaultConfigurationFileName == null) ? 0 : defaultConfigurationFileName.hashCode());
      result = prime * result + ((pathToDefaultConfigurationLocation == null) ? 0 : pathToDefaultConfigurationLocation.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      AJsonConfiguration other = (AJsonConfiguration) obj;
      if (defaultConfigurationFileName == null) {
         if (other.defaultConfigurationFileName != null)
            return false;
      } else if (!defaultConfigurationFileName.equals(other.defaultConfigurationFileName))
         return false;
      if (pathToDefaultConfigurationLocation == null) {
         if (other.pathToDefaultConfigurationLocation != null)
            return false;
      } else if (!pathToDefaultConfigurationLocation.equals(other.pathToDefaultConfigurationLocation))
         return false;
      return true;
   }


}
