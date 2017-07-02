package de.upb.cs.is.jpl.api.configuration.json;


import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;


/**
 * Interface for any kind of configuration, which is based on a json file. It defines the basic
 * operations, which can be done on any configuration.
 * 
 * @author Alexander Hetzer
 *
 */
public interface IJsonConfiguration {

   /**
    * Initializes this configuration with its default values and overrides the ones, which are
    * defined in the given json object with the given values. Furthermore a validation of the values
    * is performed.
    * 
    * @param configurationJsonObject a json object specifying which keys to overwrite and the new
    *           values
    * @throws ParameterValidationFailedException if the validation of the values failed
    */
   public void initializeConfiguration(JsonObject configurationJsonObject) throws ParameterValidationFailedException;


   /**
    * Initializes this configuration with its default values.
    */
   public void initializeDefaultConfiguration();


   /**
    * Overrides the values in this configuration with the values defined in the given json object.
    * If a value for a specific field is not present in the given json object, the existing value of
    * that field is kept. Furthermore a validation of the values is performed.
    * 
    * @param configurationJsonObject a json object specifying which keys to overwrite and the new
    *           values
    * @throws ParameterValidationFailedException if the validation of the values failed
    */
   public void overrideConfiguration(JsonObject configurationJsonObject) throws ParameterValidationFailedException;


   /**
    * Resets this configuration to its default values.
    */
   public void resetToDefault();
}
