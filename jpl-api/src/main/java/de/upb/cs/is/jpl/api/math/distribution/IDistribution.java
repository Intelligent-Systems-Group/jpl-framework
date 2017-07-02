package de.upb.cs.is.jpl.api.math.distribution;


import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.IRealDistribution;


/**
 * This interface defines the common functionality which all kinds of distributions, i.e.
 * distribution on on the real numbers or on a different space than the real numbers, need to
 * support. As most of the common mathematical knowledge for distributions cannot be easily
 * transfered to some of these special types of distributions, the required functionality is kept
 * very general and at a minimum. If you required more functionality with respect to the space of
 * the real numbers, make use of the {@link IRealDistribution}.
 * 
 * 
 * @author Alexander Hetzer
 * @author Tanja Tornede
 *
 * @param <SPACE> Defines the type of elements forming the space which this distribution is defined
 *           over.
 */
public interface IDistribution<SPACE> {

   /**
    * Generates a sample from this distribution.
    * 
    * @return a sample from this distribution
    * @throws DistributionException if some problem occurred while generating a sample
    */
   public SPACE generateSample() throws DistributionException;


   /**
    * Generates the given number of samples from this distributions and returns them as a list.
    * 
    * @param numberOfSamples the number of samples to be generated
    * @return the given number of samples generated from this distribution as a list
    * @throws DistributionException if some problem occurred while generating a sample
    */
   public List<SPACE> generateSamples(int numberOfSamples) throws DistributionException;


   /**
    * Returns the probability that the given event occurs.
    * 
    * @param event the event which the probability should be computed for
    * @return the probability that the given event occurs.
    * @throws DistributionException if the given parameter does not fit the space
    */
   public double getProbabilityFor(SPACE event) throws DistributionException;


   /**
    * Sets the parameters of this distribution based on the given Json object and performs a
    * validation of the values.
    * 
    * @param jsonObject the json object containing the distribution parameters
    * @throws ParameterValidationFailedException if the parameter validation failed
    */
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException;


   /**
    * Returns the default configuration of this distribution, initialized with values from the
    * according default configuration file.
    * 
    * @return the default configuration of this distribution
    */
   public ADistributionConfiguration getDefaultDistributionConfiguration();


   /**
    * Sets the distribution configuration of this distribution to the given configuration.
    * 
    * @param distributionConfiguration the configuration to set
    */
   public void setDistributionConfiguration(ADistributionConfiguration distributionConfiguration);


   /**
    * Returns the current distribution configuration of this distribution.
    * 
    * @return the current distribution configuration of this algorithm
    */
   public ADistributionConfiguration getDistributionConfiguration();

}
