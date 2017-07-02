package de.upb.cs.is.jpl.api.algorithm.baselearner.regression.linear;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * The configuration contains all parameters for an implementation of {@link LinearRegression}.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class LinearRegressionConfiguration extends AAlgorithmConfiguration {
   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "baselearner" + StringUtils.FORWARD_SLASH + "regression"
         + StringUtils.FORWARD_SLASH + "linear" + StringUtils.FORWARD_SLASH + "linear_regression";


   /**
    * Creates a default configuration for {@link LinearRegression}.
    */
   public LinearRegressionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      // No parameter are used

   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      // No parameter are used

   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof LinearRegressionConfiguration))
         return false;

      return true;
   }

}
