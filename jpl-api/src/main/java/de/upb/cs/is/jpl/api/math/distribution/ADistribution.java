package de.upb.cs.is.jpl.api.math.distribution;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.WrongConfigurationTypeException;
import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This abstract distribution class implements a part of the functionality required by
 * {@link IDistribution}. All distributions implemented in the jPL framework should subclass this
 * class.
 * 
 * @author Alexander Hetzer
 *
 * @param <SPACE> Defines the type of elements forming the space which this distribution is defined
 *           over.
 * @param <CONFIG> the generic type extending {@link ADistributionConfiguration}
 */
public abstract class ADistribution<SPACE, CONFIG extends ADistributionConfiguration> implements IDistribution<SPACE> {

   private static final String ERROR_WRONG_CONFIGURATION_TYPE = "Initialized distribution configuration with wrong configuration type.";

   protected CONFIG configuration;


   /**
    * Creates a new {@link ADistribution} initialized with a default configuration.
    */
   public ADistribution() {
      getDistributionConfiguration();
   }


   @Override
   public List<SPACE> generateSamples(int numberOfSamples) throws DistributionException {
      List<SPACE> generatedSamples = new ArrayList<>(numberOfSamples);
      for (int i = 0; i < numberOfSamples; i++) {
         generatedSamples.add(generateSample());
      }
      return generatedSamples;
   }


   /**
    * Creates the default distribution configuration of this distribution and returns it.
    * 
    * @return default distribution configuration of this distribution
    */
   public abstract CONFIG createDefaultDistributionConfiguration();


   @Override
   public ADistributionConfiguration getDefaultDistributionConfiguration() {
      ADistributionConfiguration distributionConfiguration = createDefaultDistributionConfiguration();
      distributionConfiguration.initializeDefaultConfiguration();
      return distributionConfiguration;
   }


   @Override
   @SuppressWarnings("unchecked")
   public void setDistributionConfiguration(ADistributionConfiguration algorithmConfiguration) {
      if (algorithmConfiguration.getClass().isInstance(createDefaultDistributionConfiguration().getClass())) {
         throw new WrongConfigurationTypeException(ERROR_WRONG_CONFIGURATION_TYPE);
      }
      this.configuration = (CONFIG) algorithmConfiguration;
   }


   @Override
   @SuppressWarnings("unchecked")
   public CONFIG getDistributionConfiguration() {
      if (configuration == null) {
         configuration = (CONFIG) getDefaultDistributionConfiguration();
      }
      return configuration;
   }


   @Override
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException {
      getDistributionConfiguration().overrideConfiguration(jsonObject);
   }


   @Override
   public String toString() {
      return getClass().getSimpleName() + StringUtils.ROUND_BRACKET_OPEN + getDistributionConfiguration().toString()
            + StringUtils.ROUND_BRACKET_CLOSE;
   }


}
