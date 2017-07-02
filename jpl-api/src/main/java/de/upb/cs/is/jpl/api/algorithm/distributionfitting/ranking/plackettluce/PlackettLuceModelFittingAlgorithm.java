package de.upb.cs.is.jpl.api.algorithm.distributionfitting.ranking.plackettluce;


import java.util.Arrays;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.distributionfitting.ranking.ARankingDistributionFittingAlgorithm;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.algorithm.FittingDistributionFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.IDistributionSampleSet;
import de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce.PlackettLuceModel;
import de.upb.cs.is.jpl.api.math.distribution.special.ranking.plackettluce.PlackettLuceModelSampleSet;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This class is a fitting algorithm for the {@link PlackettLuceModel}, trying to find the
 * parameters of a {@link PlackettLuceModel} based on a {@link PlackettLuceModelSampleSet}
 * consisting of {@link Ranking}, sampled from the according {@link PlackettLuceModel}.
 * 
 * This algorithm is based on the algorithm provided by D.R. Hunter proposed in his
 * "MM algorithms for generalized Bradley-Terry models" paper.
 * 
 * The main idea of this algorithm is to find the optimal parameter vector by iteratively optimizing
 * the current parameter. This is done by maximizing a minorization function of the log-likelihood.
 * For more details, we suggest to take a look at the paper given above.
 *
 * 
 * @author Tanja Tornede
 *
 */
public class PlackettLuceModelFittingAlgorithm
      extends ARankingDistributionFittingAlgorithm<PlackettLuceModelFittingAlgorithmConfiguration> {

   private static final String ERROR_UNSUPPORTED_SAMPLE_SET_TYPE = "Unsupported sample set type: %s";

   private PlackettLuceModelSampleSet plackettLuceSampleSet;

   private double[] parameters;
   private double[] oldParameters;

   private double differenceBetweenOldAndNewParameters;
   private int numberOfIterations;


   /**
    * Creates a {@link PlackettLuceModelFittingAlgorithm} with the default configuration.
    */
   public PlackettLuceModelFittingAlgorithm() {
      super();
   }


   /**
    * Creates a new {@link PlackettLuceModelFittingAlgorithmConfiguration} with the given minimum
    * required change on update parameter and the given iterations sample set multiplier.
    * 
    * Both of these parameters are used as stopping criteria of this
    * {@link PlackettLuceModelFittingAlgorithm}. The sample set size multiplied with the iterations
    * sample set size multiplier parameters is used as an upper bound on the amount of iterations of
    * this algorithm.
    * 
    * The minimum required change on update parameter is used in the following way: Each iteration
    * the sum of the changes of the components of the old and the new parameter vector is computed.
    * If this sum falls below the value defined by this criterion, the algorithm will stop.
    * 
    * @param minimumRequiredChangeOnUpdate the minimum required change on update parameter. Has to
    *           be larger or equal than {@code 0}.
    * @param iterationsSampleSetMultiplier the iterations sample set multiplier parameter. Has to be
    *           larger than {@code 0}.
    * @throws ParameterValidationFailedException if validating the parameters went wrong
    */
   public PlackettLuceModelFittingAlgorithm(double minimumRequiredChangeOnUpdate, int iterationsSampleSetMultiplier)
         throws ParameterValidationFailedException {
      this();
      setAlgorithmConfiguration(
            new PlackettLuceModelFittingAlgorithmConfiguration(minimumRequiredChangeOnUpdate, iterationsSampleSetMultiplier));
   }


   @Override
   public PlackettLuceModel fit(IDistributionSampleSet<Ranking> sampleSet) throws FittingDistributionFailedException {
      assertCorrectSampleSetType(sampleSet);
      initializeAlgorithm((PlackettLuceModelSampleSet) sampleSet);

      while (!shouldStop()) {
         updateParameters();
         normalizeParameters();
         updateDifferenceBetweenOldAndNewParameters();
         updateNumberOfIterations();
         oldParameters = Arrays.copyOf(parameters, parameters.length);
      }

      try {
         return new PlackettLuceModel(parameters, plackettLuceSampleSet.getValidItems());
      } catch (ParameterValidationFailedException e) {
         throw new FittingDistributionFailedException(e);
      }
   }


   /**
    * Asserts that the given {@link IDistributionSampleSet} is a {@link PlackettLuceModelSampleSet}.
    * If this is not the case a {@link FittingDistributionFailedException} is thrown.
    * 
    * @param sampleSet the {@link IDistributionSampleSet} to check
    * @throws FittingDistributionFailedException if the given {@link IDistributionSampleSet} is not
    *            a {@link PlackettLuceModelSampleSet}
    */
   private void assertCorrectSampleSetType(IDistributionSampleSet<Ranking> sampleSet) throws FittingDistributionFailedException {
      if (!(sampleSet instanceof PlackettLuceModelSampleSet)) {
         throw new FittingDistributionFailedException(String.format(ERROR_UNSUPPORTED_SAMPLE_SET_TYPE, sampleSet));
      }
   }


   /**
    * Initializes this algorithm based on the given {@link PlackettLuceModelSampleSet}.
    * 
    * @param sampleSet the {@link PlackettLuceModelSampleSet} on the basis of which this algorithm
    *           should be initialized
    */
   private void initializeAlgorithm(PlackettLuceModelSampleSet sampleSet) {
      plackettLuceSampleSet = sampleSet;
      parameters = new double[plackettLuceSampleSet.getNumberOfItems()];
      oldParameters = new double[plackettLuceSampleSet.getNumberOfItems()];
      differenceBetweenOldAndNewParameters = 1;
      numberOfIterations = 0;
   }


   /**
    * Checks whether any stopping criterion of this algorithm is met or not. Returns {@code true} if
    * at least one of the stopping criteria of the algorithm is met, otherwise {@code false}.
    * 
    * @return {@code true} if at least one of the stopping criteria of the algorithm is met,
    *         otherwise {@code false}
    */
   private boolean shouldStop() {
      return differenceBetweenOldAndNewParameters < getAlgorithmConfiguration().getMinimumRequiredChangeOnUpdate()
            || numberOfIterations > plackettLuceSampleSet.getSize() * getAlgorithmConfiguration().getIterationsSampleSetSizeMultiplier();
   }


   /**
    * Performs a single update of the parameter vector. This update is performed componentwise
    * starting at the {@code 0}th component.
    */
   private void updateParameters() {
      for (int i = 0; i < parameters.length; i++) {
         parameters[i] = computeParameterOfItem(plackettLuceSampleSet.getItemIdForItemAtIndex(i), oldParameters);
      }
   }


   /**
    * Computes the new parameter value for the item with the given item id based on the given old
    * parameter vector.
    * 
    * @param itemId the id of the item for with the parameter should be recomputed
    * @param oldParameterVector the old parameter vector on which the parameter update should be
    *           based
    * @return the new value of the parameter associated with the given item id
    */
   private double computeParameterOfItem(int itemId, double[] oldParameterVector) {
      double denominator = 0;
      for (int sampleId = 0; sampleId < plackettLuceSampleSet.getSize(); sampleId++) {
         Ranking sampleRanking = plackettLuceSampleSet.getSample(sampleId);
         for (int itemIndex = 0; itemIndex < sampleRanking.getObjectList().length - 1; itemIndex++) {
            if (plackettLuceSampleSet.isItemRankedLowerOrEqualThan(itemId, itemIndex, sampleRanking)) {
               double subDenominator = 0;
               for (int s = itemIndex; s < sampleRanking.getObjectList().length; s++) {
                  subDenominator += oldParameterVector[s];
               }
               if (isDoubleNonZero(subDenominator)) {
                  denominator += 1.0 / subDenominator;
               } else {
                  denominator += 0;
               }
            }
         }
      }
      if (isDoubleNonZero(denominator)) {
         return plackettLuceSampleSet.getNumberOfRankingsWhereItemIsRankedHigherThanLast(itemId) / denominator;
      }
      // if the denominator is 0, simply ignore it
      return plackettLuceSampleSet.getNumberOfRankingsWhereItemIsRankedHigherThanLast(itemId);
   }


   /**
    * Checks whether the given double value can numerically seen as {@code 0}. Returns {@code true}
    * if the given double value can numerically seen as {@code 0}, otherwise {@code false}.
    * 
    * @param doubleValue the given double value to check
    * @return {@code true} if the given double value can numerically seen as {@code 0}, otherwise
    *         {@code false}
    */
   private boolean isDoubleNonZero(double doubleValue) {
      return Math.abs(0 - doubleValue) > 0.0001;
   }


   /**
    * Normalizes the current parameter vector. Note that this method directly manipulates the
    * parameter vector instead of returning the result.
    */
   private void normalizeParameters() {
      CollectionsUtils.normalize(parameters);
   }


   /**
    * Updates the difference between the old and the new parameter vector. This difference is the
    * sum of the componentwise differences of the the vectors.
    */
   private void updateDifferenceBetweenOldAndNewParameters() {
      differenceBetweenOldAndNewParameters = CollectionsUtils.getAbsoluteDifferenceBetween(parameters, oldParameters);
   }


   /**
    * Updates the number of iterations, i.e. increases it by {@code 1}.
    */
   private void updateNumberOfIterations() {
      numberOfIterations++;
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new PlackettLuceModelFittingAlgorithmConfiguration();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(differenceBetweenOldAndNewParameters);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + numberOfIterations;
      result = prime * result + Arrays.hashCode(oldParameters);
      result = prime * result + Arrays.hashCode(parameters);
      result = prime * result + ((plackettLuceSampleSet == null) ? 0 : plackettLuceSampleSet.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (!super.equals(obj)) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      PlackettLuceModelFittingAlgorithm other = (PlackettLuceModelFittingAlgorithm) obj;
      if (Double.doubleToLongBits(differenceBetweenOldAndNewParameters) != Double
            .doubleToLongBits(other.differenceBetweenOldAndNewParameters)) {
         return false;
      }
      if (numberOfIterations != other.numberOfIterations) {
         return false;
      }
      if (!Arrays.equals(oldParameters, other.oldParameters)) {
         return false;
      }
      if (!Arrays.equals(parameters, other.parameters)) {
         return false;
      }
      if (plackettLuceSampleSet == null) {
         if (other.plackettLuceSampleSet != null) {
            return false;
         }
      } else if (!plackettLuceSampleSet.equals(other.plackettLuceSampleSet)) {
         return false;
      }
      return true;
   }

}
