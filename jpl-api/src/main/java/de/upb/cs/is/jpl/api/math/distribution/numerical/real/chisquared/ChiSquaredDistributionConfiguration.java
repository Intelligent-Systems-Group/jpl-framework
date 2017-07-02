package de.upb.cs.is.jpl.api.math.distribution.numerical.real.chisquared;


import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.math.distribution.ADistributionConfiguration;
import de.upb.cs.is.jpl.api.math.distribution.numerical.real.ARealDistributionConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class represents the {@link ADistributionConfiguration} for the
 * {@link ChiSquaredDistribution}. It should be used to define the parameters of the associated
 * {@link ChiSquaredDistribution}.
 * 
 * @author Alexander Hetzer
 *
 */
public class ChiSquaredDistributionConfiguration extends ARealDistributionConfiguration {

   private static final String DEFAULT_CONFIGURATION_FILENAME = "chi_squared";

   private static final String ERROR_NON_POSITIVE_AMOUNT_OF_DEGREES_OF_FREEDOM = "The amount of degrees of freedom has to be positive!";

   private static final String PARAMETER_NAME_DEGREES_OF_FREEDOM = "degrees_of_freedom";

   @SerializedName(PARAMETER_NAME_DEGREES_OF_FREEDOM)
   private int degreesOfFreedom = Integer.MAX_VALUE;


   /**
    * Creates a new {@link ChiSquaredDistributionConfiguration} whose parameters are initialized
    * with default values.
    */
   public ChiSquaredDistributionConfiguration() {
      super(DEFAULT_CONFIGURATION_FILENAME);
   }


   /**
    * Creates a new {@link ChiSquaredDistributionConfiguration} with the given amount of degrees of
    * freedom.
    * 
    * @param degreesOfFreedom the degrees of freedom to use for initialization. Has to be positive
    *           (&gt;0).
    * @throws ParameterValidationFailedException if the given amount of degrees of freedom is not
    *            positive (&lt;=0)
    */
   public ChiSquaredDistributionConfiguration(int degreesOfFreedom) throws ParameterValidationFailedException {
      this();
      this.degreesOfFreedom = degreesOfFreedom;
      validateParameters();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (degreesOfFreedom <= 0) {
         throw new ParameterValidationFailedException(ERROR_NON_POSITIVE_AMOUNT_OF_DEGREES_OF_FREEDOM);
      }
   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      ChiSquaredDistributionConfiguration castedConfiguration = (ChiSquaredDistributionConfiguration) configuration;
      if (castedConfiguration.degreesOfFreedom < Double.MAX_VALUE) {
         this.degreesOfFreedom = castedConfiguration.degreesOfFreedom;
      }
   }


   /**
    * Returns the amount of degrees of freedoms.
    * 
    * @return the amount of degrees of freedoms
    */
   public int getDegreesOfFreedom() {
      return degreesOfFreedom;
   }


   /**
    * Sets the amount of degrees of freedom to the given value.
    * 
    * @param degreesOfFreedom the new value for the amount of degrees of freedom
    */
   public void setDegreesOfFreedom(int degreesOfFreedom) {
      this.degreesOfFreedom = degreesOfFreedom;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(degreesOfFreedom);
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
      ChiSquaredDistributionConfiguration other = (ChiSquaredDistributionConfiguration) obj;
      if (Double.doubleToLongBits(degreesOfFreedom) != Double.doubleToLongBits(other.degreesOfFreedom))
         return false;
      return true;
   }


   @Override
   public String toString() {
      return PARAMETER_NAME_DEGREES_OF_FREEDOM + StringUtils.COLON + degreesOfFreedom;
   }


}
