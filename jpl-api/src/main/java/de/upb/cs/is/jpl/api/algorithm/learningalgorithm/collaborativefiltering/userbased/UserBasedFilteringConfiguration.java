package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.userbased;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * The Configuration for the {@link UserBasedFilteringLearningAlgorithm} algorithm.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class UserBasedFilteringConfiguration extends AAlgorithmConfiguration {


   /**
    * The key strings to indicate pearson correlation function.
    */
   public static final String PEARSON = "pearson";

   /**
    * The key strings to indicate cosine correlation function.
    */
   public static final String COSINE = "cosine";


   private static final String CORRELATION = "correlation";

   private static final String MIN_DOUBLE = "min_double";

   private static final String K = "k";

   private static final String LAST_PARAMETER_VALUE_PAIR = "%s: %s";
   private static final String PARAMETER_VALUE_PAIR = "%s: %s, ";
   private static final String THE_POSSIBLE_VALUES_FOR_THIS_PARAMETER_ARE_S = "The possible values for this parameter are: %s, %s";
   private static final String THE_MINIMAL_VALUE_FOR_DOUBLES_MUST_BE_AT_LEAST_ZERO = "The minimal value for doubles must be at least zero.";
   private static final String NOT_A_POSITIVE_VALUE = "The parameter k (number of neighbors checked) is not a positive value!"
         + " You need to consider at least one neighbor for neighbor-based learning.";
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "collaborativefiltering"
         + StringUtils.FORWARD_SLASH + "user_based_filtering_learning_algorithm";


   @SerializedName(K)
   protected int numberOfNeighbors = Integer.MAX_VALUE;

   @SerializedName(MIN_DOUBLE)
   protected double minDouble = 0.0;

   @SerializedName(CORRELATION)
   protected String correlationType = StringUtils.EMPTY_STRING;


   /**
    * 
    * Creates a new {@link UserBasedFilteringConfiguration} with the default configuration loaded.
    * 
    */
   public UserBasedFilteringConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   public String toString() {
      StringBuilder output = new StringBuilder(super.toString());
      output.append(StringUtils.SINGLE_WHITESPACE);
      output.append(String.format(PARAMETER_VALUE_PAIR, K, numberOfNeighbors));
      output.append(String.format(PARAMETER_VALUE_PAIR, MIN_DOUBLE, minDouble));
      output.append(String.format(LAST_PARAMETER_VALUE_PAIR, CORRELATION, correlationType));
      return output.toString();
   }


   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (!super.equals(o))
         return false;
      if (o instanceof UserBasedFilteringConfiguration) {
         UserBasedFilteringConfiguration config = (UserBasedFilteringConfiguration) o;
         boolean result = config.getCorrelationType().equals(correlationType);
         result = result && config.getNumberOfNeighbors() == numberOfNeighbors;
         result = result && Double.compare(config.getMinDouble(), minDouble) == 0;
         return result;
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode = 31 * hashCode + (correlationType.hashCode());
      hashCode = 31 * hashCode + (Integer.hashCode(numberOfNeighbors));
      hashCode = 31 * hashCode + (Double.hashCode(minDouble));
      return hashCode;
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (numberOfNeighbors <= 0) {
         throw new ParameterValidationFailedException(NOT_A_POSITIVE_VALUE);
      }
      if (minDouble < 0) {
         throw new ParameterValidationFailedException(THE_MINIMAL_VALUE_FOR_DOUBLES_MUST_BE_AT_LEAST_ZERO);
      }
      if (!correlationType.equals(COSINE) && !correlationType.equals(PEARSON)) {
         throw new ParameterValidationFailedException(String.format(THE_POSSIBLE_VALUES_FOR_THIS_PARAMETER_ARE_S, COSINE, PEARSON));
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      UserBasedFilteringConfiguration castedConfiguration = (UserBasedFilteringConfiguration) configuration;
      if (castedConfiguration.numberOfNeighbors < Integer.MAX_VALUE) {
         this.numberOfNeighbors = castedConfiguration.numberOfNeighbors;
      }
      if (castedConfiguration.minDouble > 0) {
         this.minDouble = castedConfiguration.minDouble;
      }
      if (!castedConfiguration.correlationType.isEmpty()) {
         this.correlationType = castedConfiguration.correlationType;
      }
   }


   /**
    * Returns the minimal double value that is considered != 0.
    * 
    * @return the minDouble value
    */
   public double getMinDouble() {
      return minDouble;
   }


   /**
    * Returns the selected number of neighbors.
    * 
    * @return the currently selected value for the number of neighbors (k)
    */
   public int getNumberOfNeighbors() {
      return numberOfNeighbors;
   }


   /**
    * Sets the correlation type.
    * 
    * @return the correlationType
    */
   public String getCorrelationType() {
      return correlationType;
   }


   /**
    * Sets the number of neighbors which are searched.
    * 
    * @param numberOfNeighbors the numberOfNeighbors to set
    */
   public void setNumberOfNeighbors(int numberOfNeighbors) {
      this.numberOfNeighbors = numberOfNeighbors;
   }


   /**
    * Sets the minimal value which is considered a valid rating.
    * 
    * @param minDouble the minDouble to set
    */
   public void setMinDouble(double minDouble) {
      this.minDouble = minDouble;
   }


   /**
    * Sets the type of correlation used in the comparison of the ratings vectors.
    * 
    * @param correlationType the correlationType to set
    */
   public void setCorrelationType(String correlationType) {
      this.correlationType = correlationType;
   }


}
