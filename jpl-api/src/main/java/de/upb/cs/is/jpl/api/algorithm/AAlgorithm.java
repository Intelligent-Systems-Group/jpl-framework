package de.upb.cs.is.jpl.api.algorithm;


import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.WrongConfigurationTypeException;


/**
 * This abstract class defines functions which needs to be supported by any kind of algorithm in
 * this tool.
 * 
 * @param <CONFIG> the generic type extending AAlgorithmConfiguration, configuration associated with
 *           the ABaseLearnerAlgorithm class.
 * 
 * @author Sebastian Gottschalk
 * @author Tanja Tornede
 *
 */
public abstract class AAlgorithm<CONFIG extends AAlgorithmConfiguration> implements IAlgorithm {

   private static final String ERROR_WRONG_CONFIGURATION_TYPE = "Initialized algorithm configuration with wrong configuration type.";
   protected static final String ERROR_UNSUPPORTED_DATASET_TYPE = "Unsupported dataset type '%s'. Required type: %s";

   protected CONFIG configuration;

   private String algorithmIdentifier;


   /**
    * Creates a new a algorithm with the simple name of the class as identifier.
    */
   public AAlgorithm() {
      this.algorithmIdentifier = this.getClass().getSimpleName();
      getAlgorithmConfiguration();
   }


   /**
    * Creates a new a algorithm with the algorithm identifier.
    *
    * @param algorithmIdentifier the algorithm identifier
    */
   public AAlgorithm(String algorithmIdentifier) {
      this.algorithmIdentifier = algorithmIdentifier;
      getAlgorithmConfiguration();
   }


   /**
    * Creates the default algorithm configuration of this algorithm and returns it.
    * 
    * @return Default algorithm configuration of this algorithm
    */
   protected abstract AAlgorithmConfiguration createDefaultAlgorithmConfiguration();


   @Override
   public AAlgorithmConfiguration getDefaultAlgorithmConfiguration() {
      AAlgorithmConfiguration algorithmConfiguration = createDefaultAlgorithmConfiguration();
      algorithmConfiguration.initializeDefaultConfiguration();
      return algorithmConfiguration;
   }


   @Override
   @SuppressWarnings("unchecked")
   public void setAlgorithmConfiguration(AAlgorithmConfiguration algorithmConfiguration) {
      if (algorithmConfiguration.getClass().isInstance(createDefaultAlgorithmConfiguration().getClass())) {
         throw new WrongConfigurationTypeException(ERROR_WRONG_CONFIGURATION_TYPE);
      }
      this.configuration = (CONFIG) algorithmConfiguration;
   }


   @Override
   @SuppressWarnings("unchecked")
   public CONFIG getAlgorithmConfiguration() {
      if (configuration == null) {
         configuration = (CONFIG) getDefaultAlgorithmConfiguration();
      }
      return configuration;
   }


   @Override
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException {
      getAlgorithmConfiguration().overrideConfiguration(jsonObject);
   }


   @Override
   public boolean equals(Object secondObject) {
      if (secondObject instanceof AAlgorithm) {

         @SuppressWarnings("unchecked")
         AAlgorithm<CONFIG> algorithm = AAlgorithm.class.cast(secondObject);

         if (configuration.equals(algorithm.configuration)) {
            return true;
         }
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = 31;
      hashCode = 31 * hashCode + configuration.hashCode();
      return hashCode;
   }


   @Override
   public String toString() {
      return algorithmIdentifier;
   }
}
