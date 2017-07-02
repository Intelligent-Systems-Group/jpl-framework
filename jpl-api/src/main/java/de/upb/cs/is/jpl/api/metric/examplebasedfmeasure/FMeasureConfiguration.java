package de.upb.cs.is.jpl.api.metric.examplebasedfmeasure;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.metric.AMetricConfiguration;
import de.upb.cs.is.jpl.api.metric.labelbasedfmeasure.MacroLabelBasedFMeasure;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class is the metric configuration for both the {@link MacroLabelBasedFMeasure} and the
 * {@link ExampleBasedFMeasure}. It stores the beta parameter of the f-measure.
 * 
 * @author Alexander Hetzer
 *
 */
public class FMeasureConfiguration extends AMetricConfiguration {

   private static final String ERROR_NO_NEGATIVE_BETA_VALUES = "The parameter beta can only take positive values.";

   private static final String PARAMETER_NAME_BETA = "beta";

   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "f_measure";

   @SerializedName(PARAMETER_NAME_BETA)
   private double beta = Double.MAX_VALUE;


   /**
    * Creates a new {@link FMeasureConfiguration}.
    */
   public FMeasureConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (beta < 0) {
         throw new ParameterValidationFailedException(ERROR_NO_NEGATIVE_BETA_VALUES);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      FMeasureConfiguration fMeasureConfiguration = (FMeasureConfiguration) configuration;
      if (fMeasureConfiguration.beta < Double.MAX_VALUE) {
         this.beta = fMeasureConfiguration.beta;
      }
   }


   /**
    * Returns the value of the f-measure's beta parameter.
    * 
    * @return the value of the f-measure's beta parameter
    */
   public double getBeta() {
      return beta;
   }


   /**
    * Sets the value of the f-measure's beta parameter to the given value.
    * 
    * @param beta the new value of the f-measure's beta parameter
    */
   public void setBeta(double beta) {
      this.beta = beta;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(beta);
      result = prime * result + (int) (temp ^ (temp >>> 32));
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
      FMeasureConfiguration other = (FMeasureConfiguration) obj;
      if (Double.doubleToLongBits(beta) != Double.doubleToLongBits(other.beta))
         return false;
      return true;
   }


   @Override
   public String toString() {
      return DEFAULT_CONFIGURATION_FILE_NAME + StringUtils.ROUND_BRACKET_OPEN + StringUtils.SINGLE_WHITESPACE + PARAMETER_NAME_BETA
            + StringUtils.COLON_WITH_SINGLE_WHITESPACE_BEHIND + beta + StringUtils.ROUND_BRACKET_CLOSE;
   }


}
