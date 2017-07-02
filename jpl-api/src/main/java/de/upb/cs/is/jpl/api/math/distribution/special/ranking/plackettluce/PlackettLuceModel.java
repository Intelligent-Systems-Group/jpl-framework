package de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce;


import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.distribution.special.ASpecialDistribution;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * This class is an implementation of the {@link PlackettLuceModel}, which is a statistical model
 * that can be used for ranking items. For a detailed overview over the {@link PlackettLuceModel} we
 * suggest the paper "MM ALGORITHMS FOR GENERALIZED BRADLEY–TERRY MODELS" by D. R. Hunter.
 * 
 * @author Alexander Hetzer
 *
 */
public class PlackettLuceModel extends ASpecialDistribution<Ranking, PlackettLuceModelConfiguration> {


   private static final String ERROR_INVALID_RANKING = "The given ranking is invalid.";
   private static final String ERROR_EMPTY_RANKING = "Cannot compute the probability of an empty ranking.";

   private RankingValidator rankingValidator;

   private PlackettLuceModelSamplingAlgorithm samplingAlgorithm;


   /**
    * Creates a new {@link PlackettLuceModel} with default configuration.
    */
   public PlackettLuceModel() {
      super();
   }


   /**
    * Creates a new {@link PlackettLuceModel} initialized with the given parameters as item
    * parameters on the given set of valid items.
    * 
    * @param parameters the parameters of the {@link PlackettLuceModel} to use as the item
    *           parameters
    * @param validItems the set of items this {@link PlackettLuceModel} should be defined on
    * @throws ParameterValidationFailedException if the given array of parameters is {@code null}
    */
   public PlackettLuceModel(double[] parameters, int[] validItems) throws ParameterValidationFailedException {
      this(new DenseDoubleVector(parameters), validItems);
   }


   /**
    * Creates a new {@link PlackettLuceModel} initialized with the given parameters as item
    * parameters on the given set of valid items.
    * 
    * @param parameterVector the parameters of the {@link PlackettLuceModel} to use as the item
    *           parameters
    * @param validItems the set of items this {@link PlackettLuceModel} should be defined on
    * @throws ParameterValidationFailedException if either the given parameter vector or the set of
    *            valid items is {@code null} or the internal sampling algorithm could not be
    *            initialized due to an illegal (e.g. negative) parameter vector
    */
   public PlackettLuceModel(IVector parameterVector, int[] validItems) throws ParameterValidationFailedException {
      super();
      setDistributionConfiguration(new PlackettLuceModelConfiguration(parameterVector, validItems));
      this.rankingValidator = new RankingValidator(validItems);
      this.samplingAlgorithm = new PlackettLuceModelSamplingAlgorithm(getDistributionConfiguration());
   }


   @Override
   public Ranking generateSample() throws DistributionException {
      return samplingAlgorithm.generateSample();
   }


   @Override
   public double getProbabilityFor(Ranking ranking) throws DistributionException {
      assertRankingIsValid(ranking);
      if (rankingValidator.isRankingPartial(ranking)) {
         return computeProbabilityForPartialRanking(ranking);
      }
      return computeProbabilityForCompleteRanking(ranking);
   }


   /**
    * Returns the mode, i.e. the most probable {@link Ranking}, of the {@link PlackettLuceModel}.
    * 
    * @return the mode, i.e. the most probable {@link Ranking}, of the {@link PlackettLuceModel}
    */
   public Ranking getMode() {
      int[] itemsSortedAccordingToParameterValue = PlackettLuceModelSamplingAlgorithm.getValidItemIdsSortedAccordingValues(
            getDistributionConfiguration().getValidItems(), getDistributionConfiguration().getParametersAsArray());
      return PlackettLuceModelSamplingAlgorithm.constructMonotonicallyIncreasingRankingForItems(itemsSortedAccordingToParameterValue);
   }


   /**
    * Computes the probability of the given {@link Ranking} assuming that it is partial and valid.
    * 
    * @param ranking the partial {@link Ranking} to compute the probability for
    * @return the probability of the given {@link Ranking}
    * @throws DistributionException if any problem occurred while computing the probability
    */
   private double computeProbabilityForPartialRanking(Ranking ranking) throws DistributionException {
      // Note that the marginal probability on a subset of items is itself a Plackett Luce model on
      // the subset of these items, parameterized with only those parameters linked to the according
      // items contained in the
      // subset
      int[] partialItemSet = ranking.getObjectList();
      IVector partialParameterVector = getParameterVectorForItemSubset(partialItemSet);
      PlackettLuceModel marginalPlackettLuceModelOnPartialRanking;
      try {
         marginalPlackettLuceModelOnPartialRanking = new PlackettLuceModel(partialParameterVector, partialItemSet);
      } catch (ParameterValidationFailedException e) {
         throw new DistributionException(e.getMessage(), e);
      }
      return marginalPlackettLuceModelOnPartialRanking.getProbabilityFor(ranking);
   }


   /**
    * Returns the partial parameter vector of the parameter vector of this {@link PlackettLuceModel}
    * for the given set of items, assuming that it is a subset of the item set this model was
    * defined on.
    * 
    * @param itemSubset the subset of the item set of this model for which the partial parameter
    *           vector should be returned
    * @return the partial parameter vector of the parameter vector of this {@link PlackettLuceModel}
    *         for the given set of items
    */
   private IVector getParameterVectorForItemSubset(int[] itemSubset) {
      DenseDoubleVector partialParameterVector = new DenseDoubleVector(itemSubset.length);
      for (int i = 0; i < itemSubset.length; i++) {
         partialParameterVector.setValue(i, getDistributionConfiguration().getParameterValueOfItem(itemSubset[i]));
      }
      return partialParameterVector;
   }


   /**
    * Computes the probability of the given {@link Ranking} assuming that it is valid.
    * 
    * @param ranking the {@link Ranking} to compute the probability for
    * @return the probability of the given {@link Ranking}
    * @throws DistributionException if the given {@link Ranking} is empty
    */
   private double computeProbabilityForCompleteRanking(Ranking ranking) throws DistributionException {
      int[] rankingAsArray = ranking.getObjectList();
      if (rankingAsArray.length == 0) {
         throw new DistributionException(ERROR_EMPTY_RANKING);
      }
      double partialDenominator = 0;
      double totalProbability = 1;
      for (int i = rankingAsArray.length - 1; i >= 0; i--) {
         double parameterValueForCurrentElementInRanking = getDistributionConfiguration().getParameterValueOfItem(rankingAsArray[i]);
         partialDenominator += parameterValueForCurrentElementInRanking;
         double partialProbability = parameterValueForCurrentElementInRanking / partialDenominator;
         totalProbability *= partialProbability;
      }
      return totalProbability;
   }


   /**
    * Asserts that the given {@link Ranking} is valid in terms of the {@link PlackettLuceModel}. See
    * {@link RankingValidator} for more details on when a ranking is valid. If this is not the case
    * a {@link DistributionException} is thrown.
    * 
    * @param ranking the {@link Ranking} to check
    * @throws DistributionException if the given {@link Ranking} is invalid
    */
   private void assertRankingIsValid(Ranking ranking) throws DistributionException {
      if (!rankingValidator.isRankingValid(ranking)) {
         throw new DistributionException(ERROR_INVALID_RANKING);
      }
   }


   @Override
   public PlackettLuceModelConfiguration createDefaultDistributionConfiguration() {
      PlackettLuceModelConfiguration defaultConfiguration = new PlackettLuceModelConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }

}
