package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * The {@link AAlgorithmConfiguration} for the {@link MatrixFactorizationLearningAlgorithm}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class MatrixFactorizationConfiguration extends AAlgorithmConfiguration {


   private static final String N_MAX = "n_max";
   private static final String RANDOM_SEED = "r";
   private static final String MIN_RATING = "epsilon";
   private static final String REGULARIZATION_FACTOR = "reg_factor";
   private static final String STEP_SIZE = "step_size";
   private static final String K = "k";

   private static final String LAST_PARAMETER_VALUE_PAIR = "%s:%s";
   private static final String PARAMETER_VALUE_PAIR = "%s:%s, ";
   private static final String K_NOT_A_POSITIVE_VALUE = "The parameter k (number of hidden features) is not a positive value. It must be bigger than zero.";
   private static final String NOT_A_VALID_VALUE = "The parameter %s is not a valid value. It must be bigger than %s.";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "collaborativefiltering"
         + StringUtils.FORWARD_SLASH + "matrix_factorization_learning_algorithm";


   @SerializedName(K)
   protected int numberOfHiddenFeatures = Integer.MAX_VALUE;

   @SerializedName(STEP_SIZE)
   protected double stepSize = Double.MAX_VALUE;

   @SerializedName(REGULARIZATION_FACTOR)
   protected double regularizationFactor = Double.MAX_VALUE;

   @SerializedName(MIN_RATING)
   protected double minRating = Double.MAX_VALUE;

   @SerializedName(N_MAX)
   protected int numberOfIterations = Integer.MAX_VALUE;

   @SerializedName(RANDOM_SEED)
   protected long randomSeed = Long.MAX_VALUE;


   /**
    * Creates a new {@link MatrixFactorizationConfiguration} with the default values.
    */
   public MatrixFactorizationConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   public String toString() {
      StringBuilder output = new StringBuilder(super.toString());
      output.append(StringUtils.SINGLE_WHITESPACE);
      output.append(String.format(PARAMETER_VALUE_PAIR, K, numberOfHiddenFeatures));
      output.append(String.format(PARAMETER_VALUE_PAIR, STEP_SIZE, stepSize));
      output.append(String.format(PARAMETER_VALUE_PAIR, REGULARIZATION_FACTOR, regularizationFactor));
      output.append(String.format(PARAMETER_VALUE_PAIR, MIN_RATING, minRating));
      output.append(String.format(PARAMETER_VALUE_PAIR, N_MAX, numberOfIterations));
      output.append(String.format(LAST_PARAMETER_VALUE_PAIR, RANDOM_SEED, randomSeed));
      return output.toString();
   }


   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (!super.equals(o))
         return false;
      if (o instanceof MatrixFactorizationConfiguration) {
         MatrixFactorizationConfiguration config = (MatrixFactorizationConfiguration) o;
         boolean result = config.getRandomSeed() == randomSeed && config.getNumberOfIterations() == numberOfIterations
               && config.getNumberOfHiddenFeatures() == numberOfHiddenFeatures;
         result = result && Double.compare(config.getStepSize(), stepSize) == 0
               && Double.compare(config.getRegularizationFactor(), regularizationFactor) == 0
               && Double.compare(config.getMinRating(), minRating) == 0;
         return result;
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode = 31 * hashCode + (Long.hashCode(randomSeed));
      hashCode = 31 * hashCode + (Integer.hashCode(numberOfHiddenFeatures));
      hashCode = 31 * hashCode + (Integer.hashCode(numberOfIterations));
      hashCode = 31 * hashCode + (Double.hashCode(stepSize));
      hashCode = 31 * hashCode + (Double.hashCode(regularizationFactor));
      hashCode = 31 * hashCode + (Double.hashCode(minRating));
      return hashCode;
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (numberOfHiddenFeatures <= 0) {
         throw new ParameterValidationFailedException(K_NOT_A_POSITIVE_VALUE);
      }
      if (stepSize <= 0) {
         throw new ParameterValidationFailedException(String.format(NOT_A_VALID_VALUE, STEP_SIZE, 0));
      }
      if (regularizationFactor < 0) {
         throw new ParameterValidationFailedException(String.format(NOT_A_VALID_VALUE, REGULARIZATION_FACTOR, 0));
      }
      if (minRating < 0) {
         throw new ParameterValidationFailedException(String.format(NOT_A_VALID_VALUE, MIN_RATING, 0));
      }
      if (numberOfIterations < 1) {
         throw new ParameterValidationFailedException(String.format(NOT_A_VALID_VALUE, N_MAX, 1));
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      MatrixFactorizationConfiguration castedConfiguration = (MatrixFactorizationConfiguration) configuration;
      if (castedConfiguration.numberOfHiddenFeatures != Integer.MAX_VALUE)
         this.numberOfHiddenFeatures = castedConfiguration.numberOfHiddenFeatures;
      if (castedConfiguration.stepSize < Double.MAX_VALUE)
         this.stepSize = castedConfiguration.stepSize;
      if (castedConfiguration.regularizationFactor < Double.MAX_VALUE)
         this.regularizationFactor = castedConfiguration.regularizationFactor;
      if (castedConfiguration.minRating < Double.MAX_VALUE)
         this.minRating = castedConfiguration.minRating;
      if (castedConfiguration.randomSeed != Long.MAX_VALUE)
         this.randomSeed = castedConfiguration.randomSeed;
      if (castedConfiguration.numberOfIterations != Integer.MAX_VALUE)
         this.numberOfIterations = castedConfiguration.numberOfIterations;
   }


   /**
    * Returns the desired number of hidden features for the matrix factorization. *
    * 
    * @return the number of hidden features
    */
   public int getNumberOfHiddenFeatures() {
      return numberOfHiddenFeatures;
   }


   /**
    * Sets the desired number of hidden features for the matrix factorization. *
    * 
    * @param numberOfHiddenFeatures the number of hidden features to set
    */
   public void setNumberOfHiddenFeatures(int numberOfHiddenFeatures) {
      this.numberOfHiddenFeatures = numberOfHiddenFeatures;
   }


   /**
    * Returns the initial step size for the gradient descent algorithm used for the matrix
    * factorization.
    * 
    * @return the step size for the gradient descent
    */
   public double getStepSize() {
      return stepSize;
   }


   /**
    * Sets the initial step size for the gradient descent algorithm used for the matrix
    * factorization.
    * 
    * @param stepSize the step size to set
    */
   public void setStepSize(double stepSize) {
      this.stepSize = stepSize;
   }


   /**
    * Returns the minimal value of a rating.
    * 
    * @return the minimal rating possible
    */
   public double getMinRating() {
      return minRating;
   }


   /**
    * Sets the minimal value of a rating.
    * 
    * @param minRating the minimal rating possible
    */
   public void setMinRating(double minRating) {
      this.minRating = minRating;
   }


   /**
    * Returns the regularization factor for the algorithm.
    * 
    * @return the regularizationFactor used by the algorithm
    */
   public double getRegularizationFactor() {
      return regularizationFactor;
   }


   /**
    * Sets the regularization factor for the algorithm.
    * 
    * @param regularizationFactor the regularizationFactor to set
    */
   public void setRegularizationFactor(double regularizationFactor) {
      this.regularizationFactor = regularizationFactor;
   }


   /**
    * Returns the seed value for the {@link RandomGenerator} used in the algorithm.
    * 
    * @return the randomSeed of the random generator
    */
   public long getRandomSeed() {
      return randomSeed;
   }


   /**
    * Sets the seed value for the {@link RandomGenerator} used in the algorithm.
    * 
    * @param randomSeed the randomSeed to set
    */
   public void setRandomSeed(long randomSeed) {
      this.randomSeed = randomSeed;
   }


   /**
    * Return the maximum number of iterations for gradient descent.
    * 
    * @return the maximum number of iterations
    */
   public int getNumberOfIterations() {
      return numberOfIterations;
   }


   /**
    * Set the maximum number of iterations for gradient descent.
    * 
    * @param numberOfIterations the numberOfIterations to set
    */
   public void setNumberOfIterations(int numberOfIterations) {
      this.numberOfIterations = numberOfIterations;
   }

}
