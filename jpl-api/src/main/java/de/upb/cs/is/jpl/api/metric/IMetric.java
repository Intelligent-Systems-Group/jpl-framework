package de.upb.cs.is.jpl.api.metric;


import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * Any {@link AMetric} corresponding the list of {@link ELearningProblem}(s) needs to implement this
 * interface in order to be able to calculate the corresponding {@link EMetric} for the comparison
 * of multiple algorithms or the evaluation of a single algorithm.
 *
 * @author Pritha Gupta
 * 
 * @param <INPUT> the return type of the prediction(s) and true values on which the loss needs to be
 *           checked
 * @param <OUTPUT> the metric return type
 */
public interface IMetric<INPUT, OUTPUT> {

   /**
    * Computes the list of losses for the given expected and predicted ratings. This means that the
    * loss given in the return list on position {@code i} should be the loss computed between the
    * expected rating at position {@code i} and the predicted rating at position {@code i} in the
    * given lists.
    * 
    * @param expectedRatings the ratings to be expected, i.e. the ground truth to compute the losses
    *           against
    * @param predictedRatings the predicted ratings to compute the loss for
    * @return the list of losses between the given expected ratings and the predicted ratings
    * @throws LossException if the evaluation metric cannot be computed for some reason
    */
   public List<OUTPUT> getLossForRatings(final List<INPUT> expectedRatings, final List<INPUT> predictedRatings) throws LossException;


   /**
    * Computes the loss between the expected rating and the predicted rating.
    * 
    * @param expectedRating the rating to be expected, i.e. the ground truth to compute the loss
    *           against
    * @param predictedRating the predicted ratings to compute the loss for
    * @return the loss between the given expected rating and the predicted rating
    * @throws LossException if the evaluation metric cannot be computed for some reason
    */
   public OUTPUT getLossForSingleRating(final INPUT expectedRating, final INPUT predictedRating) throws LossException;


   /**
    * Computes the mean loss for the given expected and predicted ratings It computes the loss for
    * each pair of rating and provide the mean loss.
    * 
    * @param expectedRatings the ratings to be expected, i.e. the ground truth to compute the losses
    *           against
    * @param predictedRatings the predicted ratings to compute the loss for
    * @return the list of losses between the given expected ratings and the predicted ratings
    * @throws LossException if the evaluation metric cannot be computed for some reason
    */
   public OUTPUT getAggregatedLossForRatings(final List<INPUT> expectedRatings, final List<INPUT> predictedRatings) throws LossException;


   /**
    * Calculates the weighted aggregated loss for the ratings. This method takes the weights for
    * each pair of ratings. It iterates over the function which evaluates the single loss for each
    * rating and take the weighted mean.
    * 
    * @param weights the number of times the rating appear in the dataset
    * @param expectedRatings the ratings to be expected, i.e. the ground truth to compute the losses
    *           against
    * @param predictedRatings the predicted ratings to compute the loss for
    * @return the double weighted sum of the losses between the given expected ratings and the
    *         predicted ratings
    * @throws LossException if the evaluation metric cannot be computed for some reason
    */
   public OUTPUT getWeightedAggregatedLossForRatings(List<Double> weights, List<INPUT> expectedRatings, List<INPUT> predictedRatings)
         throws LossException;


   /**
    * Returns the default configuration of this metric, initialized with values from the according
    * default configuration file.
    * 
    * @return the default configuration of this evaluation metric
    */
   AMetricConfiguration getMetricDefaultConfiguration();


   /**
    * Returns the current configuration of this evaluation metric.
    * 
    * @return the current configuration of this evaluation metric
    */
   public AMetricConfiguration getMetricConfiguration();


   /**
    * Sets the parameters of this evaluation metric based on the given Json object and performs a
    * validation of the values.
    * 
    * @param jsonObject the JSON object defining the evaluation metric
    * @throws ParameterValidationFailedException if the parameter validation failed
    */
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException;


   /**
    * Sets the evaluation metric configuration of this evaluation metric to the given configuration.
    * 
    * @param evaluationMetricConfiguration the configuration to set
    */
   public void setMetricConfiguration(AMetricConfiguration evaluationMetricConfiguration);


   /**
    * Override to string to get the simple name of the class.
    */
   @Override
   public String toString();


}
