package de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce;


import java.util.Arrays;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.gumbel.GumbelDistribution;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This class is an implementation of a {@link PlackettLuceModel} sampling algorithm using a
 * {@link GumbelDistribution} in order to generate samples. This algorithm works as follows: As a
 * first step the logarithmic function is applied componentwise to the parameter vector of the
 * {@link PlackettLuceModel}. For each of these constructed components, a {@link GumbelDistribution}
 * realization is sampled where the {@link GumbelDistribution} is passed the newly constructed
 * component as a parameter. As a last step, the set of items underlying the
 * {@link PlackettLuceModel} is sorted according to the generated {@link GumbelDistribution} samples
 * in an increasing manner. This sorting represents a ranking on the items, which is returned as a
 * sample.
 * 
 * @author Alexander Hetzer
 *
 */
public class PlackettLuceModelSamplingAlgorithm {

   /**
    * The scaling parameter used for sampling the {@link GumbelDistribution}.
    */
   private static final double GUMBEL_SCALING_PARAMETER = 1.0;

   private PlackettLuceModelConfiguration modelConfiguration;

   private GumbelDistribution[] gumbelDistributions;

   /**
    * The logarithmic parameter vector, i.e. the logarithmic function has been applied to each of
    * the components of the original parameter vector.
    */
   private DenseDoubleVector logarithmicParameterVector;


   /**
    * Creates a new {@link PlackettLuceModelSamplingAlgorithm} instance which can be used to
    * generate samples from a {@link PlackettLuceModel} initialized with the given parameter vector.
    * 
    * @param modelConfiguration the {@link PlackettLuceModelConfiguration} associated with the
    *           {@link PlackettLuceModel} which should be sampled
    * @throws ParameterValidationFailedException if the {@link GumbelDistribution}s initialized for
    *            the sampling could not be initialized correctly with the parameters from the
    *            {@link PlackettLuceModelConfiguration}
    */
   public PlackettLuceModelSamplingAlgorithm(PlackettLuceModelConfiguration modelConfiguration) throws ParameterValidationFailedException {
      this.modelConfiguration = modelConfiguration;
      computeLogarithmicParameterVector();
      initializeGumbelDistributions();
   }


   /**
    * Generates a sample from the {@link PlackettLuceModel} initialized with the locally stored
    * parameter vector. This algorithm works as follows: As a first step the logarithmic function is
    * applied componentwise to the parameter vector of the {@link PlackettLuceModel}. For each of
    * these constructed components, a {@link GumbelDistribution} realization is sampled where the
    * {@link GumbelDistribution} is passed the newly constructed component as a parameter. As a last
    * step, the set of items underlying the {@link PlackettLuceModel} is sorted according to the
    * generated {@link GumbelDistribution} samples in an increasing manner. This sorting represents
    * a ranking on the items, which is returned as a sample.
    * 
    * @return a sample from the {@link PlackettLuceModel} initialized with the locally stored
    *         parameter vector
    */
   public Ranking generateSample() {
      double[] gumbleDistributionSamples = new double[logarithmicParameterVector.length()];

      for (int i = 0; i < modelConfiguration.getNumberOfParameters(); i++) {
         gumbleDistributionSamples[i] = gumbelDistributions[i].generateSample();
      }

      // Note that we only have the indices of the items in the sorting we want, but not yet the
      // actual items, due to the way the configuration stores the items.
      int[] sortedItemIds = getValidItemIdsSortedAccordingValues(modelConfiguration.getValidItems(),
            gumbleDistributionSamples);
      return constructMonotonicallyIncreasingRankingForItems(sortedItemIds);
   }


   /**
    * Given an array of item ids, this method constructs a {@link Ranking} according to the order of
    * the item ids in the array, such that the 0th element in the array is preferred to the first,
    * the first one to the second, etc.
    * 
    * @param itemIds the ids of the items in the order in which the items should be ranked
    * @return a {@link Ranking} according to the order of the item ids in the array
    */
   public static Ranking constructMonotonicallyIncreasingRankingForItems(int[] itemIds) {
      int[] comparativeOperators = new int[itemIds.length - 1];
      Arrays.fill(comparativeOperators, Ranking.COMPARABLE_ENCODING);
      return new Ranking(itemIds, comparativeOperators);
   }


   /**
    * Returns the given valid item ids sorted according to the given parameters in a decreasing
    * order.
    * 
    * Assume that {@code validItems = [3,5,8]} and {@code parameterValues=[0.35,0.15,0.5]}. Then
    * this method will return {@code [8,3,5]}.
    * 
    * @param validItems the set of valid items to sort
    * @param values the values according to which the items should be sorted
    * @return the given valid item ids sorted according to the given parameters in a decreasing
    *         order
    */
   public static int[] getValidItemIdsSortedAccordingValues(int[] validItems, double[] values) {
      int[] itemIndicesSortedAccordingToParameterValue = CollectionsUtils.getIndicesSortedAccordingToValue(values);
      int[] sortedItemIds = new int[itemIndicesSortedAccordingToParameterValue.length];
      for (int i = 0; i < itemIndicesSortedAccordingToParameterValue.length; i++) {
         sortedItemIds[i] = validItems[itemIndicesSortedAccordingToParameterValue[i]];
      }
      return sortedItemIds;
   }


   /**
    * Computes the logarithmic parameter vector. The logarithmic parameter vector is the original
    * parameter vector with the logarithmic function applied to each of the components.
    */
   private void computeLogarithmicParameterVector() {
      if (this.logarithmicParameterVector == null) {
         IVector parameterVector = modelConfiguration.getParameterVector();
         logarithmicParameterVector = new DenseDoubleVector(parameterVector.length());
         for (int i = 0; i < parameterVector.length(); i++) {
            logarithmicParameterVector.setValue(i, Math.log(parameterVector.getValue(i)));
         }
      }
   }


   private void initializeGumbelDistributions() throws ParameterValidationFailedException {
      gumbelDistributions = new GumbelDistribution[modelConfiguration.getNumberOfParameters()];
      for (int i = 0; i < modelConfiguration.getNumberOfParameters(); i++) {
         gumbelDistributions[i] = new GumbelDistribution(logarithmicParameterVector.getValue(i), GUMBEL_SCALING_PARAMETER);
      }
   }

}
