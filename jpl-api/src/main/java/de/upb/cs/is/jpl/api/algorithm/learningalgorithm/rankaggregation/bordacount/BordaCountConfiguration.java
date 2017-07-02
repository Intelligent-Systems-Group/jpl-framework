package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.bordacount;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This configuration is used to store all necessary parameters given by the user for the
 * {@link BordaCountLearningAlgorithm}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class BordaCountConfiguration extends AAlgorithmConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "learningalgorithm" + StringUtils.FORWARD_SLASH + "rankaggregation"
         + StringUtils.FORWARD_SLASH + "borda_count";

   private static final String IS_RATING_WITH_MISSING_LABELS = "is_rating_with_missed_labels";
   @SerializedName(IS_RATING_WITH_MISSING_LABELS)
   private Boolean isRatingWithMissedLabels = null;


   /**
    * Creates a new configuration for the {@link BordaCountLearningAlgorithm}
    */
   public BordaCountConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      // The isRatingWithMissedLables can't be checked. It can only be true or false.
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      BordaCountConfiguration bordaCountConfiguration = (BordaCountConfiguration) configuration;
      if (bordaCountConfiguration.isRatingWithMissedLabels != null) {
         this.isRatingWithMissedLabels = bordaCountConfiguration.isRatingWithMissedLabels;
      }
   }


   /**
    * Returns whether the Borda Count for label scores should also add the average score for missed
    * labels.
    *
    * @return the isRatingWithMissedLabels
    */
   public boolean isRatingWithMissedLabels() {
      return isRatingWithMissedLabels;
   }


   /**
    * Sets the rating with missed labels.
    *
    * @param isRatingWithMissedLabels the isRatingWithMissedLabels to set
    */
   public void setRatingWithMissedLabels(boolean isRatingWithMissedLabels) {
      this.isRatingWithMissedLabels = isRatingWithMissedLabels;
   }


   @Override
   public String toString() {
      StringBuilder toStringBuilder = new StringBuilder();
      if (!isRatingWithMissedLabels) {
         toStringBuilder.append(IS_RATING_WITH_MISSING_LABELS);
         toStringBuilder.append(StringUtils.COLON_WITH_SINGLE_WHITESPACE_BEHIND);
         toStringBuilder.append(isRatingWithMissedLabels);
      }
      return toStringBuilder.toString();
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + (isRatingWithMissedLabels ? 1231 : 1237);
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof BordaCountConfiguration))
         return false;
      BordaCountConfiguration other = (BordaCountConfiguration) obj;
      if (isRatingWithMissedLabels != other.isRatingWithMissedLabels)
         return false;
      return true;
   }

}