package de.upb.cs.is.jpl.api.algorithm.learningalgorithm;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.IBaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This is the base class for algorithm configuration classes which uses the base learner in the
 * configuration. The path to the default configuration file location is set in the constructor of
 * this class. You should always call up to your superclass when implementing these methods:
 * <ul>
 * <li>validateParameters()</li>
 * <li>copyValues(IJsonConfiguration configuration)</li>
 * </ul>
 * 
 * @author Pritha Gupta
 *
 */
public class AAlgorithmConfigurationWithBaseLearner extends AAlgorithmConfiguration {

   private static final String ERROR_BASE_LEARNER_IS_NULL = "Base learner is null.";
   private static final String ERROR_BASE_LEARNER_IS_NOT_A_CLASSIFIER = "Base learner '%s' is not a classifier, but has to be one.";
   private static final String ERROR_BASE_LEARNER_IS_NOT_A_REGRESSION = "Base learner '%s' is not of type regreesion, but has to be one.";
   private static final String ERROR_BASE_LEARNER_IS_NOT_RETURNING_PROBABILITY = "Base learner '%s' is not returning probabilities, but should so.";

   private static final String ERROR_CANNOT_CREATE_BASE_LEARNER = "Base Learner cannot be created for the identifier: %s";
   private static final String ERROR_INVALID_BASE_LEARNER_IDENTIFIER = "Invalid base learner identifier: %s";

   protected static final String PARAMETER_NAME_BASE_LEARNER = "base_learner";

   @SerializedName(PARAMETER_NAME_BASE_LEARNER)
   protected BaselearnerDefinition baseLearnerElement = null;

   protected transient IBaselearnerAlgorithm baseLearnerAlgorithm = null;
   protected transient EBaseLearner ebaseLearner = null;


   /**
    * Creates an abstract algorithm configuration for the algorithms that uses the base learner.
    *
    * @param defaultConfigurationFileName the name of the default configuration file
    */
   public AAlgorithmConfigurationWithBaseLearner(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      setBaseLearnerWithOverridenParameters();
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      AAlgorithmConfigurationWithBaseLearner algorithmConfigurationWithBaseLearner = (AAlgorithmConfigurationWithBaseLearner) configuration;
      if (algorithmConfigurationWithBaseLearner.baseLearnerElement != null) {
         this.baseLearnerElement = algorithmConfigurationWithBaseLearner.baseLearnerElement;
      }
   }


   /**
    * Creates a base learner with the overridden parameters specified in the configuration.
    * 
    * @throws ParameterValidationFailedException if the base learner parameters or the base learner
    *            is not identified
    */
   private void setBaseLearnerWithOverridenParameters() throws ParameterValidationFailedException {
      ebaseLearner = EBaseLearner.getEBaseLearnerByIdentifier(baseLearnerElement.getName());
      if (ebaseLearner == null) {
         throw new ParameterValidationFailedException(String.format(ERROR_INVALID_BASE_LEARNER_IDENTIFIER, baseLearnerElement.getName()));
      } else {
         baseLearnerAlgorithm = ebaseLearner.createBaseLearner();
         if (baseLearnerAlgorithm == null) {
            throw new ParameterValidationFailedException(String.format(ERROR_CANNOT_CREATE_BASE_LEARNER, baseLearnerElement.getName()));
         }
         AAlgorithmConfiguration configuration = baseLearnerAlgorithm.getAlgorithmConfiguration();
         configuration.overrideConfiguration(baseLearnerElement.getParameters());
         baseLearnerAlgorithm.setAlgorithmConfiguration(configuration);
      }
   }


   /**
    * Asserts that the base learner is not {@code null} and that it is a classification learning
    * algorithm. If this is not the case, a {@link ParameterValidationFailedException} is thrown.
    * 
    * @throws ParameterValidationFailedException if the base learner is either {@code null} or if it
    *            is a regression learning algorithm
    */
   protected void assertBaseLearnerIsClassifier() throws ParameterValidationFailedException {
      assertBaseLearnerIsNotNull();
      if (!ebaseLearner.isClassifier()) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_BASE_LEARNER_IS_NOT_A_CLASSIFIER, ebaseLearner.getBaseLearnerIdentifier()));
      }
   }


   /**
    * Asserts that the base learner is not {@code null} and that it is a regression learning
    * algorithm. If this is not the case, a {@link ParameterValidationFailedException} is thrown.
    * 
    * @throws ParameterValidationFailedException if the base learner is either {@code null} or if it
    *            is a classification learning algorithm
    */
   protected void assertBaseLearnerIsRegressionLearningAlgorithm() throws ParameterValidationFailedException {
      assertBaseLearnerIsNotNull();
      if (ebaseLearner.isClassifier()) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_BASE_LEARNER_IS_NOT_A_REGRESSION, ebaseLearner.getBaseLearnerIdentifier()));
      }
   }


   /**
    * Asserts that the base learner is not {@code null} and that it returns a probability. If this
    * is not the case, a {@link ParameterValidationFailedException} is thrown.
    * 
    * @throws ParameterValidationFailedException if the base learner is either {@code null} or if it
    *            is returning a probability
    */
   protected void assertBaseLearnerReturnsProbability() throws ParameterValidationFailedException {
      assertBaseLearnerIsNotNull();
      if (!ebaseLearner.isReturningProbability()) {
         throw new ParameterValidationFailedException(
               String.format(ERROR_BASE_LEARNER_IS_NOT_RETURNING_PROBABILITY, ebaseLearner.getBaseLearnerIdentifier()));
      }
   }


   /**
    * Asserts that the base learner is not {@code null}. If this is the case, a
    * {@link ParameterValidationFailedException} is thrown.
    * 
    * @throws ParameterValidationFailedException if the base learner is {@code null}
    */
   protected void assertBaseLearnerIsNotNull() throws ParameterValidationFailedException {
      if (ebaseLearner == null || baseLearnerAlgorithm == null) {
         throw new ParameterValidationFailedException(ERROR_BASE_LEARNER_IS_NULL);
      }
   }


   /**
    * Returns the {@link EBaseLearner} corresponding to the base leaner identifier provided in the
    * configuration.
    * 
    * @return the {@link EBaseLearner} for the corresponding configuration
    */
   public EBaseLearner getEbaseLearner() {
      return ebaseLearner;
   }


   /**
    * Returns the {@link IBaselearnerAlgorithm} with the overridden parameters specified in the
    * configuration for the algorithm.
    * 
    * @return the {@link IBaselearnerAlgorithm} for the corresponding configuration
    */
   public IBaselearnerAlgorithm getBaseLearnerAlgorithm() {
      return baseLearnerAlgorithm;
   }


   /**
    * Returns the {@link BaselearnerDefinition} stored in the configuration.
    * 
    * @return the baseLearnerElement
    */
   public BaselearnerDefinition getBaseLearnerElement() {
      return baseLearnerElement;
   }


   /**
    * Sets the base learner algorithm of this configuration to the given one.
    * 
    * @param baseLearnerAlgorithm the base learner to set
    */
   public void setBaseLearnerAlgorithm(IBaselearnerAlgorithm baseLearnerAlgorithm) {
      this.baseLearnerAlgorithm = baseLearnerAlgorithm;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((baseLearnerAlgorithm == null) ? 0 : baseLearnerAlgorithm.hashCode());
      result = prime * result + ((baseLearnerElement == null) ? 0 : baseLearnerElement.hashCode());
      result = prime * result + ((ebaseLearner == null) ? 0 : ebaseLearner.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      AAlgorithmConfigurationWithBaseLearner other = (AAlgorithmConfigurationWithBaseLearner) obj;
      if (baseLearnerAlgorithm == null) {
         if (other.baseLearnerAlgorithm != null)
            return false;
      } else if (!baseLearnerAlgorithm.equals(other.baseLearnerAlgorithm))
         return false;
      if (baseLearnerElement == null) {
         if (other.baseLearnerElement != null)
            return false;
      } else if (!baseLearnerElement.equals(other.baseLearnerElement))
         return false;
      if (ebaseLearner != other.ebaseLearner)
         return false;
      return true;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_BASE_LEARNER + StringUtils.COLON + baseLearnerAlgorithm.toString() + StringUtils.SINGLE_WHITESPACE
            + StringUtils.ROUND_BRACKET_OPEN + baseLearnerAlgorithm.getAlgorithmConfiguration().toString()
            + StringUtils.ROUND_BRACKET_CLOSE;
   }


}
