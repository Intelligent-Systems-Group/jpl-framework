package de.upb.cs.is.jpl.api.algorithm;


import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;


/**
 * This interface defines behavior which needs to be supported by any kind of algorithm in this
 * tool.
 * 
 * @author Sebastian Gottschalk
 * @author Tanja Tornede
 *
 */
public interface IAlgorithm {

   /**
    * Sets the parameters of this algorithm based on the given Json object and performs a validation
    * of the values.
    * 
    * @param jsonObject the json object containing the algorithm parameters
    * @throws ParameterValidationFailedException if the parameter validation failed
    */
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException;


   /**
    * Returns the default configuration of this algorithm, initialized with values from the
    * according default configuration file.
    * 
    * @return the default configuration of this algorithm
    */
   public AAlgorithmConfiguration getDefaultAlgorithmConfiguration();


   /**
    * Sets the algorithm configuration of this algorithm to the given configuration.
    * 
    * @param algorithmConfiguration the configuration to set
    */
   public void setAlgorithmConfiguration(AAlgorithmConfiguration algorithmConfiguration);


   /**
    * Returns the current algorithm configuration of this algorithm.
    * 
    * @return the current algorithm configuration of this algorithm
    */
   public AAlgorithmConfiguration getAlgorithmConfiguration();


}
