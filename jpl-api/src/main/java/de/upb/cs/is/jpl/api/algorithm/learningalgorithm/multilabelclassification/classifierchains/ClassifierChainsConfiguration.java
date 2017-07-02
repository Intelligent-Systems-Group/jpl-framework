package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.classifierchains;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the algorithm configuration for the
 * {@link ClassifierChainsLearningAlgorithm}. Its only parameter is the permutation of the labels.
 * The permutation needs to be given in the form of a list. Consider the case of 3 labels and the
 * following list: [0,2,1]. This list corresponds to the permutation of pi(0) = 0, pi(1) = 2 and
 * pi(2) = 1. If you want no permutation at all, i.e. the base learner should be learned in the
 * input order of the labels, you can leave the permutation empty.
 * 
 * Note that when using the {@link ClassifierChainsLearningAlgorithm} in an evaluation with multiple
 * datasets, it is not possible to configure the permutation except for using the default
 * permutation. This is the case, as you can only give one configuration for one evaluation run.
 * 
 * @author Alexander Hetzer
 *
 */
public class ClassifierChainsConfiguration extends AAlgorithmConfigurationWithBaseLearner {

   private static final String ERROR_INCOMPLETE_PERMUTATION = "An incomplete permutation ist not allowed. Make sure to make the lowest number in the permutation 0.";
   private static final String ERROR_INVALID_NEGATIVE_ENTRIES_IN_PERMUTATION = "Negative entries are not allowed in the permutation.";
   private static final String ERROR_PERMUTATION_MUST_NOT_BE_NULL = "Permutation must not be null.";

   private static final String PARAMETER_NAME_PERMUTATION = "permutation";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH
         + "multilabelclassification" + StringUtils.FORWARD_SLASH + "classifier_chains";

   @SerializedName(PARAMETER_NAME_PERMUTATION)
   private List<Integer> permutation = null;

   private boolean intializedPermutationFromDefault = false;


   /**
    * Creates a new {@link ClassifierChainsConfiguration}.
    */
   public ClassifierChainsConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      super.validateParameters();
      assertBaseLearnerIsClassifier();
      assertPermutationIsValid();
   }


   /**
    * Asserts that the permutation is valid. If this is not the case a
    * {@link ParameterValidationFailedException} is thrown.
    * 
    * @throws ParameterValidationFailedException if the permutation is invalid
    */
   private void assertPermutationIsValid() throws ParameterValidationFailedException {
      assertPermutationIsNotNull();
      assertPermutationIsComplete();
   }


   /**
    * Asserts that the permutation is complete. If this is not the case a
    * {@link ParameterValidationFailedException} is thrown.
    * 
    * @throws ParameterValidationFailedException if the permutation is incomplete
    */
   private void assertPermutationIsComplete() throws ParameterValidationFailedException {
      if (!permutation.isEmpty()) {
         int maximumLabelIdInPermutation = 0;
         for (Integer i : permutation) {
            if (i > maximumLabelIdInPermutation) {
               maximumLabelIdInPermutation = i;
            }
            if (i < 0) {
               throw new ParameterValidationFailedException(ERROR_INVALID_NEGATIVE_ENTRIES_IN_PERMUTATION);
            }
         }
         List<Integer> copiedPermutation = new ArrayList<>(permutation);
         for (int i = 0; i < maximumLabelIdInPermutation; i++) {
            boolean wasRemovalSuccessful = copiedPermutation.remove(new Integer(i));
            if (!wasRemovalSuccessful) {
               throw new ParameterValidationFailedException(ERROR_INCOMPLETE_PERMUTATION);
            }
         }
      }
   }


   /**
    * Asserts that the permutation is not {@code null}. If this is not the case a
    * {@link ParameterValidationFailedException} is thrown.
    * 
    * @throws ParameterValidationFailedException if the permutation is {@code null}
    */
   private void assertPermutationIsNotNull() throws ParameterValidationFailedException {
      if (permutation == null) {
         throw new ParameterValidationFailedException(ERROR_PERMUTATION_MUST_NOT_BE_NULL);
      }
   }


   /**
    * Returns {@code true} if the permutation is empty, {@code false} otherwise.
    * 
    * @return {@code true} if the permutation is empty, {@code false} otherwise
    */
   public boolean hasDefaultPermutation() {
      if (permutation.isEmpty() || intializedPermutationFromDefault) {
         return true;
      }
      return false;
   }


   /**
    * Initializes the permutation with with the default permutation, i.e. the input order of the
    * labels.
    * 
    * @param numberOfLabels the number of labels used
    */
   public void initializeDefaultPermutation(int numberOfLabels) {
      setPermutationIntializedFromDefault();
      permutation = new ArrayList<>();
      for (int i = 0; i < numberOfLabels; i++) {
         permutation.add(i);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      super.copyValues(configuration);
      ClassifierChainsConfiguration classifierChainsConfiguration = (ClassifierChainsConfiguration) configuration;
      if (classifierChainsConfiguration.permutation != null) {
         setPermutation(classifierChainsConfiguration.permutation);
      }
   }


   /**
    * Returns the current permutation.
    * 
    * @return the current permutation
    */
   public List<Integer> getPermutation() {
      return permutation;
   }


   /**
    * Sets the permutation to the given permutation. Note that a deep copy of the list is done.
    * 
    * @param permutation the new permutation to use
    */
   public void setPermutation(List<Integer> permutation) {
      this.permutation = CollectionsUtils.getDeepCopyOf(permutation);
   }


   /**
    * Returns {@code true} if the permutation was initialized from the default value.
    * 
    * @return {@code true} if the permutation was initialized from the default value, otherwise
    *         {@code false}
    */
   public boolean isPermutationIntializedFromDefault() {
      return intializedPermutationFromDefault;
   }


   /**
    * Sets the permutation initialized from default.
    */
   public void setPermutationIntializedFromDefault() {
      this.intializedPermutationFromDefault = true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + (intializedPermutationFromDefault ? 1231 : 1237);
      result = prime * result + ((permutation == null) ? 0 : permutation.hashCode());
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
      ClassifierChainsConfiguration other = (ClassifierChainsConfiguration) obj;
      if (intializedPermutationFromDefault != other.intializedPermutationFromDefault)
         return false;
      if (permutation == null) {
         if (other.permutation != null)
            return false;
      } else if (!permutation.equals(other.permutation))
         return false;
      return true;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_PERMUTATION + StringUtils.COLON + permutation.toString() + StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND
            + super.toString();
   }


}
